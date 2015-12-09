/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.persistence.payroll.Employeemaster;
import com.onward.persistence.payroll.Paycodemaster;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.reports.RegionwiseReport;
import com.onward.reports.regionwise.ServiceRegisterDetailsReport;
import com.onward.valueobjects.MonthValueObject;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import com.onward.valueobjects.RecoveryDetail;
import com.onward.valueobjects.RegionwiseModel;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Prince vijayakumar M
 */
public class RegionwiseReportServiceImpl extends OnwardAction implements RegionwiseReportService {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private DecimalFormat decimalFormat = new DecimalFormat("####0.00");

    @GlobalDBOpenCloseAndUserPrivilages
    public String getGroupType(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("********************** RegionwiseReportServiceImpl class  getGroupType method is calling **********************");
        StringBuilder sb = new StringBuilder();
        try {
            String GROUPTYPEQUERY = "select grouphead from paycodemaster where grouphead!='' and grouphead is not null group by grouphead";
            SQLQuery grouptype_query = session.createSQLQuery(GROUPTYPEQUERY);
            sb.append("<select class=\"combobox\" name=\"grouptype\" id=\"grouptype\">");
            if (grouptype_query.list().size() > 0) {
                //sb.append("<option value=\"0\">" + "ALL" + "</option>");
                for (ListIterator it = grouptype_query.list().listIterator(); it.hasNext();) {
                    Object row = (Object) it.next();
                    String groupname = (String) row;
                    sb.append("<option value=\"" + groupname + "\">" + groupname + "</option>");
                }
            }
            sb.append("</select>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegularDeduction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, int month, int year, String epfno, String region, String grouptype, int slipno) {
//        System.out.println("********************* RegionwiseReportServiceImpl class getRegularDeduction method is calling *********************");
        Map map = new HashMap();
        List<PaySlip_Earn_Deduction_Model> schedulelist = null;
        StringBuilder buffer = new StringBuilder();
        try {

            StringBuilder sbre = new StringBuilder();
            sbre.append("select pp.employeeprovidentfundnumber, em.employeename, dm.designation,");
            sbre.append("pp.salarystructureid,dm.orderno,rm.regionname,pp.id ");
            sbre.append("from employeedeductionstransactions edt ");
            sbre.append("left join payrollprocessingdetails pp on pp.id=edt.payrollprocessingdetailsid ");
            sbre.append("left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno ");
            sbre.append("left join designationmaster dm on dm.designationcode= em.designation ");
            sbre.append("left join regionmaster rm on rm.id=pp.accregion ");
            sbre.append("where ");
            sbre.append("edt.cancelled is false ");
            sbre.append("and edt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') ");
            sbre.append("and pp.month=" + month + " ");
            sbre.append("and pp.year=" + year + " ");
            sbre.append("and pp.process is true ");
            sbre.append("and pp.accregion='" + region + "' ");
            sbre.append("and pp.employeeprovidentfundnumber='" + epfno + "' ");
            sbre.append("group by pp.employeeprovidentfundnumber, em.employeename, dm.designation, pp.section,pp.salarystructureid,dm.orderno,rm.regionname,pp.id ");
            sbre.append("order by pp.accregion,pp.section,dm.orderno ");

            SQLQuery employeequeryregular = session.createSQLQuery(sbre.toString());

            double regiontotal = 0;

            for (ListIterator its = employeequeryregular.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String PFNUMBER = "";
                String ENAME = "";
                String DESIG = "";
                String REG = "";
                String PAYMONTH = "";
                String GROUPNAME = "";

                PFNUMBER = (String) rows[0];
                ENAME = (String) rows[1];
                DESIG = SubString((String) rows[2], 12);
                String salarystructureid = (String) rows[3];
                REG = (String) rows[5];
                String payrollprocessid = (String) rows[6];

                String DEDUCTIONQUERY = "select edt.deductionmasterid,pm.paycodename,edt.amount,pm.paycodetype from employeedeductionstransactions edt "
                        + "left join paycodemaster pm on pm.paycode=edt.deductionmasterid "
                        + "where edt.payrollprocessingdetailsid = '" + payrollprocessid + "' "
                        + "and edt.cancelled is false "
                        + "and edt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "')";

                SQLQuery deduction_query = session.createSQLQuery(DEDUCTIONQUERY);

                schedulelist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                PaySlip_Earn_Deduction_Model psedm = null;
                for (ListIterator it1 = deduction_query.list().listIterator(); it1.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    psedm = new PaySlip_Earn_Deduction_Model();
                    Object[] row1 = (Object[]) it1.next();

                    String deductioncode = (String) row1[0];
                    psedm.setPaycodename(SubString((String) row1[1], 12));
                    BigDecimal bigamount = (BigDecimal) row1[2];
                    regiontotal += bigamount.doubleValue();
                    psedm.setDeductionamount(decimalFormat.format(bigamount.doubleValue()));
                    char ptype = (Character) row1[3];

                    String FILENOQUERY = null;

                    if (ptype == 'L') {
                        // <editor-fold defaultstate="collapsed" desc="Voucher">
                        FILENOQUERY = "select fileno from employeeloansandadvances ela "
                                + "left join employeeloansandadvancesdetails elad on elad.employeeloansandadvancesid=ela.id "
                                + "where ela.deductioncode='" + deductioncode + "' and elad.payrollprocessingdetailsid='" + payrollprocessid + "' "
                                + "and elad.cancelled is false";
                        // </editor-fold>
                    } else if (ptype == 'D') {
                        // <editor-fold defaultstate="collapsed" desc="Voucher">
                        FILENOQUERY = "select edd.dedn_no from employeedeductiondetails edd  "
                                + "left join salarystructure ss on ss.id=edd.salarystructureid where ss.id='" + salarystructureid + "' "
                                + "and edd.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') "
                                + "and edd.cancelled is false and edd.deductionmasterid='" + deductioncode + "'";
                        // </editor-fold>
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
                    // </editor-fold>
                }
                PAYMONTH = months[Integer.valueOf(month - 1)] + "/" + year;
                int x = 1;
                Iterator it = schedulelist.iterator();

                String className = "";
                className = (slipno % 2 == 0) ? "rowColor1" : "rowColor2";
                while (it.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) it.next();

                    if (x == 1) {
                        buffer.append("<tr class=\"" + className + "\">");
                        buffer.append("<td width=\"3%\" align=\"center\">" + slipno + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + PFNUMBER + "</td>");
                        buffer.append("<td width=\"17%\" align=\"left\">" + ENAME + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + DESIG + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getPaycodename() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getLicpolicyno() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getDeductionamount() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + PAYMONTH + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + REG + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">Regular</td>");
                        buffer.append("</tr>");
//                            System.out.println(slipno + " | " + PFNUMBER + " | " + ENAME + " | " + DESIG + " | " + psedm1.getPaycodename() + " | " + psedm1.getLicpolicyno() + " | " + psedm1.getDeductionamount());
                    } else {
                        buffer.append("<tr class=\"" + className + "\">");
                        buffer.append("<td width=\"3%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"17%\" align=\"left\">" + "" + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getPaycodename() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getLicpolicyno() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getDeductionamount() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + "" + "</td>");
                        buffer.append("</tr>");
                    }
                    x++;
                    // </editor-fold>
                }
            }

            map.put("regularlist", buffer.toString());
            map.put("regiontotal", regiontotal);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getSupplementaryDeduction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, int month, int year, String epfno, String region, String grouptype, int slipno) {
//        System.out.println("********************* RegionwiseReportServiceImpl class getSupplementaryDeduction method is calling *********************");
        Map map = new HashMap();
        List<PaySlip_Earn_Deduction_Model> schedulelist = null;
        StringBuilder buffer = new StringBuilder();
        try {
            StringBuilder sbsup = new StringBuilder();
            sbsup.append("select spb.employeeprovidentfundnumber, em.employeename, dm.designation, ");
            sbsup.append("sss.id as salaryid, dm.orderno, rm.regionname, sppd.id as sprocessid, spb.accregion, spb.section,dm.orderno,sppd.calculatedyear,sppd.calculatedmonth from supplementaryemployeedeductionstransactions sedt ");
            sbsup.append("left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid ");
            sbsup.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
            sbsup.append("left join supplementarysalarystructure sss on sss.supplementarypayrollprocessingdetailsid=sppd.id ");
            sbsup.append("left join employeemaster em on spb.employeeprovidentfundnumber = em.epfno ");
            sbsup.append("left join designationmaster dm on dm.designationcode= em.designation ");
            sbsup.append("left join regionmaster rm on rm.id=spb.accregion ");
            sbsup.append("where spb.type='SUPLEMENTARYBILL' ");
            sbsup.append("and spb.cancelled is false ");
            sbsup.append("and sppd.cancelled is false ");
            sbsup.append("and sppd.calculatedmonth = " + month + " ");
            sbsup.append("and sppd.calculatedyear = " + year + " ");
            sbsup.append("and sedt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') ");
            sbsup.append("and sedt.cancelled is false ");
            sbsup.append("and spb.accregion='" + region + "' ");
            sbsup.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
            sbsup.append("group by spb.employeeprovidentfundnumber, em.employeename, dm.designation, sss.id, dm.orderno, rm.regionname, sppd.id, spb.accregion,spb.section,dm.orderno,sppd.calculatedyear,sppd.calculatedmonth ");
            sbsup.append("order by spb.accregion,sppd.calculatedyear,sppd.calculatedmonth,spb.section,dm.orderno");

            SQLQuery employeequerysupplementary = session.createSQLQuery(sbsup.toString());

            double regiontotal = 0;

            for (ListIterator its = employeequerysupplementary.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Voucher">
                Object[] rows = (Object[]) its.next();
                String PFNUMBER = "";
                String ENAME = "";
                String DESIG = "";
                String REG = "";
                String PAYMONTH = "";
                String GROUPNAME = "";
                String supplementarypayrollprocessid = "";
                String salarystructureid = "";

                PFNUMBER = (String) rows[0];
                ENAME = (String) rows[1];
                DESIG = SubString((String) rows[2], 12);
                salarystructureid = (String) rows[3];
                REG = (String) rows[5];
                supplementarypayrollprocessid = (String) rows[6];

                String DEDUCTIONQUERY = "select sedt.deductionmasterid,pm.paycodename,sedt.amount,pm.paycodetype "
                        + "from supplementaryemployeedeductionstransactions  sedt "
                        + "left join paycodemaster pm on pm.paycode=sedt.deductionmasterid "
                        + "left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid "
                        + "where sppd.id='" + supplementarypayrollprocessid + "' and sppd.cancelled is false and sedt.cancelled is false "
                        + "and sedt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "')";

                SQLQuery deduction_query = session.createSQLQuery(DEDUCTIONQUERY);

                schedulelist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                PaySlip_Earn_Deduction_Model psedm = null;
                for (ListIterator it1 = deduction_query.list().listIterator(); it1.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    psedm = new PaySlip_Earn_Deduction_Model();
                    Object[] row1 = (Object[]) it1.next();

                    String deductioncode = (String) row1[0];
                    psedm.setPaycodename(SubString((String) row1[1], 12));
                    BigDecimal bigamount = (BigDecimal) row1[2];
                    regiontotal += bigamount.doubleValue();
                    psedm.setDeductionamount(decimalFormat.format(bigamount.doubleValue()));
                    char ptype = (Character) row1[3];

                    String FILENOQUERY = null;

                    if (ptype == 'L') {
                        // <editor-fold defaultstate="collapsed" desc="Voucher">
                        FILENOQUERY = "select fileno from  employeeloansandadvances ela "
                                + "left join supplementaryemployeeloansandadvancesdetails selad on selad.employeeloansandadvancesid=ela.id "
                                + "where selad.supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessid + "' "
                                + "and ela.deductioncode='" + deductioncode + "' and selad.cancelled is false";
                        // </editor-fold>
                    } else if (ptype == 'D') {
                        // <editor-fold defaultstate="collapsed" desc="Voucher">
                        FILENOQUERY = "select dedn_no from supplementaryemployeedeductiondetails sedd "
                                + "left join salarystructure ss on ss.id=sedd.supplementarysalarystructureid "
                                + "where sedd.deductionmasterid='" + deductioncode + "' and sedd.cancelled is false and ss.id='" + salarystructureid + "'";
                        // </editor-fold>
                    }
                    SQLQuery fileno_query = session.createSQLQuery(FILENOQUERY);
//                    System.out.println("FILENOQUERY = " + FILENOQUERY);
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
//                    System.out.println("schedulelist.size() = " + schedulelist.size());
                    schedulelist.add(psedm);
                    // </editor-fold>
                }
                PAYMONTH = months[Integer.valueOf(month - 1)] + "/" + year;
                int x = 1;
                Iterator it = schedulelist.iterator();

                String className = "";
                className = (slipno % 2 == 0) ? "rowColor1" : "rowColor2";
                while (it.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) it.next();

                    if (x == 1) {
                        buffer.append("<tr class=\"" + className + "\">");
                        buffer.append("<td width=\"3%\" align=\"center\">" + slipno + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + PFNUMBER + "</td>");
                        buffer.append("<td width=\"17%\" align=\"left\">" + ENAME + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + DESIG + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getPaycodename() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getLicpolicyno() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getDeductionamount() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + PAYMONTH + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + REG + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">Supplementary</td>");
                        buffer.append("</tr>");
//                            System.out.println(slipno + " | " + PFNUMBER + " | " + ENAME + " | " + DESIG + " | " + psedm1.getPaycodename() + " | " + psedm1.getLicpolicyno() + " | " + psedm1.getDeductionamount());
                    } else {

                        buffer.append("<tr class=\"" + className + "\">");
                        buffer.append("<td width=\"3%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"17%\" align=\"left\">" + "" + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getPaycodename() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getLicpolicyno() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + psedm1.getDeductionamount() + "</td>");
                        buffer.append("<td width=\"7%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + "" + "</td>");
                        buffer.append("<td width=\"15%\" align=\"center\">" + "" + "</td>");
                        buffer.append("</tr>");
                    }
                    x++;
                    // </editor-fold>
                }
            }
            map.put("supplementarylist", buffer.toString());
            map.put("regiontotal", regiontotal);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getScheduleGridOld(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String grouptype, String region) {
        System.out.println("********************* RegionwiseReportServiceImpl class getScheduleGrid method is calling *****************");
        Map map = new HashMap();
        try {
            int s_year = Integer.valueOf(syear);
            int s_month = Integer.valueOf(smonth) + 1;
            int e_year = Integer.valueOf(eyear);
            int e_month = Integer.valueOf(emonth) + 1;
            String Group = grouptype;
            String EPF = epfno;
            String groupname = null;
            int slipno = 1;

            double grandtotal = 0;

            StringBuilder buffer = new StringBuilder();

            buffer.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr>");
            buffer.append("<td valign=\"top\">");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr class=\"gridmenu\">");
            buffer.append("<td width=\"3%\" align=\"center\">Sno</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Epfno</td>");
            buffer.append("<td width=\"17%\" align=\"center\">Name</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Designation</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Group Name</td>");
            buffer.append("<td width=\"7%\" align=\"center\">File No</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Amount</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Month&Year</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Region</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Type</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</td>");
            buffer.append("<tr>");
            buffer.append("<td valign=\"top\">");
            buffer.append("<div id=\"scrolling\">");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");

            Iterator itr = CommonUtility.getMonthandYearBetween(s_month, s_year, e_month, e_year).iterator();
            while (itr.hasNext()) {
                // <editor-fold defaultstate="collapsed" desc="Year,Month Loop">
                MonthValueObject mvo = (MonthValueObject) itr.next();
//                System.out.println(mvo.getMonth() + "/" + mvo.getYear());

                Criteria regionCrit = session.createCriteria(Regionmaster.class);
                if (!region.equals("0")) {
                    regionCrit.add(Restrictions.sqlRestriction("id = '" + region + "' "));
                }
                regionCrit.addOrder(Order.asc("id"));

                List regionlist = regionCrit.list();
                if (regionlist.size() > 0) {
                    for (int j = 0; j < regionlist.size(); j++) {
                        // <editor-fold defaultstate="collapsed" desc="Region List">
                        Regionmaster regionmaster = (Regionmaster) regionlist.get(j);
//                        System.out.println(regionmaster.getId() + " -> " + regionmaster.getRegionname() + "  " + mvo.getMonth() + "/" + mvo.getYear());
                        double regiontotal = 0;
                        String PAYMONTH = months[Integer.valueOf(mvo.getMonth() - 1)] + "/" + mvo.getYear();
                        Criteria employeeCrit = session.createCriteria(Employeemaster.class);
                        employeeCrit.add(Restrictions.sqlRestriction("region = '" + regionmaster.getId() + "' "));
                        if (epfno.length() > 0) {
                            employeeCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
                        }
                        employeeCrit.addOrder(Order.asc("region"));
                        List employeelist = employeeCrit.list();
                        if (employeelist.size() > 0) {
                            for (int i = 0; i < employeelist.size(); i++) {
                                // <editor-fold defaultstate="collapsed" desc="Employee List">
                                Employeemaster employeemaster = (Employeemaster) employeelist.get(i);
//                                System.out.println(employeemaster.getEpfno() + " | " + regionmaster.getRegionname() + " | " + mvo.getMonth() + "/" + mvo.getYear());
                                StringBuilder sbre = new StringBuilder();
                                StringBuilder sbsup = new StringBuilder();

                                String EPFNO = employeemaster.getEpfno();
                                String EMPLOYEENAME = employeemaster.getEmployeename();


                                sbre.append("select pp.employeeprovidentfundnumber, em.employeename, dm.designation,");
                                sbre.append("pp.salarystructureid,dm.orderno,rm.regionname,pp.id ");
                                sbre.append("from employeedeductionstransactions edt ");
                                sbre.append("left join payrollprocessingdetails pp on pp.id=edt.payrollprocessingdetailsid ");
                                sbre.append("left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno ");
                                sbre.append("left join designationmaster dm on dm.designationcode= em.designation ");
                                sbre.append("left join regionmaster rm on rm.id=pp.accregion ");
                                sbre.append("where ");
                                sbre.append("edt.cancelled is false ");
                                sbre.append("and edt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') ");
                                sbre.append("and pp.month=" + mvo.getMonth() + " ");
                                sbre.append("and pp.year=" + mvo.getYear() + " ");
                                sbre.append("and pp.process is true ");
                                sbre.append("and pp.accregion='" + regionmaster.getId() + "' ");
                                sbre.append("and pp.employeeprovidentfundnumber='" + employeemaster.getEpfno() + "' ");
                                sbre.append("group by pp.employeeprovidentfundnumber, em.employeename, dm.designation, pp.section,pp.salarystructureid,dm.orderno,rm.regionname,pp.id ");
                                sbre.append("order by pp.accregion,pp.section,dm.orderno ");

                                sbsup.append("select spb.employeeprovidentfundnumber,em.employeename, dm.designation,");
                                sbsup.append("sss.id,dm.orderno,rm.regionname,sppd.id, spb.accregion, spb.section,dm.orderno,sppd.calculatedyear,sppd.calculatedmonth from supplementaryemployeedeductionstransactions sedt ");
                                sbsup.append("left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid ");
                                sbsup.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                                sbsup.append("left join supplementarysalarystructure sss on sss.supplementarypayrollprocessingdetailsid=sppd.id ");
                                sbsup.append("left join employeemaster em on spb.employeeprovidentfundnumber = em.epfno ");
                                sbsup.append("left join designationmaster dm on dm.designationcode= em.designation ");
                                sbsup.append("left join regionmaster rm on rm.id=spb.accregion ");
                                sbsup.append("where spb.type='SUPLEMENTARYBILL' ");
                                sbsup.append("and spb.cancelled is false ");
                                sbsup.append("and sppd.cancelled is false ");
                                sbsup.append("and sppd.calculatedmonth = " + mvo.getMonth() + " ");
                                sbsup.append("and sppd.calculatedyear = " + mvo.getYear() + " ");
                                sbsup.append("and sedt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') ");
                                sbsup.append("and sedt.cancelled is false ");
                                sbsup.append("and spb.accregion='" + regionmaster.getId() + "' ");
                                sbsup.append("and spb.employeeprovidentfundnumber='" + employeemaster.getEpfno() + "' ");
                                sbsup.append("group by spb.employeeprovidentfundnumber, em.employeename, dm.designation, sss.id, dm.orderno, rm.regionname, sppd.id, spb.accregion,spb.section,dm.orderno,sppd.calculatedyear,sppd.calculatedmonth ");
                                sbsup.append("order by spb.accregion,sppd.calculatedyear,sppd.calculatedmonth,spb.section,dm.orderno");

                                SQLQuery employeequeryregular = session.createSQLQuery(sbre.toString());

                                SQLQuery employeequerysupplementary = session.createSQLQuery(sbsup.toString());

                                if (employeequeryregular.list().size() > 0) {
                                    // <editor-fold defaultstate="collapsed" desc="PayrollProcessingdetails start">
//                                    System.out.println(employeemaster.getEpfno() + " | " + regionmaster.getRegionname() + " | " + mvo.getMonth() + "/" + mvo.getYear() + " Regular");
                                    Map regularmap = getRegularDeduction(session, request, response, LoggedInRegion, LoggedInUser, mvo.getMonth(), mvo.getYear(), employeemaster.getEpfno(), regionmaster.getId(), grouptype, slipno);
                                    buffer.append((String) regularmap.get("regularlist"));
                                    regiontotal += (Double) regularmap.get("regiontotal");
                                    slipno++;
                                    // </editor-fold>
                                } else if (employeequerysupplementary.list().size() > 0) {
                                    // <editor-fold defaultstate="collapsed" desc="SuppplementaryProcessingdetails start">
//                                    System.out.println(employeemaster.getEpfno() + " | " + regionmaster.getRegionname() + " | " + mvo.getMonth() + "/" + mvo.getYear() + " Supplementary");
                                    Map supplementarymap = getSupplementaryDeduction(session, request, response, LoggedInRegion, LoggedInUser, mvo.getMonth(), mvo.getYear(), employeemaster.getEpfno(), regionmaster.getId(), grouptype, slipno);
                                    buffer.append((String) supplementarymap.get("supplementarylist"));
                                    regiontotal += (Double) supplementarymap.get("regiontotal");
                                    slipno++;
                                    // </editor-fold>
                                } else {
                                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                                    // </editor-fold>
                                }
                                // </editor-fold>
                            }
                        }
                        if (regiontotal > 0) {
                            buffer.append("<tr class=\"rowColor3\">");
                            buffer.append("<td width=\"56%\" align=\"center\" colspan=\"6\">Total</td>");
                            buffer.append("<td width=\"7%\" align=\"center\">" + decimalFormat.format(regiontotal) + "</td>");
                            buffer.append("<td width=\"7%\" align=\"center\">" + PAYMONTH + "</td>");
                            buffer.append("<td width=\"15%\" align=\"center\">" + regionmaster.getRegionname() + "</td>");
                            buffer.append("<td width=\"15%\" align=\"center\">" + "" + "</td>");
                            buffer.append("</tr>");
                        }
                        grandtotal += regiontotal;
                        // </editor-fold>
                    }
                }
                // </editor-fold>
            }

            buffer.append("</table>");
            buffer.append("</div>");
            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("<tr>");
            buffer.append("<td>");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr class=\"gridmenu\">");
            buffer.append("<td width=\"56%\" colspan=\"6\" align=\"center\">Grand Total</td>");
            buffer.append("<td width=\"7%\" align=\"center\">" + decimalFormat.format(grandtotal) + "</td>");
            buffer.append("<td width=\"37%\" colspan=\"3\" align=\"center\"></td>");
//            buffer.append("<td width=\"34%\" colspan=\"10\" align=\"center\">");
//            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            map.put("schedulegrid", buffer.toString());
        } catch (Exception ex) {
            map.put("ERROR", "Storage Loss Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getScheduleGrid(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String grouptype, String region, String reporttype, String filePathwithName) {
        Map map = new HashMap();
        RegionwiseReport regionwiseReport = new RegionwiseReport();
        try {
            int slipno = 1;
            Criteria regionCrit = session.createCriteria(Regionmaster.class);
            if (!region.equals("0")) {
                regionCrit.add(Restrictions.sqlRestriction("id = '" + region + "' "));
            }
            regionCrit.addOrder(Order.asc("id"));
            String regionname = "";
            List regionlist = regionCrit.list();
            if (regionlist.size() > 0) {
                for (int j = 0; j < regionlist.size(); j++) {
                    Regionmaster regionmaster = (Regionmaster) regionlist.get(j);
                    region = regionmaster.getId();
                    regionname = regionmaster.getRegionname();
                    
                    int s_month = Integer.valueOf(smonth) + 1;
                    int a_month = Integer.valueOf(emonth) + 1;
                    String startDate = syear + "-" + s_month + "-01";
                    String endDate = eyear + "-" + a_month + "-01";
                    StringBuilder builder1 = new StringBuilder();
                    if ("1".equalsIgnoreCase(reporttype)) {
                        builder1.append(" SELECT t.* FROM crosstab(    ");
                        builder1.append(" $$SELECT employeeprovidentfundnumber, employeename, mont, amount FROM  ( ");
                        builder1.append(" select pp.employeeprovidentfundnumber, (em.employeename||'@@'||dm.designation) as employeename,to_char(to_timestamp(pp.year||'-'||pp.month,'YYYY-MM'),'mon') As mont, sum(edt.amount) as amount  from employeedeductionstransactions edt ");
                        builder1.append(" left join payrollprocessingdetails pp on pp.id=edt.payrollprocessingdetailsid ");
                        builder1.append(" and to_timestamp(pp.year||'-'||pp.month,'YYYY-MM') between '" + startDate + "' and '" + endDate + "' and pp.process is true ");
                        builder1.append(" left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno ");
                        builder1.append(" left join designationmaster dm on dm.designationcode= em.designation ");
                        builder1.append(" left join regionmaster rm on rm.id=pp.accregion ");
                        builder1.append(" left join paycodemaster pm on pm.paycode=edt.deductionmasterid  ");
                        builder1.append(" where edt.cancelled is false ");
                        if (!"0".equalsIgnoreCase(grouptype)) {
                            builder1.append(" and edt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') ");
                        }
                        //builder1.append("and to_timestamp(pp.year||'-'||pp.month,'YYYY-MM') between '" + startDate + "' and '" + endDate + "' and pp.process is true ");
                        //builder1.append(" and pp.year=2015 and pp.process is true and pp.accregion='R01' and pp.employeeprovidentfundnumber in ('049303','000437')	");
                        builder1.append(" and pp.accregion='" + region + "' ");
                        if (epfno.length() > 0) {
                            builder1.append(" and pp.employeeprovidentfundnumber in ( select epfno from employeemaster where region = '" + region + "' and epfno in ('" + epfno + "') ) ");
                        } else {
                            //builder1.append(" and pp.employeeprovidentfundnumber in ( select epfno from employeemaster where region = '" + region + "' ) ");
                        }
                        builder1.append(" group by pp.employeeprovidentfundnumber, em.employeename , dm.designation,pp.month,pp.year");
                        builder1.append(" order by pp.employeeprovidentfundnumber, dm.designation ) sub1 $$  ");
                        builder1.append(" ,'SELECT to_char(date ''1980-04-01'' + (n || ''month'')::interval, ''mon'') As short_mname  FROM generate_series(0,11) n' ");
                        builder1.append("  ) AS t ");
                        builder1.append(" (epfno text, employeename text,  ");
                        builder1.append(" apr text, may text,june text,july text,aug text,sep text,oct text,nov text,dec text,jan text,feb text,mar text ) ");
                    } else if ("2".equalsIgnoreCase(reporttype)) {
                        builder1.append(" SELECT t.* FROM crosstab(     ");
                        builder1.append(" $$SELECT employeeprovidentfundnumber, employeename, mont, amount FROM  ( ");
                        builder1.append(" select spb.employeeprovidentfundnumber, (em.employeename||'@@'||dm.designation) as employeename, ");
                        builder1.append(" to_char(to_timestamp(sppd.calculatedyear||'-'||sppd.calculatedmonth,'YYYY-MM'),'mon') As mont, sum(sedt.amount) as amount  ");
                        builder1.append(" from supplementaryemployeedeductionstransactions sedt  ");
                        builder1.append(" left join supplementarypayrollprocessingdetails sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid  ");
                        builder1.append(" and to_timestamp(sppd.calculatedyear||'-'||sppd.calculatedmonth,'YYYY-MM') between '" + startDate + "' and '" + endDate + "' and sppd.cancelled is false ");
                        builder1.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                        builder1.append(" left join supplementarysalarystructure sss on sss.supplementarypayrollprocessingdetailsid=sppd.id  ");
                        builder1.append(" left join employeemaster em on spb.employeeprovidentfundnumber = em.epfno  ");
                        builder1.append(" left join designationmaster dm on dm.designationcode= em.designation  ");
                        builder1.append(" left join regionmaster rm on rm.id=spb.accregion  ");
                        builder1.append(" left join paycodemaster pm on pm.paycode=sedt.deductionmasterid ");
                        builder1.append(" left join supplementaryemployeeloansandadvancesdetails selad on  selad.supplementarypayrollprocessingdetailsid=sppd.id ");
                        builder1.append(" left join employeeloansandadvances ela on  selad.employeeloansandadvancesid=ela.id  ");
                        builder1.append(" and ela.deductioncode=pm.paycode and selad.cancelled is false ");
                        builder1.append(" left join supplementaryemployeedeductiondetails sedd on sedd.deductionmasterid=pm.paycode and sedd.cancelled is false ");
                        builder1.append(" left join salarystructure ss on ss.id=sedd.supplementarysalarystructureid and  ss.id=sss.id ");
                        builder1.append(" where spb.type='SUPLEMENTARYBILL' and spb.cancelled is false ");
                        if (!"0".equalsIgnoreCase(grouptype)) {
                            builder1.append(" and sedt.deductionmasterid in (select paycode from paycodemaster where grouphead = '" + grouptype + "') ");
                        }
                        builder1.append(" and sedt.cancelled is false and spb.accregion= '" + region + "' ");
                        if (epfno.length() > 0) {
                            builder1.append(" and spb.employeeprovidentfundnumber='" + epfno + "' ");
                        }
                        builder1.append(" group by spb.employeeprovidentfundnumber,em.employeename,dm.designation,sppd.calculatedyear,sppd.calculatedmonth ");
                        builder1.append(" order by spb.employeeprovidentfundnumber, dm.designation ) sub1 $$  ");
                        builder1.append(" ,'SELECT to_char(date ''1980-04-01'' + (n || ''month'')::interval, ''mon'') As short_mname  FROM generate_series(0,11) n' ) AS t  ");
                        builder1.append(" (epfno text, employeename text, ");
                        builder1.append(" apr text, may text,june text,july text,aug text,sep text,oct text,nov text,dec text,jan text,feb text,mar text )  ");
                    }
                    SQLQuery query = session.createSQLQuery(builder1.toString());
                    List<RecoveryDetail> contentlist = new ArrayList<RecoveryDetail>();
                    RecoveryDetail recoveryDetail = null;
                    
                    double rowaprtotal = 0;
                    double rowmaytotal = 0;
                    double rowjuntotal = 0;
                    double rowjultotal = 0;
                    double rowaugtotal = 0;
                    double rowseptotal = 0;
                    double rowocttotal = 0;
                    double rownovtotal = 0;
                    double rowdectotal = 0;
                    double rowjantotal = 0;
                    double rowfebtotal = 0;
                    double rowmartotal = 0;                    
                    double coltotal = 0;
                    double colgrtotal = 0;
                    for (ListIterator it1 = query.list().listIterator(); it1.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Recovery Query List">
                        Object[] rows = (Object[]) it1.next();
                        recoveryDetail = new RecoveryDetail();
                        recoveryDetail.setSno(slipno + "");
                        recoveryDetail.setEpfno((String) rows[0]);
                        String enm = (String) rows[1];
                        String ename = "";
                        String design = "";
                        if(!"".equalsIgnoreCase(enm)){
                            ename = enm.split("@@")[0];
                            design = enm.split("@@")[1];
                            recoveryDetail.setEmployeename(ename);
                            recoveryDetail.setDesignation(design);
                        }else{
                            recoveryDetail.setEmployeename("");
                            recoveryDetail.setEmployeename("");
                        }
                        double aprt = doubleValue((String) rows[2]);
                        double mayt = doubleValue((String) rows[3]);
                        double junt = doubleValue((String) rows[4]);
                        double jult = doubleValue((String) rows[5]);
                        double augt = doubleValue((String) rows[6]);
                        double sept = doubleValue((String) rows[7]);
                        double octt = doubleValue((String) rows[8]);
                        double novt = doubleValue((String) rows[9]);
                        double dect = doubleValue((String) rows[10]);
                        double jant = doubleValue((String) rows[11]);
                        double febt = doubleValue((String) rows[12]);
                        double mart = doubleValue((String) rows[13]);
                        rowaprtotal += aprt;
                        rowmaytotal += mayt;
                        rowjuntotal += junt;
                        rowjultotal += jult;
                        rowaugtotal += augt;
                        rowseptotal += sept;
                        rowocttotal += octt;
                        rownovtotal += novt;
                        rowdectotal += dect;
                        rowjantotal += jant;
                        rowfebtotal += febt;
                        rowmartotal += mart;
                        
                        coltotal = 0;
                        coltotal = aprt + mayt + junt + jult + augt + sept + octt + novt + dect + jant + febt + mart;
                        colgrtotal += coltotal;
                        
                        recoveryDetail.setApr(aprt+"");
                        recoveryDetail.setMay(mayt+"");
                        recoveryDetail.setJune(junt+"");
                        recoveryDetail.setJuly(jult+"");
                        recoveryDetail.setAug(augt+"");
                        recoveryDetail.setSep(sept+"");
                        recoveryDetail.setOct(octt+"");
                        recoveryDetail.setNov(novt+"");
                        recoveryDetail.setDec(dect+"");
                        recoveryDetail.setJan(jant+"");
                        recoveryDetail.setFeb(febt+"");
                        recoveryDetail.setMar(mart+"");
                        recoveryDetail.setColTotal(coltotal+"");

                        contentlist.add(recoveryDetail);
                        slipno++;
                        //</editor-fold>
                    }
                    //map.put("headerlist", headerlist);                    
                    map.put("rowaprtotal", rowaprtotal+"");
                    map.put("rowmaytotal", rowmaytotal+"");
                    map.put("rowjuntotal", rowjuntotal+"");
                    map.put("rowjultotal", rowjultotal+"");
                    map.put("rowaugtotal", rowaugtotal+"");
                    map.put("rowseptotal", rowseptotal+"");
                    map.put("rowocttotal", rowocttotal+"");
                    map.put("rownovtotal", rownovtotal+"");
                    map.put("rowdectotal", rowdectotal+"");
                    map.put("rowjantotal", rowjantotal+"");
                    map.put("rowfebtotal", rowfebtotal+"");
                    map.put("rowmartotal", rowmartotal+"");                    
                    map.put("colgrtotal", colgrtotal+"");
                    map.put("contentlist", contentlist);
                    map.put("regionname", regionname);
                    map.put("grouptype", grouptype);
                    map.put("reporttype", reporttype);
                    regionwiseReport.GenerateScheduleGrid(map, filePathwithName);
                }
            }
        } catch (Exception ex) {
            map.put("ERROR", "EDLI Employee Report Error");
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

    @GlobalDBOpenCloseAndUserPrivilages
    public String getRegionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("********************** RegionwiseReportServiceImpl class  getRegionList method is calling **********************");
        StringBuilder sb = new StringBuilder();
        try {
            Criteria regionCrit = session.createCriteria(Regionmaster.class);
            regionCrit.addOrder(Order.asc("regionname"));
            List regionlist = regionCrit.list();
            sb.append("<select class=\"combobox\" name=\"region\" id=\"region\">");
            if (regionlist.size() > 0) {
                //sb.append("<option value=\"0\">" + "ALL" + "</option>");
                for (int j = 0; j < regionlist.size(); j++) {
                    Regionmaster regionmaster = (Regionmaster) regionlist.get(j);
                    sb.append("<option value=\"" + regionmaster.getId() + "\">" + regionmaster.getRegionname() + "</option>");
                }
            }
            sb.append("</select>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getEarningList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("********************** RegionwiseReportServiceImpl class  getEarningList method is calling **********************");
        StringBuilder sb = new StringBuilder();
        try {
            Criteria paycodeCrit = session.createCriteria(Paycodemaster.class);
            paycodeCrit.add(Restrictions.sqlRestriction("paycodetype='E'"));
            paycodeCrit.addOrder(Order.asc("paycode"));

            List paycodelist = paycodeCrit.list();
            sb.append("<select class=\"combobox\" name=\"earningid\" id=\"earningid\">");
            if (paycodelist.size() > 0) {
                for (int j = 0; j < paycodelist.size(); j++) {
                    Paycodemaster paycodemaster = (Paycodemaster) paycodelist.get(j);
                    sb.append("<option value=\"" + paycodemaster.getPaycode() + "\">" + paycodemaster.getPaycodename() + "</option>");
                }
            }
            sb.append("</select>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEarningDetailsGrid(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String earningid, String region) {
        System.out.println("********************* RegionwiseReportServiceImpl class getEarningDetailsGrid method is calling *****************");
        Map map = new HashMap();
        try {
            int s_year = Integer.valueOf(syear);
            int s_month = Integer.valueOf(smonth) + 1;
            int e_year = Integer.valueOf(eyear);
            int e_month = Integer.valueOf(emonth) + 1;
            String Earningsid = earningid;
            String EPF = epfno;
            String groupname = null;
            int slipno = 1;

            double grandtotal = 0;

            StringBuilder buffer = new StringBuilder();

            buffer.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr>");
            buffer.append("<td valign=\"top\">");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr class=\"gridmenu\">");
            buffer.append("<td width=\"3%\" align=\"center\">Sno</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Epfno</td>");
            buffer.append("<td width=\"17%\" align=\"center\">Name</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Designation</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Group Name</td>");
            buffer.append("<td width=\"7%\" align=\"center\">File No</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Amount</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Month&Year</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Region</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Type</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</td>");
            buffer.append("<tr>");
            buffer.append("<td valign=\"top\">");
            buffer.append("<div id=\"scrolling\">");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");

            Iterator itr = CommonUtility.getMonthandYearBetween(s_month, s_year, e_month, e_year).iterator();
            while (itr.hasNext()) {
                // <editor-fold defaultstate="collapsed" desc="Year,Month Loop">
                MonthValueObject mvo = (MonthValueObject) itr.next();
//                System.out.println(mvo.getMonth() + "/" + mvo.getYear());
                StringBuilder buf = new StringBuilder();

                buf.append("select spb.employeeprovidentfundnumber as epfno, em.employeename as employeename, dm.designation as designation, ");
                buf.append("rm.regionname as regionname, sppd.calculatedmonth as month, sppd.calculatedyear as year, ");
                buf.append("spb.accregion as region, sppd.id as processid, 'Supplementary' as type from supplementarypayrollprocessingdetails  sppd ");
                buf.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                buf.append("left join employeemaster em on spb.employeeprovidentfundnumber = em.epfno ");
                buf.append("left join designationmaster dm on dm.designationcode= spb.designation ");
                buf.append("left join regionmaster rm on rm.id=spb.accregion ");
                buf.append("where ");
                buf.append("sppd.calculatedyear=" + mvo.getYear() + " ");
                buf.append("and sppd.calculatedmonth=" + mvo.getMonth() + " ");
                buf.append("and spb.type='SUPLEMENTARYBILL' ");
                buf.append("and sppd.cancelled is false ");
                buf.append("and spb.cancelled is false ");
                if (!region.equals("0")) {
                    buf.append("and spb.accregion='" + region + "' ");
                }
                if (epfno.length() > 0) {
                    buf.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
                }

                buf.append("union ");

                buf.append("select pp.employeeprovidentfundnumber as epfno, em.employeename as employeename, dm.designation as designation, ");
                buf.append("rm.regionname as regionname, ");
                buf.append("pp.month as month, pp.year as year, pp.accregion as region, ");
                buf.append("pp.id as processid, 'Regular' as type from payrollprocessingdetails pp ");
                buf.append("left join employeemaster em on pp.employeeprovidentfundnumber = em.epfno ");
                buf.append("left join designationmaster dm on dm.designationcode= pp.designation  ");
                buf.append("left join regionmaster rm on rm.id=pp.accregion ");
                buf.append("where ");
                buf.append("pp.year=" + mvo.getYear() + " ");
                buf.append("and pp.month=" + mvo.getMonth() + " ");
                buf.append("and pp.process is true ");
                if (!region.equals("0")) {
                    buf.append("and pp.accregion='" + region + "' ");
                }
                if (epfno.length() > 0) {
                    buf.append("and pp.employeeprovidentfundnumber='" + epfno + "' ");
                }
                buf.append("order by region,epfno ");
//                System.out.println("buf.toString() = " + buf.toString());

                SQLQuery employeequery = session.createSQLQuery(buf.toString());


                for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    Object[] rows = (Object[]) its.next();
                    String PFNUMBER = "";
                    String ENAME = "";
                    String DESIG = "";
                    String REG = "";
                    String PAYMONTH = "";
                    String GROUPNAME = "";
                    String PROCESSID = "";
                    String TYPE = "";
                    String AMOUNT = "";

                    PFNUMBER = (String) rows[0];
                    ENAME = (String) rows[1];
                    DESIG = (String) rows[2];
                    REG = (String) rows[3];
                    PAYMONTH = months[(Integer.valueOf(rows[4].toString()) - 1)] + "/" + rows[5].toString();
                    PROCESSID = (String) rows[7];
                    TYPE = (String) rows[8];


                    String amountquery = null;
                    if (TYPE.equals("Supplementary")) {
                        amountquery = "select seet.earningmasterid, seet.amount, pm.paycodename from supplementaryemployeeearningstransactions seet "
                                + "left join paycodemaster pm on pm.paycode=seet.earningmasterid "
                                + "where supplementarypayrollprocessingdetailsid ='" + PROCESSID + "' "
                                + "and earningmasterid='" + earningid + "' "
                                + "and cancelled is false";
                        SQLQuery amount_query = session.createSQLQuery(amountquery);

                        List amolist = amount_query.list();
                        if (amolist.size() > 0) {
                            Object obj[] = (Object[]) amolist.get(0);
                            BigDecimal bd = (BigDecimal) obj[1];
                            double amo = bd.doubleValue();
                            AMOUNT = decimalFormat.format(amo);
                            GROUPNAME = (String) obj[2];
                        }


                    } else if (TYPE.equals("Regular")) {
                        amountquery = "select eet.earningmasterid, eet.amount, pm.paycodename from employeeearningstransactions eet "
                                + "left join paycodemaster pm on pm.paycode= eet.earningmasterid "
                                + "where eet.payrollprocessingdetailsid='" + PROCESSID + "' "
                                + "and eet.cancelled is false "
                                + "and eet.earningmasterid='" + earningid + "'";
                        SQLQuery amount_query = session.createSQLQuery(amountquery);

                        List amolist = amount_query.list();
                        if (amolist.size() > 0) {
                            Object obj[] = (Object[]) amolist.get(0);
                            BigDecimal bd = (BigDecimal) obj[1];
                            double amo = bd.doubleValue();
                            AMOUNT = decimalFormat.format(amo);
                            GROUPNAME = (String) obj[2];
                        }
                    } else {
                    }

                    String className = "";
                    className = (slipno % 2 == 0) ? "rowColor1" : "rowColor2";
                    buffer.append("<tr class=\"" + className + "\">");
                    buffer.append("<td width=\"3%\" align=\"center\">" + slipno + "</td>");
                    buffer.append("<td width=\"7%\" align=\"center\">" + PFNUMBER + "</td>");
                    buffer.append("<td width=\"17%\" align=\"left\">" + ENAME + "</td>");
                    buffer.append("<td width=\"15%\" align=\"center\">" + DESIG + "</td>");
                    buffer.append("<td width=\"7%\" align=\"center\">" + GROUPNAME + "</td>");
                    buffer.append("<td width=\"7%\" align=\"center\"></td>");
                    buffer.append("<td width=\"7%\" align=\"center\">" + AMOUNT + "</td>");
                    buffer.append("<td width=\"7%\" align=\"center\">" + PAYMONTH + "</td>");
                    buffer.append("<td width=\"15%\" align=\"center\">" + REG + "</td>");
                    buffer.append("<td width=\"15%\" align=\"center\">" + TYPE + "</td>");
                    buffer.append("</tr>");
                    slipno++;
                    // </editor-fold>
                }
                // </editor-fold>
            }
            buffer.append("</table>");
            buffer.append("</div>");
            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("<tr>");
            buffer.append("<td>");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr class=\"gridmenu\">");
            buffer.append("<td width=\"56%\" colspan=\"6\" align=\"center\">Grand Total</td>");
            buffer.append("<td width=\"7%\" align=\"center\">" + decimalFormat.format(grandtotal) + "</td>");
            buffer.append("<td width=\"37%\" colspan=\"3\" align=\"center\"></td>");
//            buffer.append("<td width=\"34%\" colspan=\"10\" align=\"center\">");
//            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            map.put("schedulegrid", buffer.toString());
        } catch (Exception ex) {
            map.put("ERROR", "Storage Loss Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getServiceRegisterDetailsGrid(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String region) {
        System.out.println("********************* RegionwiseReportServiceImpl class getServiceRegisterDetailsGrid method is calling *****************");
        Map map = new HashMap();
        try {
            int s_year = Integer.valueOf(syear);
            int s_month = Integer.valueOf(smonth) + 1;
            int e_year = Integer.valueOf(eyear);
            int e_month = Integer.valueOf(emonth) + 1;
            String EPF = epfno;
            int slipno = 1;
            StringBuilder buffer = new StringBuilder();

            // <editor-fold defaultstate="collapsed" desc="Grid Header">

            buffer.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr>");
            buffer.append("<td valign=\"top\">");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr class=\"gridmenu\">");
            buffer.append("<td width=\"3%\" align=\"center\">Sno</td>");
            buffer.append("<td width=\"7%\" align=\"center\">Epfno</td>");
            buffer.append("<td width=\"17%\" align=\"center\">Name</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Designation</td>");
            buffer.append("<td width=\"15%\" align=\"center\">Region</td>");


            Iterator itr = CommonUtility.getMonthandYearBetween(s_month, s_year, e_month, e_year).iterator();
            while (itr.hasNext()) {
                // <editor-fold defaultstate="collapsed" desc="Year,Month Loop">
                MonthValueObject mvo = (MonthValueObject) itr.next();
//                System.out.println(mvo.getMonth() + "/" + mvo.getYear());
                buffer.append("<td width=\"10%\" align=\"center\">");
                buffer.append(months[Integer.valueOf(mvo.getMonth() - 1)] + "/" + mvo.getYear());
                buffer.append("</td>");
                // </editor-fold>
            }

//            buffer.append("<td width=\"5%\" align=\"center\">");
//            buffer.append("Total");
//            buffer.append("</td>");

            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</td>");
            buffer.append("<tr>");
            buffer.append("<td valign=\"top\">");
            buffer.append("<div id=\"scrolling\">");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");

            // </editor-fold>

            StringBuilder builder = new StringBuilder();

            builder.append("select em.epfno,em.employeename,rm.regionname,dm.designation from employeemaster em ");
            builder.append("left join regionmaster rm on rm.id=em.region ");
            builder.append("left join designationmaster dm on dm.designationcode=em.designation ");
            builder.append("where em.region='" + region + "' ");
            builder.append("and em.epfno not in (select epfno from stoppayrolldetails where accregion='" + region + "' and reasoncode in ('RETIRED','RESIGNED','DEATH')) ");
            if (epfno.length() > 0) {
                builder.append("and em.epfno='" + epfno + "' ");
            }
            builder.append("order by em.section,dm.orderno");

//            System.out.println("builder.toString() = " + builder.toString());

            SQLQuery employeequery = session.createSQLQuery(builder.toString());

            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Employee Query">
                Object[] rows = (Object[]) its.next();
                String PFNUMBER = (String) rows[0];
                String ENAME = (String) rows[1];
                String REG = (String) rows[2];
                String DESIG = (String) rows[3];

                String className = "";
                className = (slipno % 2 == 0) ? "rowColor1" : "rowColor2";
                buffer.append("<tr class=\"" + className + "\">");
                buffer.append("<td width=\"3%\" align=\"center\">" + slipno + "</td>");
                buffer.append("<td width=\"7%\" align=\"center\">" + PFNUMBER + "</td>");
                buffer.append("<td width=\"17%\" align=\"left\">" + ENAME + "</td>");
                buffer.append("<td width=\"15%\" align=\"center\">" + DESIG + "</td>");
                buffer.append("<td width=\"15%\" align=\"center\">" + REG + "</td>");

                itr = CommonUtility.getMonthandYearBetween(s_month, s_year, e_month, e_year).iterator();
                while (itr.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="Year,Month Loop">
                    MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(mvo.getMonth() + "/" + mvo.getYear());

                    StringBuilder ea = new StringBuilder();
                    ea.append("select seet.earningmasterid as earnings, seet.amount as amount, 'Supplementary' as type ");
                    ea.append("from supplementaryemployeeearningstransactions seet ");
                    ea.append("left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid ");
                    ea.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    ea.append("where seet.earningmasterid in ('E01','E25') ");
                    ea.append("and sppd.cancelled is false ");
                    ea.append("and sppd.calculatedmonth=" + mvo.getMonth() + " ");
                    ea.append("and sppd.calculatedyear=" + mvo.getYear() + " ");
                    ea.append("and spb.type='SUPLEMENTARYBILL' ");
                    ea.append("and spb.cancelled is false ");
                    ea.append("and spb.employeeprovidentfundnumber='" + PFNUMBER + "' ");
                    ea.append("and spb.accregion='" + region + "' ");
                    ea.append("union ");
                    ea.append("select eet.earningmasterid as earnings, eet.amount as amount, 'Regular' as type ");
                    ea.append("from employeeearningstransactions eet ");
                    ea.append("left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid ");
                    ea.append("where eet.earningmasterid in ('E01','E25') ");
                    ea.append("and eet.cancelled is false ");
                    ea.append("and ppd.process is true ");
                    ea.append("and ppd.employeeprovidentfundnumber='" + PFNUMBER + "' ");
                    ea.append("and ppd.month=" + mvo.getMonth() + " ");
                    ea.append("and ppd.year=" + mvo.getYear() + " ");
                    ea.append("and ppd.accregion='" + region + "'");

//                    System.out.println("ea.toString() = " + ea.toString());

                    SQLQuery earningquery = session.createSQLQuery(ea.toString());

                    String BASIC = "";
                    String GRPAY = "";
                    String TYPE = "";
                    for (ListIterator it1 = earningquery.list().listIterator(); it1.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Employee Query">
                        Object[] row1 = (Object[]) it1.next();
                        String earn = (String) row1[0];
                        if (earn.equals("E01")) {
                            BigDecimal bamo = (BigDecimal) row1[1];
                            BASIC = decimalFormat.format(bamo.doubleValue());
                        }
                        if (earn.equals("E25")) {
                            BigDecimal bamo = (BigDecimal) row1[1];
                            GRPAY = decimalFormat.format(bamo.doubleValue());
                        }
                        TYPE = (String) row1[2];
                    }

                    buffer.append("<td width=\"10%\" align=\"center\" ");
                    if (TYPE.equals("Supplementary")) {
                        buffer.append("class=\"SupColor\"");
                    }
                    buffer.append(">");
                    buffer.append(BASIC + " - " + GRPAY);
                    buffer.append("</td>");
                    // </editor-fold>
                }


                buffer.append("</tr>");

                slipno++;
                // </editor-fold>
            }

            // <editor-fold defaultstate="collapsed" desc="Grid Footer">

            int noofcolumns = (CommonUtility.getMonthandYearBetween(s_month, s_year, e_month, e_year)).size();

            buffer.append("</table>");
            buffer.append("</div>");
            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("<tr>");
            buffer.append("<td>");
            buffer.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            buffer.append("<tr class=\"gridmenu\">");
            buffer.append("<td width=\"100%\" colspan=\"" + (5 + noofcolumns) + "\" align=\"center\"></td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            // </editor-fold>

            map.put("serviceGrid", buffer.toString());


        } catch (Exception ex) {
            map.put("ERROR", "Storage Loss Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public String getRegionList1(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("********************** RegionwiseReportServiceImpl class  getRegionList1 method is calling **********************");
        StringBuilder sb = new StringBuilder();
        try {
            Criteria regionCrit = session.createCriteria(Regionmaster.class);
            regionCrit.addOrder(Order.asc("id"));

            List regionlist = regionCrit.list();
            sb.append("<select class=\"combobox\" name=\"region\" id=\"region\">");
            if (regionlist.size() > 0) {
                for (int j = 0; j < regionlist.size(); j++) {
                    Regionmaster regionmaster = (Regionmaster) regionlist.get(j);
                    sb.append("<option value=\"" + regionmaster.getId() + "\">" + regionmaster.getRegionname() + "</option>");
                }
            }
            sb.append("</select>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getServiceRegisterDetailsReport(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String syear, String smonth, String eyear, String emonth, String epfno, String region, String filePathwithName) {
        System.out.println("********************* RegionwiseReportServiceImpl class getServiceRegisterDetailsReport method is calling *****************");
        Map map = new HashMap();
        ServiceRegisterDetailsReport srdr = new ServiceRegisterDetailsReport();
        try {
            int s_year = Integer.valueOf(syear);
            int s_month = Integer.valueOf(smonth) + 1;
            int e_year = Integer.valueOf(eyear);
            int e_month = Integer.valueOf(emonth) + 1;
            String EPF = epfno;
            int slipno = 1;

            Criteria reCrit = session.createCriteria(Regionmaster.class);
            reCrit.add(Restrictions.sqlRestriction("id='" + LoggedInRegion + "'"));
            List<Regionmaster> reList = reCrit.list();
            String regionname = "";
            if (reList.size() > 0) {
                Regionmaster regionmaster = reList.get(0);
                regionname = regionmaster.getRegionname();
            }

            List<String> headerlist = new ArrayList<String>();

            Iterator itr = CommonUtility.getMonthandYearBetween(s_month, s_year, e_month, e_year).iterator();
            while (itr.hasNext()) {
                // <editor-fold defaultstate="collapsed" desc="Year,Month Loop">
                MonthValueObject mvo = (MonthValueObject) itr.next();
//                System.out.println(mvo.getMonth() + "/" + mvo.getYear());
                headerlist.add(months[Integer.valueOf(mvo.getMonth() - 1)] + "/" + mvo.getYear());
                // </editor-fold>
            }

            StringBuilder builder = new StringBuilder();

            builder.append("select em.epfno,em.employeename,rm.regionname,dm.designation from employeemaster em ");
            builder.append("left join regionmaster rm on rm.id=em.region ");
            builder.append("left join designationmaster dm on dm.designationcode=em.designation ");
            builder.append("where em.region='" + region + "' ");
            builder.append("and em.epfno not in (select epfno from stoppayrolldetails where accregion='" + region + "' and reasoncode in ('RETIRED','RESIGNED','DEATH')) ");
            if (epfno.length() > 0) {
                builder.append("and em.epfno='" + epfno + "' ");
            }
            builder.append("order by em.section,dm.orderno");

            SQLQuery employeequery = session.createSQLQuery(builder.toString());

            if (employeequery.list().size() == 0) {
                // <editor-fold defaultstate="collapsed" desc="Year,Month Loop">
                map.put("ERROR", "There is no Record for the given Inputs");
                return map;
                // </editor-fold>
            }

            List<RegionwiseModel> contentlist = new ArrayList<RegionwiseModel>();

            RegionwiseModel rm = null;

            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold defaultstate="collapsed" desc="Employee Query">
                Object[] rows = (Object[]) its.next();
                rm = new RegionwiseModel();
                String PFNUMBER = (String) rows[0];
                String ENAME = (String) rows[1];
                String REG = (String) rows[2];
                String DESIG = (String) rows[3];

                rm.setSlipno(slipno);
                rm.setPfno(PFNUMBER);
                rm.setEmployeename(ENAME);
                rm.setDesignation(DESIG);
                rm.setRegionname(REG);

                itr = CommonUtility.getMonthandYearBetween(s_month, s_year, e_month, e_year).iterator();
                List<String> amountlist = new ArrayList<String>();
                while (itr.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="Year,Month Loop">
                    MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(mvo.getMonth() + "/" + mvo.getYear());

                    StringBuilder ea = new StringBuilder();
                    ea.append("select seet.earningmasterid as earnings, seet.amount as amount, 'Supplementary' as type ");
                    ea.append("from supplementaryemployeeearningstransactions seet ");
                    ea.append("left join supplementarypayrollprocessingdetails sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid ");
                    ea.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    ea.append("where seet.earningmasterid in ('E01','E25') ");
                    ea.append("and sppd.cancelled is false ");
                    ea.append("and sppd.calculatedmonth=" + mvo.getMonth() + " ");
                    ea.append("and sppd.calculatedyear=" + mvo.getYear() + " ");
                    ea.append("and spb.type='SUPLEMENTARYBILL' ");
                    ea.append("and spb.cancelled is false ");                    
                    ea.append("and spb.employeeprovidentfundnumber='" + PFNUMBER + "' ");
                    ea.append("and spb.accregion='" + region + "' ");
                    ea.append("union ");
                    ea.append("select eet.earningmasterid as earnings, eet.amount as amount, 'Regular' as type ");
                    ea.append("from employeeearningstransactions eet ");
                    ea.append("left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid ");
                    ea.append("where eet.earningmasterid in ('E01','E25') ");
                    ea.append("and eet.cancelled is false ");
                    ea.append("and ppd.process is true ");
                    ea.append("and ppd.employeeprovidentfundnumber='" + PFNUMBER + "' ");
                    ea.append("and ppd.month=" + mvo.getMonth() + " ");
                    ea.append("and ppd.year=" + mvo.getYear() + " ");
                    ea.append("and ppd.accregion='" + region + "'");

                    SQLQuery earningquery = session.createSQLQuery(ea.toString());

                    String BASIC = "";
                    String GRPAY = "";
                    String TYPE = "";
                    for (ListIterator it1 = earningquery.list().listIterator(); it1.hasNext();) {
                        // <editor-fold defaultstate="collapsed" desc="Employee Query">
                        Object[] row1 = (Object[]) it1.next();
                        String earn = (String) row1[0];
                        if (earn.equals("E01")) {
                            BigDecimal bamo = (BigDecimal) row1[1];
                            BASIC = decimalFormat.format(bamo.doubleValue());
                        }
                        if (earn.equals("E25")) {
                            BigDecimal bamo = (BigDecimal) row1[1];
                            GRPAY = decimalFormat.format(bamo.doubleValue());
                        }
                        TYPE = (String) row1[2];
                        //</editor-fold>
                    }
                    amountlist.add(BASIC + " - " + GRPAY);
                    // </editor-fold>
                }
                rm.setAmountlist(amountlist);
                contentlist.add(rm);
                slipno++;
                //</editor-fold>
            }
            map.put("headerlist", headerlist);
            map.put("contentlist", contentlist);
            map.put("fromdate", months[s_month - 1] + syear.substring(2, 4));
            map.put("todate", months[e_month - 1] + eyear.substring(2, 4));
            map.put("regionname", regionname);
            srdr.GenerateServiceRegisterDetailsexcel(map, filePathwithName);

        } catch (Exception ex) {
            map.put("ERROR", "Storage Loss Report Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }
}
