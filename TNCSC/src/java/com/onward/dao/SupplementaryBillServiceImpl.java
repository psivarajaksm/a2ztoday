/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.AmountInWords;
import com.onward.common.DateParser;
import com.onward.persistence.payroll.*;
import com.onward.reports.supplementary.SupplementaryAbstractReport;
import com.onward.reports.supplementary.SupplementaryAcquitanceReport;
import com.onward.reports.supplementary.SupplementaryDAAbstractReport;
import com.onward.reports.supplementary.SupplementaryDAAcquittanceBank;
import com.onward.reports.supplementary.SupplementaryDAAcquittanceCheque;
import com.onward.reports.supplementary.SupplementaryDABillReport;
import com.onward.reports.supplementary.SupplementaryDADBFReport;
import com.onward.reports.supplementary.SupplementaryDALedgerReport;
import com.onward.reports.supplementary.SupplementaryDeductionAllReport;
import com.onward.reports.supplementary.SupplementaryEPFDBFReport;
import com.onward.reports.supplementary.SupplementaryEPFformDBFReport;
import com.onward.reports.supplementary.SupplementaryEPFformReport;
import com.onward.reports.supplementary.SupplementaryIncrementArrear;
import com.onward.reports.supplementary.SupplementaryIncrementArrearAbstract;
import com.onward.reports.supplementary.SupplementaryIncrementArrearAcquitance;
import com.onward.reports.supplementary.SupplementaryLICScheduleReport;
import com.onward.reports.supplementary.SupplementaryPayslipReport;
import com.onward.valueobjects.DaArrearModel;
import com.onward.valueobjects.DaArrearSubModel;
import com.onward.valueobjects.IncrementArrearModel;
import com.onward.valueobjects.IncrementArrearsubModel;
import com.onward.valueobjects.PaySlipModel;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.onward.reports.regular.*;

/**
 *
 * @author root
 */
public class SupplementaryBillServiceImpl extends OnwardAction implements SupplementaryBillService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
        //empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
            String region = empmasterObj.getRegion();
            if ("RET".equalsIgnoreCase(region)) {
                resultMap.put("designationname", "Retired Employee");
            } else {
                resultMap.put("designationname", getDesignationMater(session, empmasterObj.getDesignation()).getDesignation());
            }
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");

        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveSupplementaryBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String orderno, String billno) {
        //System.out.println("Modify Bill" + billno);
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Map saveMap = new HashMap();
        Employeemaster empmasterObj = null;
        Supplementatypaybill supplementatypaybillObj;
        String subSalaryStructureId = "";
        String supPayBillId = "";
        String Datestring = asondate;
        SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm1.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal1 = fm1.getCalendar();
        int month = cal1.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int year = cal1.get(Calendar.YEAR);
        String qry = "select count(*) as cnt from employeeprovidentfundothers where accregion='" + LoggedInRegion + "' and month =" + month + " and year=" + year + " and supprocessid is not null ";
        int cnt = 0;
        SQLQuery epfprepared = session.createSQLQuery(qry);
        if (epfprepared.list().size() > 0) {
            Object obj = epfprepared.list().get(0);
            cnt = Integer.parseInt(obj.toString());
        }
        try {
            if (cnt == 0) {
                saveMap = (Map) request.getSession().getAttribute("addedMap");
                if (saveMap != null) {
                    empmasterObj = getEmployeeDetails(session, epfno, LoggedInRegion);

                    if (billno.trim().length() == 0) {
                        supplementatypaybillObj = new Supplementatypaybill();
                        supPayBillId = getSupplementatypaybillid(session, LoggedInRegion);
                        supplementatypaybillObj.setId(supPayBillId);
                        supplementatypaybillObj.setAccregion(LoggedInRegion);
                        supplementatypaybillObj.setDate(postgresDate(asondate));
                        supplementatypaybillObj.setEmployeemaster(empmasterObj);
                        supplementatypaybillObj.setType("SUPLEMENTARYBILL");
                        supplementatypaybillObj.setSection(empmasterObj.getSection());
                        supplementatypaybillObj.setSubsection(empmasterObj.getSubsection());
                        supplementatypaybillObj.setPaymentmode(empmasterObj.getPaymentmode());
                        supplementatypaybillObj.setEmployeecategory(empmasterObj.getCategory());
                        supplementatypaybillObj.setDesignation(empmasterObj.getDesignation());
                        supplementatypaybillObj.setAccregion(LoggedInRegion);
                        supplementatypaybillObj.setCancelled(Boolean.FALSE);
                        supplementatypaybillObj.setCreatedby(LoggedInUser);
                        supplementatypaybillObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(supplementatypaybillObj);
                        transaction.commit();

                    } else {

                        supplementatypaybillObj = new Supplementatypaybill();
                        supplementatypaybillObj.setId(billno);
                        supPayBillId = billno;
                        supplementatypaybillObj.setAccregion(LoggedInRegion);
                        supplementatypaybillObj.setDate(postgresDate(asondate));
                        supplementatypaybillObj.setEmployeemaster(empmasterObj);
                        supplementatypaybillObj.setType("SUPLEMENTARYBILL");
                        supplementatypaybillObj.setSection(empmasterObj.getSection());
                        supplementatypaybillObj.setSubsection(empmasterObj.getSubsection());
                        supplementatypaybillObj.setEmployeecategory(empmasterObj.getCategory());
                        supplementatypaybillObj.setDesignation(empmasterObj.getDesignation());
                        supplementatypaybillObj.setPaymentmode(empmasterObj.getPaymentmode());
                        supplementatypaybillObj.setCancelled(Boolean.FALSE);
                        supplementatypaybillObj.setCreatedby(LoggedInUser);
                        supplementatypaybillObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        session.update(supplementatypaybillObj);
                        transaction.commit();
                    }


                    SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        fm.parse(asondate);
                    } catch (ParseException ex) {
                        Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Calendar cal = fm.getCalendar();
                    int iDay = cal.get(Calendar.DATE);
                    int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
                    int iYear = cal.get(Calendar.YEAR);
                    int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                    //Salary Structure copy
                    String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                    String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                    String queryStr = "select id from salarystructureactual where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + epfno + "' and periodto is null ";
                    //System.out.println(queryStr);
                    // copy data from salary structure actual to salary structure start
                    List salaryStruActualList = (ArrayList) session.createSQLQuery(queryStr).list();
                    if (salaryStruActualList.size() > 0) {
                        String salaryStructureActualId = (String) salaryStruActualList.get(0);
                        queryStr = "select id from supplementarysalarystructure where accregion='" + LoggedInRegion + "' and supplementatypaybillid ='" + supPayBillId + "'";

                        List subSalaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (subSalaryStruList.size() > 0) {
                            subSalaryStructureId = (String) subSalaryStruList.get(0);
                        } else {
                            String fromDateddmmyy = String.valueOf(1) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);
                            String toDateddmmyy = String.valueOf(iLasDay) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);

                            Supplementarysalarystructure supplementarysalarystructureObj = new Supplementarysalarystructure();
                            subSalaryStructureId = getSupplementarysalarystructureid(session, LoggedInRegion);
                            supplementarysalarystructureObj.setId(subSalaryStructureId);
                            supplementarysalarystructureObj.setPeriodfrom(postgresDate(fromDateddmmyy));
                            supplementarysalarystructureObj.setPeriodto(postgresDate(toDateddmmyy));
                            supplementarysalarystructureObj.setSupplementatypaybillid(supPayBillId);
                            supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                            supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                            supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                            transaction = session.beginTransaction();
                            session.save(supplementarysalarystructureObj);
                            transaction.commit();

                        }
                        //System.out.println("sairam 1");
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE supplementaryemployeeearningsdetails  SET cancelled  = true WHERE supplementarysalarystructureid='" + subSalaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE supplementaryemployeedeductiondetails  SET cancelled  = true WHERE supplementarysalarystructureid='" + subSalaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();

                        //Earnings
                        Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                        earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureActualId + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List earDetailsList = earDetailsCrit.list();

                        if (earDetailsList.size() > 0) {
                            for (int j = 0; j < earDetailsList.size(); j++) {
                                Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);

                                Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + employeeearningsdetailsactualObj.getEarningmasterid() + "' "));
                                List earList = earDe.list();
                                if (earList.size() > 0) {
                                    Supplementaryemployeeearningsdetails earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());

                                    transaction = session.beginTransaction();
                                    session.update(earningsdetObj);
                                    transaction.commit();

                                } else {

                                    Supplementaryemployeeearningsdetails earningsdetObj = new Supplementaryemployeeearningsdetails();

                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());
                                    earningsdetObj.setEarningmasterid(employeeearningsdetailsactualObj.getEarningmasterid());
                                    earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId, LoggedInRegion));

                                    earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                                    earningsdetObj.setAccregion(LoggedInRegion);

                                    transaction = session.beginTransaction();
                                    session.save(earningsdetObj);
                                    transaction.commit();
                                }
                            }
                        }
                        //deduction
                        Criteria dedDetailsCrit = session.createCriteria(Employeedeductiondetailsactual.class);
                        dedDetailsCrit.add(Restrictions.sqlRestriction("salarystructureactualid = '" + salaryStructureActualId + "' "));
                        dedDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        dedDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List dedDetailsList = dedDetailsCrit.list();
                        resultMap.put("employeedeductionslength", dedDetailsList.size());
                        if (dedDetailsList.size() > 0) {
                            for (int j = 0; j < dedDetailsList.size(); j++) {
                                Employeedeductiondetailsactual employeedeductiondetailsObj = (Employeedeductiondetailsactual) dedDetailsList.get(j);

                                Criteria dedDe = session.createCriteria(Supplementaryemployeedeductiondetails.class);
                                dedDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                                dedDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                dedDe.add(Restrictions.sqlRestriction("deductionmasterid = '" + employeedeductiondetailsObj.getDeductionmasterid() + "' "));
                                List dedList = dedDe.list();
                                if (dedList.size() > 0) {
                                    Supplementaryemployeedeductiondetails deducdetObj = (Supplementaryemployeedeductiondetails) dedList.get(0);
                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());

                                    transaction = session.beginTransaction();
                                    session.update(deducdetObj);
                                    transaction.commit();

                                } else {

                                    Supplementaryemployeedeductiondetails deducdetObj = new Supplementaryemployeedeductiondetails();

                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDeductionmasterid(employeedeductiondetailsObj.getDeductionmasterid());
                                    deducdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId, LoggedInRegion));
                                    deducdetObj.setId(getSupplementaryemployeedeductiondetailsid(session, LoggedInRegion));
                                    deducdetObj.setAccregion(LoggedInRegion);
                                    transaction = session.beginTransaction();
                                    session.save(deducdetObj);
                                    transaction.commit();

                                }


                            }
                        }
                    }


                    Criteria dedDetailsCritT1 = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                    dedDetailsCritT1.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                    dedDetailsCritT1.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    dedDetailsCritT1.add(Restrictions.sqlRestriction("cancelled is false"));
                    List dedDetailsListT1 = dedDetailsCritT1.list();
                    if (dedDetailsListT1.size() > 0) {
                        for (int j = 0; j < dedDetailsListT1.size(); j++) {
                            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) dedDetailsListT1.get(j);

                            transaction = session.beginTransaction();
                            session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions  SET cancelled  = true WHERE supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();
                            transaction = session.beginTransaction();
                            session.createSQLQuery("UPDATE supplementaryemployeedeductionstransactions  SET cancelled  = true WHERE supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();

                            Supplementaryemployeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                            Employeeloansandadvances employeeloansandadvancesObje;
                            Criteria empLoanDetailsCrit = session.createCriteria(Supplementaryemployeeloansandadvancesdetails.class);
                            empLoanDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
                            List empLoanDetailsList = empLoanDetailsCrit.list();
                            if (empLoanDetailsList.size() > 0) {
                                for (int i = 0; i < empLoanDetailsList.size(); i++) {
                                    transaction = session.beginTransaction();
                                    employeeloansandadvancesdetailsObje = (Supplementaryemployeeloansandadvancesdetails) empLoanDetailsList.get(i);

                                    employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                                    session.update(employeeloansandadvancesdetailsObje);

                                    Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
                                    empLoanCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                    empLoanCrit.add(Restrictions.sqlRestriction("id='" + employeeloansandadvancesdetailsObje.getEmployeeloansandadvances().getId() + "'"));
                                    List empLoanList = empLoanCrit.list();
                                    if (empLoanList.size() > 0) {
                                        employeeloansandadvancesObje = (Employeeloansandadvances) empLoanList.get(0);
                                        employeeloansandadvancesObje.setCurrentinstallment(employeeloansandadvancesObje.getCurrentinstallment() - 1);
                                        employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().add(employeeloansandadvancesdetailsObje.getInstallmentamount()));
                                        session.update(employeeloansandadvancesObje);
                                    }
                                    transaction.commit();
                                }
                            }


                        }
                    }

                    Criteria dedDetailsCritT = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                    dedDetailsCritT.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                    dedDetailsCritT.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    dedDetailsCritT.add(Restrictions.sqlRestriction("cancelled is true"));
                    List dedDetailsListT = dedDetailsCritT.list();
                    if (dedDetailsListT.size() > 0) {
                        for (int j = 0; j < dedDetailsListT.size(); j++) {
                            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) dedDetailsListT.get(j);

                            transaction = session.beginTransaction();
                            session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions  SET cancelled  = true WHERE supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();
                            transaction = session.beginTransaction();
                            session.createSQLQuery("UPDATE supplementaryemployeedeductionstransactions  SET cancelled  = true WHERE supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();


                        }
                    }



                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE supplementarypayrollprocessingdetails  SET cancelled  = true WHERE supplementatypaybillid='" + supplementatypaybillObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();


                    //Salary Structure copy ends here
                    TreeSet<Integer> keys = new TreeSet<Integer>(saveMap.keySet());
                    if (saveMap.size() > 0) {
                        for (Integer objkey : keys) {
                            Map sdoObj = (Map) saveMap.get(objkey);
                            int iMonthProcess = Integer.parseInt(sdoObj.get("month").toString());
                            int iYearProcess = Integer.parseInt(sdoObj.get("year").toString());
                            int iNoofDays = Integer.parseInt(sdoObj.get("noofdays").toString());

                            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();
                            supplementarypayrollprocessingdetailsObj.setId(getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion));
                            supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
                            supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                            supplementarypayrollprocessingdetailsObj.setCalculatedmonth(iMonthProcess);
                            supplementarypayrollprocessingdetailsObj.setCalculatedyear(iYearProcess);
                            supplementarypayrollprocessingdetailsObj.setNooddayscalculated(iNoofDays);
                            supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                            supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());


                            transaction = session.beginTransaction();
                            session.persist(supplementarypayrollprocessingdetailsObj);
                            transaction.commit();

                        }
                    }



                    // Earnings and Deductions Postings Starts
                    Criteria dedDetailsCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                    dedDetailsCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                    dedDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    dedDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List dedDetailsList = dedDetailsCrit.list();
                    if (dedDetailsList.size() > 0) {
                        for (int j = 0; j < dedDetailsList.size(); j++) {
                            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) dedDetailsList.get(j);

                            transaction = session.beginTransaction();
                            session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions  SET cancelled  = true WHERE supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();
                            transaction = session.beginTransaction();
                            session.createSQLQuery("UPDATE supplementaryemployeedeductionstransactions  SET cancelled  = true WHERE supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                            transaction.commit();
                            // Earning Starts Here
                            Criteria empEarCrit = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                            empEarCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                            empEarCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empEarCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empEarList = empEarCrit.list();
                            if (empEarList.size() > 0) {
                                for (int i = 0; i < empEarList.size(); i++) {
                                    Supplementaryemployeeearningsdetails supplementaryemployeeearningsdetailsObje = (Supplementaryemployeeearningsdetails) empEarList.get(i);

                                    Criteria empEarTranCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                    empEarTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementaryemployeeearningsdetailsObje.getId() + "'"));
                                    empEarTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                    empEarTranCrit.add(Restrictions.sqlRestriction("earningmasterid='" + supplementaryemployeeearningsdetailsObje.getEarningmasterid() + "'"));
                                    List empEarTranList = empEarTranCrit.list();
                                    if (empEarTranList.size() > 0) {

                                        Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarTranList.get(0);
                                        employeeearningstransactionsObj.setEarningmasterid(supplementaryemployeeearningsdetailsObje.getEarningmasterid());
                                        employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                        //System.out.println("supplementarypayrollprocessingdetailsObj.getCalculatedmonth() ::: " + supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                        //System.out.println("supplementarypayrollprocessingdetailsObj.getCalculatedyear() :::: " + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                        String supplementarycalculateddate = "01/" + zeroFill(supplementarypayrollprocessingdetailsObj.getCalculatedmonth()) + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear();
                                        //System.out.println("supplementarycalculateddate :: " + supplementarycalculateddate);
                                        if (supplementaryemployeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                            employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), supplementaryemployeeearningsdetailsObje.getAmount()));
                                        } else {
                                            employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), getEarningsAmount(session, subSalaryStructureId, supplementaryemployeeearningsdetailsObje.getEarningmasterid(), LoggedInRegion, supplementarycalculateddate)));
                                        }
                                        employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                        employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                        employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                        transaction = session.beginTransaction();
                                        session.update(employeeearningstransactionsObj);
                                    } else {
                                        String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                                        Supplementaryemployeeearningstransactions employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();
                                        employeeearningstransactionsObj.setId(earningsTransactionId);
                                        employeeearningstransactionsObj.setEarningmasterid(supplementaryemployeeearningsdetailsObje.getEarningmasterid());
                                        employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                        //System.out.println("supplementarypayrollprocessingdetailsObj.getCalculatedmonth() ::: " + supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                        //System.out.println("supplementarypayrollprocessingdetailsObj.getCalculatedyear() :::: " + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                        String supplementarycalculateddate = "01/" + zeroFill(supplementarypayrollprocessingdetailsObj.getCalculatedmonth()) + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear();
                                        //System.out.println("supplementarycalculateddate :: " + supplementarycalculateddate);
                                        if (supplementaryemployeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                            employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), supplementaryemployeeearningsdetailsObje.getAmount()));
                                        } else {
                                            employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), getEarningsAmount(session, subSalaryStructureId, supplementaryemployeeearningsdetailsObje.getEarningmasterid(), LoggedInRegion, supplementarycalculateddate)));
                                        }
                                        employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                        employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                                        employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                        employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                        transaction = session.beginTransaction();
                                        session.save(employeeearningstransactionsObj);
                                    }
                                    transaction.commit();
                                }

                            }
                            // Earning Starts Here
                            // Deductions Starts Here
                            if (supplementarypayrollprocessingdetailsObj.getNooddayscalculated() > 15) {
                                Criteria empDeduCrit = session.createCriteria(Supplementaryemployeedeductiondetails.class);
                                empDeduCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                                empDeduCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empDeduCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                List empDeduList = empDeduCrit.list();
                                if (empDeduList.size() > 0) {
                                    for (int i = 0; i < empDeduList.size(); i++) {
                                        Supplementaryemployeedeductiondetails supplementaryemployeedeductiondetailsObje = (Supplementaryemployeedeductiondetails) empDeduList.get(i);


                                        Criteria empDeduTranCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                                        empDeduTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementaryemployeedeductiondetailsObje.getId() + "'"));
                                        empDeduTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empDeduTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + supplementaryemployeedeductiondetailsObje.getDeductionmasterid() + "'"));
                                        List empDeduTranList = empDeduTranCrit.list();
                                        if (empDeduTranList.size() > 0) {

                                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeduTranList.get(0);
                                            employeedeductionstransactionsObj.setDeductionmasterid(supplementaryemployeedeductiondetailsObje.getDeductionmasterid());
                                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                            if (supplementaryemployeedeductiondetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                                //employeedeductionstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), supplementaryemployeedeductiondetailsObje.getAmount()));
                                                employeedeductionstransactionsObj.setAmount(supplementaryemployeedeductiondetailsObje.getAmount());

                                            } else {

                                                employeedeductionstransactionsObj.setAmount(getDeductionAmount(session, subSalaryStructureId, supplementaryemployeedeductiondetailsObje.getDeductionmasterid(), supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), LoggedInRegion, asondate));
                                            }
                                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                            transaction = session.beginTransaction();
                                            session.update(employeedeductionstransactionsObj);
                                        } else {
                                            String deductionTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                            employeedeductionstransactionsObj.setId(deductionTransactionId);
                                            employeedeductionstransactionsObj.setDeductionmasterid(supplementaryemployeedeductiondetailsObje.getDeductionmasterid());
                                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                            if (supplementaryemployeedeductiondetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                                //employeedeductionstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), supplementaryemployeedeductiondetailsObje.getAmount()));
                                                employeedeductionstransactionsObj.setAmount(supplementaryemployeedeductiondetailsObje.getAmount());
                                            } else {
                                                employeedeductionstransactionsObj.setAmount(getDeductionAmount(session, subSalaryStructureId, supplementaryemployeedeductiondetailsObje.getDeductionmasterid(), supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), LoggedInRegion, asondate));
                                            }
                                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                            transaction = session.beginTransaction();
                                            session.save(employeedeductionstransactionsObj);
                                        }
                                        transaction.commit();

                                    }

                                }
                            } else {
                                Criteria empDeduCrit = session.createCriteria(Supplementaryemployeedeductiondetails.class);
                                empDeduCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                                empDeduCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empDeduCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + "D02" + "'"));
                                empDeduCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                List empDeduList = empDeduCrit.list();
                                if (empDeduList.size() > 0) {
                                    for (int i = 0; i < empDeduList.size(); i++) {
                                        Supplementaryemployeedeductiondetails supplementaryemployeedeductiondetailsObje = (Supplementaryemployeedeductiondetails) empDeduList.get(i);


                                        Criteria empDeduTranCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                                        empDeduTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementaryemployeedeductiondetailsObje.getId() + "'"));
                                        empDeduTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empDeduTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + supplementaryemployeedeductiondetailsObje.getDeductionmasterid() + "'"));
                                        List empDeduTranList = empDeduTranCrit.list();
                                        if (empDeduTranList.size() > 0) {

                                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeduTranList.get(0);
                                            employeedeductionstransactionsObj.setDeductionmasterid(supplementaryemployeedeductiondetailsObje.getDeductionmasterid());
                                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                            if (supplementaryemployeedeductiondetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                                employeedeductionstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), supplementaryemployeedeductiondetailsObje.getAmount()));
                                            } else {

                                                employeedeductionstransactionsObj.setAmount(getDeductionAmount(session, subSalaryStructureId, supplementaryemployeedeductiondetailsObje.getDeductionmasterid(), supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), LoggedInRegion, asondate));
                                            }
                                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                            transaction = session.beginTransaction();
                                            session.update(employeedeductionstransactionsObj);
                                        } else {
                                            String deductionTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                            employeedeductionstransactionsObj.setId(deductionTransactionId);
                                            employeedeductionstransactionsObj.setDeductionmasterid(supplementaryemployeedeductiondetailsObje.getDeductionmasterid());
                                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                            if (supplementaryemployeedeductiondetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                                employeedeductionstransactionsObj.setAmount(calculateEarningsForGivenDays(session, epfno, supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), supplementaryemployeedeductiondetailsObje.getAmount()));
                                            } else {
                                                employeedeductionstransactionsObj.setAmount(getDeductionAmount(session, subSalaryStructureId, supplementaryemployeedeductiondetailsObje.getDeductionmasterid(), supplementarypayrollprocessingdetailsObj.getNooddayscalculated(), supplementarypayrollprocessingdetailsObj.getCalculatedmonth(), supplementarypayrollprocessingdetailsObj.getCalculatedyear(), LoggedInRegion, asondate));
                                            }
                                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                            transaction = session.beginTransaction();
                                            session.save(employeedeductionstransactionsObj);
                                        }
                                        transaction.commit();
                                    }

                                }

                            }
                            // Dedctions Ends Here
                            //System.out.println("misc");
                            if (supplementarypayrollprocessingdetailsObj.getNooddayscalculated() > 15) {
                                // Miscelleneous Deductions
                                //System.out.println("misc 1");
                                Miscdeductions miscdeductionsObje;
                                Criteria misDeducCrit = session.createCriteria(Miscdeductions.class);
                                misDeducCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                                misDeducCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                misDeducCrit.add(Restrictions.sqlRestriction("year = " + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                                misDeducCrit.add(Restrictions.sqlRestriction("month = " + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                                List misDeducList = misDeducCrit.list();
                                if (misDeducList.size() > 0) {
//                                    System.out.println("misc2");
                                    for (int ij = 0; ij < misDeducList.size(); ij++) {
                                        miscdeductionsObje = (Miscdeductions) misDeducList.get(ij);
//                                        System.out.println("misc3");
                                        String deductionsTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                        Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                        employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                        employeedeductionstransactionsObj.setDeductionmasterid(miscdeductionsObje.getDeductionscode());
                                        employeedeductionstransactionsObj.setAmount(miscdeductionsObje.getAmount());
                                        employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                        employeedeductionstransactionsObj.setType("MISC");
                                        employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                        employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                        employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                        employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                        transaction = session.beginTransaction();
                                        session.save(employeedeductionstransactionsObj);

                                        transaction.commit();

                                    }

                                }
                                // Miscelleneous Deductions Ends Here
                                //System.out.println("misc4");
                                // Deductions Others Starts

                                Salarydeductionothers salarydeductionothersObj;
                                Criteria otherDeducCrit = session.createCriteria(Salarydeductionothers.class);
                                otherDeducCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                                otherDeducCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                otherDeducCrit.add(Restrictions.sqlRestriction("deductionmonth = " + iMonth));
                                otherDeducCrit.add(Restrictions.sqlRestriction("deductionyear = " + iYear));
                                otherDeducCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                List otherDeducList = otherDeducCrit.list();
                                if (otherDeducList.size() > 0) {
                                    for (int ij = 0; ij < otherDeducList.size(); ij++) {
                                        salarydeductionothersObj = (Salarydeductionothers) otherDeducList.get(ij);

                                        String deductionsTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                        Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                        employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                        employeedeductionstransactionsObj.setDeductionmasterid(salarydeductionothersObj.getPaycodemaster().getPaycode());
                                        employeedeductionstransactionsObj.setAmount(salarydeductionothersObj.getAmountornoofdays());
                                        employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                        employeedeductionstransactionsObj.setType("OTHERSDEDUC");
                                        employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                        employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                        employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                        employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                        transaction = session.beginTransaction();
                                        session.save(employeedeductionstransactionsObj);

                                        transaction.commit();

                                    }

                                }
                                // Deductions Others ends
                                // Loans and Advances Deductions
                                //For reprocessing
                                Supplementaryemployeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                                Employeeloansandadvances employeeloansandadvancesObje;
                                Criteria empLoanDetailsCrit = session.createCriteria(Supplementaryemployeeloansandadvancesdetails.class);
                                empLoanDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                                empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
                                List empLoanDetailsList = empLoanDetailsCrit.list();
                                //System.out.println("control in reprocessing" + empLoanDetailsList.size() + "    " + supplementarypayrollprocessingdetailsObj.getId());
                                if (empLoanDetailsList.size() > 0) {
                                    for (int i = 0; i < empLoanDetailsList.size(); i++) {
                                        transaction = session.beginTransaction();
                                        employeeloansandadvancesdetailsObje = (Supplementaryemployeeloansandadvancesdetails) empLoanDetailsList.get(i);

                                        employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                                        session.update(employeeloansandadvancesdetailsObje);

                                        Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
                                        empLoanCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empLoanCrit.add(Restrictions.sqlRestriction("id='" + employeeloansandadvancesdetailsObje.getEmployeeloansandadvances().getId() + "'"));
                                        List empLoanList = empLoanCrit.list();
                                        if (empLoanList.size() > 0) {
                                            employeeloansandadvancesObje = (Employeeloansandadvances) empLoanList.get(0);
                                            employeeloansandadvancesObje.setCurrentinstallment(employeeloansandadvancesObje.getCurrentinstallment() - 1);
                                            employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().add(employeeloansandadvancesdetailsObje.getInstallmentamount()));
                                            session.update(employeeloansandadvancesObje);
                                        }
                                        transaction.commit();
                                    }
                                }

                                //For loan deductions
                                Criteria empLoanCritUp = session.createCriteria(Employeeloansandadvances.class);
                                empLoanCritUp.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
                                empLoanCritUp.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empLoanCritUp.add(Restrictions.sqlRestriction("loanbalance > 0"));
                                List empLoanListUp = empLoanCritUp.list();
                                if (empLoanListUp.size() > 0) {
                                    for (int i = 0; i < empLoanListUp.size(); i++) {
                                        employeeloansandadvancesObje = (Employeeloansandadvances) empLoanListUp.get(i);

                                        Criteria empLoanDetailsCritUp = session.createCriteria(Supplementaryemployeeloansandadvancesdetails.class);
                                        empLoanDetailsCritUp.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                                        empLoanDetailsCritUp.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empLoanDetailsCritUp.add(Restrictions.sqlRestriction("employeeloansandadvancesid ='" + employeeloansandadvancesObje.getId() + "'"));
                                        List empLoanDetailsListUp = empLoanDetailsCritUp.list();
                                        if (empLoanDetailsListUp.size() > 0) {
                                            Supplementaryemployeeloansandadvancesdetails employeeloansandadvancesdetailsOb = (Supplementaryemployeeloansandadvancesdetails) empLoanDetailsListUp.get(0);
                                            employeeloansandadvancesdetailsOb.setCancelled(Boolean.FALSE);
                                            employeeloansandadvancesdetailsOb.setCreatedby(LoggedInUser);
                                            employeeloansandadvancesdetailsOb.setCreateddate(getCurrentDate());
                                            employeeloansandadvancesdetailsOb.setNthinstallment(employeeloansandadvancesObje.getCurrentinstallment() + 1);
//                                employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance());
                                            if (employeeloansandadvancesObje.getCurrentinstallment() == 0) {
                                                if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getFirstinstallmentamount());
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getLoanbalance());
                                                }
                                            } else {
                                                if (employeeloansandadvancesObje.getInstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getInstallmentamount());
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getLoanbalance());
                                                }

                                            }
                                            if (employeeloansandadvancesObje.getCurrentinstallment() == 0) {
                                                if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getFirstinstallmentamount()));
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getLoanbalance()));
                                                }
                                            } else {
                                                if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getInstallmentamount()));
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getLoanbalance()));
                                                }
                                            }
                                            transaction = session.beginTransaction();
                                            session.update(employeeloansandadvancesdetailsOb);
                                            transaction.commit();

                                            //Adding loan transactions in salary deductions
                                            transaction = session.beginTransaction();
                                            String deductionsTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                            employeedeductionstransactionsObj.setDeductionmasterid(employeeloansandadvancesObje.getDeductioncode());

                                            employeedeductionstransactionsObj.setAmount(employeeloansandadvancesdetailsOb.getInstallmentamount());

                                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                            employeedeductionstransactionsObj.setType("LOAN");
                                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                            transaction = session.beginTransaction();
                                            session.save(employeedeductionstransactionsObj);
                                            transaction.commit();
                                            //Adding loan transactions in salary deductions end
                                        } else {
                                            Supplementaryemployeeloansandadvancesdetails employeeloansandadvancesdetailsOb = new Supplementaryemployeeloansandadvancesdetails();
                                            String id = getmaxSequenceNumberforSupplementaryemployeeloansandadvancesdetails(session, LoggedInRegion);
                                            employeeloansandadvancesdetailsOb.setId(id);
                                            employeeloansandadvancesdetailsOb.setEmployeeloansandadvances(employeeloansandadvancesObje);
                                            employeeloansandadvancesdetailsOb.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                            employeeloansandadvancesdetailsOb.setCancelled(Boolean.FALSE);
                                            employeeloansandadvancesdetailsOb.setNthinstallment(employeeloansandadvancesObje.getCurrentinstallment() + 1);
                                            employeeloansandadvancesdetailsOb.setCreatedby(LoggedInUser);
                                            employeeloansandadvancesdetailsOb.setCreateddate(getCurrentDate());
//                                employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance());
                                            if (employeeloansandadvancesObje.getCurrentinstallment() == 0) {
                                                if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getFirstinstallmentamount());
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getLoanbalance());
                                                }
                                            } else {
                                                if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getInstallmentamount());
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setInstallmentamount(employeeloansandadvancesObje.getLoanbalance());
                                                }
                                            }
                                            if (employeeloansandadvancesObje.getCurrentinstallment() == 0) {
                                                if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getFirstinstallmentamount()));
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getLoanbalance()));
                                                }
                                            } else {
                                                if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getInstallmentamount()));
                                                } else {
                                                    employeeloansandadvancesdetailsOb.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getLoanbalance()));
                                                }
                                            }

                                            //Adding loan transactions in salary deductions
                                            transaction = session.beginTransaction();
                                            String deductionsTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                            employeedeductionstransactionsObj.setDeductionmasterid(employeeloansandadvancesObje.getDeductioncode());

                                            employeedeductionstransactionsObj.setAmount(employeeloansandadvancesdetailsOb.getInstallmentamount());

                                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                            employeedeductionstransactionsObj.setType("LOAN");
                                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                            transaction = session.beginTransaction();
                                            session.save(employeedeductionstransactionsObj);
                                            transaction.commit();
                                            employeeloansandadvancesdetailsOb.setAccregion(LoggedInRegion);
                                            transaction = session.beginTransaction();
                                            session.save(employeeloansandadvancesdetailsOb);
                                            transaction.commit();


                                        }
                                        //Adding loan transactions in salary deductions end
                                        employeeloansandadvancesObje.setCurrentinstallment(employeeloansandadvancesObje.getCurrentinstallment() + 1);
                                        if (employeeloansandadvancesObje.getCurrentinstallment() == 0) {
                                            if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getFirstinstallmentamount()));
                                            } else {
                                                employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getLoanbalance()));
                                            }
                                        } else {
                                            if (employeeloansandadvancesObje.getFirstinstallmentamount().floatValue() <= employeeloansandadvancesObje.getLoanbalance().floatValue()) {
                                                employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getInstallmentamount()));
                                            } else {
                                                employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().subtract(employeeloansandadvancesObje.getLoanbalance()));
                                            }
                                        }
                                        transaction = session.beginTransaction();
                                        session.update(employeeloansandadvancesObje);
                                        transaction.commit();

                                    }
                                }
                            } else {
                                Supplementaryemployeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                                Employeeloansandadvances employeeloansandadvancesObje;
                                Criteria empLoanDetailsCrit = session.createCriteria(Supplementaryemployeeloansandadvancesdetails.class);
                                empLoanDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                                empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
                                List empLoanDetailsList = empLoanDetailsCrit.list();
                                if (empLoanDetailsList.size() > 0) {
                                    for (int i = 0; i < empLoanDetailsList.size(); i++) {
                                        transaction = session.beginTransaction();
                                        employeeloansandadvancesdetailsObje = (Supplementaryemployeeloansandadvancesdetails) empLoanDetailsList.get(i);

                                        employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                                        session.update(employeeloansandadvancesdetailsObje);

                                        Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
                                        empLoanCrit.add(Restrictions.sqlRestriction("id='" + employeeloansandadvancesdetailsObje.getEmployeeloansandadvances().getId() + "'"));
                                        List empLoanList = empLoanCrit.list();
                                        if (empLoanList.size() > 0) {
                                            employeeloansandadvancesObje = (Employeeloansandadvances) empLoanList.get(0);
                                            employeeloansandadvancesObje.setCurrentinstallment(employeeloansandadvancesObje.getCurrentinstallment() - 1);
                                            employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().add(employeeloansandadvancesdetailsObje.getInstallmentamount()));
                                            session.update(employeeloansandadvancesObje);
                                        }
                                        transaction.commit();
                                    }
                                }

                            }
                            // Recovery Deductions
                        }
                    }
                    request.getSession().removeAttribute("addedMap");
                    resultMap.put("epfno", epfno);
                    resultMap.put("billno", supPayBillId);
                    resultMap.put("success", "Supplementary Bill Successfully Saved");
                    // Earnings and Deductions Postings Starts

                } else {
//                    System.out.println("=====()()()()()===");
                    resultMap.put("ERROR", "Please add the Supplimentary Bill Process");
                }
            } else {
                resultMap.put("ERROR", "EPF Prepated, Please Select Another date for as on date");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    public BigDecimal getEarningsAmount1(Session session, String subSalaryStructureId, String earningMasterid, String LoggedInRegion, String asondate) {

        System.out.println("earningMasterid ****************** " + earningMasterid);
        System.out.println("subSalaryStructureId ****************** " + subSalaryStructureId);

        float total = 0;
        float earamt = 0;
        Ccahra ccahraObj;
        Supplementaryemployeeearningsdetails employeeearningsdetailsObj;
        Criteria ccaHRA = session.createCriteria(Ccahra.class);
        ccaHRA.add(Restrictions.sqlRestriction("ccahra='" + earningMasterid + "'"));
        List ccaHRAList = ccaHRA.list();

        if (ccaHRAList.size() > 0) {
            for (int i = 0; i < ccaHRAList.size(); i++) {
                ccahraObj = (Ccahra) ccaHRAList.get(i);
                Criteria earCrit = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Supplementaryemployeeearningsdetails) earCritList.get(0);
                    total = total + employeeearningsdetailsObj.getAmount().floatValue();
                }

            }
        }

        String querystr = " case when periodto is null then periodfrom <= '" + postgresDate(asondate) + "' else '" + postgresDate(asondate) + "' between "
                + "periodfrom and periodto end";

        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + earningMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction(querystr));
        //System.out.println("stage 2...." + earningMasterid);
        List earSlapList = earSlapCrit.list();
        //System.out.println("earSlapList size @@@@@@@@@@@" + earSlapList.size());
        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            //System.out.println("EarningslapdetailsObj.getId() ::::::::::::::::::::::::" + EarningslapdetailsObj.getId());
            //System.out.println("EarningslapdetailsObj.getPercentage() ::::::::::::::::" + EarningslapdetailsObj.getPercentage());
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                float perc = EarningslapdetailsObj.getPercentage().floatValue() / 100;
                total = total * perc;
                float x = total;

                BigDecimal b = new BigDecimal(x);
                b = b.setScale(2, RoundingMode.UP);
                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
                y.floatValue();
                earamt = y.floatValue();

            } else {
                earamt = EarningslapdetailsObj.getAmount().floatValue();
            }

        }
        BigDecimal earamount = new BigDecimal(earamt);
        return earamount;

    }

    public BigDecimal getDeductionAmount(Session session, String subSalaryStructureId, String deductionMasterid, int nofodays, int month, int year, String LoggedInRegion, String asondate) {
        float total = 0;
        float earamt = 0;
        Ccahra ccahraObj;
        Supplementaryemployeeearningsdetails employeeearningsdetailsObj;
        Criteria ccaHRA = session.createCriteria(Ccahra.class);
        ccaHRA.add(Restrictions.sqlRestriction("ccahra='" + deductionMasterid + "'"));
        List ccaHRAList = ccaHRA.list();
        if (ccaHRAList.size() > 0) {
            for (int i = 0; i < ccaHRAList.size(); i++) {
                ccahraObj = (Ccahra) ccaHRAList.get(i);
                Criteria earCrit = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Supplementaryemployeeearningsdetails) earCritList.get(0);

                    if (employeeearningsdetailsObj.getAmount().floatValue() != 0) {
                        total = total + employeeearningsdetailsObj.getAmount().floatValue();
                    } else {
                        total = total + getEarningsAmount1(session, subSalaryStructureId, employeeearningsdetailsObj.getEarningmasterid(), LoggedInRegion, asondate).floatValue();
                    }


                }

            }
        }

        String querystr = " case when periodto is null then periodfrom <= '" + postgresDate(asondate) + "' else '" + postgresDate(asondate) + "' between "
                + "periodfrom and periodto end";

        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + deductionMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction(querystr));
        List earSlapList = earSlapCrit.list();
        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                total = calculateEarningsForGivenDays(session, "", nofodays, month, year, new BigDecimal(total)).floatValue() * EarningslapdetailsObj.getPercentage().floatValue() / 100;
                float x = total;
                BigDecimal b = new BigDecimal(x);
                b = b.setScale(2, RoundingMode.HALF_UP);
                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
                earamt = y.floatValue();
            }

        }

        BigDecimal earamount = new BigDecimal(earamt);
        return earamount;
    }

    public BigDecimal getDeductionAmountSurrender(Session session, String subSalaryStructureId, String deductionMasterid, int nofodays, String LoggedInRegion, String asondate) {
        float total = 0;
        float earamt = 0;
        Ccahra ccahraObj;
        Supplementaryemployeeearningsdetails employeeearningsdetailsObj;
        Criteria ccaHRA = session.createCriteria(Ccahra.class);
        ccaHRA.add(Restrictions.sqlRestriction("ccahra='" + deductionMasterid + "'"));
        List ccaHRAList = ccaHRA.list();
        if (ccaHRAList.size() > 0) {
            for (int i = 0; i < ccaHRAList.size(); i++) {
                ccahraObj = (Ccahra) ccaHRAList.get(i);
                Criteria earCrit = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Supplementaryemployeeearningsdetails) earCritList.get(0);

                    if (employeeearningsdetailsObj.getAmount().floatValue() != 0) {
                        total = total + employeeearningsdetailsObj.getAmount().floatValue();
                    } else {
                        total = total + getEarningsAmount1(session, subSalaryStructureId, employeeearningsdetailsObj.getEarningmasterid(), LoggedInRegion, asondate).floatValue();
                    }


                }

            }
        }

        String querystr = " case when periodto is null then periodfrom <= '" + postgresDate(asondate) + "' else '" + postgresDate(asondate) + "' between "
                + "periodfrom and periodto end";

        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + deductionMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction(querystr));
        List earSlapList = earSlapCrit.list();
        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                total = calculateEarningsForGivenDays30(nofodays, new BigDecimal(total)).floatValue() * EarningslapdetailsObj.getPercentage().floatValue() / 100;
                float x = total;
                BigDecimal b = new BigDecimal(x);
                b = b.setScale(2, RoundingMode.HALF_UP);
                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
                earamt = y.floatValue();
            }

        }

        BigDecimal earamount = new BigDecimal(earamt);
        return earamount;
    }

    public BigDecimal calculateEarningsForGivenDays(Session session, String epfno, int noofdays, int month, int year, BigDecimal totamount) {
        float total = 0;
        String Datestring = "01/" + month + "/" + year;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(Datestring);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        total = totamount.floatValue();
        total = totamount.floatValue() / iLasDay * noofdays;
        BigDecimal amount = new BigDecimal(total);
        BigDecimal amountr = amount.setScale(0, RoundingMode.HALF_UP);
        return amountr;
    }

    public BigDecimal calculateEarningsForGivenDays30(int noofdays, BigDecimal totamount) {
        float total = 0;
        total = totamount.floatValue() / 30 * noofdays;
        BigDecimal amount = new BigDecimal(total);
        BigDecimal amountr = amount.setScale(0, RoundingMode.HALF_UP);
        return amountr;
    }

    public BigDecimal getEarningsAmount(Session session, String subSalaryStructureId, String earningMasterid, String LoggedInRegion, String sdate) throws ParseException {

        //System.out.println("::::::::::: subSalaryStructureId = " + subSalaryStructureId);
        //System.out.println("::::::::::: earningMasterid = " + earningMasterid);

        float total = 0;
        float earamt = 0;
        Ccahra ccahraObj;
        Supplementaryemployeeearningsdetails employeeearningsdetailsObj;
        Criteria ccaHRA = session.createCriteria(Ccahra.class);
        ccaHRA.add(Restrictions.sqlRestriction("ccahra='" + earningMasterid + "'"));
        List ccaHRAList = ccaHRA.list();
        if (ccaHRAList.size() > 0) {
            for (int i = 0; i < ccaHRAList.size(); i++) {
                ccahraObj = (Ccahra) ccaHRAList.get(i);
                Criteria earCrit = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Supplementaryemployeeearningsdetails) earCritList.get(0);
                    total = total + employeeearningsdetailsObj.getAmount().floatValue();
                }
            }
        }

        String quey = " case when periodto is null "
                + "then periodfrom <= '" + postgresDate(sdate) + "' else '" + postgresDate(sdate) + "' between "
                + "periodfrom and periodto end";

        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + earningMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction(quey));
//        earSlapCrit.add(Restrictions.sqlRestriction("periodto is null"));
        //System.out.println("@@@@@@@@@@earningMasterid = " + earningMasterid);
        //System.out.println("@@@@@@@@@@total = " + total);
        List earSlapList = earSlapCrit.list();



        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            //System.out.println("@@@@@@@@@@EarningslapdetailsObj.getId()" + EarningslapdetailsObj.getId());
            //System.out.println("@@@@@@@@@@EarningslapdetailsObj.getPercentage()" + EarningslapdetailsObj.getPercentage());
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                float perc = EarningslapdetailsObj.getPercentage().floatValue() / 100;
                total = total * perc;
                float x = total;
                BigDecimal b = new BigDecimal(x);
                b = b.setScale(2, RoundingMode.UP);
                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
                earamt = y.floatValue();

            } else {
                earamt = EarningslapdetailsObj.getAmount().floatValue();
            }

        }

//        if (earSlapList.size() > 0) {
//            BigDecimal amt = new BigDecimal("0.00");
//            BigDecimal percen = new BigDecimal("0.00");
//            for (int i = 0; i < earSlapList.size(); i++) {
//                Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(i);
//                Date fromDate = EarningslapdetailsObj.getPeriodfrom();
//                Date toDate = EarningslapdetailsObj.getPeriodto();
//                Date supdate = javautilDate(sdate);
//
//                System.out.println("fromDate = " + fromDate);
//                System.out.println("toDate = " + toDate);
//                System.out.println("supdate = " + supdate);
//
//                if ((fromDate.after(supdate) || fromDate.equals(supdate)) && (toDate.before(supdate) || toDate.equals(supdate))) {
//                    amt = EarningslapdetailsObj.getAmount();
//                    percen = EarningslapdetailsObj.getPercentage();
//                }
//            }
//            System.out.println("amt = " + amt);
//            System.out.println("percen = " + percen);
//            if (amt.equals(new BigDecimal("0.00"))) {
//                float perc = percen.floatValue() / 100;
//                total = total * perc;
//                float x = total;
//                BigDecimal b = new BigDecimal(x);
//                b = b.setScale(2, RoundingMode.UP);
//                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
//                earamt = y.floatValue();
//
//            } else {
//                earamt = amt.floatValue();
//            }
//        }
//        if (earSlapList.size() > 0) {
//            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
//            System.out.println("@@@@@@@@@@EarningslapdetailsObj.getId()" + EarningslapdetailsObj.getId());
//            System.out.println("@@@@@@@@@@EarningslapdetailsObj.getPercentage()" + EarningslapdetailsObj.getPercentage());
//            EarningslapdetailsObj.getAmount();
//            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
//                float perc = EarningslapdetailsObj.getPercentage().floatValue() / 100;
//                total = total * perc;
//                float x = total;
//                BigDecimal b = new BigDecimal(x);
//                b = b.setScale(2, RoundingMode.UP);
//                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
//                earamt = y.floatValue();
//
//            } else {
//                earamt = EarningslapdetailsObj.getAmount().floatValue();
//            }
//
//        }
        BigDecimal earamount = new BigDecimal(earamt);
        return earamount;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map displayAddedSupplementaryDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String noofdays, String isnewmap) {
        System.out.println("********************** SupplementaryBillServiceImpl class displayAddedSupplementaryDetails method is calling ********************");
        Map resultMap = new HashMap();
        Map addMap = new HashMap();
        Map addedMap = new HashMap();
        boolean isProcessOpen = false;
        Payrollprocessing payrollprocessingObje;
        Criteria proCrit1 = session.createCriteria(Payrollprocessing.class);
        proCrit1.add(Restrictions.sqlRestriction("month=" + month));
        proCrit1.add(Restrictions.sqlRestriction("year=" + year));
        proCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        List proList1 = proCrit1.list();
        if (proList1.size() > 0) {
            payrollprocessingObje = (Payrollprocessing) proList1.get(0);
            if (payrollprocessingObje.getIsopen()) {
                isProcessOpen = true;
            } else {
                isProcessOpen = false;
            }

        }
        if (!isProcessOpen) {
            String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            // int noofworkingdays = 0;
            int noofsalaydays = 0;
            int unpaidsalarydays = 0;
            int noofdayspaidsupp = 0;
            boolean proceed = true;

            //System.out.println("Month = " + month);
            //System.out.println("Year = " + year);
            //System.out.println("no of days = " + noofdays);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
            int totaldaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            //System.out.println("totaldaysInMonth = " + totaldaysInMonth);


            try {
                if (isnewmap.equalsIgnoreCase("1")) {
                    addedMap = (Map) request.getSession().getAttribute("addedMap");
                } else {
                    addedMap = new HashMap();
                }

                Criteria payrollCrit = session.createCriteria(Payrollprocessingdetails.class);
                payrollCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                payrollCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                payrollCrit.add(Restrictions.sqlRestriction("month = '" + month + "' "));
                payrollCrit.add(Restrictions.sqlRestriction("year = '" + year + "' "));
                payrollCrit.add(Restrictions.sqlRestriction("process is true"));
                List payrollList = payrollCrit.list();
                if (payrollList.size() > 0) {
                    Payrollprocessingdetails payrollObj = (Payrollprocessingdetails) payrollList.get(0);
//                noofworkingdays=payrollObj.getWorkingday();
//                noofsalaydays=payrollObj.getSalarydays();
//                unpaidsalarydays=noofworkingdays -noofsalaydays;
                    noofsalaydays = payrollObj.getSalarydays();
                    unpaidsalarydays = payrollObj.getWorkingday() - payrollObj.getSalarydays();
                    //System.out.println("unpaidsalarydays=====" + unpaidsalarydays);
                    if (unpaidsalarydays == 0) {
                        proceed = false;
                    }
                }

                if (proceed) {
                    Criteria suppCrit = session.createCriteria(Supplementatypaybill.class);
                    suppCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                    suppCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    List suppList = suppCrit.list();
                    if (suppList.size() > 0) {
                        for (int i = 0; i < suppList.size(); i++) {
                            Supplementatypaybill suppObj = (Supplementatypaybill) suppList.get(i);
                            Criteria suppProcessCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                            suppProcessCrit.add(Restrictions.sqlRestriction("supplementatypaybillid='" + suppObj.getId() + "'"));
                            suppProcessCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            suppProcessCrit.add(Restrictions.sqlRestriction("calculatedmonth='" + month + "'"));
                            suppProcessCrit.add(Restrictions.sqlRestriction("calculatedyear='" + year + "'"));
                            suppProcessCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List suppProcessList = suppProcessCrit.list();
//                            System.out.println("supplementatypaybillid = " + suppObj.getId());
//                            System.out.println("calculatedmonth = " + month);
//                            System.out.println("calculatedyear = " + year);
//                            System.out.println("suppProcessList.size() = " + suppProcessList.size());
                            if (suppProcessList.size() > 0) {
                                for (int j = 0; j < suppProcessList.size(); j++) {
                                    Supplementarypayrollprocessingdetails suppprocessObj = (Supplementarypayrollprocessingdetails) suppProcessList.get(j);
                                    //System.out.println("suppprocessObj.getNooddayscalculated() = " + suppprocessObj.getNooddayscalculated());
                                    if (suppprocessObj.getNooddayscalculated() == null) {
                                        noofdayspaidsupp = noofdayspaidsupp + 0;
                                    } else {
                                        noofdayspaidsupp = noofdayspaidsupp + suppprocessObj.getNooddayscalculated();
                                    }
                                    //System.out.println("=====" + noofdayspaidsupp);
                                }
                            }
                        }
                        //System.out.println("noofdayspaidsupp=====" + noofdayspaidsupp);


                        if (totaldaysInMonth <= noofdayspaidsupp) {
                            proceed = false;
                        } else {
                            if (unpaidsalarydays != 0) {
                                unpaidsalarydays = unpaidsalarydays - noofdayspaidsupp;
                                if (unpaidsalarydays < 0) {
                                    proceed = false;
                                }
                            }
                        }
                    }
                    if (Integer.parseInt(noofdays) > totaldaysInMonth) {
                        proceed = false;
                        resultMap.put("ERROR", "Given No of days exceeds the " + monthName[Integer.parseInt(month) - 1]);
                    }

                    if ((noofsalaydays + noofdayspaidsupp + Integer.parseInt(noofdays)) > totaldaysInMonth) {
                        proceed = false;
                        resultMap.put("ERROR", "Given No of days exceeds the " + monthName[Integer.parseInt(month) - 1] + ".Because Already " + (noofsalaydays + noofdayspaidsupp) + " Days salary Processed.");
                    }
                    if (proceed) {
//                    System.out.println("-----");
                        if (addedMap == null) {
//                        addMap.put("epfno", epfno);
                            addMap.put("month", month);
                            addMap.put("year", year);
                            addMap.put("noofdays", noofdays);
//                        addMap.put("billdate", billdate);
//                        addMap.put("orderno", orderno);
                            addedMap.put(1, addMap);
                        } else {
                            int addedMapSize = addedMap.size();
//                        addMap.put("epfno", epfno);
                            addMap.put("month", month);
                            addMap.put("year", year);
                            addMap.put("noofdays", noofdays);
//                        addMap.put("billdate", billdate);
//                        addMap.put("orderno", orderno);
                            addedMap.put(addedMapSize + 1, addMap);
                        }

                        request.getSession().setAttribute("addedMap", addedMap);
                        resultMap.put("displayHTML", displayAddedSupplementaryDetailsInHTML(addedMap));
                    }
                } else {
                    resultMap.put("ERROR", "Already Full Month Salary Given to " + epfno + " for the month of " + monthName[Integer.parseInt(month) - 1] + " " + year);
                }


            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("ERROR", "Transaction Faild");
            }

        } else {
            resultMap.put("ERROR", "Pay Bill Process is in Open, Please Close and Continue for this month supplementary");
        }

        return resultMap;
    }

    public String displayAddedSupplementaryDetailsInHTML(Map displayMap) {
        String classname = "";
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int i = 0;
        StringBuffer resultHTML = new StringBuffer();

        try {

            TreeSet<Integer> keys = new TreeSet<Integer>(displayMap.keySet());

            if (displayMap.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td> Month</td>").append("<td> Year</td>").append("<td> No of Days</td>").append("<td>Delete</td>").append("</tr>");

//                    for (Object obj : displayMap.values()) {
                for (Integer objkey : keys) {
//                        Salarydeductionothers sdoObj = (Salarydeductionothers) obj;
                    Map sdoObj = (Map) displayMap.get(objkey);


                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"left\">" + monthName[Integer.parseInt(sdoObj.get("month").toString()) - 1] + "</td>").append("<td align=\"center\">" + sdoObj.get("year") + "</td>").append("<td align=\"center\">" + sdoObj.get("noofdays") + "</td>").append("<td align=\"center\"><input type=\"checkbox\" name=\"noofepno\" id=\"" + objkey + "\" onclick=\"deleteEmployees(this.value);\" value=\"" + objkey + "\"></td>").append("</tr>");
                    i++;

                }
            }

            resultHTML.append("</table>");


        } catch (Exception e) {
            e.printStackTrace();

        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map deleteSupplementaryBillData(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String mapId) {
        Map displayMap = new HashMap();
        Map resultMap = new HashMap();
        String classname = "";
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int i = 0;
        StringBuffer resultHTML = new StringBuffer();
        displayMap = (Map) request.getSession().getAttribute("addedMap");
//        System.out.println("==="+mapId);
        try {
//            System.out.println("displayMap.size()====="+displayMap.size());
            if (displayMap.size() > 0) {
                displayMap.remove(Integer.parseInt(mapId.toString()));
                request.getSession().removeAttribute("addedMap");
                request.getSession().setAttribute("addedMap", displayMap);
            }
//            System.out.println("displayMap.size()====="+displayMap.size());
            TreeSet<Integer> keys = new TreeSet<Integer>(displayMap.keySet());

            if (displayMap.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td> Month</td>").append("<td> Year</td>").append("<td> No of Days</td>").append("<td>Delete</td>").append("</tr>");

//                    for (Object obj : displayMap.values()) {
                for (Integer objkey : keys) {
//                        Salarydeductionothers sdoObj = (Salarydeductionothers) obj;
                    Map sdoObj = (Map) displayMap.get(objkey);


                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"left\">" + monthName[Integer.parseInt(sdoObj.get("month").toString()) - 1] + "</td>").append("<td align=\"center\">" + sdoObj.get("year") + "</td>").append("<td align=\"center\">" + sdoObj.get("noofdays") + "</td>").append("<td align=\"center\"><input type=\"checkbox\" name=\"noofepno\" id=\"" + objkey + "\" onclick=\"deleteEmployees(this.value);\" value=\"" + objkey + "\"></td>").append("</tr>");
                    i++;

                }
            } else {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td> Month</td>").append("<td> Year</td>").append("<td> No of Days</td>").append("<td>Delete</td>").append("</tr>");

            }

            resultHTML.append("</table>");
            resultMap.put("displayHTML", resultHTML.toString());
            resultMap.put("mapsize", displayMap.size());

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction Faild");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveLeaveSurrenderBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String surrenderdays, String orderno, String funtype, String surrenderdate, String designationname) {
        System.out.println("EPF No" + epfno + "As on Date" + asondate + "surrenderdays" + surrenderdays + "Order no " + orderno + "designationname " + designationname);
        Map resultMap = new HashMap();
        Transaction transaction = null;
        String subSalaryStructureId = "";
        Employeemaster employeemasterObj = null;
        String supPayBillId = "";
        Supplementatypaybill supplementatypaybillObj = null;
        boolean proceed = false;
        boolean lastRegion = true;
        try {
            String lastInRegion = "";
            if ("Retired Employee".equalsIgnoreCase(designationname)) {
                lastInRegion = getEmployeeLastSalaryDetail(session, epfno);
                if (!lastInRegion.equalsIgnoreCase(LoggedInRegion)) {
                    lastRegion = false;
                }
            }
            if (lastRegion) {
                if ("Retired Employee".equalsIgnoreCase(designationname)) {
                    employeemasterObj = getEmployeeDetails(session, epfno, "RET");
                } else {
                    employeemasterObj = getEmployeeDetails(session, epfno, LoggedInRegion);
                }
                Criteria subPaybillCrit = session.createCriteria(Supplementatypaybill.class);
                subPaybillCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
                subPaybillCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                subPaybillCrit.add(Restrictions.sqlRestriction("date='" + postgresDate(asondate) + "'"));
                subPaybillCrit.add(Restrictions.sqlRestriction("type='LEAVESURRENDER'"));
                subPaybillCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List subPaybillList = subPaybillCrit.list();

                if (subPaybillList.size() > 0) {
                    // <editor-fold defaultstate="collapsed" desc="Supplementatypaybill Table IF Part">
                    if (funtype.equalsIgnoreCase("modify")) {
                        supplementatypaybillObj = (Supplementatypaybill) subPaybillList.get(0);
                        supPayBillId = supplementatypaybillObj.getId();
                        supplementatypaybillObj.setAccregion(LoggedInRegion);
                        supplementatypaybillObj.setDate(postgresDate(asondate));
                        supplementatypaybillObj.setSldate(postgresDate(surrenderdate));
                        supplementatypaybillObj.setEmployeemaster(employeemasterObj);
                        supplementatypaybillObj.setSection(employeemasterObj.getSection());
                        supplementatypaybillObj.setSubsection(employeemasterObj.getSubsection());
                        supplementatypaybillObj.setPaymentmode(employeemasterObj.getPaymentmode());
                        supplementatypaybillObj.setEmployeecategory(employeemasterObj.getCategory());
                        supplementatypaybillObj.setDesignation(employeemasterObj.getDesignation());
                        supplementatypaybillObj.setCancelled(Boolean.FALSE);
                        supplementatypaybillObj.setCreatedby(LoggedInUser);
                        supplementatypaybillObj.setCreateddate(getCurrentDate());
                        supplementatypaybillObj.setType("LEAVESURRENDER");
                        proceed = true;
                        transaction = session.beginTransaction();
                        session.update(supplementatypaybillObj);
                        transaction.commit();
                    } else {
                        proceed = false;
                    }
                    // </editor-fold>
                } else {
                    // <editor-fold defaultstate="collapsed" desc="Supplementatypaybill Table ELSE Part">
                    supplementatypaybillObj = new Supplementatypaybill();
                    supPayBillId = getSupplementatypaybillid(session, LoggedInRegion);
                    supplementatypaybillObj.setId(supPayBillId);
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setDate(postgresDate(asondate));
                    supplementatypaybillObj.setSldate(postgresDate(surrenderdate));
                    supplementatypaybillObj.setEmployeemaster(employeemasterObj);
                    supplementatypaybillObj.setSection(employeemasterObj.getSection());
                    supplementatypaybillObj.setSubsection(employeemasterObj.getSubsection());
                    supplementatypaybillObj.setPaymentmode(employeemasterObj.getPaymentmode());
                    supplementatypaybillObj.setEmployeecategory(employeemasterObj.getCategory());
                    supplementatypaybillObj.setDesignation(employeemasterObj.getDesignation());
                    supplementatypaybillObj.setType("LEAVESURRENDER");
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setCancelled(Boolean.FALSE);
                    supplementatypaybillObj.setCreatedby(LoggedInUser);
                    supplementatypaybillObj.setCreateddate(getCurrentDate());
                    proceed = true;
                    transaction = session.beginTransaction();
                    session.save(supplementatypaybillObj);
                    transaction.commit();
                    // </editor-fold>
                }

                transaction = session.beginTransaction();
                String updateprocessDetails = "";
                if ("Retired Employee".equalsIgnoreCase(designationname)) {
                    updateprocessDetails = " UPDATE employeemaster SET eslp='" + postgresDate(surrenderdate) + "' where region='RET' and  epfno='" + epfno + "'";
                } else {
                    updateprocessDetails = " UPDATE employeemaster SET eslp='" + postgresDate(surrenderdate) + "' where region='" + LoggedInRegion + "' and  epfno='" + epfno + "'";
                }
                session.createSQLQuery(updateprocessDetails).executeUpdate();
                transaction.commit();

                SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    fm.parse(asondate);
                } catch (ParseException ex) {
                    Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                Calendar cal = fm.getCalendar();
                int iDay = cal.get(Calendar.DATE);
                int iMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
                int iYear = cal.get(Calendar.YEAR);
                //int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
                int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (proceed) {
                    // <editor-fold defaultstate="collapsed" desc="proceed IF Part">
                    //Salary Structure copy
                    String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                    String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                    String queryStr = "select id from salarystructureactual where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + epfno + "' and periodto is null ";
//                String queryStr = "select id from salarystructureactual where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + epfno + "' and "
//                        + " periodfrom<='" + fromDate + "' and "
//                        + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
                    //System.out.println(queryStr);
                    // copy data from salary structure actual to salary structure start
                    List salaryStruActualList = (ArrayList) session.createSQLQuery(queryStr).list();
                    if (salaryStruActualList.size() > 0) {
                        // <editor-fold defaultstate="collapsed" desc="salarystructureactual Table List IF Part">
                        String salaryStructureActualId = (String) salaryStruActualList.get(0);
                        queryStr = "select id from supplementarysalarystructure where accregion='" + LoggedInRegion + "' and supplementatypaybillid ='" + supPayBillId + "'";

                        List subSalaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (subSalaryStruList.size() > 0) {
                            // <editor-fold defaultstate="collapsed" desc="supplementarysalarystructure Table List IF Part">
                            subSalaryStructureId = (String) subSalaryStruList.get(0);
                            // </editor-fold>
                        } else {
                            // <editor-fold defaultstate="collapsed" desc="supplementarysalarystructure Table List IF Part">
                            String fromDateddmmyy = String.valueOf(1) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);
                            String toDateddmmyy = String.valueOf(iLasDay) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);

                            Supplementarysalarystructure supplementarysalarystructureObj = new Supplementarysalarystructure();
                            subSalaryStructureId = getSupplementarysalarystructureid(session, LoggedInRegion);
                            supplementarysalarystructureObj.setId(subSalaryStructureId);
                            supplementarysalarystructureObj.setPeriodfrom(postgresDate(fromDateddmmyy));
                            supplementarysalarystructureObj.setPeriodto(postgresDate(toDateddmmyy));
                            supplementarysalarystructureObj.setSupplementatypaybillid(supPayBillId);

                            supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                            supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                            supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(supplementarysalarystructureObj);
                            transaction.commit();
                            // </editor-fold>

                        }
                        //System.out.println("sairam 1");
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE supplementaryemployeeearningsdetails  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and supplementarysalarystructureid='" + subSalaryStructureId + "'").executeUpdate();
                        transaction.commit();
//                    transaction = session.beginTransaction();
//                    session.createSQLQuery("UPDATE supplementaryemployeedeductiondetails  SET cancelled  = true WHERE supplementarysalarystructureid='" + subSalaryStructureId + "'").executeUpdate();
//                    transaction.commit();

                        //Earnings
                        //System.out.println("sairam 2");
                        Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                        earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureActualId + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("accregion= '" + LoggedInRegion + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid in('E01','E04','E06','E07','E25') "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List earDetailsList = earDetailsCrit.list();

                        if (earDetailsList.size() > 0) {
                            // <editor-fold defaultstate="collapsed" desc="Employeeearningsdetailsactual Table List IF Part">
                            for (int j = 0; j < earDetailsList.size(); j++) {
                                Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);
                                Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + employeeearningsdetailsactualObj.getEarningmasterid() + "' "));
                                List earList = earDe.list();
                                if (earList.size() > 0) {
                                    // <editor-fold defaultstate="collapsed" desc="Supplementaryemployeeearningsdetails Table List IF Part">
                                    Supplementaryemployeeearningsdetails earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());

                                    transaction = session.beginTransaction();
                                    session.update(earningsdetObj);
                                    transaction.commit();
                                    // </editor-fold>
                                } else {
                                    // <editor-fold defaultstate="collapsed" desc="Supplementaryemployeeearningsdetails Table List ELSE Part">
                                    Supplementaryemployeeearningsdetails earningsdetObj = new Supplementaryemployeeearningsdetails();

                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());
                                    earningsdetObj.setEarningmasterid(employeeearningsdetailsactualObj.getEarningmasterid());
                                    earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId, LoggedInRegion));

                                    earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                                    earningsdetObj.setAccregion(LoggedInRegion);

                                    transaction = session.beginTransaction();
                                    session.save(earningsdetObj);
                                    transaction.commit();
                                    // </editor-fold>
                                }
                            }
                            // </editor-fold>
                        }
                        // </editor-fold>
                    }
                    //Salary Structure copy ends here
                    Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj;
                    Criteria supPayBillProcessingCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                    supPayBillProcessingCrit.add(Restrictions.sqlRestriction("supplementatypaybillid='" + supplementatypaybillObj.getId() + "'"));
                    supPayBillProcessingCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    List supPayBillProcessingList = supPayBillProcessingCrit.list();
                    if (supPayBillProcessingList.size() > 0) {
                        // <editor-fold defaultstate="collapsed" desc="Supplementarypayrollprocessingdetails Table List IF Part">
                        supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayBillProcessingList.get(0);
                        supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
                        supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        supplementarypayrollprocessingdetailsObj.setNooddayscalculated(Integer.parseInt(surrenderdays));
                        supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                        supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        session.update(supplementarypayrollprocessingdetailsObj);
                        transaction.commit();
                        // </editor-fold>
                    } else {
                        // <editor-fold defaultstate="collapsed" desc="Supplementarypayrollprocessingdetails Table List ELSE Part">
                        supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();
                        supplementarypayrollprocessingdetailsObj.setId(getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion));
                        supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
                        supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        supplementarypayrollprocessingdetailsObj.setNooddayscalculated(Integer.parseInt(surrenderdays));
                        supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                        supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(supplementarypayrollprocessingdetailsObj);
                        transaction.commit();
                        // </editor-fold>
                    }


                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and  supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'").executeUpdate();
                    transaction.commit();

                    // Earning Starts Here
                    Criteria empEarCrit = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                    empEarCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid='" + subSalaryStructureId + "'"));
                    empEarCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empEarCrit.add(Restrictions.sqlRestriction("earningmasterid in('E01','E04','E06','E07','E25') "));
                    empEarCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List empEarList = empEarCrit.list();
                    if (empEarList.size() > 0) {
                        // <editor-fold defaultstate="collapsed" desc="Supplementaryemployeeearningsdetails Table List IF Part">
                        for (int i = 0; i < empEarList.size(); i++) {
                            Supplementaryemployeeearningsdetails supplementaryemployeeearningsdetailsObje = (Supplementaryemployeeearningsdetails) empEarList.get(i);

                            Criteria empEarTranCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                            empEarTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementaryemployeeearningsdetailsObje.getId() + "'"));
                            empEarTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empEarTranCrit.add(Restrictions.sqlRestriction("earningmasterid='" + supplementaryemployeeearningsdetailsObje.getEarningmasterid() + "'"));
                            List empEarTranList = empEarTranCrit.list();
                            if (empEarTranList.size() > 0) {
                                // <editor-fold defaultstate="collapsed" desc="Supplementaryemployeeearningstransactions Table List IF Part">
                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarTranList.get(0);
                                employeeearningstransactionsObj.setEarningmasterid(supplementaryemployeeearningsdetailsObje.getEarningmasterid());
                                //System.out.println("supplementaryemployeeearningsdetailsObje.getEarningmasterid()" + supplementaryemployeeearningsdetailsObje.getEarningmasterid());
                                employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                if (supplementaryemployeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays30(Integer.parseInt(surrenderdays), supplementaryemployeeearningsdetailsObje.getAmount()));
                                    //System.out.println("");
                                } else {
                                    employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays30(Integer.parseInt(surrenderdays), getEarningsAmount(session, subSalaryStructureId, supplementaryemployeeearningsdetailsObje.getEarningmasterid(), LoggedInRegion, surrenderdate)));
                                }
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.update(employeeearningstransactionsObj);
                                // </editor-fold>
                            } else {
                                // <editor-fold defaultstate="collapsed" desc="Supplementaryemployeeearningstransactions Table List ELSE Part">
                                String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();
                                employeeearningstransactionsObj.setId(earningsTransactionId);
                                employeeearningstransactionsObj.setEarningmasterid(supplementaryemployeeearningsdetailsObje.getEarningmasterid());
                                employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                if (supplementaryemployeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays30(Integer.parseInt(surrenderdays), supplementaryemployeeearningsdetailsObje.getAmount()));
                                } else {
                                    employeeearningstransactionsObj.setAmount(calculateEarningsForGivenDays30(Integer.parseInt(surrenderdays), getEarningsAmount(session, subSalaryStructureId, supplementaryemployeeearningsdetailsObje.getEarningmasterid(), LoggedInRegion, surrenderdate)));
                                }
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeeearningstransactionsObj);
                                // </editor-fold>
                            }
                            transaction.commit();
                        }
                        // </editor-fold>
                    }
                    resultMap.put("success", "Surrender Bill Successfully Saved");
                    // </editor-fold>
                } else {
                    // <editor-fold defaultstate="collapsed" desc="proceed ELSE Part">
                    resultMap.put("ERROR", "This " + epfno.toUpperCase() + " EPF Number is alreay Leaved Surrendered on Today");
                    // </editor-fold>
                }
            } else {
                resultMap.put("ERROR", "This " + epfno.toUpperCase() + " EPF Number is Not in the Logged Region");
            }
            // Dedctions Ends Here
            // Earnings and Deductions Postings Starts
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveIncrementArrearBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String orderno, String startmonth, String startyear, String endmonth, String endyear, String earningCode, String earningAmount, String designationname) {
        String[] earningcodeArr = earningCode.split("TNCSCSEPATOR");
        String[] earningArr = earningAmount.split("TNCSCSEPATOR");
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Employeemaster employeemasterObj = null;
        for (int i = 0; i < earningcodeArr.length; i++) {
            //System.out.println(earningcodeArr[i] + "" + earningArr[i]);
        }
        boolean lastRegion = true;
        int year = Integer.parseInt(startyear);
        int edyear = Integer.parseInt(endyear);
        int edmon = Integer.parseInt(endmonth);
        int mon = Integer.parseInt(startmonth);
        String[] earcodes = earningCode.split("TNCSCSEPATOR");//{"E01", "E03", "E04", "E06", "E07", "E25"};
        Supplementatypaybill supplementatypaybillObj = null;

        try {

            String lastInRegion = "";
            if ("Retired Employee".equalsIgnoreCase(designationname)) {
                lastInRegion = getEmployeeLastSalaryDetail(session, epfno);
                if (!lastInRegion.equalsIgnoreCase(LoggedInRegion)) {
                    lastRegion = false;
                }
            }
            if (lastRegion) {
                Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj;
                Criteria supPayrollCrit = session.createCriteria(Supplementatypaybill.class);
                supPayrollCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                supPayrollCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                supPayrollCrit.add(Restrictions.sqlRestriction("type='INCREMENTARREAR'"));
                supPayrollCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
                //supPayrollCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List supPayrollList = supPayrollCrit.list();
                if (supPayrollList.size() <= 0) {
                    if ("Retired Employee".equalsIgnoreCase(designationname)) {
                        employeemasterObj = getEmployeeDetails(session, epfno, "RET");
                    } else {
                        employeemasterObj = getEmployeeDetails(session, epfno, LoggedInRegion);
                    }
                    supplementatypaybillObj = new Supplementatypaybill();
                    String supPayBillId = getSupplementatypaybillid(session, LoggedInRegion);
                    supplementatypaybillObj.setId(supPayBillId);
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setDate(postgresDate(asondate));
                    supplementatypaybillObj.setEmployeemaster(employeemasterObj);
                    supplementatypaybillObj.setType("INCREMENTARREAR");
                    supplementatypaybillObj.setSection(employeemasterObj.getSection());
                    supplementatypaybillObj.setSubsection(employeemasterObj.getSubsection());
                    supplementatypaybillObj.setPaymentmode(employeemasterObj.getPaymentmode());
                    supplementatypaybillObj.setEmployeecategory(employeemasterObj.getCategory());
                    supplementatypaybillObj.setDesignation(employeemasterObj.getDesignation());
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setCancelled(Boolean.FALSE);
                    supplementatypaybillObj.setCreatedby(LoggedInUser);
                    supplementatypaybillObj.setCreateddate(getCurrentDate());

                    transaction = session.beginTransaction();
                    session.save(supplementatypaybillObj);
                    transaction.commit();

                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE supplementarypayrollprocessingdetails  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and supplementatypaybillid='" + supplementatypaybillObj.getId() + "'").executeUpdate();
                    transaction.commit();
                    boolean cont = true;
                    while (cont) {
                        //Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj;
                        Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("calculatedmonth = " + mon));
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("calculatedyear = " + year));
//                supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "'"));
                        List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();
                        if (supPayProcList.size() > 0) {
                            supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(0);
                            supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                            supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.merge(supplementarypayrollprocessingdetailsObj);
                            transaction.commit();
                        } else {
                            supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();

                            supplementarypayrollprocessingdetailsObj.setId(getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion));
                            supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
                            supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                            supplementarypayrollprocessingdetailsObj.setCalculatedmonth(mon);
                            supplementarypayrollprocessingdetailsObj.setCalculatedyear(year);
                            supplementarypayrollprocessingdetailsObj.setNooddayscalculated(30);
                            supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                            supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());


                            transaction = session.beginTransaction();
                            session.persist(supplementarypayrollprocessingdetailsObj);
                            transaction.commit();
                        }

                        String subSalaryStructureId = "";
                        String queryStr = "select id from supplementarysalarystructure where accregion='" + LoggedInRegion + "' and supplementarypayrollprocessingdetailsid ='" + supplementarypayrollprocessingdetailsObj.getId() + "'";
                        List subSalaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (subSalaryStruList.size() > 0) {
                            subSalaryStructureId = (String) subSalaryStruList.get(0);
                        } else {
                            Supplementarysalarystructure supplementarysalarystructureObj = new Supplementarysalarystructure();
                            subSalaryStructureId = getSupplementarysalarystructureid(session, LoggedInRegion);
                            supplementarysalarystructureObj.setId(subSalaryStructureId);
                            supplementarysalarystructureObj.setSupplementatypaybillid(supplementarypayrollprocessingdetailsObj.getSupplementatypaybill().getId());
                            supplementarysalarystructureObj.setSupplementarypayrollprocessingdetailsid(supplementarypayrollprocessingdetailsObj.getId());
                            supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                            supplementarysalarystructureObj.setCancelled(Boolean.FALSE);
                            supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                            supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(supplementarysalarystructureObj);
                            transaction.commit();
                        }

                        long totalearpaid = 0;
                        // long[] subtotal = {0, 0, 0, 0, 0, 0};

                        for (int k = 0; k < earningArr.length; k++) {

                            Supplementaryemployeeearningsdetails earningsdetObj;
                            Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                            earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                            earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earningcodeArr[k] + "' "));
                            List earList = earDe.list();
                            if (earList.size() > 0) {
                                earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                                earningsdetObj.setCancelled(Boolean.FALSE);
                                earningsdetObj.setAmount(new BigDecimal(earningArr[k]));

                                transaction = session.beginTransaction();
                                session.update(earningsdetObj);
                                transaction.commit();

                            } else {

                                earningsdetObj = new Supplementaryemployeeearningsdetails();

                                earningsdetObj.setCancelled(Boolean.FALSE);
                                earningsdetObj.setAmount(new BigDecimal(earningArr[k]));
                                earningsdetObj.setEarningmasterid(earningcodeArr[k]);
                                earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId, LoggedInRegion));
                                earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                                earningsdetObj.setAccregion(LoggedInRegion);
                                transaction = session.beginTransaction();
                                session.save(earningsdetObj);
                                transaction.commit();

                            }


                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empEarnDetailsList = empEarnDetailsCrit.list();
                            if (empEarnDetailsList.size() > 0) {
                                employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                                if (isAccountedForEpF(session, employeeearningstransactionsObj.getEarningmasterid())) {
                                    totalearpaid = totalearpaid + employeeearningstransactionsObj.getAmount().longValue();
                                }
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                transaction = session.beginTransaction();
                                session.update(employeeearningstransactionsObj);
                                transaction.commit();
                            } else {
                                employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();
                                if (isAccountedForEpF(session, employeeearningstransactionsObj.getEarningmasterid())) {
                                    totalearpaid = totalearpaid + employeeearningstransactionsObj.getAmount().longValue();
                                }

                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                                employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                employeeearningstransactionsObj.setEarningmasterid(earcodes[k]);
                                String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                                employeeearningstransactionsObj.setId(earningsTransactionId);
                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                session.save(employeeearningstransactionsObj);
                                transaction.commit();
                            }


                        }
                        if (isCalculateEpf(session, epfno)) {
//                    if (!employeemasterObj.getDesignation().equalsIgnoreCase("S13") && !employeemasterObj.getDesignation().equalsIgnoreCase("S14")) {


                            Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empDeductDetailsList = empdeductDetailsCrit.list();
                            if (empDeductDetailsList.size() > 0) {
                                Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
//                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, subtotal[p]));
                                employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                transaction = session.beginTransaction();
                                session.update(employeedeductionstransactionsObj);
                                transaction.commit();
                            } else {
                                String deductionTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid("D02");
                                employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
//                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, subtotal[p]));
                                employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                                transaction.commit();
                            }
                        } else {
                            Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empDeductDetailsList = empdeductDetailsCrit.list();
                            if (empDeductDetailsList.size() > 0) {
                                Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
                                employeedeductionstransactionsObj.setAmount(new BigDecimal(0.00));
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.TRUE);
                                transaction = session.beginTransaction();
                                session.update(employeedeductionstransactionsObj);
                                transaction.commit();
                            }

                        }

                        if (mon == 12) {
                            year = year + 1;
                            mon = 0;
                        }
                        mon = mon + 1;
                        if (year == edyear) {
                            if (mon > edmon) {
                                cont = false;
                            }
                        } else {
                            if (year > edyear) {
                                cont = false;
                            }
                        }
                    }

                    year = Integer.parseInt(startyear);
                    edyear = Integer.parseInt(endyear);
                    edmon = Integer.parseInt(endmonth);
                    mon = Integer.parseInt(startmonth);
                    String enddat = edyear + "/" + edmon + "/" + "01";
                    int endDate = getLastDate(enddat);
                    String qry = " select cast(generate_series as date),employeeprovidentfundnumber,id from "
                            + " generate_series('" + year + "-" + mon + "-" + "01" + "',   '" + edyear + "-" + edmon + "-" + endDate + "', cast('1 day' as interval)) generate_series "
                            + " join supplementatypaybill as sp on sp.sldate=generate_series "
                            + " where employeeprovidentfundnumber='" + epfno + "'";

                    //System.out.println("leave surrendar" + qry);
                    SQLQuery misquery = session.createSQLQuery(qry);
                    for (ListIterator it = misquery.list().listIterator(); it.hasNext();) {
                        Object[] row = (Object[]) it.next();
                        String id = (String) row[2];
                        //System.out.println("Sai Leave Surrender id" + id);
                        Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("type = '" + "INCLEAVESURRENDAR" + "'"));
                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("typeid = '" + id + "'"));
                        List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();
                        if (supPayProcList.size() > 0) {
                            supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(0);
                            supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                            supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                            supplementarypayrollprocessingdetailsObj.setType("INCLEAVESURRENDAR");
                            supplementarypayrollprocessingdetailsObj.setTypeid(id);
                            transaction = session.beginTransaction();
                            session.merge(supplementarypayrollprocessingdetailsObj);
                            transaction.commit();
                        } else {
                            supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();

                            supplementarypayrollprocessingdetailsObj.setId(getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion));
                            supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
                            supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                            supplementarypayrollprocessingdetailsObj.setNooddayscalculated(30);
                            supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                            supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                            supplementarypayrollprocessingdetailsObj.setType("INCLEAVESURRENDAR");
                            supplementarypayrollprocessingdetailsObj.setTypeid(id);

                            transaction = session.beginTransaction();
                            session.persist(supplementarypayrollprocessingdetailsObj);
                            transaction.commit();
                        }

                        String subSalaryStructureId = "";
                        Supplementarysalarystructure supplementarysalarystructureObj = null;
                        Criteria supSalStrutCrit = session.createCriteria(Supplementarysalarystructure.class);
                        supSalStrutCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        supSalStrutCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        List<Supplementarysalarystructure> subSalaryStruList = supSalStrutCrit.list();
                        if (subSalaryStruList.size() > 0) {
                            supplementarysalarystructureObj = (Supplementarysalarystructure) subSalaryStruList.get(0);
                            subSalaryStructureId = supplementarysalarystructureObj.getId();
                            supplementarysalarystructureObj.setCancelled(Boolean.FALSE);
                            supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                            supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(supplementarysalarystructureObj);
                            transaction.commit();
                        } else {
                            supplementarysalarystructureObj = new Supplementarysalarystructure();
                            subSalaryStructureId = getSupplementarysalarystructureid(session, LoggedInRegion);
                            supplementarysalarystructureObj.setId(subSalaryStructureId);
                            supplementarysalarystructureObj.setSupplementatypaybillid(supplementarypayrollprocessingdetailsObj.getSupplementatypaybill().getId());
                            supplementarysalarystructureObj.setSupplementarypayrollprocessingdetailsid(supplementarypayrollprocessingdetailsObj.getId());
                            supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                            supplementarysalarystructureObj.setCancelled(Boolean.FALSE);
                            supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                            supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(supplementarysalarystructureObj);
                            transaction.commit();
                        }

                        for (int k = 0; k < earningArr.length; k++) {

                            Supplementaryemployeeearningsdetails earningsdetObj;
                            Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                            earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                            earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earningcodeArr[k] + "' "));
                            List earList = earDe.list();
                            if (earList.size() > 0) {
                                earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                                earningsdetObj.setCancelled(Boolean.FALSE);
                                transaction = session.beginTransaction();
                                session.update(earningsdetObj);
                                transaction.commit();
                            } else {
                                earningsdetObj = new Supplementaryemployeeearningsdetails();
                                earningsdetObj.setCancelled(Boolean.FALSE);
                                earningsdetObj.setAmount(new BigDecimal(0));
                                earningsdetObj.setEarningmasterid(earningcodeArr[k].toString());
                                earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId, LoggedInRegion));
                                earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                                earningsdetObj.setAccregion(LoggedInRegion);
                                transaction = session.beginTransaction();
                                session.save(earningsdetObj);
                                transaction.commit();

                            }


                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earningcodeArr[k].toString() + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empEarnDetailsList = empEarnDetailsCrit.list();
                            if (empEarnDetailsList.size() > 0) {
                                employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                transaction = session.beginTransaction();
                                session.update(employeeearningstransactionsObj);
                                transaction.commit();
                            } else {
                                employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                                employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                employeeearningstransactionsObj.setEarningmasterid(earningcodeArr[k].toString());
                                String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                                employeeearningstransactionsObj.setId(earningsTransactionId);
                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                session.save(employeeearningstransactionsObj);
                                transaction.commit();
                            }

                        }

                    }

                } else {
                    supplementatypaybillObj = (Supplementatypaybill) supPayrollList.get(0);
                }
                resultMap.put("billno", supplementatypaybillObj.getId());
                resultMap.put("incrementhtml", getIncrementArrearDetails(null, request, response, null, null, epfno, asondate, supplementatypaybillObj.getId(), earcodes).get("incrementhtml"));
            } else {
                resultMap.put("ERROR", "This " + epfno.toUpperCase() + " EPF Number is Not in the Logged Region");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map ModifyIncrementArrearBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String orderno, String startmonth, String startyear, String endmonth, String endyear, String billno) {
        Map resultMap = new HashMap();
        int year = Integer.parseInt(startyear);
        int edyear = Integer.parseInt(endyear);
        int edmon = Integer.parseInt(endmonth);
        int mon = Integer.parseInt(startmonth);
        int yea = Integer.parseInt(startyear);
        Transaction transaction = null;

        try {
            String eardet = " select earningmasterid from supplementaryemployeeearningsdetails eed "
                    + " left join supplementarysalarystructure sst on sst.id=eed.supplementarysalarystructureid "
                    + " left join supplementarypayrollprocessingdetails sppd on sppd.id=sst.supplementarypayrollprocessingdetailsid  "
                    + " left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid where spb.id='" + billno + "' group by earningmasterid ";

            SQLQuery subpayquery = session.createSQLQuery(eardet);
            //System.out.println("size" + subpayquery.list().size());
            Vector earningcodeArr = new Vector(0);
            String[] errcode = new String[subpayquery.list().size()];
            int i = 0;
            for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                String earningmasterid = (String) its.next();
                earningcodeArr.add(earningmasterid);
                errcode[i] = earningmasterid;
                i++;
            }


            Supplementatypaybill supplementatypaybillObj = null;
            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj;
            Criteria supPayrollCrit = session.createCriteria(Supplementatypaybill.class);
            supPayrollCrit.add(Restrictions.sqlRestriction("id = '" + billno + "' "));
            supPayrollCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            supPayrollCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            supPayrollCrit.add(Restrictions.sqlRestriction("type='INCREMENTARREAR'"));
            supPayrollCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
            List supPayrollList = supPayrollCrit.list();
            if (supPayrollList.size() > 0) {
                supplementatypaybillObj = (Supplementatypaybill) supPayrollList.get(0);

                /*transaction = session.beginTransaction();
                session.createSQLQuery("UPDATE supplementarypayrollprocessingdetails  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and supplementatypaybillid='" + supplementatypaybillObj.getId() + "'").executeUpdate();
                transaction.commit();
                
                String eetqry = "UPDATE supplementaryemployeeearningstransactions  SET cancelled=true  WHERE accregion='" + LoggedInRegion + "' and id in ("
                + " select seet.id from supplementaryemployeeearningstransactions seet "
                + " left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                + " left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                + " where spb.id='" + supplementatypaybillObj.getId() + "' );";
                
                transaction = session.beginTransaction();
                session.createSQLQuery(eetqry).executeUpdate();
                transaction.commit();
                
                String edtqry = " UPDATE supplementaryemployeedeductionstransactions SET cancelled=true WHERE accregion='" + LoggedInRegion + "' and id in ( "
                + " select sedt.id from supplementaryemployeedeductionstransactions sedt "
                + " left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid "
                + " left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                + " where spb.id='" + supplementatypaybillObj.getId() + "' );";
                
                transaction = session.beginTransaction();
                session.createSQLQuery(edtqry).executeUpdate();
                transaction.commit();
                
                String ssqry = " UPDATE supplementarysalarystructure  SET cancelled=true  WHERE accregion='" + LoggedInRegion + "' and id in ( "
                + " select sst.id from supplementarysalarystructure sst "
                + " left join supplementarypayrollprocessingdetails sppd on sppd.id=sst.supplementarypayrollprocessingdetailsid "
                + " left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid where spb.id='" + supplementatypaybillObj.getId() + "' );";
                
                
                transaction = session.beginTransaction();
                session.createSQLQuery(ssqry).executeUpdate();
                transaction.commit();
                
                String seeeqry = " UPDATE supplementaryemployeeearningsdetails SET cancelled=true WHERE accregion='" + LoggedInRegion + "' and id in ( "
                + " select eed.id from supplementaryemployeeearningsdetails eed "
                + " left join supplementarysalarystructure sst on sst.id=eed.supplementarysalarystructureid "
                + " left join supplementarypayrollprocessingdetails sppd on sppd.id=sst.supplementarypayrollprocessingdetailsid "
                + " left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid where spb.id='" + supplementatypaybillObj.getId() + "' );";
                
                transaction = session.beginTransaction();
                session.createSQLQuery(seeeqry).executeUpdate();
                transaction.commit();
                
                String seedqry = " UPDATE supplementaryemployeedeductiondetails SET cancelled=true  WHERE accregion='" + LoggedInRegion + "' and id in ( "
                + " select eed.id from supplementaryemployeedeductiondetails eed "
                + " left join supplementarysalarystructure sst on sst.id=eed.supplementarysalarystructureid "
                + " left join supplementarypayrollprocessingdetails sppd on sppd.id=sst.supplementarypayrollprocessingdetailsid "
                + " left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                + " where spb.id='" + supplementatypaybillObj.getId() + "');";
                
                transaction = session.beginTransaction();
                session.createSQLQuery(seedqry).executeUpdate();
                transaction.commit();*/

                boolean cont = true;
                while (cont) {
                    Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("calculatedmonth = " + mon));
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("calculatedyear = " + year));
                    List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();
                    if (supPayProcList.size() > 0) {
                        supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(0);
                        supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                        supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.merge(supplementarypayrollprocessingdetailsObj);
                        transaction.commit();
                    } else {
                        supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();

                        supplementarypayrollprocessingdetailsObj.setId(getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion));
                        supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
                        supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        supplementarypayrollprocessingdetailsObj.setCalculatedmonth(mon);
                        supplementarypayrollprocessingdetailsObj.setCalculatedyear(year);
                        supplementarypayrollprocessingdetailsObj.setNooddayscalculated(30);
                        supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                        supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());


                        transaction = session.beginTransaction();
                        session.persist(supplementarypayrollprocessingdetailsObj);
                        transaction.commit();
                    }

                    String subSalaryStructureId = "";
                    Supplementarysalarystructure supplementarysalarystructureObj = null;
                    Criteria supSalStrutCrit = session.createCriteria(Supplementarysalarystructure.class);
                    supSalStrutCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    supSalStrutCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                    List<Supplementarysalarystructure> subSalaryStruList = supSalStrutCrit.list();
                    if (subSalaryStruList.size() > 0) {
                        supplementarysalarystructureObj = (Supplementarysalarystructure) subSalaryStruList.get(0);
                        subSalaryStructureId = supplementarysalarystructureObj.getId();
                        supplementarysalarystructureObj.setCancelled(Boolean.FALSE);
                        supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                        supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.update(supplementarysalarystructureObj);
                        transaction.commit();
                    } else {
                        supplementarysalarystructureObj = new Supplementarysalarystructure();
                        subSalaryStructureId = getSupplementarysalarystructureid(session, LoggedInRegion);
                        supplementarysalarystructureObj.setId(subSalaryStructureId);
                        supplementarysalarystructureObj.setSupplementatypaybillid(supplementarypayrollprocessingdetailsObj.getSupplementatypaybill().getId());
                        supplementarysalarystructureObj.setSupplementarypayrollprocessingdetailsid(supplementarypayrollprocessingdetailsObj.getId());
                        supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                        supplementarysalarystructureObj.setCancelled(Boolean.FALSE);
                        supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                        supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(supplementarysalarystructureObj);
                        transaction.commit();
                    }
                    long totalearpaid = 0;
                    for (int k = 0; k < earningcodeArr.size(); k++) {

                        Supplementaryemployeeearningsdetails earningsdetObj;
                        Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                        earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                        earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earningcodeArr.get(k) + "' "));
                        List earList = earDe.list();
                        if (earList.size() > 0) {
                            earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                            earningsdetObj.setCancelled(Boolean.FALSE);
                            //earningsdetObj.setAmount(new BigDecimal(0));
                            transaction = session.beginTransaction();
                            session.update(earningsdetObj);
                            transaction.commit();

                        } else {

                            earningsdetObj = new Supplementaryemployeeearningsdetails();
                            earningsdetObj.setCancelled(Boolean.FALSE);
                            earningsdetObj.setAmount(new BigDecimal(0));
                            earningsdetObj.setEarningmasterid(earningcodeArr.get(k).toString());
                            earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId, LoggedInRegion));
                            earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                            earningsdetObj.setAccregion(LoggedInRegion);
                            transaction = session.beginTransaction();
                            session.save(earningsdetObj);
                            transaction.commit();

                        }


                        Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
                        Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earningcodeArr.get(k).toString() + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empEarnDetailsList = empEarnDetailsCrit.list();
                        if (empEarnDetailsList.size() > 0) {
                            employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                            //employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                            employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                            employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                            employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                            transaction = session.beginTransaction();
                            session.update(employeeearningstransactionsObj);
                            transaction.commit();
                        } else {
                            employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();
                            employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                            employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                            employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                            employeeearningstransactionsObj.setEarningmasterid(earningcodeArr.get(k).toString());
                            String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                            employeeearningstransactionsObj.setId(earningsTransactionId);
                            employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                            employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                            employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                            transaction = session.beginTransaction();
                            session.save(employeeearningstransactionsObj);
                            transaction.commit();
                        }


                    }

                    if (isCalculateEpf(session, epfno)) {
                        Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empDeductDetailsList = empdeductDetailsCrit.list();
                        if (empDeductDetailsList.size() > 0) {
                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
                            employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(employeedeductionstransactionsObj);
                            transaction.commit();
                        } else {
                            String deductionTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid("D02");
                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                            employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(employeedeductionstransactionsObj);
                            transaction.commit();
                        }
                    } else {
                        Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empDeductDetailsList = empdeductDetailsCrit.list();
                        if (empDeductDetailsList.size() > 0) {
                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
                            employeedeductionstransactionsObj.setAmount(new BigDecimal(0.00));
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setCancelled(Boolean.TRUE);
                            transaction = session.beginTransaction();
                            session.update(employeedeductionstransactionsObj);
                            transaction.commit();
                        }

                    }


                    if (mon == 12) {
                        year = year + 1;
                        mon = 0;
                    }
                    mon = mon + 1;
                    if (year == edyear) {
                        if (mon > edmon) {
                            cont = false;
                        }
                    } else {
                        if (year > edyear) {
                            cont = false;
                        }
                    }
                }

                year = Integer.parseInt(startyear);
                edyear = Integer.parseInt(endyear);
                edmon = Integer.parseInt(endmonth);
                mon = Integer.parseInt(startmonth);
                yea = Integer.parseInt(startyear);
                String enddat = edyear + "/" + edmon + "/" + "01";
                //System.out.println("end date of month" + enddat);
                int endDate = getLastDate(enddat);
                String qry = " select cast(generate_series as date),employeeprovidentfundnumber,id from "
                        + " generate_series('" + year + "-" + mon + "-" + "01" + "',   '" + edyear + "-" + edmon + "-" + endDate + "', cast('1 day' as interval)) generate_series "
                        + " join supplementatypaybill as sp on sp.sldate=generate_series "
                        + " where employeeprovidentfundnumber='" + epfno + "'";

                //System.out.println("leave surrendar" + qry);
                SQLQuery misquery = session.createSQLQuery(qry);
                for (ListIterator it = misquery.list().listIterator(); it.hasNext();) {
                    Object[] row = (Object[]) it.next();
                    String id = (String) row[2];
                    //System.out.println("Sai Leave Surrender id" + id);
                    Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("type = '" + "INCLEAVESURRENDAR" + "'"));
                    supPayrollProcesCrit.add(Restrictions.sqlRestriction("typeid = '" + id + "'"));
                    List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();
                    if (supPayProcList.size() > 0) {
                        supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(0);
                        supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                        supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        supplementarypayrollprocessingdetailsObj.setType("INCLEAVESURRENDAR");
                        supplementarypayrollprocessingdetailsObj.setTypeid(id);
                        transaction = session.beginTransaction();
                        session.merge(supplementarypayrollprocessingdetailsObj);
                        transaction.commit();
                    } else {
                        supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();

                        supplementarypayrollprocessingdetailsObj.setId(getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion));
                        supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
                        supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        supplementarypayrollprocessingdetailsObj.setNooddayscalculated(30);
                        supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);
                        supplementarypayrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        supplementarypayrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        supplementarypayrollprocessingdetailsObj.setType("INCLEAVESURRENDAR");
                        supplementarypayrollprocessingdetailsObj.setTypeid(id);

                        transaction = session.beginTransaction();
                        session.persist(supplementarypayrollprocessingdetailsObj);
                        transaction.commit();
                    }

                    String subSalaryStructureId = "";
                    Supplementarysalarystructure supplementarysalarystructureObj = null;
                    Criteria supSalStrutCrit = session.createCriteria(Supplementarysalarystructure.class);
                    supSalStrutCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    supSalStrutCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                    List<Supplementarysalarystructure> subSalaryStruList = supSalStrutCrit.list();
                    if (subSalaryStruList.size() > 0) {
                        supplementarysalarystructureObj = (Supplementarysalarystructure) subSalaryStruList.get(0);
                        subSalaryStructureId = supplementarysalarystructureObj.getId();
                        supplementarysalarystructureObj.setCancelled(Boolean.FALSE);
                        supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                        supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.update(supplementarysalarystructureObj);
                        transaction.commit();
                    } else {
                        supplementarysalarystructureObj = new Supplementarysalarystructure();
                        subSalaryStructureId = getSupplementarysalarystructureid(session, LoggedInRegion);
                        supplementarysalarystructureObj.setId(subSalaryStructureId);
                        supplementarysalarystructureObj.setSupplementatypaybillid(supplementarypayrollprocessingdetailsObj.getSupplementatypaybill().getId());
                        supplementarysalarystructureObj.setSupplementarypayrollprocessingdetailsid(supplementarypayrollprocessingdetailsObj.getId());
                        supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                        supplementarysalarystructureObj.setCancelled(Boolean.FALSE);
                        supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                        supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(supplementarysalarystructureObj);
                        transaction.commit();
                    }

                    for (int k = 0; k < earningcodeArr.size(); k++) {

                        Supplementaryemployeeearningsdetails earningsdetObj;
                        Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                        earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                        earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earningcodeArr.get(k) + "' "));
                        List earList = earDe.list();
                        if (earList.size() > 0) {
                            earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                            earningsdetObj.setCancelled(Boolean.FALSE);
                            transaction = session.beginTransaction();
                            session.update(earningsdetObj);
                            transaction.commit();
                        } else {
                            earningsdetObj = new Supplementaryemployeeearningsdetails();
                            earningsdetObj.setCancelled(Boolean.FALSE);
                            earningsdetObj.setAmount(new BigDecimal(0));
                            earningsdetObj.setEarningmasterid(earningcodeArr.get(k).toString());
                            earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId, LoggedInRegion));
                            earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                            earningsdetObj.setAccregion(LoggedInRegion);
                            transaction = session.beginTransaction();
                            session.save(earningsdetObj);
                            transaction.commit();

                        }


                        Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
                        Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earningcodeArr.get(k).toString() + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empEarnDetailsList = empEarnDetailsCrit.list();
                        if (empEarnDetailsList.size() > 0) {
                            employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                            employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                            employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                            employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                            employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                            transaction = session.beginTransaction();
                            session.update(employeeearningstransactionsObj);
                            transaction.commit();
                        } else {
                            employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();
                            employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                            employeeearningstransactionsObj.setAmount(new BigDecimal(0));
                            employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                            employeeearningstransactionsObj.setEarningmasterid(earningcodeArr.get(k).toString());
                            String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                            employeeearningstransactionsObj.setId(earningsTransactionId);
                            employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                            employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                            employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                            transaction = session.beginTransaction();
                            session.save(employeeearningstransactionsObj);
                            transaction.commit();
                        }

                    }

                }
                resultMap.put("incrementhtml", getIncrementArrearDetails(null, request, response, null, null, epfno, asondate, supplementatypaybillObj.getId(), errcode).get("incrementhtml"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return resultMap;
    }

    public boolean isCalculateEpf(Session session, String epfno) {
        boolean stat = false;
        String queryStr = "select edda.amount from employeedeductiondetailsactual edda "
                + " left join salarystructureactual ssa on ssa.id=edda.salarystructureactualid  "
                + " where ssa.periodto is null and cancelled is false and ssa.employeeprovidentfundnumber='" + epfno + "' and deductionmasterid='D02'";
        List subSalaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (subSalaryStruList.size() > 0) {
            stat = true;
        } else {
            stat = false;
        }

        return stat;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getIncrementArrearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String billno, String[] earcodess) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Supplementatypaybill supplementatypaybillObj = null;
        String[] earcodes = {"E01", "E02", "E04", "E06", "E07", "E25"};
//        long[] total = {0, 0, 0, 0, 0, 0};
        long[] subtotal = {0, 0, 0, 0, 0, 0};
        String subvec = "";
        try {
            Criteria supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
            supPayBillCrit.add(Restrictions.sqlRestriction("id = '" + billno + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("type='INCREMENTARREAR'"));
            supPayBillCrit.add(Restrictions.sqlRestriction("cancelled is false"));

            List<Supplementatypaybill> supPayBillList = supPayBillCrit.list();
            if (supPayBillList.size() > 0) {
                // <editor-fold defaultstate="collapsed">
                supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(0);
                StringBuffer resultHTML = new StringBuffer();
                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                resultHTML.append("<tr class=\"gridmenu\">");
                resultHTML.append("<td align=\"right\" >");
                resultHTML.append("Particulars");
                resultHTML.append("</td>");
                for (int k = 0; k < earcodes.length; k++) {
                    resultHTML.append("<td align=\"right\" >");
                    resultHTML.append(getPaycodeMater(session, earcodes[k]).getPaycodename());
                    resultHTML.append("</td>");
                }
                resultHTML.append("<td align=\"right\" >");
                resultHTML.append("Balance");
                resultHTML.append("</td>");
                resultHTML.append("</tr>");

                Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = null;
                Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                supPayrollProcesCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
                supPayrollProcesCrit.addOrder(Order.asc("calculatedyear"));
                supPayrollProcesCrit.addOrder(Order.asc("calculatedmonth"));
                List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();
                if (supPayProcList.size() > 0) {
                    for (int i = 0; i < supPayProcList.size(); i++) {
                        // <editor-fold defaultstate="collapsed">
                        for (int p = 0; p < subtotal.length; p++) {
                            subtotal[p] = 0;
                        }
                        supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(i);
                        resultHTML.append("<tr class=\"gridmenu\">");
                        resultHTML.append("<td colspan=\"" + (earcodes.length + 1) + "\" align=\"left\" >");
                        //System.out.println(":::::::::::::::: " + supplementarypayrollprocessingdetailsObj.getType());
                        if (supplementarypayrollprocessingdetailsObj.getType() != null) {
                            if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                resultHTML.append(supplementarypayrollprocessingdetailsObj.getType());
                            } else {
                                resultHTML.append(supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                            }
                        } else {
                            resultHTML.append(supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                        }
                        resultHTML.append("</td>");
                        resultHTML.append("<td>");
                        resultHTML.append(" <input type=\"button\" CLASS=\"submitbu\" name=\"adddrawn\" id=\"adddrawn\" value=\"+ Drawn\"  onclick=\"showdrwandetailsforupdation('" + epfno + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "');\"  >");
                        resultHTML.append("</td>");
                        resultHTML.append("</tr>");

                        Supplementarysalarystructure supplementarysalarystructureObj;
                        Criteria supSalStruCrit = session.createCriteria(Supplementarysalarystructure.class);
                        supSalStruCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));
                        supSalStruCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        List<Supplementarysalarystructure> supSalStruList = supSalStruCrit.list();
                        if (supSalStruList.size() > 0) {
                            // <editor-fold defaultstate="collapsed">
                            supplementarysalarystructureObj = (Supplementarysalarystructure) supSalStruList.get(0);
                            resultHTML.append("<tr >");
                            resultHTML.append("<td align=\"left\" >");
                            resultHTML.append("Due");
                            resultHTML.append("</td>");

                            for (int k = 0; k < earcodes.length; k++) {
                                // <editor-fold defaultstate="collapsed">
                                Supplementaryemployeeearningsdetails earningsdetObj;
                                Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + supplementarysalarystructureObj.getId() + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earcodes[k] + "' "));
                                earDe.add(Restrictions.sqlRestriction("cancelled is false"));
                                List<Supplementaryemployeeearningsdetails> earList = earDe.list();
                                if (earList.size() > 0) {
                                    earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                                    resultHTML.append("<td align=\"right\" >");
                                    resultHTML.append(earningsdetObj.getAmount().setScale(2));
                                    subtotal[k] = subtotal[k] + (long) earningsdetObj.getAmount().floatValue();
                                    resultHTML.append("</td>");
                                } else {
                                    resultHTML.append("<td align=\"right\" >");
                                    resultHTML.append("0.00");
                                    resultHTML.append("</td>");
                                }
                                // </editor-fold>
                            }
                            resultHTML.append("<td>");
                            resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showDueDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + supplementarysalarystructureObj.getId() + "')\">");
                            resultHTML.append("</td>");
                            resultHTML.append("</tr>");



//                            if (supplementarypayrollprocessingdetailsObj.getType() == null && !supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR") ) {
                            if (supplementarypayrollprocessingdetailsObj.getType() == null) {
                                // <editor-fold defaultstate="collapsed">
                                //Regular
                                Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("process is true"));
                                List empPayProcessDetails = empPayProcessDetailsCrit.list();
                                if (empPayProcessDetails.size() > 0) {
                                    resultHTML.append("<tr>");
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("Drawn Regular");
                                    resultHTML.append("</td>");

                                    for (int k = 0; k < earcodes.length; k++) {
                                        // <editor-fold defaultstate="collapsed">
                                        Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(0);
                                        Criteria empEarnDetailsCrit = session.createCriteria(Employeeearningstransactions.class);
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                        List empEarnDetailsList = empEarnDetailsCrit.list();
                                        if (empEarnDetailsList.size() > 0) {

                                            for (int j = 0; j < empEarnDetailsList.size(); j++) {
                                                Employeeearningstransactions employeeearningstransactionsObj = (Employeeearningstransactions) empEarnDetailsList.get(j);

                                                resultHTML.append("<td align=\"right\" >");
                                                resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                                //System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                                resultHTML.append("</td>");

                                            }

                                        } else {
                                            resultHTML.append("<td align=\"right\" >");
                                            resultHTML.append("0.00");
                                            resultHTML.append("</td>");
                                        }
                                        // </editor-fold>
                                    }
                                    resultHTML.append("<td align=\"right\" >");
                                    resultHTML.append("</td>");
                                    resultHTML.append("</tr>");
                                }

                                //Regular end
                                //Supplementaty
                                StringBuffer query = new StringBuffer();
                                query.append("select sppd.id as sppod, spb.type as spbtype, sppd.type as sppdtype, spb.id as spbid from supplementarypayrollprocessingdetails sppd ");
                                query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                                query.append(" where sppd.accregion='" + LoggedInRegion + "' and sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "'  and spb.cancelled is false and sppd.cancelled is false");
                                SQLQuery subpayquery = session.createSQLQuery(query.toString());
                                //System.out.println("query.toString() = " + query.toString());
                                //System.out.println("size" + subpayquery.list().size());
                                for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                                    // <editor-fold defaultstate="collapsed">
                                    Object[] rows = (Object[]) its.next();
                                    String payprocesid = (String) rows[0];
                                    String type = (String) rows[1];
                                    String type2 = (String) rows[2];
                                    String supid = (String) rows[3];

                                    //System.out.println(payprocesid + type);
                                    //System.out.println(supid);
                                    resultHTML.append("<tr>");
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("Drawn" + type + "  " + payprocesid + "  " + supid);
                                    resultHTML.append("</td>");

                                    for (int k = 0; k < earcodes.length; k++) {
                                        // <editor-fold defaultstate="collapsed">
                                        //System.out.println(earcodes[k]);
                                        Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                        List empEarnDetailsList = empEarnDetailsCrit.list();
                                        if (empEarnDetailsList.size() > 0) {
                                            // <editor-fold defaultstate="collapsed">
                                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                            resultHTML.append("<td align=\"right\" >");
                                            if (!type.equalsIgnoreCase("DAARREAR")) {
                                                resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                            } else {
                                                // <editor-fold defaultstate="collapsed">
                                                if (subvec.trim().length() > 0) {
                                                    subvec = subvec + "," + "'" + supid + "'";
                                                } else {
                                                    subvec = subvec + "'" + supid + "'";
                                                }
                                                //System.out.println("subvec = " + subvec);
                                                float datott = 0;
                                                Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                                empIncCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supid + "'"));
                                                //empIncCrit.add(Restrictions.sqlRestriction("processid='" + payprocesid + "'"));
                                                empIncCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                                                empIncCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                                                List incArList = empIncCrit.list();
                                                if (incArList.size() > 0) {
                                                    for (int sr = 0; sr < incArList.size(); sr++) {
                                                        // <editor-fold defaultstate="collapsed">
                                                        Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                        if (!incrementarrearreferenceObj.getType().equalsIgnoreCase("LEAVESURRENDER")) {
                                                            //System.out.println("incrementarrearreferenceObj.getArrear().floatValue() = " + incrementarrearreferenceObj.getArrear().floatValue());
                                                            datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
                                                        } else {
                                                        }
                                                        // </editor-fold>
                                                    }
                                                }
                                                resultHTML.append(datott);
                                                subtotal[k] = subtotal[k] - (long) datott;
                                                // </editor-fold>
                                            }
                                            //System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                            resultHTML.append("</td>");
                                            // </editor-fold>
                                        } else {
                                            resultHTML.append("<td align=\"right\" > ");
                                            resultHTML.append("0.00");
                                            resultHTML.append("</td>");
                                        }
                                        // </editor-fold>

                                    }
                                    resultHTML.append("<td align=\"left\" >");
                                    if (type.equalsIgnoreCase("INCREMENTMANUAL")) {
                                        resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showManIncDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + payprocesid + "')\">");
                                    }
                                    resultHTML.append("</td>");
                                    resultHTML.append("</tr>");
                                    // </editor-fold>
                                }
                                //Supplementaty end
                                // </editor-fold>

                            } else {
                                // <editor-fold defaultstate="collapsed">
                                if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                    // <editor-fold defaultstate="collapsed">
                                    resultHTML.append("<tr>");
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("Drawn");
                                    resultHTML.append("</td>");
                                    supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
                                    supPayBillCrit.add(Restrictions.sqlRestriction("id = '" + supplementarypayrollprocessingdetailsObj.getTypeid() + "' "));
                                    supPayBillList = supPayBillCrit.list();
                                    if (supPayBillList.size() > 0) {
                                        // <editor-fold>
                                        supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(0);

                                        supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                        supPayrollProcesCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
                                        supPayProcList = supPayrollProcesCrit.list();
                                        if (supPayProcList.size() > 0) {

                                            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj1 = (Supplementarypayrollprocessingdetails) supPayProcList.get(0);

                                            for (int k = 0; k < earcodes.length; k++) {
                                                // <editor-fold defaultstate="collapsed">
                                                //System.out.println(earcodes[k]);
                                                Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj1.getId() + "'"));
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                                List empEarnDetailsList = empEarnDetailsCrit.list();
                                                if (empEarnDetailsList.size() > 0) {
                                                    Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);

                                                    resultHTML.append("<td align=\"right\" >");
                                                    resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                    subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                                    //System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                                    resultHTML.append("</td>");
                                                } else {
                                                    resultHTML.append("<td align=\"right\" > ");
                                                    resultHTML.append("0.00");
                                                    resultHTML.append("</td>");
                                                }
                                                // </editor-fold>
                                            }
                                        }
                                        // </editor-fold>
                                    }
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("</td>");
                                    resultHTML.append("</tr>");
                                    //sub inc

                                    if (subvec.trim().length() > 0) {
                                        // <editor-fold defaultstate="collapsed">
                                        float datott = 0;
                                        Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                        empIncCrit.add(Restrictions.sqlRestriction("suppaybillid in(" + subvec + ")"));
                                        empIncCrit.add(Restrictions.sqlRestriction("type='" + "LEAVESURRENDER" + "'"));
                                        List incArList = empIncCrit.list();
                                        if (incArList.size() > 0) {
                                            for (int sr = 0; sr < incArList.size(); sr++) {
                                                // <editor-fold defaultstate="collapsed">
                                                resultHTML.append("<tr>");
                                                resultHTML.append("<td align=\"left\" >");
                                                resultHTML.append("Arrear Paid");
                                                resultHTML.append("</td>");
                                                for (int k1 = 0; k1 < earcodes.length; k1++) {
                                                    // <editor-fold defaultstate="collapsed">
                                                    if (earcodes[k1].equalsIgnoreCase("E04")) {
                                                        resultHTML.append("<td align=\"right\" >");
                                                        Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                        datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
                                                        resultHTML.append(datott);
                                                        subtotal[k1] = subtotal[k1] - (long) datott;
                                                        resultHTML.append("</td>");
                                                    } else {
                                                        resultHTML.append("<td align=\"left\" >");
                                                        resultHTML.append("</td>");
                                                    }
                                                    // </editor-fold>
                                                }
                                                resultHTML.append("<td align=\"left\" >");
                                                resultHTML.append("</td>");
                                                resultHTML.append("</tr>");
                                                // </editor-fold>
                                            }
                                        }
                                        // </editor-fold>
                                    }
                                    // </editor-fold>
                                }

                                if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                    //System.out.println("--------------------------------------------------");
                                    StringBuffer query = new StringBuffer();
                                    query.append("select sppd.id as sppod, spb.type as spbtype, sppd.type as sppdtype, spb.id as spbid from supplementarypayrollprocessingdetails sppd ");
                                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                                    query.append(" where sppd.accregion='" + LoggedInRegion + "' and spb.type='SLINCREMENTMANUAL'  and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "'  and spb.cancelled is false and sppd.cancelled is false");
                                    SQLQuery subpayquery = session.createSQLQuery(query.toString());
                                    //System.out.println(query.toString());
                                    //System.out.println("size" + subpayquery.list().size());
                                    for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                                        // <editor-fold defaultstate="collapsed">
                                        Object[] rows = (Object[]) its.next();
                                        String payprocesid = (String) rows[0];
                                        String type = (String) rows[1];
                                        String type2 = (String) rows[2];
                                        String supid = (String) rows[3];

                                        //System.out.println(payprocesid + type);
                                        //System.out.println(supid);
                                        resultHTML.append("<tr>");
                                        resultHTML.append("<td align=\"left\" >");
                                        resultHTML.append("Drawn" + type);
                                        resultHTML.append("</td>");

                                        for (int k = 0; k < earcodes.length; k++) {
                                            // <editor-fold defaultstate="collapsed">
                                            //System.out.println(earcodes[k]);
                                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List empEarnDetailsList = empEarnDetailsCrit.list();
                                            if (empEarnDetailsList.size() > 0) {
                                                // <editor-fold defaultstate="collapsed">
                                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                                resultHTML.append("<td align=\"right\" >");
                                                if (!type.equalsIgnoreCase("DAARREAR")) {
                                                    resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                    subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                                } else {
                                                    // <editor-fold defaultstate="collapsed">
                                                    if (subvec.trim().length() > 0) {
                                                        subvec = subvec + "," + "'" + supid + "'";
                                                    } else {
                                                        subvec = subvec + "'" + supid + "'";
                                                    }
                                                    float datott = 0;
                                                    Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                                    empIncCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supid + "'"));
                                                    empIncCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                                                    empIncCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                                                    List incArList = empIncCrit.list();
                                                    if (incArList.size() > 0) {
                                                        for (int sr = 0; sr < incArList.size(); sr++) {
                                                            // <editor-fold defaultstate="collapsed">
                                                            Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                            if (!incrementarrearreferenceObj.getType().equalsIgnoreCase("LEAVESURRENDER")) {
                                                                datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
                                                            } else {
                                                            }
                                                            // </editor-fold>
                                                        }
                                                    }
                                                    resultHTML.append(datott);
                                                    subtotal[k] = subtotal[k] - (long) datott;
                                                    // </editor-fold>
                                                }

                                                resultHTML.append("</td>");
                                                // </editor-fold>
                                            } else {
                                                resultHTML.append("<td align=\"right\" > ");
                                                resultHTML.append("0.00");
                                                resultHTML.append("</td>");
                                            }
                                            // </editor-fold>

                                        }
                                        resultHTML.append("<td align=\"left\" >");
                                        if (type.equalsIgnoreCase("SLINCREMENTMANUAL")) {
                                            resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showManIncDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + payprocesid + "')\">");
                                        }
                                        resultHTML.append("</td>");
                                        resultHTML.append("</tr>");
                                        // </editor-fold>
                                    }

                                }
                                // </editor-fold>
                            }
                            // </editor-fold>
                        }
                        resultHTML.append("<tr>");
                        resultHTML.append("<td align=\"left\" >");
                        resultHTML.append("Balance");
                        resultHTML.append("</td>");
                        float subtot = 0;
                        for (int p = 0; p < subtotal.length; p++) {
                            // <editor-fold defaultstate="collapsed">
                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[p] + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empEarnDetailsList = empEarnDetailsCrit.list();
                            if (empEarnDetailsList.size() > 0) {
                                // <editor-fold defaultstate="collapsed">
                                employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
                                transaction = session.beginTransaction();
                                session.update(employeeearningstransactionsObj);
                                transaction.commit();
                                // </editor-fold>
                            } else {
                                // <editor-fold defaultstate="collapsed">
                                employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();

                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
                                employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                employeeearningstransactionsObj.setEarningmasterid(earcodes[p]);
                                String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                                employeeearningstransactionsObj.setId(earningsTransactionId);
                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);

                                transaction = session.beginTransaction();
                                session.save(employeeearningstransactionsObj);
                                transaction.commit();
                                // </editor-fold>
                            }
                            resultHTML.append("<td align=\"right\" >");
                            subtot = subtot + employeeearningstransactionsObj.getAmount().floatValue();
                            resultHTML.append(employeeearningstransactionsObj.getAmount());
                            resultHTML.append("</td>");
                            // </editor-fold>
                        }
                        resultHTML.append("<td align=\"right\" >");
                        resultHTML.append(subtot);
                        resultHTML.append("</td>");
                        resultHTML.append("</tr>");
                        // </editor-fold>
                    }
                }
                resultHTML.append("</table>");
                resultMap.put("incrementhtml", resultHTML.toString());
                // </editor-fold>
            } else {
                StringBuffer resultHTML = new StringBuffer();
                resultHTML.append("Increment Arear not Made");
                resultMap.put("incrementhtml", resultHTML.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    public synchronized String getmaxSequenceNumberforSupplementaryemployeeloansandadvancesdetails(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementaryemployeeloansandadvancesdetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementaryemployeeloansandadvancesdetailsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public synchronized String getSupplementatypaybillid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementatypaybillid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementatypaybillid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public synchronized String getSupplementarypayrollprocessingdetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementarypayrollprocessingdetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementarypayrollprocessingdetailsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public synchronized String getSupplementarysalarystructureid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementarysalarystructureid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementarysalarystructureid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public synchronized String getSupplementaryemployeeearningsdetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementaryemployeeearningsdetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementaryemployeeearningsdetailsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public synchronized String getSupplementaryemployeedeductiondetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementaryemployeedeductiondetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementaryemployeedeductiondetailsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public synchronized String getSupplementaryemployeeearningstransactionsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementaryemployeeearningstransactionsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementaryemployeeearningstransactionsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public synchronized String getSupplementaryemployeedeductionstransactionsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSupplementaryemployeedeductionstransactionsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSupplementaryemployeedeductionstransactionsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public Employeemaster getEmployeeDetails(Session session, String employeeProvidentFundNumber, String LoggedInRegion) {
        Employeemaster employeemasterObj = null;


        try {
            Criteria lrCrit = session.createCriteria(Employeemaster.class);
            lrCrit.add(Restrictions.sqlRestriction("region = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + employeeProvidentFundNumber + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                employeemasterObj = (Employeemaster) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }


        return employeemasterObj;
    }

    public Supplementarysalarystructure getSupplementarysalarystructure(Session session, String supplementarySalaryStructureId, String LoggedInRegion) {
        Supplementarysalarystructure supplementarysalarystructureObj = null;


        try {
            Criteria lrCrit = session.createCriteria(Supplementarysalarystructure.class);
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("id = '" + supplementarySalaryStructureId + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                supplementarysalarystructureObj = (Supplementarysalarystructure) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }


        return supplementarysalarystructureObj;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map modifySupplementaryBillDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype) {
        Map resultMap = new LinkedHashMap();
        resultMap.put("0", "--Select--");



        try {
            Criteria lrCrit = session.createCriteria(Supplementatypaybill.class);
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
            lrCrit.add(Restrictions.sqlRestriction("date='" + postgresDate(asondate) + "'"));
            lrCrit.add(Restrictions.sqlRestriction("type='" + Billtype + "'"));
            lrCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            lrCrit.addOrder(Order.asc("id"));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                for (int i = 0; i < ldList.size(); i++) {
                    Supplementatypaybill billObj = (Supplementatypaybill) ldList.get(i);
                    resultMap.put(billObj.getId(), billObj.getId());
                }
            } else {
                resultMap.put("ERROR", "Given date is not made Supplementary Bill");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map modifyIncrementArrears(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype) {
        Map resultMap = new LinkedHashMap();
        resultMap.put("0", "--Select--");


        try {
            Criteria lrCrit = session.createCriteria(Supplementatypaybill.class);
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
            lrCrit.add(Restrictions.sqlRestriction("date='" + postgresDate(asondate) + "'"));
            lrCrit.add(Restrictions.sqlRestriction("type='" + Billtype + "'"));
            lrCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            lrCrit.addOrder(Order.asc("id"));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                for (int i = 0; i < ldList.size(); i++) {
                    Supplementatypaybill billObj = (Supplementatypaybill) ldList.get(i);
                    resultMap.put(billObj.getId(), billObj.getId());
                }
            } else {
                resultMap.put("ERROR", "Given date is not made Supplementary Bill");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map modifySurrenderLeaveDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype) {
        Map resultMap = new LinkedHashMap();
        resultMap.put("0", "--Select--");



        try {
            Criteria lrCrit = session.createCriteria(Supplementatypaybill.class);
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
            lrCrit.add(Restrictions.sqlRestriction("date='" + postgresDate(asondate) + "'"));
            lrCrit.add(Restrictions.sqlRestriction("type='" + Billtype + "'"));
            lrCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            lrCrit.addOrder(Order.asc("id"));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Supplementatypaybill billObj = (Supplementatypaybill) ldList.get(0);
                Criteria slCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                slCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                slCrit.add(Restrictions.sqlRestriction("supplementatypaybillid='" + billObj.getId() + "'"));
                List slList = slCrit.list();
                if (slList.size() > 0) {
                    Supplementarypayrollprocessingdetails slbillObj = (Supplementarypayrollprocessingdetails) slList.get(0);
                    resultMap.put("noofdays", slbillObj.getNooddayscalculated());
                }
                resultMap.put("sldate", dateToString(billObj.getSldate()));

            } else {
                resultMap.put("ERROR", "Given date is not made Supplementary Bill");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map deleteSurrenderLeaveDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype) {
        Map resultMap = new LinkedHashMap();
        Transaction transaction = null;



        try {
            Criteria lrCrit = session.createCriteria(Supplementatypaybill.class);
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
            lrCrit.add(Restrictions.sqlRestriction("date='" + postgresDate(asondate) + "'"));
            lrCrit.add(Restrictions.sqlRestriction("type='" + Billtype + "'"));
            lrCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            lrCrit.addOrder(Order.asc("id"));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Supplementatypaybill billObj = (Supplementatypaybill) ldList.get(0);
                billObj.setCancelled(Boolean.TRUE);
                billObj.setCreatedby(LoggedInUser);
                billObj.setCreateddate(getCurrentDate());
                transaction = session.beginTransaction();
                session.update(billObj);
                transaction.commit();


                Criteria slCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                slCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                slCrit.add(Restrictions.sqlRestriction("supplementatypaybillid='" + billObj.getId() + "'"));
                List slList = slCrit.list();
                if (slList.size() > 0) {
                    Supplementarypayrollprocessingdetails slbillObj = (Supplementarypayrollprocessingdetails) slList.get(0);
                    slbillObj.setCancelled(Boolean.TRUE);
                    slbillObj.setCreatedby(LoggedInUser);
                    slbillObj.setCreateddate(getCurrentDate());
                    transaction = session.beginTransaction();
                    session.update(slbillObj);
                    transaction.commit();

                    Criteria slerCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                    slerCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    slerCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + slbillObj.getId() + "'"));
                    List<Supplementaryemployeeearningstransactions> slerList = slerCrit.list();
                    if (slerList.size() > 0) {
                        for (Supplementaryemployeeearningstransactions slerObj : slerList) {
                            slerObj.setCancelled(Boolean.TRUE);
                            slerObj.setCreatedby(LoggedInUser);
                            slerObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(slerObj);
                            transaction.commit();

                        }
                    }
                }
                resultMap.put("Success", "Successfully Surrender Leave Deleted");
            } else {
                resultMap.put("ERROR", "Given date is not made Supplementary Bill");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map displaySupplementaryBillsData(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String billid) {
        Map resultMap = new HashMap();
        Map addMap = new HashMap();
        Map addedMap = new HashMap();
        int i = 1;


        try {
            Criteria lrCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
            lrCrit.add(Restrictions.sqlRestriction("supplementatypaybillid='" + billid + "'"));
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            lrCrit.addOrder(Order.asc("calculatedyear"));
            lrCrit.addOrder(Order.asc("calculatedmonth"));
            List<Supplementarypayrollprocessingdetails> ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                for (Supplementarypayrollprocessingdetails billObj : ldList) {
                    addMap = new HashMap();

                    addMap.put("month", billObj.getCalculatedmonth());
                    addMap.put("year", billObj.getCalculatedyear());
                    addMap.put("noofdays", billObj.getNooddayscalculated());

                    addedMap.put(i, addMap);
                    i++;
                }
            }

            request.getSession().setAttribute("addedMap", addedMap);
            resultMap.put(
                    "displayHTML", displayModifyDatas(addedMap));
            resultMap.put(
                    "mapsize", addedMap.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

//        resultMap.put("displayHTML", resultHTML.toString());
        return resultMap;
    }

    public String displayModifyDatas(Map displayMap) {
        String classname = "";
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        StringBuffer resultHTML = new StringBuffer();
        int i = 0;
        try {
            TreeSet<Integer> keys = new TreeSet<Integer>(displayMap.keySet());

            if (displayMap.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td> Month</td>").append("<td> Year</td>").append("<td> No of Days</td>").append("<td>Modify</td>").append("</tr>");

                for (Integer objkey : keys) {
                    //System.out.println("objkey-==" + objkey);
                    Map sdoObj = (Map) displayMap.get(objkey);
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    String date = monthName[Integer.parseInt(sdoObj.get("month").toString()) - 1] + " " + sdoObj.get("year");
                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"left\">" + monthName[Integer.parseInt(sdoObj.get("month").toString()) - 1] + "</td>").append("<td align=\"center\">" + sdoObj.get("year") + "</td>").append("<td align=\"center\">" + sdoObj.get("noofdays") + "</td>").append("<td align=\"center\"><input type=\"radio\" name=\"billno\" id=\"" + objkey + "\" onclick=\"getModifyData('" + objkey + "','" + date + "','" + sdoObj.get("month") + "','" + sdoObj.get("year") + "','" + sdoObj.get("noofdays") + "')\"></td>").append("</tr>");
                    i++;
                }

            }
//            System.out.println("=="+resultHTML.toString());
            resultHTML.append("</table>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map displayAddModifyDataDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String mapId, String month, String year, String noofdays) {
        Map displayMap = new HashMap();
        Map resultMap = new HashMap();
        Map addMap = new HashMap();

        try {
            displayMap = (Map) request.getSession().getAttribute("addedMap");
            if (displayMap.size() > 0) {
                displayMap.remove(Integer.parseInt(mapId.toString()));
                addMap.put("month", Integer.parseInt(month.toString()));
                addMap.put("year", Integer.parseInt(year.toString()));
                addMap.put("noofdays", Integer.parseInt(noofdays.toString()));
                displayMap.put(Integer.parseInt(mapId.toString()), addMap);

                request.getSession().removeAttribute("addedMap");
                request.getSession().setAttribute("addedMap", displayMap);
                resultMap.put("displayHTML", displayModifyDatas(displayMap));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction Faild");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryBillPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String billtype, String filePath) {
        Map map = new HashMap();
        java.sql.Date AsOnDate = DateParser.postgresDate(asondate);
        try {
            System.out.println("*********************SupplementaryBillServiceImpl class EmployeeSupplementaryBillPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//            PayBillPrinter pbp = new PayBillPrinter();
            SupplementaryPayslipReport spr = new SupplementaryPayslipReport();
            PaySlipModel psm = new PaySlipModel();
            String EmployeeDetails_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            String earn_dedu_id = null;
            String salarystructureid = null;

            if (epfno == null || epfno.length() == 0) {

                EmployeeDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,em.dateofbirth,  em.dateofappoinment,em.dateofconfirmation,"
                        + "rm.regionname, sn.sectionname,dm.designation,em.employeecode,spb.id, dm.payscalecode, em.banksbaccount, em.pancardno, rm.billssigningauthority as billssigningauthority from supplementatypaybill spb "
                        + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                        + "left join  sectionmaster sn on spb.section=sn.id "
                        + "left join  regionmaster rm on spb.accregion=rm.id "
                        + "left join  designationmaster dm on spb.designation=dm.designationcode "
                        + "where spb.date='" + AsOnDate + "' and spb.type='" + billtype + "' and spb.accregion='" + LoggedInRegion + "'";

            } else if (epfno != null || epfno.length() > 0) {

                EmployeeDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,em.dateofbirth,  em.dateofappoinment,"
                        + "em.dateofconfirmation, rm.regionname, sn.sectionname,dm.designation,em.employeecode,spb.id, dm.payscalecode, em.banksbaccount, em.pancardno, rm.billssigningauthority as billssigningauthority from supplementatypaybill spb "
                        + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                        + "left join  sectionmaster sn on spb.section=sn.id "
                        + "left join  regionmaster rm on spb.accregion=rm.id "
                        + "left join  designationmaster dm on spb.designation=dm.designationcode "
                        + "where spb.date='" + AsOnDate + "' and spb.type='" + billtype + "' and spb.employeeprovidentfundnumber='" + epfno + "' and "
                        + "em.epfno='" + epfno + "' and spb.accregion='" + LoggedInRegion + "'";
            }

//            if (epfno == null || epfno.length() == 0) {
//
//                EmployeeDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,em.dateofbirth,  em.dateofappoinment,em.dateofconfirmation,"
//                        + "rm.regionname, sn.sectionname,dm.designation,em.employeecode,spb.id, dm.payscalecode from supplementatypaybill spb "
//                        + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
//                        + "left join  sectionmaster sn on em.section=sn.id "
//                        + "left join  regionmaster rm on em.region=rm.id "
//                        + "left join  designationmaster dm on em.designation=dm.designationcode "
//                        + "where spb.date='" + AsOnDate + "' and spb.type='" + billtype + "'";
//
//            } else if (epfno != null || epfno.length() > 0) {
//
//                EmployeeDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,em.dateofbirth,  em.dateofappoinment,"
//                        + "em.dateofconfirmation, rm.regionname, sn.sectionname,dm.designation,em.employeecode,spb.id, dm.payscalecode from supplementatypaybill spb "
//                        + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
//                        + "left join  sectionmaster sn on em.section=sn.id "
//                        + "left join  regionmaster rm on em.region=rm.id "
//                        + "left join  designationmaster dm on em.designation=dm.designationcode "
//                        + "where spb.date='" + AsOnDate + "' and spb.type='" + billtype + "' and spb.employeeprovidentfundnumber='" + epfno + "' and em.epfno='" + epfno + "'";
//
//            }

            String AsOnMonth = months[Integer.valueOf(asondate.substring(3, 5)) - 1];
            String AsOnYear = asondate.substring(6, 10);
//            String payslipstartingdate = months[Integer.valueOf(asondate.substring(3, 5)) - 1] + "\"" + asondate.substring(6, 10);

//            System.out.println("Query = " + EmployeeDetails_Query);

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
//            System.out.println("employeequery.list().size()" + employeequery.list().size());
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();
                psm.setPfno((String) rows[0]);
                psm.setEmployeename((String) rows[1]);
                if (rows[2] == null) {
                    psm.setDateofbirth("");
                } else {
                    String date = rows[2].toString();
                    psm.setDateofbirth(DateParser.convertTheDateIntoDisplayFormat(date));
                }
                if (rows[3] == null) {
                    psm.setDateofappointment("");
                } else {
                    String date = rows[3].toString();
                    psm.setDateofappointment(DateParser.convertTheDateIntoDisplayFormat(date));
                }
                if (rows[4] == null) {
                    psm.setDateofconformation("");
                } else {
                    String date = rows[4].toString();
                    psm.setDateofconformation(DateParser.convertTheDateIntoDisplayFormat(date));
                }
                psm.setPayslipyear(AsOnYear);
                psm.setPayslipmonth(AsOnMonth);
                psm.setBranch((String) rows[5]);
                psm.setSectionname((String) rows[6]);
                psm.setDesignation((String) rows[7]);
                psm.setEmpno((String) rows[8]);
                String subSalStrucId = (String) rows[9];
                if (rows[10] != null) {
                    psm.setPayscale((String) rows[10]);
                } else {
                    psm.setPayscale("");
                }

                if (rows[11] != null) {
                    String bankaccountno = (String) rows[11];
                    if (bankaccountno.equalsIgnoreCase("Null") || bankaccountno.equalsIgnoreCase("NULL")) {
                        psm.setBankaccountno("");
                    } else {
                        psm.setBankaccountno((String) rows[11]);
                    }
                } else {
                    psm.setBankaccountno("");
                }

                if (rows[12] != null) {
                    String pancardno = (String) rows[12];
                    if (pancardno.equalsIgnoreCase("Null") || pancardno.equalsIgnoreCase("NULL")) {
                        psm.setPancardno("");
                    } else {
                        psm.setPancardno((String) rows[12]);
                    }
                } else {
                    psm.setPancardno("");
                }
                psm.setSigningauthority((String) rows[13]);

                String ProcessingPeriod_Query = "select calculatedmonth,calculatedyear from supplementarypayrollprocessingdetails where supplementatypaybillid = '" + subSalStrucId + "' and cancelled is false order by calculatedyear,calculatedmonth";

//                System.out.println("ProcessingPeriod_Query = " + ProcessingPeriod_Query);

                SQLQuery processingperiodquery = session.createSQLQuery(ProcessingPeriod_Query);

                String payslipstartingdate = "";
                String payslipenddate = "";

                int recordsize = processingperiodquery.list().size();

                Object[] rowbegin = (Object[]) processingperiodquery.list().get(0);

                Object[] rowend = (Object[]) processingperiodquery.list().get(recordsize - 1);

                if (rowbegin[0] != null && rowbegin[1] != null && rowend[0] != null && rowend[1] != null) {
                    payslipstartingdate = months[(Integer) rowbegin[0] - 1] + "\"" + (Integer) rowbegin[1];
                    payslipenddate = months[(Integer) rowend[0] - 1] + "\"" + (Integer) rowend[1];
                }

                psm.setBilltype(billtype);
                psm.setPayslipstartingdate(payslipstartingdate);
                psm.setPayslipenddate(payslipenddate);
//                System.out.println("payslipstartingdate = " + payslipstartingdate);
//                System.out.println("payslipenddate = " + payslipenddate);

                String NoofDays_Query = "select sum(nooddayscalculated) noofdays from supplementarypayrollprocessingdetails "
                        + "where supplementatypaybillid='" + subSalStrucId + "' and cancelled is false";

                String Deduction_Query = " select pm.paycodename,sum(edt.amount) "
                        + " from  supplementaryemployeedeductionstransactions edt "
                        + " left join paycodemaster pm on edt.deductionmasterid = pm.paycode "
                        + " left join supplementarypayrollprocessingdetails spbpd on spbpd.id=edt.supplementarypayrollprocessingdetailsid "
                        + " left join supplementatypaybill spb on spb.id=spbpd.supplementatypaybillid where spb.id='" + subSalStrucId + "' and spbpd.cancelled is false and edt.cancelled is false "
                        + " group by pm.paycode  order by pm.paycode  ";


                String Earnings_Query = " select pm.paycodename,sum(edt.amount)  "
                        + " from  supplementaryemployeeearningstransactions edt "
                        + " left join paycodemaster pm on edt.earningmasterid = pm.paycode "
                        + " left join supplementarypayrollprocessingdetails spbpd on spbpd.id=edt.supplementarypayrollprocessingdetailsid "
                        + " left join supplementatypaybill spb on spb.id=spbpd.supplementatypaybillid where spb.id='" + subSalStrucId + "' and spbpd.cancelled is false and edt.cancelled is false "
                        + " group by pm.paycode  order by pm.paycode ";

                String Loan_Query = "select employeemaster.epfno,paycodemaster.paycodename, employeeloansandadvances.loanamount,"
                        + "supplementaryemployeeloansandadvancesdetails.loanbalance,employeeloansandadvances.totalinstallment, "
                        + "supplementaryemployeeloansandadvancesdetails.nthinstallment from supplementaryemployeeloansandadvancesdetails left join supplementarypayrollprocessingdetails on "
                        //                        + "supplementaryemployeeloansandadvancesdetails.loanbalance,employeeloansandadvances.totalinstallment, employeeloansandadvances."
                        //                        + "employeeloansandadvances.loanbalance,employeeloansandadvances.totalinstallment, employeeloansandadvances."
                        //                        + "currentinstallment from supplementaryemployeeloansandadvancesdetails left join supplementarypayrollprocessingdetails on "
                        + "supplementarypayrollprocessingdetails.id=supplementaryemployeeloansandadvancesdetails.supplementarypayrollprocessingdetailsid left join "
                        + "employeeloansandadvances on employeeloansandadvances.id=supplementaryemployeeloansandadvancesdetails.employeeloansandadvancesid "
                        + "left join supplementatypaybill on supplementatypaybill.id=supplementarypayrollprocessingdetails.supplementatypaybillid "
                        + "left join paycodemaster on paycodemaster.paycode=employeeloansandadvances.deductioncode left join employeemaster "
                        + "on employeemaster.epfno= supplementatypaybill.employeeprovidentfundnumber where supplementatypaybill.id='" + subSalStrucId + "'  "
                        + "and supplementaryemployeeloansandadvancesdetails.cancelled is false order by paycodemaster.paycode";

                String OrginalEarnings = " select eed.earningmasterid,eed.amount from supplementaryemployeeearningsdetails eed, supplementatypaybill spb, supplementarysalarystructure sss where "
                        + " sss.supplementatypaybillid=spb.id and eed.supplementarysalarystructureid  = sss.id and  spb.id = '" + subSalStrucId + "' ";

                SQLQuery noofdaysquery = session.createSQLQuery(NoofDays_Query);
                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
                SQLQuery loanquery = session.createSQLQuery(Loan_Query);
                //SQLQuery loanquery = session.createSQLQuery(Loan_Query);
                SQLQuery originalearningsquery = session.createSQLQuery(OrginalEarnings);

//                System.out.println("noofdaysquery -> " + noofdaysquery);
//                System.out.println("deductionquery -> " + deductionquery);
//                System.out.println("earningsquery -> " + earningsquery);

                String noofdays = null;

                if (noofdaysquery.list().size() > 0) {
                    Object obj = noofdaysquery.list().get(0);
                    noofdays = obj.toString();
                }
//                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//                System.out.println("Noofdays =>" + noofdays);
//                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                psm.setPayday(noofdays);

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

                for (ListIterator it = deductionquery.list().listIterator(); it.hasNext();) {
                    Object[] row = (Object[]) it.next();
                    psedm = new PaySlip_Earn_Deduction_Model();
                    psedm.setDeductionname(DeduStringSet((String) row[0]));
                    psedm.setDeductionamount((row[1]).toString());
                    sum += Double.valueOf(psedm.getDeductionamount());
                    deductionlist.add(psedm);
                }
                psm.setTotaldeductions(String.valueOf(decimalFormat.format(sum)));//Set Total Deduction Amount

                sum = 0;
                for (ListIterator it = earningsquery.list().listIterator(); it.hasNext();) {
                    Object[] row = (Object[]) it.next();
                    psedm = new PaySlip_Earn_Deduction_Model();
                    psedm.setEarningsname(EarnStringSet((String) row[0]));
                    psedm.setEarningsamount((row[1]).toString());
                    sum += Double.valueOf(psedm.getEarningsamount());
                    earninglist.add(psedm);
                }
                psm.setTotalearnings(String.valueOf(decimalFormat.format(sum)));//Set Total Earnings Amount

//                System.out.println("Loan List ::::::::::::::::::::::::::::::: " + loanlist.size());

                for (ListIterator it = loanquery.list().listIterator(); it.hasNext();) {
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
                }

                String basicpay = null;
                String da = null;
                String hra = null;
                String cca = null;
                String gradepay = null;

                for (ListIterator it = originalearningsquery.list().listIterator(); it.hasNext();) {
                    Object[] row = (Object[]) it.next();
                    if (row[0].toString().equalsIgnoreCase("E01")) {
                        basicpay = row[1].toString();
                    }
                    if (row[0].toString().equalsIgnoreCase("E04")) {
                        da = row[1].toString();
                    }
                    if (row[0].toString().equalsIgnoreCase("E06")) {
                        hra = row[1].toString();
                    }
                    if (row[0].toString().equalsIgnoreCase("E07")) {
                        cca = row[1].toString();
                    }
                    if (row[0].toString().equalsIgnoreCase("E25")) {
                        gradepay = row[1].toString();
                    }
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
                 * *****************
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
                psm.setDistrict(psm.getBranch());
                psm.setPincode("");
//                psm.setPayscale("9300 - 34800");
                psm.setDateofelsurrenderdate("");
                psm.setRate("");
//                psm.setPayday(noofdays);
                if (Double.valueOf(psm.getNetsalary()) > 0) {
//                    System.out.println("Net Salary = " + psm.getNetsalary());
                    psm.setNetsalarywords(AmountInWords.convertAmountintoWords(psm.getNetsalary()));
                } else {
                    psm.setNetsalarywords(" ");
                }
                spr.getSupplementaryPrintWriter(psm, filePath);
                slipno++;
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Sublementary Slip Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    public String QueryString(String[] billnumbers) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < billnumbers.length; i++) {
            buffer.append("'");
            buffer.append(billnumbers[i]);
            buffer.append("'");
            if (i + 1 != billnumbers.length) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String[] billnumbers, String asondate, String billtype, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class EmployeeSupplementaryAbstractPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//            PayBillPrinter pbp = new PayBillPrinter();
//            AbstractReport ar = new AbstractReport();
            SupplementaryAbstractReport sar = new SupplementaryAbstractReport();
            PaySlipModel psm;
            String SectionDetails_Query = null;
            PaySlip_Earn_Deduction_Model psedm = null;
            PaySlip_Earn_Deduction_Model psedm1 = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            String EarningsList_Query = null;
            String DeductionList_Query = null;

            EarningsList_Query = "select seet.earningmasterid,pm.paycodename,sum(seet.amount) amount from supplementaryemployeeearningstransactions seet "
                    + "left join paycodemaster pm on pm.paycode=seet.earningmasterid where seet.supplementarypayrollprocessingdetailsid in "
                    + "(" + QueryString(billnumbers) + ") and seet.cancelled is false group by seet.earningmasterid,pm.paycodename order by pm.paycodename";

            DeductionList_Query = "select sedt.deductionmasterid,pm.paycodename,sum(sedt.amount) amount from supplementaryemployeedeductionstransactions sedt "
                    + "left join paycodemaster pm on pm.paycode=sedt.deductionmasterid where sedt.supplementarypayrollprocessingdetailsid in "
                    + "(" + QueryString(billnumbers) + ") and sedt.cancelled is false group by sedt.deductionmasterid,pm.paycodename order by pm.paycodename";

            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";

            if (reList.size()
                    > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }
//            System.out.println("EarningsList_Query = " + EarningsList_Query);
//            System.out.println("DeductionList_Query = " + DeductionList_Query);
            SQLQuery earningsquery = session.createSQLQuery(EarningsList_Query);
            SQLQuery deductionquery = session.createSQLQuery(DeductionList_Query);
            int earningrecordsize = earningsquery.list().size();
            int deductionrecordsize = deductionquery.list().size();
            if (earningrecordsize == 0 && deductionrecordsize
                    == 0) {
                map.put("ERROR", "There is no data for the Given Inputs");
                return map;
            }
            double earningstotal = 0;
            double deductiontotal = 0;
            List<PaySlip_Earn_Deduction_Model> earningslist = new ArrayList<PaySlip_Earn_Deduction_Model>();
//                    System.out.println("Section Name ::::::::::::::::::::::::: " + psm.getSectionname());
//
//                    System.out.println("*********************** Earning List ***************************");
            for (ListIterator ite = earningsquery.list().listIterator();
                    ite.hasNext();) {
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
//                        System.out.println(psedm.getEarningsname() + " - " + psedm.getEarningsamount());
                earningslist.add(psedm);
            }
//                    System.out.println("----------------------------------------------------------------");
            List<PaySlip_Earn_Deduction_Model> deductionlist = new ArrayList<PaySlip_Earn_Deduction_Model>();
//                    System.out.println("*********************** Deduction List ***************************");
            for (ListIterator itd = deductionquery.list().listIterator();
                    itd.hasNext();) {
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
//                        System.out.println(psedm.getDeductionname() + " - " + psedm.getDeductionamount());
                deductionlist.add(psedm);
            }
//                    System.out.println("----------------------------------------------------------------");
//
//                    System.out.println("Before earningslist.size - >" + earningslist.size());
//                    System.out.println("Before deductionlist.size - >" + deductionlist.size());

            if (earningslist.size()
                    < deductionlist.size()) {
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
            psm = new PaySlipModel();

            psm.setPayslipyear(asondate);

            psm.setBranch(regionname);

            if (billtype.equals(
                    "LEAVESURRENDER")) {
                psm.setBilltype("FOR SURRENDER LEAVE");
            } else {
                psm.setBilltype("FOR LEAVE SALARY");
            }

            psm.setTotalearnings(decimalFormat.format(earningstotal));
            psm.setTotaldeductions(decimalFormat.format(deductiontotal));
            psm.setEarningslist(earningslist);

            psm.setDeductionlist(deductionlist);

            psm.setNetsalary(decimalFormat.format(totalnetsalary));
            psm.setNetsalarywords(AmountInWords.convertAmountintoWords(psm.getNetsalary()));
            sar.getSalaryAbstractPrintWriter(psm, filePath);
        } catch (Exception ex) {
            map.put("ERROR", "Supplementary Abstract Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map displaySupplementaryAbstractDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String billdate, String billtype) {
        Map map = new HashMap();
        java.sql.Date BillDate = DateParser.postgresDate(billdate);
        try {
            System.out.println("*********************SupplementaryBillServiceImpl class displaySupplementaryAbstractDetails method is calling *****************");
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            String AbstractDetails_Query = null;

            if (billtype.equals("ALL")) {
                AbstractDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,sspd.id,sspd.calculatedmonth,sspd.calculatedyear,"
                        + "sspd.nooddayscalculated,sspd.accregion,spb.type from supplementatypaybill  spb "
                        + "left join employeemaster em on spb.employeeprovidentfundnumber=em.epfno "
                        + "left join supplementarypayrollprocessingdetails sspd on sspd.supplementatypaybillid=spb.id  "
                        + "where spb.date='" + BillDate + "' and spb.accregion='" + LoggedInRegion + "' and sspd.cancelled is false order by sspd.supplementatypaybillid,sspd.id";
            } else {
                AbstractDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,sspd.id,sspd.calculatedmonth,sspd.calculatedyear,"
                        + "sspd.nooddayscalculated,sspd.accregion,spb.type from supplementatypaybill  spb "
                        + "left join employeemaster em on spb.employeeprovidentfundnumber=em.epfno "
                        + "left join supplementarypayrollprocessingdetails sspd on sspd.supplementatypaybillid=spb.id "
                        + "where spb.date='" + BillDate + "' and spb.type='" + billtype + "' and spb.accregion='" + LoggedInRegion + "' and sspd.cancelled is false "
                        + "order by sspd.supplementatypaybillid,sspd.id";
            }

//            if (billtype.equals("ALL")) {
//                AbstractDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,sspd.id,sspd.calculatedmonth,sspd.calculatedyear,"
//                        + "sspd.nooddayscalculated,sspd.accregion,spb.type from supplementatypaybill  spb left join employeemaster em on "
//                        + "spb.employeeprovidentfundnumber=em.epfno left join supplementarypayrollprocessingdetails sspd on "
//                        + "sspd.supplementatypaybillid=spb.id where spb.date='" + BillDate + "' and sspd.accregion='" + regionid + "' and sspd.cancelled is false order by sspd.supplementatypaybillid,sspd.id";
//            } else {
//                AbstractDetails_Query = "select spb.employeeprovidentfundnumber,em.employeename,sspd.id,sspd.calculatedmonth,sspd.calculatedyear,"
//                        + "sspd.nooddayscalculated,sspd.accregion,spb.type from supplementatypaybill  spb left join employeemaster em on "
//                        + "spb.employeeprovidentfundnumber=em.epfno left join supplementarypayrollprocessingdetails sspd on sspd.supplementatypaybillid=spb.id "
//                        + "where spb.date='" + BillDate + "' and spb.type='" + billtype + "' and sspd.accregion='" + regionid + "' and sspd.cancelled is false order by sspd.supplementatypaybillid,sspd.id";
//            }

            //System.out.println("Query = " + AbstractDetails_Query);

            SQLQuery abstractquery = session.createSQLQuery(AbstractDetails_Query);
//            System.out.println("abstractquery.list().size()" + abstractquery.list().size());
            if (abstractquery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            String billno = null;
            String lightrow = "lightrow";
            String darkrow = "darkrow";
            StringBuffer buffer = new StringBuffer();
            buffer.append("<table align=\"center\" width=\"700\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
            buffer.append("<tr class=\"mainheader\">");
            buffer.append("<td width=\"5%\" align=\"center\">Sno</td>");
            buffer.append("<td width=\"10%\">EPF NO</td>");
            buffer.append("<td width=\"25%\">Employee Name</td>");
            buffer.append("<td width=\"25%\">Type of Bill</td>");
            buffer.append("<td width=\"15%\">Billing Month</td>");
            buffer.append("<td width=\"15%\">No Of Days</td>");
            buffer.append("<td width=\"5%\" align=\"center\">");
            buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
            buffer.append("</td>");
            buffer.append("</tr>");

            for (ListIterator its = abstractquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();

                buffer.append("<tr class=\"" + lightrow + "\">");

                buffer.append("<td width=\"5%\" align=\"center\">");
                buffer.append(slipno);
                buffer.append("</td>");

                buffer.append("<td width=\"10%\">");
                buffer.append((String) rows[0]);
                buffer.append("</td>");

                buffer.append("<td width=\"25%\">");
                buffer.append((String) rows[1]);
                buffer.append("</td>");

                buffer.append("<td width=\"25%\">");
                buffer.append((String) rows[7]);
                buffer.append("</td>");

                buffer.append("<td width=\"15%\">");
                if (rows[3] != null && rows[4] != null) {
                    buffer.append(months[(Integer) rows[3] - 1] + rows[4].toString());
                } else {
                    buffer.append("");
                }
                buffer.append("</td>");

                buffer.append("<td width=\"15%\">");

                buffer.append((rows[5] != null) ? rows[5].toString() : "");
//                buffer.append(rows[5].toString());
                buffer.append("</td>");

                buffer.append("<td width=\"5%\" align=\"center\">");
                buffer.append("<input type=\"checkbox\" name=\"billnumbers\" id=\"billnumbers\" value=" + (String) rows[2] + ">");
                buffer.append("</td>");

                buffer.append("</tr>");
                slipno++;


            }
            buffer.append("</table>");
            map.put("AbstractGrid", buffer.toString());


        } catch (Exception ex) {
            map.put("ERROR", "Sublementary Bill Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryEPFformPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* EmployeePayBillServiceImpl class EmployeeSupplementaryEPFformPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            SupplementaryEPFformReport sepfr = new SupplementaryEPFformReport();
            PaySlipModel psm;
            String EmployeeDetails_Query = null;
//            System.out.println("epfno = " + epfno);
//            System.out.println("sectionname = " + sectionname);
//            System.out.println("year = " + year);
//            System.out.println("month = " + month);
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;

            EmployeeDetails_Query = "select sp.employeeprovidentfundnumber, sp.id, date_part('year',sp.date) as year, date_part('month',sp.date) as month,"
                    + "em.employeename, rm.regionname, dm.designation, em.employeecode, sp.section from supplementatypaybill sp "
                    + "left join employeemaster em on em.epfno=sp.employeeprovidentfundnumber "
                    + "left join regionmaster rm on sp.accregion=rm.id "
                    + "left join designationmaster dm on sp.designation=dm.designationcode where "
                    + "date_part('month',sp.date)=" + p_month + " and date_part('year',sp.date)=" + p_year + " and sp.section not in('S13','S14') "
                    + "and sp.type='SUPLEMENTARYBILL' and sp.accregion='" + LoggedInRegion + "' order by sp.section,dm.orderno";

//            EmployeeDetails_Query = "select sp.employeeprovidentfundnumber, sp.id, date_part('year',sp.date) as year, date_part('month',sp.date) as month,"
//                    + "em.employeename, rm.regionname, dm.designation, em.employeecode, sp.section from supplementatypaybill sp, "
//                    + "employeemaster em,regionmaster rm,designationmaster dm where date_part('month',sp.date) = " + p_month + " and "
//                    + "date_part('year',sp.date) = " + p_year + " and em.epfno = sp.employeeprovidentfundnumber and sp.accregion=rm.id "
//                    + "and em.designation=dm.designationcode and sp.section not in('S13','S14') and sp.type='SUPLEMENTARYBILL' "
//                    + "and rm.defaultregion is true order by sp.section,dm.orderno";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
//            System.out.println("************************************************");
//            System.out.println("EmployeeDetails_Query -> " + EmployeeDetails_Query);
//            System.out.println("************************************************");
//            System.out.println("employeequery.list().size()" + employeequery.list().size());
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();

                Object[] rows = (Object[]) its.next();
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);
                psm.setPayslipyear(rows[2].toString());
                double mon_double = (Double) rows[3];
                int mon_int = (int) mon_double;
//                System.out.println("mon_int = " + mon_int);
//                System.out.println("months[mon_int - 1] = " + months[mon_int - 1]);
                psm.setPayslipmonth(months[mon_int - 1]);
//                psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setDesignation((String) rows[6]);
                psm.setEmpno((String) rows[7]);
                psm.setSectionname((String) rows[8]);

                String Supplementaryid_Query = "select id,calculatedyear,calculatedmonth from supplementarypayrollprocessingdetails  "
                        + "where supplementatypaybillid = '" + earn_dedu_id + "' and cancelled is false";

//                System.out.println("************************************************");
//                System.out.println("Supplementaryid_Query -> " + Supplementaryid_Query);
//                System.out.println("************************************************");

                String supplementaryid = null;
                String calculatedyear = null;
                String calculatedmonth = null;

                SQLQuery supplementaryidquery = session.createSQLQuery(Supplementaryid_Query);
                for (ListIterator itsup = supplementaryidquery.list().listIterator(); itsup.hasNext();) {
                    Object[] rowsup = (Object[]) itsup.next();
                    supplementaryid = (String) rowsup[0];
                    if (rowsup[1] != null) {
                        calculatedyear = rowsup[1].toString();
                        psm.setPayslipyearprevious(String.valueOf(calculatedyear));
                    } else {
                        psm.setPayslipyearprevious("");
                    }
                    if (rowsup[2] != null) {
                        calculatedmonth = months[((Integer) rowsup[2]) - 1];
                        psm.setPayslipmonthprevious(String.valueOf(calculatedmonth));
                    } else {
                        psm.setPayslipmonthprevious("");
                    }


                    String Deduction_Query = "select deductionmasterid,amount from supplementaryemployeedeductionstransactions where "
                            + "supplementarypayrollprocessingdetailsid ='" + supplementaryid + "' and cancelled is false";

                    String Salary_Query = "select sum(edt.amount) salary from  supplementaryemployeeearningstransactions edt where "
                            + "edt.supplementarypayrollprocessingdetailsid='" + supplementaryid + "' and edt.cancelled is false and edt.earningmasterid in "
                            + "(select paycode from ccahra where ccahra='D02') ";

//                String Earnings_Query = "select eet.earningmasterid,eet.amount from employeeearningstransactions  eet,"
//                        + "paycodemaster pm where eet.payrollprocessingdetailsid='" + earn_dedu_id + "' and eet.cancelled is false and eet.earningmasterid=pm.paycode";


                    SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                    SQLQuery salaryquery = session.createSQLQuery(Salary_Query);

//                    System.out.println("************************************************");
//                    System.out.println("Deduction_Query ->" + Deduction_Query);
//                    System.out.println("Salary_Query ->" + Salary_Query);
//                    System.out.println("************************************************");
//                System.out.println("deductionquery -> " + deductionquery);
//                System.out.println("earningsquery -> " + earningsquery);

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
//                    System.out.println(ex.getKey() + "-" + ex.getValue());
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

//                System.out.println("psm.getPfno() -> " + psm.getPfno());
//                System.out.println("psm.getEmpno() -> " + psm.getEmpno());
//                System.out.println("psm.getEmployeename() -> " + psm.getEmployeename());
//                System.out.println("psm.getPayslipyear() -> " + psm.getPayslipyear());
//                System.out.println("psm.getPayslipmonth() -> " + psm.getPayslipmonth());
//                System.out.println("psm.getDesignation() -> " + psm.getDesignation());
//                System.out.println("psm.getBranch() -> " + psm.getBranch());
//                System.out.println("psm.getSalary() -> " + psm.getSalary());
//                System.out.println("psm.getEpf() -> " + psm.getEpf());
//                System.out.println("psm.getEpfloan() -> " + psm.getEpfloan());
//                System.out.println("psm.getVpf() -> " + psm.getVpf());
//                System.out.println("psm.getEmployertotal() -> " + psm.getEmployertotal());
//                System.out.println("psm.getPercentage833() -> " + psm.getPercentage833());
//                System.out.println("psm.getPercentage367() -> " + psm.getPercentage367());
//                System.out.println("psm.getRemarkstotal() -> " + psm.getRemarkstotal());
//                System.out.println("psm.getPrintingrecordsize() -> " + psm.getPrintingrecordsize());
//                System.out.println("psm.getSlipno() -> " + psm.getSlipno());
//                System.out.println("===================================================");

                    sepfr.getEPFformPrintWriter(psm, filePath);
//                pbp.getEPFformPrintWriter(psm, filePath);
                    slipno++;
                }

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
    public Map EmployeeSupplementaryEPFformDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* EmployeePayBillServiceImpl class EmployeeSupplementaryEPFformDBFPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            SupplementaryEPFformDBFReport sepfr = new SupplementaryEPFformDBFReport();
            PaySlipModel psm = new PaySlipModel();
            String EmployeeDetails_Query = null;
//            System.out.println("epfno = " + epfno);
//            System.out.println("sectionname = " + sectionname);
//            System.out.println("year = " + year);
//            System.out.println("month = " + month);
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;

            EmployeeDetails_Query = "select sp.employeeprovidentfundnumber, sp.id, date_part('year',sp.date) as year, "
                    + "date_part('month',sp.date) as month,em.employeename, rm.regionname, dm.designation, em.employeecode, "
                    + "sp.section from supplementatypaybill sp, employeemaster em,regionmaster rm,designationmaster dm "
                    + "where date_part('month',sp.date)=" + p_month + "   and date_part('year',sp.date)=" + p_year + " and "
                    + "em.epfno = sp.employeeprovidentfundnumber and sp.accregion=rm.id and em.designation=dm.designationcode "
                    + "and sp.section not in('S13','S14') and sp.accregion='" + LoggedInRegion + "' order by sp.section,dm.orderno";

//            EmployeeDetails_Query = "select sp.employeeprovidentfundnumber, sp.id, date_part('year',sp.date) as year, "
//                    + "date_part('month',sp.date) as month,em.employeename, rm.regionname, dm.designation, em.employeecode, "
//                    + "sp.section from supplementatypaybill sp, employeemaster em,regionmaster rm,designationmaster dm "
//                    + "where date_part('month',sp.date) = " + p_month + " and date_part('year',sp.date) = " + p_year + " and "
//                    + "em.epfno = sp.employeeprovidentfundnumber and sp.accregion=rm.id and em.designation=dm.designationcode "
//                    + "and sp.section not in('S13','S14') and rm.defaultregion is true order by sp.section,dm.orderno";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
//            System.out.println("employeequery.list().size()" + employeequery.list().size());
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
                double mon_double = (Double) rows[3];
                int mon_int = (int) mon_double;
//                System.out.println("mon_int = " + mon_int);
//                System.out.println("months[mon_int - 1] = " + months[mon_int - 1]);
//                psm.setPayslipmonth(months[mon_int - 1]);
//                psm.setPayslipmonth(months[(Integer) rows[3] - 1]);
                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                psm.setDesignation((String) rows[6]);
                psm.setEmpno((String) rows[7]);
                psm.setSectionname((String) rows[8]);

                String Deduction_Query = "select deductionmasterid,sum(amount) amount from supplementaryemployeedeductionstransactions sedt "
                        + "left join supplementarypayrollprocessingdetails spp on spp.id = sedt.supplementarypayrollprocessingdetailsid "
                        + "where spp.supplementatypaybillid = '" + earn_dedu_id + "' and sedt.cancelled is false group by sedt.deductionmasterid";

                String Earnings_Query = "select earningmasterid,sum(amount) amount from supplementaryemployeeearningstransactions seet "
                        + "left join supplementarypayrollprocessingdetails spp on spp.id = seet.supplementarypayrollprocessingdetailsid "
                        + "where spp.supplementatypaybillid = '" + earn_dedu_id + "' and seet.cancelled is false group by seet.earningmasterid";


                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);

                Map<String, String> earn_ded_map = new HashMap<String, String>();

                double basicpay = 0;
                double da = 0;
                double gradepay = 0;
                double perpay = 0;
                double splpay = 0;
                double epf = 0;
                double epfloan = 0;
                double vpf = 0;
                double Salary = 0;
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

                for (ListIterator it = earningsquery.list().listIterator(); it.hasNext();) {
                    Object[] row = (Object[]) it.next();
                    if (row[0] != null) {
                        String earningname = (String) row[0];
                        String earningamount = "0";
                        if (row[1] != null) {
                            earningamount = row[1].toString();
                        } else {
                            earningamount = "0";
                        }
                        earn_ded_map.put(earningname, earningamount);
                    }
                }

                Iterator itr = earn_ded_map.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry ex = (Entry) itr.next();
//                    System.out.println(ex.getKey() + "-" + ex.getValue());
                }

//                System.out.println("earn_ded_map.get('L02') -> "+earn_ded_map.get("L02"));
//                System.out.println("earn_ded_map.containsKey('LO2') -> "+earn_ded_map.containsKey("LO2"));

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

                /*
                 * Calculate Salary
                 */

                if (earn_ded_map.get("E01") != null) {
                    basicpay = Double.valueOf(earn_ded_map.get("E01"));
                }
                if (earn_ded_map.get("E02") != null) {
                    splpay = Double.valueOf(earn_ded_map.get("E02"));
                }
                if (earn_ded_map.get("E03") != null) {
                    perpay = Double.valueOf(earn_ded_map.get("E03"));
                }
                if (earn_ded_map.get("E04") != null) {
                    da = Double.valueOf(earn_ded_map.get("E04"));
                }
                if (earn_ded_map.get("E25") != null) {
                    gradepay = Double.valueOf(earn_ded_map.get("E25"));
                }

                Salary = basicpay + splpay + perpay + da + gradepay;

                /*
                 * Calculate 12% Of Salary
                 */

                epf = (Salary * 12) / 100;

                employertotal = epf + epfloan + vpf;


                long roundsalary = Math.round(Salary);
                long roundepf = Math.round(epf);
                long roundemployertotal = Math.round(employertotal);

                psm.setSalary(String.valueOf(roundsalary));
                psm.setEpf(String.valueOf(roundepf));
                psm.setEmployertotal(String.valueOf(roundemployertotal));

//                long percentage833 = 1250;
                double rsalary = roundsalary;
                double per833 = 8.33;
                long percentage833 = Math.round((rsalary * per833) / 100);
                if (percentage833 > 1250) {
                    percentage833 = 1250;
                }
                long percentage367 = roundepf - percentage833;
                long remarkstotal = percentage833 + percentage367;

                psm.setPercentage833(String.valueOf(percentage833));
                psm.setPercentage367(String.valueOf(percentage367));
                psm.setRemarkstotal(String.valueOf(remarkstotal));
                psm.setSlipno(String.valueOf(slipno));
                psm.setPrintingrecordsize(printingrecordsize);

//                System.out.println("psm.getPfno() -> " + psm.getPfno());
//                System.out.println("psm.getEmpno() -> " + psm.getEmpno());
//                System.out.println("psm.getEmployeename() -> " + psm.getEmployeename());
//                System.out.println("psm.getPayslipyear() -> " + psm.getPayslipyear());
//                System.out.println("psm.getPayslipmonth() -> " + psm.getPayslipmonth());
//                System.out.println("psm.getDesignation() -> " + psm.getDesignation());
//                System.out.println("psm.getBranch() -> " + psm.getBranch());
//                System.out.println("psm.getSalary() -> " + psm.getSalary());
//                System.out.println("psm.getEpf() -> " + psm.getEpf());
//                System.out.println("psm.getEpfloan() -> " + psm.getEpfloan());
//                System.out.println("psm.getVpf() -> " + psm.getVpf());
//                System.out.println("psm.getEmployertotal() -> " + psm.getEmployertotal());
//                System.out.println("psm.getPercentage833() -> " + psm.getPercentage833());
//                System.out.println("psm.getPercentage367() -> " + psm.getPercentage367());
//                System.out.println("psm.getRemarkstotal() -> " + psm.getRemarkstotal());
//                System.out.println("psm.getPrintingrecordsize() -> " + psm.getPrintingrecordsize());
//                System.out.println("psm.getSlipno() -> " + psm.getSlipno());
//                System.out.println("===================================================");

                sepfr.getEPFformPrintWriter(psm, filePath);
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
    public Map getEmployeeRegionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map<String, String> RegionMap = new LinkedHashMap<String, String>();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class getEmployeeRegionList method is calling *****************");
            Criteria empDetailsCrit = session.createCriteria(Regionmaster.class);
//            empDetailsCrit.add(Restrictions.sqlRestriction("paycodetype = 'D' "));
            empDetailsCrit.addOrder(Order.asc("id"));
            List empDetailsList = empDetailsCrit.list();

            if (empDetailsList.size()
                    > 0) {
                for (int i = 0; i < empDetailsList.size(); i++) {
                    Regionmaster regionmaster = (Regionmaster) empDetailsList.get(i);
                    RegionMap.put(regionmaster.getId(), regionmaster.getRegionname());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return RegionMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getSupplementaryEmployeeDeductionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map<String, String> deductionMap = new LinkedHashMap<String, String>();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class getSupplementaryEmployeeDeductionList method is calling *****************");
            Criteria empDetailsCrit = session.createCriteria(Paycodemaster.class);
//            empDetailsCrit.add(Restrictions.sqlRestriction("paycodetype = 'D' "));
            empDetailsCrit.addOrder(Order.asc("paycodename"));
            List empDetailsList = empDetailsCrit.list();

            if (empDetailsList.size()
                    > 0) {
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
    public Map EmployeeSupplementaryDeductionAllPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionid, String year, String month, String reporttype, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class EmployeeSupplementaryDeductionAllPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            SupplementaryDeductionAllReport dar = new SupplementaryDeductionAllReport();
            PaySlipModel psm = new PaySlipModel();
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String Deductionid = deductionid;
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

            if (Integer.valueOf(reporttype) == 1) {

                EmployeeDetails_Query = "select sp.id, sp.employeeprovidentfundnumber, em.employeename, sm.sectionname, dm.designation, rm.regionname, "
                        + "date_part('year',sp.date) as year, date_part('month',sp.date) as month from supplementatypaybill sp "
                        + "left join employeemaster em on em.epfno = sp.employeeprovidentfundnumber "
                        + "left join regionmaster rm on sp.accregion=rm.id "
                        + "left join designationmaster dm on sp.designation=dm.designationcode "
                        + "left join sectionmaster sm on sp.section=sm.id where "
                        + "sp.accregion='" + LoggedInRegion + "' and date_part('month',sp.date)=" + p_month + " and date_part('year',sp.date)=" + p_year + " and "
                        + "sp.section='S13' order by sp.section,sp.subsection,dm.orderno";

            } else if (Integer.valueOf(reporttype) == 2) {

                EmployeeDetails_Query = "select sp.id, sp.employeeprovidentfundnumber, em.employeename, sm.sectionname, dm.designation, rm.regionname, "
                        + "date_part('year',sp.date) as year, date_part('month',sp.date) as month from supplementatypaybill sp "
                        + "left join employeemaster em on em.epfno = sp.employeeprovidentfundnumber "
                        + "left join regionmaster rm on sp.accregion=rm.id "
                        + "left join designationmaster dm on sp.designation=dm.designationcode "
                        + "left join sectionmaster sm on sp.section=sm.id where "
                        + "sp.accregion='" + LoggedInRegion + "' and date_part('month',sp.date)=" + p_month + " and date_part('year',sp.date)=" + p_year + " and "
                        + "sp.section='S14' order by sp.section,sp.subsection,dm.orderno";

            } else if (Integer.valueOf(reporttype) == 3) {

                EmployeeDetails_Query = "select sp.id, sp.employeeprovidentfundnumber, em.employeename, sm.sectionname, dm.designation, rm.regionname, "
                        + "date_part('year',sp.date) as year, date_part('month',sp.date) as month from supplementatypaybill sp "
                        + "left join employeemaster em on em.epfno = sp.employeeprovidentfundnumber "
                        + "left join regionmaster rm on sp.accregion=rm.id "
                        + "left join designationmaster dm on sp.designation=dm.designationcode "
                        + "left join sectionmaster sm on sp.section=sm.id where "
                        + "sp.accregion='" + LoggedInRegion + "' and date_part('month',sp.date)=" + p_month + " and date_part('year',sp.date)=" + p_year + " and "
                        + "sp.section not in('S13','S14') order by sp.section,sp.subsection,dm.orderno";
            }

//            if (Integer.valueOf(reporttype) == 1) {
//
//                EmployeeDetails_Query = "select sp.id, sp.employeeprovidentfundnumber, em.employeename, sm.sectionname, dm.designation, rm.regionname, "
//                        + "date_part('year',sp.date) as year, date_part('month',sp.date) as month from supplementatypaybill sp, employeemaster em,"
//                        + "regionmaster rm,designationmaster dm,sectionmaster sm where date_part('month',sp.date) = " + p_month + " and date_part('year',sp.date) = " + p_year + " and "
//                        + "em.epfno = sp.employeeprovidentfundnumber and sp.accregion=rm.id and em.designation=dm.designationcode and sp.section='S13' "
//                        + "and rm.defaultregion is true and sp.section=sm.id  order by sp.section,sp.subsection,dm.orderno";
//
//            } else if (Integer.valueOf(reporttype) == 2) {
//
//                EmployeeDetails_Query = "select sp.id, sp.employeeprovidentfundnumber, em.employeename, sm.sectionname, dm.designation, rm.regionname, "
//                        + "date_part('year',sp.date) as year, date_part('month',sp.date) as month from supplementatypaybill sp, employeemaster em,"
//                        + "regionmaster rm,designationmaster dm,sectionmaster sm where date_part('month',sp.date) = " + p_month + " and date_part('year',sp.date) = " + p_year + " and "
//                        + "em.epfno = sp.employeeprovidentfundnumber and sp.accregion=rm.id and em.designation=dm.designationcode and sp.section='S14' "
//                        + "and rm.defaultregion is true and sp.section=sm.id  order by sp.section,sp.subsection,dm.orderno";
//
//            } else if (Integer.valueOf(reporttype) == 3) {
//
//                EmployeeDetails_Query = "select sp.id, sp.employeeprovidentfundnumber, em.employeename, sm.sectionname, dm.designation, rm.regionname, "
//                        + "date_part('year',sp.date) as year, date_part('month',sp.date) as month from supplementatypaybill sp, employeemaster em, "
//                        + "regionmaster rm,designationmaster dm,sectionmaster sm where date_part('month',sp.date) = " + p_month + " and date_part('year',sp.date) = " + p_year + " and "
//                        + "em.epfno = sp.employeeprovidentfundnumber and sp.accregion=rm.id and em.designation=dm.designationcode and sp.section "
//                        + "not in('S13','S14') and rm.defaultregion is true and sp.section=sm.id  order by sp.section,sp.subsection,dm.orderno";
//
//            }

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
                double mon_double = (Double) rows[7];
                int mon_int = (int) mon_double;
//                System.out.println("mon_int = " + mon_int);
//                System.out.println("months[mon_int - 1] = " + months[mon_int - 1]);
                psm.setPayslipmonth(months[mon_int - 1]);

                String Deduction_Query = "select edt.deductionmasterid,pm.paycodename,sum(edt.amount) from  supplementaryemployeedeductionstransactions edt "
                        + "left join paycodemaster pm on edt.deductionmasterid = pm.paycode left join supplementarypayrollprocessingdetails spbpd on "
                        + "spbpd.id=edt.supplementarypayrollprocessingdetailsid left join supplementatypaybill spb on spb.id=spbpd.supplementatypaybillid "
                        + "where spb.id='" + earn_dedu_id + "' and spbpd.cancelled is false and edt.cancelled is false and edt.deductionmasterid='" + Deductionid + "' and pm.paycode='" + Deductionid + "' "
                        + "group by edt.deductionmasterid,pm.paycode  order by pm.paycode";

                String Earnings_Query = "select eet.earningmasterid,pm.paycodename,sum(eet.amount) from  supplementaryemployeeearningstransactions eet "
                        + "left join paycodemaster pm on eet.earningmasterid = pm.paycode left join supplementarypayrollprocessingdetails spbpd on "
                        + "spbpd.id=eet.supplementarypayrollprocessingdetailsid left join supplementatypaybill spb on spb.id=spbpd.supplementatypaybillid "
                        + "where spb.id='" + earn_dedu_id + "' and spbpd.cancelled is false and eet.cancelled is false and eet.earningmasterid='" + Deductionid + "' and pm.paycode='" + Deductionid + "' "
                        + "group by eet.earningmasterid,pm.paycode  order by pm.paycode";

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

                        String LoanInstall_Query = "select employeeloansandadvances.loanamount,employeeloansandadvances.totalinstallment, "
                                + "employeeloansandadvances.currentinstallment from supplementaryemployeeloansandadvancesdetails left join "
                                + "supplementarypayrollprocessingdetails on "
                                + "supplementarypayrollprocessingdetails.id=supplementaryemployeeloansandadvancesdetails.supplementarypayrollprocessingdetailsid "
                                + "left join employeeloansandadvances on employeeloansandadvances.id=supplementaryemployeeloansandadvancesdetails.employeeloansandadvancesid "
                                + "left join supplementatypaybill on supplementatypaybill.id=supplementarypayrollprocessingdetails.supplementatypaybillid left join paycodemaster "
                                + "on paycodemaster.paycode=employeeloansandadvances.deductioncode left join employeemaster on employeemaster.epfno= supplementatypaybill.employeeprovidentfundnumber "
                                + "where supplementatypaybill.id='" + earn_dedu_id + "' and supplementaryemployeeloansandadvancesdetails.cancelled is false and employeeloansandadvances.deductioncode='" + Deductionid + "' "
                                + "order by paycodemaster.paycode";

                        SQLQuery loanquery = session.createSQLQuery(LoanInstall_Query);

                        if (loanquery.list().size() > 0) {
                            for (ListIterator itl = loanquery.list().listIterator(); itl.hasNext();) {
                                Object[] ro = (Object[]) itl.next();
                                String totalinstallment = "";
                                String currentinstallment = "";
                                if (ro[1] != null) {
                                    totalinstallment = ro[1].toString();
                                }
                                if (ro[2] != null) {
                                    currentinstallment = ro[2].toString();
                                }
                                psm.setInstallment("(" + currentinstallment + "/" + totalinstallment + ")");
                            }
                        } else {
                            psm.setInstallment("");
                        }
//                        System.out.println("************************************************");
//                        System.out.println("psm.getDeductionname() ->"+psm.getDeductionname());
//                        System.out.println("earn_dedu_id ->" + earn_dedu_id);
//                        System.out.println("psm.getBranch() ->" + psm.getBranch());
//                        System.out.println("psm.getSectionname() ->" + psm.getSectionname());
//                        System.out.println("psm.getPayslipyear()->" + psm.getPayslipyear());
//                        System.out.println("psm.getPayslipmonth() ->" + psm.getPayslipmonth());
//                        System.out.println(psm.getSlipno()+"\t"+psm.getPfno()+"\t"+psm.getEmployeename()+"\t"+psm.getDesignation()+"\t"+psm.getAmount()+"\t"+psm.getInstallment());
//                        System.out.println("************************************************");
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

//                        System.out.println("************************************************");
//                        System.out.println("psm.getDeductionname() ->"+psm.getDeductionname());
//                        System.out.println("earn_dedu_id ->" + earn_dedu_id);
//                        System.out.println("psm.getBranch() ->" + psm.getBranch());
//                        System.out.println("psm.getSectionname() ->" + psm.getSectionname());
//                        System.out.println("psm.getPayslipyear()->" + psm.getPayslipyear());
//                        System.out.println("psm.getPayslipmonth() ->" + psm.getPayslipmonth());
//                        System.out.println(psm.getSlipno()+"\t"+psm.getPfno()+"\t"+psm.getEmployeename()+"\t"+psm.getDesignation()+"\t"+psm.getAmount()+"\t"+psm.getInstallment());
//                        System.out.println("************************************************");
                        dar.getDeductionAllPrintWriter(psm, filePath);
                        slipno++;
                    }

                }
//                psm.setPrintingrecordsize(printingrecordsize);
//                ledgerReport.getDeductionLedgerPrintWriter(psm, filePath);

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

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryLICSchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* EmployeePayBillServiceImpl class EmployeeLICSchedulePrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            SupplementaryLICScheduleReport supplementaryLICScheduleReport = new SupplementaryLICScheduleReport();
            PaySlipModel psm;
            PaySlip_Earn_Deduction_Model psedm;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String earn_dedu_id = null;
            String salarystructureid = null;

            EmployeeDetails_Query = "select sp.employeeprovidentfundnumber, sp.id, date_part('year',sp.date) as year, date_part('month',sp.date) as month,"
                    + "em.employeename, rm.regionname, dm.designation, em.employeecode, sp.section from supplementatypaybill sp "
                    + "left join employeemaster em on em.epfno = sp.employeeprovidentfundnumber "
                    + "left join regionmaster rm on sp.accregion=rm.id "
                    + "left join designationmaster dm on sp.designation=dm.designationcode where "
                    + "date_part('month',sp.date)=" + p_month + " and date_part('year',sp.date)=" + p_year + " "
                    + "and sp.section not in('S13','S14') and sp.type='SUPLEMENTARYBILL' "
                    + "and sp.accregion='" + LoggedInRegion + "' order by sp.section,dm.orderno";

//            EmployeeDetails_Query = "select sp.employeeprovidentfundnumber, sp.id, date_part('year',sp.date) as year, date_part('month',sp.date) as month,"
//                    + "em.employeename, rm.regionname, dm.designation, em.employeecode, sp.section from supplementatypaybill sp, employeemaster em,"
//                    + "regionmaster rm,designationmaster dm where date_part('month',sp.date) = " + p_month + " and date_part('year',sp.date) = " + p_year + " and "
//                    + "em.epfno = sp.employeeprovidentfundnumber and sp.accregion=rm.id and em.designation=dm.designationcode and "
//                    + "sp.section not in('S13','S14') and sp.type='SUPLEMENTARYBILL' and rm.defaultregion is true order by sp.section,dm.orderno";

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
                psm.setRecordno(recordno);
                psm.setPfno((String) rows[0]);
                earn_dedu_id = ((String) rows[1]);

                psm.setPayslipyear(rows[2].toString());
                double mon_double = (Double) rows[3];
                int mon_int = (int) mon_double;
                psm.setPayslipmonth(months[mon_int - 1]);

                psm.setEmployeename((String) rows[4]);
                psm.setBranch((String) rows[5]);
                if (rows[6] != null) {
                    psm.setDesignation(DeduStringSet((String) rows[6]));
                } else {
                    psm.setDesignation("");
                }
                psm.setEmpno((String) rows[7]);
                psm.setPrintingrecordsize(printingrecordsize);

                String Payroll_Processing_Query = "select id,calculatedyear,calculatedmonth from supplementarypayrollprocessingdetails "
                        + "where supplementatypaybillid = '" + earn_dedu_id + "' and cancelled is false";

                String LIC_Policy_Query = "select deductionmasterid,dedn_no from supplementaryemployeedeductiondetails sedd "
                        + "left join supplementarysalarystructure sss on sedd.supplementarysalarystructureid=sss.id where "
                        + "sss.supplementatypaybillid='" + earn_dedu_id + "' and sedd.cancelled is false";

                SQLQuery licPolicyquery = session.createSQLQuery(LIC_Policy_Query);

                Map<String, String> licmap = new HashMap<String, String>();

                for (ListIterator it = licPolicyquery.list().listIterator(); it.hasNext();) {
                    Object[] row = (Object[]) it.next();
                    String deductionmasterid = (String) row[0];
                    String policyno = (String) row[1];
                    licmap.put(deductionmasterid, policyno);
                }


                SQLQuery payrollprocessingquery = session.createSQLQuery(Payroll_Processing_Query);
                for (ListIterator it1 = payrollprocessingquery.list().listIterator(); it1.hasNext();) {
                    Object[] row1 = (Object[]) it1.next();
                    String payprocessingdetailsid = (String) row1[0];
                    String calculatedyear = row1[1].toString();
                    String calculatedmonth = row1[2].toString();

                    String LIC_Amount_Query = "select deductionmasterid,amount from supplementaryemployeedeductionstransactions where "
                            + "supplementarypayrollprocessingdetailsid ='" + payprocessingdetailsid + "' and cancelled is false and deductionmasterid in (select paycode from "
                            + "paycodemaster where grouphead = 'LIC')";

                    SQLQuery licamountquery = session.createSQLQuery(LIC_Amount_Query);

                    if (licamountquery.list().size() > 0) {

                        List<PaySlip_Earn_Deduction_Model> liclist = new ArrayList<PaySlip_Earn_Deduction_Model>();
                        for (ListIterator it = licamountquery.list().listIterator(); it.hasNext();) {
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
                        supplementaryLICScheduleReport.getLICSchedulePrintWriter(psm, filePath);
                        slipno++;
                    }
                }
//                System.out.println("recordno = " + recordno);
//                System.out.println("psm.getPrintingrecordsize() = " + psm.getPrintingrecordsize());
                recordno++;
            }

            supplementaryLICScheduleReport.LICScheduleGrandTotal(filePath);
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "LIC Schedule Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryPayDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class EmployeeSupplementaryDeductionAllPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            SupplementaryEPFDBFReport sepfdbfr = new SupplementaryEPFDBFReport();
            PaySlipModel psm = null;
            String PayDBF_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);

//            PayDBF_Query = "select em.epfno, em.employeecode, spb.id, spb.type  from supplementatypaybill spb "
            PayDBF_Query = "select left(split_part(em.epfno, '/',1),6) as epfno, left(split_part(em.employeecode, '/',1),6) as employeecode, spb.id, spb.type  from supplementatypaybill spb "
                    + "left join employeemaster em on spb.employeeprovidentfundnumber=em.epfno "
                    + "left join regionmaster rm on spb.accregion=rm.id where date_part('month',spb.date)=" + p_month + " "
                    + "and date_part('year',spb.date)=" + p_year + " and spb.accregion='" + LoggedInRegion + "' order by spb.employeeprovidentfundnumber";

//            PayDBF_Query = "select em.epfno, em.employeecode, spb.id, spb.type  from supplementatypaybill spb "
//                    + "left join employeemaster em on spb.employeeprovidentfundnumber=em.epfno "
//                    + "left join regionmaster rm on spb.accregion=rm.id where date_part('month',spb.date) = " + p_month + " "
//                    + "and date_part('year',spb.date) = " + p_year + " and rm.defaultregion is true order by "
//                    + "spb.employeeprovidentfundnumber";
//
            SQLQuery paydpfquery = session.createSQLQuery(PayDBF_Query);
            if (paydpfquery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = paydpfquery.list().size();
            for (ListIterator its = paydpfquery.list().listIterator(); its.hasNext();) {
                Object[] rows = (Object[]) its.next();

                String pfno = (String) rows[0];

                String employeeno;
                if (rows[1] != null) {
                    employeeno = (String) rows[1];
                } else {
                    employeeno = "";
                }
                String paybillid = (String) rows[2];
                String billtype = (String) rows[3];

                String Earning_Query = "SELECT sppd.calculatedyear, sppd.calculatedmonth,seet.earningmasterid, seet.amount FROM "
                        + "supplementarypayrollprocessingdetails  sppd left join supplementaryemployeeearningstransactions seet on "
                        + "seet.supplementarypayrollprocessingdetailsid = sppd.id where supplementatypaybillid='" + paybillid + "' and sppd.cancelled is false "
                        + "and seet.cancelled is false";

                SQLQuery earingsquery = session.createSQLQuery(Earning_Query);

                for (ListIterator it1 = earingsquery.list().listIterator(); it1.hasNext();) {
                    Object[] row1 = (Object[]) it1.next();
                    psm = new PaySlipModel();
                    if (row1[0] != null) {
                        psm.setPayslipyear((row1[0].toString()).substring(2, 4));
                    } else {
                        psm.setPayslipyear("");
                    }
                    if (row1[1] != null) {
                        int calculatedmonth = (Integer) row1[1];
                        psm.setPayslipmonth(months[calculatedmonth - 1]);
                    } else {
                        psm.setPayslipmonth("");
                    }
                    psm.setPaycode((String) row1[2]);
                    psm.setAmount(row1[3].toString());
                    psm.setPfno(pfno);
                    psm.setBilltype(billtype);
                    psm.setEmpno(employeeno);

//                    System.out.println("*******************************************************************");
//                    System.out.println("psm.getSlipno = " + psm.getSlipno());
//                    System.out.println("psm.getEmpno() = " + psm.getEmpno());
//                    System.out.println("psm.getPaycode() = " + psm.getPaycode());
//                    System.out.println("psm.getAmount() = " + psm.getAmount());
//                    System.out.println("psm.getPfno() = " + psm.getPfno());
//                    System.out.println("psm.getPayslipmonth() = " + psm.getPayslipmonth());
//                    System.out.println("psm.getPayslipyear() = " + psm.getPayslipyear());
//                    System.out.println("psm.getBilltype() = " + psm.getBilltype());
//                    System.out.println("*******************************************************************");


                    sepfdbfr.getEPFformPrintWriter(psm, filePath);
                    slipno++;
                }
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    private static String DeduStringSet(String str) {
        if (str.length() > 10) {
            return str.substring(0, 10);
        } else {
            return str;
        }
    }

    private static String EarnStringSet(String str) {
        if (str.length() > 9) {
            return str.substring(0, 9);
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

    public Map getEmployeeEarningsforIA(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        HashMap resultMap = new HashMap();
        HashMap earningsMap = new HashMap();

        int slno = 0;


        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);
//                resultMap.put("salarystructureid", salarystructureObj.getId());
//                resultMap.put("salaryeffectdat", dateToString(salarystructureObj.getPeriodfrom()));
//                resultMap.put("salaryeffectorderno", salarystructureObj.getOrderno());
                Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salarystructureObj.getId() + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List earDetailsList = earDetailsCrit.list();
//                resultMap.put("employeeearningslength", earDetailsList.size());
                if (earDetailsList.size() > 0) {
                    for (int j = 0; j < earDetailsList.size(); j++) {
                        Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);
                        earningsMap.put(slno, getPaycodeDetails(session, employeeearningsdetailsactualObj.getEarningmasterid()));
                        earningsMap.put(slno + earDetailsList.size(), employeeearningsdetailsactualObj.getAmount());
                        slno = slno + 1;
                    }

                }

            } else {
                earningsMap.put("ERROR", "ERROR");
            }

            resultMap.put(
                    "earningslist", getEarningsList(session));
            resultMap.put(
                    "employeeearningslist", earningsMap);
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

            if (earList.size()
                    > 0) {
                paycodemasterObj = (Paycodemaster) earList.get(0);
                resultString = paycodemasterObj.getPaycodename();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultString;
    }

    public Map getEarningsList(Session session) {
        Map resultMap = new HashMap();
        Paycodemaster paycodemasterObj;
        int slno = 0;


        try {
            Criteria earCrit = session.createCriteria(Paycodemaster.class);
            earCrit.add(Restrictions.sqlRestriction("paycodetype ='E'"));
            List earList = earCrit.list();

            if (earList.size()
                    > 0) {
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

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getIncrementEarnings(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber) {
//        String regionCode = (String) request.getSession(false).getAttribute("regioncode");
//        System.out.println("Region Code Taken From Session" + regionCode);
        Map resultMap = new HashMap();
        resultMap.put("earningslist", getEarningsList(session));
        resultMap.put("employeeearningslist", getEmployeeEarningsList(session, employeeProvidentFundNumber, LoggedInRegion));
        return resultMap;

    }

    public Map getEmployeeEarningsList(Session session, String employeeProvidentFundNumber, String LoggedInRegion) {
        Map resultMap = new HashMap();
        int slno = 0;


        try {
            Criteria lrCrit = session.createCriteria(Salarystructureactual.class);
            lrCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeeProvidentFundNumber + "' "));
            lrCrit.add(Restrictions.sqlRestriction("periodto is null "));
            lrCrit.addOrder(Order.desc("periodfrom"));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Salarystructureactual salarystructureObj = (Salarystructureactual) ldList.get(0);
                resultMap.put("salarystructureid", salarystructureObj.getId());
                resultMap.put("salaryeffectdat", dateToString(salarystructureObj.getPeriodfrom()));
                resultMap.put("salaryeffectorderno", salarystructureObj.getOrderno());
                Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salarystructureObj.getId() + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List earDetailsList = earDetailsCrit.list();
                resultMap.put("employeeearningslength", earDetailsList.size());
                if (earDetailsList.size() > 0) {
                    for (int j = 0; j < earDetailsList.size(); j++) {
                        Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);
                        resultMap.put(slno, getPaycodeDetails(session, employeeearningsdetailsactualObj.getEarningmasterid()));
                        resultMap.put(slno + earDetailsList.size(), employeeearningsdetailsactualObj.getAmount());
                        System.out.println(slno + "            " + employeeearningsdetailsactualObj.getAmount());
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

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDueDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String suppaybillid) {
//        String regionCode = (String) request.getSession(false).getAttribute("regioncode");
//        System.out.println("Region Code Taken From Session" + regionCode);
        Map resultMap = new HashMap();
        resultMap.put("earningslist", getEarningsList(session));
        resultMap.put("employeeearningslist", getDueList(session, epfno, month, year, suppaybillid, LoggedInRegion));
        return resultMap;
    }

    public Map getDueList(Session session, String epfno, String month, String year, String supsalstrucidid, String LoggedInRegion) {
        //System.out.println(supsalstrucidid);
        Map resultMap = new HashMap();
        String[] earcodes = {"E01", "E02", "E04", "E06", "E07", "E25"};
        resultMap.put("employeeearningslength", earcodes.length);
        int slno = 0;
        try {
            for (int i = 0; i < earcodes.length; i++) {
                Criteria earDetailsCrit = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + supsalstrucidid + "' "));
                earDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid = '" + earcodes[i] + "' "));

                List earDetailsList = earDetailsCrit.list();

                if (earDetailsList.size()
                        > 0) {
                    for (int j = 0; j < earDetailsList.size(); j++) {
                        Supplementaryemployeeearningsdetails employeeearningsdetailsactualObj = (Supplementaryemployeeearningsdetails) earDetailsList.get(j);
                        resultMap.put(slno, employeeearningsdetailsactualObj.getEarningmasterid());
                        resultMap.put(slno + earcodes.length, employeeearningsdetailsactualObj.getAmount());
                        //System.out.println(slno + "            " + employeeearningsdetailsactualObj.getAmount());
                        slno = slno + 1;
                    }

                } else {
                    resultMap.put(slno, earcodes[i]);
                    resultMap.put(slno + earcodes.length, new BigDecimal("0"));
                    //System.out.println(slno + "            " + new BigDecimal("0"));
                    slno = slno + 1;
                }
            }
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map updateincrementarreardue(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String supsalstrucidid, String earningCode, String earningAmount) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Supplementaryemployeeearningsdetails earningsdetObj = null;
//        System.out.println("Code" + earningCode);
        try {
            String[] earningcodeArr = earningCode.split("TNCSCSEPATOR");
            String[] earningArr = earningAmount.split("TNCSCSEPATOR");
//            System.out.println(earningcodeArr.length);
            for (int i = 0; i < earningcodeArr.length; i++) {
//                System.out.println(supsalstrucidid + earningcodeArr[i]);
                Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + supsalstrucidid + "' "));
                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earningcodeArr[i] + "' "));
                List earList = earDe.list();

                if (earList.size()
                        > 0) {
//                    System.out.println("Sairam Sairam");
                    earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                    earningsdetObj.setCancelled(Boolean.FALSE);
                    earningsdetObj.setAmount(new BigDecimal(earningArr[i]));
                    System.out.println(earningcodeArr[i] + " " + earningArr[i]);
                    transaction = session.beginTransaction();
                    session.update(earningsdetObj);
                    transaction.commit();
                } else {
                    System.out.println(earningcodeArr[i] + " " + earningArr[i]);
                    earningsdetObj = new Supplementaryemployeeearningsdetails();
                    earningsdetObj.setCancelled(Boolean.FALSE);
                    earningsdetObj.setAmount(new BigDecimal(earningArr[i]));
                    earningsdetObj.setEarningmasterid(earningcodeArr[i]);
                    earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, supsalstrucidid, LoggedInRegion));
                    earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                    earningsdetObj.setAccregion(LoggedInRegion);

                    earningsdetObj.setAmount(new BigDecimal(earningArr[i]));

                    transaction = session.beginTransaction();
                    session.save(earningsdetObj);
                    transaction.commit();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getIncrementEarningsUpdation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String asondate, String designationname) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Employeemaster employeemasterObj = null;

        try {
            if ("Retired Employee".equalsIgnoreCase(designationname)) {
                employeemasterObj = getEmployeeDetails(session, epfno, "RET");
            } else {
                employeemasterObj = getEmployeeDetails(session, epfno, LoggedInRegion);
            }
            Supplementatypaybill supplementatypaybillObj = new Supplementatypaybill();
            String supPayBillId = getSupplementatypaybillid(session, LoggedInRegion);
            supplementatypaybillObj.setId(supPayBillId);
            supplementatypaybillObj.setAccregion(LoggedInRegion);
            supplementatypaybillObj.setDate(postgresDate(asondate));
            supplementatypaybillObj.setEmployeemaster(employeemasterObj);
            if (!month.equalsIgnoreCase("null")) {
                supplementatypaybillObj.setType("INCREMENTMANUAL");
            } else {
                supplementatypaybillObj.setType("SLINCREMENTMANUAL");
            }

            supplementatypaybillObj.setSection(employeemasterObj.getSection());
            supplementatypaybillObj.setSubsection(employeemasterObj.getSubsection());
            supplementatypaybillObj.setPaymentmode(employeemasterObj.getPaymentmode());
            supplementatypaybillObj.setEmployeecategory(employeemasterObj.getCategory());
            supplementatypaybillObj.setDesignation(employeemasterObj.getDesignation());
            supplementatypaybillObj.setAccregion(LoggedInRegion);
            supplementatypaybillObj.setCancelled(Boolean.FALSE);

            transaction = session.beginTransaction();
            session.save(supplementatypaybillObj);
            transaction.commit();

            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();
            String id = getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion);
            supplementarypayrollprocessingdetailsObj.setId(id);
            supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
            supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
            if (!month.equalsIgnoreCase("null")) {
                supplementarypayrollprocessingdetailsObj.setCalculatedmonth(Integer.parseInt(month));
            } else {
                supplementarypayrollprocessingdetailsObj.setType("SLINCREMENTMANUAL");
            }
            if (!year.equalsIgnoreCase("null")) {
                supplementarypayrollprocessingdetailsObj.setCalculatedyear(Integer.parseInt(year));
            }

            supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);


            transaction = session.beginTransaction();
            session.persist(supplementarypayrollprocessingdetailsObj);
            transaction.commit();

            String[] earcodes = {"E01", "E02", "E04", "E06", "E07", "E25"};
            for (int k = 0; k < earcodes.length; k++) {

                Supplementaryemployeeearningstransactions earningsdetObj = new Supplementaryemployeeearningstransactions();
                earningsdetObj.setCancelled(Boolean.FALSE);
                earningsdetObj.setAmount(BigDecimal.ZERO);
                earningsdetObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                earningsdetObj.setEarningmasterid(earcodes[k]);
                String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                earningsdetObj.setId(earningsTransactionId);
                earningsdetObj.setAccregion(LoggedInRegion);
                transaction = session.beginTransaction();
                session.save(earningsdetObj);
                transaction.commit();
            }


            resultMap.put("subpayproid", supplementarypayrollprocessingdetailsObj.getId());
            resultMap.put("earningslist", getEarningsList(session));
            resultMap.put("employeeearningslist", getIncrementManual(session, supplementarypayrollprocessingdetailsObj.getId(), LoggedInRegion));
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    public Map getIncrementManual(Session session, String suppayprocesid, String LoggedInRegion) {
//        System.out.println(suppayprocesid);
        Map resultMap = new HashMap();
        int slno = 0;


        try {
            Criteria earDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
            earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            earDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + suppayprocesid + "' "));
            List earDetailsList = earDetailsCrit.list();

            resultMap.put(
                    "employeeearningslength", earDetailsList.size());
            if (earDetailsList.size()
                    > 0) {
                for (int j = 0; j < earDetailsList.size(); j++) {
                    Supplementaryemployeeearningstransactions employeeearningsObj = (Supplementaryemployeeearningstransactions) earDetailsList.get(j);
                    resultMap.put(slno, employeeearningsObj.getEarningmasterid());
                    resultMap.put(slno + earDetailsList.size(), employeeearningsObj.getAmount());
//                    System.out.println(slno + "            " + employeeearningsObj.getAmount());
                    slno = slno + 1;
                }

            }
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map updateincrementarrearmanual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String supsalstrucidid, String earningCode, String earningAmount) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
//        System.out.println("Code" + earningCode);
        try {
            String[] earningcodeArr = earningCode.split("TNCSCSEPATOR");
            String[] earningArr = earningAmount.split("TNCSCSEPATOR");
//        System.out.println(earningcodeArr.length);
            for (int i = 0; i < earningcodeArr.length; i++) {
//            System.out.println(supsalstrucidid + earningcodeArr[i]);
                Criteria earDe = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                earDe.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supsalstrucidid + "' "));
                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earningcodeArr[i] + "' "));
                List earList = earDe.list();

                if (earList.size()
                        > 0) {
                    Supplementaryemployeeearningstransactions earningsdetObj = (Supplementaryemployeeearningstransactions) earList.get(0);
                    earningsdetObj.setCancelled(Boolean.FALSE);
                    earningsdetObj.setCreatedby(LoggedInUser);
                    earningsdetObj.setCreateddate(getCurrentDate());
                    earningsdetObj.setAmount(new BigDecimal(earningArr[i]));

                    transaction = session.beginTransaction();
                    session.update(earningsdetObj);
                    transaction.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getManIncDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String processid) {
//        String regionCode = (String) request.getSession(false).getAttribute("regioncode");
//        System.out.println("Region Code Taken From Session" + regionCode);
        Map resultMap = new HashMap();
        resultMap.put("subpayproid", processid);
        resultMap.put("earningslist", getEarningsList(session));
        resultMap.put("employeeearningslist", getManIncList(session, epfno, month, year, processid, LoggedInRegion));
        return resultMap;
    }

    public Map getManIncList(Session session, String epfno, String month, String year, String processid, String LoggedInRegion) {
//        System.out.println(processid);
        Map resultMap = new HashMap();
        int slno = 0;


        try {
            Criteria earDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
            earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            earDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + processid + "' "));
            List earDetailsList = earDetailsCrit.list();

            resultMap.put(
                    "employeeearningslength", earDetailsList.size());
            if (earDetailsList.size()
                    > 0) {
                for (int j = 0; j < earDetailsList.size(); j++) {
                    Supplementaryemployeeearningstransactions employeeearningsdetailsactualObj = (Supplementaryemployeeearningstransactions) earDetailsList.get(j);
                    resultMap.put(slno, employeeearningsdetailsactualObj.getEarningmasterid());
                    resultMap.put(slno + earDetailsList.size(), employeeearningsdetailsactualObj.getAmount());
//                    System.out.println(slno + "            " + employeeearningsdetailsactualObj.getAmount());
                    slno = slno + 1;
                }

            }
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveIncrementArrearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate) {
        Map resultMap = new HashMap();
        long totalearpaid = 0;
        Transaction transaction = null;
        Supplementatypaybill supplementatypaybillObj = null;
        String[] earcodes = {"E01", "E02", "E04", "E06", "E07", "E25"};
//        long[] total = {0, 0, 0, 0, 0, 0};
        long[] subtotal = {0, 0, 0, 0, 0, 0};
        String subvec = "";
        try {
            Criteria supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
            supPayBillCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("type='INCREMENTARREAR'"));
            supPayBillCrit.add(Restrictions.sqlRestriction("cancelled is false"));

            List<Supplementatypaybill> supPayBillList = supPayBillCrit.list();
            if (supPayBillList.size() > 0) {
                // <editor-fold defaultstate="collapsed">
                supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(0);
                StringBuffer resultHTML = new StringBuffer();
                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                resultHTML.append("<tr class=\"gridmenu\">");
                resultHTML.append("<td align=\"right\" >");
                resultHTML.append("Particulars");
                resultHTML.append("</td>");
                for (int k = 0; k < earcodes.length; k++) {
                    resultHTML.append("<td align=\"right\" >");
                    resultHTML.append(getPaycodeMater(session, earcodes[k]).getPaycodename());
                    resultHTML.append("</td>");
                }
                resultHTML.append("<td align=\"right\" >");
                resultHTML.append("Balance");
                resultHTML.append("</td>");
                resultHTML.append("</tr>");

                Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = null;
                Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                supPayrollProcesCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
                supPayrollProcesCrit.addOrder(Order.asc("calculatedyear"));
                supPayrollProcesCrit.addOrder(Order.asc("calculatedmonth"));
                List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();
                if (supPayProcList.size() > 0) {
                    for (int i = 0; i < supPayProcList.size(); i++) {
                        // <editor-fold defaultstate="collapsed">
                        for (int p = 0; p < subtotal.length; p++) {
                            subtotal[p] = 0;
                        }
                        supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(i);
                        resultHTML.append("<tr class=\"gridmenu\">");
                        resultHTML.append("<td colspan=\"" + (earcodes.length + 1) + "\" align=\"left\" >");
                        //System.out.println(":::::::::::::::: " + supplementarypayrollprocessingdetailsObj.getType());
                        if (supplementarypayrollprocessingdetailsObj.getType() != null) {
                            if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                resultHTML.append(supplementarypayrollprocessingdetailsObj.getType());
                            } else {
                                resultHTML.append(supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                            }
                        } else {
                            resultHTML.append(supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                        }
                        resultHTML.append("</td>");
                        resultHTML.append("<td>");
                        resultHTML.append(" <input type=\"button\" CLASS=\"submitbu\" name=\"adddrawn\" id=\"adddrawn\" value=\"+ Drawn\"  onclick=\"showdrwandetailsforupdation('" + epfno + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "');\"  >");
                        resultHTML.append("</td>");
                        resultHTML.append("</tr>");

                        Supplementarysalarystructure supplementarysalarystructureObj;
                        Criteria supSalStruCrit = session.createCriteria(Supplementarysalarystructure.class);
                        supSalStruCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));
                        supSalStruCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        List<Supplementarysalarystructure> supSalStruList = supSalStruCrit.list();
                        if (supSalStruList.size() > 0) {
                            // <editor-fold defaultstate="collapsed">
                            supplementarysalarystructureObj = (Supplementarysalarystructure) supSalStruList.get(0);
                            resultHTML.append("<tr >");
                            resultHTML.append("<td align=\"left\" >");
                            resultHTML.append("Due");
                            resultHTML.append("</td>");

                            for (int k = 0; k < earcodes.length; k++) {
                                // <editor-fold defaultstate="collapsed">
                                Supplementaryemployeeearningsdetails earningsdetObj;
                                Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + supplementarysalarystructureObj.getId() + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earcodes[k] + "' "));
                                earDe.add(Restrictions.sqlRestriction("cancelled is false"));
                                List<Supplementaryemployeeearningsdetails> earList = earDe.list();
                                if (earList.size() > 0) {
                                    earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                                    resultHTML.append("<td align=\"right\" >");
                                    resultHTML.append(earningsdetObj.getAmount().setScale(2));
                                    subtotal[k] = subtotal[k] + (long) earningsdetObj.getAmount().floatValue();
                                    resultHTML.append("</td>");
                                } else {
                                    resultHTML.append("<td align=\"right\" >");
                                    resultHTML.append("0.00");
                                    resultHTML.append("</td>");
                                }
                                // </editor-fold>
                            }
                            resultHTML.append("<td>");
                            resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showDueDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + supplementarysalarystructureObj.getId() + "')\">");
                            resultHTML.append("</td>");
                            resultHTML.append("</tr>");

                            if (supplementarypayrollprocessingdetailsObj.getType() == null) {
                                // <editor-fold defaultstate="collapsed">
                                //Regular
                                Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                                empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("process is true"));
                                List empPayProcessDetails = empPayProcessDetailsCrit.list();
                                if (empPayProcessDetails.size() > 0) {
                                    Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(0);
                                    resultHTML.append("<tr>");
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("Drawn Regular");
                                    resultHTML.append("</td>");

                                    //Incrementarrearreference
                                    Criteria empIncRefCrit = session.createCriteria(Incrementarrearreference.class);
                                    empIncRefCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                    empIncRefCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supplementatypaybillObj.getId() + "'"));
                                    empIncRefCrit.add(Restrictions.sqlRestriction("type='" + "REGULAR" + "'"));
                                    empIncRefCrit.add(Restrictions.sqlRestriction("processid='" + payrollprocessingdetailsObj.getId() + "'"));
                                    List empIncRefList = empIncRefCrit.list();
                                    if (empIncRefList.size() > 0) {
                                        Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) empIncRefList.get(0);
                                        incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                        incrementarrearreferenceObj.setProcessid(payrollprocessingdetailsObj.getId());
                                        incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                        incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                        incrementarrearreferenceObj.setType("REGULAR");
                                        incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                        incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                        incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                                        transaction = session.beginTransaction();
                                        session.update(incrementarrearreferenceObj);
                                        transaction.commit();

                                    } else {
                                        Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                                        incrementarrearreferenceObj.setId(getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                                        incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                        incrementarrearreferenceObj.setProcessid(payrollprocessingdetailsObj.getId());
                                        incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                        incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                        incrementarrearreferenceObj.setType("REGULAR");
                                        incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                        incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                        incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                                        transaction = session.beginTransaction();
                                        session.save(incrementarrearreferenceObj);
                                        transaction.commit();
                                    }
                                    //Incrementarrearreference

                                    for (int k = 0; k < earcodes.length; k++) {
                                        // <editor-fold defaultstate="collapsed">

                                        Criteria empEarnDetailsCrit = session.createCriteria(Employeeearningstransactions.class);
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                        List empEarnDetailsList = empEarnDetailsCrit.list();
                                        if (empEarnDetailsList.size() > 0) {

                                            for (int j = 0; j < empEarnDetailsList.size(); j++) {
                                                Employeeearningstransactions employeeearningstransactionsObj = (Employeeearningstransactions) empEarnDetailsList.get(j);

                                                resultHTML.append("<td align=\"right\" >");
                                                resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                                //System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                                resultHTML.append("</td>");

                                            }

                                        } else {
                                            resultHTML.append("<td align=\"right\" >");
                                            resultHTML.append("0.00");
                                            resultHTML.append("</td>");
                                        }
                                        // </editor-fold>
                                    }
                                    resultHTML.append("<td align=\"right\" >");
                                    resultHTML.append("</td>");
                                    resultHTML.append("</tr>");
                                }

                                //Regular end
                                //Supplementaty
                                StringBuffer query = new StringBuffer();
                                query.append("select sppd.id as sppod, spb.type as spbtype, sppd.type as sppdtype, spb.id as spbid from supplementarypayrollprocessingdetails sppd ");
                                query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                                query.append(" where sppd.accregion='" + LoggedInRegion + "' and sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "'  and spb.cancelled is false and sppd.cancelled is false");
                                SQLQuery subpayquery = session.createSQLQuery(query.toString());
                                //System.out.println("query.toString() = " + query.toString());
                                //System.out.println("size" + subpayquery.list().size());
                                for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                                    // <editor-fold defaultstate="collapsed">
                                    Object[] rows = (Object[]) its.next();
                                    String payprocesid = (String) rows[0];
                                    String type = (String) rows[1];
                                    String type2 = (String) rows[2];
                                    String supid = (String) rows[3];

                                    //Incrementarrearreference
                                    Criteria empIncRefCrit = session.createCriteria(Incrementarrearreference.class);
                                    empIncRefCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                    empIncRefCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supplementatypaybillObj.getId() + "'"));
                                    empIncRefCrit.add(Restrictions.sqlRestriction("type='" + "SUPPLEMENTARY" + "'"));
                                    empIncRefCrit.add(Restrictions.sqlRestriction("processid='" + payprocesid + "'"));
                                    List empIncRefList = empIncRefCrit.list();
                                    if (empIncRefList.size() > 0) {
                                        Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) empIncRefList.get(0);
                                        incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                        incrementarrearreferenceObj.setProcessid(payprocesid);
                                        incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                        incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                        incrementarrearreferenceObj.setType("SUPPLEMENTARY");
                                        incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                        incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                        incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                                        transaction = session.beginTransaction();
                                        session.update(incrementarrearreferenceObj);
                                        transaction.commit();
                                    } else {
                                        Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                                        incrementarrearreferenceObj.setId(getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                                        incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                        incrementarrearreferenceObj.setProcessid(payprocesid);
                                        incrementarrearreferenceObj.setType("SUPPLEMENTARY");
                                        incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                        incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                        incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                        incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                        incrementarrearreferenceObj.setCreateddate(getCurrentDate());
                                        transaction = session.beginTransaction();
                                        session.save(incrementarrearreferenceObj);
                                        transaction.commit();
                                    }
                                    //Incrementarrearreference

                                    //System.out.println(payprocesid + type);
                                    //System.out.println(supid);
                                    resultHTML.append("<tr>");
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("Drawn" + type + "  " + payprocesid + "  " + supid);
                                    resultHTML.append("</td>");

                                    for (int k = 0; k < earcodes.length; k++) {
                                        // <editor-fold defaultstate="collapsed">
                                        //System.out.println(earcodes[k]);
                                        Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                        List empEarnDetailsList = empEarnDetailsCrit.list();
                                        if (empEarnDetailsList.size() > 0) {
                                            // <editor-fold defaultstate="collapsed">
                                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                            resultHTML.append("<td align=\"right\" >");
                                            if (!type.equalsIgnoreCase("DAARREAR")) {
                                                resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                            } else {
                                                // <editor-fold defaultstate="collapsed">
                                                if (subvec.trim().length() > 0) {
                                                    subvec = subvec + "," + "'" + supid + "'";
                                                } else {
                                                    subvec = subvec + "'" + supid + "'";
                                                }
                                                //System.out.println("subvec = " + subvec);
                                                float datott = 0;
                                                Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                                empIncCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supid + "'"));
                                                //empIncCrit.add(Restrictions.sqlRestriction("processid='" + payprocesid + "'"));
                                                empIncCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                                                empIncCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                                                List incArList = empIncCrit.list();
                                                if (incArList.size() > 0) {
                                                    for (int sr = 0; sr < incArList.size(); sr++) {
                                                        // <editor-fold defaultstate="collapsed">
                                                        Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                        if (!incrementarrearreferenceObj.getType().equalsIgnoreCase("LEAVESURRENDER")) {
                                                            //System.out.println("incrementarrearreferenceObj.getArrear().floatValue() = " + incrementarrearreferenceObj.getArrear().floatValue());
                                                            datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
                                                        } else {
                                                        }
                                                        // </editor-fold>
                                                    }
                                                }
                                                resultHTML.append(datott);
                                                subtotal[k] = subtotal[k] - (long) datott;
                                                // </editor-fold>
                                            }
                                            //System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                            resultHTML.append("</td>");
                                            // </editor-fold>
                                        } else {
                                            resultHTML.append("<td align=\"right\" > ");
                                            resultHTML.append("0.00");
                                            resultHTML.append("</td>");
                                        }
                                        // </editor-fold>

                                    }
                                    resultHTML.append("<td align=\"left\" >");
                                    if (type.equalsIgnoreCase("INCREMENTMANUAL")) {
                                        resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showManIncDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + payprocesid + "')\">");
                                    }
                                    resultHTML.append("</td>");
                                    resultHTML.append("</tr>");
                                    // </editor-fold>
                                }
                                //Supplementaty end
                                // </editor-fold>

                            } else {
                                // <editor-fold defaultstate="collapsed">
                                if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                    // <editor-fold defaultstate="collapsed">
                                    resultHTML.append("<tr>");
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("Drawn");
                                    resultHTML.append("</td>");
                                    supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
                                    supPayBillCrit.add(Restrictions.sqlRestriction("id = '" + supplementarypayrollprocessingdetailsObj.getTypeid() + "' "));
                                    supPayBillList = supPayBillCrit.list();
                                    if (supPayBillList.size() > 0) {
                                        // <editor-fold>
                                        supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(0);

                                        supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                                        supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                        supPayrollProcesCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
                                        supPayProcList = supPayrollProcesCrit.list();
                                        if (supPayProcList.size() > 0) {

                                            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj1 = (Supplementarypayrollprocessingdetails) supPayProcList.get(0);

                                            for (int k = 0; k < earcodes.length; k++) {
                                                // <editor-fold defaultstate="collapsed">
                                                //System.out.println(earcodes[k]);
                                                Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj1.getId() + "'"));
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                                List empEarnDetailsList = empEarnDetailsCrit.list();
                                                if (empEarnDetailsList.size() > 0) {
                                                    Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);

                                                    resultHTML.append("<td align=\"right\" >");
                                                    resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                    subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                                    //System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                                    resultHTML.append("</td>");
                                                } else {
                                                    resultHTML.append("<td align=\"right\" > ");
                                                    resultHTML.append("0.00");
                                                    resultHTML.append("</td>");
                                                }
                                                // </editor-fold>
                                            }
                                        }
                                        // </editor-fold>
                                    }
                                    resultHTML.append("<td align=\"left\" >");
                                    resultHTML.append("</td>");
                                    resultHTML.append("</tr>");
                                    //sub inc
                                    //System.out.println("subvec.trim() = " + subvec.trim());
                                    if (subvec.trim().length() > 0) {
                                        // <editor-fold defaultstate="collapsed">
                                        float datott = 0;
                                        Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                        empIncCrit.add(Restrictions.sqlRestriction("suppaybillid in(" + subvec + ")"));
                                        empIncCrit.add(Restrictions.sqlRestriction("type='" + "LEAVESURRENDER" + "'"));
                                        List incArList = empIncCrit.list();
                                        if (incArList.size() > 0) {
                                            for (int sr = 0; sr < incArList.size(); sr++) {
                                                // <editor-fold defaultstate="collapsed">
                                                resultHTML.append("<tr>");
                                                resultHTML.append("<td align=\"left\" >");
                                                resultHTML.append("Arrear Paid");
                                                resultHTML.append("</td>");
                                                for (int k1 = 0; k1 < earcodes.length; k1++) {
                                                    // <editor-fold defaultstate="collapsed">
                                                    if (earcodes[k1].equalsIgnoreCase("E04")) {
                                                        resultHTML.append("<td align=\"right\" >");
                                                        Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                        datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
                                                        resultHTML.append(datott);
                                                        subtotal[k1] = subtotal[k1] - (long) datott;
                                                        resultHTML.append("</td>");
                                                    } else {
                                                        resultHTML.append("<td align=\"left\" >");
                                                        resultHTML.append("</td>");
                                                    }
                                                    // </editor-fold>
                                                }
                                                resultHTML.append("<td align=\"left\" >");
                                                resultHTML.append("</td>");
                                                resultHTML.append("</tr>");
                                                // </editor-fold>
                                            }
                                        }
                                        // </editor-fold>
                                    }
                                    // </editor-fold>
                                }

                                if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                    //System.out.println("--------------------------------------------------");
                                    StringBuffer query = new StringBuffer();
                                    query.append("select sppd.id as sppod, spb.type as spbtype, sppd.type as sppdtype, spb.id as spbid from supplementarypayrollprocessingdetails sppd ");
                                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                                    query.append(" where sppd.accregion='" + LoggedInRegion + "' and spb.type='SLINCREMENTMANUAL'  and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "'  and spb.cancelled is false and sppd.cancelled is false");
                                    SQLQuery subpayquery = session.createSQLQuery(query.toString());
                                    //System.out.println(query.toString());
                                    //System.out.println("size" + subpayquery.list().size());
                                    for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                                        // <editor-fold defaultstate="collapsed">
                                        Object[] rows = (Object[]) its.next();
                                        String payprocesid = (String) rows[0];
                                        String type = (String) rows[1];
                                        String type2 = (String) rows[2];
                                        String supid = (String) rows[3];

                                        System.out.println(payprocesid + type);
                                        System.out.println(supid);
                                        resultHTML.append("<tr>");
                                        resultHTML.append("<td align=\"left\" >");
                                        resultHTML.append("Drawn" + type);
                                        resultHTML.append("</td>");

                                        for (int k = 0; k < earcodes.length; k++) {
                                            // <editor-fold defaultstate="collapsed">
                                            System.out.println(earcodes[k]);
                                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List empEarnDetailsList = empEarnDetailsCrit.list();
                                            if (empEarnDetailsList.size() > 0) {
                                                // <editor-fold defaultstate="collapsed">
                                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                                resultHTML.append("<td align=\"right\" >");
                                                if (!type.equalsIgnoreCase("DAARREAR")) {
                                                    resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
                                                    subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                                } else {
                                                    // <editor-fold defaultstate="collapsed">
                                                    if (subvec.trim().length() > 0) {
                                                        subvec = subvec + "," + "'" + supid + "'";
                                                    } else {
                                                        subvec = subvec + "'" + supid + "'";
                                                    }
                                                    float datott = 0;
                                                    Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                                    empIncCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supid + "'"));
                                                    empIncCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                                                    empIncCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                                                    List incArList = empIncCrit.list();
                                                    if (incArList.size() > 0) {
                                                        for (int sr = 0; sr < incArList.size(); sr++) {
                                                            // <editor-fold defaultstate="collapsed">
                                                            Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                            if (!incrementarrearreferenceObj.getType().equalsIgnoreCase("LEAVESURRENDER")) {
                                                                datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
                                                            } else {
                                                            }
                                                            // </editor-fold>
                                                        }
                                                    }
                                                    resultHTML.append(datott);
                                                    subtotal[k] = subtotal[k] - (long) datott;
                                                    // </editor-fold>
                                                }
                                                System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                                resultHTML.append("</td>");
                                                // </editor-fold>
                                            } else {
                                                resultHTML.append("<td align=\"right\" > ");
                                                resultHTML.append("0.00");
                                                resultHTML.append("</td>");
                                            }
                                            // </editor-fold>

                                        }
                                        resultHTML.append("<td align=\"left\" >");
                                        if (type.equalsIgnoreCase("SLINCREMENTMANUAL")) {
                                            resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showManIncDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + payprocesid + "')\">");
                                        }
                                        resultHTML.append("</td>");
                                        resultHTML.append("</tr>");
                                        // </editor-fold>
                                    }

                                }
                                // </editor-fold>
                            }
                            // </editor-fold>
                        } //SSSSSSSSSSSSSS

                        totalearpaid = 0;
                        for (int p = 0; p < subtotal.length; p++) {
                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[p] + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empEarnDetailsList = empEarnDetailsCrit.list();
                            if (empEarnDetailsList.size() > 0) {
                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
                                if (isAccountedForEpF(session, earcodes[p])) {
                                    totalearpaid = totalearpaid + subtotal[p];
                                }
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.update(employeeearningstransactionsObj);
                                transaction.commit();
                            } else {
                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();

                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
                                if (isAccountedForEpF(session, earcodes[p])) {
                                    totalearpaid = totalearpaid + subtotal[p];
                                }
                                employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                                employeeearningstransactionsObj.setEarningmasterid(earcodes[p]);
                                String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                                employeeearningstransactionsObj.setId(earningsTransactionId);
                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);

                                transaction = session.beginTransaction();
                                session.save(employeeearningstransactionsObj);
                                transaction.commit();
                            }

                        }

                        if (isCalculateEpf(session, epfno)) {
                            //System.out.println("supplementary payroll processing details epf calculation " + supplementarypayrollprocessingdetailsObj.getId());
                            //System.out.println("total" + totalearpaid);
                            Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empDeductDetailsList = empdeductDetailsCrit.list();
                            if (empDeductDetailsList.size() > 0) {
                                Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
//                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, subtotal[p]));
                                if (supplementarypayrollprocessingdetailsObj.getType() != null) {
                                    if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                        employeedeductionstransactionsObj.setAmount(new BigDecimal("0.00"));
                                    } else {
                                        employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                                    }
                                } else {
                                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                                }
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.update(employeedeductionstransactionsObj);
                                transaction.commit();
                            } else {
                                String deductionTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                                Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid("D02");
                                employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
//                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, subtotal[p]));
                                if (supplementarypayrollprocessingdetailsObj.getType() != null) {
                                    if (supplementarypayrollprocessingdetailsObj.getType().equalsIgnoreCase("INCLEAVESURRENDAR")) {
                                        employeedeductionstransactionsObj.setAmount(new BigDecimal("0.00"));
                                    } else {
                                        employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                                    }
                                } else {
                                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                                }

                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                                transaction.commit();
                            }
                        } else {
                            Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                            empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List empDeductDetailsList = empdeductDetailsCrit.list();
                            if (empDeductDetailsList.size() > 0) {
                                Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
                                employeedeductionstransactionsObj.setAmount(new BigDecimal(0.00));
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.TRUE);
                                transaction = session.beginTransaction();
                                session.update(employeedeductionstransactionsObj);
                                transaction.commit();
                            }
                        }


//                        //SSSSSSSSSSSSSSSS
//                        resultHTML.append("<tr>");
//                        resultHTML.append("<td align=\"left\" >");
//                        resultHTML.append("Balance");
//                        resultHTML.append("</td>");
//                        float subtot = 0;
//                        for (int p = 0; p < subtotal.length; p++) {
//                            // <editor-fold defaultstate="collapsed">
//                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
//                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
//                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
//                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
//                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[p] + "'"));
//                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//                            List empEarnDetailsList = empEarnDetailsCrit.list();
//                            if (empEarnDetailsList.size() > 0) {
//                                // <editor-fold defaultstate="collapsed">
//                                employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
//                                employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
//                                transaction = session.beginTransaction();
//                                session.update(employeeearningstransactionsObj);
//                                transaction.commit();
//                                // </editor-fold>
//                            } else {
//                                // <editor-fold defaultstate="collapsed">
//                                employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();
//
//                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
//                                employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
//                                employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
//                                employeeearningstransactionsObj.setEarningmasterid(earcodes[p]);
//                                String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
//                                employeeearningstransactionsObj.setId(earningsTransactionId);
//                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);
//
//                                transaction = session.beginTransaction();
//                                session.save(employeeearningstransactionsObj);
//                                transaction.commit();
//                                // </editor-fold>
//                            }
//                            resultHTML.append("<td align=\"right\" >");
//                            subtot = subtot + employeeearningstransactionsObj.getAmount().floatValue();
//                            resultHTML.append(employeeearningstransactionsObj.getAmount());
//                            resultHTML.append("</td>");
//                            // </editor-fold>
//                        }
//                        resultHTML.append("<td align=\"right\" >");
//                        resultHTML.append(subtot);
//                        resultHTML.append("</td>");
//                        resultHTML.append("</tr>");
//                        // </editor-fold>
                    }
                }
                resultHTML.append("</table>");
                resultMap.put("incrementhtml", resultHTML.toString());
                // </editor-fold>
            } else {
                StringBuffer resultHTML = new StringBuffer();
                resultHTML.append("Increment Arear not Made");
                resultMap.put("incrementhtml", resultHTML.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        resultMap.put("message", "Saved Successfully");
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveIncrementArrearDetails_OLD(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate) {

        Map resultMap = new HashMap();
        Transaction transaction = null;
        long totalearpaid = 0;
        Supplementatypaybill supplementatypaybillObj = null;
        String[] earcodes = {"E01", "E03", "E04", "E06", "E07", "E25"};
        long[] subtotal = {0, 0, 0, 0, 0, 0};
        try {
            Criteria supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
            supPayBillCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            supPayBillCrit.add(Restrictions.sqlRestriction("type='INCREMENTARREAR'"));
            supPayBillCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List<Supplementatypaybill> supPayBillList = supPayBillCrit.list();
            if (supPayBillList.size() > 0) {
                supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(0);
            }
            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj;
            Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);

            supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
            supPayrollProcesCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
            List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();

            if (supPayProcList.size() > 0) {
                for (int i = 0; i < supPayProcList.size(); i++) {
                    for (int p = 0; p < subtotal.length; p++) {
                        subtotal[p] = 0;
                    }
                    supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(i);

                    Supplementarysalarystructure supplementarysalarystructureObj;
                    Criteria supSalStruCrit = session.createCriteria(Supplementarysalarystructure.class);
                    supSalStruCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    supSalStruCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));

                    List<Supplementarysalarystructure> supSalStruList = supSalStruCrit.list();
                    if (supSalStruList.size() > 0) {
                        supplementarysalarystructureObj = (Supplementarysalarystructure) supSalStruList.get(0);

                        for (int k = 0; k < earcodes.length; k++) {
                            Supplementaryemployeeearningsdetails earningsdetObj;
                            Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                            earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + supplementarysalarystructureObj.getId() + "' "));
                            earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earcodes[k] + "' "));
                            List<Supplementaryemployeeearningsdetails> earList = earDe.list();
                            if (earList.size() > 0) {
                                earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                                subtotal[k] = subtotal[k] + (long) earningsdetObj.getAmount().floatValue();
                            }
                        }
//Regular

                        Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
                        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
                        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                        List empPayProcessDetails = empPayProcessDetailsCrit.list();
                        if (empPayProcessDetails.size() > 0) {
                            Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(0);
                            //Incrementarrearreference
                            Criteria empIncRefCrit = session.createCriteria(Incrementarrearreference.class);
                            empIncRefCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empIncRefCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supplementatypaybillObj.getId() + "'"));
                            empIncRefCrit.add(Restrictions.sqlRestriction("type='" + "REGULAR" + "'"));
                            empIncRefCrit.add(Restrictions.sqlRestriction("processid='" + payrollprocessingdetailsObj.getId() + "'"));
                            List empIncRefList = empIncRefCrit.list();
                            if (empIncRefList.size() > 0) {
                                Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) empIncRefList.get(0);
                                incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                incrementarrearreferenceObj.setProcessid(payrollprocessingdetailsObj.getId());
                                incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                incrementarrearreferenceObj.setType("REGULAR");
                                incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                session.update(incrementarrearreferenceObj);
                                transaction.commit();

                            } else {
                                Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                                incrementarrearreferenceObj.setId(getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                                incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                incrementarrearreferenceObj.setProcessid(payrollprocessingdetailsObj.getId());
                                incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                incrementarrearreferenceObj.setType("REGULAR");
                                incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                session.save(incrementarrearreferenceObj);
                                transaction.commit();
                            }
                            //Incrementarrearreference

                            for (int k = 0; k < earcodes.length; k++) {


                                Criteria empEarnDetailsCrit = session.createCriteria(Employeeearningstransactions.class);
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                List empEarnDetailsList = empEarnDetailsCrit.list();
                                if (empEarnDetailsList.size() > 0) {

                                    for (int j = 0; j < empEarnDetailsList.size(); j++) {
                                        Employeeearningstransactions employeeearningstransactionsObj = (Employeeearningstransactions) empEarnDetailsList.get(j);
                                        subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
//                                        System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                    }
                                }
                            }
                        }
                        //Regular end
                        for (int k = 0; k < earcodes.length; k++) {
                            System.out.println(earcodes[k] + " " + subtotal[k]);
                        }

                        //Supplementaty
                        StringBuffer query = new StringBuffer();
                        query.append("select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
                        query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                        query.append(" where spb.accregion='" + LoggedInRegion + "' and sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and spb.cancelled is false and sppd.cancelled is false");
//                    query.append("select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
//                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
//                    query.append(" where sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "'");
                        SQLQuery subpayquery = session.createSQLQuery(query.toString());
//                        System.out.println("size" + subpayquery.list().size());
                        for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                            Object[] rows = (Object[]) its.next();
                            String payprocesid = (String) rows[0];
                            String type = (String) rows[1];
                            //Incrementarrearreference
                            Criteria empIncRefCrit = session.createCriteria(Incrementarrearreference.class);
                            empIncRefCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empIncRefCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supplementatypaybillObj.getId() + "'"));
                            empIncRefCrit.add(Restrictions.sqlRestriction("type='" + "SUPPLEMENTARY" + "'"));
                            empIncRefCrit.add(Restrictions.sqlRestriction("processid='" + payprocesid + "'"));
                            List empIncRefList = empIncRefCrit.list();
                            if (empIncRefList.size() > 0) {
                                Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) empIncRefList.get(0);
                                incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                incrementarrearreferenceObj.setProcessid(payprocesid);
                                incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                incrementarrearreferenceObj.setType("SUPPLEMENTARY");
                                incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                                transaction = session.beginTransaction();
                                session.update(incrementarrearreferenceObj);
                                transaction.commit();
                            } else {
                                Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                                incrementarrearreferenceObj.setId(getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                                incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                                incrementarrearreferenceObj.setProcessid(payprocesid);
                                incrementarrearreferenceObj.setType("SUPPLEMENTARY");
                                incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                                incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                                incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                                incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                                incrementarrearreferenceObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(incrementarrearreferenceObj);
                                transaction.commit();
                            }
                            //Incrementarrearreference


                            for (int k = 0; k < earcodes.length; k++) {
                                System.out.println("supplementary processing id" + payprocesid + "" + earcodes[k]);
                                Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                List empEarnDetailsList = empEarnDetailsCrit.list();
                                if (empEarnDetailsList.size() > 0) {
                                    Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);

                                    subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
//                                    System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                    System.out.println("amount" + employeeearningstransactionsObj.getAmount().floatValue());
                                }


                            }

                        }
                        //Supplementaty end
                        for (int k = 0; k < earcodes.length; k++) {
                            System.out.println(earcodes[k] + " " + subtotal[k]);
                        }
                    }
                    totalearpaid = 0;
                    for (int p = 0; p < subtotal.length; p++) {
                        Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[p] + "'"));
                        empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empEarnDetailsList = empEarnDetailsCrit.list();
                        if (empEarnDetailsList.size() > 0) {
                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                            employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
                            if (isAccountedForEpF(session, earcodes[p])) {
                                totalearpaid = totalearpaid + subtotal[p];
                            }
                            employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                            employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(employeeearningstransactionsObj);
                            transaction.commit();
                        } else {
                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();

                            employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                            employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
                            if (isAccountedForEpF(session, earcodes[p])) {
                                totalearpaid = totalearpaid + subtotal[p];
                            }
                            employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                            employeeearningstransactionsObj.setEarningmasterid(earcodes[p]);
                            String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                            employeeearningstransactionsObj.setId(earningsTransactionId);
                            employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                            employeeearningstransactionsObj.setCreatedby(LoggedInUser);

                            transaction = session.beginTransaction();
                            session.save(employeeearningstransactionsObj);
                            transaction.commit();
                        }

                    }

                    if (isCalculateEpf(session, epfno)) {
                        System.out.println("supplementary payroll processing details epf calculation " + supplementarypayrollprocessingdetailsObj.getId());
                        System.out.println("total" + totalearpaid);
                        Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empDeductDetailsList = empdeductDetailsCrit.list();
                        if (empDeductDetailsList.size() > 0) {
                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
//                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, subtotal[p]));
                            employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(employeedeductionstransactionsObj);
                            transaction.commit();
                        } else {
                            String deductionTransactionId = getSupplementaryemployeedeductionstransactionsid(session, LoggedInRegion);
                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = new Supplementaryemployeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid("D02");
                            employeedeductionstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
//                    employeedeductionstransactionsObj.setAmount(getEPFAmount(session, subtotal[p]));
                            employeedeductionstransactionsObj.setAmount(getEPFAmount(session, totalearpaid, asondate));
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(employeedeductionstransactionsObj);
                            transaction.commit();
                        }
                    } else {
                        Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                        empdeductDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empDeductDetailsList = empdeductDetailsCrit.list();
                        if (empDeductDetailsList.size() > 0) {
                            Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
                            employeedeductionstransactionsObj.setAmount(new BigDecimal(0.00));
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setCancelled(Boolean.TRUE);
                            transaction = session.beginTransaction();
                            session.update(employeedeductionstransactionsObj);
                            transaction.commit();
                        }
                    }



                }
            }

            resultMap.put("message", "Saved Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
//        PrintIncrementArrearDetails(session, request, response, LoggedInRegion, LoggedInUser, epfno, asondate);

        return resultMap;
    }

    public boolean isAccountedForEpF(Session session, String earningMasterId) {
        Criteria ccaHRA = session.createCriteria(Ccahra.class);
        ccaHRA.add(Restrictions.sqlRestriction("ccahra='D02'"));
        ccaHRA.add(Restrictions.sqlRestriction("paycode='" + earningMasterId + "'"));
        List ccaHRAList = ccaHRA.list();

        if (ccaHRAList.size()
                > 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized String getMaxIncrementarrearreferenceid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;


        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();

            if (ldList.size()
                    > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getIncrementarrearreferenceid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setIncrementarrearreferenceid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map PrintIncrementArrearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String filePathwithName) {
        System.out.println("*********************** SupplementaryBillServiceImpl class PrintIncrementArrearDetails Method is calling ***********************");
        Map resultMap = new HashMap();
        Transaction transaction;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        IncrementArrearModel incrementArrearModel = null;
        SupplementaryIncrementArrear sia = new SupplementaryIncrementArrear();
        String[] earcodes = {"E01", "E02", "E04", "E06", "E07", "E25"};
        String subvec = "";
        int slipno = 1;
        try {
            StringBuilder strbuild = new StringBuilder();
            strbuild.append("select spb.id, spb.employeeprovidentfundnumber, em.employeename, dm.designation, rm.regionname, sm.sectionname ");
            strbuild.append("from supplementatypaybill spb ");
            strbuild.append("left join employeemaster em on spb.employeeprovidentfundnumber=em.epfno ");
            strbuild.append("left join designationmaster dm on dm.designationcode=spb.designation ");
            strbuild.append("left join regionmaster rm on rm.id=spb.accregion ");
            strbuild.append("left join sectionmaster sm on sm.id=spb.section ");
            strbuild.append("where ");
            strbuild.append("spb.type='INCREMENTARREAR' ");
            strbuild.append("and spb.date='" + postgresDate(asondate) + "' ");
            strbuild.append("and spb.cancelled is false ");
            strbuild.append("and spb.accregion='" + LoggedInRegion + "'");
            if (epfno.length() > 0) {
                strbuild.append("and spb.employeeprovidentfundnumber='" + epfno + "'");
            }
//            strbuild.append("and spb.id='R011699'");

            SQLQuery EmployeeQuery = session.createSQLQuery(strbuild.toString());

            System.out.println("sb.toString()" + strbuild.toString());
            if (EmployeeQuery.list().size() == 0) {
                resultMap.put("ERROR", "There is no Record for the given Inputs");
                return resultMap;
            }
            for (ListIterator lit = EmployeeQuery.list().listIterator(); lit.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Supplementarypaybill Start">
                Object[] ob = (Object[]) lit.next();
                String supplementarypaybillid = (String) ob[0];
                String pfno = (String) ob[1];
                String employeename = (String) ob[2];
                String designation = (String) ob[3];
                String region = (String) ob[4];
                String section = (String) ob[5];

                incrementArrearModel = new IncrementArrearModel();

                incrementArrearModel.setEpfno(pfno);
                incrementArrearModel.setName(employeename);
                incrementArrearModel.setDesignation(designation);
                incrementArrearModel.setSection(section);
                incrementArrearModel.setRegion(region);

                // <editor-fold defaultstate="collapsed" desc="SupplementaryPayrollProcess">
                StringBuilder sb = new StringBuilder();

                sb.append("select id, calculatedyear, calculatedmonth, type, typeid from supplementarypayrollprocessingdetails ");
                sb.append("where supplementatypaybillid = '" + supplementarypaybillid + "' and cancelled is false  order by calculatedyear,calculatedmonth,id");

                SQLQuery employeeQuery = session.createSQLQuery(sb.toString());
                System.out.println("sb.toString()" + sb.toString());
                if (employeeQuery.list().size() == 0) {
                    resultMap.put("ERROR", "There is no Record for the given Inputs");
                    return resultMap;
                }
                for (ListIterator its = employeeQuery.list().listIterator(); its.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Employeequery">
                    Object[] rows = (Object[]) its.next();
                    String supplementarypayrollprocessid = (String) rows[0];
                    int year = 0;
                    int month = 0;
                    if (rows[1] != null) {
                        year = Integer.valueOf(rows[1].toString());
                    }
                    if (rows[2] != null) {
                        month = Integer.valueOf(rows[2].toString());
                    }
                    String supptype = (String) rows[3];
                    String supplementarypayrollprocesstypeid = (String) rows[4];
//                    String surrenderdate = "";
//                    if (rows[11] != null) {
//                        Date sdate = (Date) rows[11];
//                        surrenderdate = dateToString(sdate);
//                        surrenderdate = months[(Integer.valueOf(surrenderdate.substring(3, 5))) - 1] + "\"" + surrenderdate.substring(6, 10);
//                    }

                    StringBuilder ded = new StringBuilder();
                    ded.append("select sedt.deductionmasterid, sedt.amount from supplementaryemployeedeductionstransactions sedt ");
                    ded.append("left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid ");
                    ded.append("where supplementatypaybillid='" + supplementarypaybillid + "' ");
                    ded.append("and sedt.cancelled is false ");
                    ded.append("and sppd.cancelled is false ");
                    ded.append("and sedt.deductionmasterid='D02' ");
                    ded.append("and sppd.calculatedyear=" + year + " ");
                    ded.append("and sppd.calculatedmonth=" + month + "");

                    System.out.println("year = " + year + " month = " + month);

                    SQLQuery dedquery = session.createSQLQuery(ded.toString());

                    double deductionamount = 0;

                    if (dedquery.list().size() > 0) {
                        Object obj[] = (Object[]) dedquery.list().get(0);
                        String deductionid = (String) obj[0];
                        deductionamount = ((BigDecimal) obj[1]).doubleValue();
                    }

                    List<IncrementArrearsubModel> rlist = new ArrayList<IncrementArrearsubModel>();

                    // <editor-fold defaultstate="collapsed" desc="DUE PART">

                    sb = new StringBuilder();
                    sb.append("select seed.earningmasterid, seed.amount from supplementaryemployeeearningsdetails seed ");
                    sb.append("left join supplementarysalarystructure sss on sss.id=seed.supplementarysalarystructureid ");
                    sb.append("where seed.cancelled is false and sss.supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessid + "' ");
                    sb.append("and sss.cancelled is false");

                    SQLQuery dueQuery = session.createSQLQuery(sb.toString());

                    Map<String, String> detmap = new HashMap<String, String>();
                    for (ListIterator it1 = dueQuery.list().listIterator(); it1.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Duequery">
                        Object[] row1 = (Object[]) it1.next();
                        String earningid = (String) row1[0];
                        double dueamount = ((BigDecimal) row1[1]).doubleValue();
                        detmap.put(earningid, decimalFormat.format(dueamount));
                        // </editor-fold>
                    }

                    String period = "";
                    String basic = "";
                    String pp = "";
                    String grpay = "";
                    String splpay = "";
                    String da = "";
                    String hra = "";
                    String cca = "";
                    String epf = "";

                    basic = (detmap.get("E01") != null) ? (String) detmap.get("E01") : "0.00";
                    splpay = (detmap.get("E02") != null) ? (String) detmap.get("E02") : "0.00";
                    pp = (detmap.get("E03") != null) ? (String) detmap.get("E03") : "0.00";
                    da = (detmap.get("E04") != null) ? (String) detmap.get("E04") : "0.00";
                    hra = (detmap.get("E06") != null) ? (String) detmap.get("E06") : "0.00";
                    cca = (detmap.get("E07") != null) ? (String) detmap.get("E07") : "0.00";
                    grpay = (detmap.get("E25") != null) ? (String) detmap.get("E25") : "0.00";
                    epf = (detmap.get("D02") != null) ? (String) detmap.get("D02") : "0.00";


                    IncrementArrearsubModel iam = new IncrementArrearsubModel();
                    if (month == 0 && year == 0) {
                        iam.setPeriod("");
                    } else {
                        iam.setPeriod(months[month - 1] + "\"" + (String.valueOf(year)).substring(2, 4));
                    }
                    iam.setBasic(basic);
                    iam.setSplpay(splpay);
                    iam.setPp(pp);
                    iam.setDa(da);
                    iam.setHra(hra);
                    iam.setCca(cca);
                    iam.setGrpay(grpay);
                    iam.setType("DUE");
                    iam.setEpf(epf);

                    rlist.add(iam);

                    // </editor-fold>

                    if (supptype == null || !(supptype.equalsIgnoreCase("INCLEAVESURRENDAR"))) {
                        // <editor-fold defaultstate="collapsed" desc="Without Leave Surrendor Part">

                        // <editor-fold defaultstate="collapsed" desc="DRAWN REGULAR">
                        sb = new StringBuilder();
                        sb.append("select eet.earningmasterid, eet.amount from employeeearningstransactions eet ");
                        sb.append("left join payrollprocessingdetails pp on pp.id=eet.payrollprocessingdetailsid ");
                        sb.append("where eet.cancelled is false and pp.process is true and pp.month=" + month + " ");
                        sb.append("and pp.year=" + year + " and pp.employeeprovidentfundnumber='" + pfno + "' ");
                        sb.append("and pp.accregion='" + LoggedInRegion + "'");

                        SQLQuery regularearningsquery = session.createSQLQuery(sb.toString());

                        detmap = new HashMap<String, String>();

                        for (ListIterator it1 = regularearningsquery.list().listIterator(); it1.hasNext();) {
                            // <editor-fold defaultstate="collapsed" desc="Regular Earnings Query">
                            Object[] row1 = (Object[]) it1.next();
                            if (row1[0] != null) {
                                String earningid = (String) row1[0];
                                double dueamount = ((BigDecimal) row1[1]).doubleValue();
                                detmap.put(earningid, decimalFormat.format(dueamount));
                            }
                            // </editor-fold>
                        }

                        period = "";
                        basic = "";
                        pp = "";
                        grpay = "";
                        splpay = "";
                        da = "";
                        hra = "";
                        cca = "";

                        basic = (detmap.get("E01") != null) ? (String) detmap.get("E01") : "0.00";
                        splpay = (detmap.get("E02") != null) ? (String) detmap.get("E02") : "0.00";
                        pp = (detmap.get("E03") != null) ? (String) detmap.get("E03") : "0.00";
                        da = (detmap.get("E04") != null) ? (String) detmap.get("E04") : "0.00";
                        hra = (detmap.get("E06") != null) ? (String) detmap.get("E06") : "0.00";
                        cca = (detmap.get("E07") != null) ? (String) detmap.get("E07") : "0.00";
                        grpay = (detmap.get("E25") != null) ? (String) detmap.get("E25") : "0.00";

                        iam = new IncrementArrearsubModel();
                        iam.setPeriod(months[month - 1] + "\"" + (String.valueOf(year)).substring(2, 4));
                        iam.setBasic(basic);
                        iam.setSplpay(splpay);
                        iam.setPp(pp);
                        iam.setDa(da);
                        iam.setHra(hra);
                        iam.setCca(cca);
                        iam.setGrpay(grpay);
                        iam.setType("DRAWN REGULAR");

                        rlist.add(iam);

                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="DRAWN SUPPLEMENTARY">
                        sb = new StringBuilder();
                        sb.append("select sppd.id as sppod, spb.type as spbtype, sppd.type as sppdtype, spb.id as spbid ");
                        sb.append("from supplementarypayrollprocessingdetails sppd ");
                        sb.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                        sb.append("where ");
                        sb.append("sppd.accregion='" + LoggedInRegion + "' ");
                        sb.append("and sppd.calculatedyear=" + year + "  ");
                        sb.append("and sppd.calculatedmonth=" + month + " ");
                        sb.append("and spb.employeeprovidentfundnumber= '" + pfno + "' ");
                        sb.append("and spb.id!='" + supplementarypaybillid + "'  ");
                        sb.append("and spb.cancelled is false ");
                        sb.append("and sppd.cancelled is false");

//                        System.out.println("Leave arrear paid subpaybill id = "+sb.toString());

                        SQLQuery supplementaryquery = session.createSQLQuery(sb.toString());

                        for (ListIterator it1 = supplementaryquery.list().listIterator(); it1.hasNext();) {
                            // <editor-fold defaultstate="collapsed" desc="Supplementary Query">
                            Object[] row1 = (Object[]) it1.next();
                            String spayrollprocessid = (String) row1[0];
                            String stype = (String) row1[1];
                            String supid = (String) row1[3];

                            detmap = new HashMap<String, String>();
                            detmap.put("E01", "0.00");
                            detmap.put("E02", "0.00");
                            detmap.put("E03", "0.00");
                            detmap.put("E04", "0.00");
                            detmap.put("E06", "0.00");
                            detmap.put("E07", "0.00");
                            detmap.put("E25", "0.00");

                            for (int k = 0; k < earcodes.length; k++) {
                                // <editor-fold defaultstate="collapsed">
                                Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + spayrollprocessid + "'"));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                List empEarnDetailsList = empEarnDetailsCrit.list();
                                if (empEarnDetailsList.size() > 0) {
                                    // <editor-fold defaultstate="collapsed">
                                    Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                    if (!stype.equalsIgnoreCase("DAARREAR")) {
                                        BigDecimal bigamount = employeeearningstransactionsObj.getAmount();
                                        double damo = bigamount.doubleValue();
                                        detmap.put(earcodes[k], decimalFormat.format(damo));
//                                    System.out.println(earcodes[k] + " - " + employeeearningstransactionsObj.getAmount().setScale(2));
//                                    subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                    } else {
                                        // <editor-fold defaultstate="collapsed">
                                        if (subvec.trim().length() > 0) {
                                            subvec = subvec + "," + "'" + supid + "'";
                                        } else {
                                            subvec = subvec + "'" + supid + "'";
                                        }
//                                        System.out.println("subvec = "+subvec);
                                        double datott = 0;
                                        Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                        empIncCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supid + "'"));
                                        empIncCrit.add(Restrictions.sqlRestriction("month=" + month));
                                        empIncCrit.add(Restrictions.sqlRestriction("year=" + year));
                                        List incArList = empIncCrit.list();
                                        if (incArList.size() > 0) {
                                            for (int sr = 0; sr < incArList.size(); sr++) {
                                                // <editor-fold defaultstate="collapsed">
                                                Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                if (!incrementarrearreferenceObj.getType().equalsIgnoreCase("LEAVESURRENDER")) {
                                                    datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
//                                                System.out.println(earcodes[k] + " -> " + datott);
                                                } else {
                                                }
                                                // </editor-fold>
                                            }
                                        }

                                        detmap.put(earcodes[k], decimalFormat.format(datott));
                                        // </editor-fold>
                                    }
                                    // </editor-fold>
                                } else {
                                }
                                // </editor-fold>
                            }

                            period = "";
                            basic = "";
                            pp = "";
                            grpay = "";
                            splpay = "";
                            da = "";
                            hra = "";
                            cca = "";

                            basic = (detmap.get("E01") != null) ? (String) detmap.get("E01") : "0.00";
                            splpay = (detmap.get("E02") != null) ? (String) detmap.get("E02") : "0.00";
                            pp = (detmap.get("E03") != null) ? (String) detmap.get("E03") : "0.00";
                            da = (detmap.get("E04") != null) ? (String) detmap.get("E04") : "0.00";
                            hra = (detmap.get("E06") != null) ? (String) detmap.get("E06") : "0.00";
                            cca = (detmap.get("E07") != null) ? (String) detmap.get("E07") : "0.00";
                            grpay = (detmap.get("E25") != null) ? (String) detmap.get("E25") : "0.00";

                            iam = new IncrementArrearsubModel();
                            iam.setPeriod(months[month - 1] + "\"" + (String.valueOf(year)).substring(2, 4));
                            iam.setBasic(basic);
                            iam.setSplpay(splpay);
                            iam.setPp(pp);
                            iam.setDa(da);
                            iam.setHra(hra);
                            iam.setCca(cca);
                            iam.setGrpay(grpay);
                            iam.setType(SubString("DRAWN " + stype, 21));

                            rlist.add(iam);
                            // </editor-fold>
                        }
                        // </editor-fold>

                        // </editor-fold>
                    } else {
                        // <editor-fold defaultstate="collapsed" desc="Leave Surrendor Drawn Part">
                        if (supptype.equalsIgnoreCase("INCLEAVESURRENDAR")) {
                            // <editor-fold defaultstate="collapsed" desc="Leave Surrendor Part">
                            sb = new StringBuilder();
                            sb.append("select seet.earningmasterid,seet.amount from supplementaryemployeeearningstransactions seet ");
                            sb.append("left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid ");
                            sb.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                            sb.append("where spb.id='" + supplementarypayrollprocesstypeid + "' and sppd.accregion='" + LoggedInRegion + "' and sppd.cancelled is false and seet.cancelled is false");

                            SQLQuery slQuery = session.createSQLQuery(sb.toString());

                            detmap = new HashMap<String, String>();
                            for (ListIterator it1 = slQuery.list().listIterator(); it1.hasNext();) {
                                // <editor-fold defaultstate="collapsed" desc="Duequery">
                                Object[] row1 = (Object[]) it1.next();
                                String earningid = (String) row1[0];
                                double dueamount = ((BigDecimal) row1[1]).doubleValue();
                                detmap.put(earningid, decimalFormat.format(dueamount));
                                // </editor-fold>
                            }

                            period = "";
                            basic = "";
                            pp = "";
                            grpay = "";
                            splpay = "";
                            da = "";
                            hra = "";
                            cca = "";
                            epf = "";

                            basic = (detmap.get("E01") != null) ? (String) detmap.get("E01") : "0.00";
                            splpay = (detmap.get("E02") != null) ? (String) detmap.get("E02") : "0.00";
                            pp = (detmap.get("E03") != null) ? (String) detmap.get("E03") : "0.00";
                            da = (detmap.get("E04") != null) ? (String) detmap.get("E04") : "0.00";
                            hra = (detmap.get("E06") != null) ? (String) detmap.get("E06") : "0.00";
                            cca = (detmap.get("E07") != null) ? (String) detmap.get("E07") : "0.00";
                            grpay = (detmap.get("E25") != null) ? (String) detmap.get("E25") : "0.00";
                            epf = (detmap.get("D02") != null) ? (String) detmap.get("D02") : "0.00";

                            iam = new IncrementArrearsubModel();
//                            iam.setPeriod(surrenderdate);
                            iam.setBasic(basic);
                            iam.setSplpay(splpay);
                            iam.setPp(pp);
                            iam.setDa(da);
                            iam.setHra(hra);
                            iam.setCca(cca);
                            iam.setGrpay(grpay);
                            iam.setType("DRAWN SURRENDER LEAVE");
                            iam.setEpf(epf);
                            iam.setPeriod("");

                            rlist.add(iam);

                            // </editor-fold>

                            // <editor-fold defaultstate="collapsed" desc="Leave Surrendor ARREAR PAID">
//                            System.out.println("subvec.trim() = " + subvec.trim());
                            if (subvec.trim().length() > 0) {
                                // <editor-fold defaultstate="collapsed">
                                double datott = 0;

                                sb = new StringBuilder();
                                sb.append("select arrear from incrementarrearreference where suppaybillid in (" + subvec + ") and type = 'LEAVESURRENDER'");

                                SQLQuery arrearpaidQuery = session.createSQLQuery(sb.toString());

                                for (ListIterator it1 = arrearpaidQuery.list().listIterator(); it1.hasNext();) {
                                    // <editor-fold defaultstate="collapsed">
                                    period = "0.00";
                                    basic = "0.00";
                                    pp = "0.00";
                                    grpay = "0.00";
                                    splpay = "0.00";
                                    da = "0.00";
                                    hra = "0.00";
                                    cca = "0.00";
                                    epf = "0.00";

                                    Object row1 = (Object) it1.next();
                                    if (row1 != null) {
                                        BigDecimal bigamount = (BigDecimal) row1;
                                        datott = datott + bigamount.doubleValue();
                                        da = decimalFormat.format(datott);
                                    }

                                    iam = new IncrementArrearsubModel();
                                    iam.setBasic(basic);
                                    iam.setSplpay(splpay);
                                    iam.setPp(pp);
                                    iam.setDa(da);
                                    iam.setHra(hra);
                                    iam.setCca(cca);
                                    iam.setGrpay(grpay);
                                    iam.setType("ARREAR PAID");
                                    iam.setEpf(epf);
                                    iam.setPeriod("");

                                    rlist.add(iam);
                                    // </editor-fold>
                                }
                                // </editor-fold>
                            }
                            // </editor-fold>
                        }
                        if (supptype.equalsIgnoreCase("INCLEAVESURRENDAR")) {
                            sb = new StringBuilder();
                            sb.append("select sppd.id as sppod, spb.type as spbtype, sppd.type as sppdtype, spb.id as spbid ");
                            sb.append("from supplementarypayrollprocessingdetails sppd ");
                            sb.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                            sb.append("where ");
                            sb.append("sppd.accregion='" + LoggedInRegion + "' ");
                            sb.append("and spb.type='SLINCREMENTMANUAL' ");
//                        sb.append("and sppd.calculatedyear=" + year + "  ");
//                        sb.append("and sppd.calculatedmonth=" + month + " ");
                            sb.append("and spb.employeeprovidentfundnumber= '" + pfno + "' ");
                            sb.append("and spb.id!='" + supplementarypaybillid + "'  ");
                            sb.append("and spb.cancelled is false ");
                            sb.append("and sppd.cancelled is false");

//                        System.out.println("Leave arrear paid subpaybill id = "+sb.toString());

                            SQLQuery supplementaryquery = session.createSQLQuery(sb.toString());

                            for (ListIterator it1 = supplementaryquery.list().listIterator(); it1.hasNext();) {
                                // <editor-fold defaultstate="collapsed" desc="Supplementary Query">
                                Object[] row1 = (Object[]) it1.next();
                                String spayrollprocessid = (String) row1[0];
                                String stype = (String) row1[1];
                                String supid = (String) row1[3];

                                detmap = new HashMap<String, String>();
                                detmap.put("E01", "0.00");
                                detmap.put("E02", "0.00");
                                detmap.put("E03", "0.00");
                                detmap.put("E04", "0.00");
                                detmap.put("E06", "0.00");
                                detmap.put("E07", "0.00");
                                detmap.put("E25", "0.00");

                                for (int k = 0; k < earcodes.length; k++) {
                                    // <editor-fold defaultstate="collapsed">
                                    Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + spayrollprocessid + "'"));
                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                    List empEarnDetailsList = empEarnDetailsCrit.list();
                                    if (empEarnDetailsList.size() > 0) {
                                        // <editor-fold defaultstate="collapsed">
                                        Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                        if (!stype.equalsIgnoreCase("DAARREAR")) {
                                            BigDecimal bigamount = employeeearningstransactionsObj.getAmount();
                                            double damo = bigamount.doubleValue();
                                            detmap.put(earcodes[k], decimalFormat.format(damo));
//                                    System.out.println(earcodes[k] + " - " + employeeearningstransactionsObj.getAmount().setScale(2));
//                                    subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
                                        } else {
                                            // <editor-fold defaultstate="collapsed">
                                            if (subvec.trim().length() > 0) {
                                                subvec = subvec + "," + "'" + supid + "'";
                                            } else {
                                                subvec = subvec + "'" + supid + "'";
                                            }
//                                        System.out.println("subvec = "+subvec);
                                            double datott = 0;
                                            Criteria empIncCrit = session.createCriteria(Incrementarrearreference.class);
                                            empIncCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supid + "'"));
                                            empIncCrit.add(Restrictions.sqlRestriction("month=" + month));
                                            empIncCrit.add(Restrictions.sqlRestriction("year=" + year));
                                            List incArList = empIncCrit.list();
                                            if (incArList.size() > 0) {
                                                for (int sr = 0; sr < incArList.size(); sr++) {
                                                    // <editor-fold defaultstate="collapsed">
                                                    Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) incArList.get(sr);
                                                    if (!incrementarrearreferenceObj.getType().equalsIgnoreCase("LEAVESURRENDER")) {
                                                        datott = datott + incrementarrearreferenceObj.getArrear().floatValue();
//                                                System.out.println(earcodes[k] + " -> " + datott);
                                                    } else {
                                                    }
                                                    // </editor-fold>
                                                }
                                            }

                                            detmap.put(earcodes[k], decimalFormat.format(datott));
                                            // </editor-fold>
                                        }
                                        // </editor-fold>
                                    } else {
                                    }
                                    // </editor-fold>
                                }

                                period = "";
                                basic = "";
                                pp = "";
                                grpay = "";
                                splpay = "";
                                da = "";
                                hra = "";
                                cca = "";

                                basic = (detmap.get("E01") != null) ? (String) detmap.get("E01") : "0.00";
                                splpay = (detmap.get("E02") != null) ? (String) detmap.get("E02") : "0.00";
                                pp = (detmap.get("E03") != null) ? (String) detmap.get("E03") : "0.00";
                                da = (detmap.get("E04") != null) ? (String) detmap.get("E04") : "0.00";
                                hra = (detmap.get("E06") != null) ? (String) detmap.get("E06") : "0.00";
                                cca = (detmap.get("E07") != null) ? (String) detmap.get("E07") : "0.00";
                                grpay = (detmap.get("E25") != null) ? (String) detmap.get("E25") : "0.00";

                                iam = new IncrementArrearsubModel();
                                iam.setPeriod("");
//                            iam.setPeriod(months[month - 1] + "\"" + (String.valueOf(year)).substring(2, 4));
                                iam.setBasic(basic);
                                iam.setSplpay(splpay);
                                iam.setPp(pp);
                                iam.setDa(da);
                                iam.setHra(hra);
                                iam.setCca(cca);
                                iam.setGrpay(grpay);
                                iam.setType(SubString("DRAWN SL MANUAL", 21));

                                rlist.add(iam);
                                // </editor-fold>
                            }
                            // </editor-fold>
                        }
                    }

                    incrementArrearModel.setIncrementarrearlist(rlist);
                    incrementArrearModel.setSlipno(slipno);
                    incrementArrearModel.setEpfamount(decimalFormat.format(deductionamount));

                    sia.getIncrementArrearPrintWriter(incrementArrearModel, filePathwithName);

                    slipno++;

                    Iterator itr = rlist.iterator();
                    while (itr.hasNext()) {
                        IncrementArrearsubModel iam1 = (IncrementArrearsubModel) itr.next();
//                    System.out.println(iam1.getType() + " - " + iam1.getPeriod() + " - " + iam1.getBasic() + " - " + iam1.getPp() + " - " + iam1.getGrpay() + " - " + iam1.getSplpay() + " - " + iam1.getDa() + " - " + iam1.getHra() + " - " + iam1.getCca() + " - " + iam1.getEpf());
                    }

                    // </editor-fold>
                }
                // </editor-fold>


                // </editor-fold>
            }
            sia.total(incrementArrearModel, filePathwithName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        resultMap.put("message", "Saved Successfully");
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryDABillPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String pfno, String filePathwithName) {
        System.out.println("***************************** class SupplementaryBillServiceImpl method EmployeeSupplementaryDABillPrintOut is calling ******************************");
        Map map = new HashMap();
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SupplementaryDABillReport supplementaryDABillReport = new SupplementaryDABillReport();
        DaArrearModel dam = null;
        //System.out.println("pfno = " + pfno);
        //System.out.println("pfno.length() = " + pfno.length());
//        System.out.println("asondate = " + asondate);
        int slipno = 1;

        String EMPLOYEEQUERY = null;

        if (pfno.length() == 0) {
            EMPLOYEEQUERY = "select spb.id,spb.employeeprovidentfundnumber,sm.sectionname,rm.regionname,em.employeename,dm.designation from "
                    + "supplementatypaybill spb  "
                    + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
                    + "left join regionmaster rm on rm.id=spb.accregion "
                    + "left join designationmaster dm on dm.designationcode=spb.designation "
                    + "left join sectionmaster sm on sm.id=spb.section "
                    + "where spb.dabatch='" + dabatch + "' and spb.type='DAARREAR' and "
                    + "spb.cancelled is false and spb.accregion='" + LoggedInRegion + "' "
                    //                    + "and spb.employeeprovidentfundnumber  "
                    //                    + "not in (select epfno from stoppayrolldetails where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') "
                    //                    + "and accregion='" + LoggedInRegion + "') "
                    + "order by spb.section,spb.designation";

//            EMPLOYEEQUERY = "select spb.id,spb.employeeprovidentfundnumber,sm.sectionname,rm.regionname,em.employeename,dm.designation from "
//                    + "supplementatypaybill spb "
//                    + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
//                    + "left join regionmaster rm on rm.id=spb.accregion "
//                    + "left join designationmaster dm on dm.designationcode=spb.designation "
//                    + "left join sectionmaster sm on sm.id=spb.section "
//                    + "where spb.dabatch='" + dabatch + "' and spb.type='DAARREAR' and spb.cancelled is false and spb.accregion='" + LoggedInRegion + "' "
//                    + "order by spb.section,spb.designation";
        } else {
            EMPLOYEEQUERY = "select spb.id,spb.employeeprovidentfundnumber,sm.sectionname,rm.regionname,em.employeename,dm.designation from "
                    + "supplementatypaybill spb "
                    + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
                    + "left join regionmaster rm on rm.id=spb.accregion "
                    + "left join designationmaster dm on dm.designationcode=spb.designation "
                    + "left join sectionmaster sm on sm.id=spb.section "
                    + "where spb.dabatch='" + dabatch + "' and spb.type='DAARREAR' and spb.cancelled is false and spb.accregion='" + LoggedInRegion + "' and  spb.employeeprovidentfundnumber='" + pfno + "' "
                    + "order by spb.section,spb.designation";
        }

        SQLQuery Employee_Query = session.createSQLQuery(EMPLOYEEQUERY);
//        System.out.println("Employee_Query.list().size()" + Employee_Query.list().size());
//        System.out.println("EMPLOYEEQUERY = " + EMPLOYEEQUERY);
        if (Employee_Query.list().size() == 0) {
            map.put("ERROR", "There is no Record for the given Inputs");
            return map;
        }
        for (ListIterator its = Employee_Query.list().listIterator(); its.hasNext();) {
            // <editor-fold defaultstate="collapsed" desc="Voucher">
            dam = new DaArrearModel();
            Object[] rows = (Object[]) its.next();
            String suppleypaybillid = (String) rows[0];
            String epfno = (String) rows[1];
            String startmonth = "";
            String endmonth = "";
            dam.setEpfno(epfno);
            dam.setSection((String) rows[2]);
            dam.setRegion((String) rows[3]);
            dam.setEmployeename((String) rows[4]);
            dam.setDesignation((String) rows[5]);
            dam.setSlipno(slipno);
            dam.setBatchno(dabatch);

            double incr_basic = 0;
            double incr_perpay = 0;
            double incr_grpay = 0;
            double incr_due = 0;
            double incr_drawn = 0;
            double incr_arrear = 0;
            double incr_epf = 0;
            double incr_net = 0;

//            System.out.println(dam.getSlipno() + " ." + dam.getEpfno());

            String SUPPLYPAYROLLPROCESSQUERY = "SELECT id, supplementatypaybillid, calculatedyear, calculatedmonth FROM "
                    + "supplementarypayrollprocessingdetails where supplementatypaybillid = '" + suppleypaybillid + "' order by calculatedyear, calculatedmonth";

            String STARTDATEQUERY = "SELECT calculatedyear , MIN(calculatedmonth)  FROM supplementarypayrollprocessingdetails WHERE "
                    + "supplementatypaybillid = '" + suppleypaybillid + "' and calculatedyear = (SELECT MIN(calculatedyear) FROM supplementarypayrollprocessingdetails "
                    + "WHERE supplementatypaybillid = '" + suppleypaybillid + "') GROUP BY calculatedyear";

            String ENDDATEQUERY = "SELECT calculatedyear, MAX(calculatedmonth)  FROM supplementarypayrollprocessingdetails WHERE "
                    + "supplementatypaybillid = '" + suppleypaybillid + "' and calculatedyear = (SELECT MAX(calculatedyear) FROM supplementarypayrollprocessingdetails "
                    + "WHERE supplementatypaybillid = '" + suppleypaybillid + "') GROUP BY calculatedyear";

//            System.out.println("SUPPLYPAYROLLPROCESSQUERY = " + SUPPLYPAYROLLPROCESSQUERY);

            SQLQuery startdate_Query = session.createSQLQuery(STARTDATEQUERY);

            SQLQuery endate_Query = session.createSQLQuery(ENDDATEQUERY);

            Object startobj[] = (Object[]) startdate_Query.list().get(0);

            Object endobj[] = (Object[]) endate_Query.list().get(0);

            startmonth = months[Integer.valueOf(startobj[1].toString()) - 1] + "\"" + String.valueOf(startobj[0].toString()).substring(2, 4);

            endmonth = months[Integer.valueOf(endobj[1].toString()) - 1] + "\"" + String.valueOf(endobj[0].toString()).substring(2, 4);

            dam.setStartmonth(startmonth);
            dam.setEndmonth(endmonth);

            List<DaArrearSubModel> dalist = new ArrayList<DaArrearSubModel>();

            DaArrearSubModel dasm = null;

            SQLQuery SupplyPayrollprocess_Query = session.createSQLQuery(SUPPLYPAYROLLPROCESSQUERY);

//            System.out.println("SupplyPayrollprocess_Query.list().size() " + SupplyPayrollprocess_Query.list().size());

            for (ListIterator it1 = SupplyPayrollprocess_Query.list().listIterator(); it1.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] row1 = (Object[]) it1.next();
                String supplementarypayrollprocessid = (String) row1[0];
                int calculatedyear = Integer.valueOf(row1[2].toString());
                int calculatedmonth = Integer.valueOf(row1[3].toString());
                String calculatedperiod = months[calculatedmonth - 1] + "\"" + String.valueOf(calculatedyear).substring(2, 4);
                String type[] = {"REGULAR", "SUPPLEMENTARY", "INCREMENTARREAR", "LEAVESURRENDER", "SUPLEMENTARYBILL"};

                for (int i = 0; i < type.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    String QUERY = "SELECT id, suppaybillid, type, processid, due, arrear, epf, drawn FROM incrementarrearreference "
                            + "where suppaybillid = '" + suppleypaybillid + "' and month=" + calculatedmonth + " and year=" + calculatedyear + " "
                            + "and accregion='" + LoggedInRegion + "' and type='" + type[i] + "' and arrear!=0";

//                    System.out.println("QUERY = " + QUERY);

                    SQLQuery da_query = session.createSQLQuery(QUERY);
                    for (ListIterator it2 = da_query.list().listIterator(); it2.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Voucher">
                        double basic = 0;
                        double perpay = 0;
                        double grpay = 0;
                        double dueamount = 0;
                        double drawnamount = 0;
                        double arrearamount = 0;
                        double epfamount = 0;
                        double netamount = 0;
                        double da = 0;

                        Object[] row2 = (Object[]) it2.next();
                        String SupId = (String) row2[1];
                        String TypeName = (String) row2[2];
                        String ProcessId = (String) row2[3];
                        BigDecimal bigdueamount = (BigDecimal) row2[4];
                        BigDecimal bigarrearamount = (BigDecimal) row2[5];

                        if (row2[6] != null) {
                            BigDecimal bigepfamount = (BigDecimal) row2[6];
                            epfamount = bigepfamount.doubleValue();
                        }

                        BigDecimal bigdrawnamount = (BigDecimal) row2[7];
                        dueamount = bigdueamount.doubleValue();
                        arrearamount = bigarrearamount.doubleValue();
                        drawnamount = bigdrawnamount.doubleValue();

                        String reporttype = type[i];

                        Map<String, Double> drawnmap = new HashMap<String, Double>();

                        String EARNING_QUERY = null;

                        if (type[i].equals("REGULAR")) {
                            EARNING_QUERY = "select eet.earningmasterid,eet.amount,ppd.id,ppd.year,ppd.month from employeeearningstransactions eet "
                                    + "left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid "
                                    + "where eet.cancelled is false and eet.earningmasterid in ('E01', 'E03', 'E04', 'E25') and "
                                    + "ppd.id='" + ProcessId + "' and ppd.month=" + calculatedmonth + " and ppd.year=" + calculatedyear + " "
                                    + "and ppd.process is true";
                        } else if (type[i].equals("LEAVESURRENDER")) {
                            EARNING_QUERY = "select seet.earningmasterid,seet.amount,sppd.calculatedyear,sppd.calculatedmonth "
                                    + "from supplementaryemployeeearningstransactions  seet  "
                                    + "left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                                    + "where sppd.id = '" + ProcessId + "' and sppd.cancelled is false and seet.cancelled is false "
                                    + "and seet.earningmasterid in ('E01', 'E03', 'E04', 'E25')";
                        } else {
                            EARNING_QUERY = "select seet.earningmasterid,seet.amount,sppd.calculatedyear,sppd.calculatedmonth "
                                    + "from supplementaryemployeeearningstransactions  seet  "
                                    + "left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                                    + "where sppd.id = '" + ProcessId + "' and sppd.cancelled is false and seet.cancelled is false "
                                    + "and sppd.calculatedyear = " + calculatedyear + " and sppd.calculatedmonth = " + calculatedmonth + "  "
                                    + "and seet.earningmasterid in ('E01', 'E03', 'E04', 'E25')";
                        }

                        SQLQuery Earnings_Query = session.createSQLQuery(EARNING_QUERY);

//                        System.out.println("EARNING_QUERY = " + EARNING_QUERY);

                        for (ListIterator it = Earnings_Query.list().listIterator(); it.hasNext();) {
                            // <editor-fold defaultstate="collapsed" desc="Voucher">
                            Object[] row = (Object[]) it.next();
                            String paycode = (String) row[0];
                            double amount = Double.valueOf(row[1].toString());
                            drawnmap.put(paycode, amount);
                            // </editor-fold>
                        }

                        if (drawnmap.get("E01") != null) {
                            basic = drawnmap.get("E01");
                        }
                        if (drawnmap.get("E03") != null) {
                            perpay = drawnmap.get("E03");
                        }
                        if (drawnmap.get("E04") != null) {
                            da = drawnmap.get("E04");
                        }
                        if (drawnmap.get("E25") != null) {
                            grpay = drawnmap.get("E25");
                        }

                        if (TypeName.equals("INCREMENTARREAR")) {
                            incr_basic += basic;
                            incr_perpay += perpay;
                            incr_grpay += grpay;
                            incr_due += dueamount;
                            incr_drawn += drawnamount;
                            incr_arrear += arrearamount;
                            incr_epf += epfamount;
                            incr_net += (arrearamount - epfamount);
                        } else {
                            dasm = new DaArrearSubModel();
                            dasm.setProcessmonth(calculatedperiod);
                            dasm.setBasic(decimalFormat.format(basic));
                            dasm.setPerpay(decimalFormat.format(perpay));
                            dasm.setGrpay(decimalFormat.format(grpay));
                            dasm.setDue(decimalFormat.format(dueamount));
                            dasm.setDrawn(decimalFormat.format(drawnamount));
                            dasm.setArrear(decimalFormat.format(arrearamount));
                            dasm.setEpfamount(decimalFormat.format(epfamount));
                            dasm.setType(TypeName);
                            netamount = arrearamount - epfamount;
                            dasm.setNet(decimalFormat.format(netamount));
                            dalist.add(dasm);
                        }
//                        System.out.println(calculatedperiod + " - " + basic + " - " + perpay + " - " + grpay + " - " + dueamount + " - " + da + " - " + arrearamount);
                        // </editor-fold>
                    }
                    // </editor-fold>
                }
                // </editor-fold>
            }

            for (ListIterator it1 = SupplyPayrollprocess_Query.list().listIterator(); it1.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] row1 = (Object[]) it1.next();
                String supplementarypayrollprocessid = (String) row1[0];
                int calculatedyear = Integer.valueOf(row1[2].toString());
                int calculatedmonth = Integer.valueOf(row1[3].toString());
                String calculatedperiod = months[calculatedmonth - 1] + String.valueOf(calculatedyear).substring(2, 4) + "M";
                String type[] = {"REGULAR", "SUPPLEMENTARY", "INCREMENTARREAR", "LEAVESURRENDER", "SUPLEMENTARYBILL"};

                for (int i = 0; i < type.length; i++) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">

                    String QUERY = "select basic, perpay, gradepay, due, drawn, type, arrear, epf, month, year from daarreardiffentry "
                            + "where supplementarypayroll='" + suppleypaybillid + "' and month = " + calculatedmonth + " "
                            + "and year=" + calculatedyear + " and type='" + type[i] + "'";

//                    System.out.println("QUERY = " + QUERY);

                    try {
                        SQLQuery manual_query = session.createSQLQuery(QUERY);
                        for (ListIterator it2 = manual_query.list().listIterator(); it2.hasNext();) {
                            // <editor-fold defaultstate="collapsed" desc="Voucher">
                            double basic = 0;
                            double perpay = 0;
                            double grpay = 0;
                            double dueamount = 0;
                            double drawnamount = 0;
                            double arrearamount = 0;
                            double epfamount = 0;
                            double netamount = 0;
                            double da = 0;
                            String TypeName = "";

                            Object[] row2 = (Object[]) it2.next();

                            if (row2[0] != null) {
                                BigDecimal man_basic = (BigDecimal) row2[0];
                                basic = man_basic.doubleValue();
                            }
                            if (row2[1] != null) {
                                BigDecimal man_perpay = (BigDecimal) row2[1];
                                perpay = man_perpay.doubleValue();
                            }
                            if (row2[2] != null) {
                                BigDecimal man_grpay = (BigDecimal) row2[2];
                                grpay = man_grpay.doubleValue();
                            }
                            if (row2[3] != null) {
                                BigDecimal man_due = (BigDecimal) row2[3];
                                dueamount = man_due.doubleValue();
                            }
                            if (row2[4] != null) {
                                BigDecimal man_drawn = (BigDecimal) row2[4];
                                drawnamount = man_drawn.doubleValue();
                            }
                            if (row2[6] != null) {
                                BigDecimal man_arrear = (BigDecimal) row2[6];
                                arrearamount = man_arrear.doubleValue();
                            }
                            if (row2[7] != null) {
                                BigDecimal man_epf = (BigDecimal) row2[7];
                                epfamount = man_epf.doubleValue();
                            }

                            netamount = arrearamount - epfamount;

                            TypeName = (String) row2[5];

                            if (TypeName.equals("INCREMENTARREAR")) {
                                incr_basic += basic;
                                incr_perpay += perpay;
                                incr_grpay += grpay;
                                incr_due += dueamount;
                                incr_drawn += drawnamount;
                                incr_arrear += arrearamount;
                                incr_epf += epfamount;
                                incr_net += (arrearamount - epfamount);
                            } else {
                                dasm = new DaArrearSubModel();
                                dasm.setProcessmonth(calculatedperiod);
                                dasm.setBasic(decimalFormat.format(basic));
                                dasm.setPerpay(decimalFormat.format(perpay));
                                dasm.setGrpay(decimalFormat.format(grpay));
                                dasm.setDue(decimalFormat.format(dueamount));
                                dasm.setDrawn(decimalFormat.format(drawnamount));
                                dasm.setArrear(decimalFormat.format(arrearamount));
                                dasm.setEpfamount(decimalFormat.format(epfamount));
                                dasm.setType(TypeName);
                                netamount = arrearamount - epfamount;
                                dasm.setNet(decimalFormat.format(netamount));
                                dalist.add(dasm);
                            }
//                        System.out.println(calculatedperiod + " - " + basic + " - " + perpay + " - " + grpay + " - " + dueamount + " - " + da + " - " + arrearamount);
                            // </editor-fold>
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // </editor-fold>
                }
                // </editor-fold>
            }
            if (incr_net != 0) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                dasm = new DaArrearSubModel();
                dasm.setProcessmonth("INC-AR");
                dasm.setBasic(decimalFormat.format(incr_basic));
                dasm.setPerpay(decimalFormat.format(incr_perpay));
                dasm.setGrpay(decimalFormat.format(incr_grpay));
                dasm.setDue(decimalFormat.format(incr_due));
                dasm.setDrawn(decimalFormat.format(incr_drawn));
//                        dasm.setDrawn(decimalFormat.format(da));
                dasm.setArrear(decimalFormat.format(incr_arrear));
                dasm.setEpfamount(decimalFormat.format(incr_epf));
                dasm.setType("INCREMENTARREAR");
                dasm.setNet(decimalFormat.format(incr_net));
                dalist.add(dasm);
                // </editor-fold>
            }
            dam.setDaarrearlist(dalist);
            supplementaryDABillReport.DABillReportPrint(dam, filePathwithName);
            slipno++;
            // </editor-fold>
        }

        supplementaryDABillReport.DABillReportGrandtotal(dam, filePathwithName);
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryDAAcquitanceCashPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String atype, String filePathwithName) {
        System.out.println("******************** SupplementaryBillServiceImpl class EmployeeSupplementaryDAAcquitanceCashPrintOut method is calling *********************");
        Map map = new HashMap();
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SupplementaryDAAcquittanceBank sdaab = new SupplementaryDAAcquittanceBank();
        String EMPLOYEEQUERY = null;
        String paymentmode = null;
        paymentmode = "C";
        String paymenttype = "Cash";
        try {
            EMPLOYEEQUERY = "select spb.id,spb.employeeprovidentfundnumber,sm.sectionname,rm.regionname,em.employeename,dm.designation, "
                    + "spb.paymentmode,em.banksbaccount,sm.id as scode from supplementatypaybill spb  "
                    + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
                    + "left join regionmaster rm on rm.id=spb.accregion "
                    + "left join designationmaster dm on dm.designationcode=spb.designation "
                    + "left join sectionmaster sm on sm.id=spb.section "
                    + "where spb.dabatch='" + dabatch + "' "
                    + "and spb.type='DAARREAR' "
                    + "and spb.cancelled is false "
                    + "and spb.accregion='" + LoggedInRegion + "' "
                    + "and  spb.paymentmode='" + atype.trim() + "' "
                    + "order by spb.section,spb.designation";

//            EMPLOYEEQUERY = "select spb.id,spb.employeeprovidentfundnumber,sm.sectionname,rm.regionname,em.employeename,dm.designation,"
//                    + "spb.paymentmode,em.banksbaccount from supplementatypaybill spb  "
//                    + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
//                    + "left join regionmaster rm on rm.id=spb.accregion "
//                    + "left join designationmaster dm on dm.designationcode=spb.designation "
//                    + "left join sectionmaster sm on sm.id=spb.section "
//                    + "where spb.dabatch='" + dabatch + "' and spb.type='DAARREAR' and "
//                    + "spb.cancelled is false and spb.accregion='" + LoggedInRegion + "' "
//                    + "and spb.employeeprovidentfundnumber  "
//                    + "not in (select epfno from stoppayrolldetails "
//                    + "where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') and accregion='" + LoggedInRegion + "') and  spb.paymentmode='" + atype.trim() + "' "
//                    + "order by spb.section,spb.designation";

            DaArrearModel dam = null;
            int slipno = 1;
            SQLQuery Employee_Query = session.createSQLQuery(EMPLOYEEQUERY);

            if (Employee_Query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            for (ListIterator its = Employee_Query.list().listIterator(); its.hasNext();) {
                dam = new DaArrearModel();
                Object[] rows = (Object[]) its.next();
                String suppleypaybillid = (String) rows[0];
                String epfno = (String) rows[1];
                String startmonth = "";
                String endmonth = "";
                dam.setEpfno(epfno);
                dam.setSection((String) rows[2]);
                dam.setRegion((String) rows[3]);
                dam.setEmployeename((String) rows[4]);
                dam.setDesignation((String) rows[5]);
//            dam.setPaymenttype((String) rows[6]);
                dam.setPaymenttype(paymenttype);
                dam.setBankaccountno((String) rows[7]);
                String sectioncode = (String) rows[8];
                dam.setSlipno(slipno);
                dam.setBatchno(dabatch);
//                System.out.println(dam.getSlipno() + " ." + dam.getEpfno());
//                System.out.println("suppleypaybillid = " + suppleypaybillid);

                String ACQUITTANCEQUERY = "  select coalesce(sum(arrear),0.00) as arrear,coalesce(sum(epf),0.00) as epf from incrementarrearreference iar "
                        + " join supplementatypaybill as spb on spb.id=iar.suppaybillid  "
                        + " where spb.accregion = '" + LoggedInRegion + "'   and spb.dabatch ='" + dabatch + "'  and spb.section= '" + sectioncode + "' "
                        + " and employeeprovidentfundnumber='" + epfno + "' and spb.paymentmode='" + atype.trim() + "' ";

                String DAARREARMANUALENTRY = "  select coalesce(sum(arrear),0.00) as arrear,coalesce(sum(epf),0.00) as epf from daarreardiffentry dad "
                        + " join supplementatypaybill as spb on spb.id=dad.supplementarypayroll  "
                        + " where spb.accregion = '" + LoggedInRegion + "'   and spb.dabatch ='" + dabatch + "'  and spb.section= '" + sectioncode + "' "
                        + " and employeeprovidentfundnumber='" + epfno + "' and spb.paymentmode='" + atype.trim() + "' ";

                String STARTDATEQUERY = "SELECT calculatedyear , MIN(calculatedmonth)  FROM supplementarypayrollprocessingdetails WHERE "
                        + "supplementatypaybillid = '" + suppleypaybillid + "' and calculatedyear = (SELECT MIN(calculatedyear) FROM supplementarypayrollprocessingdetails "
                        + "WHERE supplementatypaybillid = '" + suppleypaybillid + "') GROUP BY calculatedyear";

                String ENDDATEQUERY = "SELECT calculatedyear, MAX(calculatedmonth)  FROM supplementarypayrollprocessingdetails WHERE "
                        + "supplementatypaybillid = '" + suppleypaybillid + "' and calculatedyear = (SELECT MAX(calculatedyear) FROM supplementarypayrollprocessingdetails "
                        + "WHERE supplementatypaybillid = '" + suppleypaybillid + "') GROUP BY calculatedyear";

                SQLQuery startdate_Query = session.createSQLQuery(STARTDATEQUERY);

                SQLQuery endate_Query = session.createSQLQuery(ENDDATEQUERY);

                Object startobj[] = (Object[]) startdate_Query.list().get(0);

                Object endobj[] = (Object[]) endate_Query.list().get(0);

                startmonth = months[Integer.valueOf(startobj[1].toString()) - 1] + "\"" + String.valueOf(startobj[0].toString()).substring(2, 4);

                endmonth = months[Integer.valueOf(endobj[1].toString()) - 1] + "\"" + String.valueOf(endobj[0].toString()).substring(2, 4);
                double daarrear = 0;
                double epf = 0;
                double daarrear_manual = 0;
                double epf_manual = 0;
                double net = 0;

                SQLQuery Ecq_Query = session.createSQLQuery(ACQUITTANCEQUERY);
                for (ListIterator it1 = Ecq_Query.list().listIterator(); it1.hasNext();) {
                    Object[] row1 = (Object[]) it1.next();
                    BigDecimal big_daamount = (BigDecimal) row1[0];
                    BigDecimal big_epfamount = (BigDecimal) row1[1];
                    daarrear = big_daamount.doubleValue();
                    epf = big_epfamount.doubleValue();
                }
                SQLQuery DA_Query = session.createSQLQuery(DAARREARMANUALENTRY);
                for (ListIterator it1 = DA_Query.list().listIterator(); it1.hasNext();) {
                    Object[] row1 = (Object[]) it1.next();
                    BigDecimal big_daamount = (BigDecimal) row1[0];
                    BigDecimal big_epfamount = (BigDecimal) row1[1];
                    daarrear_manual = big_daamount.doubleValue();
                    epf_manual = big_epfamount.doubleValue();
                }
                if ((daarrear_manual != 0) || (epf_manual != 0)) {
                    daarrear = daarrear + daarrear_manual;
                    epf = epf + epf_manual;
                }
                net = daarrear - epf;
                dam.setDaarrear(decimalFormat.format(daarrear));
                dam.setEpf(decimalFormat.format(epf));
                dam.setNet(decimalFormat.format(net));
                dam.setStartmonth(startmonth);
                dam.setEndmonth(endmonth);
                sdaab.DAAcquittanceBankReportPrint(dam, filePathwithName);
                slipno++;
            }
            sdaab.grandtotal(filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Acquitance Cash Generated Error!");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryDAAcquitanceChequePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String chequeno, String filePathwithName) {
        System.out.println("************************ SupplementaryBillServiceImpl class EmployeeSupplementaryDAAcquitanceChequePrintOut method is calling *******************");
        Map map = new HashMap();
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SupplementaryDAAcquittanceCheque sdaac = new SupplementaryDAAcquittanceCheque();
        String EMPLOYEEQUERY = null;
        String paymentmode = null;
        paymentmode = "C";
        String paymenttype = "Cash";
        long CHEQUENUMBER = Long.valueOf(chequeno);
        try {

            StringBuilder sb = new StringBuilder();

            sb.append("select spb.id,spb.employeeprovidentfundnumber,sm.sectionname,rm.regionname,em.employeename,dm.designation,");
            sb.append("spb.paymentmode,em.banksbaccount from supplementatypaybill spb  ");
            sb.append("left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber ");
            sb.append("left join regionmaster rm on rm.id=spb.accregion ");
            sb.append("left join designationmaster dm on dm.designationcode=spb.designation ");
            sb.append("left join sectionmaster sm on sm.id=spb.section ");
            sb.append("where spb.dabatch='" + dabatch + "' and spb.type='DAARREAR' and ");
            sb.append("spb.cancelled is false and spb.accregion='" + LoggedInRegion + "' ");
            sb.append("and  spb.paymentmode='B' ");
            sb.append("order by spb.section,spb.designation");

//            EMPLOYEEQUERY = "select spb.id,spb.employeeprovidentfundnumber,sm.sectionname,rm.regionname,em.employeename,dm.designation,"
//                    + "spb.paymentmode,em.banksbaccount from supplementatypaybill spb  "
//                    + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
//                    + "left join regionmaster rm on rm.id=spb.accregion "
//                    + "left join designationmaster dm on dm.designationcode=spb.designation "
//                    + "left join sectionmaster sm on sm.id=spb.section "
//                    + "where spb.dabatch='" + dabatch + "' and spb.type='DAARREAR' and "
//                    + "spb.cancelled is false and spb.accregion='" + LoggedInRegion + "' "
//                    + "and spb.employeeprovidentfundnumber  "
//                    + "not in (select epfno from stoppayrolldetails "
//                    + "where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') and accregion='" + LoggedInRegion + "') and  spb.paymentmode='B' "
//                    + "order by spb.section,spb.designation";

            DaArrearModel dam = null;
            int slipno = 1;
            SQLQuery Employee_Query = session.createSQLQuery(sb.toString());

            if (Employee_Query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            for (ListIterator its = Employee_Query.list().listIterator(); its.hasNext();) {
                dam = new DaArrearModel();
                Object[] rows = (Object[]) its.next();
                String suppleypaybillid = (String) rows[0];
                String epfno = (String) rows[1];
                String startmonth = "";
                String endmonth = "";
                dam.setEpfno(epfno);
                dam.setSection((String) rows[2]);
                dam.setRegion((String) rows[3]);
                dam.setEmployeename((String) rows[4]);
                dam.setDesignation((String) rows[5]);
//            dam.setPaymenttype((String) rows[6]);
                dam.setPaymenttype(paymenttype);
                dam.setChequeno(String.valueOf(CHEQUENUMBER));
                dam.setSlipno(slipno);
                dam.setBatchno(dabatch);
//                System.out.println(dam.getSlipno() + " ." + dam.getEpfno());
//                System.out.println("suppleypaybillid = " + suppleypaybillid);                

                String ARREARQUERY = "select seet.earningmasterid,sum(amount) from supplementaryemployeeearningstransactions seet "
                        + "left join supplementarypayrollprocessingdetails sppd on sppd.id = seet.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + suppleypaybillid + "' and seet.cancelled is false group by seet.earningmasterid";

                String EPFQUERY = "select sedt.deductionmasterid,sum(sedt.amount) FROM supplementaryemployeedeductionstransactions sedt "
                        + "left join supplementarypayrollprocessingdetails sppd on sppd.id = sedt.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + suppleypaybillid + "' and sedt.cancelled is false group by sedt.deductionmasterid";

                String STARTDATEQUERY = "SELECT calculatedyear , MIN(calculatedmonth)  FROM supplementarypayrollprocessingdetails WHERE "
                        + "supplementatypaybillid = '" + suppleypaybillid + "' and calculatedyear = (SELECT MIN(calculatedyear) FROM supplementarypayrollprocessingdetails "
                        + "WHERE supplementatypaybillid = '" + suppleypaybillid + "') GROUP BY calculatedyear";

                String ENDDATEQUERY = "SELECT calculatedyear, MAX(calculatedmonth)  FROM supplementarypayrollprocessingdetails WHERE "
                        + "supplementatypaybillid = '" + suppleypaybillid + "' and calculatedyear = (SELECT MAX(calculatedyear) FROM supplementarypayrollprocessingdetails "
                        + "WHERE supplementatypaybillid = '" + suppleypaybillid + "') GROUP BY calculatedyear";

                SQLQuery Arrear_Query = session.createSQLQuery(ARREARQUERY);

                SQLQuery Epf_Query = session.createSQLQuery(EPFQUERY);

                SQLQuery startdate_Query = session.createSQLQuery(STARTDATEQUERY);

                SQLQuery endate_Query = session.createSQLQuery(ENDDATEQUERY);

                Object startobj[] = (Object[]) startdate_Query.list().get(0);

                Object endobj[] = (Object[]) endate_Query.list().get(0);

                startmonth = months[Integer.valueOf(startobj[1].toString()) - 1] + "\"" + String.valueOf(startobj[0].toString()).substring(2, 4);

                endmonth = months[Integer.valueOf(endobj[1].toString()) - 1] + "\"" + String.valueOf(endobj[0].toString()).substring(2, 4);
                double daarrear = 0;
                double epf = 0;
                double net = 0;
                String earningid = null;
                String deductionid = null;
                for (ListIterator it1 = Arrear_Query.list().listIterator(); it1.hasNext();) {
                    Object[] row1 = (Object[]) it1.next();
                    earningid = (String) row1[0];
                    daarrear = Double.valueOf(row1[1].toString());
                }
                for (ListIterator it1 = Epf_Query.list().listIterator(); it1.hasNext();) {
                    Object[] row1 = (Object[]) it1.next();
                    deductionid = (String) row1[0];
                    epf = Double.valueOf(row1[1].toString());
                }

                net = daarrear - epf;
                dam.setDaarrear(decimalFormat.format(daarrear));
                dam.setEpf(decimalFormat.format(epf));
                dam.setNet(decimalFormat.format(net));
                dam.setStartmonth(startmonth);
                dam.setEndmonth(endmonth);
                sdaac.DAAcquittanceChequeReportPrint(dam, filePathwithName);
                slipno++;
                CHEQUENUMBER++;
            }
            sdaac.grandtotal(filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Acquitance Cash Generated Error!");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryIncrementArrearAcquitanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePathwithName) {
        System.out.println("********************* SupplementaryBillServiceImpl class EmployeeSupplementaryIncrementArrearAcquitanceDetails method is calling *****************");
        Map map = new HashMap();
//        System.out.println("asondate = " + postgresDate(asondate));
        IncrementArrearModel iam = null;
        SupplementaryIncrementArrearAcquitance siaa = new SupplementaryIncrementArrearAcquitance();
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//        String QUERY = "select em.epfno,em.employeename,spb.designation,spb.section,rm.regionname,sum(seet.amount) from supplementaryemployeeearningstransactions seet "
//                + "left join supplementarypayrollprocessingdetails sppd on seet.supplementarypayrollprocessingdetailsid=sppd.id "
//                + "left join supplementatypaybill spb on sppd.supplementatypaybillid=spb.id "
//                + "left join regionmaster rm on rm.id=spb.accregion "
//                + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
//                + "where sppd.cancelled is false and spb.date='" + postgresDate(asondate) + "' and "
//                + "spb.type='INCREMENTARREAR' and spb.cancelled is false and rm.defaultregion is true group by "
//                + "em.epfno,em.employeename,spb.designation,spb.section,rm.regionname";

        String QUERY = "select spb.id,em.epfno,em.employeename,dm.designation,sm.sectionname,rm.regionname,em.banksbaccount from  supplementatypaybill spb "
                + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
                + "left join designationmaster dm on dm.designationcode=spb.designation "
                + "left join sectionmaster sm on sm.id=spb.section "
                + "left join regionmaster rm on rm.id=spb.accregion where spb.date='" + postgresDate(asondate) + "' and "
                + "spb.type='INCREMENTARREAR' and spb.cancelled is false and spb.accregion='" + LoggedInRegion + "'";

//        String QUERY = "select spb.id,em.epfno,em.employeename,spb.designation,sm.sectionname,rm.regionname from  supplementatypaybill spb "
//                + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
//                + "left join sectionmaster sm on sm.id=spb.section "
//                + "left join regionmaster rm on rm.id=spb.accregion where spb.date='" + postgresDate(asondate) + "' and "
//                + "spb.type='INCREMENTARREAR' and spb.cancelled is false and spb.accregion='" + LoggedInRegion + "'";

        SQLQuery AcquitanceQuery = session.createSQLQuery(QUERY);
//        System.out.println("AcquitanceQuery.list().size()" + AcquitanceQuery.list().size());


        if (AcquitanceQuery.list().size() == 0) {
            map.put("ERROR", "There is no Record for the given Inputs");


            return map;


        }
        int slipno = 1;


        for (ListIterator its = AcquitanceQuery.list().listIterator(); its.hasNext();) {
            iam = new IncrementArrearModel();
            Object[] rows = (Object[]) its.next();
            String supplementarypaybillid = (String) rows[0];
            String epfno = (String) rows[1];
            iam.setEpfno(epfno);
            iam.setName((String) rows[2]);


            if (rows[3] == null) {
                iam.setDesignation("");


            } else {
                iam.setDesignation(SubString((String) rows[3], 15));


            }

            iam.setSection((String) rows[4]);
            iam.setRegion((String) rows[5]);
            iam.setBankno((String) rows[6]);

            String PAYROLLPROCESSQuery = "SELECT calculatedyear, calculatedmonth FROM supplementarypayrollprocessingdetails where "
                    + "supplementatypaybillid = '" + supplementarypaybillid + "'";

            SQLQuery PayrollQuery = session.createSQLQuery(PAYROLLPROCESSQuery);



            double deductionamount = 0;


            double earningamount = 0;



            if (PayrollQuery.list().size() > 0) {
                for (ListIterator its1 = PayrollQuery.list().listIterator(); its1.hasNext();) {
                    Object[] rows1 = (Object[]) its1.next();


                    int calculatedyear = Integer.valueOf(rows1[0].toString());


                    int calculatedmonth = Integer.valueOf(rows1[1].toString());


                    double regulardeductionamount = 0;


                    double supplementarydeductionamount = 0;

//                    String REGULARDEDUCTIONQUERY = "select sum(edt.amount) as amount from employeedeductionstransactions edt "
//                            + "left join payrollprocessingdetails ppd on ppd.id=edt.payrollprocessingdetailsid  "
//                            + "where edt.cancelled is false and ppd.employeeprovidentfundnumber='" + epfno + "' "
//                            + "and ppd.month=" + calculatedmonth + " and ppd.year=" + calculatedyear + " and edt.deductionmasterid in ('D02')";

                    String SUPPLEMENTARYDEDUCTIONQUERY = "select sum(sedt.amount) as amount from supplementaryemployeedeductionstransactions sedt "
                            + "left join supplementarypayrollprocessingdetails sppd on sedt.supplementarypayrollprocessingdetailsid=sppd.id "
                            + "where sppd.calculatedmonth=" + calculatedmonth + " and sppd.calculatedyear=" + calculatedyear + " "
                            + "and sedt.cancelled is false and deductionmasterid='D02' "
                            + "and  sppd.supplementatypaybillid= '" + supplementarypaybillid + "'";

//                    String REGULAREARNINGSQUERY = "select sum(eet.amount) as amount from employeeearningstransactions eet "
//                            + "left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid  "
//                            + "where eet.cancelled is false and ppd.employeeprovidentfundnumber='" + epfno + "' "
//                            + "and ppd.month=" + calculatedmonth + " and ppd.year=" + calculatedyear + " and "
//                            + "eet.earningmasterid in ('E01','E02','E03','E04','E06','E07','E25')";

                    String SUPPLEMENTARYEARNINGSQUERY = "select sum(seet.amount) as amount from supplementaryemployeeearningstransactions seet "
                            + "left join supplementarypayrollprocessingdetails sppd on seet.supplementarypayrollprocessingdetailsid=sppd.id "
                            + "where sppd.calculatedmonth=" + calculatedmonth + " and sppd.calculatedyear=" + calculatedyear + " "
                            + "and seet.cancelled is false and "
                            + "seet.earningmasterid in ('E01','E02','E03','E04','E06','E07','E25') "
                            + "and sppd.supplementatypaybillid= '" + supplementarypaybillid + "'";

//                    String REGULARDEDUCTIONQUERY = "select sum(amount) from employeedeductionstransactions edt "
//                            + "left join incrementarrearreference iap on iap.processid=edt.payrollprocessingdetailsid "
//                            + "where iap.suppaybillid='" + supplementarypaybillid + "' and edt.deductionmasterid='D02' "
//                            + "and iap.month=" + calculatedmonth + " and iap.year=" + calculatedyear + " and iap.type='REGULAR'";
//
//                    String SUPPLEMENTARYDEDUCTIONQUERY = "select sum(sedt.amount) from supplementaryemployeedeductionstransactions sedt "
//                            + "left join incrementarrearreference iap on iap.processid=sedt.supplementarypayrollprocessingdetailsid "
//                            + "where iap.suppaybillid='" + supplementarypaybillid + "' and sedt.deductionmasterid='D02' "
//                            + "and iap.month=" + calculatedmonth + " and iap.year=" + calculatedyear + " and iap.type='SUPPLEMENTARY'";
//
//                    String REGULAREARNINGSQUERY = "select sum(amount) from employeeearningstransactions eet "
//                            + "left join incrementarrearreference iap on iap.processid=eet.payrollprocessingdetailsid "
//                            + "where iap.suppaybillid='" + supplementarypaybillid + "' and eet.earningmasterid in ('E01','E04','E06','E07','E25') "
//                            + "and iap.month=" + calculatedmonth + " and iap.year=" + calculatedyear + " and iap.type='REGULAR'";
//
//                    String SUPPLEMENTARYEARNINGSQUERY = "select sum(amount) from supplementaryemployeeearningstransactions seet "
//                            + "left join incrementarrearreference iap on iap.processid=seet.supplementarypayrollprocessingdetailsid  "
//                            + "where iap.suppaybillid='" + supplementarypaybillid + "' and seet.earningmasterid in ('E01','E04','E06','E07','E25') "
//                            + "and iap.month=" + calculatedmonth + " and iap.year=" + calculatedyear + " and iap.type='SUPPLEMENTARY'";

//                    SQLQuery RegularDeductionQuery = session.createSQLQuery(REGULARDEDUCTIONQUERY);
                    SQLQuery SupplementaryDeductionQuery = session.createSQLQuery(SUPPLEMENTARYDEDUCTIONQUERY);
//                    SQLQuery RegularEarningsQuery = session.createSQLQuery(REGULAREARNINGSQUERY);
                    SQLQuery SupplementaryEarningsQuery = session.createSQLQuery(SUPPLEMENTARYEARNINGSQUERY);

//                    Object obj1 = (Object) RegularDeductionQuery.list().get(0);
                    Object obj2 = (Object) SupplementaryDeductionQuery.list().get(0);
//                    Object obj3 = (Object) RegularEarningsQuery.list().get(0);
                    Object obj4 = (Object) SupplementaryEarningsQuery.list().get(0);



                    double regular_earnings = 0;


                    double regular_deductions = 0;


                    double supplementary_earnings = 0;


                    double supplementary_deductions = 0;

//                    if (obj1 != null) {
//                        deductionamount += Double.valueOf(obj1.toString());
//                        regular_deductions += Double.valueOf(obj1.toString());
//                    }


                    if (obj2 != null) {
                        deductionamount += Double.valueOf(obj2.toString());
                        supplementary_deductions += Double.valueOf(obj2.toString());


                    }
//                    if (obj3 != null) {
//                        earningamount += Double.valueOf(obj3.toString());
//                        regular_earnings += Double.valueOf(obj3.toString());
//                    }
                    if (obj4 != null) {
                        earningamount += Double.valueOf(obj4.toString());
                        supplementary_earnings += Double.valueOf(obj4.toString());


                    }

//                    System.out.println(calculatedmonth + "/" + calculatedyear);
//
//                    System.out.println("Regularearings | SupplementaryEarings | RegularDeductions | SupplementaryDeductions | Total Earnings | Total Deductions");
//                    System.out.println(regular_earnings + " | " + supplementary_earnings + " | " + regular_deductions + " | " + supplementary_deductions + " | " + (regular_earnings + supplementary_earnings) + " | " + (regular_deductions + supplementary_deductions));

                }
            }

            iam.setPayment(decimalFormat.format(earningamount - deductionamount));
            iam.setSlipno(slipno);
            iam.setPaymentdate(asondate);
            siaa.getIncrementArrearAcquitancePrintWriter(iam, filePathwithName);
            slipno++;

        }


        siaa.total(iam, filePathwithName);



        return map;


    }

    public BigDecimal getEPFAmount(Session session, float totalDA, String asondate) {
//        float total = 0;
        float earamt = 0;

        String querystr = " case when periodto is null then periodfrom <= '" + postgresDate(asondate) + "' else '" + postgresDate(asondate) + "' between "
                + "periodfrom and periodto end";

        Criteria earSlapCrit = session.createCriteria(
                Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='D02'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + totalDA));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + totalDA));
        earSlapCrit.add(Restrictions.sqlRestriction(querystr));
        List earSlapList = earSlapCrit.list();

        if (earSlapList.size()
                > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                float perc = EarningslapdetailsObj.getPercentage().floatValue() / 100;
                totalDA = totalDA * perc;
                float x = totalDA;

                BigDecimal b = new BigDecimal(x);
                b = b.setScale(2, RoundingMode.UP);
                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
                y.floatValue();
                earamt = y.floatValue();

            } else {
                earamt = EarningslapdetailsObj.getAmount().floatValue();
            }

        }
        BigDecimal earamount = new BigDecimal(earamt);
        return earamount;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getSupplementaryPaidDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String supplementarytype, String epfno) {
        System.out.println("********************* SupplementaryBillServiceImpl class getSupplementaryPaidDetails method is calling *****************");
        Map map = new HashMap();
        DecimalFormat df = new DecimalFormat("####0.00");


        try {
            String QUERY = null;
            StringBuffer buffer = new StringBuffer();

            String QUERYALL = "select spb.employeeprovidentfundnumber,spb.date,spb.type,em.employeename,sn.sectionname,dm.designation,spb.id "
                    + "from supplementatypaybill spb "
                    + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                    + "left join  sectionmaster sn on spb.section=sn.id "
                    + "left join  regionmaster rm on spb.accregion=rm.id "
                    + "left join  designationmaster dm on spb.designation=dm.designationcode "
                    + "where spb.accregion='" + LoggedInRegion + "' and spb.cancelled is false and "
                    + "spb.date between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "' order by spb.type,spb.date desc";

            String QUERYWITHTYPE = "select spb.employeeprovidentfundnumber,spb.date,spb.type,em.employeename,sn.sectionname,dm.designation,spb.id "
                    + "from supplementatypaybill spb "
                    + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                    + "left join  sectionmaster sn on spb.section=sn.id "
                    + "left join  regionmaster rm on spb.accregion=rm.id "
                    + "left join  designationmaster dm on spb.designation=dm.designationcode "
                    + "where spb.accregion='" + LoggedInRegion + "' and spb.cancelled is false and "
                    + "spb.type='" + supplementarytype + "' and spb.date between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' order by spb.type,spb.date desc";

            String QUERYALLWITHPFNO = "select spb.employeeprovidentfundnumber,spb.date,spb.type,em.employeename,sn.sectionname,dm.designation,spb.id "
                    + "from supplementatypaybill spb "
                    + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                    + "left join  sectionmaster sn on spb.section=sn.id "
                    + "left join  regionmaster rm on spb.accregion=rm.id "
                    + "left join  designationmaster dm on spb.designation=dm.designationcode "
                    + "where spb.accregion='" + LoggedInRegion + "' and spb.cancelled is false and  "
                    + "spb.employeeprovidentfundnumber='" + epfno + "' and spb.date between '" + postgresDate(startingdate) + "' "
                    + "and '" + postgresDate(enddate) + "' order by spb.type,spb.date desc";

            String QUERYWITHTYPEANDPFNO = "select spb.employeeprovidentfundnumber,spb.date,spb.type,em.employeename,sn.sectionname,dm.designation,spb.id "
                    + "from supplementatypaybill spb "
                    + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                    + "left join  sectionmaster sn on spb.section=sn.id "
                    + "left join  regionmaster rm on spb.accregion=rm.id "
                    + "left join  designationmaster dm on spb.designation=dm.designationcode "
                    + "where spb.accregion='" + LoggedInRegion + "' and spb.cancelled is false and "
                    + "spb.type='" + supplementarytype + "' and spb.employeeprovidentfundnumber='" + epfno + "' and "
                    + "spb.date between '" + postgresDate(startingdate) + "' and '" + postgresDate(enddate) + "' order by spb.type,spb.date desc";



            if (epfno.length() > 0 && supplementarytype.length() > 0) {
                QUERY = QUERYWITHTYPEANDPFNO;


            } else if (epfno.length() > 0 && supplementarytype.length() == 0) {
                QUERY = QUERYALLWITHPFNO;


            } else if (supplementarytype.length() > 0 && epfno.length() == 0) {
                QUERY = QUERYWITHTYPE;


            } else if (epfno.length() == 0 && supplementarytype.length() == 0) {
                QUERY = QUERYALL;


            } //            System.out.println("Query -> " + QUERY);

            SQLQuery paiddetails_query = session.createSQLQuery(QUERY);



            if (paiddetails_query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");


                return map;


            } else {

                buffer.append("<table width=\"90%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                buffer.append("<tr class=\"gridmenu\">");
                buffer.append("<td>Sno</td>");
                buffer.append("<td>Pfno</td>");
                buffer.append("<td>Name</td>");
                buffer.append("<td>Date</td>");
                buffer.append("<td>Bill Type</td>");
                buffer.append("<td>Section</td>");
                buffer.append("<td>Designation</td>");
                buffer.append("<td>Basic</td>");
                buffer.append("<td>Spl</td>");
                buffer.append("<td>DA</td>");
                buffer.append("<td>HRA</td>");
                buffer.append("<td>CCA</td>");
                buffer.append("<td>Grade Pay</td>");
//                buffer.append("<td width=\"5%\">");
                buffer.append("<td>");
//                buffer.append("<input type=\"checkbox\" name=\"billheader\" id=\"billheader\" value=\"billno\" onclick=\"SelectAll(this)\">");
                buffer.append("</td>");
                buffer.append("</tr>");



                int i = 0;



                for (ListIterator its = paiddetails_query.list().listIterator(); its.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Employee Details">
                    Object[] rows = (Object[]) its.next();
                    String pfno = (String) rows[0];
                    Date date = (Date) rows[1];
                    String sdate = dateToString(date);
                    String stype = (String) rows[2];
                    String employeename = (String) rows[3];
                    String section = (String) rows[4];
                    String designation = (String) rows[5];
                    String spid = (String) rows[6];
                    String basicpay = "";
                    String splpay = "";
                    String da = "";
                    String hra = "";
                    String cca = "";
                    String gradepay = "";
                    String classname = "";



                    if (i % 2 == 0) {
                        classname = "rowColor1";


                    } else {
                        classname = "rowColor2";


                    }

                    Map<String, Double> earningsmap = new HashMap<String, Double>();

                    // <editor-fold defaultstate="collapsed" desc="Earnings Query">
                    String EARNINGQUERY = "select earningmasterid,amount from supplementaryemployeeearningstransactions seet "
                            + "left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                            + "where sppd.supplementatypaybillid='" + spid + "' and sppd.cancelled is false";



//                    System.out.println("EARNINGQUERY -> " + EARNINGQUERY);

                    SQLQuery earnings_query = session.createSQLQuery(EARNINGQUERY);



                    if (earnings_query.list().size() > 0) {
                        for (ListIterator it1 = earnings_query.list().listIterator(); it1.hasNext();) {
                            // <editor-fold defaultstate="collapsed" desc="Earning for loop">
                            Object[] row1 = (Object[]) it1.next();
                            String earningmasterid = (String) row1[0];
                            BigDecimal amo = (BigDecimal) row1[1];


                            double amount = amo.doubleValue();
                            earningsmap.put(earningmasterid, amount);
                            // </editor-fold>


                        }
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="SET Earnings">
                    if (earningsmap.get("E01") != null) {
                        basicpay = df.format(earningsmap.get("E01"));


                    }
                    if (earningsmap.get("E02") != null) {
                        splpay = df.format(earningsmap.get("E02"));


                    }
                    if (earningsmap.get("E04") != null) {
                        da = df.format(earningsmap.get("E04"));


                    }
                    if (earningsmap.get("E06") != null) {
                        hra = df.format(earningsmap.get("E06"));


                    }
                    if (earningsmap.get("E07") != null) {
                        cca = df.format(earningsmap.get("E07"));


                    }
                    if (earningsmap.get("E25") != null) {
                        gradepay = df.format(earningsmap.get("E25"));


                    }
                    // </editor-fold>

                    buffer.append("<tr class=\"" + classname + "\">");
                    buffer.append("<td align=\"center\">" + (i + 1) + "</td>");
                    buffer.append("<td align=\"left\">" + pfno + "</td>");
                    buffer.append("<td align=\"left\">" + employeename + "</td>");
                    buffer.append("<td align=\"center\">" + sdate + "</td>");
                    buffer.append("<td align=\"center\">" + stype + "</td>");


                    if (section == null) {
                        section = "";


                    }
                    if (designation == null) {
                        designation = "";


                    }
                    buffer.append("<td align=\"left\">" + section + "</td>");
                    buffer.append("<td align=\"left\">" + designation + "</td>");
                    buffer.append("<td align=\"right\">" + basicpay + "</td>");
                    buffer.append("<td align=\"right\">" + splpay + "</td>");
                    buffer.append("<td align=\"right\">" + da + "</td>");
                    buffer.append("<td align=\"right\">" + hra + "</td>");
                    buffer.append("<td align=\"right\">" + cca + "</td>");
                    buffer.append("<td align=\"right\">" + gradepay + "</td>");
                    buffer.append("<td align=\"center\">");
                    buffer.append("<input type=\"radio\" name=\"vno\" id=\"vno\" value=" + spid + ">");
                    buffer.append("</td>");
                    buffer.append("</tr>");
                    i++;
// </editor-fold>

                }


                buffer.append("</table>");
                map.put("paygrid", buffer.toString());


            }

        } catch (Exception ex) {
            map.put("ERROR", "Grid Generation Error!");
            ex.printStackTrace();


        }
        return map;


    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map employeeSupplementaryAcquaintancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String billtype, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class employeeSupplementaryAcquaintancePrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            java.sql.Date AsOnDate = DateParser.postgresDate(asondate);
            SupplementaryAcquitanceReport ar = new SupplementaryAcquitanceReport();
            PaySlipModel psm;
            String EmployeeDetails_Query = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            EmployeeDetails_Query = "select spb.id,spb.employeeprovidentfundnumber,em.employeecode,em.employeename,em.fpfno,"
                    + "rm.regionname,sn.sectionname,dm.designation,em.banksbaccount,spb.date from supplementatypaybill spb "
                    + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                    + "left join  sectionmaster sn on spb.section=sn.id "
                    + "left join  regionmaster rm on spb.accregion=rm.id "
                    + "left join  designationmaster dm on spb.designation=dm.designationcode "
                    + "where spb.type='" + billtype + "' and spb.accregion='" + LoggedInRegion + "' and "
                    + "spb.date='" + AsOnDate + "' and spb.cancelled is false order by spb.date";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);


            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");


                return map;


            }
            int printingrecordsize = employeequery.list().size();



            int slipno = 1;


            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                psm = new PaySlipModel();
//                psm.setPaymenttype(paymenttype);
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

                Date date = (Date) rows[9];
                psm.setDate(dateToString(date));
//                psm.setPayslipyear(rows[9].toString());
//                psm.setPayslipmonth(months[(Integer) rows[10] - 1]);

                String Earnings_Query = "select sum(amount) from supplementaryemployeeearningstransactions seet "
                        + "left join  supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + earn_dedu_id + "' and sppd.cancelled is false and seet.cancelled is false";

                String Deduction_Query = "select sum(amount) from supplementaryemployeedeductionstransactions sedt "
                        + "left join  supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + earn_dedu_id + "' and sppd.cancelled is false  and sedt.cancelled is false";

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
//                System.out.println("******************************************************************************");
//                System.out.println(" ->" +psm.getPfno());
//                System.out.println(" ->" +psm.getEmployeename());
//                System.out.println(" ->" +psm.getEmpno());
//                System.out.println(psm.getTotalearnings()+ " - " + psm.getTotaldeductions() + "=" + psm.getNetsalary());
//                System.out.println("******************************************************************************");
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
    public Map employeeSupplementaryEarningsLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class employeeSupplementaryEarningsLedgerPrintOut method is calling *****************");
            //DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            EarningsLedgerReport ledgerReport = new EarningsLedgerReport();
            PaySlipModel psm;
            java.sql.Date AsOnDate = DateParser.postgresDate(asondate);
            String EmployeeDetails_Query = null;
            PaySlip_Earn_Deduction_Model psedm = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            EmployeeDetails_Query = "select spb.id,spb.employeeprovidentfundnumber,em.employeecode,em.employeename,em.fpfno,"
                    + "rm.regionname,sn.sectionname,dm.designation,em.banksbaccount,spb.date from supplementatypaybill spb "
                    + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                    + "left join  sectionmaster sn on spb.section=sn.id "
                    + "left join  regionmaster rm on spb.accregion=rm.id "
                    + "left join  designationmaster dm on spb.designation=dm.designationcode "
                    + "where spb.type='SUPLEMENTARYBILL' and spb.accregion='" + LoggedInRegion + "' and "
                    + "spb.date='" + AsOnDate + "' and spb.cancelled is false order by spb.date";

            SQLQuery employeequery = session.createSQLQuery(EmployeeDetails_Query);
            if (employeequery.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }
            int slipno = 1;
            int printingrecordsize = employeequery.list().size();
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                Map<String, PaySlip_Earn_Deduction_Model> earningsmap = new ConcurrentHashMap<String, PaySlip_Earn_Deduction_Model>();
                psm = new PaySlipModel();
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
                psm.setSectionname((String) rows[6]);
                psm.setDesignation((String) rows[7]);
                psm.setBankaccountno((String) rows[8]);
                psm.setPayslipyear(rows[9].toString());
                psm.setPayslipmonth(months[2]);
                psm.setProcess(true);
                //psm.setRemarks((String) rows[12]);

                String Earnings_Query = " select pm.paycode, pm.paycodename,seet.amount  from supplementaryemployeeearningstransactions seet "
                        + " left join paycodemaster pm on seet.earningmasterid = pm.paycode"
                        + " left join  supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                        + " where sppd.supplementatypaybillid = '" + earn_dedu_id + "' and sppd.cancelled is false and seet.cancelled is false order by pm.paycode";

                String EarningsSum_Query = "select sum(amount) from supplementaryemployeeearningstransactions seet "
                        + "left join  supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + earn_dedu_id + "' and sppd.cancelled is false and seet.cancelled is false ";

                SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
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
                psm.setPrintingrecordsize(printingrecordsize);
                ledgerReport.getEarningsLedgerPrintWriter(psm, filePath);
                slipno++;
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
    public Map employeeSupplementaryDeductionLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePath) {
        Map map = new HashMap();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class employeeSupplementaryDeductionLedgerPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DeductionLedgerReport ledgerReport = new DeductionLedgerReport();
            PaySlipModel psm = new PaySlipModel();
            java.sql.Date AsOnDate = DateParser.postgresDate(asondate);
            String EmployeeDetails_Query = null;
            PaySlip_Earn_Deduction_Model psedm = null;
            PaySlip_Earn_Deduction_Model psedm1 = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            EmployeeDetails_Query = "select spb.id,spb.employeeprovidentfundnumber,em.employeecode,em.employeename,em.fpfno,"
                    + "rm.regionname,sn.sectionname,dm.designation,em.banksbaccount,spb.date from supplementatypaybill spb "
                    + "left join  employeemaster em on spb.employeeprovidentfundnumber = em.epfno "
                    + "left join  sectionmaster sn on spb.section=sn.id "
                    + "left join  regionmaster rm on spb.accregion=rm.id "
                    + "left join  designationmaster dm on spb.designation=dm.designationcode "
                    + "where spb.type='SUPLEMENTARYBILL' and spb.accregion='" + LoggedInRegion + "' and "
                    + "spb.date='" + AsOnDate + "' and spb.cancelled is false order by spb.date";

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
                psm.setPayslipmonth(months[3]);
                //psm.setPaymentmode(rows[11].toString());
                psm.setProcess(true);
                //psm.setRemarks((String) rows[14]);

                String Deduction_Query = "select pm.paycode,pm.paycodename,sedt.amount from supplementaryemployeedeductionstransactions sedt "
                        + " left join paycodemaster pm on sedt.deductionmasterid = pm.paycode "
                        + "left join  supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + earn_dedu_id + "' and sppd.cancelled is false  and sedt.cancelled is false ";

                String DeductionSum_Query = "select sum(amount) from supplementaryemployeedeductionstransactions sedt "
                        + "left join  supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + earn_dedu_id + "' and sppd.cancelled is false  and sedt.cancelled is false";

                String EarningsSum_Query = "select sum(amount)  as earningsamount from supplementaryemployeeearningstransactions seet "
                        + "left join  supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                        + "where sppd.supplementatypaybillid = '" + earn_dedu_id + "' and sppd.cancelled is false and seet.cancelled is false";

                SQLQuery deductionquery = session.createSQLQuery(Deduction_Query);
                //List<PaySlip_Earn_Deduction_Model> earninglist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                for (ListIterator it = deductionquery.list().listIterator(); it.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Deduction_Query">
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
                slipno++;
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

    private Map<String, PaySlip_Earn_Deduction_Model> sortByKeys(Map<String, PaySlip_Earn_Deduction_Model> deductionmap) {
        List<String> keys = new LinkedList<String>(deductionmap.keySet());
        Collections.sort(keys);
        Map<String, PaySlip_Earn_Deduction_Model> sortedMap = new LinkedHashMap<String, PaySlip_Earn_Deduction_Model>();
        for (String key : keys) {
            sortedMap.put(key, deductionmap.get(key));
        }
        return sortedMap;
    }

    private Integer getLastDate(String processDate) {
        String Datestring = processDate;
        SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
        try {
            fm.parse(Datestring);


        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = fm.getCalendar();
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return iLasDay;


    }

    private String SubString(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);


        } else {
            return str;


        }
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryIncrementArrearAbstractDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePathwithName) {
        Map map = new HashMap();
        try {
            System.out.println("********************* SupplementaryBillServiceImpl class EmployeeSupplementaryIncrementArrearAbstractDetails method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
//            PayBillPrinter pbp = new PayBillPrinter();
//            AbstractReport ar = new AbstractReport();
            SupplementaryIncrementArrearAbstract siaa = new SupplementaryIncrementArrearAbstract();
            PaySlipModel psm;
            String SectionDetails_Query = null;
            PaySlip_Earn_Deduction_Model psedm = null;
            PaySlip_Earn_Deduction_Model psedm1 = null;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            String EarningsList_Query = null;
            String DeductionList_Query = null;

            EarningsList_Query = "select seet.earningmasterid,pm.paycodename,sum(seet.amount) as amount from supplementaryemployeeearningstransactions seet "
                    + "left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                    + "left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                    + "left join paycodemaster pm on pm.paycode=seet.earningmasterid "
                    + "where seet.cancelled is false and sppd.cancelled is false and spb.date='" + postgresDate(asondate) + "' "
                    + "and spb.type='INCREMENTARREAR' "
                    + "and spb.cancelled is false "
                    + "and spb.accregion='" + LoggedInRegion + "' group by seet.earningmasterid,pm.paycodename order by pm.paycodename";

            DeductionList_Query = "select sedt.deductionmasterid,pm.paycodename,sum(sedt.amount) as amount from supplementaryemployeedeductionstransactions sedt "
                    + "left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid "
                    + "left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                    + "left join paycodemaster pm on pm.paycode=sedt.deductionmasterid "
                    + "where sedt.cancelled is false and sppd.cancelled is false and spb.date='" + postgresDate(asondate) + "' "
                    + "and spb.type='INCREMENTARREAR' "
                    + "and spb.cancelled is false "
                    + "and spb.accregion='" + LoggedInRegion + "' group by sedt.deductionmasterid,pm.paycodename order by pm.paycodename";

            Criteria reCrit = session.createCriteria(
                    Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";

            if (reList.size()
                    > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }
//            System.out.println("EarningsList_Query = " + EarningsList_Query);
//            System.out.println("DeductionList_Query = " + DeductionList_Query);
            SQLQuery earningsquery = session.createSQLQuery(EarningsList_Query);
            SQLQuery deductionquery = session.createSQLQuery(DeductionList_Query);
            int earningrecordsize = earningsquery.list().size();
            int deductionrecordsize = deductionquery.list().size();
            if (earningrecordsize == 0 && deductionrecordsize
                    == 0) {
                map.put("ERROR", "There is no data for the Given Inputs");
                return map;
            }
            double earningstotal = 0;
            double deductiontotal = 0;
            List<PaySlip_Earn_Deduction_Model> earningslist = new ArrayList<PaySlip_Earn_Deduction_Model>();
//                    System.out.println("Section Name ::::::::::::::::::::::::: " + psm.getSectionname());
//
//                    System.out.println("*********************** Earning List ***************************");
            for (ListIterator ite = earningsquery.list().listIterator();
                    ite.hasNext();) {
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
//                        System.out.println(psedm.getEarningsname() + " - " + psedm.getEarningsamount());
                earningslist.add(psedm);
            }
//                    System.out.println("----------------------------------------------------------------");
            List<PaySlip_Earn_Deduction_Model> deductionlist = new ArrayList<PaySlip_Earn_Deduction_Model>();
//                    System.out.println("*********************** Deduction List ***************************");
            for (ListIterator itd = deductionquery.list().listIterator();
                    itd.hasNext();) {
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
//                        System.out.println(psedm.getDeductionname() + " - " + psedm.getDeductionamount());
                deductionlist.add(psedm);
            }
//                    System.out.println("----------------------------------------------------------------");
//
//                    System.out.println("Before earningslist.size - >" + earningslist.size());
//                    System.out.println("Before deductionlist.size - >" + deductionlist.size());

            if (earningslist.size()
                    < deductionlist.size()) {

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
            psm = new PaySlipModel();

            psm.setPayslipyear(asondate);

            psm.setBranch(regionname);

            psm.setTotalearnings(decimalFormat.format(earningstotal));

            psm.setTotaldeductions(decimalFormat.format(deductiontotal));

            psm.setEarningslist(earningslist);

            psm.setDeductionlist(deductionlist);

            psm.setNetsalary(decimalFormat.format(totalnetsalary));
            psm.setNetsalarywords(AmountInWords.convertAmountintoWords(psm.getNetsalary()));
            siaa.getSalaryAbstractPrintWriter(psm, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "Supplementary Abstract Generated Error");
            ex.printStackTrace();


        } finally {
        }
        return map;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryDADBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String filePathwithName) {
        System.out.println("***************************** class SupplementaryBillServiceImpl method EmployeeSupplementaryDADBFPrintOut is calling ******************************");
        Map map = new HashMap();
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SupplementaryDADBFReport sdadbfr = new SupplementaryDADBFReport();
        DaArrearModel dam = null;
        int slipno = 1;


        try {
//            String EMPLOYEEQUERY = "select spb.id, spb.employeeprovidentfundnumber, rm.regionname, sm.sectionname, spb.paymentmode, dm.designation, "
//                    + "em.employeename, em.banksbaccount, spb.section  from supplementatypaybill spb "
//                    + "left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber "
//                    + "left join regionmaster rm on rm.id=spb.accregion  "
//                    + "left join sectionmaster sm on sm.id=spb.section  "
//                    + "left join designationmaster dm on dm.designationcode=spb.designation  "
//                    + "where spb.dabatch='" + dabatch + "' and "
//                    + "spb.cancelled is false and "
//                    + "spb.paymentmode='C' and "
//                    + "spb.section not in('S13','S14') and "
//                    + "spb.employeeprovidentfundnumber not in (select epfno from stoppayrolldetails where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') and accregion='" + LoggedInRegion + "') and "
//                    + "spb.accregion='" + LoggedInRegion + "' "
//                    + "order by spb.section,dm.orderno";
            StringBuilder sb = new StringBuilder();

            sb.append("select spb.id, spb.employeeprovidentfundnumber, rm.regionname, sm.sectionname, spb.paymentmode, dm.designation, ");
            sb.append("em.employeename, em.banksbaccount, spb.section  from supplementatypaybill spb ");
            sb.append("left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber ");
            sb.append("left join regionmaster rm on rm.id=spb.accregion ");
            sb.append("left join sectionmaster sm on sm.id=spb.section ");
            sb.append("left join designationmaster dm on dm.designationcode=spb.designation ");
            sb.append("where ");
            sb.append("spb.dabatch='" + dabatch + "' ");
            sb.append("and spb.cancelled is false ");
            sb.append("and spb.paymentmode='C' ");
            sb.append("and spb.employeeprovidentfundnumber not in (select epfno from stoppayrolldetails where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') and accregion='" + LoggedInRegion + "') ");
            sb.append("and spb.accregion='" + LoggedInRegion + "' ");
            sb.append("order by spb.section,dm.orderno");


            SQLQuery Employee_Query = session.createSQLQuery(sb.toString());
//        System.out.println("Employee_Query.list().size()" + Employee_Query.list().size());
            //System.out.println("QUERY = " + sb.toString());
            if (Employee_Query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            List<DaArrearModel> dalist = new ArrayList<DaArrearModel>();

            for (ListIterator its = Employee_Query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                dam = new DaArrearModel();
                Object[] rows = (Object[]) its.next();

                String suppleypaybillid = (String) rows[0];
                String epfno = (String) rows[1];
                String region = (String) rows[2];
                String section = (String) rows[3];
                String paymentmode = (String) rows[4];
                String designation = (String) rows[5];
                String employeename = (String) rows[6];
                String bankaccountno = (String) rows[7];
                String sectioncode = (String) rows[8];

                dam.setEpfno(epfno);
                dam.setRegion(region);
                dam.setSection(SubString(section, 20));
                dam.setPaymenttype(paymentmode);
                dam.setDesignation(designation);
                dam.setEmployeename(employeename);
                dam.setBankaccountno(bankaccountno);
                dam.setSectioncode(sectioncode);

                String EARNINGSQUERY = "select sum(amount) from supplementaryemployeeearningstransactions seet "
                        + "left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid  "
                        + "where sppd.supplementatypaybillid='" + suppleypaybillid + "' and "
                        + "seet.earningmasterid='E04' and sppd.cancelled is false and seet.cancelled is false";

                String DEDUCTIONQUERY = "select sum(amount) from supplementaryemployeedeductionstransactions sedt "
                        + "left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid  "
                        + "where sppd.supplementatypaybillid='" + suppleypaybillid + "' and "
                        + "sedt.deductionmasterid='D02' and sppd.cancelled is false and sedt.cancelled is false";

                SQLQuery earning_Query = session.createSQLQuery(EARNINGSQUERY);

                SQLQuery deduction_Query = session.createSQLQuery(DEDUCTIONQUERY);

                BigDecimal big_daamount = (BigDecimal) earning_Query.list().get(0);

                BigDecimal big_epfamount = (BigDecimal) deduction_Query.list().get(0);

                double daamount = big_daamount.doubleValue();
                double epfamount = big_epfamount.doubleValue();

                dam.setDaarrear(decimalFormat.format(daamount));
                dam.setEpf(decimalFormat.format(epfamount));
                System.out.println("suppleypaybillid: "+suppleypaybillid);
                System.out.println(epfno);
                System.out.println(daamount);
                System.out.println(epfamount);
                dalist.add(dam);
                // </editor-fold>
            }

            sdadbfr.DABillReportPrint(dalist, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "DA DBF Report Generated Error!");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryDAAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String paymentmode, String amonth, String ayear, String bmonth, String byear, String filePathwithName) {
        System.out.println("***************************** class SupplementaryBillServiceImpl method EmployeeSupplementaryDAAbstractPrintOut is calling ******************************");
        Map map = new HashMap();
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SupplementaryDAAbstractReport sdaar = new SupplementaryDAAbstractReport();
        DaArrearModel dam = null;
        int slipno = 1;
        try {
            String SECTIONQUERY = null;
            if (paymentmode.length() == 0) {
                SECTIONQUERY = "select spb.section, sm.sectionname, rm.regionname from supplementatypaybill spb "
                        + "left join sectionmaster sm on sm.id=spb.section "
                        + "left join regionmaster rm on rm.id=spb.accregion "
                        + "where spb.accregion = '" + LoggedInRegion + "' "
                        + "and spb.dabatch='" + dabatch + "' "
                        //                        + "and spb.section not in('S13','S14') "
                        + "and spb.employeeprovidentfundnumber not in (select epfno from stoppayrolldetails where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') and accregion='" + LoggedInRegion + "') "
                        + "and spb.cancelled is false "
                        + "group by spb.section,sm.sectionname,rm.regionname order by spb.section";
            } else {
                SECTIONQUERY = "select spb.section, sm.sectionname, rm.regionname from supplementatypaybill spb "
                        + "left join sectionmaster sm on sm.id=spb.section "
                        + "left join regionmaster rm on rm.id=spb.accregion "
                        + "where spb.accregion = '" + LoggedInRegion + "' "
                        + "and spb.dabatch='" + dabatch + "' "
                        //                        + "and spb.section not in('S13','S14') "
                        + "and spb.employeeprovidentfundnumber not in (select epfno from stoppayrolldetails where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') and accregion='" + LoggedInRegion + "') "
                        + "and spb.cancelled is false "
                        + "and spb.paymentmode='" + paymentmode + "' "
                        + "group by spb.section,sm.sectionname,rm.regionname order by spb.section";
            }
            SQLQuery section_Query = session.createSQLQuery(SECTIONQUERY);
            if (section_Query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = section_Query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                dam = new DaArrearModel();
                Object[] rows = (Object[]) its.next();

                String sectioncode = (String) rows[0];
                String sectionname = (String) rows[1];
                String regionname = (String) rows[2];
                String mode = "";
                if (paymentmode.equals("C")) {
                    mode = "BANK";
                } else if (paymentmode.equals("B")) {
                    mode = "CHEQUE";
                } else {
                    mode = "ALL";
                }

                dam.setRegion(regionname);
                dam.setSection(SubString(sectionname, 25));
                dam.setPaymenttype(mode);
                dam.setBatchno(dabatch);

                double daamount = 0;
                double epfamount = 0;
                double daamount_manual = 0;
                double epfamount_manual = 0;

                StringBuilder sbearings = new StringBuilder();
                StringBuilder sbdaarrearmanual = new StringBuilder();
                sbearings.append(" select coalesce(sum(arrear),0.00) as daamount ,coalesce(sum(epf),0.00) as epfamount from incrementarrearreference iar ");
                sbearings.append("join supplementatypaybill as spb on spb.id=iar.suppaybillid  ");
                sbearings.append("where spb.accregion = '" + LoggedInRegion + "'   and spb.dabatch ='" + dabatch + "'  ");
                if (paymentmode.length() > 0) {
                    sbearings.append(" and spb.paymentmode='" + paymentmode + "' ");
                }
                sbearings.append("and spb.section= '" + sectioncode + "'  and year= " + ayear + " and month between " + amonth + " and " + bmonth + "");

                sbdaarrearmanual.append(" select coalesce(sum(arrear),0.00) as arrear,coalesce(sum(epf),0.00) as epf from daarreardiffentry dad ");
                sbdaarrearmanual.append(" join supplementatypaybill as spb on spb.id=dad.supplementarypayroll  ");
                sbdaarrearmanual.append(" where spb.accregion = '" + LoggedInRegion + "'   and spb.dabatch ='" + dabatch + "'  ");
                sbdaarrearmanual.append(" and spb.section= '" + sectioncode + "' and dad.year= " + ayear + " and dad.month between " + amonth + " and " + bmonth + " ");
                if (paymentmode.length() > 0) {
                    sbdaarrearmanual.append(" and spb.paymentmode='" + paymentmode + "' ");
                }
                SQLQuery earning_Query = session.createSQLQuery(sbearings.toString());
                for (ListIterator its1 = earning_Query.list().listIterator(); its1.hasNext();) {
                    Object[] rows1 = (Object[]) its1.next();
                    BigDecimal big_daamount = (BigDecimal) rows1[0];
                    BigDecimal big_epfamount = (BigDecimal) rows1[1];
                    daamount = big_daamount.doubleValue();
                    epfamount = big_epfamount.doubleValue();
                }
                SQLQuery DA_Query = session.createSQLQuery(sbdaarrearmanual.toString());
                for (ListIterator it1 = DA_Query.list().listIterator(); it1.hasNext();) {
                    Object[] row1 = (Object[]) it1.next();
                    BigDecimal big_daamount = (BigDecimal) row1[0];
                    BigDecimal big_epfamount = (BigDecimal) row1[1];
                    daamount_manual = big_daamount.doubleValue();
                    epfamount_manual = big_epfamount.doubleValue();
                }
                if ((daamount_manual != 0) || (epfamount_manual != 0)) {
                    daamount = daamount + daamount_manual;
                    epfamount = epfamount + epfamount_manual;
                }
                dam.setDaarrear(decimalFormat.format(daamount));
                dam.setEpf(decimalFormat.format(epfamount));
                dam.setSlipno(slipno);
                dam.setStartmonth(months[Integer.parseInt(amonth) - 1] + "'" + ayear);
                dam.setEndmonth(months[Integer.parseInt(bmonth) - 1] + "'" + ayear);
                sdaar.DAAbstractReportPrint(dam, filePathwithName);
                slipno++;
                // </editor-fold>
            }

            sdaar.grandtotal(filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "DA Abstract Report Generated Error!");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeSupplementaryDALedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String month, String year, String filePathwithName) {
        System.out.println("***************************** class SupplementaryBillServiceImpl method EmployeeSupplementaryDALedgerPrintOut is calling ******************************");
        Map map = new HashMap();
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        SupplementaryDALedgerReport sdalr = new SupplementaryDALedgerReport();
        DaArrearModel dam = null;
        int YEAR = Integer.valueOf(year);
        int MONTH = Integer.valueOf(month) + 1;
        int slipno = 1;
        try {
            String EMPLOYEEQUERY = "select pp.id,em.epfno,em.employeename,rm.regionname,sn.sectionname,dm.designation,pp.process,pp.remarks "
                    + "from payrollprocessingdetails pp "
                    + "left join employeemaster em on em.epfno = pp.employeeprovidentfundnumber "
                    + "left join regionmaster rm on pp.accregion=rm.id "
                    + "left join sectionmaster sn on pp.section=sn.id "
                    + "left join designationmaster dm on pp.designation=dm.designationcode "
                    + "where "
                    + "pp.year=" + YEAR + " "
                    + "and pp.month=" + MONTH + " "
                    + "and pp.accregion='" + LoggedInRegion + "' "
                    + "and pp.employeeprovidentfundnumber not in (select epfno from stoppayrolldetails where (reasoncode='RETIRED' or reasoncode='TRANSFERRED') and accregion='" + LoggedInRegion + "') "
                    + "order by pp.section,cast(dm.orderno as numeric),pp.employeeprovidentfundnumber";

            SQLQuery employee_Query = session.createSQLQuery(EMPLOYEEQUERY);
//        System.out.println("Employee_Query.list().size()" + Employee_Query.list().size());
//            System.out.println("SECTIONQUERY = " + SECTIONQUERY);
            if (employee_Query.list().size() == 0) {
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
            }

            for (ListIterator its = employee_Query.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String payrollid = (String) rows[0];
                String epfno = (String) rows[1];
                String employeename = (String) rows[2];
                String region = (String) rows[3];
                String section = (String) rows[4];
                String designation = (String) rows[5];
                boolean process = Boolean.valueOf(rows[6].toString());
                String remarks = (String) rows[7];

                double daamount = 0;
                double epfamount = 0;
                double netamount = 0;


                String EARNINGSQUERY = "select sum(amount) earningamount from supplementaryemployeeearningstransactions  seet "
                        + "left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid "
                        + "left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                        + "where spb.accregion = '" + LoggedInRegion + "' "
                        + "and spb.dabatch='" + dabatch + "' "
                        + "and spb.employeeprovidentfundnumber='" + epfno + "' "
                        + "and spb.cancelled is false "
                        + "and sppd.cancelled is false "
                        + "and seet.cancelled is false "
                        + "and seet.earningmasterid='E04'";

                String DEDUCTIONQUERY = "select sum(amount) deductionamount from supplementaryemployeedeductionstransactions  sedt "
                        + "left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid "
                        + "left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                        + "where spb.accregion = '" + LoggedInRegion + "' "
                        + "and spb.dabatch='" + dabatch + "' "
                        + "and spb.employeeprovidentfundnumber='" + epfno + "' "
                        + "and spb.cancelled is false "
                        + "and sppd.cancelled is false "
                        + "and sedt.cancelled is false "
                        + "and sedt.deductionmasterid='D02'";


                SQLQuery earning_Query = session.createSQLQuery(EARNINGSQUERY);

                SQLQuery deduction_Query = session.createSQLQuery(DEDUCTIONQUERY);

//                System.out.println("EARNINGSQUERY = " + EARNINGSQUERY);
//                System.out.println("DEDUCTIONQUERY = " + DEDUCTIONQUERY);

                if (earning_Query.list().get(0) != null) {
                    BigDecimal big_daamount = (BigDecimal) earning_Query.list().get(0);
                    daamount = big_daamount.doubleValue();
                }

                if (deduction_Query.list().get(0) != null) {
                    BigDecimal big_epfamount = (BigDecimal) deduction_Query.list().get(0);
                    epfamount = big_epfamount.doubleValue();
                }
                netamount = daamount - epfamount;

                dam = new DaArrearModel();
                dam.setEpfno(epfno);
                dam.setEmployeename(employeename);
                dam.setSection(section);
                dam.setDesignation(designation);
                dam.setRegion(region);
                dam.setProcess(process);
                dam.setRemarks(remarks);
                dam.setDaarrear(decimalFormat.format(daamount));
                dam.setEpf(decimalFormat.format(epfamount));
                dam.setNet(decimalFormat.format(netamount));
                dam.setBatchno(dabatch);

                dam.setSlipno(slipno);
                sdalr.DALedgerReportPrint(dam, filePathwithName);
//                System.out.println("slipno = " + slipno);
                slipno++;
                // </editor-fold>
            }
            sdalr.grandtotal(filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "DA Abstract Report Generated Error!");
            ex.printStackTrace();
        }
        return map;
    }

    private String zeroFill(int no) {
        String str = null;
        String st = String.valueOf(no);
        return (st.length() > 1) ? st : "0" + st;
    }

    public String getEmployeeLastSalaryDetail(Session session, String epfno) {
        String selectQuery = " SELECT max(year||'-'||month||'-'||accregion) as salarylastwithdrawn from payrollprocessingdetails  where process ='true' and employeeprovidentfundnumber ='" + epfno + "' ";
        List subSalaryStruList = (ArrayList) session.createSQLQuery(selectQuery).list();
        String lastRegion = "";
        if (subSalaryStruList.size() > 0) {
            String ltRegion = (String) subSalaryStruList.get(0);
            lastRegion = ltRegion.split("-")[2];
        }
        return lastRegion;
    }
}