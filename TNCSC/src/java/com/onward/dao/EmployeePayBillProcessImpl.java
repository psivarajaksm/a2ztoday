/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.persistence.payroll.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sivaraja.P
 */
public class EmployeePayBillProcessImpl extends OnwardAction implements EmployeePayBillProcess {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map payRollProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String processDate, String serialno, String epfno) {
        Map resultMap = new HashMap();
//        System.out.println("ProcessDate" + processDate);
        Employeemaster employeemasterObje = null;
        int serialNumber = Integer.parseInt(serialno) + 1;
        String salaryStructureId = "";
        Transaction transaction;
        Payrollprocessingdetails payrollprocessingdetailsObj;
        Employeeearningstransactions employeeearningstransactionsObj;
        Employeedeductionstransactions employeedeductionstransactionsObj;
        Employeeearningsdetails employeeearningsdetailsObje;
        Employeedeductiondetails employeedeductiondetailsObje;
        Payrollprocessing payrollprocessingObje = null;
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
        //int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        System.out.println("Days of Month " + iLasDay);
//        System.out.println("Month is " + iMonth);
        boolean proceed = false;
        Criteria proCrit = session.createCriteria(Payrollprocessing.class);
        proCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
        proCrit.add(Restrictions.sqlRestriction("year=" + iYear));
//        System.out.println("Logged In Region" + LoggedInRegion);
        proCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        List proList = proCrit.list();
        if (proList.size() > 0) {
            payrollprocessingObje = (Payrollprocessing) proList.get(0);
            if (payrollprocessingObje.getIsopen()) {
                proceed = true;
            } else {
                resultMap.put("reason", "Already Processed and closed");
                proceed = false;
            }
        } else {
            int yea = 0;
            int mon = 0;
            if (iMonth == 1) {
                yea = iYear - 1;
                mon = 12;
            } else {
                yea = iYear;
                mon = iMonth - 1;
            }

            Criteria proCrit1 = session.createCriteria(Payrollprocessing.class);
            proCrit1.add(Restrictions.sqlRestriction("month=" + mon));
            proCrit1.add(Restrictions.sqlRestriction("year=" + yea));
            proCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            List proList1 = proCrit1.list();
            if (proList1.size() > 0) {
                payrollprocessingObje = (Payrollprocessing) proList1.get(0);
                if (!payrollprocessingObje.getIsopen()) {
                    payrollprocessingObje = new Payrollprocessing();
                    String procId = getMaxPayBillProcessingid(session, LoggedInRegion);
                    payrollprocessingObje.setIsopen(Boolean.TRUE);
                    payrollprocessingObje.setMonth(iMonth);
                    payrollprocessingObje.setYear(iYear);
                    payrollprocessingObje.setId(procId);
                    payrollprocessingObje.setAccregion(LoggedInRegion);
                    payrollprocessingObje.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                    payrollprocessingObje.setCreatedby(LoggedInUser);
                    payrollprocessingObje.setCreateddate(getCurrentDate());
                    transaction = session.beginTransaction();
                    session.save(payrollprocessingObje);
                    transaction.commit();
                    proceed = true;
                } else {
                    proceed = false;
                    resultMap.put("reason", "Close Previous Month and Process");
                }
            } else {
                proceed = false;
                resultMap.put("reason", "Process & Close Previous Month and for this Process");
            }


        }
//        System.out.println("Current Serial No: " + serialNumber);
        System.out.println("Region: " + LoggedInRegion + "  Epf No " + epfno);
        if (proceed) {
            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
            empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List empList = empCrit.list();
            if (empList.size() > 0) {
                resultMap.put("proceed", "yes");
                resultMap.put("serialno", serialNumber);
                employeemasterObje = (Employeemaster) empList.get(0);
                if (employeemasterObje.isProcess() == true) {
                    String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                    String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                    String queryStr = "select id from salarystructureactual where employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and periodto is null and accregion='" + LoggedInRegion + "'";
//                    System.out.println(queryStr);
                    // copy data from salary structure actual to salary structure start
                    List salaryStruActualList = (ArrayList) session.createSQLQuery(queryStr).list();
                    if (salaryStruActualList.size() > 0) {
                        String salaryStructureActualId = (String) salaryStruActualList.get(0);

                        queryStr = "select id from salarystructure where employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom='" + fromDate + "' and accregion='" + LoggedInRegion + "'";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            salaryStructureId = (String) salaryStruList.get(0);
                            salaryStructureOrder = "";
                        } else {
                            String fromDateddmmyy = String.valueOf(1) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);
                            String toDateddmmyy = String.valueOf(iLasDay) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);

                            Salarystructure salarystructureObj = new Salarystructure();
                            salaryStructureId = getMaxSalaryStructureid(session, LoggedInRegion);
                            salarystructureObj.setId(salaryStructureId);
                            salarystructureObj.setPeriodfrom(postgresDate(fromDateddmmyy));
                            salarystructureObj.setOrderno(salaryStructureOrder);
                            salarystructureObj.setPeriodto(postgresDate(toDateddmmyy));
                            salarystructureObj.setEmployeemaster(employeemasterObje);
                            salarystructureObj.setAccregion(LoggedInRegion);
                            salarystructureObj.setCreatedby(LoggedInUser);
                            salarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(salarystructureObj);
                            transaction.commit();

                        }
//                        System.out.println("sairam 1");
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeeearningsdetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeedeductiondetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        //Earnings
//                        System.out.println("sairam 2");
                        Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                        earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureActualId + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List earDetailsList = earDetailsCrit.list();
                        resultMap.put("employeeearningslength", earDetailsList.size());
                        if (earDetailsList.size() > 0) {
                            for (int j = 0; j < earDetailsList.size(); j++) {
                                Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);

                                Criteria earDe = session.createCriteria(Employeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + employeeearningsdetailsactualObj.getEarningmasterid() + "' "));
                                List earList = earDe.list();
                                if (earList.size() > 0) {
                                    Employeeearningsdetails earningsdetObj = (Employeeearningsdetails) earList.get(0);
                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());

                                    transaction = session.beginTransaction();
                                    session.update(earningsdetObj);
                                    transaction.commit();

                                } else {

                                    Employeeearningsdetails earningsdetObj = new Employeeearningsdetails();

                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());
                                    earningsdetObj.setEarningmasterid(employeeearningsdetailsactualObj.getEarningmasterid());
                                    earningsdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    earningsdetObj.setId(getMaxSalaryEmployeeEarningDetailsid(session, LoggedInRegion));
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

                                Criteria dedDe = session.createCriteria(Employeedeductiondetails.class);
                                dedDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                dedDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                dedDe.add(Restrictions.sqlRestriction("deductionmasterid = '" + employeedeductiondetailsObj.getDeductionmasterid() + "' "));
                                List dedList = dedDe.list();
                                if (dedList.size() > 0) {
                                    Employeedeductiondetails deducdetObj = (Employeedeductiondetails) dedList.get(0);
                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    transaction = session.beginTransaction();
                                    session.update(deducdetObj);
                                    transaction.commit();

                                } else {

                                    Employeedeductiondetails deducdetObj = new Employeedeductiondetails();

                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    deducdetObj.setDeductionmasterid(employeedeductiondetailsObj.getDeductionmasterid());
                                    deducdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    deducdetObj.setId(getMaxSalaryEmployeeDeductionsDetailsid(session, LoggedInRegion));
                                    deducdetObj.setAccregion(LoggedInRegion);
                                    transaction = session.beginTransaction();
                                    session.save(deducdetObj);
                                    transaction.commit();

                                }


                            }
                        }




                    }
                    // copy data from salary structure actual to salary structure end
//                    System.out.println("copy data from salary structure actual to salary structure end");
                    Criteria empBillProcessCrit = session.createCriteria(Payrollprocessingdetails.class);
                    empBillProcessCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                    List empBillProcessList = empBillProcessCrit.list();
                    if (empBillProcessList.size() > 0) {
                        payrollprocessingdetailsObj = (Payrollprocessingdetails) empBillProcessList.get(0);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        payrollprocessingdetailsObj.setDesignation(employeemasterObje.getDesignation());
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                        //payrollprocessingdetailsObj.setWorkedday(workedDay);
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        transaction = session.beginTransaction();
                        session.update(payrollprocessingdetailsObj);
                        transaction.commit();
                    } else {
                        payrollprocessingdetailsObj = new Payrollprocessingdetails();
                        String id = getMaxSeqNumberBillProcess(session, LoggedInRegion);
                        payrollprocessingdetailsObj.setId(id);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setEmployeecategory(employeemasterObje.getCategory());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setDesignation(employeemasterObje.getDesignation());
                        payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
//                        payrollprocessingdetailsObj.setWorkedday(workedDay);
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";

//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        session.save(payrollprocessingdetailsObj);
                        transaction = session.beginTransaction();
                        transaction.commit();

                    }

                    // cancel when reprocees
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeeearningstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeedeductionstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();

                    // Earning
                    Criteria empEarCrit = session.createCriteria(Employeeearningsdetails.class);
                    empEarCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empEarCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                    empEarCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List empEarList = empEarCrit.list();
                    if (empEarList.size() > 0) {
                        for (int i = 0; i < empEarList.size(); i++) {
                            employeeearningsdetailsObje = (Employeeearningsdetails) empEarList.get(i);

                            Criteria empEarTranCrit = session.createCriteria(Employeeearningstransactions.class);
                            empEarTranCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empEarTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empEarTranCrit.add(Restrictions.sqlRestriction("earningmasterid='" + employeeearningsdetailsObje.getEarningmasterid() + "'"));
                            List empEarTranList = empEarTranCrit.list();
                            if (empEarTranList.size() > 0) {

                                employeeearningstransactionsObj = (Employeeearningstransactions) empEarTranList.get(0);
                                employeeearningstransactionsObj.setEarningmasterid(employeeearningsdetailsObje.getEarningmasterid());
                                employeeearningstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                if (employeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, employeeearningsdetailsObje.getAmount()));
                                } else {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, getEarningsAmount(session, salaryStructureId, employeeearningsdetailsObje.getEarningmasterid(), processDate)));
                                }
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.update(employeeearningstransactionsObj);
                            } else {
                                String earningsTransactionId = getMaxEmployeeearningstransactionsid(session, LoggedInRegion);
                                employeeearningstransactionsObj = new Employeeearningstransactions();
                                employeeearningstransactionsObj.setId(earningsTransactionId);
                                employeeearningstransactionsObj.setEarningmasterid(employeeearningsdetailsObje.getEarningmasterid());
                                employeeearningstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                if (employeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, employeeearningsdetailsObje.getAmount()));
                                } else {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, getEarningsAmount(session, salaryStructureId, employeeearningsdetailsObje.getEarningmasterid(), processDate)));
                                }
                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeeearningstransactionsObj);
                            }
                            transaction.commit();
                        }

                    }

                    // Regular Deductions
                    Criteria empDeduCrit = session.createCriteria(Employeedeductiondetails.class);
                    empDeduCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empDeduCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                    empDeduCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List empDeduList = empDeduCrit.list();
                    if (empDeduList.size() > 0) {
                        for (int j = 0; j < empDeduList.size(); j++) {
                            employeedeductiondetailsObje = (Employeedeductiondetails) empDeduList.get(j);
                            Criteria empDeducTranCrit = session.createCriteria(Employeedeductionstransactions.class);
                            empDeducTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empDeducTranCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empDeducTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + employeedeductiondetailsObje.getDeductionmasterid() + "'"));
                            List empDeducTranList = empDeducTranCrit.list();
                            if (empDeducTranList.size() > 0) {
                                BigDecimal amt = new BigDecimal("0");
                                employeedeductionstransactionsObj = (Employeedeductionstransactions) empDeducTranList.get(0);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeedeductiondetailsObje.getDeductionmasterid());
                                if (employeedeductiondetailsObje.getAmount().equals(new BigDecimal("0.00"))) {
//                                    System.out.println("equals zero sairam");
                                    amt = getDeductionAmount(session, salaryStructureId, employeedeductiondetailsObje.getDeductionmasterid(), processDate);
                                } else {
                                    amt = employeedeductiondetailsObje.getAmount();
                                }
                                employeedeductionstransactionsObj.setAmount(amt);

//                            if (employeedeductiondetailsObje.getDeductionmasterid().equalsIgnoreCase("D02")) {                                
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount().multiply(new BigDecimal("0.12")));
//                            } else {
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount());
//                            }
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                transaction = session.beginTransaction();
                                session.update(employeedeductionstransactionsObj);
                            } else {
                                String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                                employeedeductionstransactionsObj = new Employeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeedeductiondetailsObje.getDeductionmasterid());
//                            if (employeedeductiondetailsObje.getDeductionmasterid().equalsIgnoreCase("D02")) {
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount().multiply(new BigDecimal("0.12")));
//                            } else {
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount());
//                            }
                                BigDecimal amt = new BigDecimal("0");
                                if (employeedeductiondetailsObje.getAmount().equals(new BigDecimal("0.00"))) {
//                                    System.out.println("equals zero sairam");
                                    amt = getDeductionAmount(session, salaryStructureId, employeedeductiondetailsObje.getDeductionmasterid(), processDate);
                                } else {
                                    amt = employeedeductiondetailsObje.getAmount();
                                }
                                employeedeductionstransactionsObj.setAmount(amt);
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                            }
                            transaction.commit();
                        }

                    }

                    // Miscelleneous Deductions
                    Miscdeductions miscdeductionsObje;
                    Criteria misDeducCrit = session.createCriteria(Miscdeductions.class);
                    misDeducCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeemasterObje.getEpfno() + "' "));
                    misDeducCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    misDeducCrit.add(Restrictions.sqlRestriction("year = " + iYear));
                    misDeducCrit.add(Restrictions.sqlRestriction("month = " + iMonth));
                    List misDeducList = misDeducCrit.list();
                    if (misDeducList.size() > 0) {
                        for (int j = 0; j < misDeducList.size(); j++) {
                            miscdeductionsObje = (Miscdeductions) misDeducList.get(j);

                            String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                            employeedeductionstransactionsObj = new Employeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid(miscdeductionsObje.getDeductionscode());
                            employeedeductionstransactionsObj.setAmount(miscdeductionsObje.getAmount());
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setType("MISC");
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(employeedeductionstransactionsObj);

                            transaction.commit();

                        }

                    }
                    // Miscelleneous Deductions Ends Here

                    // Deductions Others Starts

                    Salarydeductionothers salarydeductionothersObj;
                    Criteria otherDeducCrit = session.createCriteria(Salarydeductionothers.class);
                    otherDeducCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeemasterObje.getEpfno() + "' "));
                    otherDeducCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "'"));
                    otherDeducCrit.add(Restrictions.sqlRestriction("deductionmonth = " + iMonth));
                    otherDeducCrit.add(Restrictions.sqlRestriction("deductionyear = " + iYear));
                    otherDeducCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List otherDeducList = otherDeducCrit.list();
                    if (otherDeducList.size() > 0) {
                        for (int j = 0; j < otherDeducList.size(); j++) {
                            salarydeductionothersObj = (Salarydeductionothers) otherDeducList.get(j);

                            String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                            employeedeductionstransactionsObj = new Employeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid(salarydeductionothersObj.getPaycodemaster().getPaycode());
                            employeedeductionstransactionsObj.setAmount(salarydeductionothersObj.getAmountornoofdays());
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setType("OTHERSDEDUC");
                            employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
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
                    Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                    Employeeloansandadvances employeeloansandadvancesObje;
                    Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
                    List empLoanDetailsList = empLoanDetailsCrit.list();
                    if (empLoanDetailsList.size() > 0) {
                        for (int i = 0; i < empLoanDetailsList.size(); i++) {
                            transaction = session.beginTransaction();
                            employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(i);

                            employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                            employeeloansandadvancesdetailsObje.setCreatedby(LoggedInUser);
                            employeeloansandadvancesdetailsObje.setCreateddate(getCurrentDate());
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

                    //For loan deductions
                    Criteria empLoanCritUp = session.createCriteria(Employeeloansandadvances.class);
                    empLoanCritUp.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empLoanCritUp.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empLoanCritUp.add(Restrictions.sqlRestriction("loanbalance > 0"));
                    List empLoanListUp = empLoanCritUp.list();
                    if (empLoanListUp.size() > 0) {
                        for (int i = 0; i < empLoanListUp.size(); i++) {
                            employeeloansandadvancesObje = (Employeeloansandadvances) empLoanListUp.get(i);

                            Criteria empLoanDetailsCritUp = session.createCriteria(Employeeloansandadvancesdetails.class);
                            empLoanDetailsCritUp.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empLoanDetailsCritUp.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empLoanDetailsCritUp.add(Restrictions.sqlRestriction("employeeloansandadvancesid='" + employeeloansandadvancesObje.getId() + "'"));
                            List empLoanDetailsListUp = empLoanDetailsCritUp.list();
                            if (empLoanDetailsListUp.size() > 0) {
                                Employeeloansandadvancesdetails employeeloansandadvancesdetailsOb = (Employeeloansandadvancesdetails) empLoanDetailsListUp.get(0);
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
                                String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                                employeedeductionstransactionsObj = new Employeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeeloansandadvancesObje.getDeductioncode());

                                employeedeductionstransactionsObj.setAmount(employeeloansandadvancesdetailsOb.getInstallmentamount());
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setType("LOAN");
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                                transaction.commit();
                                //Adding loan transactions in salary deductions end                
                            } else {
                                Employeeloansandadvancesdetails employeeloansandadvancesdetailsOb = new Employeeloansandadvancesdetails();
                                String id = getMaxEmployeeLoansAndAdvancesDetailsid(session, LoggedInRegion);
                                employeeloansandadvancesdetailsOb.setId(id);
                                employeeloansandadvancesdetailsOb.setAccregion(LoggedInRegion);
                                employeeloansandadvancesdetailsOb.setEmployeeloansandadvances(employeeloansandadvancesObje);
                                employeeloansandadvancesdetailsOb.setPayrollprocessingdetails(payrollprocessingdetailsObj);
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
                                String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                                employeedeductionstransactionsObj = new Employeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeeloansandadvancesObje.getDeductioncode());

                                employeedeductionstransactionsObj.setAmount(employeeloansandadvancesdetailsOb.getInstallmentamount());

                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setType("LOAN");
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                                transaction.commit();





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
                    // Recovery Deductions
                    // Recovery Reprocess
                    Employeerecoverydetails employeerecoverydetailsObje;
                    Employeerecoveries employeerecoveriesObje;
                    Criteria empRecovDetailsCrit = session.createCriteria(Employeerecoverydetails.class);
                    empRecovDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                    empRecovDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
                    List empRecovDetailsList = empRecovDetailsCrit.list();
                    if (empRecovDetailsList.size() > 0) {
                        for (int i = 0; i < empRecovDetailsList.size(); i++) {
                            transaction = session.beginTransaction();
                            employeerecoverydetailsObje = (Employeerecoverydetails) empRecovDetailsList.get(i);

                            employeerecoverydetailsObje.setCancelled(Boolean.TRUE);
                            employeerecoverydetailsObje.setCreatedby(LoggedInUser);
                            employeerecoverydetailsObje.setCreateddate(getCurrentDate());
                            session.update(employeerecoverydetailsObje);

                            Criteria empRecCrit = session.createCriteria(Employeerecoveries.class);
                            empRecCrit.add(Restrictions.sqlRestriction("id='" + employeerecoverydetailsObje.getEmployeerecoveries().getId() + "'"));
                            List empRecList = empRecCrit.list();
                            if (empRecList.size() > 0) {
                                employeerecoveriesObje = (Employeerecoveries) empRecList.get(0);
                                employeerecoveriesObje.setCurrentinstallment(employeerecoveriesObje.getCurrentinstallment() - 1);
                                if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                    employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().add(employeerecoveriesObje.getFirstinstallmentamount()));
                                } else {
                                    employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().add(employeerecoveriesObje.getInstallmentamount()));
                                }
                                session.update(employeerecoveriesObje);
                            }
                            transaction.commit();
                        }
                    }
                    //For Recovery deductions
                    Criteria empRecovCritUp = session.createCriteria(Employeerecoveries.class);
                    empRecovCritUp.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empRecovCritUp.add(Restrictions.sqlRestriction("totalinstallment > currentinstallment"));
                    List empRecovListUp = empRecovCritUp.list();
                    if (empRecovListUp.size() > 0) {
                        for (int i = 0; i < empRecovListUp.size(); i++) {
                            employeerecoveriesObje = (Employeerecoveries) empRecovListUp.get(i);
                            employeerecoveriesObje.setCurrentinstallment(employeerecoveriesObje.getCurrentinstallment() + 1);
                            if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().subtract(employeerecoveriesObje.getFirstinstallmentamount()));
                            } else {
                                employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().subtract(employeerecoveriesObje.getInstallmentamount()));
                            }
                            Criteria empRecovDetailsCritUp = session.createCriteria(Employeerecoverydetails.class);
                            empRecovDetailsCritUp.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empRecovDetailsCritUp.add(Restrictions.sqlRestriction("employeerecoveriesid='" + employeerecoveriesObje.getId() + "'"));
                            List empRecovDetailsListUp = empRecovDetailsCritUp.list();
                            if (empRecovDetailsListUp.size() > 0) {
                                Employeerecoverydetails employeerecoverydetailsOb = (Employeerecoverydetails) empRecovDetailsListUp.get(0);
                                employeerecoverydetailsOb.setCancelled(Boolean.FALSE);
                                employeerecoverydetailsOb.setCreatedby(LoggedInUser);
                                employeerecoverydetailsOb.setCreateddate(getCurrentDate());
                                employeerecoverydetailsOb.setNthinstallment(employeerecoveriesObje.getCurrentinstallment());
                                employeerecoverydetailsOb.setLoanbalance(employeerecoveriesObje.getLoanbalance());
                                if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getInstallmentamount());
                                } else {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getFirstinstallmentamount());
                                }
                                transaction = session.beginTransaction();
                                session.update(employeerecoverydetailsOb);
                                transaction.commit();
                            } else {
                                Employeerecoverydetails employeerecoverydetailsOb = new Employeerecoverydetails();
                                String id = getMaxEmployeeRecoveriesDetailsid(session, LoggedInRegion);
                                employeerecoverydetailsOb.setId(id);
                                employeerecoverydetailsOb.setEmployeerecoveries(employeerecoveriesObje);
                                employeerecoverydetailsOb.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                employeerecoverydetailsOb.setCancelled(Boolean.FALSE);
                                employeerecoverydetailsOb.setCreatedby(LoggedInUser);
                                employeerecoverydetailsOb.setCreateddate(getCurrentDate());
                                employeerecoverydetailsOb.setNthinstallment(employeerecoveriesObje.getCurrentinstallment());
                                employeerecoverydetailsOb.setLoanbalance(employeerecoveriesObje.getLoanbalance());
                                if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getInstallmentamount());
                                } else {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getFirstinstallmentamount());
                                }
                                employeerecoverydetailsOb.setAccregion(LoggedInRegion);
                                transaction = session.beginTransaction();
                                session.save(employeerecoverydetailsOb);
                                transaction.commit();
                            }
                            transaction = session.beginTransaction();
                            session.update(employeerecoveriesObje);
                            transaction.commit();


                            // Adding recovery transactions in salary deductions
                            transaction = session.beginTransaction();
                            String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                            employeedeductionstransactionsObj = new Employeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid(employeerecoveriesObje.getDeductioncode());
                            if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                employeedeductionstransactionsObj.setAmount(employeerecoveriesObje.getFirstinstallmentamount());
                            } else {
                                employeedeductionstransactionsObj.setAmount(employeerecoveriesObje.getInstallmentamount());
                            }
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            employeedeductionstransactionsObj.setType("RECOVERY");
                            employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                            transaction = session.beginTransaction();
                            session.save(employeedeductionstransactionsObj);
                            transaction.commit();
                            //  Adding recovery transactions in salary deductions end  
                        }
                    }


                    //Confirm transaction save status

                    payrollprocessingdetailsObj = null;
                } else {

                    // Pay Roll Not Processing 
                    String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                    String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
//                    String queryStr = "select id from salarystructureactual where employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
//                            + " periodfrom<='" + fromDate + "' and "
//                            + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
                    String queryStr = "select id from salarystructureactual where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and periodto is null ";
//                    System.out.println(queryStr);

                    // copy data from salary structure actual to salary structure start
                    List salaryStruActualList = (ArrayList) session.createSQLQuery(queryStr).list();
                    if (salaryStruActualList.size() > 0) {
                        String salaryStructureActualId = (String) salaryStruActualList.get(0);

                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom='" + fromDate + "'";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            salaryStructureId = (String) salaryStruList.get(0);
                            salaryStructureOrder = "";
                        } else {
                            String fromDateddmmyy = String.valueOf(1) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);
                            String toDateddmmyy = String.valueOf(iLasDay) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);

                            Salarystructure salarystructureObj = new Salarystructure();
                            salaryStructureId = getMaxSalaryStructureid(session, LoggedInRegion);
                            salarystructureObj.setId(salaryStructureId);
                            salarystructureObj.setPeriodfrom(postgresDate(fromDateddmmyy));
                            salarystructureObj.setOrderno(salaryStructureOrder);
                            salarystructureObj.setPeriodto(postgresDate(toDateddmmyy));
                            salarystructureObj.setEmployeemaster(employeemasterObje);
                            salarystructureObj.setAccregion(LoggedInRegion);
                            salarystructureObj.setCreatedby(LoggedInUser);
                            salarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(salarystructureObj);
                            transaction.commit();

                        }
//                        System.out.println("sairam 1");
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeeearningsdetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeedeductiondetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        //Earnings
//                        System.out.println("sairam 2");
                        Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                        earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureActualId + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List earDetailsList = earDetailsCrit.list();
                        resultMap.put("employeeearningslength", earDetailsList.size());
                        if (earDetailsList.size() > 0) {
                            for (int j = 0; j < earDetailsList.size(); j++) {
                                Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);

                                Criteria earDe = session.createCriteria(Employeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + employeeearningsdetailsactualObj.getEarningmasterid() + "' "));
                                List earList = earDe.list();
                                if (earList.size() > 0) {
                                    Employeeearningsdetails earningsdetObj = (Employeeearningsdetails) earList.get(0);
                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());

                                    transaction = session.beginTransaction();
                                    session.update(earningsdetObj);
                                    transaction.commit();

                                } else {

                                    Employeeearningsdetails earningsdetObj = new Employeeearningsdetails();

                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());
                                    earningsdetObj.setEarningmasterid(employeeearningsdetailsactualObj.getEarningmasterid());
                                    earningsdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    earningsdetObj.setId(getMaxSalaryEmployeeEarningDetailsid(session, LoggedInRegion));
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

                                Criteria dedDe = session.createCriteria(Employeedeductiondetails.class);
                                dedDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                dedDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                dedDe.add(Restrictions.sqlRestriction("deductionmasterid = '" + employeedeductiondetailsObj.getDeductionmasterid() + "' "));
                                List dedList = dedDe.list();
                                if (dedList.size() > 0) {
                                    Employeedeductiondetails deducdetObj = (Employeedeductiondetails) dedList.get(0);
                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    transaction = session.beginTransaction();
                                    session.update(deducdetObj);
                                    transaction.commit();

                                } else {

                                    Employeedeductiondetails deducdetObj = new Employeedeductiondetails();

                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDeductionmasterid(employeedeductiondetailsObj.getDeductionmasterid());
                                    deducdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    deducdetObj.setId(getMaxSalaryEmployeeDeductionsDetailsid(session, LoggedInRegion));
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    deducdetObj.setAccregion(LoggedInRegion);
                                    transaction = session.beginTransaction();
                                    session.save(deducdetObj);
                                    transaction.commit();

                                }


                            }
                        }




                    }
                    // copy data from salary structure actual to salary structure end                 

                    String stoppay = "select reasoncode from stoppayrolldetails where accregion='" + LoggedInRegion + "' and epfno ='" + employeemasterObje.getEpfno() + "' and enddate is null";
//                    String stoppay = "select reasoncode from stoppayrolldetails where accregion='" + LoggedInRegion + "' and epfno ='" + employeemasterObje.getEpfno() + "' and startdate<='" + postgresDate(processDate) + "' and  "
//                            + " case when enddate is null then true else case when enddate>='" + postgresDate(processDate) + "' then true else false end end  ";
                    String stoppaymentreasoncode = "";
                    List stopPayList = (ArrayList) session.createSQLQuery(stoppay).list();
                    if (stopPayList.size() > 0) {
                        if (stopPayList.get(0) != null) {
                            stoppaymentreasoncode = (String) stopPayList.get(0);

                        }
                    }

                    Criteria empBillProcessCrit = session.createCriteria(Payrollprocessingdetails.class);
                    empBillProcessCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                    List empBillProcessList = empBillProcessCrit.list();
                    if (empBillProcessList.size() > 0) {
                        payrollprocessingdetailsObj = (Payrollprocessingdetails) empBillProcessList.get(0);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                        payrollprocessingdetailsObj.setSalarydays(0);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        transaction = session.beginTransaction();
                        session.update(payrollprocessingdetailsObj);
                        transaction.commit();
                    } else {
                        payrollprocessingdetailsObj = new Payrollprocessingdetails();
                        String id = getMaxSeqNumberBillProcess(session, LoggedInRegion);
                        payrollprocessingdetailsObj.setId(id);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                        payrollprocessingdetailsObj.setSalarydays(0);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";

//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        session.save(payrollprocessingdetailsObj);
                        transaction = session.beginTransaction();
                        transaction.commit();

                    }

                    // cancel when reprocees
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeeearningstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeedeductionstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();

                    // Loans and Advances Deductions
                    //For reprocessing 
                    Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                    Employeeloansandadvances employeeloansandadvancesObje;
                    Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List empLoanDetailsList = empLoanDetailsCrit.list();
                    if (empLoanDetailsList.size() > 0) {
                        for (int i = 0; i < empLoanDetailsList.size(); i++) {
                            transaction = session.beginTransaction();
                            employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(i);

                            employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                            employeeloansandadvancesdetailsObje.setCreatedby(LoggedInUser);
                            employeeloansandadvancesdetailsObje.setCreateddate(getCurrentDate());
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
                    // Pay Roll Not Processing
                }


            } else {
                resultMap.put("proceed", "no");
                // Reversing processed then transferred
                String empsql = "select employeeprovidentfundnumber from payrollprocessingdetails where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ";
                empsql = empsql + "not in (select epfno from employeemaster where region='" + LoggedInRegion + "') and month=" + iMonth + " and year=" + iYear + " and accregion='" + LoggedInRegion + "'";

                SQLQuery empquery = session.createSQLQuery(empsql);
                for (ListIterator its = empquery.list().listIterator(); its.hasNext();) {
                    String transepfno = (String) its.next();
//                    System.out.println("Reversing processed then transferred" + transepfno);
                    empCrit = session.createCriteria(Employeemaster.class);
                    empCrit.add(Restrictions.sqlRestriction("epfno='" + transepfno + "'"));
                    empList = empCrit.list();
                    if (empList.size() > 0) {
                        employeemasterObje = (Employeemaster) empList.get(0);
                        // Pay Roll Not Processing 
                        String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        String queryStr = "select id from salarystructureactual where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and periodto is null ";
                        String stoppay = "select reasoncode from stoppayrolldetails where accregion='" + LoggedInRegion + "' and epfno ='" + transepfno + "' and enddate is null";

                        String stoppaymentreasoncode = "";
                        List stopPayList = (ArrayList) session.createSQLQuery(stoppay).list();
                        if (stopPayList.size() > 0) {
                            if (stopPayList.get(0) != null) {
                                stoppaymentreasoncode = (String) stopPayList.get(0);
                            }
                        }

                        Criteria empBillProcessCrit = session.createCriteria(Payrollprocessingdetails.class);
                        empBillProcessCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + transepfno + "'"));
                        empBillProcessCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                        empBillProcessCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                        empBillProcessCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                        List empBillProcessList = empBillProcessCrit.list();
                        if (empBillProcessList.size() > 0) {
                            payrollprocessingdetailsObj = (Payrollprocessingdetails) empBillProcessList.get(0);
                            payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                            payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                            payrollprocessingdetailsObj.setMonth(iMonth);
                            payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                            payrollprocessingdetailsObj.setYear(iYear);
                            payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                            payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                            payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                            int workingDay = iLasDay;
                            payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                            payrollprocessingdetailsObj.setSalarydays(0);
                            payrollprocessingdetailsObj.setProcess(false);
                            payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(payrollprocessingdetailsObj);
                            transaction.commit();
                        } else {
                            payrollprocessingdetailsObj = new Payrollprocessingdetails();
                            String id = getMaxSeqNumberBillProcess(session, LoggedInRegion);
                            payrollprocessingdetailsObj.setId(id);
                            payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                            payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                            payrollprocessingdetailsObj.setMonth(iMonth);
                            payrollprocessingdetailsObj.setYear(iYear);
                            payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                            payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                            payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                            payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                            int workingDay = iLasDay;
                            payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                            payrollprocessingdetailsObj.setSalarydays(0);
                            payrollprocessingdetailsObj.setProcess(false);//                           
                            payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                            payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                            session.save(payrollprocessingdetailsObj);
                            transaction = session.beginTransaction();
                            transaction.commit();

                        }

                        // cancel when reprocees
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeeearningstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeedeductionstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion ='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();

                        // Loans and Advances Deductions
                        //For reprocessing 
                        Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                        Employeeloansandadvances employeeloansandadvancesObje;
                        Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                        empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                        empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                        empLoanDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empLoanDetailsList = empLoanDetailsCrit.list();
                        if (empLoanDetailsList.size() > 0) {
                            for (int i = 0; i < empLoanDetailsList.size(); i++) {
                                transaction = session.beginTransaction();
                                employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(i);

                                employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                                employeeloansandadvancesdetailsObje.setCreatedby(LoggedInUser);
                                employeeloansandadvancesdetailsObje.setCreateddate(getCurrentDate());
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
                        // Pay Roll Not Processing

                        //Reverse and Transffered
                    }
                }


                resultMap.put("reason", "Processed Successfully");
            }

        } else {
            resultMap.put("proceed", "no");
        }
        resultMap.put("msg", "Sairam");
        return resultMap;
    }

    public Map payRollProcessThread(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String processDate, String serialno, String epfno) {
        Map resultMap = new HashMap();
//        System.out.println("ProcessDate" + processDate);
        Employeemaster employeemasterObje = null;
        int serialNumber = Integer.parseInt(serialno) + 1;
        String salaryStructureId = "";
        Transaction transaction;
        Payrollprocessingdetails payrollprocessingdetailsObj;
        Employeeearningstransactions employeeearningstransactionsObj;
        Employeedeductionstransactions employeedeductionstransactionsObj;
        Employeeearningsdetails employeeearningsdetailsObje;
        Employeedeductiondetails employeedeductiondetailsObje;
        Payrollprocessing payrollprocessingObje = null;
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
        //int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        System.out.println("Days of Month " + iLasDay);
//        System.out.println("Month is " + iMonth);
        boolean proceed = false;
        Criteria proCrit = session.createCriteria(Payrollprocessing.class);
        proCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
        proCrit.add(Restrictions.sqlRestriction("year=" + iYear));
//        System.out.println("Logged In Region" + LoggedInRegion);
        proCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        List proList = proCrit.list();
        if (proList.size() > 0) {
            payrollprocessingObje = (Payrollprocessing) proList.get(0);
            if (payrollprocessingObje.getIsopen()) {
                proceed = true;
            } else {
                resultMap.put("reason", "Already Processed and closed");
                proceed = false;
            }
        } else {
            int yea = 0;
            int mon = 0;
            if (iMonth == 1) {
                yea = iYear - 1;
                mon = 12;
            } else {
                yea = iYear;
                mon = iMonth - 1;
            }

            Criteria proCrit1 = session.createCriteria(Payrollprocessing.class);
            proCrit1.add(Restrictions.sqlRestriction("month=" + mon));
            proCrit1.add(Restrictions.sqlRestriction("year=" + yea));
            proCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            List proList1 = proCrit1.list();
            if (proList1.size() > 0) {
                payrollprocessingObje = (Payrollprocessing) proList1.get(0);
                if (!payrollprocessingObje.getIsopen()) {
                    payrollprocessingObje = new Payrollprocessing();
                    String procId = getMaxPayBillProcessingid(session, LoggedInRegion);
                    payrollprocessingObje.setIsopen(Boolean.TRUE);
                    payrollprocessingObje.setMonth(iMonth);
                    payrollprocessingObje.setYear(iYear);
                    payrollprocessingObje.setId(procId);
                    payrollprocessingObje.setAccregion(LoggedInRegion);
                    payrollprocessingObje.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                    payrollprocessingObje.setCreatedby(LoggedInUser);
                    payrollprocessingObje.setCreateddate(getCurrentDate());
                    transaction = session.beginTransaction();
                    session.save(payrollprocessingObje);
                    transaction.commit();
                    proceed = true;
                } else {
                    proceed = false;
                    resultMap.put("reason", "Close Previous Month and Process");
                }
            } else {
                proceed = false;
                resultMap.put("reason", "Process & Close Previous Month and for this Process");
            }


        }
//        System.out.println("Current Serial No: " + serialNumber);
        System.out.println("Region: " + LoggedInRegion + "  Epf No " + epfno);
        if (proceed) {
            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
            empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List empList = empCrit.list();
            if (empList.size() > 0) {
                resultMap.put("proceed", "yes");
                resultMap.put("serialno", serialNumber);
                employeemasterObje = (Employeemaster) empList.get(0);
                if (employeemasterObje.isProcess() == true) {
                    String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                    String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                    String queryStr = "select id from salarystructureactual where employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and periodto is null and accregion='" + LoggedInRegion + "'";
//                    System.out.println(queryStr);
                    // copy data from salary structure actual to salary structure start
                    List salaryStruActualList = (ArrayList) session.createSQLQuery(queryStr).list();
                    if (salaryStruActualList.size() > 0) {
                        String salaryStructureActualId = (String) salaryStruActualList.get(0);

                        queryStr = "select id from salarystructure where employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom='" + fromDate + "' and accregion='" + LoggedInRegion + "'";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            salaryStructureId = (String) salaryStruList.get(0);
                            salaryStructureOrder = "";
                        } else {
                            String fromDateddmmyy = String.valueOf(1) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);
                            String toDateddmmyy = String.valueOf(iLasDay) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);

                            Salarystructure salarystructureObj = new Salarystructure();
                            salaryStructureId = getMaxSalaryStructureid(session, LoggedInRegion);
                            salarystructureObj.setId(salaryStructureId);
                            salarystructureObj.setPeriodfrom(postgresDate(fromDateddmmyy));
                            salarystructureObj.setOrderno(salaryStructureOrder);
                            salarystructureObj.setPeriodto(postgresDate(toDateddmmyy));
                            salarystructureObj.setEmployeemaster(employeemasterObje);
                            salarystructureObj.setAccregion(LoggedInRegion);
                            salarystructureObj.setCreatedby(LoggedInUser);
                            salarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(salarystructureObj);
                            transaction.commit();

                        }
//                        System.out.println("sairam 1");
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeeearningsdetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeedeductiondetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        //Earnings
//                        System.out.println("sairam 2");
                        Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                        earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureActualId + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List earDetailsList = earDetailsCrit.list();
                        resultMap.put("employeeearningslength", earDetailsList.size());
                        if (earDetailsList.size() > 0) {
                            for (int j = 0; j < earDetailsList.size(); j++) {
                                Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);

                                Criteria earDe = session.createCriteria(Employeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + employeeearningsdetailsactualObj.getEarningmasterid() + "' "));
                                List earList = earDe.list();
                                if (earList.size() > 0) {
                                    Employeeearningsdetails earningsdetObj = (Employeeearningsdetails) earList.get(0);
                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());

                                    transaction = session.beginTransaction();
                                    session.update(earningsdetObj);
                                    transaction.commit();

                                } else {

                                    Employeeearningsdetails earningsdetObj = new Employeeearningsdetails();

                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());
                                    earningsdetObj.setEarningmasterid(employeeearningsdetailsactualObj.getEarningmasterid());
                                    earningsdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    earningsdetObj.setId(getMaxSalaryEmployeeEarningDetailsid(session, LoggedInRegion));
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

                                Criteria dedDe = session.createCriteria(Employeedeductiondetails.class);
                                dedDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                dedDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                dedDe.add(Restrictions.sqlRestriction("deductionmasterid = '" + employeedeductiondetailsObj.getDeductionmasterid() + "' "));
                                List dedList = dedDe.list();
                                if (dedList.size() > 0) {
                                    Employeedeductiondetails deducdetObj = (Employeedeductiondetails) dedList.get(0);
                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    transaction = session.beginTransaction();
                                    session.update(deducdetObj);
                                    transaction.commit();

                                } else {

                                    Employeedeductiondetails deducdetObj = new Employeedeductiondetails();

                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    deducdetObj.setDeductionmasterid(employeedeductiondetailsObj.getDeductionmasterid());
                                    deducdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    deducdetObj.setId(getMaxSalaryEmployeeDeductionsDetailsid(session, LoggedInRegion));
                                    deducdetObj.setAccregion(LoggedInRegion);
                                    transaction = session.beginTransaction();
                                    session.save(deducdetObj);
                                    transaction.commit();

                                }


                            }
                        }




                    }
                    // copy data from salary structure actual to salary structure end
//                    System.out.println("copy data from salary structure actual to salary structure end");
                    Criteria empBillProcessCrit = session.createCriteria(Payrollprocessingdetails.class);
                    empBillProcessCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                    List empBillProcessList = empBillProcessCrit.list();
                    if (empBillProcessList.size() > 0) {
                        payrollprocessingdetailsObj = (Payrollprocessingdetails) empBillProcessList.get(0);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        payrollprocessingdetailsObj.setDesignation(employeemasterObje.getDesignation());
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                        //payrollprocessingdetailsObj.setWorkedday(workedDay);
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        transaction = session.beginTransaction();
                        session.update(payrollprocessingdetailsObj);
                        transaction.commit();
                    } else {
                        payrollprocessingdetailsObj = new Payrollprocessingdetails();
                        String id = getMaxSeqNumberBillProcess(session, LoggedInRegion);
                        payrollprocessingdetailsObj.setId(id);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setEmployeecategory(employeemasterObje.getCategory());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setDesignation(employeemasterObje.getDesignation());
                        payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
//                        payrollprocessingdetailsObj.setWorkedday(workedDay);
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";

//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        session.save(payrollprocessingdetailsObj);
                        transaction = session.beginTransaction();
                        transaction.commit();

                    }

                    // cancel when reprocees
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeeearningstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeedeductionstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();

                    // Earning
                    Criteria empEarCrit = session.createCriteria(Employeeearningsdetails.class);
                    empEarCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empEarCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                    empEarCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List empEarList = empEarCrit.list();
                    if (empEarList.size() > 0) {
                        for (int i = 0; i < empEarList.size(); i++) {
                            employeeearningsdetailsObje = (Employeeearningsdetails) empEarList.get(i);

                            Criteria empEarTranCrit = session.createCriteria(Employeeearningstransactions.class);
                            empEarTranCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empEarTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empEarTranCrit.add(Restrictions.sqlRestriction("earningmasterid='" + employeeearningsdetailsObje.getEarningmasterid() + "'"));
                            List empEarTranList = empEarTranCrit.list();
                            if (empEarTranList.size() > 0) {

                                employeeearningstransactionsObj = (Employeeearningstransactions) empEarTranList.get(0);
                                employeeearningstransactionsObj.setEarningmasterid(employeeearningsdetailsObje.getEarningmasterid());
                                employeeearningstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                if (employeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, employeeearningsdetailsObje.getAmount()));
                                } else {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, getEarningsAmount(session, salaryStructureId, employeeearningsdetailsObje.getEarningmasterid(), processDate)));
                                }
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.update(employeeearningstransactionsObj);
                            } else {
                                String earningsTransactionId = getMaxEmployeeearningstransactionsid(session, LoggedInRegion);
                                employeeearningstransactionsObj = new Employeeearningstransactions();
                                employeeearningstransactionsObj.setId(earningsTransactionId);
                                employeeearningstransactionsObj.setEarningmasterid(employeeearningsdetailsObje.getEarningmasterid());
                                employeeearningstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                if (employeeearningsdetailsObje.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, employeeearningsdetailsObje.getAmount()));
                                } else {
                                    employeeearningstransactionsObj.setAmount(deductLLP(session, employeemasterObje.getEpfno(), processDate, getEarningsAmount(session, salaryStructureId, employeeearningsdetailsObje.getEarningmasterid(), processDate)));
                                }
                                employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                                employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                                employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                                employeeearningstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeeearningstransactionsObj);
                            }
                            transaction.commit();
                        }

                    }

                    // Regular Deductions
                    Criteria empDeduCrit = session.createCriteria(Employeedeductiondetails.class);
                    empDeduCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empDeduCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                    empDeduCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List empDeduList = empDeduCrit.list();
                    if (empDeduList.size() > 0) {
                        for (int j = 0; j < empDeduList.size(); j++) {
                            employeedeductiondetailsObje = (Employeedeductiondetails) empDeduList.get(j);
                            Criteria empDeducTranCrit = session.createCriteria(Employeedeductionstransactions.class);
                            empDeducTranCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empDeducTranCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empDeducTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + employeedeductiondetailsObje.getDeductionmasterid() + "'"));
                            List empDeducTranList = empDeducTranCrit.list();
                            if (empDeducTranList.size() > 0) {
                                BigDecimal amt = new BigDecimal("0");
                                employeedeductionstransactionsObj = (Employeedeductionstransactions) empDeducTranList.get(0);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeedeductiondetailsObje.getDeductionmasterid());
                                if (employeedeductiondetailsObje.getAmount().equals(new BigDecimal("0.00"))) {
//                                    System.out.println("equals zero sairam");
                                    amt = getDeductionAmount(session, salaryStructureId, employeedeductiondetailsObje.getDeductionmasterid(), processDate);
                                } else {
                                    amt = employeedeductiondetailsObje.getAmount();
                                }
                                employeedeductionstransactionsObj.setAmount(amt);

//                            if (employeedeductiondetailsObje.getDeductionmasterid().equalsIgnoreCase("D02")) {                                
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount().multiply(new BigDecimal("0.12")));
//                            } else {
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount());
//                            }
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                transaction = session.beginTransaction();
                                session.update(employeedeductionstransactionsObj);
                            } else {
                                String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                                employeedeductionstransactionsObj = new Employeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeedeductiondetailsObje.getDeductionmasterid());
//                            if (employeedeductiondetailsObje.getDeductionmasterid().equalsIgnoreCase("D02")) {
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount().multiply(new BigDecimal("0.12")));
//                            } else {
//                                employeedeductionstransactionsObj.setAmount(employeedeductiondetailsObje.getAmount());
//                            }
                                BigDecimal amt = new BigDecimal("0");
                                if (employeedeductiondetailsObje.getAmount().equals(new BigDecimal("0.00"))) {
//                                    System.out.println("equals zero sairam");
                                    amt = getDeductionAmount(session, salaryStructureId, employeedeductiondetailsObje.getDeductionmasterid(), processDate);
                                } else {
                                    amt = employeedeductiondetailsObje.getAmount();
                                }
                                employeedeductionstransactionsObj.setAmount(amt);
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                            }
                            transaction.commit();
                        }

                    }

                    // Miscelleneous Deductions
                    Miscdeductions miscdeductionsObje;
                    Criteria misDeducCrit = session.createCriteria(Miscdeductions.class);
                    misDeducCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeemasterObje.getEpfno() + "' "));
                    misDeducCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    misDeducCrit.add(Restrictions.sqlRestriction("year = " + iYear));
                    misDeducCrit.add(Restrictions.sqlRestriction("month = " + iMonth));
                    List misDeducList = misDeducCrit.list();
                    if (misDeducList.size() > 0) {
                        for (int j = 0; j < misDeducList.size(); j++) {
                            miscdeductionsObje = (Miscdeductions) misDeducList.get(j);

                            String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                            employeedeductionstransactionsObj = new Employeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid(miscdeductionsObje.getDeductionscode());
                            employeedeductionstransactionsObj.setAmount(miscdeductionsObje.getAmount());
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setType("MISC");
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(employeedeductionstransactionsObj);

                            transaction.commit();

                        }

                    }
                    // Miscelleneous Deductions Ends Here

                    // Deductions Others Starts

                    Salarydeductionothers salarydeductionothersObj;
                    Criteria otherDeducCrit = session.createCriteria(Salarydeductionothers.class);
                    otherDeducCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + employeemasterObje.getEpfno() + "' "));
                    otherDeducCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "'"));
                    otherDeducCrit.add(Restrictions.sqlRestriction("deductionmonth = " + iMonth));
                    otherDeducCrit.add(Restrictions.sqlRestriction("deductionyear = " + iYear));
                    otherDeducCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List otherDeducList = otherDeducCrit.list();
                    if (otherDeducList.size() > 0) {
                        for (int j = 0; j < otherDeducList.size(); j++) {
                            salarydeductionothersObj = (Salarydeductionothers) otherDeducList.get(j);

                            String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                            employeedeductionstransactionsObj = new Employeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid(salarydeductionothersObj.getPaycodemaster().getPaycode());
                            employeedeductionstransactionsObj.setAmount(salarydeductionothersObj.getAmountornoofdays());
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setType("OTHERSDEDUC");
                            employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
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
                    Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                    Employeeloansandadvances employeeloansandadvancesObje;
                    Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
                    List empLoanDetailsList = empLoanDetailsCrit.list();
                    if (empLoanDetailsList.size() > 0) {
                        for (int i = 0; i < empLoanDetailsList.size(); i++) {
                            transaction = session.beginTransaction();
                            employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(i);

                            employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                            employeeloansandadvancesdetailsObje.setCreatedby(LoggedInUser);
                            employeeloansandadvancesdetailsObje.setCreateddate(getCurrentDate());
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

                    //For loan deductions
                    Criteria empLoanCritUp = session.createCriteria(Employeeloansandadvances.class);
                    empLoanCritUp.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empLoanCritUp.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empLoanCritUp.add(Restrictions.sqlRestriction("loanbalance > 0"));
                    List empLoanListUp = empLoanCritUp.list();
                    if (empLoanListUp.size() > 0) {
                        for (int i = 0; i < empLoanListUp.size(); i++) {
                            employeeloansandadvancesObje = (Employeeloansandadvances) empLoanListUp.get(i);

                            Criteria empLoanDetailsCritUp = session.createCriteria(Employeeloansandadvancesdetails.class);
                            empLoanDetailsCritUp.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empLoanDetailsCritUp.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            empLoanDetailsCritUp.add(Restrictions.sqlRestriction("employeeloansandadvancesid='" + employeeloansandadvancesObje.getId() + "'"));
                            List empLoanDetailsListUp = empLoanDetailsCritUp.list();
                            if (empLoanDetailsListUp.size() > 0) {
                                Employeeloansandadvancesdetails employeeloansandadvancesdetailsOb = (Employeeloansandadvancesdetails) empLoanDetailsListUp.get(0);
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
                                String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                                employeedeductionstransactionsObj = new Employeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeeloansandadvancesObje.getDeductioncode());

                                employeedeductionstransactionsObj.setAmount(employeeloansandadvancesdetailsOb.getInstallmentamount());
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setType("LOAN");
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                                transaction.commit();
                                //Adding loan transactions in salary deductions end                
                            } else {
                                Employeeloansandadvancesdetails employeeloansandadvancesdetailsOb = new Employeeloansandadvancesdetails();
                                String id = getMaxEmployeeLoansAndAdvancesDetailsid(session, LoggedInRegion);
                                employeeloansandadvancesdetailsOb.setId(id);
                                employeeloansandadvancesdetailsOb.setAccregion(LoggedInRegion);
                                employeeloansandadvancesdetailsOb.setEmployeeloansandadvances(employeeloansandadvancesObje);
                                employeeloansandadvancesdetailsOb.setPayrollprocessingdetails(payrollprocessingdetailsObj);
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
                                String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                                employeedeductionstransactionsObj = new Employeedeductionstransactions();
                                employeedeductionstransactionsObj.setId(deductionsTransactionId);
                                employeedeductionstransactionsObj.setDeductionmasterid(employeeloansandadvancesObje.getDeductioncode());

                                employeedeductionstransactionsObj.setAmount(employeeloansandadvancesdetailsOb.getInstallmentamount());

                                employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                                employeedeductionstransactionsObj.setType("LOAN");
                                employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                                employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                                employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                                transaction = session.beginTransaction();
                                session.save(employeedeductionstransactionsObj);
                                transaction.commit();





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
                    // Recovery Deductions
                    // Recovery Reprocess
                    Employeerecoverydetails employeerecoverydetailsObje;
                    Employeerecoveries employeerecoveriesObje;
                    Criteria empRecovDetailsCrit = session.createCriteria(Employeerecoverydetails.class);
                    empRecovDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                    empRecovDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
                    List empRecovDetailsList = empRecovDetailsCrit.list();
                    if (empRecovDetailsList.size() > 0) {
                        for (int i = 0; i < empRecovDetailsList.size(); i++) {
                            transaction = session.beginTransaction();
                            employeerecoverydetailsObje = (Employeerecoverydetails) empRecovDetailsList.get(i);

                            employeerecoverydetailsObje.setCancelled(Boolean.TRUE);
                            employeerecoverydetailsObje.setCreatedby(LoggedInUser);
                            employeerecoverydetailsObje.setCreateddate(getCurrentDate());
                            session.update(employeerecoverydetailsObje);

                            Criteria empRecCrit = session.createCriteria(Employeerecoveries.class);
                            empRecCrit.add(Restrictions.sqlRestriction("id='" + employeerecoverydetailsObje.getEmployeerecoveries().getId() + "'"));
                            List empRecList = empRecCrit.list();
                            if (empRecList.size() > 0) {
                                employeerecoveriesObje = (Employeerecoveries) empRecList.get(0);
                                employeerecoveriesObje.setCurrentinstallment(employeerecoveriesObje.getCurrentinstallment() - 1);
                                if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                    employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().add(employeerecoveriesObje.getFirstinstallmentamount()));
                                } else {
                                    employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().add(employeerecoveriesObje.getInstallmentamount()));
                                }
                                session.update(employeerecoveriesObje);
                            }
                            transaction.commit();
                        }
                    }
                    //For Recovery deductions
                    Criteria empRecovCritUp = session.createCriteria(Employeerecoveries.class);
                    empRecovCritUp.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empRecovCritUp.add(Restrictions.sqlRestriction("totalinstallment > currentinstallment"));
                    List empRecovListUp = empRecovCritUp.list();
                    if (empRecovListUp.size() > 0) {
                        for (int i = 0; i < empRecovListUp.size(); i++) {
                            employeerecoveriesObje = (Employeerecoveries) empRecovListUp.get(i);
                            employeerecoveriesObje.setCurrentinstallment(employeerecoveriesObje.getCurrentinstallment() + 1);
                            if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().subtract(employeerecoveriesObje.getFirstinstallmentamount()));
                            } else {
                                employeerecoveriesObje.setLoanbalance(employeerecoveriesObje.getLoanbalance().subtract(employeerecoveriesObje.getInstallmentamount()));
                            }
                            Criteria empRecovDetailsCritUp = session.createCriteria(Employeerecoverydetails.class);
                            empRecovDetailsCritUp.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                            empRecovDetailsCritUp.add(Restrictions.sqlRestriction("employeerecoveriesid='" + employeerecoveriesObje.getId() + "'"));
                            List empRecovDetailsListUp = empRecovDetailsCritUp.list();
                            if (empRecovDetailsListUp.size() > 0) {
                                Employeerecoverydetails employeerecoverydetailsOb = (Employeerecoverydetails) empRecovDetailsListUp.get(0);
                                employeerecoverydetailsOb.setCancelled(Boolean.FALSE);
                                employeerecoverydetailsOb.setCreatedby(LoggedInUser);
                                employeerecoverydetailsOb.setCreateddate(getCurrentDate());
                                employeerecoverydetailsOb.setNthinstallment(employeerecoveriesObje.getCurrentinstallment());
                                employeerecoverydetailsOb.setLoanbalance(employeerecoveriesObje.getLoanbalance());
                                if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getInstallmentamount());
                                } else {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getFirstinstallmentamount());
                                }
                                transaction = session.beginTransaction();
                                session.update(employeerecoverydetailsOb);
                                transaction.commit();
                            } else {
                                Employeerecoverydetails employeerecoverydetailsOb = new Employeerecoverydetails();
                                String id = getMaxEmployeeRecoveriesDetailsid(session, LoggedInRegion);
                                employeerecoverydetailsOb.setId(id);
                                employeerecoverydetailsOb.setEmployeerecoveries(employeerecoveriesObje);
                                employeerecoverydetailsOb.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                                employeerecoverydetailsOb.setCancelled(Boolean.FALSE);
                                employeerecoverydetailsOb.setCreatedby(LoggedInUser);
                                employeerecoverydetailsOb.setCreateddate(getCurrentDate());
                                employeerecoverydetailsOb.setNthinstallment(employeerecoveriesObje.getCurrentinstallment());
                                employeerecoverydetailsOb.setLoanbalance(employeerecoveriesObje.getLoanbalance());
                                if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getInstallmentamount());
                                } else {
                                    employeerecoverydetailsOb.setInstallmentamount(employeerecoveriesObje.getFirstinstallmentamount());
                                }
                                employeerecoverydetailsOb.setAccregion(LoggedInRegion);
                                transaction = session.beginTransaction();
                                session.save(employeerecoverydetailsOb);
                                transaction.commit();
                            }
                            transaction = session.beginTransaction();
                            session.update(employeerecoveriesObje);
                            transaction.commit();


                            // Adding recovery transactions in salary deductions
                            transaction = session.beginTransaction();
                            String deductionsTransactionId = getMaxEmployeedeductionstransactionsid(session, LoggedInRegion);
                            employeedeductionstransactionsObj = new Employeedeductionstransactions();
                            employeedeductionstransactionsObj.setId(deductionsTransactionId);
                            employeedeductionstransactionsObj.setDeductionmasterid(employeerecoveriesObje.getDeductioncode());
                            if (employeerecoveriesObje.getCurrentinstallment() == 1) {
                                employeedeductionstransactionsObj.setAmount(employeerecoveriesObje.getFirstinstallmentamount());
                            } else {
                                employeedeductionstransactionsObj.setAmount(employeerecoveriesObje.getInstallmentamount());
                            }
                            employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                            employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                            employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                            employeedeductionstransactionsObj.setType("RECOVERY");
                            employeedeductionstransactionsObj.setPayrollprocessingdetails(payrollprocessingdetailsObj);
                            transaction = session.beginTransaction();
                            session.save(employeedeductionstransactionsObj);
                            transaction.commit();
                            //  Adding recovery transactions in salary deductions end  
                        }
                    }


                    //Confirm transaction save status

                    payrollprocessingdetailsObj = null;
                } else {

                    // Pay Roll Not Processing 
                    String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                    String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
//                    String queryStr = "select id from salarystructureactual where employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
//                            + " periodfrom<='" + fromDate + "' and "
//                            + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
                    String queryStr = "select id from salarystructureactual where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and periodto is null ";
//                    System.out.println(queryStr);

                    // copy data from salary structure actual to salary structure start
                    List salaryStruActualList = (ArrayList) session.createSQLQuery(queryStr).list();
                    if (salaryStruActualList.size() > 0) {
                        String salaryStructureActualId = (String) salaryStruActualList.get(0);

                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom='" + fromDate + "'";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            salaryStructureId = (String) salaryStruList.get(0);
                            salaryStructureOrder = "";
                        } else {
                            String fromDateddmmyy = String.valueOf(1) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);
                            String toDateddmmyy = String.valueOf(iLasDay) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iYear);

                            Salarystructure salarystructureObj = new Salarystructure();
                            salaryStructureId = getMaxSalaryStructureid(session, LoggedInRegion);
                            salarystructureObj.setId(salaryStructureId);
                            salarystructureObj.setPeriodfrom(postgresDate(fromDateddmmyy));
                            salarystructureObj.setOrderno(salaryStructureOrder);
                            salarystructureObj.setPeriodto(postgresDate(toDateddmmyy));
                            salarystructureObj.setEmployeemaster(employeemasterObje);
                            salarystructureObj.setAccregion(LoggedInRegion);
                            salarystructureObj.setCreatedby(LoggedInUser);
                            salarystructureObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.save(salarystructureObj);
                            transaction.commit();

                        }
//                        System.out.println("sairam 1");
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeeearningsdetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeedeductiondetails  SET cancelled  = true WHERE salarystructureid='" + salaryStructureId + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        //Earnings
//                        System.out.println("sairam 2");
                        Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
                        earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureActualId + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List earDetailsList = earDetailsCrit.list();
                        resultMap.put("employeeearningslength", earDetailsList.size());
                        if (earDetailsList.size() > 0) {
                            for (int j = 0; j < earDetailsList.size(); j++) {
                                Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);

                                Criteria earDe = session.createCriteria(Employeeearningsdetails.class);
                                earDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + employeeearningsdetailsactualObj.getEarningmasterid() + "' "));
                                List earList = earDe.list();
                                if (earList.size() > 0) {
                                    Employeeearningsdetails earningsdetObj = (Employeeearningsdetails) earList.get(0);
                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());

                                    transaction = session.beginTransaction();
                                    session.update(earningsdetObj);
                                    transaction.commit();

                                } else {

                                    Employeeearningsdetails earningsdetObj = new Employeeearningsdetails();

                                    earningsdetObj.setCancelled(Boolean.FALSE);
                                    earningsdetObj.setAmount(employeeearningsdetailsactualObj.getAmount());
                                    earningsdetObj.setEarningmasterid(employeeearningsdetailsactualObj.getEarningmasterid());
                                    earningsdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    earningsdetObj.setId(getMaxSalaryEmployeeEarningDetailsid(session, LoggedInRegion));
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

                                Criteria dedDe = session.createCriteria(Employeedeductiondetails.class);
                                dedDe.add(Restrictions.sqlRestriction("salarystructureid = '" + salaryStructureId + "' "));
                                dedDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                                dedDe.add(Restrictions.sqlRestriction("deductionmasterid = '" + employeedeductiondetailsObj.getDeductionmasterid() + "' "));
                                List dedList = dedDe.list();
                                if (dedList.size() > 0) {
                                    Employeedeductiondetails deducdetObj = (Employeedeductiondetails) dedList.get(0);
                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    transaction = session.beginTransaction();
                                    session.update(deducdetObj);
                                    transaction.commit();

                                } else {

                                    Employeedeductiondetails deducdetObj = new Employeedeductiondetails();

                                    deducdetObj.setCancelled(Boolean.FALSE);
                                    deducdetObj.setAmount(employeedeductiondetailsObj.getAmount());
                                    deducdetObj.setDeductionmasterid(employeedeductiondetailsObj.getDeductionmasterid());
                                    deducdetObj.setSalarystructure(getSalarystructure(session, salaryStructureId));
                                    deducdetObj.setId(getMaxSalaryEmployeeDeductionsDetailsid(session, LoggedInRegion));
                                    deducdetObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                                    deducdetObj.setAccregion(LoggedInRegion);
                                    transaction = session.beginTransaction();
                                    session.save(deducdetObj);
                                    transaction.commit();

                                }


                            }
                        }




                    }
                    // copy data from salary structure actual to salary structure end                 

                    String stoppay = "select reasoncode from stoppayrolldetails where accregion='" + LoggedInRegion + "' and epfno ='" + employeemasterObje.getEpfno() + "' and enddate is null";
//                    String stoppay = "select reasoncode from stoppayrolldetails where accregion='" + LoggedInRegion + "' and epfno ='" + employeemasterObje.getEpfno() + "' and startdate<='" + postgresDate(processDate) + "' and  "
//                            + " case when enddate is null then true else case when enddate>='" + postgresDate(processDate) + "' then true else false end end  ";
                    String stoppaymentreasoncode = "";
                    List stopPayList = (ArrayList) session.createSQLQuery(stoppay).list();
                    if (stopPayList.size() > 0) {
                        if (stopPayList.get(0) != null) {
                            stoppaymentreasoncode = (String) stopPayList.get(0);

                        }
                    }

                    Criteria empBillProcessCrit = session.createCriteria(Payrollprocessingdetails.class);
                    empBillProcessCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                    empBillProcessCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                    List empBillProcessList = empBillProcessCrit.list();
                    if (empBillProcessList.size() > 0) {
                        payrollprocessingdetailsObj = (Payrollprocessingdetails) empBillProcessList.get(0);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                        payrollprocessingdetailsObj.setSalarydays(0);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        transaction = session.beginTransaction();
                        session.update(payrollprocessingdetailsObj);
                        transaction.commit();
                    } else {
                        payrollprocessingdetailsObj = new Payrollprocessingdetails();
                        String id = getMaxSeqNumberBillProcess(session, LoggedInRegion);
                        payrollprocessingdetailsObj.setId(id);
                        payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                        payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                        payrollprocessingdetailsObj.setMonth(iMonth);
                        payrollprocessingdetailsObj.setYear(iYear);
                        payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                        payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                        payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                        payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                        int workingDay = iLasDay;
                        payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                        payrollprocessingdetailsObj.setSalarydays(0);
                        payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                        payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                        payrollprocessingdetailsObj.setProcess(employeemasterObje.isProcess());
                        fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        queryStr = "select id from salarystructure where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and "
                                + " periodfrom<='" + fromDate + "' and "
                                + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";

//                        System.out.println(queryStr);
                        List salaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
                        if (salaryStruList.size() > 0) {
                            if (salaryStruList.get(0) != null) {
                                salaryStructureId = (String) salaryStruList.get(0);
                                payrollprocessingdetailsObj.setSalarystructureid(salaryStructureId);
                            }
                        }
                        payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                        session.save(payrollprocessingdetailsObj);
                        transaction = session.beginTransaction();
                        transaction.commit();

                    }

                    // cancel when reprocees
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeeearningstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();
                    transaction = session.beginTransaction();
                    session.createSQLQuery("UPDATE employeedeductionstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                    transaction.commit();

                    // Loans and Advances Deductions
                    //For reprocessing 
                    Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                    Employeeloansandadvances employeeloansandadvancesObje;
                    Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empLoanDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                    List empLoanDetailsList = empLoanDetailsCrit.list();
                    if (empLoanDetailsList.size() > 0) {
                        for (int i = 0; i < empLoanDetailsList.size(); i++) {
                            transaction = session.beginTransaction();
                            employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(i);

                            employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                            employeeloansandadvancesdetailsObje.setCreatedby(LoggedInUser);
                            employeeloansandadvancesdetailsObje.setCreateddate(getCurrentDate());
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
                    // Pay Roll Not Processing
                }


            } else {
                resultMap.put("proceed", "no");
                // Reversing processed then transferred
                String empsql = "select employeeprovidentfundnumber from payrollprocessingdetails where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ";
                empsql = empsql + "not in (select epfno from employeemaster where region='" + LoggedInRegion + "') and month=" + iMonth + " and year=" + iYear + " and accregion='" + LoggedInRegion + "'";

                SQLQuery empquery = session.createSQLQuery(empsql);
                for (ListIterator its = empquery.list().listIterator(); its.hasNext();) {
                    String transepfno = (String) its.next();
//                    System.out.println("Reversing processed then transferred" + transepfno);
                    empCrit = session.createCriteria(Employeemaster.class);
                    empCrit.add(Restrictions.sqlRestriction("epfno='" + transepfno + "'"));
                    empList = empCrit.list();
                    if (empList.size() > 0) {
                        employeemasterObje = (Employeemaster) empList.get(0);
                        // Pay Roll Not Processing 
                        String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
                        String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
                        String queryStr = "select id from salarystructureactual where accregion='" + LoggedInRegion + "' and employeeprovidentfundnumber ='" + employeemasterObje.getEpfno() + "' and periodto is null ";
                        String stoppay = "select reasoncode from stoppayrolldetails where accregion='" + LoggedInRegion + "' and epfno ='" + transepfno + "' and enddate is null";

                        String stoppaymentreasoncode = "";
                        List stopPayList = (ArrayList) session.createSQLQuery(stoppay).list();
                        if (stopPayList.size() > 0) {
                            if (stopPayList.get(0) != null) {
                                stoppaymentreasoncode = (String) stopPayList.get(0);
                            }
                        }

                        Criteria empBillProcessCrit = session.createCriteria(Payrollprocessingdetails.class);
                        empBillProcessCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + transepfno + "'"));
                        empBillProcessCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                        empBillProcessCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                        empBillProcessCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                        List empBillProcessList = empBillProcessCrit.list();
                        if (empBillProcessList.size() > 0) {
                            payrollprocessingdetailsObj = (Payrollprocessingdetails) empBillProcessList.get(0);
                            payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                            payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                            payrollprocessingdetailsObj.setMonth(iMonth);
                            payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                            payrollprocessingdetailsObj.setYear(iYear);
                            payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                            payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                            payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                            int workingDay = iLasDay;
                            payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                            payrollprocessingdetailsObj.setSalarydays(0);
                            payrollprocessingdetailsObj.setProcess(false);
                            payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(payrollprocessingdetailsObj);
                            transaction.commit();
                        } else {
                            payrollprocessingdetailsObj = new Payrollprocessingdetails();
                            String id = getMaxSeqNumberBillProcess(session, LoggedInRegion);
                            payrollprocessingdetailsObj.setId(id);
                            payrollprocessingdetailsObj.setEmployeemaster(employeemasterObje);
                            payrollprocessingdetailsObj.setPayrollprocessingid(payrollprocessingObje.getId());
                            payrollprocessingdetailsObj.setMonth(iMonth);
                            payrollprocessingdetailsObj.setYear(iYear);
                            payrollprocessingdetailsObj.setRemarks(stoppaymentreasoncode);
                            payrollprocessingdetailsObj.setSection(employeemasterObje.getSection());
                            payrollprocessingdetailsObj.setSubsection(employeemasterObje.getSubsection());
                            payrollprocessingdetailsObj.setPaymentmode(employeemasterObje.getPaymentmode());
                            int workingDay = iLasDay;
                            payrollprocessingdetailsObj.setWorkingday((short) workingDay);
                            payrollprocessingdetailsObj.setSalarydays(0);
                            payrollprocessingdetailsObj.setProcess(false);//                           
                            payrollprocessingdetailsObj.setAccregion(LoggedInRegion);
                            payrollprocessingdetailsObj.setCreatedby(LoggedInUser);
                            payrollprocessingdetailsObj.setCreateddate(getCurrentDate());
                            session.save(payrollprocessingdetailsObj);
                            transaction = session.beginTransaction();
                            transaction.commit();

                        }

                        // cancel when reprocees
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeeearningstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();
                        transaction = session.beginTransaction();
                        session.createSQLQuery("UPDATE employeedeductionstransactions  SET cancelled  = true WHERE payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "' and accregion ='" + LoggedInRegion + "'").executeUpdate();
                        transaction.commit();

                        // Loans and Advances Deductions
                        //For reprocessing 
                        Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
                        Employeeloansandadvances employeeloansandadvancesObje;
                        Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                        empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                        empLoanDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                        empLoanDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                        List empLoanDetailsList = empLoanDetailsCrit.list();
                        if (empLoanDetailsList.size() > 0) {
                            for (int i = 0; i < empLoanDetailsList.size(); i++) {
                                transaction = session.beginTransaction();
                                employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(i);

                                employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                                employeeloansandadvancesdetailsObje.setCreatedby(LoggedInUser);
                                employeeloansandadvancesdetailsObje.setCreateddate(getCurrentDate());
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
                        // Pay Roll Not Processing

                        //Reverse and Transffered
                    }
                }


                resultMap.put("reason", "Processed Successfully");
            }

        } else {
            resultMap.put("proceed", "no");
        }
        resultMap.put("msg", "Sairam");
        return resultMap;
    }

    public synchronized String getMaxSeqNumberBillProcess(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getPayrollprocessingdetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPayrollprocessingdetailsid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        //System.out.println("bill processing serial no" + maxStr);
        return maxStr;
    }

    public synchronized String getMaxEmployeeearningstransactionsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeearningstransactionsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeearningstransactionsid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        //System.out.println("bill processing serial no" + maxStr);
        return maxStr;
    }

    public synchronized String getMaxEmployeedeductionstransactionsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeedeductionstransactionsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeedeductionstransactionsid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        //System.out.println("bill processing serial no" + maxStr);
        return maxStr;
    }

    public synchronized String getMaxPayBillProcessingid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getPayrollprocessingid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPayrollprocessingid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        //System.out.println("bill processing serial no" + maxStr);
        return maxStr;
    }

    public synchronized String getMaxSalaryStructureid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSalarystructureid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSalarystructureid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        //System.out.println("bill processing serial no" + maxStr);
        return maxStr;
    }

    public Salarystructure getSalarystructure(Session session, String id) {
        Salarystructure salarystructureObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Salarystructure.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + id + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                salarystructureObj = (Salarystructure) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }


        return salarystructureObj;
    }

    public synchronized String getMaxSalaryEmployeeEarningDetailsid(Session session, String regionCode) {
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

    public synchronized String getMaxSalaryEmployeeDeductionsDetailsid(Session session, String regionCode) {
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

    public synchronized String getMaxEmployeeLoansAndAdvancesDetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeloansandadvancesdetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeloansandadvancesdetailsid(maxNoStr);
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

    public synchronized String getMaxEmployeeRecoveriesDetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeerecoverydetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeerecoverydetailsid(maxNoStr);
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

    public BigDecimal getEarningsAmount(Session session, String salaryStructureId, String earningMasterid, String processDate) {
        //System.out.println("************************ EmployeePayBillProcessImpl class getEarningsAmount method is calling **************************");
        //System.out.println("**************************************************************************************************************************");
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
//                System.out.println("salary Structure id " + salaryStructureId);
//                System.out.println("salary Structure code " + ccahraObj.getPaycodemaster().getPaycode());
                Criteria earCrit = session.createCriteria(Employeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Employeeearningsdetails) earCritList.get(0);
//                    System.out.println(employeeearningsdetailsObj.getAmount());
                    total = total + employeeearningsdetailsObj.getAmount().floatValue();
                }

            }
        }
//        System.out.println("stage 1...." + total);
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
        //int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        System.out.println("Month is " + iMonth);
        String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
        String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
        String queryStr = " periodfrom<='" + fromDate + "' and " + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
//        System.out.println("processDate ::::::::::::::: "+processDate);
        String quey = " case when periodto is null "
                + "then periodfrom <= '" + postgresDate(processDate) + "' else '" + postgresDate(processDate) + "' between "
                + "periodfrom and periodto end";

        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + earningMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction(quey));
//        System.out.println("earningslap query -------------------------------------->"+earSlapCrit.toString());
//        System.out.println("stage 2...." + earningMasterid);
        List earSlapList = earSlapCrit.list();
        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                float perc = EarningslapdetailsObj.getPercentage().floatValue() / 100;
                total = total * perc;
                float x = total;
//                System.out.println("Total Value" + x);
                BigDecimal b = new BigDecimal(x);
                b = b.setScale(2, RoundingMode.UP);
                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);

                earamt = y.floatValue();

            } else {
                earamt = EarningslapdetailsObj.getAmount().floatValue();
            }

        }

        BigDecimal earamount = new BigDecimal(earamt);
//        System.out.println("stage 3...." + earamount);
        return earamount;

    }

    public BigDecimal getEarningsAmount1(Session session, String salaryStructureId, String earningMasterid, String processDate) {
        //System.out.println("**************************************************************************************************************************");
        //BigDecimal amount = new BigDecimal(0);
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
                //System.out.println("salary Structure id " + salaryStructureId);
                //System.out.println("salary Structure code " + ccahraObj.getPaycodemaster().getPaycode());
                Criteria earCrit = session.createCriteria(Employeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Employeeearningsdetails) earCritList.get(0);
                    //System.out.println(employeeearningsdetailsObj.getAmount());
                    total = total + employeeearningsdetailsObj.getAmount().floatValue();
                }

            }
        }
        //System.out.println("stage 1...." + total);
        String Dtformat1 = "dd/MM/yyyy";
        String Datestring = processDate;
        //String salaryStructureOrder = null;
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
        //int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //System.out.println("Month is " + iMonth);
        String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
        String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
        String queryStr = " periodfrom<='" + fromDate + "' and " + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + earningMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction(queryStr));
        //System.out.println("stage 2...." + earningMasterid);
        List earSlapList = earSlapCrit.list();
        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
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
        //System.out.println("stage 3...." + earamount);
        return earamount;

    }

    public BigDecimal getDeductionAmount(Session session, String salaryStructureId, String earningMasterid, String processDate) {
        //BigDecimal amount = new BigDecimal(0);
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
                //System.out.println("salary Structure id " + salaryStructureId);
                //System.out.println("salary Structure code " + ccahraObj.getPaycodemaster().getPaycode());
                Criteria earCrit = session.createCriteria(Employeeearningsdetails.class);
                earCrit.add(Restrictions.sqlRestriction("salarystructureid='" + salaryStructureId + "'"));
                earCrit.add(Restrictions.sqlRestriction("earningmasterid ='" + ccahraObj.getPaycodemaster().getPaycode() + "'"));
                List earCritList = earCrit.list();
                if (earCritList.size() > 0) {
                    employeeearningsdetailsObj = (Employeeearningsdetails) earCritList.get(0);
                    //System.out.println(employeeearningsdetailsObj.getAmount());

                    if (employeeearningsdetailsObj.getAmount().floatValue() != 0) {
                        total = total + employeeearningsdetailsObj.getAmount().floatValue();
                    } else {
                        total = total + getEarningsAmount1(session, salaryStructureId, employeeearningsdetailsObj.getEarningmasterid(), processDate).floatValue();
                    }


                }

            }
        }
        //System.out.println("stage 1...." + total);
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
        //int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //System.out.println("Month is " + iMonth);
        String fromDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(1);
        String toDate = String.valueOf(iYear) + "-" + String.valueOf(iMonth) + "-" + String.valueOf(iLasDay);
        String queryStr = " periodfrom<='" + fromDate + "' and " + " case when periodto is null then true else case when periodto>='" + toDate + "' then true else false end end ";
        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='" + earningMasterid + "'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + total));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + total));
        //earSlapCrit.add(Restrictions.sqlRestriction(queryStr));
        //System.out.println("stage 2...." + earningMasterid);
        List earSlapList = earSlapCrit.list();
        if (earSlapList.size() > 0) {
            Earningslapdetails EarningslapdetailsObj = (Earningslapdetails) earSlapList.get(0);
            EarningslapdetailsObj.getAmount();
            if (EarningslapdetailsObj.getAmount().equals(new BigDecimal("0.00"))) {
                float perc = EarningslapdetailsObj.getPercentage().floatValue() / 100;
                //System.out.println("Deduction Total to be calculated for sum" + total);
                total = total * EarningslapdetailsObj.getPercentage().floatValue() / 100;
                float x = total;

                //System.out.println("Deduction Total Value" + x);
                BigDecimal b = new BigDecimal(x);
                b = b.setScale(2, RoundingMode.HALF_UP);
                //BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
                BigDecimal y = b.setScale(0, RoundingMode.HALF_UP);
                earamt = y.floatValue();
            }

        }

        BigDecimal earamount = new BigDecimal(earamt);
        //System.out.println("stage 3...." + earamount);
        return earamount;
    }

    public BigDecimal deductLLP(Session session, String epfno, String processDate, BigDecimal totamount) {
        float total = 0;
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
        //int iLasDay = cal.get(Calendar.DAY_OF_MONTH);
        int iLasDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //System.out.println("Days of Month " + iLasDay);
        //System.out.println("Month is " + iMonth);

        try {
            Criteria lrCrit = session.createCriteria(Employeeattendance.class);
            lrCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            lrCrit.add(Restrictions.sqlRestriction("year = " + iYear));
            lrCrit.add(Restrictions.sqlRestriction("month = " + iMonth));
            List ldList = lrCrit.list();
            total = totamount.floatValue();
            if (ldList.size() > 0) {
                Employeeattendance employeeattendanceObj = (Employeeattendance) ldList.get(0);
                int llp = employeeattendanceObj.getLlp();
                if (llp > 0) {
                    total = totamount.floatValue() - (Math.round(totamount.floatValue() / iLasDay) * llp);
                } else {
                    total = totamount.floatValue();
                }
                Criteria proCrit = session.createCriteria(Payrollprocessingdetails.class);
                proCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                proCrit.add(Restrictions.sqlRestriction("year = " + iYear));
                proCrit.add(Restrictions.sqlRestriction("month = " + iMonth));
                List proList = proCrit.list();
                if (proList.size() > 0) {
                    Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) proList.get(0);
                    short workingDay = (short) (iLasDay);
                    short workedDay = (short) (iLasDay - llp);

                    payrollprocessingdetailsObj.setWorkingday(workingDay);
                    payrollprocessingdetailsObj.setWorkedday(workedDay);
                    payrollprocessingdetailsObj.setSalarydays(iLasDay - llp);
                    Transaction transaction = session.beginTransaction();
                    session.update(payrollprocessingdetailsObj);
                    transaction.commit();
                }

            } else {
                Criteria proCrit = session.createCriteria(Payrollprocessingdetails.class);
                proCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                proCrit.add(Restrictions.sqlRestriction("year = " + iYear));
                proCrit.add(Restrictions.sqlRestriction("month = " + iMonth));
                List proList = proCrit.list();
                if (proList.size() > 0) {
                    Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) proList.get(0);
                    short workingDay = (short) (iLasDay);
                    payrollprocessingdetailsObj.setWorkingday(workingDay);
                    payrollprocessingdetailsObj.setWorkedday(workingDay);
                    payrollprocessingdetailsObj.setSalarydays((int) workingDay);
                    Transaction transaction = session.beginTransaction();
                    session.update(payrollprocessingdetailsObj);
                    transaction.commit();
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        BigDecimal amount = new BigDecimal(total);

        return amount;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        empCrit.add(Restrictions.sqlRestriction("category='" + "R" + "'"));
//        empCrit.add(Restrictions.sqlRestriction("epfno in ('" + "047066" + "','047803','049392')"));
        List empList = empCrit.list();
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                employeemasterObj = (Employeemaster) empList.get(i);
                resultMap.put(i, employeemasterObj.getEpfno());
            }

        }
        resultMap.put("length", empList.size());
        return resultMap;
    }

    private HashMap calculateAge(String birthdate, String processdate) {
        int years = 0;
        int months = 0;
        int days = 0;
        boolean flag = false;
        Date birthDate = null;
        Date processDate = null;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            birthDate = sdf.parse(birthdate);
            processDate = sdf.parse(processdate);

            //create calendar object for birth day
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTimeInMillis(birthDate.getTime());
            //create calendar object for current day
            //long currentTime = System.currentTimeMillis();
            Calendar now = Calendar.getInstance();
            //now.setTimeInMillis(currentTime);
            now.setTimeInMillis(processDate.getTime());
            //Get difference between years
            years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
            int currMonth = now.get(Calendar.MONTH) + 1;
            int birthMonth = birthDay.get(Calendar.MONTH) + 1;

            //Get difference between months
            months = currMonth - birthMonth;
            //if month difference is in negative then reduce years by one and calculate the number of months.
            if (months < 0) {
                years--;
                months = 12 - birthMonth + currMonth;
                if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                    months--;
                }
            } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                years--;
                months = 11;
            }
            //Calculate the days
            /*if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE)) {
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
            } else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
            } else {
            days = 0;
            if (months == 12) {
            years++;
            months = 0;
            }
            } */
            if (now.getActualMaximum(Calendar.DAY_OF_MONTH) > birthDay.get(Calendar.DATE)) {
                days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DATE);
            } else if (now.getActualMaximum(Calendar.DAY_OF_MONTH) < birthDay.get(Calendar.DATE)) {
                int today = now.get(Calendar.DAY_OF_MONTH);
                now.add(Calendar.MONTH, -1);
                days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
            } else {
                days = 0;
                if (months == 12) {
                    years++;
                    months = 0;
                }
            }
            int CBMonth = birthMonth - 1;
            if (currMonth == CBMonth && birthDay.get(Calendar.DATE) == 1) {
                flag = true;
            }
        } catch (Exception e) {
        }
        HashMap returnMap = new HashMap();
        returnMap.put("age", years);
        returnMap.put("month", months);
        returnMap.put("day", days);
        returnMap.put("status", flag);
        return returnMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map preparePayRoll(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String processDate, String serialno, String epfno) {
        Map resultMap = new HashMap();
        String Datestring1 = processDate;
        SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm1.parse(Datestring1);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal1 = fm1.getCalendar();
        int iMonth = cal1.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        int iYear = cal1.get(Calendar.YEAR);

        boolean isProcessOpen = false;
        Payrollprocessing payrollprocessingObje;
        Criteria proCrit1 = session.createCriteria(Payrollprocessing.class);
        proCrit1.add(Restrictions.sqlRestriction("month=" + iMonth));
        proCrit1.add(Restrictions.sqlRestriction("year=" + iYear));
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
            int serialNumber = Integer.parseInt(serialno) + 1;
            resultMap.put("serialno", serialNumber);
            int currentAge = 0;
            int currentMonth = 0;
            boolean isFirstMonthDOB = false;
            Employeemaster empObj = getEmployeemaster(session, epfno, LoggedInRegion);
            if (empObj.getDateofbirth() != null) {
                //currentAge = getCurrentAge(dateToString(empObj.getDateofbirth()));
                //isFirstMonthDOB = isFirstMonthAfterBirthDate(dateToString(empObj.getDateofbirth()));
                //HashMap getMap = isFirstMonthAfterBirthdate(dateToString(empObj.getDateofbirth()),processDate);
                HashMap getMap = calculateAge(dateToString(empObj.getDateofbirth()), processDate);
                currentAge = (Integer) getMap.get("age");
                currentMonth = (Integer) getMap.get("month");
                isFirstMonthDOB = (Boolean) getMap.get("status");
            }
            //Regular EPF Calculation
            Criteria empBillProcessCrit = session.createCriteria(Payrollprocessingdetails.class);
            empBillProcessCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
            empBillProcessCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
            empBillProcessCrit.add(Restrictions.sqlRestriction("year=" + iYear));
            empBillProcessCrit.add(Restrictions.sqlRestriction("process is true"));
            List empBillProcessList = empBillProcessCrit.list();
            if (empBillProcessList.size() > 0) {
                Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empBillProcessList.get(0);
                double epf = 0;
                double sal = 0;
                long percentage367 = 0;
                long percentage833 = 0;
                String emty = "0.00";
                epf = getEPFAmount(session, payrollprocessingdetailsObj.getId()).doubleValue();
                if (epf != 0) {
                    if (currentAge >= 58) {
                        if (((currentAge == 58 && currentMonth == 0) || isFirstMonthDOB)) {
                            sal = getSalary(session, payrollprocessingdetailsObj.getId()).doubleValue();
                            double per833 = 8.33;
                            percentage833 = Math.round((sal * per833) / 100);
                            if (percentage833 > 1250) {
                                percentage833 = 1250;
                            }
                            percentage367 = (long) (epf - percentage833);
                        } else {
                            percentage367 = (long) epf;
                            percentage833 = 0;
                        }
                    } else {
                        sal = getSalary(session, payrollprocessingdetailsObj.getId()).doubleValue();
                        double per833 = 8.33;
                        percentage833 = Math.round((sal * per833) / 100);
                        if (percentage833 > 1250) {
                            percentage833 = 1250;
                        }
                        percentage367 = (long) (epf - percentage833);
                    }
                } else {
                    percentage367 = 0;
                    percentage833 = 0;
                }

                BigDecimal epfAmount = getEPFAmount(session, payrollprocessingdetailsObj.getId());

                Criteria empEpfCrit = session.createCriteria(Employeeprovidentfundothers.class);
                empEpfCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
                empEpfCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                empEpfCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                empEpfCrit.add(Restrictions.sqlRestriction("cancelled = FALSE "));
                empEpfCrit.add(Restrictions.sqlRestriction("payrollcategory='" + "R" + "'"));
                List empEpfList = empEpfCrit.list();
                if (empEpfList.size() > 0) {
                    Employeeprovidentfundothers epfObj = (Employeeprovidentfundothers) empEpfList.get(0);
                    epfObj.setEmployeemaster(empObj);
                    epfObj.setMonth(payrollprocessingdetailsObj.getMonth());
                    epfObj.setYear(payrollprocessingdetailsObj.getYear());
                    epfObj.setEmpcategory(empObj.getCategory());
                    epfObj.setPayrollcategory("R");
                    epfObj.setSalary(getSalary(session, payrollprocessingdetailsObj.getId()));
                    epfObj.setEpfwhole(epfAmount);
                    epfObj.setFbf(StringtobigDecimal(emty));
                    epfObj.setRl(getLPFRegularAmount(session, payrollprocessingdetailsObj.getId()));
                    epfObj.setVpf(getVPFRegularAmount(session, payrollprocessingdetailsObj.getId()));
                    epfObj.setDvpf(StringtobigDecimal(emty));
                    epfObj.setNrl(StringtobigDecimal(emty));
                    epfObj.setEcfb(StringtobigDecimal(String.valueOf(percentage833)));
                    epfObj.setEcpf(StringtobigDecimal(String.valueOf(percentage367)));
                    epfObj.setSubs(epfAmount);
                    epfObj.setContributions(epfAmount);
                    epfObj.setSmonth(payrollprocessingdetailsObj.getMonth());
                    epfObj.setSyear(payrollprocessingdetailsObj.getYear());
                    epfObj.setCancelled(Boolean.FALSE);
                    epfObj.setAccregion(LoggedInRegion);
                    epfObj.setCreatedby(LoggedInUser);
                    epfObj.setCreateddate(getCurrentDate());
                    epfObj.setRegprocessid(payrollprocessingdetailsObj.getId());
                    Transaction transaction = session.beginTransaction();
                    session.update(epfObj);
                    transaction.commit();
                } else {
                    Employeeprovidentfundothers epfObj = new Employeeprovidentfundothers();
                    String id = getMaxEmployeeprovidentfundothersid(session, LoggedInRegion);
                    epfObj.setId(id);
                    epfObj.setEmployeemaster(empObj);
                    epfObj.setMonth(payrollprocessingdetailsObj.getMonth());
                    epfObj.setYear(payrollprocessingdetailsObj.getYear());
                    epfObj.setEmpcategory(empObj.getCategory());
                    epfObj.setPayrollcategory("R");
                    epfObj.setSalary(getSalary(session, payrollprocessingdetailsObj.getId()));
                    epfObj.setEpfwhole(epfAmount);
                    epfObj.setFbf(StringtobigDecimal(emty));
                    epfObj.setRl(getLPFRegularAmount(session, payrollprocessingdetailsObj.getId()));
                    epfObj.setVpf(getVPFRegularAmount(session, payrollprocessingdetailsObj.getId()));
                    epfObj.setDvpf(StringtobigDecimal(emty));
                    epfObj.setNrl(StringtobigDecimal(emty));
                    epfObj.setEcfb(StringtobigDecimal(String.valueOf(percentage833)));
                    epfObj.setEcpf(StringtobigDecimal(String.valueOf(percentage367)));
                    epfObj.setSubs(epfAmount);
                    epfObj.setContributions(epfAmount);
                    epfObj.setSmonth(payrollprocessingdetailsObj.getMonth());
                    epfObj.setSyear(payrollprocessingdetailsObj.getYear());
                    epfObj.setCancelled(Boolean.FALSE);
                    epfObj.setCreatedby(LoggedInUser);
                    epfObj.setCreateddate(getCurrentDate());
                    epfObj.setRegprocessid(payrollprocessingdetailsObj.getId());
                    epfObj.setAccregion(LoggedInRegion);

                    Transaction transaction = session.beginTransaction();
                    session.save(epfObj);
                    transaction.commit();
                }
            } else {
                Criteria empEpfCrit = session.createCriteria(Employeeprovidentfundothers.class);
                empEpfCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
                empEpfCrit.add(Restrictions.sqlRestriction("month=" + iMonth));
                empEpfCrit.add(Restrictions.sqlRestriction("year=" + iYear));
                empEpfCrit.add(Restrictions.sqlRestriction("cancelled = FALSE "));
                empEpfCrit.add(Restrictions.sqlRestriction("payrollcategory='" + "R" + "'"));
                List empEpfList = empEpfCrit.list();
                if (empEpfList.size() > 0) {
                    Employeeprovidentfundothers epfObj = (Employeeprovidentfundothers) empEpfList.get(0);

                    epfObj.setCancelled(Boolean.TRUE);
                    epfObj.setCreatedby(LoggedInUser);
                    epfObj.setCreateddate(getCurrentDate());
                    epfObj.setAccregion(LoggedInRegion);

                    Transaction transaction = session.beginTransaction();
                    session.update(epfObj);
                    transaction.commit();


                }
            }
            //Regular EPF Calculation Ends
            //Supplementary EPF Calculation Starts
            String sqr_str1 = " UPDATE employeeprovidentfundothers  SET cancelled  = true WHERE cancelled =false and supprocessid in ( "
                    + " select sppd.id from supplementarypayrollprocessingdetails sppd "
                    + " left join supplementatypaybill spb on sppd.supplementatypaybillid=spb.id "
                    + " where extract(month from spb.date)= " + iMonth + " and extract(year from spb.date)=" + iYear + " "
                    + " and spb.employeeprovidentfundnumber='" + epfno + "' and (spb.type='SUPLEMENTARYBILL' or spb.type='INCREMENTARREAR' or spb.type='DAARREAR' ) ) ";
            Transaction updatetransaction = session.beginTransaction();
            session.createSQLQuery(sqr_str1).executeUpdate();
            updatetransaction.commit();

            String sqr_str = "select * from supplementarypayrollprocessingdetails sppd "
                    + " left join supplementatypaybill spb on sppd.supplementatypaybillid=spb.id "
                    + " where extract(month from spb.date)= " + iMonth + " and extract(year from spb.date)=" + iYear + " and spb.employeeprovidentfundnumber='" + epfno + "' and sppd.cancelled is false and (spb.type='SUPLEMENTARYBILL' or spb.type='INCREMENTARREAR' or spb.type='DAARREAR'  )";
            SQLQuery suppayquery = session.createSQLQuery(sqr_str);
            for (ListIterator it = suppayquery.list().listIterator(); it.hasNext();) {
                Object[] row = (Object[]) it.next();
                if (row[0] != null) {
                    String suppayprocessid = (String) row[0];

                    Criteria empSupBillProcessCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                    empSupBillProcessCrit.add(Restrictions.sqlRestriction("id='" + suppayprocessid + "'"));

                    List empSupEpfList = empSupBillProcessCrit.list();
                    if (empSupEpfList.size() > 0) {
                        Supplementarypayrollprocessingdetails epfSupObj = (Supplementarypayrollprocessingdetails) empSupEpfList.get(0);
                        String emty = "0.00";
                        long percentage367 = 0;
                        long percentage833 = 0;
                        long ecfbAmount = 0;
                        double epf = 0;
                        double sal = 0;
                        sal = getSupplementarySalarySalary(session, suppayprocessid).doubleValue();
                        epf = getSupEPFAmount(session, suppayprocessid).doubleValue();
                        double per833 = 8.33;
                        double tempper833 = 0;
                        if (epf != 0) {
                            ecfbAmount = getECFBAccounted(session, epfno, String.valueOf(epfSupObj.getCalculatedmonth()), String.valueOf(epfSupObj.getCalculatedyear()));
                            if (currentAge >= 58) {
                                if (currentAge == 58 && isFirstMonthDOB) {
                                    percentage833 = Math.round((sal * per833) / 100);
                                    if (percentage833 > 1250) {
                                        percentage833 = 1250;
                                    }
                                    percentage367 = (long) (epf - percentage833);
                                } else {
                                    percentage367 = (long) epf;
                                    percentage833 = 0;
                                }
                                if (ecfbAmount >= 1250) {
                                    percentage833 = 0;
                                    percentage367 = (long) epf;
                                    percentage367 = percentage367 + percentage833;
                                } else {
                                    percentage833 = percentage833 - ecfbAmount;
                                    percentage367 = percentage367 + ecfbAmount;
                                }
                            } else {
                                percentage833 = Math.round((sal * per833) / 100);
                                if (percentage833 > 1250) {
                                    percentage833 = 1250;
                                }
                                percentage367 = (long) (epf - percentage833);
                                if (ecfbAmount >= 1250) {
                                    percentage833 = 0;
                                    percentage367 = (long) epf;
                                    percentage367 = percentage367 + percentage833;
                                } else {
                                    tempper833 = 1250 - ecfbAmount;
                                    if (percentage833 >= tempper833) {
                                        //percentage367 = Math.abs(percentage833 - (long) tempper833);
                                        percentage367 = Math.abs((long) epf - (long) tempper833);
                                        percentage833 = (long) tempper833;
                                    } else {
                                        percentage833 = percentage367 + percentage833;
                                        percentage367 = 0;
                                        //percentage833 = (long) tempper833;

                                    }
                                    //percentage367 = Math.abs(percentage833 - (long) tempper833);
                                    //percentage833 = (long) tempper833;
                                }
                            }
                        } else {
                            percentage367 = 0;
                            percentage833 = 0;
                        }
                        //System.out.println("epfno:: "+ epfno + " ::currentAge:: "+currentAge +" ::year:: "+ epfSupObj.getCalculatedyear()+" ::month:: "+epfSupObj.getCalculatedmonth() + " ::sal:: "+ sal +" ::epf::"+epf +" ::ecfbAmount:: "+ ecfbAmount +" ::(sal*per833)/100:: " + Math.round((sal * per833) / 100) +" ::percentage367:: " +percentage367+" ::percentage833:: " +percentage833 );
                        BigDecimal supEPFAmount = getSupEPFAmount(session, suppayprocessid);

                        Criteria empEpfCrit = session.createCriteria(Employeeprovidentfundothers.class);
                        empEpfCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
                        empEpfCrit.add(Restrictions.sqlRestriction("smonth=" + epfSupObj.getCalculatedmonth()));
                        empEpfCrit.add(Restrictions.sqlRestriction("syear=" + epfSupObj.getCalculatedyear()));
                        empEpfCrit.add(Restrictions.sqlRestriction("cancelled = FALSE "));
                        empEpfCrit.add(Restrictions.sqlRestriction("payrollcategory='" + "S" + "'"));
                        List empEpfList = empEpfCrit.list();
                        if (empEpfList.size() > 0) {
                            Employeeprovidentfundothers epfObj = (Employeeprovidentfundothers) empEpfList.get(0);
                            epfObj.setEmployeemaster(empObj);
                            epfObj.setMonth(iMonth);
                            epfObj.setYear(iYear);
                            epfObj.setEmpcategory(empObj.getCategory());
                            epfObj.setPayrollcategory("S");
                            epfObj.setSalary(getSupplementarySalarySalary(session, suppayprocessid));
                            epfObj.setEpfwhole(new BigDecimal(epf));
                            epfObj.setFbf(StringtobigDecimal(emty));
                            epfObj.setRl(getLPFSupplementaryAmount(session, suppayprocessid));
                            epfObj.setVpf(getVPFSupplementaryAmount(session, suppayprocessid));
                            epfObj.setDvpf(StringtobigDecimal(emty));
                            epfObj.setNrl(StringtobigDecimal(emty));
                            epfObj.setEcfb(StringtobigDecimal(String.valueOf(percentage833)));
                            epfObj.setEcpf(StringtobigDecimal(String.valueOf(percentage367)));
                            epfObj.setSubs(supEPFAmount);
                            epfObj.setContributions(supEPFAmount);
                            epfObj.setSmonth(epfSupObj.getCalculatedmonth());
                            epfObj.setSyear(epfSupObj.getCalculatedyear());
                            epfObj.setCancelled(Boolean.FALSE);
                            epfObj.setAccregion(LoggedInRegion);
                            epfObj.setCreatedby(LoggedInUser);
                            epfObj.setCreateddate(getCurrentDate());
                            epfObj.setSupprocessid(suppayprocessid);
                            Transaction transaction = session.beginTransaction();
                            session.update(epfObj);
                            transaction.commit();
                        } else {
                            Employeeprovidentfundothers epfObj = new Employeeprovidentfundothers();
                            String id = getMaxEmployeeprovidentfundothersid(session, LoggedInRegion);
                            epfObj.setId(id);
                            epfObj.setEmployeemaster(empObj);
                            epfObj.setMonth(iMonth);
                            epfObj.setYear(iYear);
                            epfObj.setEmpcategory(empObj.getCategory());
                            epfObj.setPayrollcategory("S");
                            epfObj.setSalary(getSupplementarySalarySalary(session, suppayprocessid));
                            epfObj.setEpfwhole(new BigDecimal(epf));
                            epfObj.setFbf(StringtobigDecimal(emty));
                            epfObj.setRl(getLPFSupplementaryAmount(session, suppayprocessid));
                            epfObj.setVpf(getVPFSupplementaryAmount(session, suppayprocessid));
                            epfObj.setDvpf(StringtobigDecimal(emty));
                            epfObj.setNrl(StringtobigDecimal(emty));
                            epfObj.setEcfb(StringtobigDecimal(String.valueOf(percentage833)));
                            epfObj.setEcpf(StringtobigDecimal(String.valueOf(percentage367)));
                            epfObj.setSubs(supEPFAmount);
                            epfObj.setContributions(supEPFAmount);
                            epfObj.setSmonth(epfSupObj.getCalculatedmonth());
                            epfObj.setSyear(epfSupObj.getCalculatedyear());
                            epfObj.setCancelled(Boolean.FALSE);
                            epfObj.setAccregion(LoggedInRegion);
                            epfObj.setCreatedby(LoggedInUser);
                            epfObj.setCreateddate(getCurrentDate());
                            epfObj.setSupprocessid(suppayprocessid);

                            Transaction transaction = session.beginTransaction();
                            session.save(epfObj);
                            transaction.commit();
                        }
                    }
                }
            }
            //Supplementary EPF Calculation Starts
            resultMap.put("reason", "Successfully Prepared");
        } else {
            resultMap.put("reason", "Close Pay Bill Process and Prepare EPF");
        }
        return resultMap;
    }

    private long getECFBAccounted(Session session, String efno, String month, String year) {
        //String epfStr = "SELECT sum(coalesce(ecfb,'0')) FROM employeeprovidentfundothers where ( (smonth=" + month + " and syear=" + year + ") or  (month=" + month + " and year=" + year + ")) and epfno='" + efno + "' and cancelled is false";
//        String epfStr = "SELECT sum(coalesce(ecfb,'0')) FROM employeeprovidentfundothers where smonth=" + month + " and syear=" + year + " and epfno='" + efno + "' and cancelled is false";
        String epfStr = " select "
                + " coalesce((select sum(coalesce(ecfb,'0')) as regularAmt FROM employeeprovidentfundothers as r"
                + " where epfno='" + efno + "' and month=" + month + " and year=" + year + " "
                + " and payrollcategory='R'  and cancelled is false"
                + " ),0) as regularAmt,"
                + " coalesce((select  sum(coalesce(ecfb,'0')) as suppleAmt FROM employeeprovidentfundothers as s "
                + " where epfno='" + efno + "'  and smonth=" + month + " and syear=" + year + " "
                + " and payrollcategory='S' and cancelled is false ),0) as suppleAmt ";

        BigDecimal epf = new BigDecimal(0.00);
        double regularAmt = 0;
        double suppleAmt = 0;
        SQLQuery arearList1 = session.createSQLQuery(epfStr);
        for (ListIterator it = arearList1.list().listIterator(); it.hasNext();) {
            Object[] row = (Object[]) it.next();
            if (row[0] != null) {
                regularAmt = Double.parseDouble(row[0].toString());
                suppleAmt = Double.parseDouble(row[1].toString());
            }
        }
        if (regularAmt >= 1250) {
            epf = new BigDecimal(regularAmt);
        } else {
            epf = new BigDecimal(regularAmt + suppleAmt);
        }
        /*List arearList1 = (ArrayList) session.createSQLQuery(epfStr).list(); 
        if (arearList1.size() > 0) {
        epf = (BigDecimal) arearList1.get(0);
        } else {
        epf = new BigDecimal(0.00);
        }*/
        if (epf == null) {
            epf = new BigDecimal(0.00);
        }
        return epf.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
    }

    public synchronized String getMaxEmployeeprovidentfundothersid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeprovidentfundothersid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeprovidentfundothersid(maxNoStr);
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

    public BigDecimal getEPFAmount(Session session, String processingId) {
        BigDecimal empD = new BigDecimal("0");
        Criteria empEarTranCrit = session.createCriteria(Employeedeductionstransactions.class);
        empEarTranCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + processingId + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + "D02" + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List empEarTranList = empEarTranCrit.list();
        if (empEarTranList.size() > 0) {
            Employeedeductionstransactions employeeearningstransactionsObj = (Employeedeductionstransactions) empEarTranList.get(0);
            empD = employeeearningstransactionsObj.getAmount();
        }
        return empD;
    }

    public BigDecimal getLPFRegularAmount(Session session, String processingId) {
        BigDecimal empD = new BigDecimal("0");
        Criteria empEarTranCrit = session.createCriteria(Employeedeductionstransactions.class);
        empEarTranCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + processingId + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + "L02" + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List empEarTranList = empEarTranCrit.list();
        if (empEarTranList.size() > 0) {
            Employeedeductionstransactions employeeearningstransactionsObj = (Employeedeductionstransactions) empEarTranList.get(0);
            empD = employeeearningstransactionsObj.getAmount();
        }
        return empD;
    }

    public BigDecimal getLPFSupplementaryAmount(Session session, String processingId) {
        BigDecimal empD = new BigDecimal("0");
        Criteria empEarTranCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
        empEarTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + processingId + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + "L02" + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List empEarTranList = empEarTranCrit.list();
        if (empEarTranList.size() > 0) {
            Supplementaryemployeedeductionstransactions employeeearningstransactionsObj = (Supplementaryemployeedeductionstransactions) empEarTranList.get(0);
            empD = employeeearningstransactionsObj.getAmount();
        }
        return empD;
    }

    public BigDecimal getVPFSupplementaryAmount(Session session, String processingId) {
        BigDecimal empD = new BigDecimal("0");
        Criteria empEarTranCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
        empEarTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + processingId + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + "D03" + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List empEarTranList = empEarTranCrit.list();
        if (empEarTranList.size() > 0) {
            Supplementaryemployeedeductionstransactions employeeearningstransactionsObj = (Supplementaryemployeedeductionstransactions) empEarTranList.get(0);
            empD = employeeearningstransactionsObj.getAmount();
        }
        return empD;
    }

    public BigDecimal getVPFRegularAmount(Session session, String processingId) {
        BigDecimal empD = new BigDecimal("0");
        Criteria empEarTranCrit = session.createCriteria(Employeedeductionstransactions.class);
        empEarTranCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + processingId + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + "D03" + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List empEarTranList = empEarTranCrit.list();
        if (empEarTranList.size() > 0) {
            Employeedeductionstransactions employeeearningstransactionsObj = (Employeedeductionstransactions) empEarTranList.get(0);
            empD = employeeearningstransactionsObj.getAmount();
        }
        return empD;
    }

    public BigDecimal getSupEPFAmount(Session session, String processingId) {
        BigDecimal empD = new BigDecimal("0");
        Criteria empEarTranCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
        empEarTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + processingId + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("deductionmasterid='" + "D02" + "'"));
        empEarTranCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List empEarTranList = empEarTranCrit.list();
        if (empEarTranList.size() > 0) {
            Supplementaryemployeedeductionstransactions employeeearningstransactionsObj = (Supplementaryemployeedeductionstransactions) empEarTranList.get(0);
            empD = employeeearningstransactionsObj.getAmount();
        }
        return empD;
    }

    public BigDecimal getSalary(Session session, String processingId) {
        //String earningamount = "0";
        BigDecimal empD;
        String Earnings_Query = " select coalesce(sum(edt.amount),'0') as amout "
                + " from  employeeearningstransactions edt "
                + " where edt.payrollprocessingdetailsid='" + processingId + "' and edt.cancelled is false and edt.earningmasterid in (select paycode from ccahra where ccahra='D02') ";

        SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
        if (earningsquery.list().size() > 0) {
            empD = (BigDecimal) earningsquery.list().get(0);
        } else {
            empD = new BigDecimal("0");
        }
        //System.out.println("dd = "+dd);



        //empD = new BigDecimal(earningamount);
        return empD;
    }

    public BigDecimal getSupplementarySalarySalary(Session session, String subProcessingId) {
        //String earningamount = "0";
        BigDecimal empD;
        String Earnings_Query = " select coalesce(sum(edt.amount),'0') as amout "
                + " from  supplementaryemployeeearningstransactions edt "
                + " where edt.supplementarypayrollprocessingdetailsid='" + subProcessingId + "' and edt.cancelled is false and edt.earningmasterid in (select paycode from ccahra where ccahra='D02') ";

        SQLQuery earningsquery = session.createSQLQuery(Earnings_Query);
        if (earningsquery.list().size() > 0) {
            empD = (BigDecimal) earningsquery.list().get(0);
        } else {
            empD = new BigDecimal("0");
        }

        return empD;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPayrollProcessDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Payrollprocessing payrollprocessingObj;
        Criteria queryCrit = session.createCriteria(Payrollprocessing.class);
        queryCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        queryCrit.add(Restrictions.sqlRestriction("id = accregion||''||(select max(payrollprocessingid) from regionmaster where id='" + LoggedInRegion + "')"));

        List payListDate = queryCrit.list();
        if (payListDate.size() > 0) {
            for (int i = 0; i < payListDate.size(); i++) {
                payrollprocessingObj = (Payrollprocessing) payListDate.get(i);
                resultMap.put("year", payrollprocessingObj.getYear());
                resultMap.put("month", payrollprocessingObj.getMonth());
                resultMap.put("isopen", payrollprocessingObj.getIsopen());
            }
        }
        resultMap.put("length", payListDate.size());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map startPayRollProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        empCrit.add(Restrictions.sqlRestriction("category='" + "R" + "'"));
//        empCrit.add(Restrictions.sqlRestriction("epfno in ('" + "047066" + "','047803','049392')"));        
        List empList = empCrit.list();
        System.out.println("empList.size() " + empList.size());
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                employeemasterObj = (Employeemaster) empList.get(i);
                try {
                    //resultMap.put(i, employeemasterObj.getEpfno());
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(employeemasterObj.getEpfno());
                try {
                    payRollProcessThread(session, request, response, LoggedInRegion, LoggedInUser, "30/11/2015", "0", employeemasterObj.getEpfno());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        resultMap.put("length", empList.size());
        return resultMap;
    }
}
