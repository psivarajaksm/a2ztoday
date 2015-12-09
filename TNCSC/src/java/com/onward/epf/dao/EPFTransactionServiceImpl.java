/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.epf.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import com.onward.persistence.payroll.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Karthikeyan.S
 */
public class EPFTransactionServiceImpl extends OnwardAction implements EPFTransactionService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEpfTransaction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        resultMap.put("epfdetails", getRecordedDetailsInHTML(session, month, year, LoggedInRegion).toString());
        return resultMap;
    }

    public String getRecordedDetailsInHTML(Session session, String month, String year, String LoggedInRegion) {
        StringBuffer resultHTML = new StringBuffer();
        String classname = "";
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            resultHTML.append("<FONT SIZE=2>");
            resultHTML.append("<table  width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            resultHTML.append("<thead>");
            resultHTML.append("<tr>");
            resultHTML.append("<td>S.No</td>");
            resultHTML.append("<td>Month & Year</td>");
            resultHTML.append("<td>Epf No</td>");
            resultHTML.append("<td>Employee Name</td>");
            resultHTML.append("<td>salary</td>");
            resultHTML.append("<td>Epfwhole</td>");
            resultHTML.append("<td>FBF</td>");
            resultHTML.append("<td>RL</td>");
            resultHTML.append("<td>VPF</td>");
            resultHTML.append("<td>ECPF</td>");
            resultHTML.append("<td>ECFB</td>");
            resultHTML.append("<td>NRL</td>");
            resultHTML.append("<td>Subscription</td>");
            resultHTML.append("<td>Contributions</td>");
            resultHTML.append("<td>Emp Category</td>");
            resultHTML.append("<td>Payroll Category</td>");
            resultHTML.append("<td>Modify</td>");
            resultHTML.append("</tr>");
            resultHTML.append("</thead>");
            resultHTML.append("<tbody>");

            Criteria Crit = session.createCriteria(Employeeprovidentfundothers.class);
            Crit.add(Restrictions.sqlRestriction("month=" + month));
            Crit.add(Restrictions.sqlRestriction("year=" + year));
            Crit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            Crit.add(Restrictions.sqlRestriction("cancelled is false "));
            Crit.addOrder(Order.asc("payrollcategory"));
            List fundsList = Crit.list();
            System.out.println("==" + fundsList.size());
            if (fundsList.size() > 0) {
                for (int i = 0; i < fundsList.size(); i++) {
                    Employeeprovidentfundothers fundsObj = null;
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    fundsObj = (Employeeprovidentfundothers) fundsList.get(i);
//                    Designationmaster designationObj = (Designationmaster) designationList.get(i);
                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    if (fundsObj.getPayrollcategory().equalsIgnoreCase("S") || fundsObj.getPayrollcategory().equalsIgnoreCase("F")) {

                        resultHTML.append("<td align=\"left\">" + monthName[fundsObj.getSmonth() - 1] + "'" + fundsObj.getSyear() + "</td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + monthName[fundsObj.getMonth() - 1] + "'" + fundsObj.getYear() + "</td>");
                    }

                    resultHTML.append("<td align=\"left\">" + fundsObj.getEmployeemaster().getEpfno() + "</td>");
                    resultHTML.append("<td align=\"left\">" + fundsObj.getEmployeemaster().getEmployeename() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getSalary() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getEpfwhole() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getFbf() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getRl() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getVpf() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getEcpf() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getEcfb() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getNrl() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getSubs() + "</td>");
                    resultHTML.append("<td align=\"right\">" + fundsObj.getContributions() + "</td>");
                    resultHTML.append("<td align=\"center\">" + fundsObj.getEmpcategory() + "</td>");
                    resultHTML.append("<td align=\"center\">" + fundsObj.getPayrollcategory() + "</td>");
                    if (fundsObj.getPayrollcategory().equalsIgnoreCase("M")) {
                        resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"epfid\" id=\"" + fundsObj.getId() + "\" onclick=\"setEPFId('" + fundsObj.getId() + "')\"></td>");
                    } else {
                        resultHTML.append("<td align=\"center\"> </td>");
                    }

                    resultHTML.append("</tr>");
                }

            }
            resultHTML.append("</tbody>");
            resultHTML.append("<tfoot>");

//            resultHTML.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"manualepf\" id=\"manualepf\" value=\"Add\"  onclick=\"showApplicationReceiptEntryForm();\"  >" + "</td>");

            resultHTML.append("<td colspan=\"5\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"manualepf\" id=\"manualepf\" value=\"Add\"  onclick=\"addManualEPF();\"  >" + "</td>");

            resultHTML.append("</tfoot>");
            resultHTML.append("</table>");
            resultHTML.append("</FONT>");



        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDetailsForModification(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfId) {
        Map resultMap = new HashMap();
        Criteria epfCrit = session.createCriteria(Employeeprovidentfundothers.class);
        epfCrit.add(Restrictions.sqlRestriction("id = '" + epfId + "' "));
//        epfCrit.add(Restrictions.sqlRestriction("payrollcategory = '" + "F" + "' "));
        List epfList = epfCrit.list();
        if (epfList.size() > 0) {

            Employeeprovidentfundothers epfObj = (Employeeprovidentfundothers) epfList.get(0);
            resultMap.put("epfno", epfObj.getEmployeemaster().getEpfno());
            resultMap.put("employeename", epfObj.getEmployeemaster().getEmployeename());
            resultMap.put("salary", epfObj.getSalary());
            resultMap.put("epfwhole", epfObj.getEpfwhole());
            resultMap.put("fbf", epfObj.getFbf());
            resultMap.put("rl", epfObj.getRl());
            resultMap.put("vpf", epfObj.getVpf());
            resultMap.put("ecpf", epfObj.getEcpf());
            resultMap.put("ecfb", epfObj.getEcfb());
            resultMap.put("nrl", epfObj.getNrl());
            resultMap.put("epfid", epfObj.getId());
            if (epfObj.getPayrollcategory().equalsIgnoreCase("S")) {
                resultMap.put("mmonth", epfObj.getSmonth());
                resultMap.put("myear", epfObj.getSyear());
            } else {
                resultMap.put("mmonth", epfObj.getMonth());
                resultMap.put("myear", epfObj.getYear());
            }

            resultMap.put("empcategory", epfObj.getEmpcategory());
            resultMap.put("payrollcategory", epfObj.getPayrollcategory());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfId) {
        Map resultMap = new HashMap();
        Employeemaster empObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Employeemaster.class);
            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + epfId + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                empObj = (Employeemaster) ldList.get(0);
                resultMap.put("employeename", empObj.getEmployeename());
                resultMap.put("empcategory", empObj.getCategory());
                resultMap.put("fbfno", empObj.getFpfno());
                resultMap.put("fathername", empObj.getFathername());
                resultMap.put("dateofbirth", dateToString(empObj.getDateofbirth()));
                resultMap.put("empcategory", empObj.getCategory());
                resultMap.put("designation", getDesignationMater(session, empObj.getDesignation()).getDesignation());
                resultMap.put("region", empObj.getRegion());


            } else {
                resultMap.put("ERROR", "Given Employee EPF Number is Wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeEPF(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfid, String epfnumber, String salary, String epfwhole, String fbr, String rl, String vpf, String epf, String fbf, String nrc, String smonth, String syear, String empcategory, String payrollcategory) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
//        System.out.println("==" + epfid + "---" + epfnumber + "---" + salary + "---" + epfwhole + "---" + fbr + "---" + rl + "---" + vpf + "---" + epf + "---" + fbf + "---" + nrc);
        try {
            if (epfid.equalsIgnoreCase("0")) {
                Employeeprovidentfundothers epfObj = new Employeeprovidentfundothers();
                epfObj.setId(getMaxSeqNumberEPF(session, LoggedInRegion));
                epfObj.setEmployeemaster(getEmployeemaster(session, epfnumber));
                epfObj.setSalary(StringtobigDecimal(salary));
                epfObj.setEpfwhole(StringtobigDecimal(epfwhole));
                epfObj.setFbf(StringtobigDecimal(fbr));
                epfObj.setRl(StringtobigDecimal(rl));
                epfObj.setVpf(StringtobigDecimal(vpf));
                epfObj.setEcpf(StringtobigDecimal(epf));
                epfObj.setEcfb(StringtobigDecimal(fbf));
                epfObj.setNrl(StringtobigDecimal(nrc));
                epfObj.setSubs(StringtobigDecimal(epfwhole));
                epfObj.setContributions(StringtobigDecimal(epfwhole));
//                epfObj.setPayrollcategory("M");
                transaction = session.beginTransaction();
                epfObj.setMonth(Integer.parseInt(smonth.toString()));
                epfObj.setYear(Integer.parseInt(syear.toString()));
                epfObj.setCancelled(Boolean.FALSE);
                epfObj.setSupprocessid(null);
                epfObj.setRegprocessid(null);
                epfObj.setCreatedby(LoggedInUser);
                epfObj.setCreateddate(getCurrentDate());
                epfObj.setPayrollcategory(payrollcategory);
                epfObj.setEmpcategory(empcategory);
                epfObj.setAccregion(LoggedInRegion);
                session.save(epfObj);
                transaction.commit();
                resultMap.put("message", "Successfully Saved.");
            } else {
                Criteria epfCrit = session.createCriteria(Employeeprovidentfundothers.class);
                epfCrit.add(Restrictions.sqlRestriction("id = '" + epfid + "' "));
                List epfList = epfCrit.list();
                if (epfList.size() > 0) {
                    Employeeprovidentfundothers epfObj = (Employeeprovidentfundothers) epfList.get(0);
//                System.out.println("epfObj==" + epfObj.getEmployeemaster().getEpfno());
                    epfObj.setEmployeemaster(getEmployeemaster(session, epfnumber));
                    epfObj.setSalary(StringtobigDecimal(salary));
                    epfObj.setEpfwhole(StringtobigDecimal(epfwhole));
                    epfObj.setFbf(StringtobigDecimal(fbr));
                    epfObj.setRl(StringtobigDecimal(rl));
                    epfObj.setVpf(StringtobigDecimal(vpf));
                    epfObj.setEcpf(StringtobigDecimal(epf));
                    epfObj.setEcfb(StringtobigDecimal(fbf));
                    epfObj.setNrl(StringtobigDecimal(nrc));
                    epfObj.setSubs(StringtobigDecimal(epfwhole));
                    epfObj.setContributions(StringtobigDecimal(epfwhole));
//                    epfObj.setPayrollcategory("M");
                    transaction = session.beginTransaction();
                    epfObj.setMonth(Integer.parseInt(smonth.toString()));
                    epfObj.setYear(Integer.parseInt(syear.toString()));
                    epfObj.setCancelled(Boolean.FALSE);
                    epfObj.setSupprocessid(null);
                    epfObj.setRegprocessid(null);
                    epfObj.setCreatedby(LoggedInUser);
                    epfObj.setCreateddate(getCurrentDate());
                    epfObj.setAccregion(LoggedInRegion);
                    epfObj.setEmpcategory(empcategory);
                    session.update(epfObj);
                    transaction.commit();
                    resultMap.put("message", "Successfully Modified.");
//                resultMap.put("recordedDetails", getRecordedDetailsInHTML(session, String.valueOf(epfObj.getMonth()), String.valueOf(epfObj.getYear()), epfObj.getEmpcategory(),LoggedInRegion).toString());
                }
            }
            resultMap.put("epfdetails", getRecordedDetailsInHTML(session, smonth, syear, LoggedInRegion).toString());

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction Failed");
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return resultMap;
    }

    public Employeemaster getEmployeemaster(Session session, String epfno) {

        Employeemaster employeemasterObj = null;

        try {

            Criteria lrCrit = session.createCriteria(Employeemaster.class);

//            lrCrit.add(Restrictions.sqlRestriction("region = '" + LoggedInRegion + "' "));

            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));

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

    public synchronized String getMaxSeqNumberEPF(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeprovidentfundothersid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeprovidentfundothersid(maxNoStr);
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
    public Map getEpfLoanApplReceipt(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int i = 1;

        try {


            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Region</th>");
            stringBuff.append("<th width=\"5%\">EPF Number</th>");
            stringBuff.append("<th width=\"5%\">Employee Name</th>");
            stringBuff.append("<th width=\"5%\">Designation</th>");
            stringBuff.append("<th width=\"5%\">Appl Date</th>");
            stringBuff.append("<th width=\"5%\">Loan Type</th>");
            stringBuff.append("<th width=\"5%\">Loan Amount</th>");
            stringBuff.append("<th width=\"5%\">No of Installments</th>");
            stringBuff.append("<th width=\"5%\">Modify</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria loanAppCrit = session.createCriteria(Epfloanapplication.class);
            loanAppCrit.add(Restrictions.sqlRestriction("month=" + month));
            loanAppCrit.add(Restrictions.sqlRestriction("year=" + year));
            loanAppCrit.add(Restrictions.sqlRestriction("status='NEW'"));
            List<Epfloanapplication> loanAppList = loanAppCrit.list();
            if (loanAppList.size() > 0) {
                for (Epfloanapplication epfloanapplicationObj : loanAppList) {
                    Employeemaster employeemasterObj = null;
                    employeemasterObj = getEmployeemaster(session, epfloanapplicationObj.getEmployeeepfmaster().getEpfno());
                    stringBuff.append("<tr>");
                    stringBuff.append("<td>" + i + "</td>");
                    if (epfloanapplicationObj.getRegionmaster() != null) {
                        stringBuff.append("<td>" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEpfno() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEmployeename() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + dateToString(epfloanapplicationObj.getDate()) + "</td>");
                    if (epfloanapplicationObj.getEpfloantypes() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEpfloantypes().getName() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + epfloanapplicationObj.getLoanamount() + "</td>");
                    stringBuff.append("<td>" + epfloanapplicationObj.getNoofinstallment() + "</td>");
                    stringBuff.append("<td align=\"center\"><input type=\"radio\" name=\"epfid\" id=\"" + epfloanapplicationObj.getId() + "\" onclick=\"modifyEPFLoanApplicationReceiptForm('" + epfloanapplicationObj.getId() + "')\"></td>");
                    stringBuff.append("</tr>");
                    i++;
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr>");
            stringBuff.append("<td colspan=\"9\" align=\"center\" ><input type=\"button\" CLASS=\"submitbu\" name=\"loanappadd\" id=\"loanappadd\" value=\"Add Loan\"  onclick=\"showApplicationReceiptEntryForm();\"  ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
//            stringBuff.append("<input type=\"button\" CLASS=\"submitbu\" name=\"loanappadd\" id=\"loanappadd\" value=\"Add\"  onclick=\"showApplicationReceiptEntryForm();\"  >");
            stringBuff.append("</FONT>");

            resultMap.put("loanreceiptdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEpfLoanApplications(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int i = 1;

        try {
            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Region</th>");
            stringBuff.append("<th width=\"5%\">EPF Number</th>");
            stringBuff.append("<th width=\"5%\">Employee Name</th>");
            stringBuff.append("<th width=\"5%\">Designation</th>");
            stringBuff.append("<th width=\"5%\">Appl Date</th>");
            stringBuff.append("<th width=\"5%\">Loan Type</th>");
            stringBuff.append("<th width=\"5%\">Loan Amount</th>");
            stringBuff.append("<th width=\"5%\">View WorkingSheet</th>");
            stringBuff.append("<th width=\"5%\">Modify</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria loanAppCrit = session.createCriteria(Epfloanapplication.class);
            loanAppCrit.add(Restrictions.sqlRestriction("month=" + month));
            loanAppCrit.add(Restrictions.sqlRestriction("year=" + year));
            List<Epfloanapplication> loanAppList = loanAppCrit.list();
            if (loanAppList.size() > 0) {
                for (Epfloanapplication epfloanapplicationObj : loanAppList) {

                    stringBuff.append("<tr>");
                    stringBuff.append("<td>" + i + "</td>");
                    if (epfloanapplicationObj.getRegionmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getRegionmaster().getRegionname() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEpfno() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEmployeename() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + getDesignationMater(session, getEmployeemaster(session, epfloanapplicationObj.getEmployeeepfmaster().getEpfno()).getDesignation()) + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + epfloanapplicationObj.getDate() + "</td>");
                    if (epfloanapplicationObj.getEpfloantypes() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEpfloantypes().getName() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + epfloanapplicationObj.getLoanamount() + "</td>");
                    stringBuff.append("<td></td>");
                    stringBuff.append("<td align=\"center\"><input type=\"radio\" name=\"epfid\" id=\"" + epfloanapplicationObj.getId() + "\" onclick=\"approvalForm('" + epfloanapplicationObj.getId() + "')\"></td>");
                    stringBuff.append("</tr>");
                    i++;
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
//            stringBuff.append("<input type=\"button\" CLASS=\"submitbu\" name=\"loanappadd\" id=\"loanappadd\" value=\"Add\"  onclick=\"showApplicationReceiptEntryForm();\"  >");
            stringBuff.append("</FONT>");

            resultMap.put("loanreceiptdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEpfLoanApplicationsForWorkingSheet(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int i = 1;
        try {

            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Region</th>");
            stringBuff.append("<th width=\"5%\">EPF Number</th>");
            stringBuff.append("<th width=\"5%\">Employee Name</th>");
            stringBuff.append("<th width=\"5%\">Designation</th>");
            stringBuff.append("<th width=\"5%\">Appl Date</th>");
            stringBuff.append("<th width=\"5%\">Loan Type</th>");
            stringBuff.append("<th width=\"5%\">Loan Amount</th>");
            stringBuff.append("<th width=\"5%\">No of Installments</th>");
            stringBuff.append("<th width=\"5%\"><input type=\"checkbox\" id=\"loanall\" name=\"loanall\" value=\"\" onclick=\"checkAll(this.value)\" /></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria loanAppCrit = session.createCriteria(Epfloanapplication.class);
            loanAppCrit.add(Restrictions.sqlRestriction("month=" + month));
            loanAppCrit.add(Restrictions.sqlRestriction("year=" + year));
            loanAppCrit.add(Restrictions.sqlRestriction("status='NEW'"));
            loanAppCrit.add(Restrictions.sqlRestriction("isfinalsettlement is false"));
            List<Epfloanapplication> loanAppList = loanAppCrit.list();
            if (loanAppList.size() > 0) {
                for (Epfloanapplication epfloanapplicationObj : loanAppList) {
                    Employeemaster employeemasterObj = null;
                    employeemasterObj = getEmployeemaster(session, epfloanapplicationObj.getEmployeeepfmaster().getEpfno());
                    stringBuff.append("<tr>");
                    stringBuff.append("<td>" + i + "</td>");
                    if (epfloanapplicationObj.getRegionmaster() != null) {
                        stringBuff.append("<td>" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEpfno() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEmployeename() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + dateToString(epfloanapplicationObj.getDate()) + "</td>");
                    if (epfloanapplicationObj.getEpfloantypes() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEpfloantypes().getName() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + epfloanapplicationObj.getLoanamount() + "</td>");
                    stringBuff.append("<td>" + epfloanapplicationObj.getNoofinstallment() + "</td>");
                    stringBuff.append("<td align=\"center\"><input type=\"checkbox\" name=\"loanappid\" id=\"loanappid" + epfloanapplicationObj.getId() + "\" value=\"" + epfloanapplicationObj.getId() + "\" /></td>");
                    stringBuff.append("</tr>");
                    i++;
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr>");
            stringBuff.append("<td colspan=\"4\">&nbsb;&nbsb;<&nbsb;&nbsb;&nbsb;/td>");
            stringBuff.append("<td align=\"center\" ><input type=\"button\" CLASS=\"submitbutton\" name=\"workingsheet\" id=\"workingsheet\" value=\"Loan Working Sheet\"></td>");
            stringBuff.append("<td colspan=\"4\">&nbsb;&nbsb;<&nbsb;&nbsb;&nbsb;/td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
//            stringBuff.append("<input type=\"button\" CLASS=\"submitbu\" name=\"loanappadd\" id=\"loanappadd\" value=\"Add\"  onclick=\"showApplicationReceiptEntryForm();\"  >");
            stringBuff.append("</FONT>");

            resultMap.put("loanreceiptdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEpfLoanTransaction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int i = 1;
        try {

            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Region</th>");
            stringBuff.append("<th width=\"5%\">EPF Number</th>");
            stringBuff.append("<th width=\"5%\">Employee Name</th>");
            stringBuff.append("<th width=\"5%\">Designation</th>");
            stringBuff.append("<th width=\"5%\">Appl Date</th>");
            stringBuff.append("<th width=\"5%\">Loan Type</th>");
            stringBuff.append("<th width=\"5%\">Loan Amount</th>");
            stringBuff.append("<th width=\"5%\"><input type=\"checkbox\" id=\"loanall\" name=\"loanall\" value=\"\" onclick=\"checkAll(this.value)\" /></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria loanAppCrit = session.createCriteria(Epfloanapplication.class);
            loanAppCrit.add(Restrictions.sqlRestriction("month=" + month));
            loanAppCrit.add(Restrictions.sqlRestriction("year=" + year));
            List<Epfloanapplication> loanAppList = loanAppCrit.list();
            if (loanAppList.size() > 0) {
                for (Epfloanapplication epfloanapplicationObj : loanAppList) {
                    Employeemaster employeemasterObj = null;
                    employeemasterObj = getEmployeemaster(session, epfloanapplicationObj.getEmployeeepfmaster().getEpfno());
                    stringBuff.append("<tr>");
                    stringBuff.append("<td>" + i + "</td>");
                    if (epfloanapplicationObj.getRegionmaster() != null) {
                        stringBuff.append("<td>" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEpfno() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEmployeename() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + dateToString(epfloanapplicationObj.getDate()) + "</td>");
                    if (epfloanapplicationObj.getEpfloantypes() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEpfloantypes().getName() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + epfloanapplicationObj.getLoanamount() + "</td>");
                    stringBuff.append("<td align=\"center\"><input type=\"checkbox\" name=\"loanappid\" id=\"loanappid" + epfloanapplicationObj.getId() + "\" value=\"" + epfloanapplicationObj.getId() + "\" /></td>");
                    stringBuff.append("</tr>");
                    i++;
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr>");
            stringBuff.append("<td colspan=\"9\" align=\"center\" ><input type=\"button\" CLASS=\"submitbu\" name=\"loantrasaction\" id=\"loantrasaction\" value=\"Process\"  ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
//            stringBuff.append("<input type=\"button\" CLASS=\"submitbu\" name=\"loanappadd\" id=\"loanappadd\" value=\"Add\"  onclick=\"showApplicationReceiptEntryForm();\"  >");
            stringBuff.append("</FONT>");

            resultMap.put("loanreceiptdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEpfLoanForApproval(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int i = 1;
        try {

            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"taxtable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"5%\">Sl.No</th>");
            stringBuff.append("<th width=\"5%\">Region</th>");
            stringBuff.append("<th width=\"5%\">EPF Number</th>");
            stringBuff.append("<th width=\"5%\">Employee Name</th>");
            stringBuff.append("<th width=\"5%\">Designation</th>");
            stringBuff.append("<th width=\"5%\">Appl Date</th>");
            stringBuff.append("<th width=\"5%\">Loan Type</th>");
            stringBuff.append("<th width=\"5%\">Loan Amount</th>");
            stringBuff.append("<th width=\"5%\">Approved Amount</th>");
            stringBuff.append("<th width=\"5%\">No of Installments</th>");
            stringBuff.append("<th width=\"5%\"><input type=\"checkbox\" id=\"loanall\" name=\"loanall\" value=\"\" onclick=\"checkAll(this.value)\" /></th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria loanAppCrit = session.createCriteria(Epfloanapplication.class);
            loanAppCrit.add(Restrictions.sqlRestriction("month=" + month));
            loanAppCrit.add(Restrictions.sqlRestriction("year=" + year));
            loanAppCrit.add(Restrictions.sqlRestriction("status='WORKINGSHEET'"));
            loanAppCrit.add(Restrictions.sqlRestriction("isfinalsettlement is false"));
            List<Epfloanapplication> loanAppList = loanAppCrit.list();
            if (loanAppList.size() > 0) {
                for (Epfloanapplication epfloanapplicationObj : loanAppList) {
                    Employeemaster employeemasterObj = null;
                    employeemasterObj = getEmployeemaster(session, epfloanapplicationObj.getEmployeeepfmaster().getEpfno());
                    stringBuff.append("<tr>");
                    stringBuff.append("<td>" + i + "</td>");
                    if (epfloanapplicationObj.getRegionmaster() != null) {
                        stringBuff.append("<td>" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEpfno() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEmployeeepfmaster().getEmployeename() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    if (epfloanapplicationObj.getEmployeeepfmaster() != null) {
                        stringBuff.append("<td>" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + dateToString(epfloanapplicationObj.getDate()) + "</td>");
                    if (epfloanapplicationObj.getEpfloantypes() != null) {
                        stringBuff.append("<td>" + epfloanapplicationObj.getEpfloantypes().getName() + "</td>");
                    } else {
                        stringBuff.append("<td>" + "" + "</td>");
                    }
                    stringBuff.append("<td>" + epfloanapplicationObj.getLoanamount() + "</td>");
                    stringBuff.append("<td>" + epfloanapplicationObj.getApprovedamount() + "</td>");
                    stringBuff.append("<td>" + epfloanapplicationObj.getNoofinstallment() + "</td>");
                    stringBuff.append("<td align=\"center\"><input type=\"checkbox\" name=\"loanappid\" id=\"loanappid" + epfloanapplicationObj.getId() + "\" value=\"" + epfloanapplicationObj.getId() + "\" /></td>");
                    stringBuff.append("</tr>");
                    i++;
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("<tfoot>");
            stringBuff.append("<tr>");
            stringBuff.append("<td colspan=\"9\" align=\"center\" ><input type=\"button\" CLASS=\"submitbu\" name=\"loanapproval\" id=\"loanapproval\" value=\"Approval\" ><input type=\"button\" CLASS=\"submitbu\" name=\"loanreject\" id=\"loanreject\" value=\"Reject\" ></td>");
            stringBuff.append("</tr>");
            stringBuff.append("</tfoot>");
            stringBuff.append("</table>");
//            stringBuff.append("<input type=\"button\" CLASS=\"submitbu\" name=\"loanappadd\" id=\"loanappadd\" value=\"Add\"  onclick=\"showApplicationReceiptEntryForm();\"  >");
            stringBuff.append("</FONT>");

            resultMap.put("loanreceiptdetails", stringBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveLoanApplicationReceiptEntry(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfloanappreceiptid, String epfnumber, String applicationdate, String loantype, String loanamount, String tapalno, String month, String myear, String isfinalsettlement, String loaninstallment) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            if (epfloanappreceiptid.trim().length() > 0) {
                Epfloanapplication epfloanapplicationObj = CommonUtility.getEpfloanapplication(session, epfloanappreceiptid.trim());
                epfloanapplicationObj.setDate(postgresDate(applicationdate));
                epfloanapplicationObj.setEmployeeepfmaster(CommonUtility.getEmployeeepfmaster(session, epfnumber));
                epfloanapplicationObj.setEpfloantypes(CommonUtility.getEpfloantypes(session, loantype));
                if (isfinalsettlement.equalsIgnoreCase("N")) {
                    epfloanapplicationObj.setLoanamount(new BigDecimal(loanamount));
                    epfloanapplicationObj.setNoofinstallment(Long.parseLong(loaninstallment));
                    epfloanapplicationObj.setIsfinalsettlement(Boolean.FALSE);
                } else {
                    epfloanapplicationObj.setLoanamount(new BigDecimal(0.00));
                    epfloanapplicationObj.setNoofinstallment(0l);
                    epfloanapplicationObj.setIsfinalsettlement(Boolean.TRUE);
                }
                epfloanapplicationObj.setStatus("NEW");
                epfloanapplicationObj.setMonth(Long.parseLong(month));
                epfloanapplicationObj.setTapalno(tapalno);
                epfloanapplicationObj.setYear(Long.parseLong(myear));
                epfloanapplicationObj.setRegionmaster(getRegionmaster(session, LoggedInRegion));
                transaction = session.beginTransaction();
                session.update(epfloanapplicationObj);
                transaction.commit();


            } else {

                Epfloanapplication epfloanapplicationObj = new Epfloanapplication();
                epfloanapplicationObj.setId(getMaxSeqNumberEpfloanapplication(session, LoggedInRegion));
                // epfloanapplicationObj.setAccountingyear(CommonUtility.getAccountingyear(session, myear));
                epfloanapplicationObj.setDate(postgresDate(applicationdate));
                Employeeepfmaster effMast = CommonUtility.getEmployeeepfmaster(session, epfnumber);
                epfloanapplicationObj.setEmployeeepfmaster(effMast);
                epfloanapplicationObj.setEpfloantypes(CommonUtility.getEpfloantypes(session, loantype));
                if (isfinalsettlement.equalsIgnoreCase("N")) {
                    epfloanapplicationObj.setLoanamount(new BigDecimal(loanamount));
                    epfloanapplicationObj.setNoofinstallment(Long.parseLong(loaninstallment));
                    epfloanapplicationObj.setIsfinalsettlement(Boolean.FALSE);
                } else {
                    epfloanapplicationObj.setLoanamount(new BigDecimal(0.00));
                    epfloanapplicationObj.setNoofinstallment(0l);
                    epfloanapplicationObj.setIsfinalsettlement(Boolean.TRUE);
                }
                epfloanapplicationObj.setMonth(Long.parseLong(month));
                epfloanapplicationObj.setStatus("NEW");
                epfloanapplicationObj.setTapalno(tapalno);
                epfloanapplicationObj.setYear(Long.parseLong(myear));
                epfloanapplicationObj.setRegionmaster(getRegionmaster(session, LoggedInRegion));
                transaction = session.beginTransaction();
                session.save(epfloanapplicationObj);
                transaction.commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultMap;
    }

    public synchronized String getMaxSeqNumberEpfloanapplication(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEpfloanapplicationid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEpfloanapplicationid(maxNoStr);
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
    public Map loadLoanTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map loanMap = new LinkedHashMap();
        Map regionMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        try {
            Criteria rgnCrit = session.createCriteria(Epfloantypes.class);
            List<Epfloantypes> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Epfloantypes lbobj : rgnList) {
                loanMap.put(lbobj.getId(), lbobj.getName());
            }
            resultMap.put("epfloantypes", loanMap);


            Criteria rgnCrit1 = session.createCriteria(Regionmaster.class);
            rgnCrit1.addOrder(Order.asc("regionname"));
            List<Regionmaster> rgnList1 = rgnCrit1.list();
//            resultMap = new TreeMap();
            for (Regionmaster lbobj1 : rgnList1) {
                regionMap.put(lbobj1.getId(), lbobj1.getRegionname());

            }
            resultMap.put("regionlist", regionMap);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEPFLoanApplicationReceiptDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfloanid) {
        Map resultMap = new HashMap();
        Epfloanapplication empObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Epfloanapplication.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + epfloanid + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                empObj = (Epfloanapplication) ldList.get(0);
                resultMap.put("epfnumber", empObj.getEmployeeepfmaster().getEpfno());
                resultMap.put("applicationdate", dateToString(empObj.getDate()));
                resultMap.put("loantype", empObj.getEpfloantypes().getId());
                resultMap.put("loanamount", empObj.getLoanamount());
                resultMap.put("tapalno", empObj.getTapalno());
                if (empObj.getIsfinalsettlement()) {
                    resultMap.put("isfinalsettle", "Y");
                } else {
                    resultMap.put("isfinalsettle", "N");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Employeemaster empMastObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Employeemaster.class);
            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + empObj.getEmployeeepfmaster().getEpfno() + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                empMastObj = (Employeemaster) ldList.get(0);
                resultMap.put("employeename", empMastObj.getEmployeename());
                resultMap.put("empcategory", empMastObj.getCategory());
                resultMap.put("designation", getDesignationMater(session, empMastObj.getDesignation()).getDesignation());
                resultMap.put("region", empMastObj.getRegion());
                resultMap.put("fbfno", empMastObj.getFpfno());
                resultMap.put("fathername", empMastObj.getFathername());
                resultMap.put("dateofbirth", dateToString(empMastObj.getDateofbirth()));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getWorkingSheet(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String loanidsArr) {
        Map resultMap = new HashMap();
        Transaction transaction;
        String[] strArray = null;
        strArray = loanidsArr.split(java.util.regex.Pattern.quote("-"));
//        String challanId = SequenceNumberGenerator.getMaxSeqNumberBankChallan(session, LoggedInRegion);
        transaction = session.beginTransaction();

        try {
            for (String loanid : strArray) {
                Criteria loanCrit = session.createCriteria(Epfloanapplication.class);
                loanCrit.add(Restrictions.sqlRestriction("id='" + loanid + "'"));
                List loanList = loanCrit.list();
                if (loanList.size() > 0) {
                    Epfloanapplication epfloanapplicationObj = (Epfloanapplication) loanList.get(0);
                    epfloanapplicationObj.setStatus("WORKINGSHEET");
                    session.merge(epfloanapplicationObj);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return resultMap;
    }
}
