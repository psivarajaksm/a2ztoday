/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.persistence.payroll.Employeemaster;
import com.onward.persistence.payroll.Employeeprovidentfundothers;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.reports.fundsup.FundsUpEPFformDBFReport;
import com.onward.reports.fundsup.FundsUpEPFformReport;
import com.onward.valueobjects.EpfModel;
import com.onward.valueobjects.EpfModelObject;
import com.onward.valueobjects.PaySlipModel;
import java.io.FileWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SQLQuery;

/**
 *
 * @author root
 */
public class EmployeeFundSubServiceImpl extends OnwardAction implements EmployeeFundSubService {

    String classname = "";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String employeetype) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "'"));
        empDetailsCrit.add(Restrictions.sqlRestriction("category = '" + employeetype + "'"));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong or Employee Type is Wrong. ");

        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRecordedDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeetype, String month, String year) {
        Map resultMap = new HashMap();
        resultMap.put("recordedDetails", getRecordedDetailsInHTML(session, month, year, employeetype, LoggedInRegion).toString());
        return resultMap;
    }

//    @GlobalDBOpenCloseAndUserPrivilages
    public String getRecordedDetailsInHTML(Session session, String smonth, String syear, String employeetype, String LoggedInRegion) {
        StringBuffer resultHTML = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int currnetmonth = month + 1;

        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {

            Criteria Crit = session.createCriteria(Employeeprovidentfundothers.class);
            if (!employeetype.equalsIgnoreCase("0")) {
                Crit.add(Restrictions.sqlRestriction("month=" + smonth));
                Crit.add(Restrictions.sqlRestriction("year=" + syear));
                if (!employeetype.equalsIgnoreCase("ALL")) {
                    Crit.add(Restrictions.sqlRestriction("empcategory='" + employeetype.toUpperCase() + "'"));
                }
            } else {
                Crit.add(Restrictions.sqlRestriction("month=" + currnetmonth));
                Crit.add(Restrictions.sqlRestriction("year=" + year));
            }
            Crit.add(Restrictions.sqlRestriction("payrollcategory='" + "F" + "'"));
            Crit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            List fundsList = Crit.list();
            System.out.println("==" + fundsList.size());
            if (fundsList.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">");
                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr class=\"gridmenu\">");
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
                resultHTML.append("<td>Modify</td>");
                resultHTML.append("</tr>");
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
                    resultHTML.append("<td align=\"center\">" + monthName[fundsObj.getMonth() - 1] + "'" + fundsObj.getYear() + "</td>");
                    resultHTML.append("<td align=\"center\">" + fundsObj.getEmployeemaster().getEpfno() + "</td>");
                    resultHTML.append("<td align=\"center\">" + fundsObj.getEmployeemaster().getEmployeename() + "</td>");
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
                    resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"epfid\" id=\"" + fundsObj.getId() + "\" onclick=\"setEPFId('" + fundsObj.getId() + "')\"></td>");
                    resultHTML.append("</tr>");
                }

            } else {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">");
                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr class=\"gridmenu\">");
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
                resultHTML.append("</tr>");
                resultHTML.append("</table>");
                resultHTML.append("</td></tr>");
            }
            resultHTML.append("</table>");

//            resultMap.put("recordedDetails", resultHTML.toString());
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
        epfCrit.add(Restrictions.sqlRestriction("payrollcategory = '" + "F" + "' "));
        List epfList = epfCrit.list();
        if (epfList.size() > 0) {

            Employeeprovidentfundothers epfObj = (Employeeprovidentfundothers) epfList.get(0);
            resultMap.put("epfno", epfObj.getEmployeemaster().getEpfno());
            resultMap.put("salary", epfObj.getSalary());
            resultMap.put("epfwhole", epfObj.getEpfwhole());
            resultMap.put("fbf", epfObj.getFbf());
            resultMap.put("rl", epfObj.getRl());
            resultMap.put("vpf", epfObj.getVpf());
            resultMap.put("ecpf", epfObj.getEcpf());
            resultMap.put("ecfb", epfObj.getEcfb());
            resultMap.put("nrl", epfObj.getNrl());
            resultMap.put("epfid", epfObj.getId());
            resultMap.put("mmonth", epfObj.getSmonth());
            resultMap.put("myear", epfObj.getSyear());
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map modifyEmployeeEPF(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfid, String epfnumber, String salary, String epfwhole, String fbr, String rl, String vpf, String epf, String fbf, String nrc, String smonth, String syear) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
//        System.out.println("==" + epfid + "---" + epfnumber + "---" + salary + "---" + epfwhole + "---" + fbr + "---" + rl + "---" + vpf + "---" + epf + "---" + fbf + "---" + nrc);
        try {
            Criteria epfCrit = session.createCriteria(Employeeprovidentfundothers.class);
            epfCrit.add(Restrictions.sqlRestriction("id = '" + epfid + "' "));
            List epfList = epfCrit.list();
            if (epfList.size() > 0) {
                Employeeprovidentfundothers epfObj = (Employeeprovidentfundothers) epfList.get(0);
//                System.out.println("epfObj==" + epfObj.getEmployeemaster().getEpfno());
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
                epfObj.setPayrollcategory("F");
                transaction = session.beginTransaction();
                epfObj.setSmonth(Integer.parseInt(smonth.toString()));
                epfObj.setSyear(Integer.parseInt(syear.toString()));
                epfObj.setCancelled(Boolean.FALSE);
                epfObj.setSupprocessid(null);
                epfObj.setRegprocessid(null);
                epfObj.setCreatedby(LoggedInUser);
                epfObj.setCreateddate(getCurrentDate());
                session.update(epfObj);
                transaction.commit();
                resultMap.put("message", "Successfully Modified.");
                resultMap.put("recordedDetails", getRecordedDetailsInHTML(session, String.valueOf(epfObj.getMonth()), String.valueOf(epfObj.getYear()), epfObj.getEmpcategory(), LoggedInRegion).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction Failed");
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeEPF(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String employeetype, String epfno, String employeename, String salaryamt, String smonth, String syear) {
        Map resultMap = new HashMap();
        double epf = 0;
        String emty = "0.00";
        Transaction transaction = null;

        try {

            epf = (Double.parseDouble(salaryamt) * 12) / 100;

            long roundsalary = Math.round(Double.parseDouble(salaryamt));
            long roundepf = Math.round(epf);

            double per833 = 8.33;
            long percentage833 = Math.round((roundsalary * per833) / 100);
            if (percentage833 > 1250) {
                percentage833 = 1250;
            }
            long percentage367 = roundepf - percentage833;
//            long remarkstotal = percentage833 + percentage367;

            Employeeprovidentfundothers epfObj = new Employeeprovidentfundothers();
//            System.out.println("epfObj==" + epfObj.getEmployeemaster().getEpfno());

            Employeemaster empObj = new Employeemaster();
            empObj.setEpfno(epfno);
            empObj.setEmployeename(employeename);

            epfObj.setId(getMaxSeqNumberEPF(session, LoggedInRegion));
            epfObj.setEmployeemaster(empObj);
            epfObj.setMonth(Integer.parseInt(month.toString()));
            epfObj.setYear(Integer.parseInt(year.toString()));
            epfObj.setEmpcategory(employeetype);
            epfObj.setSalary(StringtobigDecimal(String.valueOf(roundsalary)));
            epfObj.setEpfwhole(StringtobigDecimal(String.valueOf(roundepf)));
            epfObj.setFbf(StringtobigDecimal(emty));
            epfObj.setRl(StringtobigDecimal(emty));
            epfObj.setVpf(StringtobigDecimal(emty));
            epfObj.setDvpf(StringtobigDecimal(emty));
            epfObj.setNrl(StringtobigDecimal(emty));
            epfObj.setEcfb(StringtobigDecimal(String.valueOf(percentage833)));
            epfObj.setEcpf(StringtobigDecimal(String.valueOf(percentage367)));
            epfObj.setSubs(StringtobigDecimal(String.valueOf(roundepf)));
            epfObj.setContributions(StringtobigDecimal(String.valueOf(roundepf)));
            epfObj.setSmonth(Integer.parseInt(smonth.toString()));
            epfObj.setPayrollcategory("F");
            epfObj.setSyear(Integer.parseInt(syear.toString()));
            epfObj.setAccregion(LoggedInRegion);
            epfObj.setCancelled(Boolean.FALSE);
            epfObj.setSupprocessid(null);
            epfObj.setRegprocessid(null);
            epfObj.setCreatedby(LoggedInUser);
            epfObj.setCreateddate(getCurrentDate());
//            epfObj.setContributions(StringtobigDecimal(setPrecision(percentage367.to,'0.00')));
            transaction = session.beginTransaction();
            session.save(epfObj);
            transaction.commit();
            resultMap.put("message", "Successfully Saved.");
            resultMap.put("recordedDetails", getRecordedDetailsInHTML(session, month, year, employeetype, LoggedInRegion).toString());

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Transaction Failed");
        }

        return resultMap;
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
    public Map EmployeeFundsUpEPFformDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String empcategory) {
        Map map = new HashMap();
        try {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            FundsUpEPFformDBFReport epfr = new FundsUpEPFformDBFReport();
            PaySlipModel psm;
            String Querys = null;
            String Regular_Query = null;
            String Supplementary_Query = null;
            String FundsUp_Query = null;
            String FundsUp_Seasonal_Loadmen_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);            
            String sal_type = null;
            String epfcate = "";
            int slipno = 1;

            String[] Category = {"R", "S", "F"};

            Regular_Query = "SELECT epf.epfno, epf.month, epf.year, epf.salary, epf.epfwhole, epf.rl, epf.vpf, epf.dvpf, epf.ecpf, epf.ecfb, "
                    + "epf.accregion, epf.empcategory, epf.payrollcategory from employeeprovidentfundothers epf left join regionmaster rm on "
                    + "rm.id=epf.accregion left join employeemaster em on em.epfno=epf.epfno left join designationmaster dm on "
                    + "dm.designationcode=em.designation left join payrollprocessingdetails ppd on ppd.employeeprovidentfundnumber=epf.epfno "
                    + "where epf.year=" + p_year + " and epf.month=" + p_month + " and epf.cancelled is false and epf.payrollcategory='R' and epf.accregion='" + LoggedInRegion + "' "
                    + "and ppd.year = " + p_year + " and ppd.month= " + p_month + " and ppd.section not in ('S13','S14') and ppd.process is true and epf.empcategory='" + empcategory + "'";
                       
            Supplementary_Query = "select epf.epfno, epf.month, epf.year, sum(epf.salary) salary, "
                    + "sum(epf.epfwhole) epfwhole, sum(epf.rl) rl, sum(epf.vpf) vpf, sum(epf.dvpf) dvpf, sum(epf.ecpf) ecpf, "
                    + "sum(epf.ecfb) ecfb, epf.accregion, epf.empcategory, epf.payrollcategory, spb.type from employeeprovidentfundothers epf "
                    + "left join supplementarypayrollprocessingdetails sppd on sppd.id=epf.supprocessid "
                    + "left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                    + "left join employeemaster em on em.epfno=epf.epfno "
                    + "left join designationmaster dm on dm.designationcode=em.designation "
                    + "left join regionmaster rm on rm.id=epf.accregion "
                    + "where epf.empcategory='" + empcategory + "' and epf.year=" + p_year + " and "
                    + "epf.month=" + p_month + " and "
                    + "epf.payrollcategory='S' and "
                    + "epf.cancelled is false and "
                    + "epf.accregion='" + LoggedInRegion + "' and "
                    + "spb.cancelled is false and "
                    + "sppd.cancelled is false  "
                    + "group by epf.epfno, epf.month, epf.year, epf.accregion, epf.empcategory, epf.payrollcategory, spb.type "
                    + "order by spb.type,epf.epfno";

            FundsUp_Query = "SELECT epf.epfno, epf.month, epf.year, epf.salary, epf.epfwhole, epf.rl, epf.vpf, epf.dvpf, epf.ecpf, epf.ecfb, "
                    + "epf.accregion, epf.empcategory, epf.payrollcategory from employeeprovidentfundothers epf left join regionmaster rm on rm.id=epf.accregion "
                    + "left join employeemaster em on em.epfno=epf.epfno left join designationmaster dm on dm.designationcode=em.designation left join "
                    + "payrollprocessingdetails ppd on ppd.employeeprovidentfundnumber=epf.epfno where epf.year=" + p_year + " and epf.month=" + p_month + " and epf.cancelled is "
                    + "false and epf.payrollcategory='F' and epf.accregion='" + LoggedInRegion + "' and ppd.year = " + p_year + " and ppd.month= " + p_month + " and ppd.section not in ('S13','S14') "
                    + "and ppd.process is true and epf.empcategory='" + empcategory + "'";
            
            FundsUp_Seasonal_Loadmen_Query = "select rm.regionname,epf.month,epf.year,epf.epfno,em.employeename,dm.designation,epf.salary,epf.epfwhole,epf.rl,"
                    + "epf.vpf,epf.dvpf,epf.nrl,epf.ecfb,epf.ecpf, cast('MANUAL' as text) as type from employeeprovidentfundothers epf left join regionmaster rm on rm.id=epf.accregion "
                    + "left join employeemaster em on em.epfno=epf.epfno left join designationmaster dm on dm.designationcode=em.designation "
                    + " where epf.year=" + p_year + " and epf.month=" + p_month + " and "
                    + "epf.cancelled is false and epf.payrollcategory='F' and epf.accregion='" + LoggedInRegion + "' "
                    + " and epf.empcategory='" + empcategory + "'";
            
            psm = new PaySlipModel();
            if ("L".equalsIgnoreCase(empcategory)) {
                epfcate = " (MANUAL)";
                SQLQuery query = session.createSQLQuery(FundsUp_Seasonal_Loadmen_Query);
                getFundUpFormatDBF(query, psm, epfr, empcategory, sal_type, months, filePath, slipno);
            } else if ("S".equalsIgnoreCase(empcategory)) {
                epfcate = " (SUPPLEMENTARY)";
                SQLQuery query = session.createSQLQuery(FundsUp_Seasonal_Loadmen_Query);
                getFundUpFormatDBF(query, psm, epfr, empcategory, sal_type, months, filePath, slipno);
            } else {
                for (int i = 0; i < Category.length; i++) {
                    if (Category[i].equals("R")) {
                        Querys = Regular_Query;
                        sal_type = "R";
                    } else if (Category[i].equals("S")) {
                        Querys = Supplementary_Query;
                        sal_type = "S";
                    } else if (Category[i].equals("F")) {
                        Querys = FundsUp_Query;
                        sal_type = "M";
                    }
                    SQLQuery query = session.createSQLQuery(Querys);
                    getFundUpFormatDBF(query, psm, epfr, empcategory, sal_type, months, filePath, slipno);
                }
            }
            map.put("ERROR", null);

        } catch (Exception ex) {
            map.put("ERROR", "EPF Form (Flat file) Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    private void getFundUpFormatDBF(SQLQuery query, PaySlipModel psm, FundsUpEPFformDBFReport epfr, String empcategory, String sal_type, String months[], String filePath, int slipno) {
        BigDecimal Big_Salary;
        BigDecimal Big_Epf;
        BigDecimal Big_Rl;
        BigDecimal Big_Vpf;
        BigDecimal Big_Dvpf;
        BigDecimal Big_Ecpf;
        BigDecimal Big_Ecfb;
        String emp_type = null;
            
        for (ListIterator its = query.list().listIterator(); its.hasNext();) {
            psm = new PaySlipModel();
            Object[] rows = (Object[]) its.next();

            psm.setSalarytype(sal_type);
            psm.setEmployeecategory(empcategory);

            psm.setPfno((String) rows[0]);
            psm.setPayslipmonth(months[(Integer) rows[1] - 1]);
            psm.setPayslipyear(rows[2].toString());

            if (rows[3] != null) {
                Big_Salary = (BigDecimal) rows[3];
            } else {
                Big_Salary = new BigDecimal(0);
            }

            if (rows[4] != null) {
                Big_Epf = (BigDecimal) rows[4];
            } else {
                Big_Epf = new BigDecimal(0);
            }

            if (rows[5] != null) {
                Big_Rl = (BigDecimal) rows[5];
            } else {
                Big_Rl = new BigDecimal(0);
            }

            if (rows[6] != null) {
                Big_Vpf = (BigDecimal) rows[6];
            } else {
                Big_Vpf = new BigDecimal(0);
            }

            if (rows[7] != null) {
                Big_Dvpf = (BigDecimal) rows[7];
            } else {
                Big_Dvpf = new BigDecimal(0);
            }

            if (rows[8] != null) {
                Big_Ecpf = (BigDecimal) rows[8];
            } else {
                Big_Ecpf = new BigDecimal(0);
            }

            if (rows[9] != null) {
                Big_Ecfb = (BigDecimal) rows[9];
            } else {
                Big_Ecfb = new BigDecimal(0);
            }

            double Salary = Big_Salary.doubleValue();
            double Epf = Big_Epf.doubleValue();
            double Rl = Big_Rl.doubleValue();
            double Vpf = Big_Vpf.doubleValue();
            double Dvpf = Big_Dvpf.doubleValue();
            double Ecpf = Big_Ecpf.doubleValue();
            double Ecfb = Big_Ecfb.doubleValue();

            double total = Epf + Rl + Vpf + Dvpf;

            long roundsalary = Math.round(Salary);
            long roundEpf = Math.round(Epf);
            long roundRl = Math.round(Rl);
            long roundVpf = Math.round(Vpf);
            long roundDvpf = Math.round(Dvpf);
            long roundEcpf = Math.round(Ecpf);
            long roundEcfb = Math.round(Ecfb);

            psm.setSalary(String.valueOf(roundsalary));
            psm.setEpf(String.valueOf(roundEpf));
            psm.setEpfloan(String.valueOf(roundRl));
            psm.setVpf(String.valueOf(roundVpf));
            psm.setDavpf(String.valueOf(roundDvpf));
            psm.setPercentage833(String.valueOf(roundEcfb));
            psm.setPercentage367(String.valueOf(roundEcpf));
            psm.setBranch((String) rows[10]);
            if (rows[11] != null) {
                emp_type = (String) rows[11];
                if (emp_type.equals("R")) {
                    psm.setEmployeetype("RE");
                } else if (emp_type.equals("S")) {
                    psm.setEmployeetype("SE");
                } else if (emp_type.equals("L")) {
                    psm.setEmployeetype("LM");
                }
            } else {
                psm.setEmployeetype("  ");
            }

            String payrollcategory = (String) rows[12];
            if (payrollcategory.equals("S")) {
                String typename = (String) rows[13];
                if (typename.equalsIgnoreCase("INCREMENTMANUAL")) {
                    psm.setSupplementarytype("IM");
                } else if (typename.equalsIgnoreCase("INCREMENTARREAR")) {
                    psm.setSupplementarytype("IA");
                } else if (typename.equalsIgnoreCase("LEAVESURRENDER")) {
                    psm.setSupplementarytype("SL");
                } else if (typename.equalsIgnoreCase("SUPLEMENTARYBILL")) {
                    psm.setSupplementarytype("SB");
                } else if (typename.equalsIgnoreCase("DAMANUAL")) {
                    psm.setSupplementarytype("DM");
                } else if (typename.equalsIgnoreCase("DAARREAR")) {
                    psm.setSupplementarytype("DA");
                }else{
                    psm.setSupplementarytype("");
                }
            }
            if (total > 0) {
                epfr.getEPFformPrintWriter(psm, filePath);
                slipno++;
            }
        }
    }
    
    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeFundsUpEPFformDownloadPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath) {
        Map map = new HashMap();
        try {
            String Query = null;
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            EpfModel epfModelObj = null;
            String key = "pass@123";
            ArrayList<EpfModel> epfModelList = new ArrayList<EpfModel>();
            EpfModelObject epfModelObject = new EpfModelObject();

            Query = "select epf.id, epf.epfno, epf.month, epf.year, epf.salary, epf.epfwhole, epf.fbf, epf.rl, epf.vpf, epf.dvpf, epf.ecpf, epf.ecfb, "
                    + "epf.nrl, epf.subs, epf.contributions, epf.empcategory, epf.payrollcategory, epf.cancelled, epf.smonth, epf.syear, epf.accregion, "
                    + "epf.supprocessid, epf.regprocessid from employeeprovidentfundothers epf left join regionmaster rm on rm.id=epf.accregion "
                    + "left join employeemaster em on em.epfno=epf.epfno left join designationmaster dm on dm.designationcode=em.designation "
                    + "left join payrollprocessingdetails ppd on ppd.employeeprovidentfundnumber=epf.epfno where epf.year=" + p_year + " and epf.month=" + p_month + " and "
                    + "epf.accregion='" + LoggedInRegion + "' and ppd.year = " + p_year + " and ppd.month= " + p_month + " and ppd.section not in ('S13','S14')";

            SQLQuery epfquery = session.createSQLQuery(Query);

            for (ListIterator its = epfquery.list().listIterator(); its.hasNext();) {
                epfModelObj = new EpfModel();
                Object[] rows = (Object[]) its.next();

//                epfModelObj.setId(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage((String) rows[0], key)));
//                epfModelObj.setEpfno(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage((String) rows[1], key)));
//                epfModelObj.setMonth(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[2].toString(), key)));
//                epfModelObj.setYear(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[3].toString(), key)));
//                epfModelObj.setSalary(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[4].toString(), key)));
//                epfModelObj.setEpfwhole(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[5].toString(), key)));
//                epfModelObj.setFbf(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[6].toString(), key)));
//                epfModelObj.setRl(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[7].toString(), key)));;
//                epfModelObj.setVpf(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[8].toString(), key)));
//                epfModelObj.setDvpf(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[9].toString(), key)));
//                epfModelObj.setEcpf(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[10].toString(), key)));
//                epfModelObj.setEcfb(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[11].toString(), key)));
//                epfModelObj.setNrl(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[12].toString(), key)));
//                epfModelObj.setSubs(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[13].toString(), key)));
//                epfModelObj.setContributions(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[14].toString(), key)));
//                epfModelObj.setEmpcategory(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage((String) rows[15], key)));
//                epfModelObj.setPayrollcategory(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage((String) rows[16], key)));
//                epfModelObj.setCancelled(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[17].toString(), key)));
//                epfModelObj.setSmonth(rows[18] == null ? EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage("", key)) : EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[18].toString(), key)));
//                epfModelObj.setSyear(rows[19] == null ? EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage("", key)) : EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage(rows[19].toString(), key)));
//                epfModelObj.setAccregion(EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage((String) rows[20], key)));
//                epfModelObj.setSupprocessid(rows[21] == null ? EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage("", key)) : EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage((String) rows[21], key)));
//                epfModelObj.setRegprocessid(rows[22] == null ? EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage("", key)) : EncryptDecrypt.base64encode(EncryptDecrypt.xorMessage((String) rows[22], key)));

                epfModelObj.setId((String) rows[0]);
                epfModelObj.setEpfno((String) rows[1]);
                epfModelObj.setMonth(rows[2].toString());
                epfModelObj.setYear(rows[3].toString());
                epfModelObj.setSalary(rows[4].toString());
                epfModelObj.setEpfwhole(rows[5].toString());
                epfModelObj.setFbf(rows[6].toString());
                epfModelObj.setRl(rows[7].toString());
                epfModelObj.setVpf(rows[8].toString());
                epfModelObj.setDvpf(rows[9].toString());
                epfModelObj.setEcpf(rows[10].toString());
                epfModelObj.setEcfb(rows[11].toString());
                epfModelObj.setNrl(rows[12].toString());
                epfModelObj.setSubs(rows[13].toString());
                epfModelObj.setContributions(rows[14].toString());
                epfModelObj.setEmpcategory((String) rows[15]);
                epfModelObj.setPayrollcategory((String) rows[16]);
                epfModelObj.setCancelled(rows[17].toString());
                epfModelObj.setSmonth(rows[18] == null ? "" : rows[18].toString());
                epfModelObj.setSyear(rows[19] == null ? "" : rows[19].toString());
                epfModelObj.setAccregion((String) rows[20]);
                epfModelObj.setSupprocessid(rows[21] == null ? "" : (String) rows[21]);
                epfModelObj.setRegprocessid(rows[22] == null ? "" : (String) rows[22]);

                epfModelList.add(epfModelObj);

//                System.out.println("*********************************************************");
//                System.out.println("epfModelObj.getId() " + epfModelObj.getId());
//                System.out.println("epfModelObj.getEpfno() " + epfModelObj.getEpfno());
//                System.out.println("epfModelObj.getMonth() " + epfModelObj.getMonth());
//                System.out.println("epfModelObj.getYear() " + epfModelObj.getYear());
//                System.out.println("epfModelObj.getSalary() " + epfModelObj.getSalary());
//                System.out.println("epfModelObj.getEpfwhole() " + epfModelObj.getEpfwhole());
//                System.out.println("epfModelObj.getFbf() " + epfModelObj.getFbf());
//                System.out.println("epfModelObj.getRl() " + epfModelObj.getRl());
//                System.out.println("epfModelObj.getVpf() " + epfModelObj.getVpf());
//                System.out.println("epfModelObj.getDvpf() " + epfModelObj.getDvpf());
//                System.out.println("epfModelObj.getEcpf() " + epfModelObj.getEcpf());
//                System.out.println("epfModelObj.getEcfb() " + epfModelObj.getEcfb());
//                System.out.println("epfModelObj.getNrl() " + epfModelObj.getNrl());
//                System.out.println("epfModelObj.getSubs() " + epfModelObj.getSubs());
//                System.out.println("epfModelObj.getContributions() " + epfModelObj.getContributions());
//                System.out.println("epfModelObj.getEmpcategory() " + epfModelObj.getEmpcategory());
//                System.out.println("epfModelObj.getPayrollcategory() " + epfModelObj.getPayrollcategory());
//                System.out.println("epfModelObj.getCancelled() " + epfModelObj.getCancelled());
//                System.out.println("epfModelObj.getSmonth() " + epfModelObj.getSmonth());
//                System.out.println("epfModelObj.getSyear() " + epfModelObj.getSyear());
//                System.out.println("epfModelObj.getAccregion() " + epfModelObj.getAccregion());
//                System.out.println("epfModelObj.getSupprocessid() " + epfModelObj.getSupprocessid());
//                System.out.println("epfModelObj.getRegprocessid() " + epfModelObj.getRegprocessid());
//                System.out.println("*********************************************************");

            }

            epfModelObject.setEpfModelList(epfModelList);

            JAXBContext context = JAXBContext.newInstance(EpfModelObject.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(epfModelObject, System.out);

            Writer w = null;
            try {
                w = new FileWriter(filePath);
                m.marshal(epfModelObject, w);
            } finally {
                try {
                    w.close();
                    map.put("ERROR", null);
                } catch (Exception e) {
                    map.put("ERROR", "XML File Creation Error");
                }
            }

        } catch (Exception ex) {
            map.put("ERROR", "Epf XML File Creation Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeFundsUpEPFformPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String empcategory, String filePath) {
        Map map = new HashMap();
        try {
            //System.out.println("********************* EmployeeFundSubServiceImpl class EmployeeFundsUpEPFformPrintOut method is calling *****************");
            //DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            //DecimalFormat nft = new DecimalFormat("#00.###");
//            PayBillPrinter pbp = new PayBillPrinter();
            FundsUpEPFformReport epfr = new FundsUpEPFformReport();
            PaySlipModel psm=null;
            String Querys = null;
            String Regular_Query = null;
            String Supplementary_Query = null;
            String FundsUp_Query = null;
            String FundsUp_Seasonal_Loadmen_Query = null;
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            String epfcate = "";


            String[] Category = {"R", "S", "F"};

            Regular_Query = "select rm.regionname,epf.month,epf.year,epf.epfno,em.employeename,dm.designation,epf.salary,epf.epfwhole,epf.rl,"
                    + "epf.vpf,epf.dvpf,epf.nrl,epf.ecfb,epf.ecpf, cast('REGULAR' as text) as type from employeeprovidentfundothers epf left join regionmaster rm on rm.id=epf.accregion "
                    + "left join employeemaster em on em.epfno=epf.epfno left join designationmaster dm on dm.designationcode=em.designation "
                    + "left join payrollprocessingdetails ppd on ppd.employeeprovidentfundnumber=epf.epfno where epf.year=" + p_year + " and epf.month=" + p_month + " and "
                    + "epf.cancelled is false and epf.payrollcategory='R' and epf.accregion='" + LoggedInRegion + "' and ppd.year = " + p_year + " and ppd.month= " + p_month + " and "
                    + "ppd.section not in ('S13','S14') and ppd.process is true and epf.empcategory='" + empcategory + "'";

            Supplementary_Query = "select rm.regionname,epf.month,epf.year,epf.epfno,em.employeename,dm.designation,sum(epf.salary) salary, "
                    + "sum(epf.epfwhole) epfwhole, sum(epf.rl) rl, sum(epf.vpf) vpf, sum(epf.dvpf) dvpf, sum(epf.nrl) nrl, "
                    + "sum(epf.ecfb) ecfb, sum(epf.ecpf) ecpf, spb.type as type, spb.dabatch as batchno from employeeprovidentfundothers epf "
                    + "left join supplementarypayrollprocessingdetails sppd on sppd.id=epf.supprocessid "
                    + "left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid "
                    + "left join employeemaster em on em.epfno=epf.epfno "
                    + "left join designationmaster dm on dm.designationcode=em.designation "
                    + "left join regionmaster rm on rm.id=epf.accregion "
                    + "where epf.empcategory='" + empcategory + "' and " 
                    + "epf.year=" + p_year + " and "
                    + "epf.month=" + p_month + " and "
                    + "epf.payrollcategory='S' and "
                    + "epf.cancelled is false and "
                    + "epf.accregion='" + LoggedInRegion + "' and "
                    + "spb.cancelled is false and "
                    + "sppd.cancelled is false  "
                    + "group by rm.regionname,epf.month,epf.year,epf.epfno,em.employeename,dm.designation,spb.type,spb.dabatch "
                    + "order by spb.type,spb.dabatch,epf.epfno,dm.designation";

            FundsUp_Query = "select rm.regionname,epf.month,epf.year,epf.epfno,em.employeename,dm.designation,epf.salary,epf.epfwhole,epf.rl,"
                    + "epf.vpf,epf.dvpf,epf.nrl,epf.ecfb,epf.ecpf, cast('MANUAL' as text) as type from employeeprovidentfundothers epf left join regionmaster rm on rm.id=epf.accregion "
                    + "left join employeemaster em on em.epfno=epf.epfno left join designationmaster dm on dm.designationcode=em.designation "
                    + "left join payrollprocessingdetails ppd on ppd.employeeprovidentfundnumber=epf.epfno where epf.year=" + p_year + " and epf.month=" + p_month + " and "
                    + "epf.cancelled is false and epf.payrollcategory='F' and epf.accregion='" + LoggedInRegion + "' and ppd.year = " + p_year + " and ppd.month= " + p_month + " "
                    + "and ppd.section not in ('S13','S14') and epf.empcategory='" + empcategory + "'";
            
            FundsUp_Seasonal_Loadmen_Query = "select rm.regionname,epf.month,epf.year,epf.epfno,em.employeename,dm.designation,epf.salary,epf.epfwhole,epf.rl,"
                    + "epf.vpf,epf.dvpf,epf.nrl,epf.ecfb,epf.ecpf, cast('MANUAL' as text) as type from employeeprovidentfundothers epf left join regionmaster rm on rm.id=epf.accregion "
                    + "left join employeemaster em on em.epfno=epf.epfno left join designationmaster dm on dm.designationcode=em.designation "
                    + " where epf.year=" + p_year + " and epf.month=" + p_month + " and "
                    + "epf.cancelled is false and epf.payrollcategory='F' and epf.accregion='" + LoggedInRegion + "' "
                    + " and epf.empcategory='" + empcategory + "'";
            
            int slipno = 1;
            String category = null;            
            if ("L".equalsIgnoreCase(empcategory)) {
                epfcate = " (MANUAL)";
                SQLQuery query = session.createSQLQuery(FundsUp_Seasonal_Loadmen_Query);
                category = getFundUpFormat(query, psm, epfr, epfcate, "F", months, category, filePath, slipno);
            } else if ("S".equalsIgnoreCase(empcategory)) {
                epfcate = " (SUPPLEMENTARY)";
                SQLQuery query = session.createSQLQuery(FundsUp_Seasonal_Loadmen_Query);
                category = getFundUpFormat(query, psm, epfr, epfcate, "S", months, category, filePath, slipno);
            } else {
                for (int i = 0; i < Category.length; i++) {
                    if (Category[i].equals("R")) {
                        Querys = Regular_Query;
                        epfcate = " (REGULAR)";
                    } else if (Category[i].equals("S")) {
                        Querys = Supplementary_Query;
                        epfcate = " (SUPPLEMENTARY)";
                    } else if (Category[i].equals("F")) {
                        Querys = FundsUp_Query;
                        epfcate = " (MANUAL)";
                    }
                    SQLQuery query = session.createSQLQuery(Querys);
                    category = getFundUpFormat(query, psm, epfr, epfcate, Category[i], months, category, filePath, slipno);
                }
            }
            epfr.EPFformGrandTotalPrint(filePath, category);
            map.put("ERROR", null);
        } catch (Exception ex) {
            map.put("ERROR", "EPF Form (Flat file) Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }
    private String getFundUpFormat(SQLQuery query, PaySlipModel psm, FundsUpEPFformReport epfr, String epfcate, String Category, String months[], String category, String filePath, int slipno) {
        BigDecimal Big_Salary;
        BigDecimal Big_Epf;
        BigDecimal Big_Rl;
        BigDecimal Big_Vpf;
        BigDecimal Big_Dvpf;
        BigDecimal Big_Nrl;
        BigDecimal Big_Ecpf;
        BigDecimal Big_Ecfb;
        for (ListIterator its = query.list().listIterator(); its.hasNext();) {
            psm = new PaySlipModel();
            Object[] rows = (Object[]) its.next();
                        
            psm.setEpfcategory(epfcate);
            psm.setPayrollcategory(Category);
            psm.setBranch((String) rows[0]);
            psm.setPayslipmonth(months[(Integer) rows[1] - 1]);
            psm.setPayslipyear(rows[2].toString());
            psm.setSlipno(String.valueOf(slipno));
            psm.setPfno((String) rows[3]);
            psm.setEmployeename((String) rows[4]);
            psm.setDesignation((String) rows[5]);

            if (rows[6] != null) {
                Big_Salary = (BigDecimal) rows[6];
            } else {
                Big_Salary = new BigDecimal(0);
            }

            if (rows[7] != null) {
                Big_Epf = (BigDecimal) rows[7];
            } else {
                Big_Epf = new BigDecimal(0);
            }

            if (rows[8] != null) {
                Big_Rl = (BigDecimal) rows[8];
            } else {
                Big_Rl = new BigDecimal(0);
            }

            if (rows[9] != null) {
                Big_Vpf = (BigDecimal) rows[9];
            } else {
                Big_Vpf = new BigDecimal(0);
            }

            if (rows[10] != null) {
                Big_Dvpf = (BigDecimal) rows[10];
            } else {
                Big_Dvpf = new BigDecimal(0);
            }

            if (rows[11] != null) {
                Big_Nrl = (BigDecimal) rows[10];
            } else {
                Big_Nrl = new BigDecimal(0);
            }

            if (rows[12] != null) {
                Big_Ecfb = (BigDecimal) rows[12];
            } else {
                Big_Ecfb = new BigDecimal(0);
            }

            if (rows[13] != null) {
                Big_Ecpf = (BigDecimal) rows[13];
            } else {
                Big_Ecpf = new BigDecimal(0);
            }
            category = (String) rows[14];
            psm.setSupplementarytype(category);
            if (Category.equals("S")) {
                if (rows[15] != null) {
                    psm.setDabatchno((String) rows[15]);
                }
            }
            double Salary = Big_Salary.doubleValue();
            double Epf = Big_Epf.doubleValue();
            double Rl = Big_Rl.doubleValue();
            double Vpf = Big_Vpf.doubleValue();
            double Dvpf = Big_Dvpf.doubleValue();
            double Nrl = Big_Dvpf.doubleValue();
            double Ecpf = Big_Ecpf.doubleValue();
            double Ecfb = Big_Ecfb.doubleValue();
            double employertotal = 0;

            employertotal = Epf + Rl + Vpf;

            long roundsalary = Math.round(Salary);
            long roundEpf = Math.round(Epf);
            long roundRl = Math.round(Rl);
            long roundVpf = Math.round(Vpf);
            long roundDvpf = Math.round(Dvpf);
            long roundNrl = Math.round(Nrl);
            long roundEmployertotal = Math.round(employertotal);
            long roundEcpf = Math.round(Ecpf);
            long roundEcfb = Math.round(Ecfb);

            psm.setSalary(String.valueOf(roundsalary));
            psm.setEpf(String.valueOf(roundEpf));
            psm.setEpfloan(String.valueOf(roundRl));
            psm.setVpf(String.valueOf(roundVpf));
            psm.setDavpf(String.valueOf(roundDvpf));
            psm.setNrl(String.valueOf(roundNrl));
            psm.setEmployertotal(String.valueOf(roundEmployertotal));
            psm.setPercentage833(String.valueOf(roundEcfb));
            psm.setPercentage367(String.valueOf(roundEcpf));
            if (employertotal != 0) {
                epfr.getEPFformPrintWriter(psm, filePath);
                slipno++;
            }
        }
        return category;
    }   
}
