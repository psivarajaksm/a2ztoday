/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.persistence.payroll.Employeeearningstransactions;
import com.onward.persistence.payroll.Employeemaster;
import com.onward.persistence.payroll.Paycodemaster;
import com.onward.persistence.payroll.Payrollprocessing;
import com.onward.persistence.payroll.Payrollprocessingdetails;
import com.onward.persistence.payroll.Professionaltaxslap;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.persistence.payroll.Salarydeductionothers;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sk
 */
public class SalaryDeductionOthersServiceImpl extends OnwardAction implements SalaryDeductionOthersService {

    String classname = "";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoadDeductionTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map deductionsMap = new LinkedHashMap();
        deductionsMap.put("0", "--Select--");
        String paycode = "";
        String paycodename = "";
        try {
            Criteria ernCrit = session.createCriteria(Paycodemaster.class);
            ernCrit.add(Restrictions.sqlRestriction("paycodetype='D'"));
            ernCrit.addOrder(Order.asc("paycodename"));
            List<Paycodemaster> ernList = ernCrit.list();
            resultMap = new TreeMap();
            for (Paycodemaster lbobj : ernList) {
                paycode = lbobj.getPaycode();
                paycodename = lbobj.getPaycodename();
                deductionsMap.put(paycode, paycodename);
            }
            resultMap.put("deductionList", deductionsMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map checkDeductionProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionmode) {
        Map resultMap = new HashMap();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int currnetmonth = month + 1;
        boolean proceed = false;
        int deductionMode = Integer.parseInt(deductionmode.toString());
        int previosYear = 0;
        int nextYear = 0;

        try {
            if (month < 3) {
                previosYear = year - 1;
                nextYear = year;
//                idValue = previosYear + " - " + year;
            } else {
                previosYear = year;
                nextYear = year + 1;
//                idValue = year + " - " + nextYear;
            }

            Criteria proCrit = session.createCriteria(Payrollprocessing.class);
            proCrit.add(Restrictions.sqlRestriction("month=" + currnetmonth));
            proCrit.add(Restrictions.sqlRestriction("year=" + year));
            proCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            List proList = proCrit.list();
            if (proList.size() > 0) {
                Payrollprocessing payrollprocessingObje = (Payrollprocessing) proList.get(0);
                if (payrollprocessingObje.getIsopen()) {
                    proceed = true;
                } else {
                    resultMap.put("ERROR", "Already Pay Roll Processed and closed");
                    proceed = false;
                }
            } else {
                proceed = true;
            }
            if (proceed) {
                if (deductionmode.equalsIgnoreCase("1") || deductionmode.equalsIgnoreCase("2")) {
                    Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                    onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + deductionMode));
                    onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
                    onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
                    List onedayList = onedayCrit.list();
                    if (onedayList.size() > 0) {
                        resultMap.put("reason", "Already Salary Deduction Others Processed.Do You Want to Continue the Reagain Process?");
                    } else {
                        resultMap.put("reason", "");
                    }
                } else if (deductionmode.equalsIgnoreCase("3")) {
                    Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                    onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + deductionMode));
                    onedayCrit.add(Restrictions.sqlRestriction("deductionmonth in (4,5,6,7,8,9)"));
//                    onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
                    onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
                    List onedayList = onedayCrit.list();
                    if (onedayList.size() > 0) {
                        resultMap.put("reason", "Already Salary Deduction Others Processed.Do You Want to Continue the Reagain Process?");
                    } else {
                        if (currnetmonth < 10 && currnetmonth > 3) {
                            resultMap.put("reason", "");
                        } else {
                            resultMap.put("ERROR", "Can't processed first half year pf deduction on this month");
                        }
                    }
                } else if (deductionmode.equalsIgnoreCase("4")) {
                    if (currnetmonth != 1 && currnetmonth != 2 && currnetmonth != 3 && currnetmonth != 10 && currnetmonth != 11 && currnetmonth != 12) {
                        resultMap.put("ERROR", "Can't processed second half year pf deduction on this month");
                    } else {
                        if (currnetmonth == 1 || currnetmonth == 2 || currnetmonth == 3 || currnetmonth == 10 || currnetmonth == 11 || currnetmonth == 12) {
                            Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                            onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + deductionMode));
                            onedayCrit.add(Restrictions.sqlRestriction("((deductionmonth in (1,2,3) and deductionyear=" + nextYear + ") or (deductionmonth in (10,11,12) and deductionyear=" + previosYear + "))"));
                            //                    onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
//                        onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
                            List onedayList = onedayCrit.list();
                            if (onedayList.size() > 0) {
                                resultMap.put("reason", "Already Salary Deduction Others Processed.Do You Want to Continue the Reagain Process?");
                            } else {
                                resultMap.put("reason", "");
//                            Criteria onedayCrit1 = session.createCriteria(Salarydeductionothers.class);
//                            onedayCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
//                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmode=" + deductionMode));
//                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmonth in (10,11,12)"));
//                            //                    onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
//                            onedayCrit1.add(Restrictions.sqlRestriction("deductionyear=" + year));
//                            List onedayList1 = onedayCrit1.list();
//                            if (onedayList1.size() > 0) {
//                                resultMap.put("reason", "Already Salary Deduction Others Processed.Do You Want to Continue the Reagain Process?");
//                            } else {
//                                resultMap.put("reason", "");
//                            }
                            }
                        }

                    }
                }

//                Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
//                onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion+"'"));
//                onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + deductionMode));
//                onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
//                onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
////                onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
//                List onedayList = onedayCrit.list();
//                if (onedayList.size() > 0) {
//                    resultMap.put("reason", "Already Salary Deduction Others Processed.Do You Want to Continue the Reagain Process?");
//
//                } else {
//                    if (deductionmode.equalsIgnoreCase("3")) {              // first half year
//                        if (currnetmonth < 10 && currnetmonth > 3) {
//                            resultMap.put("reason", "");
////                            resultMap.put("continue", "continue");
//                        }else{
//                             resultMap.put("ERROR", "Can't processed first half year pf deduction on this month");
//                        }
//                    }else{
//                        if (currnetmonth < 4 || currnetmonth > 9) {
//                            resultMap.put("reason", "");
////                                resultMap.put("continue", "continue");
//                        } else {
//                            resultMap.put("ERROR", "Can't processed second half year pf deduction on this month");
//                        }
//                    }
//
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map salayDeductionOthersProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductioncode, String deductionmode, String month, String year, String deductionamt, String isnew) {

        Map resultMap = new HashMap();
        boolean proceed = false;
        Employeemaster employeemasterObje;
        Map displayMap = new LinkedHashMap();
        Map saveMap = new LinkedHashMap();
        BigDecimal totAmount = new BigDecimal(0.00);
        BigDecimal onedaysalaryAmount = new BigDecimal(0.00);
        BigDecimal salaryAmount = new BigDecimal(0.00);
        int notprocesssalarycount = 0;
        int currentmonth = Integer.parseInt(month.toString());
        int currentyear = Integer.parseInt(year.toString());
//        System.out.println("currentmonth===" + currentmonth);
        try {


            if (deductionmode.equalsIgnoreCase("1")) {              // Fixed Amount
                Criteria empCrit = session.createCriteria(Employeemaster.class);
                empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                empCrit.add(Restrictions.sqlRestriction("process is true"));
                empCrit.addOrder(Order.asc("epfno"));
                List empList = empCrit.list();
                if (empList.size() > 0) {
                    for (int j = 0; j < empList.size(); j++) {
                        employeemasterObje = (Employeemaster) empList.get(j);
                        Salarydeductionothers otherObj = new Salarydeductionothers();

                        if (isnew.equalsIgnoreCase("2")) {
                            Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                            onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            onedayCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                            onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currentmonth));
                            onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + currentyear));
                            onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + deductionmode));
//                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                            List onedayList = onedayCrit.list();
                            if (onedayList.size() > 0) {
                                Salarydeductionothers deductionObje = (Salarydeductionothers) onedayList.get(0);
                                otherObj.setId(deductionObje.getId());
                            }
                        }

                        Paycodemaster payObj = new Paycodemaster();
                        payObj.setPaycode(deductioncode);


                        otherObj.setPaycodemaster(payObj);
                        otherObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                        otherObj.setEmployeemaster(employeemasterObje);
                        otherObj.setDeductionmonth(currentmonth);
                        otherObj.setDeductionyear(currentyear);
                        otherObj.setAmountornoofdays(new BigDecimal(deductionamt));
                        otherObj.setIntallmenttype(1);
                        otherObj.setCancelled(Boolean.FALSE);
                        otherObj.setAccregion(LoggedInRegion);
                        otherObj.setCreatedby(LoggedInUser);
                        otherObj.setCreateddate(getCurrentDate());
                        displayMap.put(employeemasterObje.getEpfno(), otherObj);
                        saveMap.put(employeemasterObje.getEpfno(), otherObj);
                    }
//                        resultMap.put("displayHTML", getPTaxDetailsInHTML(displayMap, currentmonth - 1, currentyear, "1"));
                }
            } else {
                Criteria empCrit = session.createCriteria(Employeemaster.class);
                empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                empCrit.add(Restrictions.sqlRestriction("process is true"));
                empCrit.addOrder(Order.asc("epfno"));
                List empList = empCrit.list();
                if (empList.size() > 0) {
                    for (int j = 0; j < empList.size(); j++) {
                        employeemasterObje = (Employeemaster) empList.get(j);

//                            currentmonth = currentmonth - 1;
                        Criteria proCrit1 = session.createCriteria(Payrollprocessingdetails.class);
                        proCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                        proCrit1.add(Restrictions.sqlRestriction("month=" + (currentmonth - 1)));
                        proCrit1.add(Restrictions.sqlRestriction("year=" + year));
                        proCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                        List proList1 = proCrit1.list();
//                            System.out.println("currentmonth1111===" + currentmonth);
//                            System.out.println("epfno===" + employeemasterObje.getEpfno());
                        if (proList1.size() > 0) {
                            Payrollprocessingdetails PayrollprocessingdetailsObje = (Payrollprocessingdetails) proList1.get(0);

                            Criteria earDe = session.createCriteria(Employeeearningstransactions.class);
                            earDe.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                            earDe.setProjection(Projections.sum("amount"));
                            earDe.add(Restrictions.sqlRestriction("cancelled is false"));
                            earDe.add(Restrictions.sqlRestriction("payrollprocessingdetailsid = '" + PayrollprocessingdetailsObje.getId() + "' "));
                            BigDecimal totalAmount = (BigDecimal) earDe.uniqueResult();
                            if (totalAmount == null) {
                                totAmount = new BigDecimal(0.00);
                            } else {
                                totAmount = totalAmount;
                            }
                            notprocesssalarycount = notprocesssalarycount + 1;

                        }
//                            System.out.println("totAmount===" + totAmount);
                        if (notprocesssalarycount != 0) {
//                                currentmonth=currentmonth+1;
                            Calendar cal = Calendar.getInstance();
                            cal.set(currentyear, Integer.parseInt(month.toString()) - 1, 1);
                            int days = cal.getActualMaximum(cal.DAY_OF_MONTH);
                            salaryAmount = totAmount.divide(new BigDecimal(days), RoundingMode.UP);
//                                salaryAmount = totAmount.divide(new BigDecimal(days),0,RoundingMode.UP);
                            salaryAmount = roundBigDecimal(salaryAmount);
                            salaryAmount = bigDecimalValidate(setPrecision(String.valueOf(salaryAmount), "0.00"));
                            onedaysalaryAmount = salaryAmount.multiply(new BigDecimal(deductionamt));

                            Paycodemaster payObj = new Paycodemaster();
                            payObj.setPaycode(deductioncode);
                            Salarydeductionothers otherObj = new Salarydeductionothers();
                            if (isnew.equalsIgnoreCase("2")) {
                                Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                                onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                onedayCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currentmonth));
                                onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + currentyear));
                                onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + deductionmode));
                                //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                List onedayList = onedayCrit.list();
                                if (onedayList.size() > 0) {
                                    Salarydeductionothers deductionObje = (Salarydeductionothers) onedayList.get(0);
                                    otherObj.setId(deductionObje.getId());
                                    otherObj.setCancelled(Boolean.FALSE);
                                }
                            }
                            otherObj.setPaycodemaster(payObj);
                            otherObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                            otherObj.setEmployeemaster(employeemasterObje);
                            otherObj.setDeductionmonth(currentmonth);
                            otherObj.setDeductionyear(currentyear);
                            otherObj.setAmountornoofdays(onedaysalaryAmount);
                            otherObj.setIntallmenttype(1);
                            otherObj.setCancelled(Boolean.FALSE);
                            otherObj.setAccregion(LoggedInRegion);
                            otherObj.setCreatedby(LoggedInUser);
                            otherObj.setCreateddate(getCurrentDate());
                            displayMap.put(employeemasterObje.getEpfno(), otherObj);
                            saveMap.put(employeemasterObje.getEpfno(), otherObj);
                        }
                    }
                }

            }
            resultMap.put("displayHTML", getPTaxDetailsInHTML(displayMap, currentmonth - 1, currentyear, "1"));
            request.getSession().setAttribute("saveMap", saveMap);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction Faild");
        }
        return resultMap;
    }

    public String getPTaxDetailsInHTML(Map displayMap, int month, int year, String installmentmode) {
        System.out.println("month==" + month);
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int i = 0;
        StringBuffer resultHTML = new StringBuffer();
        try {
            if (installmentmode.equalsIgnoreCase("1")) {

                if (displayMap.size() > 0) {
                    resultHTML.append("<FONT SIZE=2>");
                    resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"ptaxtable\">");
//                    resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                    resultHTML.append("<tr><td valign=\"top\">");
//                    resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                    resultHTML.append("<thead>");
                    resultHTML.append("<tr class=\"gridmenu\">");
                    resultHTML.append("<td>S.No</td>");
                    resultHTML.append("<td style=\"cursor:pointer;\">EPF No</td>");
                    resultHTML.append("<td style=\"cursor:pointer;\">Employee Name</td>");
                    resultHTML.append("<td> " + monthName[month] + " " + year + "</td>");
                    resultHTML.append("<td>Exception Employee</td>");
                    resultHTML.append("</tr>");
                    resultHTML.append("</thead>");
                    resultHTML.append("<tbody>");

                    for (Object obj : displayMap.values()) {
                        Salarydeductionothers sdoObj = (Salarydeductionothers) obj;

                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }
                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"center\">" + sdoObj.getEmployeemaster().getEpfno() + "</td>");
                        resultHTML.append("<td align=\"left\">" + sdoObj.getEmployeemaster().getEmployeename() + "</td>");
                        resultHTML.append("<td align=\"center\">" + sdoObj.getAmountornoofdays() + "</td>");
                        resultHTML.append("<td align=\"center\"><input type=\"checkbox\" name=\"noofepno\" id=\"" + sdoObj.getEmployeemaster().getEpfno() + "\"  value=\"" + sdoObj.getEmployeemaster().getEpfno() + "\"></td>");
//                        resultHTML.append("<td align=\"center\"><input type=\"checkbox\" name=\"noofepno\" id=\"" + sdoObj.getEmployeemaster().getEpfno() + "\" onclick=\"checkEmployees(this.value);\" value=\"" + sdoObj.getEmployeemaster().getEpfno() + "\"></td>");
                        resultHTML.append("</tr>");
                        i++;

                    }
                }
            } else {
                int nextYear = year;
                int nextMonth = month;
                if (month + 1 == 12) {
                    nextMonth = 0;
                    nextYear = year + 1;
                }
                if (displayMap.size() > 0) {
                    resultHTML.append("<FONT SIZE=2>");
                    resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"ptaxtable\">");
//                    resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                    resultHTML.append("<tr><td valign=\"top\">");
//                    resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                    resultHTML.append("<thead>");
                    resultHTML.append("<tr class=\"gridmenu\">");
                    resultHTML.append("<td>S.No</td>");
                    resultHTML.append("<td style=\"cursor:pointer;\">EPF No</td>");
                    resultHTML.append("<td style=\"cursor:pointer;\">Employee Name</td>");
                    resultHTML.append("<td> " + monthName[month] + " " + year + "</td>");
                    resultHTML.append("<td> " + monthName[nextMonth + 1] + " " + nextYear + "</td>");
                    resultHTML.append("<td>Exception Employee</td>");
                    resultHTML.append("</tr>");
                    resultHTML.append("</thead>");
                    resultHTML.append("<tbody>");
                    for (Object obj : displayMap.values()) {
                        Salarydeductionothers sdoObj = (Salarydeductionothers) obj;
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }

                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                        resultHTML.append("<td align=\"center\">" + sdoObj.getEmployeemaster().getEpfno() + "</td>");
                        resultHTML.append("<td align=\"left\">" + sdoObj.getEmployeemaster().getEmployeename() + "</td>");
                        resultHTML.append("<td align=\"center\">" + sdoObj.getAmountornoofdays() + "</td>");
                        resultHTML.append("<td align=\"center\">" + sdoObj.getId() + "</td>");
                        resultHTML.append("<td align=\"center\"><input type=\"checkbox\" name=\"noofepno\" id=\"" + sdoObj.getEmployeemaster().getEpfno() + "\"  value=\"" + sdoObj.getEmployeemaster().getEpfno() + "\"></td>");
//                                .append("<td align=\"center\"><input type=\"checkbox\" name=\"noofepno\" id=\"" + sdoObj.getEmployeemaster().getEpfno() + "\" onclick=\"checkEmployees(this.value);\" value=\"" + sdoObj.getEmployeemaster().getEpfno() + "\"></td>")
                        resultHTML.append("</tr>");
                        i++;
                    }
                }
            }
            resultHTML.append("</tbody>");
            resultHTML.append("</table>");
            resultHTML.append("</FONT>");


        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("resultHTML.toString()==" + resultHTML.toString());
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveDeductionOthers(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String hdnemployees, String installmentmode, String delimiter, String isNew) {
        Map resultMap = new HashMap();
        Map saveInnerMap = new HashMap();
        Map displayMap = null;
        String[] strArray = null;
        try {
//            String delimiter = "-";
            displayMap = (Map) request.getSession().getAttribute("saveMap");

            if (installmentmode.equalsIgnoreCase("2")) {
                saveInnerMap = (Map) request.getSession().getAttribute("saveInnerMap");
            }
            if (!hdnemployees.equalsIgnoreCase("") && hdnemployees != null) {
                strArray = hdnemployees.split(java.util.regex.Pattern.quote(delimiter));
                for (String exceptEmp1 : strArray) {
//                    System.out.println("exceptEmp1===="+exceptEmp1);
                    if (displayMap.containsKey(exceptEmp1)) {
                        Salarydeductionothers sdoObj = (Salarydeductionothers) displayMap.get(exceptEmp1);
                        sdoObj.setCancelled(Boolean.TRUE);
//                        displayMap.remove(exceptEmp1);
//                        displayMap.put(sdoObj.getEmployeemaster().getEpfno(), sdoObj);
                    }
                    if (installmentmode.equalsIgnoreCase("2")) {
                        if (saveInnerMap.containsKey(exceptEmp1)) {
                            Salarydeductionothers sdoObj = (Salarydeductionothers) saveInnerMap.get(exceptEmp1);
                            sdoObj.setCancelled(Boolean.TRUE);
//                            saveInnerMap.remove(exceptEmp1);
//                            saveInnerMap.put(sdoObj.getEmployeemaster().getEpfno(), sdoObj);
                        }
                    }

                }
            }
            if (displayMap.size() > 0) {
                for (Object obj : displayMap.values()) {
                    Salarydeductionothers sdoObj = (Salarydeductionothers) obj;
                    if (isNew.equalsIgnoreCase("1")) {
                        sdoObj.setId(getMaxSeqNumberSalaryDeductionOthers(session, LoggedInRegion));
                    }
//                    System.out.println("sdoObj===" + sdoObj.getId());
                    sdoObj.setAccregion(LoggedInRegion);
                    sdoObj.setCreateddate(getCurrentDate());
                    sdoObj.setCreatedby(LoggedInUser);
                    this.savePF(session, sdoObj, isNew);
                }
                if (installmentmode.equalsIgnoreCase("2")) {
                    for (Object obj : saveInnerMap.values()) {
                        Salarydeductionothers sdoObj = (Salarydeductionothers) obj;
                        sdoObj.setAccregion(LoggedInRegion);
                        sdoObj.setCreateddate(getCurrentDate());
                        sdoObj.setCreatedby(LoggedInUser);
                        if (isNew.equalsIgnoreCase("1")) {
                            sdoObj.setId(getMaxSeqNumberSalaryDeductionOthers(session, LoggedInRegion));
                            this.savePF(session, sdoObj, isNew);
                        } else {
                            if (sdoObj.getId().equalsIgnoreCase("")) {
                                sdoObj.setId(getMaxSeqNumberSalaryDeductionOthers(session, LoggedInRegion));
                                this.savePF(session, sdoObj, "1");
                            } else {
                                this.savePF(session, sdoObj, isNew);
                            }
                        }

                    }
                    request.getSession().removeAttribute("saveInnerMap");

                }
                request.getSession().removeAttribute("displayMap");
                request.getSession().removeAttribute("saveMap");
            }
            resultMap.put("reason", "Deduction Others Successfully Deducted");



        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    public void savePF(Session session, Salarydeductionothers sdoObj, String isNew) {
        Transaction transaction = null;
        try {

            transaction = session.beginTransaction();
            if (isNew.equalsIgnoreCase("1")) {
                session.save(sdoObj);
            } else {
                session.update(sdoObj);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    public synchronized String getMaxSeqNumberSalaryDeductionOthers(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getSalarydeductionothersid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setSalarydeductionothersid(maxNoStr);
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
    public Map loadFinanicalYears(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map yearMap = new LinkedHashMap();
        yearMap.put("0", "--Select--");

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String idValue = "";
        int nextYear = 0;
        int previosYear = 0;
        try {
            if (month < 3) {
                previosYear = year - 1;
                idValue = previosYear + " - " + year;
            } else {
                nextYear = year + 1;
                idValue = year + " - " + nextYear;
            }
//            for(int i=year;i<year+10;year++){
//                nextYear=""+year+1;
//                idValue=year+" - "+nextYear;
////                if(month>=3){
////
////                }
//                yearMap.put(idValue, idValue);
//            }
//            nextYear = year + 1;
//            idValue = year + " - " + nextYear;
            yearMap.put(idValue, idValue);
            resultMap.put("finanicalyearslist", yearMap);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map proffesionalTaxDeductionProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String finanicalyear, String deductionmode, String installmentmode, String isnew) {

        Map resultMap = new HashMap();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int currnetmonth = month + 1;
        System.out.println("month==" + month);
        System.out.println("currnetmonth==" + currnetmonth);
        boolean proceed = false;
        int notprocesssalarycount = 0;
        Employeemaster employeemasterObje;
        BigDecimal grossAmount = new BigDecimal(0.00);
        BigDecimal totalgrossAmount = new BigDecimal(0.00);
        BigDecimal totAmount = new BigDecimal(0.00);
        BigDecimal pfdeductionAmount = new BigDecimal(0.00);
        BigDecimal pfdeductionAmount1 = new BigDecimal(0.00);
        Map displayMap = new LinkedHashMap();
        Map saveMap = new LinkedHashMap();
        Map saveInnerMap = new LinkedHashMap();
        Transaction transaction = null;
        int dmonth = 0, dyear = 0, nextMonth = 0;

        try {
            if (installmentmode.equalsIgnoreCase("2")) {
                if (currnetmonth == 9 || currnetmonth == 3) {
                    resultMap.put("ERROR", "Double Payment Cannot process on This Month");
                    proceed = false;
                } else {
                    proceed = true;
                }
            } else {
                proceed = true;
            }
            if (proceed) {
                if (deductionmode.equalsIgnoreCase("3")) {              // first half year
                    // <editor-fold defaultstate="collapsed">
                    if (currnetmonth < 10 && currnetmonth > 3) {
                        Criteria empCrit = session.createCriteria(Employeemaster.class);
                        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                        empCrit.add(Restrictions.sqlRestriction("process is true"));
                        empCrit.addOrder(Order.asc("epfno"));
//                            empCrit.add(Restrictions.sqlRestriction("region='R01'"));
                        List empList = empCrit.list();
                        if (empList.size() > 0) {
                            // <editor-fold defaultstate="collapsed">
                            for (int j = 0; j < empList.size(); j++) {
                                // <editor-fold defaultstate="collapsed">
                                employeemasterObje = (Employeemaster) empList.get(j);
//                                System.out.println("employeemasterObje=====" + employeemasterObje.getEpfno());
                                String epf = employeemasterObje.getEpfno();
                                System.out.println("epf Number = " + epf);
                                for (int i = 4; i <= 9; i++) {
                                    // <editor-fold defaultstate="collapsed" desc="for loop">
                                    System.out.println(i + " :::::::::::::::::: " + i);
                                    Criteria proCrit1 = session.createCriteria(Payrollprocessingdetails.class);
                                    proCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                    proCrit1.add(Restrictions.sqlRestriction("month=" + i));
                                    proCrit1.add(Restrictions.sqlRestriction("year=" + year));
                                    proCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                    List proList1 = proCrit1.list();

                                    if (proList1.size() > 0) {
                                        // <editor-fold defaultstate="collapsed" desc="if">
                                        Payrollprocessingdetails PayrollprocessingdetailsObje = (Payrollprocessingdetails) proList1.get(0);

                                        Criteria earDe = session.createCriteria(Employeeearningstransactions.class);
                                        earDe.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        earDe.setProjection(Projections.sum("amount"));
                                        earDe.add(Restrictions.sqlRestriction("cancelled is false"));
                                        earDe.add(Restrictions.sqlRestriction("payrollprocessingdetailsid = '" + PayrollprocessingdetailsObje.getId() + "' "));
                                        BigDecimal totalAmount = (BigDecimal) earDe.uniqueResult();
                                        System.out.println("epf= " + epf);
                                        System.out.println("PayrollprocessingdetailsObje.getId() = " + PayrollprocessingdetailsObje.getId());
                                        System.out.println("totalAmount = " + totalAmount);
                                        if (totalAmount == null) {
                                            totAmount = new BigDecimal(0.00);
                                        } else {
                                            totAmount = totalAmount;
                                        }
                                        grossAmount = grossAmount.add(totAmount);
                                        // </editor-fold>
                                    } else {
                                        notprocesssalarycount = notprocesssalarycount + 1;
                                    }
                                    // </editor-fold>
                                    System.out.println("*****************************************************************************");
                                }
                                System.out.println("notprocesssalarycount===" + notprocesssalarycount);
                                if (notprocesssalarycount != 6) {
                                    // <editor-fold defaultstate="collapsed">
//                                    if (notprocesssalarycount != 3 || notprocesssalarycount != 6) {
                                    totalgrossAmount = grossAmount.multiply(new BigDecimal(notprocesssalarycount));
                                    pfdeductionAmount = getPTDeductionSlap(totalgrossAmount, LoggedInRegion, session);

                                    if (installmentmode.equalsIgnoreCase("1")) {        //Single Installment
                                        // <editor-fold defaultstate="collapsed">
                                        Paycodemaster payObj = new Paycodemaster();
                                        payObj.setPaycode("L31");

                                        Salarydeductionothers otherObj = new Salarydeductionothers();
                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductioncode='L31'"));
//                                            onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + Integer.parseInt(deductionmode.toString())));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList = onedayCrit.list();
                                            if (onedayList.size() > 0) {
                                                Salarydeductionothers deductionObje = (Salarydeductionothers) onedayList.get(0);
                                                otherObj.setId(deductionObje.getId());
                                                otherObj.setCancelled(Boolean.FALSE);
                                            }
                                            // </editor-fold>
                                        }

                                        otherObj.setPaycodemaster(payObj);
                                        otherObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                                        otherObj.setEmployeemaster(employeemasterObje);
                                        otherObj.setDeductionmonth(currnetmonth);
                                        otherObj.setDeductionyear(year);
                                        otherObj.setAmountornoofdays(pfdeductionAmount);
                                        otherObj.setIntallmenttype(Integer.parseInt(installmentmode.toString()));
                                        otherObj.setAccregion(LoggedInRegion);
                                        otherObj.setCancelled(Boolean.FALSE);
                                        otherObj.setCreatedby(LoggedInUser);
                                        otherObj.setCreateddate(getCurrentDate());
                                        displayMap.put(employeemasterObje.getEpfno(), otherObj);
                                        saveMap.put(employeemasterObje.getEpfno(), otherObj);


                                        dmonth = currnetmonth + 1;
                                        dyear = year;
                                        if (dmonth == 12) {
                                            dyear = dyear + 1;
                                        }

                                        //  start  double payment to single payment
                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit1 = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmonth=" + dmonth));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionyear=" + dyear));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductioncode='L31'"));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList1 = onedayCrit1.list();
                                            if (onedayList1.size() > 0) {
                                                transaction = session.beginTransaction();
                                                Salarydeductionothers deductionObje1 = (Salarydeductionothers) onedayList1.get(0);
                                                deductionObje1.setCancelled(Boolean.TRUE);
                                                session.update(deductionObje1);
                                                transaction.commit();
                                            }
                                            // </editor-fold>
                                        }
                                        //  End  double payment to single payment

                                        // </editor-fold>
                                    } else {                //Double Installment
                                        // <editor-fold defaultstate="collapsed">
                                        pfdeductionAmount1 = pfdeductionAmount.divide(new BigDecimal(2), RoundingMode.UP);
                                        pfdeductionAmount = pfdeductionAmount.subtract(pfdeductionAmount1);
//                                        System.out.println("pfdeductionAmount1=="+pfdeductionAmount1+"====pfdeductionAmount==="+pfdeductionAmount);

                                        Paycodemaster payObj = new Paycodemaster();
                                        payObj.setPaycode("L31");

                                        Salarydeductionothers otherObj = new Salarydeductionothers();
                                        otherObj.setPaycodemaster(payObj);
                                        otherObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                                        otherObj.setEmployeemaster(employeemasterObje);
                                        otherObj.setDeductionmonth(currnetmonth);
                                        otherObj.setDeductionyear(year);
                                        otherObj.setAmountornoofdays(pfdeductionAmount1);
                                        otherObj.setIntallmenttype(Integer.parseInt(installmentmode.toString()));
                                        otherObj.setCancelled(Boolean.FALSE);
                                        otherObj.setAccregion(LoggedInRegion);
                                        otherObj.setId(String.valueOf(pfdeductionAmount));
                                        otherObj.setCreatedby(LoggedInUser);
                                        otherObj.setCreateddate(getCurrentDate());

                                        displayMap.put(employeemasterObje.getEpfno(), otherObj);

                                        Salarydeductionothers copyObj = new Salarydeductionothers();
                                        BeanUtils.copyProperties(copyObj, otherObj);

                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductioncode='L31'"));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList = onedayCrit.list();
                                            if (onedayList.size() > 0) {
                                                Salarydeductionothers deductionObje = (Salarydeductionothers) onedayList.get(0);
                                                copyObj.setId(deductionObje.getId());
                                                copyObj.setCancelled(Boolean.FALSE);
                                            }
                                            // </editor-fold>
                                        }
                                        saveMap.put(employeemasterObje.getEpfno(), copyObj);

//                                        dmonth = currnetmonth + 1;
                                        dyear = year;
                                        if (currnetmonth == 12) {
                                            dmonth = 1;
                                            dyear = dyear + 1;
                                        } else {
                                            dmonth = currnetmonth + 1;
                                        }

                                        Salarydeductionothers nextObj = new Salarydeductionothers();
                                        nextObj.setPaycodemaster(payObj);
                                        nextObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                                        nextObj.setEmployeemaster(employeemasterObje);
                                        nextObj.setDeductionmonth(dmonth);
                                        nextObj.setDeductionyear(dyear);
                                        nextObj.setAmountornoofdays(pfdeductionAmount);
                                        nextObj.setIntallmenttype(Integer.parseInt(installmentmode.toString()));
                                        nextObj.setCancelled(Boolean.FALSE);
                                        nextObj.setAccregion(LoggedInRegion);
                                        nextObj.setCreatedby(LoggedInUser);
                                        nextObj.setCreateddate(getCurrentDate());
                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit1 = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmonth=" + dmonth));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionyear=" + dyear));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductioncode='L31'"));
//                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmode=" + Integer.parseInt(deductionmode.toString())));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList1 = onedayCrit1.list();
                                            if (onedayList1.size() > 0) {
                                                Salarydeductionothers deductionObje1 = (Salarydeductionothers) onedayList1.get(0);
                                                nextObj.setId(deductionObje1.getId());
                                                nextObj.setCancelled(Boolean.FALSE);
                                            } else {
                                                nextObj.setId("");
                                            }
                                            // </editor-fold>
                                        }
                                        saveInnerMap.put(employeemasterObje.getEpfno(), nextObj);
                                        // </editor-fold>
                                    }
                                    // </editor-fold>
                                }
                                grossAmount = new BigDecimal(0.00);
                                notprocesssalarycount = 0;
                                // </editor-fold>
                            }
                            resultMap.put("displayHTML", getPTaxDetailsInHTML(displayMap, currnetmonth - 1, year, installmentmode));
//                                request.getSession().setAttribute("displayMap", displayMap);
                            request.getSession().setAttribute("saveMap", saveMap);
                            request.getSession().setAttribute("saveInnerMap", saveInnerMap);
                            // </editor-fold>
                        }

                    } else {
                        resultMap.put("reason", "Can't processed first half year pf deduction on this month");
                    }
                    // </editor-fold>
                } else {                                                 // second half year
                    if (currnetmonth < 4 || currnetmonth > 9) {
                        notprocesssalarycount = 0;
                        Criteria empCrit = session.createCriteria(Employeemaster.class);
                        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                        empCrit.add(Restrictions.sqlRestriction("process is true"));
                        empCrit.addOrder(Order.asc("epfno"));

                        List empList = empCrit.list();
                        if (empList.size() > 0) {
                            for (int j = 0; j < empList.size(); j++) {
                                // <editor-fold defaultstate="collapsed">
                                employeemasterObje = (Employeemaster) empList.get(j);
//                                System.out.println("employeemasterObje=====" + employeemasterObje.getEpfno());
                                int i, p, q, r;
                                p = 10;
                                q = 12;
                                for (i = p; i <= q; i++) {
                                    // <editor-fold defaultstate="collapsed">
                                    Criteria proCrit1 = session.createCriteria(Payrollprocessingdetails.class);
                                    proCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                    proCrit1.add(Restrictions.sqlRestriction("month=" + i));
                                    proCrit1.add(Restrictions.sqlRestriction("year=" + year));
                                    proCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                    List proList1 = proCrit1.list();
//                                            System.out.println("proList1.size()==="+proList1.size());
                                    if (proList1.size() > 0) {
                                        Payrollprocessingdetails PayrollprocessingdetailsObje = (Payrollprocessingdetails) proList1.get(0);
                                        Criteria earDe = session.createCriteria(Employeeearningstransactions.class);
                                        earDe.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        earDe.setProjection(Projections.sum("amount"));
                                        earDe.add(Restrictions.sqlRestriction("cancelled is false"));
                                        earDe.add(Restrictions.sqlRestriction("payrollprocessingdetailsid = '" + PayrollprocessingdetailsObje.getId() + "' "));
                                        BigDecimal totalAmount = (BigDecimal) earDe.uniqueResult();

                                        System.out.println("employeemasterObje.getEpfno() = " + employeemasterObje.getEpfno());
                                        System.out.println("PayrollprocessingdetailsObje.getId() = " + PayrollprocessingdetailsObje.getId());
                                        System.out.println("i = " + i);
                                        System.out.println("year = " + year);
                                        System.out.println("totalAmount = " + totalAmount);
                                        if (totalAmount == null) {
                                            System.out.println("if part ::::::::::::::::::");
                                            totAmount = new BigDecimal(0.00);
                                        } else {
                                            totAmount = totalAmount;
                                        }
                                        grossAmount = grossAmount.add(totAmount);
                                    } else {
                                        notprocesssalarycount = notprocesssalarycount + 1;
                                    }
                                    // </editor-fold>
                                }
                                for (i = 1; i <= 3; i++) {
                                    // <editor-fold defaultstate="collapsed">
                                    Criteria proCrit1 = session.createCriteria(Payrollprocessingdetails.class);
                                    proCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                    proCrit1.add(Restrictions.sqlRestriction("month=" + i));
                                    proCrit1.add(Restrictions.sqlRestriction("year=" + (year + 1)));
                                    proCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                    List proList1 = proCrit1.list();
//                                            System.out.println("proList1.size()==="+proList1.size());
                                    if (proList1.size() > 0) {
                                        Payrollprocessingdetails PayrollprocessingdetailsObje = (Payrollprocessingdetails) proList1.get(0);
                                        Criteria earDe = session.createCriteria(Employeeearningstransactions.class);
                                        earDe.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                        earDe.setProjection(Projections.sum("amount"));
                                        earDe.add(Restrictions.sqlRestriction("cancelled is false"));
                                        earDe.add(Restrictions.sqlRestriction("payrollprocessingdetailsid = '" + PayrollprocessingdetailsObje.getId() + "' "));
                                        BigDecimal totalAmount = (BigDecimal) earDe.uniqueResult();
                                        System.out.println("employeemasterObje.getEpfno() = " + employeemasterObje.getEpfno());
                                        System.out.println("PayrollprocessingdetailsObje.getId() = " + PayrollprocessingdetailsObje.getId());
                                        System.out.println("i = " + i);
                                        System.out.println("year = " + (year+1));
                                        System.out.println("totalAmount = " + totalAmount);

                                        if (totalAmount == null) {
                                            System.out.println("if part ::::::::::::::::::");
                                            totAmount = new BigDecimal(0.00);
                                        } else {
                                            totAmount = totalAmount;
                                        }
                                        grossAmount = grossAmount.add(totAmount);
                                    } else {
                                        notprocesssalarycount = notprocesssalarycount + 1;
                                    }
                                    // </editor-fold>
                                }
                                System.out.println("notprocesssalarycount = " + notprocesssalarycount);
                                if (notprocesssalarycount != 6) {
//                                if (notprocesssalarycount != 0) {
//                                    if (notprocesssalarycount != 3 || notprocesssalarycount != 6) {
                                    totalgrossAmount = grossAmount.multiply(new BigDecimal(notprocesssalarycount));

                                    pfdeductionAmount = getPTDeductionSlap(totalgrossAmount, LoggedInRegion, session);

                                    if (installmentmode.equalsIgnoreCase("1")) {   // Single Installment
                                        // <editor-fold defaultstate="collapsed">
                                        Paycodemaster payObj = new Paycodemaster();
                                        payObj.setPaycode("L31");

                                        Salarydeductionothers otherObj = new Salarydeductionothers();
                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductioncode='L31'"));
//                                            onedayCrit.add(Restrictions.sqlRestriction("deductionmode=" + Integer.parseInt(deductionmode.toString())));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList = onedayCrit.list();
                                            if (onedayList.size() > 0) {
                                                Salarydeductionothers deductionObje = (Salarydeductionothers) onedayList.get(0);
                                                otherObj.setId(deductionObje.getId());
                                                otherObj.setCancelled(Boolean.FALSE);
                                            }
                                            // </editor-fold>
                                        }
                                        otherObj.setPaycodemaster(payObj);
                                        otherObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                                        otherObj.setEmployeemaster(employeemasterObje);
                                        otherObj.setDeductionmonth(currnetmonth);
                                        otherObj.setDeductionyear(year);
                                        otherObj.setAmountornoofdays(pfdeductionAmount);
                                        otherObj.setIntallmenttype(Integer.parseInt(installmentmode.toString()));
                                        otherObj.setCancelled(Boolean.FALSE);
                                        otherObj.setAccregion(LoggedInRegion);
                                        otherObj.setCreatedby(LoggedInUser);
                                        otherObj.setCreateddate(getCurrentDate());
                                        displayMap.put(employeemasterObje.getEpfno(), otherObj);
                                        saveMap.put(employeemasterObje.getEpfno(), otherObj);
                                        dmonth = currnetmonth;
                                        dyear = year;
                                        if (dmonth == 12) {
                                            dyear = dyear + 1;
                                        }
                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit1 = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmonth=" + dmonth));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionyear=" + dyear));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductioncode='L31'"));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList1 = onedayCrit1.list();
                                            if (onedayList1.size() > 0) {
                                                transaction = session.beginTransaction();
                                                Salarydeductionothers deductionObje1 = (Salarydeductionothers) onedayList1.get(0);
                                                deductionObje1.setCancelled(Boolean.TRUE);
                                                session.update(deductionObje1);
                                                transaction.commit();
                                            }
                                            // </editor-fold>
                                        }
                                        // </editor-fold>
                                    } else {               //Double Installment
                                        // <editor-fold defaultstate="collapsed">
                                        pfdeductionAmount1 = pfdeductionAmount.divide(new BigDecimal(2), RoundingMode.UP);
                                        pfdeductionAmount = pfdeductionAmount.subtract(pfdeductionAmount1);

                                        Paycodemaster payObj = new Paycodemaster();
                                        payObj.setPaycode("L31");

                                        Salarydeductionothers otherObj = new Salarydeductionothers();
                                        otherObj.setPaycodemaster(payObj);
                                        otherObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                                        otherObj.setEmployeemaster(employeemasterObje);
                                        otherObj.setDeductionmonth(currnetmonth);
                                        otherObj.setDeductionyear(year);
                                        otherObj.setAmountornoofdays(pfdeductionAmount1);
                                        otherObj.setIntallmenttype(Integer.parseInt(installmentmode.toString()));
                                        otherObj.setCancelled(Boolean.FALSE);
                                        otherObj.setAccregion(LoggedInRegion);
                                        otherObj.setId(String.valueOf(pfdeductionAmount));
                                        otherObj.setCreatedby(LoggedInUser);
                                        otherObj.setCreateddate(getCurrentDate());

                                        Salarydeductionothers copyObj = new Salarydeductionothers();
                                        BeanUtils.copyProperties(copyObj, otherObj);

                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionmonth=" + currnetmonth));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductionyear=" + year));
                                            onedayCrit.add(Restrictions.sqlRestriction("deductioncode='L31'"));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList = onedayCrit.list();
                                            if (onedayList.size() > 0) {
                                                Salarydeductionothers deductionObje = (Salarydeductionothers) onedayList.get(0);
                                                copyObj.setId(deductionObje.getId());
                                                copyObj.setCancelled(Boolean.FALSE);
                                            }
                                            // </editor-fold>
                                        }

                                        displayMap.put(employeemasterObje.getEpfno(), otherObj);

                                        saveMap.put(employeemasterObje.getEpfno(), copyObj);
                                        dyear = year;
                                        if (currnetmonth == 12) {
                                            dmonth = 1;
                                            dyear = dyear + 1;
                                        } else {
                                            dmonth = currnetmonth + 1;
                                        }

                                        Salarydeductionothers nextObj = new Salarydeductionothers();
                                        nextObj.setPaycodemaster(payObj);
                                        nextObj.setDeductionmode(Integer.parseInt(deductionmode.toString()));
                                        nextObj.setEmployeemaster(employeemasterObje);
                                        nextObj.setDeductionmonth(dmonth);
                                        nextObj.setDeductionyear(dyear);
                                        nextObj.setAmountornoofdays(pfdeductionAmount);
                                        nextObj.setIntallmenttype(Integer.parseInt(installmentmode.toString()));
                                        nextObj.setCancelled(Boolean.FALSE);
                                        nextObj.setAccregion(LoggedInRegion);
                                        nextObj.setCreatedby(LoggedInUser);
                                        nextObj.setCreateddate(getCurrentDate());

                                        if (isnew.equalsIgnoreCase("2")) {
                                            // <editor-fold defaultstate="collapsed">
                                            Criteria onedayCrit1 = session.createCriteria(Salarydeductionothers.class);
                                            onedayCrit1.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + employeemasterObje.getEpfno() + "'"));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmonth=" + dmonth));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionyear=" + dyear));
                                            onedayCrit1.add(Restrictions.sqlRestriction("deductioncode='L31'"));
//                                            onedayCrit1.add(Restrictions.sqlRestriction("deductionmode=" + Integer.parseInt(deductionmode.toString())));
                                            //                            onedayCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                                            List onedayList1 = onedayCrit1.list();
                                            if (onedayList1.size() > 0) {
                                                Salarydeductionothers deductionObje1 = (Salarydeductionothers) onedayList1.get(0);
                                                nextObj.setId(deductionObje1.getId());
                                                nextObj.setCancelled(Boolean.FALSE);
                                            } else {
                                                nextObj.setId("");
                                            }
                                            // </editor-fold>
                                        }
                                        saveInnerMap.put(employeemasterObje.getEpfno(), nextObj);
                                        // </editor-fold>
                                    }
                                }
                                grossAmount = new BigDecimal(0.00);
                                notprocesssalarycount = 0;
                            }
                            resultMap.put("displayHTML", getPTaxDetailsInHTML(displayMap, currnetmonth - 1, year, installmentmode));
//                                request.getSession().setAttribute("displayMap", displayMap);
                            request.getSession().setAttribute("saveMap", saveMap);
                            request.getSession().setAttribute("saveInnerMap", saveInnerMap);
                            // </editor-fold>
                        }
                    } else {
                        resultMap.put("reason", "Can't processed second half year pf deduction on this month");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    public BigDecimal getPTDeductionSlap(BigDecimal amount, String LoggedInRegion, Session session) {
        Double totalgrossamount = Double.valueOf(amount.toString());
        System.out.println("amount==" + amount);
        BigDecimal pfdeductionAmount = new BigDecimal(0.00);

//        if (totalgrossamount > 75000) {
//            pfdeductionAmount = new BigDecimal(1095.00);
//        } else if (totalgrossamount > 21000 && totalgrossamount < 30001) {
//            pfdeductionAmount = new BigDecimal(100.00);
//        } else if (totalgrossamount > 30000 && totalgrossamount < 45001) {
//            pfdeductionAmount = new BigDecimal(235.00);
//        } else if (totalgrossamount > 45000 && totalgrossamount < 60001) {
//            pfdeductionAmount = new BigDecimal(510.00);
//        } else if (totalgrossamount > 60000 && totalgrossamount < 75001) {
//            pfdeductionAmount = new BigDecimal(760.00);
//        }


        Criteria ptaxSlapCrit = session.createCriteria(Professionaltaxslap.class);
        ptaxSlapCrit.add(Restrictions.sqlRestriction("deductioncode='L31'"));
        ptaxSlapCrit.add(Restrictions.sqlRestriction("regioncode='" + LoggedInRegion + "'"));
        ptaxSlapCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        ptaxSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + amount));
        ptaxSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + amount));
        List ptaxSlapList = ptaxSlapCrit.list();

        if (ptaxSlapList.size() > 0) {

            Professionaltaxslap professionaltaxslapObj = (Professionaltaxslap) ptaxSlapList.get(0);
            professionaltaxslapObj.getAmount();
            if (professionaltaxslapObj.getAmount().equals(new BigDecimal("0.00"))) {
                float perc = professionaltaxslapObj.getPercentage().floatValue() / 100;
                amount = amount.multiply(new BigDecimal(perc));
                amount = amount.setScale(2, RoundingMode.UP);
                pfdeductionAmount = amount.setScale(0, RoundingMode.HALF_UP);



            } else {
                pfdeductionAmount = professionaltaxslapObj.getAmount();
            }
        }
        return pfdeductionAmount;
    }
}
