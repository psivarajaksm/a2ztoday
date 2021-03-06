/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.common.CommonUtility;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import com.onward.reports.bonus.*;
import com.onward.valueobjects.PaySlipModel;
import java.util.ListIterator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.SQLQuery;
import com.onward.common.DateParser;
import com.onward.valueobjects.MonthValueObject;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

/**
 *
 * @author root
 */
public class BonusBillServiceImpl implements BonusBillService {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @GlobalDBOpenCloseAndUserPrivilages
    public Map EmployeeBonusPDPPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startmonth, String startyear, String endmonth, String endyear, String epfno1, String category, String section, String filePathwithName) {
        Map map = new HashMap();
        try {
            System.out.println("********************* BonusBillServiceImpl class EmployeeBonusPDPPrintOut method is calling *****************");
            BonusBDPReport bonusBDPReport = new BonusBDPReport();
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            int START_MONTH = Integer.valueOf(startmonth) + 1;
            int START_YEAR = Integer.valueOf(startyear);
            int END_MONTH_REGULAR = Integer.valueOf(endmonth) + 1;
            int END_YEAR_REGULAR = Integer.valueOf(endyear);
            int END_MONTH_SUPPLEMENTARY = 0;
            int END_YEAR_SUPPLEMENTARY = 0;

            if ((END_MONTH_REGULAR + 6) > 12) {
                END_MONTH_SUPPLEMENTARY = (END_MONTH_REGULAR + 6) - 12;
                END_YEAR_SUPPLEMENTARY = END_YEAR_REGULAR + 1;
            } else {
                END_MONTH_SUPPLEMENTARY = END_MONTH_REGULAR + 6;
                END_YEAR_SUPPLEMENTARY = END_YEAR_REGULAR;
            }
            String pfno = "";
            StringBuilder ssb = new StringBuilder();
            if (epfno1.trim().length() > 0) {
                ssb.append("SELECT epfno, region FROM employeemaster where epfno='" + epfno1 + "' ");
            } else {
                ssb.append("SELECT epfno, region FROM employeemaster where region='" + LoggedInRegion + "' and section='" + section + "'");
            }
            SQLQuery employeequeryssb = session.createSQLQuery(ssb.toString());
            for (ListIterator itsssb = employeequeryssb.list().listIterator(); itsssb.hasNext();) {
                Object[] rowssb = (Object[]) itsssb.next();
                pfno = (String) rowssb[0];
//            System.out.println("startmonth =" + START_MONTH);
//            System.out.println("startyear = " + START_YEAR);
//            System.out.println("endmonth = " + END_MONTH_REGULAR);
//            System.out.println("endyear =" + END_YEAR_REGULAR);
//            System.out.println("END_MONTH_SUPPLEMENTARY -> " + END_MONTH_SUPPLEMENTARY);
//            System.out.println("END_YEAR_SUPPLEMENTARY -> " + END_YEAR_SUPPLEMENTARY);

                BonusBDPReport bDPReport = new BonusBDPReport();
                PaySlipModel psm = null;

                StringBuilder sb = new StringBuilder();

                if (category.equalsIgnoreCase("R")) {

                    sb.append("SELECT epfno, employeename, rm.regionname, sn.sectionname, dm.designation, process FROM employeemaster em ");
                    sb.append("left join regionmaster rm on em.region=rm.id ");
                    sb.append("left join sectionmaster sn on sn.id=em.section ");
                    sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
                    sb.append("where ");
                    sb.append("em.region='" + LoggedInRegion + "' ");
                    sb.append("and cast(dm.orderno as integer) > 17 ");
                    if (pfno.length() > 0) {
                        sb.append("and epfno='" + pfno + "' ");
                    }
                    sb.append("order by rm.id, sn.id, cast(dm.orderno as integer)");
                } else {
                    sb.append("SELECT epfno, employeename, rm.regionname, sn.sectionname, dm.designation, process FROM employeemaster em ");
                    sb.append("left join regionmaster rm on rm.id='" + LoggedInRegion + "' ");
                    sb.append("left join sectionmaster sn on sn.id=em.section ");
                    sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
                    sb.append("where ");
                    sb.append("(em.region='TRANS' or  em.region='RET') ");
                    sb.append("and cast(dm.orderno as integer) > 17 ");
                    if (pfno.length() > 0) {
                        sb.append("and epfno='" + pfno + "' ");
                    }
                    sb.append("order by rm.id, sn.id, cast(dm.orderno as integer)");
                }

                System.out.println("query = " + sb.toString());

                SQLQuery employeequery = session.createSQLQuery(sb.toString());
                int slipno = 1;

                for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="ALL">
                    psm = new PaySlipModel();
                    Object[] rows = (Object[]) its.next();
                    String epfno = (String) rows[0];
                    String employeename = (String) rows[1];
                    String region = (String) rows[2];
                    String sectionname = (String) rows[3];
                    String designation = (String) rows[4];

                    psm.setSlipno(String.valueOf(slipno));
                    psm.setPfno(epfno);
                    psm.setEmployeename((String) rows[1]);
                    psm.setBranch((String) rows[2]);
                    psm.setSectionname((String) rows[3]);
                    psm.setDesignation((String) rows[4]);
                    psm.setPayslipstartingdate(startyear);
                    psm.setPayslipenddate(endyear.substring(2, 4));
                    psm.setRegion(SubString(region, 15));

                    PaySlip_Earn_Deduction_Model psedm = null;

                    List<PaySlip_Earn_Deduction_Model> regularlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    List<PaySlip_Earn_Deduction_Model> suplist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    double basic_total = 0;
                    double spay_total = 0;
                    double pp_total = 0;
                    double grpay_total = 0;
                    double da_total = 0;
                    double tot_total = 0;

                    double sup_basictotal = 0;
                    double sup_spaytotal = 0;
                    double sup_pptotal = 0;
                    double sup_grpaytotal = 0;
                    double sup_datotal = 0;
                    double sup_tottotal = 0;

                    double grant_basictotal = 0;
                    double grant_spaytotal = 0;
                    double grant_pptotal = 0;
                    double grant_grpaytotal = 0;
                    double grant_datotal = 0;
                    double grant_tottotal = 0;

                    StringBuilder earnbuilder = null;
                    StringBuilder suppaybillbuilder = null;
                    StringBuilder supearningsbuilder = null;

//                System.out.println("Regular*********************************");

                    Iterator itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_REGULAR, END_YEAR_REGULAR).iterator();
                    while (itr.hasNext()) {
                        // <editor-fold defaultstate="collapsed" desc="REGULAR">
                        MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(epfno+" Regular = "+mvo.getMonth() + "/" + mvo.getYear());

                        earnbuilder = new StringBuilder();
                        earnbuilder.append("select eet.earningmasterid, eet.amount from employeeearningstransactions eet ");
                        earnbuilder.append("left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid ");
                        earnbuilder.append("where ");
                        earnbuilder.append("eet.cancelled is false ");
                        earnbuilder.append("and eet.earningmasterid in ('E01','E02','E03','E04','E25') ");
                        earnbuilder.append("and eet.accregion='" + LoggedInRegion + "' ");
                        earnbuilder.append("and ppd.accregion='" + LoggedInRegion + "' ");
                        earnbuilder.append("and ppd.process is true ");
                        earnbuilder.append("and ppd.employeeprovidentfundnumber='" + epfno + "' ");
                        earnbuilder.append("and year=" + mvo.getYear() + " ");
                        earnbuilder.append("and month=" + mvo.getMonth() + " ");

//                    System.out.println("Regularquery = " + earnbuilder.toString());

                        SQLQuery payrollRegularquery = session.createSQLQuery(earnbuilder.toString());

                        Map<String, Double> earningsmap = new HashMap<String, Double>();
                        psedm = new PaySlip_Earn_Deduction_Model();
                        double total = 0;
                        for (ListIterator it1 = payrollRegularquery.list().listIterator(); it1.hasNext();) {
                            Object[] row1 = (Object[]) it1.next();
                            String earningmasterid = (String) row1[0];
                            BigDecimal amo = (BigDecimal) row1[1];
                            double amount = amo.doubleValue();
                            earningsmap.put(earningmasterid, amount);
                            total += amount;
                        }
                        psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));
                        if (total == 0) {
                            psedm.setTotalamount("");
                        } else {
                            psedm.setTotalamount(decimalFormat.format(total));
                            tot_total += total;
                        }
//                        psedm.setTotalamount(String.valueOf(total));
                        //Basic
                        if (earningsmap.get("E01") != null) {
                            psedm.setBasic(decimalFormat.format(earningsmap.get("E01")));
                            basic_total += earningsmap.get("E01");
                        } else {
                            psedm.setBasic("");
                        }
                        //Special Pay
                        if (earningsmap.get("E02") != null) {
                            psedm.setSpay(decimalFormat.format(earningsmap.get("E02")));
                            spay_total += earningsmap.get("E02");
//                            psedm.setSpay(String.valueOf(earningsmap.get("E02")));
                        } else {
                            psedm.setSpay("");
                        }
                        //Per Pay
                        if (earningsmap.get("E03") != null) {
                            psedm.setPp(decimalFormat.format(earningsmap.get("E03")));
                            pp_total += earningsmap.get("E03");
//                            psedm.setPp(String.valueOf(earningsmap.get("E03")));
                        } else {
                            psedm.setPp("");
                        }
                        //DA
                        if (earningsmap.get("E04") != null) {
                            psedm.setDa(decimalFormat.format(earningsmap.get("E04")));
                            da_total += earningsmap.get("E04");
//                            psedm.setDa(String.valueOf(earningsmap.get("E04")));
                        } else {
                            psedm.setDa("");
                        }
                        //Grade Pay
                        if (earningsmap.get("E25") != null) {
                            psedm.setGrpay(decimalFormat.format(earningsmap.get("E25")));
                            grpay_total += earningsmap.get("E25");
//                            psedm.setGrpay(String.valueOf(earningsmap.get("E25")));
                        } else {
                            psedm.setGrpay("");
                        }
                        regularlist.add(psedm);
                        // </editor-fold>
                    }

                    psm.setEarningslist(regularlist);

//                System.out.println("Supplementary*********************************");

                    itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_SUPPLEMENTARY, END_YEAR_SUPPLEMENTARY).iterator();
                    while (itr.hasNext()) {
                        // <editor-fold defaultstate="collapsed" desc="SUPPLEMENTARY">
                        MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(epfno+" Supplementary = "+mvo.getMonth() + "/" + mvo.getYear());

                        suppaybillbuilder = new StringBuilder();
                        suppaybillbuilder.append("select sppd.id, spb.type, sppd.calculatedmonth, sppd.calculatedyear  from supplementarypayrollprocessingdetails  sppd ");
                        suppaybillbuilder.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                        suppaybillbuilder.append("where ");
                        suppaybillbuilder.append("sppd.cancelled is false ");
                        suppaybillbuilder.append("and sppd.accregion='" + LoggedInRegion + "' ");
                        suppaybillbuilder.append("and sppd.calculatedyear=" + mvo.getYear() + " ");
                        suppaybillbuilder.append("and sppd.calculatedmonth=" + mvo.getMonth() + " ");
                        suppaybillbuilder.append("and spb.accregion='" + LoggedInRegion + "' ");
                        suppaybillbuilder.append("and spb.cancelled is false ");
                        suppaybillbuilder.append("and spb.type not in ('SLINCREMENTMANUAL', 'LEAVESURRENDER') ");
                        suppaybillbuilder.append("and spb.employeeprovidentfundnumber='" + epfno + "'");

                        SQLQuery supplementarypaybillquery = session.createSQLQuery(suppaybillbuilder.toString());
                        String supplementarytype = "";

                        if (supplementarypaybillquery.list().size() > 0) {
                            for (ListIterator is = supplementarypaybillquery.list().listIterator(); is.hasNext();) {
                                // <editor-fold defaultstate="collapsed" desc="supplementarypaybillquery">
                                Object[] rs = (Object[]) is.next();
                                String supplementarypayrollprocessid = (String) rs[0];
                                supplementarytype = (String) rs[1];

                                supearningsbuilder = new StringBuilder();
                                supearningsbuilder.append("select earningmasterid, amount from supplementaryemployeeearningstransactions ");
                                supearningsbuilder.append("where ");
                                supearningsbuilder.append("cancelled is false ");
                                supearningsbuilder.append("and accregion='" + LoggedInRegion + "' ");
                                supearningsbuilder.append("and earningmasterid in ('E01','E02','E03','E04','E25') ");
                                supearningsbuilder.append("and supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessid + "'");

                                SQLQuery supplementaryearningquery = session.createSQLQuery(supearningsbuilder.toString());

                                Map<String, Double> sup_earningsmap = new HashMap<String, Double>();
                                psedm = new PaySlip_Earn_Deduction_Model();

                                if (supplementaryearningquery.list().size() > 0) {
                                    // <editor-fold defaultstate="collapsed" desc="SUPPLEMENTARY IF PART">
                                    double total = 0;
                                    for (ListIterator sit3 = supplementaryearningquery.list().listIterator(); sit3.hasNext();) {
                                        Object[] srow2 = (Object[]) sit3.next();
                                        String earningmasterid = (String) srow2[0];
                                        BigDecimal amo = (BigDecimal) srow2[1];
                                        double amount = amo.doubleValue();
                                        sup_earningsmap.put(earningmasterid, amount);
                                        total += amount;
                                    }
                                    psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));
                                    if (total == 0) {
                                        psedm.setTotalamount("");
                                    } else {
                                        psedm.setTotalamount(decimalFormat.format(total));
                                        sup_tottotal += total;
                                    }
//                                    psedm.setTotalamount(String.valueOf(total));
                                    //Basic
                                    if (sup_earningsmap.get("E01") != null) {
                                        psedm.setBasic(decimalFormat.format(sup_earningsmap.get("E01")));
                                        sup_basictotal += sup_earningsmap.get("E01");
                                    } else {
                                        psedm.setBasic("");
                                    }
                                    //Special Pay
                                    if (sup_earningsmap.get("E02") != null) {
                                        psedm.setSpay(decimalFormat.format(sup_earningsmap.get("E02")));
                                        sup_spaytotal += sup_earningsmap.get("E02");
                                    } else {
                                        psedm.setSpay("");
                                    }
                                    //Per Pay
                                    if (sup_earningsmap.get("E03") != null) {
                                        psedm.setPp(decimalFormat.format(sup_earningsmap.get("E03")));
                                        sup_pptotal += sup_earningsmap.get("E03");
                                    } else {
                                        psedm.setPp("");
                                    }
                                    //DA
                                    if (sup_earningsmap.get("E04") != null) {
                                        psedm.setDa(decimalFormat.format(sup_earningsmap.get("E04")));
                                        sup_datotal += sup_earningsmap.get("E04");
                                    } else {
                                        psedm.setDa("");
                                    }
                                    //Grade Pay
                                    if (sup_earningsmap.get("E25") != null) {
                                        psedm.setGrpay(decimalFormat.format(sup_earningsmap.get("E25")));
                                        sup_grpaytotal += sup_earningsmap.get("E25");
                                    } else {
                                        psedm.setGrpay("");
                                    }
                                    psedm.setSupplementarytype(supplementarytype);
                                    suplist.add(psedm);
                                    // </editor-fold>
                                }
                                // </editor-fold>
                            }
                        }
                        // </editor-fold>
                    }

                    psm.setSupplementarylist(suplist);

                    grant_basictotal = basic_total + sup_basictotal;
                    grant_spaytotal = spay_total + sup_spaytotal;
                    grant_pptotal = pp_total + sup_pptotal;
                    grant_datotal = da_total + sup_datotal;
                    grant_grpaytotal = grpay_total + sup_grpaytotal;
                    grant_tottotal = tot_total + sup_tottotal;


                    Map<String, String> totalMap = new HashMap<String, String>();
                    totalMap.put("basic_total", decimalFormat.format(basic_total));
                    totalMap.put("spay_total", decimalFormat.format(spay_total));
                    totalMap.put("pp_total", decimalFormat.format(pp_total));
                    totalMap.put("grpay_total", decimalFormat.format(grpay_total));
                    totalMap.put("da_total", decimalFormat.format(da_total));
                    totalMap.put("tot_total", decimalFormat.format(tot_total));

                    totalMap.put("sup_basictotal", decimalFormat.format(sup_basictotal));
                    totalMap.put("sup_spaytotal", decimalFormat.format(sup_spaytotal));
                    totalMap.put("sup_pptotal", decimalFormat.format(sup_pptotal));
                    totalMap.put("sup_grpaytotal", decimalFormat.format(sup_grpaytotal));
                    totalMap.put("sup_datotal", decimalFormat.format(sup_datotal));
                    totalMap.put("sup_tottotal", decimalFormat.format(sup_tottotal));

                    totalMap.put("grant_basictotal", decimalFormat.format(grant_basictotal));
                    totalMap.put("grant_spaytotal", decimalFormat.format(grant_spaytotal));
                    totalMap.put("grant_pptotal", decimalFormat.format(grant_pptotal));
                    totalMap.put("grant_grpaytotal", decimalFormat.format(grant_grpaytotal));
                    totalMap.put("grant_datotal", decimalFormat.format(grant_datotal));
                    totalMap.put("grant_tottotal", decimalFormat.format(grant_tottotal));

                    psm.setTotalmap(totalMap);

//                System.out.println("##########################################################################################################################");
//
//                Iterator itr1 = psm.getSupplementarylist().iterator();
//                while (itr1.hasNext()) {
//                    PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr1.next();
//                    System.out.println(psedm1.getPeriod() + "\t" + psedm1.getBasic() + "\t" + psedm1.getSpay() + "\t" + psedm1.getPp() + "\t" + psedm1.getDa() + "\t" + psedm1.getGrpay() + "\t" + psedm1.getTotalamount() + "\t" + psedm1.getCalculatedperiod());
//                }
//
//                System.out.println("##########################################################################################################################");
                    bonusBDPReport.getBonusBDPPrintWriter(psm, filePathwithName);
                    slipno++;
                    // </editor-fold>
                }

            }

        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map RetiredInTransferEmployeeBonusPDPPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startmonth, String startyear, String endmonth, String endyear, String epfno1, String category, String section, String filePathwithName) {
        Map map = new HashMap();
        try {
            System.out.println("********************* BonusBillServiceImpl class EmployeeBonusPDPPrintOut method is calling *****************");
            BonusBDPReport bonusBDPReport = new BonusBDPReport();
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            int START_MONTH = Integer.valueOf(startmonth) + 1;
            int START_YEAR = Integer.valueOf(startyear);
            int END_MONTH_REGULAR = Integer.valueOf(endmonth) + 1;
            int END_YEAR_REGULAR = Integer.valueOf(endyear);
            int END_MONTH_SUPPLEMENTARY = 0;
            int END_YEAR_SUPPLEMENTARY = 0;

            if ((END_MONTH_REGULAR + 6) > 12) {
                END_MONTH_SUPPLEMENTARY = (END_MONTH_REGULAR + 6) - 12;
                END_YEAR_SUPPLEMENTARY = END_YEAR_REGULAR + 1;
            } else {
                END_MONTH_SUPPLEMENTARY = END_MONTH_REGULAR + 6;
                END_YEAR_SUPPLEMENTARY = END_YEAR_REGULAR;
            }
            String pfno = "";
            StringBuilder ssb = new StringBuilder();
            if (epfno1.trim().length() > 0) {
                ssb.append("SELECT epfno, region FROM employeemaster where epfno='" + epfno1 + "' ");
            } else {
                ssb.append("SELECT epfno, region FROM employeemaster where region='" + LoggedInRegion + "' and section='" + section + "'");
            }
            SQLQuery employeequeryssb = session.createSQLQuery(ssb.toString());
            for (ListIterator itsssb = employeequeryssb.list().listIterator(); itsssb.hasNext();) {
                Object[] rowssb = (Object[]) itsssb.next();
                pfno = (String) rowssb[0];
//            System.out.println("startmonth =" + START_MONTH);
//            System.out.println("startyear = " + START_YEAR);
//            System.out.println("endmonth = " + END_MONTH_REGULAR);
//            System.out.println("endyear =" + END_YEAR_REGULAR);
//            System.out.println("END_MONTH_SUPPLEMENTARY -> " + END_MONTH_SUPPLEMENTARY);
//            System.out.println("END_YEAR_SUPPLEMENTARY -> " + END_YEAR_SUPPLEMENTARY);

                BonusBDPReport bDPReport = new BonusBDPReport();
                PaySlipModel psm = null;

                StringBuilder sb = new StringBuilder();

//                if (category.equalsIgnoreCase("R")) {

                sb.append("SELECT epfno, employeename, rm.regionname, sn.sectionname, dm.designation, process FROM employeemaster em ");
                sb.append("left join regionmaster rm on '" + LoggedInRegion + "'=rm.id ");
                sb.append("left join sectionmaster sn on sn.id=em.section ");
                sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
                sb.append("where ");
                sb.append("( em.region in('RET','TRANS') or '" + LoggedInRegion + "' in (select accregion from payrollprocessingdetails where employeeprovidentfundnumber='" + pfno + "' group by accregion ) )");
                sb.append("and cast(dm.orderno as integer) > 17 ");
                if (pfno.length() > 0) {
                    sb.append("and epfno='" + pfno + "' ");
                }
                sb.append("order by rm.id, sn.id, cast(dm.orderno as integer)");
//                } else {
//                    sb.append("SELECT epfno, employeename, rm.regionname, sn.sectionname, dm.designation, process FROM employeemaster em ");
//                    sb.append("left join regionmaster rm on rm.id='" + LoggedInRegion + "' ");
//                    sb.append("left join sectionmaster sn on sn.id=em.section ");
//                    sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
//                    sb.append("where ");
//                    sb.append("(em.region='TRANS' or  em.region='RET') ");
//                    sb.append("and cast(dm.orderno as integer) > 17 ");
//                    if (pfno.length() > 0) {
//                        sb.append("and epfno='" + pfno + "' ");
//                    }
//                    sb.append("order by rm.id, sn.id, cast(dm.orderno as integer)");
//                }

                System.out.println("query = " + sb.toString());

                SQLQuery employeequery = session.createSQLQuery(sb.toString());
                int slipno = 1;

                for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="ALL">
                    psm = new PaySlipModel();
                    Object[] rows = (Object[]) its.next();
                    String epfno = (String) rows[0];
                    String employeename = (String) rows[1];
                    String region = (String) rows[2];
                    String sectionname = (String) rows[3];
                    String designation = (String) rows[4];

                    psm.setSlipno(String.valueOf(slipno));
                    psm.setPfno(epfno);
                    psm.setEmployeename((String) rows[1]);
                    psm.setBranch((String) rows[2]);
                    psm.setSectionname((String) rows[3]);
                    psm.setDesignation((String) rows[4]);
                    psm.setPayslipstartingdate(startyear);
                    psm.setPayslipenddate(endyear.substring(2, 4));
                    psm.setRegion(SubString(region, 15));

                    PaySlip_Earn_Deduction_Model psedm = null;

                    List<PaySlip_Earn_Deduction_Model> regularlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    List<PaySlip_Earn_Deduction_Model> suplist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                    double basic_total = 0;
                    double spay_total = 0;
                    double pp_total = 0;
                    double grpay_total = 0;
                    double da_total = 0;
                    double tot_total = 0;

                    double sup_basictotal = 0;
                    double sup_spaytotal = 0;
                    double sup_pptotal = 0;
                    double sup_grpaytotal = 0;
                    double sup_datotal = 0;
                    double sup_tottotal = 0;

                    double grant_basictotal = 0;
                    double grant_spaytotal = 0;
                    double grant_pptotal = 0;
                    double grant_grpaytotal = 0;
                    double grant_datotal = 0;
                    double grant_tottotal = 0;

                    StringBuilder earnbuilder = null;
                    StringBuilder suppaybillbuilder = null;
                    StringBuilder supearningsbuilder = null;

//                System.out.println("Regular*********************************");

                    Iterator itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_REGULAR, END_YEAR_REGULAR).iterator();
                    while (itr.hasNext()) {
                        // <editor-fold defaultstate="collapsed" desc="REGULAR">
                        MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(epfno+" Regular = "+mvo.getMonth() + "/" + mvo.getYear());

                        earnbuilder = new StringBuilder();
                        earnbuilder.append("select eet.earningmasterid, eet.amount from employeeearningstransactions eet ");
                        earnbuilder.append("left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid ");
                        earnbuilder.append("where ");
                        earnbuilder.append("eet.cancelled is false ");
                        earnbuilder.append("and eet.earningmasterid in ('E01','E02','E03','E04','E25') ");
                        earnbuilder.append("and eet.accregion='" + LoggedInRegion + "' ");
                        earnbuilder.append("and ppd.accregion='" + LoggedInRegion + "' ");
                        earnbuilder.append("and ppd.process is true ");
                        earnbuilder.append("and ppd.employeeprovidentfundnumber='" + epfno + "' ");
                        earnbuilder.append("and year=" + mvo.getYear() + " ");
                        earnbuilder.append("and month=" + mvo.getMonth() + " ");

//                    System.out.println("Regularquery = " + earnbuilder.toString());

                        SQLQuery payrollRegularquery = session.createSQLQuery(earnbuilder.toString());

                        Map<String, Double> earningsmap = new HashMap<String, Double>();
                        psedm = new PaySlip_Earn_Deduction_Model();
                        double total = 0;
                        for (ListIterator it1 = payrollRegularquery.list().listIterator(); it1.hasNext();) {
                            Object[] row1 = (Object[]) it1.next();
                            String earningmasterid = (String) row1[0];
                            BigDecimal amo = (BigDecimal) row1[1];
                            double amount = amo.doubleValue();
                            earningsmap.put(earningmasterid, amount);
                            total += amount;
                        }
                        psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));
                        if (total == 0) {
                            psedm.setTotalamount("");
                        } else {
                            psedm.setTotalamount(decimalFormat.format(total));
                            tot_total += total;
                        }
//                        psedm.setTotalamount(String.valueOf(total));
                        //Basic
                        if (earningsmap.get("E01") != null) {
                            psedm.setBasic(decimalFormat.format(earningsmap.get("E01")));
                            basic_total += earningsmap.get("E01");
                        } else {
                            psedm.setBasic("");
                        }
                        //Special Pay
                        if (earningsmap.get("E02") != null) {
                            psedm.setSpay(decimalFormat.format(earningsmap.get("E02")));
                            spay_total += earningsmap.get("E02");
//                            psedm.setSpay(String.valueOf(earningsmap.get("E02")));
                        } else {
                            psedm.setSpay("");
                        }
                        //Per Pay
                        if (earningsmap.get("E03") != null) {
                            psedm.setPp(decimalFormat.format(earningsmap.get("E03")));
                            pp_total += earningsmap.get("E03");
//                            psedm.setPp(String.valueOf(earningsmap.get("E03")));
                        } else {
                            psedm.setPp("");
                        }
                        //DA
                        if (earningsmap.get("E04") != null) {
                            psedm.setDa(decimalFormat.format(earningsmap.get("E04")));
                            da_total += earningsmap.get("E04");
//                            psedm.setDa(String.valueOf(earningsmap.get("E04")));
                        } else {
                            psedm.setDa("");
                        }
                        //Grade Pay
                        if (earningsmap.get("E25") != null) {
                            psedm.setGrpay(decimalFormat.format(earningsmap.get("E25")));
                            grpay_total += earningsmap.get("E25");
//                            psedm.setGrpay(String.valueOf(earningsmap.get("E25")));
                        } else {
                            psedm.setGrpay("");
                        }
                        regularlist.add(psedm);
                        // </editor-fold>
                    }

                    psm.setEarningslist(regularlist);

//                System.out.println("Supplementary*********************************");

                    itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_SUPPLEMENTARY, END_YEAR_SUPPLEMENTARY).iterator();
                    while (itr.hasNext()) {
                        // <editor-fold defaultstate="collapsed" desc="SUPPLEMENTARY">
                        MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(epfno+" Supplementary = "+mvo.getMonth() + "/" + mvo.getYear());

                        suppaybillbuilder = new StringBuilder();
                        suppaybillbuilder.append("select sppd.id, spb.type, sppd.calculatedmonth, sppd.calculatedyear  from supplementarypayrollprocessingdetails  sppd ");
                        suppaybillbuilder.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                        suppaybillbuilder.append("where ");
                        suppaybillbuilder.append("sppd.cancelled is false ");
                        suppaybillbuilder.append("and sppd.accregion='" + LoggedInRegion + "' ");
                        suppaybillbuilder.append("and sppd.calculatedyear=" + mvo.getYear() + " ");
                        suppaybillbuilder.append("and sppd.calculatedmonth=" + mvo.getMonth() + " ");
                        suppaybillbuilder.append("and spb.accregion='" + LoggedInRegion + "' ");
                        suppaybillbuilder.append("and spb.cancelled is false ");
                        suppaybillbuilder.append("and spb.type not in ('SLINCREMENTMANUAL', 'LEAVESURRENDER') ");
                        suppaybillbuilder.append("and spb.employeeprovidentfundnumber='" + epfno + "'");

                        SQLQuery supplementarypaybillquery = session.createSQLQuery(suppaybillbuilder.toString());
                        String supplementarytype = "";

                        if (supplementarypaybillquery.list().size() > 0) {
                            for (ListIterator is = supplementarypaybillquery.list().listIterator(); is.hasNext();) {
                                // <editor-fold defaultstate="collapsed" desc="supplementarypaybillquery">
                                Object[] rs = (Object[]) is.next();
                                String supplementarypayrollprocessid = (String) rs[0];
                                supplementarytype = (String) rs[1];

                                supearningsbuilder = new StringBuilder();
                                supearningsbuilder.append("select earningmasterid, amount from supplementaryemployeeearningstransactions ");
                                supearningsbuilder.append("where ");
                                supearningsbuilder.append("cancelled is false ");
                                supearningsbuilder.append("and accregion='" + LoggedInRegion + "' ");
                                supearningsbuilder.append("and earningmasterid in ('E01','E02','E03','E04','E25') ");
                                supearningsbuilder.append("and supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessid + "'");

                                SQLQuery supplementaryearningquery = session.createSQLQuery(supearningsbuilder.toString());

                                Map<String, Double> sup_earningsmap = new HashMap<String, Double>();
                                psedm = new PaySlip_Earn_Deduction_Model();

                                if (supplementaryearningquery.list().size() > 0) {
                                    // <editor-fold defaultstate="collapsed" desc="SUPPLEMENTARY IF PART">
                                    double total = 0;
                                    for (ListIterator sit3 = supplementaryearningquery.list().listIterator(); sit3.hasNext();) {
                                        Object[] srow2 = (Object[]) sit3.next();
                                        String earningmasterid = (String) srow2[0];
                                        BigDecimal amo = (BigDecimal) srow2[1];
                                        double amount = amo.doubleValue();
                                        sup_earningsmap.put(earningmasterid, amount);
                                        total += amount;
                                    }
                                    psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));
                                    if (total == 0) {
                                        psedm.setTotalamount("");
                                    } else {
                                        psedm.setTotalamount(decimalFormat.format(total));
                                        sup_tottotal += total;
                                    }
//                                    psedm.setTotalamount(String.valueOf(total));
                                    //Basic
                                    if (sup_earningsmap.get("E01") != null) {
                                        psedm.setBasic(decimalFormat.format(sup_earningsmap.get("E01")));
                                        sup_basictotal += sup_earningsmap.get("E01");
                                    } else {
                                        psedm.setBasic("");
                                    }
                                    //Special Pay
                                    if (sup_earningsmap.get("E02") != null) {
                                        psedm.setSpay(decimalFormat.format(sup_earningsmap.get("E02")));
                                        sup_spaytotal += sup_earningsmap.get("E02");
                                    } else {
                                        psedm.setSpay("");
                                    }
                                    //Per Pay
                                    if (sup_earningsmap.get("E03") != null) {
                                        psedm.setPp(decimalFormat.format(sup_earningsmap.get("E03")));
                                        sup_pptotal += sup_earningsmap.get("E03");
                                    } else {
                                        psedm.setPp("");
                                    }
                                    //DA
                                    if (sup_earningsmap.get("E04") != null) {
                                        psedm.setDa(decimalFormat.format(sup_earningsmap.get("E04")));
                                        sup_datotal += sup_earningsmap.get("E04");
                                    } else {
                                        psedm.setDa("");
                                    }
                                    //Grade Pay
                                    if (sup_earningsmap.get("E25") != null) {
                                        psedm.setGrpay(decimalFormat.format(sup_earningsmap.get("E25")));
                                        sup_grpaytotal += sup_earningsmap.get("E25");
                                    } else {
                                        psedm.setGrpay("");
                                    }
                                    psedm.setSupplementarytype(supplementarytype);
                                    suplist.add(psedm);
                                    // </editor-fold>
                                }
                                // </editor-fold>
                            }
                        }
                        // </editor-fold>
                    }

                    psm.setSupplementarylist(suplist);

                    grant_basictotal = basic_total + sup_basictotal;
                    grant_spaytotal = spay_total + sup_spaytotal;
                    grant_pptotal = pp_total + sup_pptotal;
                    grant_datotal = da_total + sup_datotal;
                    grant_grpaytotal = grpay_total + sup_grpaytotal;
                    grant_tottotal = tot_total + sup_tottotal;


                    Map<String, String> totalMap = new HashMap<String, String>();
                    totalMap.put("basic_total", decimalFormat.format(basic_total));
                    totalMap.put("spay_total", decimalFormat.format(spay_total));
                    totalMap.put("pp_total", decimalFormat.format(pp_total));
                    totalMap.put("grpay_total", decimalFormat.format(grpay_total));
                    totalMap.put("da_total", decimalFormat.format(da_total));
                    totalMap.put("tot_total", decimalFormat.format(tot_total));

                    totalMap.put("sup_basictotal", decimalFormat.format(sup_basictotal));
                    totalMap.put("sup_spaytotal", decimalFormat.format(sup_spaytotal));
                    totalMap.put("sup_pptotal", decimalFormat.format(sup_pptotal));
                    totalMap.put("sup_grpaytotal", decimalFormat.format(sup_grpaytotal));
                    totalMap.put("sup_datotal", decimalFormat.format(sup_datotal));
                    totalMap.put("sup_tottotal", decimalFormat.format(sup_tottotal));

                    totalMap.put("grant_basictotal", decimalFormat.format(grant_basictotal));
                    totalMap.put("grant_spaytotal", decimalFormat.format(grant_spaytotal));
                    totalMap.put("grant_pptotal", decimalFormat.format(grant_pptotal));
                    totalMap.put("grant_grpaytotal", decimalFormat.format(grant_grpaytotal));
                    totalMap.put("grant_datotal", decimalFormat.format(grant_datotal));
                    totalMap.put("grant_tottotal", decimalFormat.format(grant_tottotal));

                    psm.setTotalmap(totalMap);

//                System.out.println("##########################################################################################################################");
//
//                Iterator itr1 = psm.getSupplementarylist().iterator();
//                while (itr1.hasNext()) {
//                    PaySlip_Earn_Deduction_Model psedm1 = (PaySlip_Earn_Deduction_Model) itr1.next();
//                    System.out.println(psedm1.getPeriod() + "\t" + psedm1.getBasic() + "\t" + psedm1.getSpay() + "\t" + psedm1.getPp() + "\t" + psedm1.getDa() + "\t" + psedm1.getGrpay() + "\t" + psedm1.getTotalamount() + "\t" + psedm1.getCalculatedperiod());
//                }
//
//                System.out.println("##########################################################################################################################");
                    bonusBDPReport.getBonusBDPPrintWriter(psm, filePathwithName);
                    slipno++;
                    // </editor-fold>
                }

            }

        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
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
}
