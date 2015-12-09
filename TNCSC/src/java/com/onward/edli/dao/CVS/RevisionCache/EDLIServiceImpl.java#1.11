/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.edli.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import com.onward.persistence.payroll.Employeemaster;
import com.onward.persistence.payroll.Payrollprocessingdetails;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.reports.EDLIReport;
import com.onward.valueobjects.EDLIModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author user
 */
public class EDLIServiceImpl extends OnwardAction implements EDLIService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEDLIDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        try {
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            int i = 1;

            String EMP_CATEGORY_QUERY = "select regionmaster.id,regionmaster.regionname,"
                    + "count(case when (payrollprocessingdetails.employeecategory = 'R' and  year=" + p_year + " and month=" + p_month + " and process is true) then 1 end) x_count, "
                    + "count(case when (payrollprocessingdetails.employeecategory = 'S'and  year=" + p_year + " and month=" + p_month + " and process is true ) then 1 end) y_count, "
                    + "count(case when (payrollprocessingdetails.employeecategory = 'L'and  year=" + p_year + " and month=" + p_month + " and process is true ) then 1 end) z_count "
                    + "from regionmaster LEFT join payrollprocessingdetails on regionmaster.id=payrollprocessingdetails.accregion "
                    + "group by regionmaster.regionname, regionmaster.id order by regionmaster.regionname";

//            String EMP_CATEGORY_QUERY = "select regionmaster.id,regionmaster.regionname,"
//                    + "COALESCE(count(case when (payrollprocessingdetails.employeecategory = 'R' and  year=" + p_year + " and month=" + p_month + " ) then 1 end),'0') x_count, "
//                    + "COALESCE(count(case when (payrollprocessingdetails.employeecategory = 'S'and  year=" + p_year + " and month=" + p_month + " ) then 1 end),'0') y_count, "
//                    + "COALESCE(count(case when (payrollprocessingdetails.employeecategory = 'L'and  year=" + p_year + " and month=" + p_month + " ) then 1 end),'0') z_count "
//                    + "from regionmaster LEFT join payrollprocessingdetails on regionmaster.id=payrollprocessingdetails.accregion "
//                    + "group by regionmaster.regionname, regionmaster.id order by regionmaster.id";

            SQLQuery employeecategoryquery = session.createSQLQuery(EMP_CATEGORY_QUERY);

            StringBuffer stringBuff = new StringBuffer();

            if (employeecategoryquery.list().size() > 0) {
                stringBuff.append("<FONT SIZE=2>");
                stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"edlidetailstable\">");
                stringBuff.append("<thead>");
                stringBuff.append("<tr>");
                stringBuff.append("<td width=\"3%\" align=\"center\">Sno</td>");
                stringBuff.append("<td width=\"40%\" align=\"center\">Region Name</td>");
                stringBuff.append("<td width=\"19%\" align=\"center\">Regular</td>");
                stringBuff.append("<td width=\"19%\" align=\"center\">Seasonal</td>");
                stringBuff.append("<td width=\"19%\" align=\"center\">Load Man</td>");
                stringBuff.append("</tr>");
                stringBuff.append("</thead>");
                stringBuff.append("<tbody>");
                long regtotal = 0;
                long seatotal = 0;
                long loatotal = 0;
                for (ListIterator its = employeecategoryquery.list().listIterator(); its.hasNext();) {
                    String className = "";
                    className = (i % 2 == 0) ? "rowColor1" : "rowColor2";
                    Object[] rows = (Object[]) its.next();
                    String regionId = (String) rows[0];
                    String regionName = (String) rows[1];

                    BigInteger big_regular = (BigInteger) rows[2];
                    BigInteger big_seasonal = (BigInteger) rows[3];
                    BigInteger big_loadman = (BigInteger) rows[4];
                    long regular = big_regular.longValue();
                    long seasonal = big_seasonal.longValue();
                    long loadman = big_loadman.longValue();
                    regtotal = regtotal + regular;
                    seatotal = seatotal + seasonal;
                    loatotal = loatotal + loadman;
                    stringBuff.append("<tr class=\"" + className + "\">");
                    stringBuff.append("<td width=\"3%\" align=\"center\">" + i + "</td>");
                    stringBuff.append("<td width=\"40%\" align=\"left\">" + regionName + "</td>");
                    stringBuff.append("<td width=\"19%\" align=\"center\">" + regular + "</td>");
                    stringBuff.append("<td width=\"19%\" align=\"center\">" + seasonal + "</td>");
                    stringBuff.append("<td width=\"19%\" align=\"center\">" + loadman + "</td>");
                    stringBuff.append("</tr>");
                    i++;
                }

                stringBuff.append("</tbody>");
                stringBuff.append("<tfoot>");
                stringBuff.append("<tr >");
                stringBuff.append("<td width=\"43%\" align=\"center\" colspan=\"2\">Total</td>");
                stringBuff.append("<td width=\"19%\" align=\"center\"><a href=\"#\" onclick=\"showdesignationwisedetails('R')\" >" + regtotal + "</a></td>");
                stringBuff.append("<td width=\"19%\" align=\"center\"><a href=\"#\" onclick=\"showdesignationwisedetails('S')\" >" + seatotal + "</a></td>");
                stringBuff.append("<td width=\"19%\" align=\"center\"><a href=\"#\" onclick=\"showdesignationwisedetails('L')\" >" + loatotal + "</a></td>");
                stringBuff.append("</tr>");
                stringBuff.append("</tfoot>");
                stringBuff.append("</table>");
                stringBuff.append("</FONT>");
            }


            resultMap.put("edlidetails", stringBuff.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDesignationWiseEmployeesDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String empcatagory) {
        Map resultMap = new HashMap();
        try {
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
            int i = 1;

//            StringBuilder DESIGNATION_QUERY = new StringBuilder("select coalesce(dm.designation,'UNKNOWN') as designation,count(*),dm.designationcode from employeemaster as em ");
//            DESIGNATION_QUERY.append(" left join designationmaster as dm on dm.designationcode=em.designation ");
//            DESIGNATION_QUERY.append(" group by dm.designation,dm.designationcode order by dm.designationcode ");

            StringBuilder DESIGNATION_QUERY = new StringBuilder(" select coalesce(dm.designation,'UNKNOWN') as designation,count(*),dm.designationcode   ");
            DESIGNATION_QUERY.append(" from payrollprocessingdetails as ppd  ");
            DESIGNATION_QUERY.append(" left join designationmaster as dm on dm.designationcode=ppd.designation  ");
            DESIGNATION_QUERY.append(" where ppd.employeecategory = '" + empcatagory + "' and  year=" + p_year + " and month=" + p_month + "  ");
            DESIGNATION_QUERY.append(" and employeecategory='" + empcatagory + "'  and ppd.process is true");
//            DESIGNATION_QUERY.append(" and ppd.process is true and employeecategory='"+empcatagory+"' ");
            DESIGNATION_QUERY.append(" group by dm.designation,dm.designationcode,ppd.employeecategory ");
            DESIGNATION_QUERY.append(" order by  cast(dm.orderno as integer) ");

//            StringBuilder DESIGNATION_QUERY = new StringBuilder(" select coalesce(dm.designation,'UNKNOWN') as designation,count(*),dm.designationcode  ");
//            DESIGNATION_QUERY.append(" from employeemaster as em   ");
//            DESIGNATION_QUERY.append(" left join designationmaster as dm on dm.designationcode=em.designation  ");
//            DESIGNATION_QUERY.append(" LEFT join regionmaster as rm on rm.id=em.region  ");
//            DESIGNATION_QUERY.append(" LEFT join payrollprocessingdetails on em.region=payrollprocessingdetails.accregion  ");
//            DESIGNATION_QUERY.append(" and em.epfno=payrollprocessingdetails.employeeprovidentfundnumber ");
//            DESIGNATION_QUERY.append(" where payrollprocessingdetails.employeecategory = '"+empcatagory+"' and  year=" + p_year + " and month=" + p_month + "  ");
//            DESIGNATION_QUERY.append(" and payrollprocessingdetails.process is true and employeecategory='"+empcatagory+"' ");
//            DESIGNATION_QUERY.append(" group by dm.designation,dm.designationcode,payrollprocessingdetails.employeecategory ");
//            DESIGNATION_QUERY.append(" order by dm.designationcode ");

            SQLQuery designationlistquery = session.createSQLQuery(DESIGNATION_QUERY.toString());

            StringBuilder designationBuff = new StringBuilder();

            if (designationlistquery.list().size() > 0) {
                designationBuff.append("<FONT SIZE=2>");
                designationBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"edlipaidtable\">");
                designationBuff.append("<thead>");
                designationBuff.append("<tr>");
                designationBuff.append("<td width=\"3%\" align=\"center\">Sno</td>");
                designationBuff.append("<td width=\"60%\" align=\"center\">Designation Name</td>");
                designationBuff.append("<td width=\"37%\" align=\"center\">Count</td>");
                designationBuff.append("</tr>");
                designationBuff.append("</thead>");
                designationBuff.append("<tbody>");
                long designtotal = 0;

                for (ListIterator its = designationlistquery.list().listIterator(); its.hasNext();) {
                    String className = "";
                    className = (i % 2 == 0) ? "rowColor1" : "rowColor2";
                    Object[] rows = (Object[]) its.next();
                    String designationName = (String) rows[0];
                    String designationcode = (String) rows[2];

                    BigInteger big_regular = (BigInteger) rows[1];

                    long regularcount = big_regular.longValue();
                    designtotal = designtotal + regularcount;

                    designationBuff.append("<tr class=\"" + className + "\">");
                    designationBuff.append("<td width=\"3%\" align=\"center\">" + i + "</td>");
                    designationBuff.append("<td width=\"60%\" align=\"left\">" + designationName + "</td>");
                    designationBuff.append("<td width=\"37%\" align=\"center\"><a href=\"#\" onclick=\"showdesignationEmployees('" + designationcode + "','" + empcatagory + "')\" >" + regularcount + "</a></td>");
                    designationBuff.append("</tr>");
                    i++;
                }

                designationBuff.append("</tbody>");
                designationBuff.append("<tfoot>");
                designationBuff.append("<tr >");
                designationBuff.append("<td width=\"63%\" align=\"center\" colspan=\"2\">Total</td>");
                designationBuff.append("<td width=\"37%\" align=\"center\">" + designtotal + "</td>");
                designationBuff.append("</tr>");
                designationBuff.append("</tfoot>");
                designationBuff.append("</table>");
                designationBuff.append("</FONT>");
            }

            resultMap.put("edlidetailssal", designationBuff.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDesignationEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String designationcode, String empcatagory) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int selectmonth = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            int p_year = Integer.valueOf(year);
            int p_month = Integer.valueOf(month);
//            selectmonth = Integer.parseInt(month);
//            startYear = Integer.parseInt(year.toString()) - 58;
//            if (selectmonth == 1 || selectmonth == 2 || selectmonth == 3) {
//                startYear = startYear + 1;
//            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria ppdCrit = session.createCriteria(Payrollprocessingdetails.class);
            ppdCrit.add(Restrictions.sqlRestriction("employeecategory='" + empcatagory + "'"));
            ppdCrit.add(Restrictions.sqlRestriction("designation='" + designationcode + "'"));
            ppdCrit.add(Restrictions.sqlRestriction("month='" + p_month + "' and year='" + p_year + "' "));
            List ppdList = ppdCrit.list();
            for (int i = 0; i < ppdList.size(); i++) {
                Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) ppdList.get(i);
                Criteria empCrit = session.createCriteria(Employeemaster.class);
                empCrit.add(Restrictions.sqlRestriction("epfno='" + payrollprocessingdetailsObj.getEmployeemaster().getEpfno() + "'"));
//                empCrit.add(Restrictions.sqlRestriction("designation='" + designationcode + "'"));
//                empCrit.add(Restrictions.sqlRestriction("category='" + empcatagory + "'"));
//            empCrit.addOrder(Order.asc("region"));
                List empList = empCrit.list();
                if (empList.size() > 0) {
                    Employeemaster employeemasterObj = (Employeemaster) empList.get(0);
                    stringBuff.append("<tr >");
                    if (employeemasterObj.getRegion().equalsIgnoreCase("TRANS")) {
                        stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                    } else if (employeemasterObj.getRegion().equalsIgnoreCase("RET")) {
                        stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                    } else if (employeemasterObj.getRegion() != null) {
                        stringBuff.append("<td >" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                    } else {
                        stringBuff.append("<td >" + "Unknown" + "</td>");
                    }
//                stringBuff.append("<td >" + employeemasterObj.getRegionmaster().getRegionname()+ "</td>");
                    stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                    stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                    stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                    if (employeemasterObj.getDesignation() != null) {
                        stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    } else {
                        stringBuff.append("<td >" + "Unknown" + "</td>");
                    }
                    stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                    stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                    if (employeemasterObj.getDateofbirth() != null) {
                        birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                        if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
                            retirementyear = Integer.parseInt(birthArray[2]) + 60;
                        } else {
                            retirementyear = Integer.parseInt(birthArray[2]) + 58;
                        }

                        if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                            stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                        } else if (Integer.parseInt(birthArray[0]) == 1) {
                            stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                        } else {
                            stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                        }

//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                    } else {
                        stringBuff.append("<td > - </td>");
                    }


                    stringBuff.append("</tr>");
                } else {
                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + CommonUtility.getRegion(session, payrollprocessingdetailsObj.getAccregion()).getRegionname() + "</td>");
                    stringBuff.append("<td >" + payrollprocessingdetailsObj.getEmployeemaster().getEpfno() + "</td>");
                    stringBuff.append("<td > - </td>");
                    stringBuff.append("<td > - </td>");
                    stringBuff.append("<td > - </td>");
                    stringBuff.append("<td > - </td>");
                    stringBuff.append("<td > - </td>");
                    stringBuff.append("<td > - </td>");
                    stringBuff.append("</tr>");


                }

            }


            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("designationwiseemployees", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAppointmentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromyear, String toyear) {
        Map resultMap = new HashMap();
        String budgetId = "";
        String startyear = "";
        String endyear = "";
        int aprtotal = 0;
        int maytotal = 0;
        int junetotal = 0;
        int julytotal = 0;
        int augtotal = 0;
        int septotal = 0;
        int octtotal = 0;
        int novtotal = 0;
        int dectotal = 0;
        int jantotal = 0;
        int febtotal = 0;
        int martotal = 0;
        int yeartotal = 0;
        int startYear = 0;
        int endYear = 0;
        String regionId = "";
        StringBuffer stringBuff = new StringBuffer();
        stringBuff.append("<FONT SIZE=2>");
        stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"empappointtable\">");
        stringBuff.append("<thead>");
        stringBuff.append("<tr>");
        stringBuff.append("<th colspan=\"14\">" + fromyear + " to " + toyear + " Appointment Details " + "</th>");
        stringBuff.append("</tr>");
        stringBuff.append("<tr>");
        stringBuff.append("<th>Region Name</th>");
        stringBuff.append("<th>April</th>");
        stringBuff.append("<th>May</th>");
        stringBuff.append("<th>June</th>");
        stringBuff.append("<th>July</th>");
        stringBuff.append("<th>August</th>");
        stringBuff.append("<th>Sept</th>");
        stringBuff.append("<th>October</th>");
        stringBuff.append("<th>November</th>");
        stringBuff.append("<th>December</th>");
        stringBuff.append("<th>January</td>");
        stringBuff.append("<th>February</th>");
        stringBuff.append("<th>March</th>");
        stringBuff.append("<th>Total</th>");
        stringBuff.append("</tr>");
        stringBuff.append("</thead>");
        stringBuff.append("<tbody>");
        startYear = Integer.parseInt(fromyear.toString());
        endYear = Integer.parseInt(toyear.toString());

        StringBuffer qryBuffer = new StringBuffer();
        qryBuffer.append(" SELECT mthreport.* ");
        qryBuffer.append(" FROM  ");
        qryBuffer.append(" crosstab('SELECT i.regionname::text As row_name,i.id::text as regionid , to_char(if.dateofappoinment, ''mon'')::text As monthname,  ");
        qryBuffer.append(" count(*) as countvalue ");
        qryBuffer.append(" FROM regionmaster As i  ");
        qryBuffer.append(" LEFT JOIN employeemaster As if  ");
        qryBuffer.append(" ON i.id = if.region ");
        qryBuffer.append(" and date(if.dateofappoinment) between to_date(''01-04-" + startYear + "'',''dd-MM-yyyy'') AND  to_date(''31-03-" + endYear + "'',''dd-MM-yyyy'')	   ");
        qryBuffer.append(" GROUP BY i.regionname, to_char(if.dateofappoinment, ''mon''), date_part(''month'', if.dateofappoinment),regionid ");
        qryBuffer.append(" ORDER BY i.regionname',  ");
        qryBuffer.append(" 'SELECT to_char(date ''" + startYear + "-04-01'' + (n || '' month'')::interval, ''mon'') As short_mname  ");
        qryBuffer.append(" FROM generate_series(0,11) n') ");
        qryBuffer.append(" As mthreport(regionname text,regionid text, apr integer, may integer, june integer,  ");
        qryBuffer.append(" july integer, augu integer, sep integer, oct integer,  ");
        qryBuffer.append(" nov integer, dec integer, jan integer, feb integer,  ");
        qryBuffer.append(" mar integer)");

        Query query = session.createSQLQuery(qryBuffer.toString());
        List retireList = query.list();
//
//        Criteria empCrit = session.createCriteria(Employeemaster.class);
//        //empCrit.add(Restrictions.sqlRestriction("region='" + selectedregion + "'"));
//        empCrit.addOrder(Order.asc("dateofbirth"));
//        List empList = empCrit.list();
        int regtot = 0;
        int tot = 0;
        for (ListIterator it1 = retireList.listIterator(); it1.hasNext();) {
            Object[] row1 = (Object[]) it1.next();
            regtot = 0;
            if (row1[1] != null) {
                regionId = (String) row1[1];
            }
            stringBuff.append("<tr >");
            stringBuff.append("<td >" + row1[0] + "</td>");
            if (row1[2] != null) {
                tot = (Integer) row1[2];
                aprtotal = aprtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('4','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[3] != null) {
                tot = (Integer) row1[3];
                maytotal = maytotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('5','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[4] != null) {
                tot = (Integer) row1[4];
                junetotal = junetotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('6','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[5] != null) {
                tot = (Integer) row1[5];
                julytotal = julytotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('7','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[6] != null) {
                tot = (Integer) row1[6];
                augtotal = augtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('8','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[7] != null) {
                tot = (Integer) row1[7];
                septotal = septotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('9','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[8] != null) {
                tot = (Integer) row1[8];
                octtotal = octtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('10','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[9] != null) {
                tot = (Integer) row1[9];
                novtotal = novtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('11','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[10] != null) {
                tot = (Integer) row1[10];
                dectotal = dectotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('12','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[11] != null) {
                tot = (Integer) row1[11];
                jantotal = jantotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('1','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[12] != null) {
                tot = (Integer) row1[12];
                febtotal = febtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('2','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[13] != null) {
                tot = (Integer) row1[13];
                martotal = martotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseAppointmentEmployees('3','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getRegionAllMonthsAppointmentEmployees('" + regionId + "')\" >" + regtot + "</a></td>");
            stringBuff.append("</tr>");
        }
        stringBuff.append("</tbody>");
        stringBuff.append("<tfoot>");
        stringBuff.append("<tr >");
        stringBuff.append("<td align=\"center\" >" + "Total" + "</td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('4')\" >" + aprtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('5')\" >" + maytotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('6')\" >" + junetotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('7')\" >" + julytotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('8')\" >" + augtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('9')\" >" + septotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('10')\" >" + octtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('11')\" >" + novtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('12')\" >" + dectotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('1')\" >" + jantotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('2')\" >" + febtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseAppointmentEmployees('3')\" >" + martotal + "</a></td>");
        yeartotal = aprtotal + maytotal + junetotal + julytotal + augtotal + septotal + octtotal + novtotal + dectotal + jantotal + febtotal + martotal;
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getAllRegionAppointmentEmployees()\" >" + yeartotal + "</a></td>");
        stringBuff.append("</tr>");
        stringBuff.append("</tfoot>");
        stringBuff.append("</table>");
        stringBuff.append("</FONT>");

        resultMap.put("regionwiseemployee", stringBuff.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionMonthwiseAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String regionid, String year) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int selectmonth = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            selectmonth = Integer.parseInt(month);
            startYear = Integer.parseInt(year.toString());
            if (selectmonth == 1 || selectmonth == 2 || selectmonth == 3) {
                startYear = startYear + 1;
            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
//            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("region='" + regionid + "'"));
            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(MONTH FROM  dateofappoinment )=" + Integer.parseInt(month)));
            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(YEAR FROM  dateofappoinment )=" + startYear));
            empCrit.addOrder(Order.asc("dateofappoinment"));
            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
//                stringBuff.append("<td >" + employeemasterObj.getRegionmaster().getRegionname()+ "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getALLRegionMonthwiseAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int selectmonth = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            selectmonth = Integer.parseInt(month);
            startYear = Integer.parseInt(year.toString());
            if (selectmonth == 1 || selectmonth == 2 || selectmonth == 3) {
                startYear = startYear + 1;
            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
//            empCrit.add(Restrictions.sqlRestriction("region='R01'"));
            empCrit.add(Restrictions.sqlRestriction("region is not null"));
            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(MONTH FROM  dateofappoinment )=" + Integer.parseInt(month)));
            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(YEAR FROM  dateofappoinment )=" + startYear));
            empCrit.addOrder(Order.asc("dateofappoinment"));
//            empCrit.addOrder(Order.asc("EXTRACT(YEAR FROM  dateofbirth )"));
            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
                if (employeemasterObj.getRegion().equalsIgnoreCase("TRANS")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion().equalsIgnoreCase("RET")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion() != null) {
                    stringBuff.append("<td >" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionAllMonthsAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String regionid, String endyear) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int endYear = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            startYear = Integer.parseInt(startyear.toString());
            endYear = Integer.parseInt(endyear.toString());
//            if(selectmonth==1 || selectmonth==2 || selectmonth==3){
//                startYear =startYear + 1;
//            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
//            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("region='" + regionid + "'"));
            empCrit.add(Restrictions.or(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofappoinment ) between 4 and 12 and  EXTRACT(YEAR FROM  dateofappoinment )=" + startYear + ") "), Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofappoinment ) between 1 and 3 and  EXTRACT(YEAR FROM  dateofappoinment )=" + endYear + ") ")));
//            empCrit.add(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 4 and 12 and  EXTRACT(YEAR FROM  dateofbirth )=1954) "));
//            empCrit.add(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 4 and 12 and  EXTRACT(YEAR FROM  dateofbirth )=1954) "));
//            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(YEAR FROM  dateofbirth )=" + startYear));
            empCrit.addOrder(Order.asc("dateofappoinment"));

            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
//                stringBuff.append("<td >" + employeemasterObj.getRegionmaster().getRegionname()+ "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAllRegionAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int endYear = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            startYear = Integer.parseInt(startyear.toString());
            endYear = Integer.parseInt(endyear.toString());
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("dateofappoinment is not null"));
            empCrit.add(Restrictions.sqlRestriction("region is not null"));
            empCrit.add(Restrictions.or(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofappoinment ) between 4 and 12 and  EXTRACT(YEAR FROM  dateofappoinment )=" + startYear + ") "), Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofappoinment ) between 1 and 3 and  EXTRACT(YEAR FROM  dateofappoinment )=" + endYear + ") ")));
            empCrit.addOrder(Order.asc("dateofappoinment"));

            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
                if (employeemasterObj.getRegion().equalsIgnoreCase("TRANS")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion().equalsIgnoreCase("RET")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion() != null) {
                    stringBuff.append("<td >" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRetirementDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromyear, String toyear) {
        Map resultMap = new HashMap();

        int aprtotal = 0;
        int maytotal = 0;
        int junetotal = 0;
        int julytotal = 0;
        int augtotal = 0;
        int septotal = 0;
        int octtotal = 0;
        int novtotal = 0;
        int dectotal = 0;
        int jantotal = 0;
        int febtotal = 0;
        int martotal = 0;
        int yeartotal = 0;
        int startYear = 0;
        int endYear = 0;
        int startYearforOA = 0;
        int endYearforOA = 0;
        String regionId = "";
        StringBuffer stringBuff = new StringBuffer();
        stringBuff.append("<FONT SIZE=2>");
        stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"empretirementtable\">");
        stringBuff.append("<thead>");
        stringBuff.append("<tr>");
        stringBuff.append("<th colspan=\"14\">" + fromyear + " to " + toyear + " Retirement Details " + "</th>");
        stringBuff.append("</tr>");
        stringBuff.append("<tr>");
        stringBuff.append("<th>Region Name</th>");
        stringBuff.append("<th>April</th>");
        stringBuff.append("<th>May</th>");
        stringBuff.append("<th>June</th>");
        stringBuff.append("<th>July</th>");
        stringBuff.append("<th>August</th>");
        stringBuff.append("<th>Sept</th>");
        stringBuff.append("<th>October</th>");
        stringBuff.append("<th>November</th>");
        stringBuff.append("<th>December</th>");
        stringBuff.append("<th>January</td>");
        stringBuff.append("<th>February</th>");
        stringBuff.append("<th>March</th>");
        stringBuff.append("<th>Total</th>");
        stringBuff.append("</tr>");
        stringBuff.append("</thead>");
        stringBuff.append("<tbody>");
        startYear = Integer.parseInt(fromyear.toString()) - 58;
        startYearforOA = Integer.parseInt(fromyear.toString()) - 60;
        endYear = Integer.parseInt(toyear.toString()) - 58;
        endYearforOA = Integer.parseInt(toyear.toString()) - 60;
//        endYear=startYear + 1;

        StringBuffer qryBuffer = new StringBuffer();
        qryBuffer.append(" SELECT mthreport.* ");
        qryBuffer.append(" FROM  ");
        qryBuffer.append(" crosstab('SELECT i.regionname::text As row_name,i.id::text as regionid , to_char(if.dateofbirth, ''mon'')::text As monthname,  ");
        qryBuffer.append(" count(*) as countvalue");
        qryBuffer.append(" FROM regionmaster As i  ");
        qryBuffer.append(" LEFT JOIN employeemaster As if  ");
        qryBuffer.append(" ON i.id = if.region ");
        qryBuffer.append(" and case when substring(if.designation from 1 for 2)=''LE'' then  ");
        qryBuffer.append(" date(if.dateofbirth) between to_date(''01-04-" + startYearforOA + "'',''dd-MM-yyyy'') AND  to_date(''31-03-" + endYearforOA + "'',''dd-MM-yyyy'')     ");
        qryBuffer.append(" else  ");
        qryBuffer.append(" date(if.dateofbirth) between to_date(''01-04-" + startYear + "'',''dd-MM-yyyy'') AND  to_date(''31-03-" + endYear + "'',''dd-MM-yyyy'')    ");
        qryBuffer.append(" end  ");

//        qryBuffer.append(" and case when if.designation like ''LE%'' then  ");
//        qryBuffer.append(" date(if.dateofbirth) between to_date(''01-04-" + startYearforOA + "'',''dd-MM-yyyy'') AND  to_date(''31-03-" + endYearforOA + "'',''dd-MM-yyyy'')	  end ");
//        qryBuffer.append("  when date(if.dateofbirth) between to_date(''01-04-" + startYear + "'',''dd-MM-yyyy'') AND  to_date(''31-03-" + endYear + "'',''dd-MM-yyyy'')	   ");
//        qryBuffer.append(" date(if.dateofbirth) between to_date(''01-04-" + Integer.parseInt(fromyear.toString()) - 60 + "'',''dd-MM-yyyy'') AND  to_date(''31-03-" + Integer.parseInt(fromyear.toString()) - 60 + "'',''dd-MM-yyyy'')	   ");
//        qryBuffer.append("  end ");
//        qryBuffer.append(" and date(if.dateofbirth) between to_date(''01-04-" + startYear + "'',''dd-MM-yyyy'') AND  to_date(''31-03-" + endYear + "'',''dd-MM-yyyy'')	   ");
        qryBuffer.append(" GROUP BY i.regionname, to_char(if.dateofbirth, ''mon''), date_part(''month'', if.dateofbirth),regionid ");
        qryBuffer.append(" ORDER BY i.regionname',  ");
        qryBuffer.append(" 'SELECT to_char(date ''" + startYear + "-04-01'' + (n || '' month'')::interval, ''mon'') As short_mname  ");
        qryBuffer.append(" FROM generate_series(0,11) n') ");
        qryBuffer.append(" As mthreport(regionname text,regionid text, apr integer, may integer, june integer,  ");
        qryBuffer.append(" july integer, augu integer, sep integer, oct integer,  ");
        qryBuffer.append(" nov integer, dec integer, jan integer, feb integer,  ");
        qryBuffer.append(" mar integer)");

        System.out.println("qryBuffer===" + qryBuffer);
        Query query = session.createSQLQuery(qryBuffer.toString());
        List retireList = query.list();
//
//        Criteria empCrit = session.createCriteria(Employeemaster.class);
//        //empCrit.add(Restrictions.sqlRestriction("region='" + selectedregion + "'"));
//        empCrit.addOrder(Order.asc("dateofbirth"));
//        List empList = empCrit.list();
        int regtot = 0;
        int tot = 0;
        for (ListIterator it1 = retireList.listIterator(); it1.hasNext();) {
            Object[] row1 = (Object[]) it1.next();
            regtot = 0;
            if (row1[1] != null) {
                regionId = (String) row1[1];
            }

            stringBuff.append("<tr >");
            stringBuff.append("<td >" + row1[0] + "</td>");
            if (row1[2] != null) {
                tot = (Integer) row1[2];
                aprtotal = aprtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('4','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[3] != null) {
                tot = (Integer) row1[3];
                maytotal = maytotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('5','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[4] != null) {
                tot = (Integer) row1[4];
                junetotal = junetotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('6','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[5] != null) {
                tot = (Integer) row1[5];
                julytotal = julytotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('7','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[6] != null) {
                tot = (Integer) row1[6];
                augtotal = augtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('8','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[7] != null) {
                tot = (Integer) row1[7];
                septotal = septotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('9','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[8] != null) {
                tot = (Integer) row1[8];
                octtotal = octtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('10','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[9] != null) {
                tot = (Integer) row1[9];
                novtotal = novtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('11','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[10] != null) {
                tot = (Integer) row1[10];
                dectotal = dectotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('12','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[11] != null) {
                tot = (Integer) row1[11];
                jantotal = jantotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('1','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[12] != null) {
                tot = (Integer) row1[12];
                febtotal = febtotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('2','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            if (row1[13] != null) {
                tot = (Integer) row1[13];
                martotal = martotal + tot;
                regtot = regtot + tot;
                stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"showRegionMonthwiseRetirementEmployees('3','" + regionId + "')\" >" + tot + "</a></td>");
            } else {
                stringBuff.append("<td align=\"center\" >-</td>");
            }
            stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getRegionAllMonthsRetirementEmployees('" + regionId + "')\" >" + regtot + "</a></td>");
            stringBuff.append("</tr>");
        }
        stringBuff.append("</tbody>");
        stringBuff.append("<tfoot>");
        stringBuff.append("<tr >");
        stringBuff.append("<td align=\"center\" >" + "Total" + "</td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('4')\" >" + aprtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('5')\" >" + maytotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('6')\" >" + junetotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('7')\" >" + julytotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('8')\" >" + augtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('9')\" >" + septotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('10')\" >" + octtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('11')\" >" + novtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('12')\" >" + dectotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('1')\" >" + jantotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('2')\" >" + febtotal + "</a></td>");
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getALLRegionMonthwiseRetirementEmployees('3')\" >" + martotal + "</a></td>");
        yeartotal = aprtotal + maytotal + junetotal + julytotal + augtotal + septotal + octtotal + novtotal + dectotal + jantotal + febtotal + martotal;
        stringBuff.append("<td align=\"center\" ><a href=\"#\" onclick=\"getAllRegionRetirementEmployees()\" >" + yeartotal + "</a></td>");
        stringBuff.append("</tr>");
        stringBuff.append("</tfoot>");
        stringBuff.append("</table>");
        stringBuff.append("</FONT>");

        resultMap.put("regionwiseemployee", stringBuff.toString());
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionMonthwiseRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String regionid, String year) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int startYearforOA = 0;
        int selectmonth = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            selectmonth = Integer.parseInt(month);
            startYear = Integer.parseInt(year.toString()) - 58;
            startYearforOA = Integer.parseInt(year.toString()) - 60;
            if (selectmonth == 1 || selectmonth == 2 || selectmonth == 3) {
                startYear = startYear + 1;
                startYearforOA = startYearforOA + 1;
            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
//            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("region='" + regionid + "'"));
            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(MONTH FROM  dateofbirth )=" + Integer.parseInt(month)));
            empCrit.add(Restrictions.sqlRestriction(" case when substring(designation from 1 for 2)='LE' then EXTRACT(YEAR FROM  dateofbirth )=" + startYearforOA + " else   EXTRACT(YEAR FROM  dateofbirth )=" + startYear + " end"));
//            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(YEAR FROM  dateofbirth )=" + startYear));
            empCrit.addOrder(Order.asc("dateofbirth"));
            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
//                stringBuff.append("<td >" + employeemasterObj.getRegionmaster().getRegionname()+ "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getALLRegionMonthwiseRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int startYearforOA = 0;
        int selectmonth = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            selectmonth = Integer.parseInt(month);
            startYear = Integer.parseInt(year.toString()) - 58;
            startYearforOA = Integer.parseInt(year.toString()) - 60;
            if (selectmonth == 1 || selectmonth == 2 || selectmonth == 3) {
                startYear = startYear + 1;
                startYearforOA = startYearforOA + 1;
            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
//            empCrit.add(Restrictions.sqlRestriction("region='R01'"));
            empCrit.add(Restrictions.sqlRestriction("region is not null"));
            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(MONTH FROM  dateofbirth )=" + Integer.parseInt(month)));
//            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(YEAR FROM  dateofbirth )=" + startYear));
            empCrit.add(Restrictions.sqlRestriction(" case when substring(designation from 1 for 2)='LE' then EXTRACT(YEAR FROM  dateofbirth )=" + startYearforOA + " else   EXTRACT(YEAR FROM  dateofbirth )=" + startYear + " end"));
            empCrit.addOrder(Order.asc("dateofbirth"));
//            empCrit.addOrder(Order.asc("EXTRACT(YEAR FROM  dateofbirth )"));
//            empCrit.addOrder(Order.asc("EXTRACT(MONTH FROM  dateofbirth )"));
            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
                if (employeemasterObj.getRegion().equalsIgnoreCase("TRANS")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion().equalsIgnoreCase("RET")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion() != null) {
                    stringBuff.append("<td >" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }

                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionAllMonthsRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String regionid, String endyear) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int startYearforOA = 0;
        int endYear = 0;
        int endYearforOA = 0;
        String birthArray[];
        int retirementyear = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            startYear = Integer.parseInt(startyear.toString()) - 58;
            startYearforOA = Integer.parseInt(startyear.toString()) - 60;
            endYear = Integer.parseInt(endyear.toString()) - 58;
            endYearforOA = Integer.parseInt(endyear.toString()) - 60;
//            if(selectmonth==1 || selectmonth==2 || selectmonth==3){
//                startYear =startYear + 1;
//            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("region='" + regionid + "'"));
            empCrit.add(
                    Restrictions.or(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 4 and 12 and  "
                    + " case when substring(designation from 1 for 2)='LE' then EXTRACT(YEAR FROM  dateofbirth )=" + startYearforOA + " else   EXTRACT(YEAR FROM  dateofbirth )=" + startYear + " end) "),
                    //                    + "EXTRACT(YEAR FROM  dateofbirth )=" + startYear + ") "),
                    Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 1 and 3 and "
                    + " case when substring(designation from 1 for 2)='LE' then EXTRACT(YEAR FROM  dateofbirth )=" + endYearforOA + " else   EXTRACT(YEAR FROM  dateofbirth )=" + endYear + " end) ")));
//                    + " EXTRACT(YEAR FROM  dateofbirth )=" + endYear + ") ")));
//                    + " EXTRACT(YEAR FROM  dateofbirth )=" + endYear + ") ")));
//            empCrit.add(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 4 and 12 and  EXTRACT(YEAR FROM  dateofbirth )=1954) "));
//            empCrit.add(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 4 and 12 and  EXTRACT(YEAR FROM  dateofbirth )=1954) "));
//            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(YEAR FROM  dateofbirth )=" + startYear));
            empCrit.addOrder(Order.asc("dateofbirth"));

            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
                if (employeemasterObj.getRegion().equalsIgnoreCase("TRANS")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion().equalsIgnoreCase("RET")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion() != null) {
                    stringBuff.append("<td >" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }

//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAllRegionRetirementEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear) {
        Map resultMap = new HashMap();
        int startYear = 0;
        int endYear = 0;
        String birthArray[];
        int retirementyear = 0;
        int startYearforOA = 0;
        int endYearforOA = 0;

        try {
            StringBuffer stringBuff = new StringBuffer();
            startYear = Integer.parseInt(startyear.toString()) - 58;
            endYear = Integer.parseInt(endyear.toString()) - 58;
            startYearforOA = Integer.parseInt(startyear.toString()) - 60;
            endYearforOA = Integer.parseInt(endyear.toString()) - 60;
//            if(selectmonth==1 || selectmonth==2 || selectmonth==3){
//                startYear =startYear + 1;
//            }


            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");


            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("dateofbirth is not null"));
            empCrit.add(Restrictions.sqlRestriction("region is not null"));
            empCrit.add(
                    Restrictions.or(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 4 and 12 and  "
                    + " case when substring(designation from 1 for 2)='LE' then EXTRACT(YEAR FROM  dateofbirth )=" + startYearforOA + " else   EXTRACT(YEAR FROM  dateofbirth )=" + startYear + " end) "),
                    Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 1 and 3 and "
                    + " case when substring(designation from 1 for 2)='LE' then EXTRACT(YEAR FROM  dateofbirth )=" + endYearforOA + " else   EXTRACT(YEAR FROM  dateofbirth )=" + endYear + " end) ")));
//            empCrit.add(Restrictions.or(Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 4 and 12 and  EXTRACT(YEAR FROM  dateofbirth )=" + startYear + ") "),
//                    Restrictions.sqlRestriction("(EXTRACT(MONTH FROM  dateofbirth ) between 1 and 3 and  EXTRACT(YEAR FROM  dateofbirth )=" + endYear + ") ")));
            empCrit.addOrder(Order.asc("dateofbirth"));

            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
                stringBuff.append("<tr >");
                if (employeemasterObj.getRegion().equalsIgnoreCase("TRANS")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion().equalsIgnoreCase("RET")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion() != null) {
                    stringBuff.append("<td >" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }

                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        String regionid = "";
        String regionname = "";

        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.addOrder(Order.asc("regionname"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {
                regionid = lbobj.getId();
                regionname = lbobj.getRegionname();
                regionMap.put(regionid, regionname);
                System.out.println(regionid + "->" + regionname);
            }
            resultMap.put("regionlist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionwiseEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String selecteddate) {
        Map resultMap = new HashMap();
        String birthArray[];
        int retirementyear = 0;
        try {


            StringBuffer stringBuff = new StringBuffer();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th>Region Name</th>");
            stringBuff.append("<th>EPF No</th>");
            stringBuff.append("<th>Employee Name</th>");
            stringBuff.append("<th>Father Name</th>");
            stringBuff.append("<th>Designation</th>");
            stringBuff.append("<th>Date of Birth</th>");
            stringBuff.append("<th>Date of Appointment</th>");
            stringBuff.append("<th>Date of Retirement</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("CASE WHEN designation like 'LE%' THEN  to_date(EXTRACT(year from dateofbirth + interval '60 years')::text,'yyyy-mm-dd') >'2010-01-01' ELSE  to_date(EXTRACT(year from dateofbirth + interval '58 years')::text,'yyyy-mm-dd') >'" + postgresDate(selecteddate) + "' END"));
//            empCrit.add(Restrictions.sqlRestriction("CASE WHEN designation ='LE15' THEN  to_date(EXTRACT(year from dateofbirth + interval '60 years')::text,'yyyy-mm-dd') >'2010-01-01' ELSE  to_date(EXTRACT(year from dateofbirth + interval '58 years')::text,'yyyy-mm-dd') >'" + postgresDate(selecteddate) + "' END"));
//            empCrit.add(Restrictions.sqlRestriction("CASE WHEN designation ='LE05' THEN  to_date(EXTRACT(year from dateofbirth + interval '60 years')::text,'yyyy-mm-dd') >'2010-01-01' ELSE  to_date(EXTRACT(year from dateofbirth + interval '58 years')::text,'yyyy-mm-dd') >'" + postgresDate(selecteddate) + "' END"));
//            empCrit.add(Restrictions.sqlRestriction("CASE WHEN designation ='LE05' THEN  to_date(EXTRACT(year from dateofbirth + interval '60 years')::text,'yyyy-mm-dd') >'2010-01-01' ELSE  to_date(EXTRACT(year from dateofbirth + interval '58 years')::text,'yyyy-mm-dd') >'" + postgresDate(selecteddate) + "' END"));
            empCrit.add(Restrictions.sqlRestriction("dateofbirth is not null"));
            // empCrit.add(Restrictions.sqlRestriction("region is not null"));
//        empCrit.add(Restrictions.ge("dateofbirth", postgresDate(selecteddate)));
//            empCrit.add(Restrictions.sqlRestriction("to_date(EXTRACT(year from dateofbirth + interval '58 years')::text,'yyyy-mm-dd') >'" + postgresDate(selecteddate) + "'"));
            empCrit.addOrder(Order.asc("dateofbirth"));
            List empList = empCrit.list();
            for (int i = 0; i < empList.size(); i++) {
                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
//             java.util.Date currentDate = new java.util.Date(employeemasterObj.getDateofbirth());
//            System.out.println("Current Date : " + currentDate);
//            String calculatedDate =dateToString(addYears(currentDate, 58));
//            if(DateUtility.DateGreaterThan(selecteddate, calculatedDate, "dd/MM/yyyy")){
//
//            }
                stringBuff.append("<tr >");
                if (employeemasterObj.getRegion().equalsIgnoreCase("TRANS")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion().equalsIgnoreCase("RET")) {
                    stringBuff.append("<td >" + employeemasterObj.getRegion() + "</td>");
                } else if (employeemasterObj.getRegion() != null) {
                    stringBuff.append("<td >" + getRegionmaster(session, employeemasterObj.getRegion()).getRegionname() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }
                stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
                if (employeemasterObj.getDesignation() != null) {
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                } else {
                    stringBuff.append("<td >" + "Unknown" + "</td>");
                }

                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofbirth()) + "</td>");
                stringBuff.append("<td >" + dateToString(employeemasterObj.getDateofappoinment()) + "</td>");
                if (employeemasterObj.getDateofbirth() != null) {
                    birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
                    if (employeemasterObj.getDesignation().substring(0, 2).equalsIgnoreCase("LE")) {
//                        if(employeemasterObj.getDesignation().equalsIgnoreCase("LE05") || employeemasterObj.getDesignation().equalsIgnoreCase("LE15")){
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(11, retirementyear - 1) + "</td>");
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear) + "</td>");
                    } else {
                        stringBuff.append("<td >" + CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear) + "</td>");
                    }
//                        stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                } else {
                    stringBuff.append("<td > - </td>");
                }
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                if (employeemasterObj.getDesignation() == null) {
//                    retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                } else {
//                    if (employeemasterObj.getDesignation().equalsIgnoreCase("LE15")) {
//                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
//                    } else {
//                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                    }
//                }

//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
                stringBuff.append("</tr>");
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");

            resultMap.put("regionwiseemployee", stringBuff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }
//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map getALLRegionMonthwiseAppointmentEmployees(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year) {
//        Map resultMap = new HashMap();
//        int startYear = 0;
//        int selectmonth = 0;
//        String birthArray[];
//        int retirementyear = 0;
//
//        try {
//            StringBuffer stringBuff = new StringBuffer();
//            selectmonth = Integer.parseInt(month);
//            startYear = Integer.parseInt(year.toString());
//            if (selectmonth == 1 || selectmonth == 2 || selectmonth == 3) {
//                startYear = startYear + 1;
//            }
//
//
//            stringBuff.append("<FONT SIZE=2>");
//            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"emptable\">");
//            stringBuff.append("<thead>");
//            stringBuff.append("<tr>");
//            stringBuff.append("<th>Region Name</th>");
//            stringBuff.append("<th>EPF No</th>");
//            stringBuff.append("<th>Employee Name</th>");
//            stringBuff.append("<th>Father Name</th>");
//            stringBuff.append("<th>Designation</th>");
//            stringBuff.append("<th>Date of Birth</th>");
//            stringBuff.append("<th>Date of Appointment</th>");
//            stringBuff.append("<th>Date of Retirement</th>");
//            stringBuff.append("</tr>");
//            stringBuff.append("</thead>");
//            stringBuff.append("<tbody>");
//
//
//            Criteria empCrit = session.createCriteria(Employeemaster.class);
////            empCrit.add(Restrictions.sqlRestriction("region='R01'"));
//            empCrit.add(Restrictions.sqlRestriction("region is not null"));
//            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(MONTH FROM  dateofappoinment )=" + Integer.parseInt(month)));
//            empCrit.add(Restrictions.sqlRestriction(" EXTRACT(YEAR FROM  dateofappoinment )=" + startYear));
//            empCrit.addOrder(Order.asc("dateofappoinment"));
////            empCrit.addOrder(Order.asc("EXTRACT(YEAR FROM  dateofbirth )"));
////            empCrit.addOrder(Order.asc("EXTRACT(MONTH FROM  dateofbirth )"));
//            List empList = empCrit.list();
//            for (int i = 0; i < empList.size(); i++) {
//                Employeemaster employeemasterObj = (Employeemaster) empList.get(i);
//                stringBuff.append("<tr >");
//                stringBuff.append("<td >" + employeemasterObj.getRegionmaster().getRegionname() + "</td>");
//                stringBuff.append("<td >" + employeemasterObj.getEmployeeepfmaster().getEpfno() + "</td>");
//                stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
//                stringBuff.append("<td >" + employeemasterObj.getFathername() + "</td>");
//                stringBuff.append("<td >" + employeemasterObj.getDesignation() + "</td>");
//                stringBuff.append("<td >" + employeemasterObj.getDateofbirth() + "</td>");
//                stringBuff.append("<td >" + employeemasterObj.getDateofappoinment() + "</td>");
//                birthArray = dateToString(employeemasterObj.getDateofbirth()).split("/");
//                retirementyear = Integer.parseInt(birthArray[2]) + 58;
//                stringBuff.append("<td >" + retirementyear + "-" + birthArray[1] + "-" + birthArray[0] + "</td>");
//                stringBuff.append("</tr>");
//            }
//            stringBuff.append("</tbody>");
//            stringBuff.append("</table>");
//            stringBuff.append("</FONT>");
//
//            resultMap.put("regionmonthwiseemployee", stringBuff.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return resultMap;
//    }
   
    @GlobalDBOpenCloseAndUserPrivilages
    @Override
    public Map edliEmployeeReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String category, String filePathwithName) {        
        Map map = new HashMap();
        EDLIReport eDLIReport = new EDLIReport();
        try {
            int slipno = 1;
            String categorytxt = "";
            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }
            List<String> headerlist = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();            
//            builder.append("select rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment,dm.designationcode,dm.remarks from employeemaster em ");
//            builder.append("left join regionmaster rm on rm.id=em.region ");
//            builder.append("left join designationmaster dm on dm.designationcode=em.designation ");
//            builder.append("where em.region='" + LoggedInRegion + "' and  em.category='" + category + "' ");
//            builder.append("order by em.section,dm.orderno");
            Calendar curcal = Calendar.getInstance();
            int currentYear = curcal.get(Calendar.YEAR);
            builder.append("select rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment, ");
            builder.append("dm.designationcode,dm.remarks ,sum(eet.amount) as earningsamt ");
            builder.append("from employeemaster em  ");
            builder.append("left join regionmaster rm on rm.id=em.region  ");
            builder.append("left join designationmaster dm on dm.designationcode=em.designation  ");
            builder.append("left join paycodemaster pm on pm.paycode in ('E01','E02','E03','E04','E25') ");
            builder.append("left join payrollprocessingdetails pp on pp.employeeprovidentfundnumber = em.epfno  ");
            builder.append("and pp.year= " + currentYear + "  and pp.month=4  and pp.accregion='" + LoggedInRegion + "' and pp.process  is true  ");
            builder.append("left join employeeearningstransactions eet on eet.payrollprocessingdetailsid=pp.id and eet.earningmasterid = pm.paycode  ");
            builder.append("and eet.cancelled is false where em.region='" + LoggedInRegion + "' and  em.category='" + category + "'  ");
            builder.append("group by rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment, ");
            builder.append("dm.designationcode,dm.remarks order by em.section,dm.orderno ");
            
            SQLQuery employeequery = session.createSQLQuery(builder.toString());

            if (employeequery.list().size() == 0) {
                // <editor-fold defaultstate="collapsed" desc="employeequery Loop">
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
                // </editor-fold>
            }
            List<EDLIModel> contentlist = new ArrayList<EDLIModel>();
            EDLIModel edlim = null;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Employee Query">
                Object[] rows = (Object[]) its.next();
                edlim = new EDLIModel();                
                edlim.setSlipno(slipno+"");
                edlim.setRegionname((String) rows[0]);
                edlim.setPfno((String) rows[1]);
                edlim.setGender((String) rows[2]);
                edlim.setEmployeename((String) rows[3]);
                String designation = (String) rows[4];
                edlim.setDesignation(designation);
                Date dob = (Date) rows[5];
                edlim.setDateofbirth(dateToString(dob));
                Date doa = (Date) rows[6];
                edlim.setDateofappoinment(dateToString(doa));
                String designationcode = (String) rows[7];
                String designationgroup = (String) rows[8];
                edlim.setEarningsamount((rows[9]!=null)?rows[9].toString():" ");
                int retirementyear =0;
                if ((dob != null) && (designation != null)) {
                    String birthArray[] = dateToString(dob).split("/");
                    if ((designationcode.substring(0, 2).equalsIgnoreCase("LE"))&&("OA".equalsIgnoreCase(designationgroup))) {
                        retirementyear = Integer.parseInt(birthArray[2]) + 60;
                    } else {
                        retirementyear = Integer.parseInt(birthArray[2]) + 58;
                    }
                    if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                        edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(11, retirementyear - 1));
                    } else if (Integer.parseInt(birthArray[0]) == 1) {
                        edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear));
                    } else {
                        edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear));
                    }
                } else {
                    edlim.setDateofconfirmation("");
                }
                if(!"".equalsIgnoreCase(edlim.getDateofconfirmation())){
                    String monyear[] = edlim.getDateofconfirmation().split(" ");
                    edlim.setRetmonth(monyear[0]);
                    edlim.setRetyear(monyear[1]);
                }else{
                    edlim.setRetmonth("");
                    edlim.setRetyear("");
                }
                contentlist.add(edlim);
                slipno++;
                //</editor-fold>
            }
            map.put("headerlist", headerlist);
            map.put("contentlist", contentlist);
            map.put("regionname", regionname);            
            if("R".equalsIgnoreCase(category)){
                categorytxt = "Regular";
            }else if("S".equalsIgnoreCase(category)){
                categorytxt = "Seasonal";
            }else if("L".equalsIgnoreCase(category)){
                categorytxt = "Load Man";
            }
            map.put("category", categorytxt);
            eDLIReport.GenerateEDLIEmployeeDetailsexcel(map, filePathwithName);
        } catch (Exception ex) {
            map.put("ERROR", "EDLI Employee Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }
    @GlobalDBOpenCloseAndUserPrivilages
    @Override
    public Map edliEmployeeEarningsDeductionPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String ayear, String amonth, String region, String category, String reporttype, String filePathwithName) {
        Map map = new HashMap();
        if ("3".equalsIgnoreCase(reporttype)) {
            edliEmployeeNetSalaryReportPrintOut(session, request, response, LoggedInRegion, LoggedInUser, syear, smonth, ayear, amonth, region, category, filePathwithName, reporttype);
        }else {
            map = edliEmployeeEarningsReportPrintOut(session, request, response, LoggedInRegion, LoggedInUser, syear, smonth, ayear, amonth, region, category, filePathwithName, reporttype);
        }
        return map;
    }
    
    public Map edliEmployeeEarningsReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String syear, String smonth, String ayear, String amonth, String region, String category, String filePathwithName, String reporttype) {
        Map map = new HashMap();
        EDLIReport eDLIReport = new EDLIReport();
        try {
            int slipno = 1;
            String categorytxt = "";
            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }
            List<String> headerlist = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();    
            int s_month = Integer.valueOf(smonth) + 1;
            int a_month = Integer.valueOf(amonth) + 1;
            String startDate =syear+"-"+s_month+"-01";
            String endDate =ayear+"-"+a_month+"-01";
            if ("1".equalsIgnoreCase(reporttype)) {
                builder.append("select rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment,  ");
                builder.append("dm.designationcode,dm.remarks ,sum(eet.amount) as earningsamt ,pm.paycode ");
                builder.append("from  employeeearningstransactions eet ");
                builder.append("left join paycodemaster pm on pm.paycodetype='E' ");
                builder.append("join payrollprocessingdetails as pp on eet.payrollprocessingdetailsid=pp.id and eet.earningmasterid = pm.paycode  and eet.cancelled is false  ");
                builder.append("left join employeemaster em  on pp.employeeprovidentfundnumber = em.epfno   ");
                builder.append("left join regionmaster rm on rm.id=em.region   ");
                builder.append("left join designationmaster dm on dm.designationcode=em.designation   ");
                builder.append("where eet.accregion='" + LoggedInRegion + "' and  em.category='" + category + "'  ");
                builder.append("and to_timestamp(pp.year||'-'||pp.month,'YYYY-MM') between '" + startDate + "' and '" + endDate + "'  ");
                builder.append("and pp.accregion='" + LoggedInRegion + "' and pp.process  is true ");
                builder.append("group by rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment, dm.designationcode,dm.remarks ,pm.paycode ");
                builder.append("order by em.section,dm.orderno ,em.epfno,pm.paycode ");
            } else if ("2".equalsIgnoreCase(reporttype)) {
                builder.append("select rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment,  ");
                builder.append("dm.designationcode,dm.remarks , sum(eet.amount) as deductionamt,pm.paycode ");
                builder.append("from  employeedeductionstransactions eet ");
                builder.append("left join paycodemaster pm on pm.paycodetype in ('D','L') ");
                builder.append("join payrollprocessingdetails as pp on eet.payrollprocessingdetailsid=pp.id and eet.deductionmasterid = pm.paycode  and eet.cancelled is false  ");
                builder.append("left join employeemaster em  on pp.employeeprovidentfundnumber = em.epfno   ");
                builder.append("left join regionmaster rm on rm.id=em.region   ");
                builder.append("left join designationmaster dm on dm.designationcode=em.designation   ");
                builder.append("where eet.accregion='" + LoggedInRegion + "' and  em.category='" + category + "'  ");
                builder.append("and to_timestamp(pp.year||'-'||pp.month,'YYYY-MM') between '" + startDate + "' and '" + endDate + "'  ");
                builder.append("and pp.accregion='" + LoggedInRegion + "' and pp.process  is true ");
                builder.append("group by rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment, dm.designationcode,dm.remarks ,pm.paycode ");
                builder.append("order by em.section,dm.orderno ,em.epfno,pm.paycode  ");
            }
            SQLQuery employeequery = session.createSQLQuery(builder.toString());

            if (employeequery.list().size() == 0) {
                // <editor-fold defaultstate="collapsed" desc="employeequery Loop">
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
                // </editor-fold>
            }
            List<EDLIModel> contentlist = new ArrayList<EDLIModel>();
            EDLIModel edlim = null;
            String epfno = "";
            String epfnorow = "";
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Employee Query">
                Object[] rows = (Object[]) its.next();
                epfno = (String) rows[1];
                String paycode = (String) rows[10];
                if (!"".equals(epfnorow) && epfno.equalsIgnoreCase(epfnorow)) {
                    if ("1".equalsIgnoreCase(reporttype)) {
                        edlim = fillEarningAmount(edlim, paycode, (rows[9] != null) ? rows[9].toString() : "0");
                    } else {
                        edlim = fillDeducationAmount(edlim, paycode, (rows[9] != null) ? rows[9].toString() : "0");
                    }
                } else {
                    edlim = new EDLIModel();
                    edlim.setSlipno(slipno + "");
                    edlim.setRegionname((String) rows[0]);
                    edlim.setPfno((String) rows[1]);
                    edlim.setGender((String) rows[2]);
                    edlim.setEmployeename((String) rows[3]);
                    String designation = (String) rows[4];
                    edlim.setDesignation(designation);
                    Date dob = (Date) rows[5];
                    edlim.setDateofbirth(dateToString(dob));
                    Date doa = (Date) rows[6];
                    edlim.setDateofappoinment(dateToString(doa));
                    String designationcode = (String) rows[7];
                    String designationgroup = (String) rows[8];
                    if ("1".equalsIgnoreCase(reporttype)) {
                        edlim = fillEarningAmount(edlim, paycode, (rows[9] != null) ? rows[9].toString() : "0");
                    } else {
                        edlim = fillDeducationAmount(edlim, paycode, (rows[9] != null) ? rows[9].toString() : "0");
                    }
                    int retirementyear = 0;
                    if ((dob != null) && (designation != null)) {
                        String birthArray[] = dateToString(dob).split("/");
                        if ((designationcode.substring(0, 2).equalsIgnoreCase("LE")) && ("OA".equalsIgnoreCase(designationgroup))) {
                            retirementyear = Integer.parseInt(birthArray[2]) + 60;
                        } else {
                            retirementyear = Integer.parseInt(birthArray[2]) + 58;
                        }
                        if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                            edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(11, retirementyear - 1));
                        } else if (Integer.parseInt(birthArray[0]) == 1) {
                            edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear));
                        } else {
                            edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear));
                        }
                    } else {
                        edlim.setDateofconfirmation("");
                    }
                    if (!"".equalsIgnoreCase(edlim.getDateofconfirmation())) {
                        String monyear[] = edlim.getDateofconfirmation().split(" ");
                        edlim.setRetmonth(monyear[0]);
                        edlim.setRetyear(monyear[1]);
                    } else {
                        edlim.setRetmonth("");
                        edlim.setRetyear("");
                    }
                    epfnorow = epfno;
                    contentlist.add(edlim);
                    slipno++;
                }

                //</editor-fold>
            }
            map.put("headerlist", headerlist);
            map.put("contentlist", contentlist);
            map.put("regionname", regionname);            
            if("R".equalsIgnoreCase(category)){
                categorytxt = "Regular";
            }else if("S".equalsIgnoreCase(category)){
                categorytxt = "Seasonal";
            }else if("L".equalsIgnoreCase(category)){
                categorytxt = "Load Man";
            }
            map.put("category", categorytxt);            
            eDLIReport.GenerateEDLIEmployeeEarningsDeductionDetailsexcel(map, filePathwithName, reporttype);
        } catch (Exception ex) {
            map.put("ERROR", "EDLI Employee Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }
            
    public Map edliEmployeeNetSalaryReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String syear, String smonth, String ayear, String amonth, String region, String category, String filePathwithName, String reporttype) {
        Map map = new HashMap();
        EDLIReport eDLIReport = new EDLIReport();
        try {
            int slipno = 1;
            String categorytxt = "";
            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }
            List<String> headerlist = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();    
            int s_month = Integer.valueOf(smonth) + 1;
            int a_month = Integer.valueOf(amonth) + 1;
            String startDate =syear+"-"+s_month+"-01";
            String endDate =ayear+"-"+a_month+"-01";
            
            builder.append("select regionname,epfno,gender,employeename,designation,dateofbirth,dateofappoinment,  ");
            builder.append("designationcode,remarks ,sum(earningsamt) as earningsamt ,sum(deductionamt) as deductionamt,designationgroup ");
            builder.append(" from (( select rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment,  ");
            builder.append("dm.designationcode,dm.remarks ,sum(eet.amount) as earningsamt ,0 as deductionamt, dm.designationgroup ");
            builder.append("from  employeeearningstransactions eet ");
            builder.append("left join paycodemaster pm on pm.paycodetype='E' ");
            builder.append("join payrollprocessingdetails as pp on eet.payrollprocessingdetailsid=pp.id and eet.earningmasterid = pm.paycode  and eet.cancelled is false  ");
            builder.append("left join employeemaster em  on pp.employeeprovidentfundnumber = em.epfno   ");
            builder.append("left join regionmaster rm on rm.id=em.region   ");
            builder.append("left join designationmaster dm on dm.designationcode=em.designation   ");
            builder.append("where eet.accregion='" + LoggedInRegion + "' and  em.category='" + category + "'  ");
            builder.append("and to_timestamp(pp.year||'-'||pp.month,'YYYY-MM') between '" + startDate + "' and '" + endDate + "'  ");
            builder.append("and pp.accregion='" + LoggedInRegion + "' and pp.process  is true ");
            builder.append("group by rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment, dm.designationcode,dm.remarks ,pm.paycode,dm.designationgroup ");
            builder.append("order by em.section,dm.orderno ,em.epfno,pm.paycode ) union all ( ");
            builder.append("select rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment,  ");
            builder.append("dm.designationcode,dm.remarks ,0 as earningsamt, sum(eet.amount) as deductionamt,dm.designationgroup ");
            builder.append("from  employeedeductionstransactions eet ");
            builder.append("left join paycodemaster pm on pm.paycodetype in ('D','L') ");
            builder.append("join payrollprocessingdetails as pp on eet.payrollprocessingdetailsid=pp.id and eet.deductionmasterid = pm.paycode  and eet.cancelled is false  ");
            builder.append("left join employeemaster em  on pp.employeeprovidentfundnumber = em.epfno   ");
            builder.append("left join regionmaster rm on rm.id=em.region   ");
            builder.append("left join designationmaster dm on dm.designationcode=em.designation   ");
            builder.append("where eet.accregion='" + LoggedInRegion + "' and  em.category='" + category + "'  ");
            builder.append("and to_timestamp(pp.year||'-'||pp.month,'YYYY-MM') between '" + startDate + "' and '" + endDate + "'  ");
            builder.append("and pp.accregion='" + LoggedInRegion + "' and pp.process  is true ");
            builder.append("group by rm.regionname,em.epfno,em.gender,em.employeename,dm.designation,em.dateofbirth,em.dateofappoinment, dm.designationcode,dm.remarks ,pm.paycode,dm.designationgroup ");
            builder.append("order by em.section,dm.orderno ,em.epfno,pm.paycode ))  as earded  ");
            builder.append("group by  regionname,epfno,gender,employeename,designation,dateofbirth,dateofappoinment,  ");
            builder.append("designationcode,remarks,designationgroup order by designationgroup,epfno ");
            
            SQLQuery employeequery = session.createSQLQuery(builder.toString());

            if (employeequery.list().size() == 0) {
                // <editor-fold defaultstate="collapsed" desc="employeequery Loop">
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
                // </editor-fold>
            }
            List<EDLIModel> contentlist = new ArrayList<EDLIModel>();
            EDLIModel edlim = null;            
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Employee Query">
                Object[] rows = (Object[]) its.next();
                    edlim = new EDLIModel();
                    edlim.setSlipno(slipno + "");
                    edlim.setRegionname((String) rows[0]);
                    edlim.setPfno((String) rows[1]);
                    edlim.setGender((String) rows[2]);
                    edlim.setEmployeename((String) rows[3]);
                    String designation = (String) rows[4];
                    edlim.setDesignation(designation);
                    Date dob = (Date) rows[5];
                    edlim.setDateofbirth(dateToString(dob));
                    Date doa = (Date) rows[6];
                    edlim.setDateofappoinment(dateToString(doa));
                    String designationcode = (String) rows[7];
                    String dgroup = (String) rows[8];
                    BigDecimal earamount = new BigDecimal((rows[9]!=null)?rows[9].toString():"0");
                    BigDecimal dedamount = new BigDecimal((rows[10]!=null)?rows[10].toString():"0");
                    edlim.setEarningsamount(earamount+"");
                    edlim.setDeductionamount(dedamount+"");
                    BigDecimal totalamt = earamount.subtract(dedamount);
                    edlim.setNetamount(totalamt+"");
                    
                    totalearningsamount = new BigDecimal(earamount+"").add(totalearningsamount);
                    edlim.setTotalearningsamount(totalearningsamount+"");
                    totaldeductionamount = new BigDecimal(dedamount+"").add(totaldeductionamount);
                    edlim.setTotaldeductionamount(totaldeductionamount+"");
                    totalnetamount = new BigDecimal(totalamt+"").add(totalnetamount);
                    edlim.setTotalnetamount(totalnetamount+"");                    
                    int retirementyear = 0;
                    if ((dob != null) && (designation != null)) {
                        String birthArray[] = dateToString(dob).split("/");
                        if ((designationcode.substring(0, 2).equalsIgnoreCase("LE")) && ("OA".equalsIgnoreCase(dgroup))) {
                            retirementyear = Integer.parseInt(birthArray[2]) + 60;
                        } else {
                            retirementyear = Integer.parseInt(birthArray[2]) + 58;
                        }
                        if (Integer.parseInt(birthArray[0]) == 1 && Integer.parseInt(birthArray[1]) == 1) {
                            edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(11, retirementyear - 1));
                        } else if (Integer.parseInt(birthArray[0]) == 1) {
                            edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 2, retirementyear));
                        } else {
                            edlim.setDateofconfirmation(CommonUtility.getMonthAndYear(Integer.parseInt(birthArray[1]) - 1, retirementyear));
                        }
                    } else {
                        edlim.setDateofconfirmation("");
                    }
                    if (!"".equalsIgnoreCase(edlim.getDateofconfirmation())) {
                        String monyear[] = edlim.getDateofconfirmation().split(" ");
                        edlim.setRetmonth(monyear[0]);
                        edlim.setRetyear(monyear[1]);
                    } else {
                        edlim.setRetmonth("");
                        edlim.setRetyear("");
                    }
                    String designationgroup = (String) rows[11];
                    if("A".equalsIgnoreCase(designationgroup)){
                        aGroupearTotal = earamount.add(aGroupearTotal);
                        aGroupdedTotal = dedamount.add(aGroupdedTotal);
                        aGroupnetTotal = totalamt.add(aGroupnetTotal);
                    }else if("B".equalsIgnoreCase(designationgroup)){
                        if (agroup) {
                            EDLIModel edlim1 = new EDLIModel();
                            edlim1.setEarningsamount("groupwise");
                            edlim1.setDesignationEargrouptotal(aGroupearTotal + "");
                            edlim1.setDesignationDedgrouptotal(aGroupdedTotal + "");
                            edlim1.setDesignationNetgrouptotal(aGroupnetTotal + "");
                            contentlist.add(edlim1);
                            agroup = false;
                        }
                        bGroupearTotal = earamount.add(bGroupearTotal);
                        bGroupdedTotal = dedamount.add(bGroupdedTotal);
                        bGroupnetTotal = totalamt.add(bGroupnetTotal);
                    }else if("C".equalsIgnoreCase(designationgroup)){
                        if (bgroup) {
                            EDLIModel edlim1 = new EDLIModel();
                            edlim1.setEarningsamount("groupwise");
                            edlim1.setDesignationEargrouptotal(bGroupearTotal + "");
                            edlim1.setDesignationDedgrouptotal(bGroupdedTotal + "");
                            edlim1.setDesignationNetgrouptotal(bGroupnetTotal + "");
                            contentlist.add(edlim1);
                            bgroup = false;
                        }
                        cGroupearTotal = earamount.add(cGroupearTotal);
                        cGroupdedTotal = dedamount.add(cGroupdedTotal);
                        cGroupnetTotal = totalamt.add(cGroupnetTotal);
                    }else if("D".equalsIgnoreCase(designationgroup)){
                        if (cgroup) {
                            EDLIModel edlim1 = new EDLIModel();
                            edlim1.setEarningsamount("groupwise");
                            edlim1.setDesignationEargrouptotal(cGroupearTotal + "");
                            edlim1.setDesignationDedgrouptotal(cGroupdedTotal + "");
                            edlim1.setDesignationNetgrouptotal(cGroupnetTotal + "");
                            contentlist.add(edlim1);
                            cgroup = false;
                        }
                        dGroupearTotal = earamount.add(dGroupearTotal);
                        dGroupdedTotal = dedamount.add(dGroupdedTotal);
                        dGroupnetTotal = totalamt.add(dGroupnetTotal);
                    }
                    edlim.setDesignationgroup(designationgroup);
                    contentlist.add(edlim);
                    slipno++;               

                //</editor-fold>
            }            
            EDLIModel edlim1 = new EDLIModel();            
            edlim1.setEarningsamount("groupwise");
            edlim1.setDesignationEargrouptotal(dGroupearTotal + "");
            edlim1.setDesignationDedgrouptotal(dGroupdedTotal + "");
            edlim1.setDesignationNetgrouptotal(dGroupnetTotal + "");            
            contentlist.add(edlim1);
            edlim1 = new EDLIModel();
            edlim1.setTotalearningsamount(totalearningsamount+"");
            edlim1.setTotaldeductionamount(totaldeductionamount+"");
            edlim1.setTotalnetamount(totalnetamount+"");
            contentlist.add(edlim1);
            
            map.put("headerlist", headerlist);
            map.put("contentlist", contentlist);
            map.put("regionname", regionname);            
            if("R".equalsIgnoreCase(category)){
                categorytxt = "Regular";
            }else if("S".equalsIgnoreCase(category)){
                categorytxt = "Seasonal";
            }else if("L".equalsIgnoreCase(category)){
                categorytxt = "Load Man";
            }
            map.put("category", categorytxt);
            eDLIReport.GenerateEDLIEmployeeEarningsDeductionDetailsexcel(map, filePathwithName, reporttype);
        } catch (Exception ex) {
            map.put("ERROR", "EDLI Employee Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }
    
    BigDecimal totalAmount= new BigDecimal(0);
    BigDecimal totalearningsamount= new BigDecimal(0);
    BigDecimal totaldeductionamount= new BigDecimal(0);
    BigDecimal totalnetamount= new BigDecimal(0);
    BigDecimal aGroupearTotal= new BigDecimal(0);
    BigDecimal bGroupearTotal= new BigDecimal(0);
    BigDecimal cGroupearTotal= new BigDecimal(0);
    BigDecimal dGroupearTotal= new BigDecimal(0);
    BigDecimal aGroupdedTotal= new BigDecimal(0);
    BigDecimal bGroupdedTotal= new BigDecimal(0);
    BigDecimal cGroupdedTotal= new BigDecimal(0);
    BigDecimal dGroupdedTotal= new BigDecimal(0);
    BigDecimal aGroupnetTotal= new BigDecimal(0);
    BigDecimal bGroupnetTotal= new BigDecimal(0);
    BigDecimal cGroupnetTotal= new BigDecimal(0);
    BigDecimal dGroupnetTotal= new BigDecimal(0);
    boolean agroup =true;
    boolean bgroup =true;
    boolean cgroup =true;
    private EDLIModel fillEarningAmount(EDLIModel edli, String paycode, String amt) {
        if ("E01".equalsIgnoreCase(paycode)) {
            edli.setBasicpay(amt);
        } else if ("E02".equalsIgnoreCase(paycode)) {
            edli.setSplpay(amt);
        } else if ("E03".equalsIgnoreCase(paycode)) {
            edli.setPerpay(amt);
        } else if ("E04".equalsIgnoreCase(paycode)) {
            edli.setDa(amt);
        } else if ("E06".equalsIgnoreCase(paycode)) {
            edli.setHra(amt);
        } else if ("E07".equalsIgnoreCase(paycode)) {
            edli.setCca(amt);
        } else if ("E25".equalsIgnoreCase(paycode)) {
            edli.setGradepay(amt);
        } else if ("E10".equalsIgnoreCase(paycode)) {
            edli.setConvallow(amt);
        } else {
            double othAmount = Double.parseDouble(edli.getOtheramounts());
            edli.setOtheramounts(Double.parseDouble(amt)+othAmount+"");
        }
        double earningsamount = Double.parseDouble(edli.getEarningsamount());
        edli.setEarningsamount(Double.parseDouble(amt)+earningsamount+"");
        
        totalAmount = new BigDecimal(amt).add(totalAmount) ;
        edli.setTotalearningsamount(totalAmount+"");
        
        return edli;
    }
    
    private EDLIModel fillDeducationAmount(EDLIModel edli, String paycode, String amt) {
        if (("D01".equalsIgnoreCase(paycode)) ||("D02".equalsIgnoreCase(paycode))) {
            double gpfAmount = Double.parseDouble(edli.getGpf());
            edli.setGpf(Double.parseDouble(amt)+gpfAmount+"");            
        } else if ("D03".equalsIgnoreCase(paycode)) {
            edli.setVpf(amt);
        } else if ("D04".equalsIgnoreCase(paycode)) {
            edli.setSpf(amt);
        } else if ("D06".equalsIgnoreCase(paycode)) {
            edli.setFbf(amt);
        } else if ("D31".equalsIgnoreCase(paycode)) {
            edli.setHis(amt);
        } else if ("D12".equalsIgnoreCase(paycode)) {
            edli.setIncometax(amt);
        } else if ("L02".equalsIgnoreCase(paycode)) {
            edli.setEpfloan(amt);
        } else if ("L06".equalsIgnoreCase(paycode)) {
            edli.setFa(amt);
        } else {
            double othAmount = Double.parseDouble(edli.getOtheramounts());
            edli.setOtheramounts(Double.parseDouble(amt)+othAmount+"");
        }
        double deductionamount = Double.parseDouble(edli.getDeductionamount());
        edli.setDeductionamount(Double.parseDouble(amt)+deductionamount+"");
        
        totalAmount = new BigDecimal(amt).add(totalAmount) ;
        edli.setTotaldeductionamount(totalAmount+"");
        
        return edli;
    }
}
