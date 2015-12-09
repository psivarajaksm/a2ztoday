/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.common.CommonUtility;
import com.onward.persistence.payroll.Sectionmaster;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import com.onward.valueobjects.PaySlipModel;
import java.util.ListIterator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.SQLQuery;
import com.onward.reports.IncomeTaxExcelReport;
import com.onward.reports.incometax.IncomeTaxReport;
import com.onward.valueobjects.MonthValueObject;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Prince vijayakumar M
 */
public class IncomeTaxServiceImpl implements IncomeTaxService {

    private String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @GlobalDBOpenCloseAndUserPrivilages
    public Map IncomeTaxTentativeParticularsPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String endyear, String pfno, String reporttype, String filePathwithName, String sectionid) {
        Map map = new HashMap();
        try {
            System.out.println("********************* IncomeTaxServiceImpl class IncomeTaxTentativeParticularsPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            IncomeTaxReport taxReport = new IncomeTaxReport();
            IncomeTaxExcelReport excelReport = new IncomeTaxExcelReport();
            int START_MONTH = 0;
            int START_YEAR = Integer.valueOf(startyear);
            int END_MONTH_REGULAR = 0;
            int END_YEAR_REGULAR = Integer.valueOf(endyear);

            if (("S13".equalsIgnoreCase(sectionid)) || ("S14".equalsIgnoreCase(sectionid))) {
                START_MONTH = 3;
                END_MONTH_REGULAR = 2;
            } else {
                START_MONTH = 4;
                END_MONTH_REGULAR = 3;
            }

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT epfno, employeename, rm.regionname, sn.sectionname, dm.designation, process FROM employeemaster em ");
            sb.append("left join regionmaster rm on em.region=rm.id ");
            sb.append("left join sectionmaster sn on sn.id=em.section ");
            sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
            sb.append("where ");
            sb.append("sn.id='" + sectionid + "' ");
            sb.append("and em.region='" + LoggedInRegion + "' ");
            if (pfno.length() > 0) {
                sb.append("and epfno='" + pfno + "' ");
            }
            sb.append("order by rm.id, sn.id, cast(dm.orderno as integer)");

//            sb.append("SELECT epfno, employeename, rm.regionname, sn.sectionname, dm.designation, process FROM employeemaster em ");
//            sb.append("left join regionmaster rm on em.region=rm.id ");
//            sb.append("left join sectionmaster sn on sn.id=em.section ");
//            sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
//            sb.append("where ");
//            sb.append("em.region='" + LoggedInRegion + "' ");
////            sb.append("and cast(dm.orderno as integer) > 17 ");
//            if (pfno.length() > 0) {
//                sb.append("and epfno='" + pfno + "' ");
//            }
//            sb.append("order by rm.id, sn.id, cast(dm.orderno as integer)");

            PaySlipModel psm = null;

            SQLQuery employeequery = session.createSQLQuery(sb.toString());
            //System.out.println("sb.toString()" + sb.toString());
            int slipno = 1;

            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold desc="Start Employee Query .">
                psm = new PaySlipModel();
                Object[] rows = (Object[]) its.next();
                String epfno = (String) rows[0];
                String employeename = (String) rows[1];
                String region = (String) rows[2];
                String sectionname = (String) rows[3];
                String designation = (String) rows[4];
                psm.setSlipno(String.valueOf(slipno));
                psm.setPfno(epfno);
                psm.setEmployeename(employeename);
                psm.setBranch(region);
                psm.setSectionname(sectionname);
                psm.setDesignation(designation);
                psm.setPayslipstartingdate(startyear);
                psm.setPayslipenddate(endyear.substring(2, 4));

                double basic_total = 0;
                double spay_total = 0;
                double pp_total = 0;
                double da_total = 0;
                double grpay_total = 0;
                double hra_total = 0;
                double cca_total = 0;
                double others_total = 0;
                double gross_total = 0;
                double it_total = 0;
                double epf_total = 0;
                double gpf_total = 0;
                double vpf_total = 0;
                double proftax_total = 0;
                double proftax_total0 = 0;
                double proftax_total1 = 0;
                double proftax_total2 = 0;
                double proftax_total3 = 0;
                double proftax_total4 = 0;
                double his_total = 0;
                double fbf_total = 0;

                double gpf = 0.00;
                double cpsregular = 0.00;
                double cpsrecovery = 0.00;
                double fbfarrloan = 0.00;
                double fbfarr = 0.00;
                double fbfgovt = 0.00;
                double fbfregular = 0.00;

//                double da_basic_total = 0;
//                double da_spay_total = 0;
//                double da_pp_total = 0;
//                double da_da_total = 0;
//                double da_grpay_total = 0;
//                double da_hra_total = 0;
//                double da_cca_total = 0;
//                double da_others_total = 0;
//                double da_gross_total = 0;
//                double da_it_total = 0;
//                double da_epf_total = 0;
//                double da_vpf_total = 0;
//                double da_proftax_total = 0;

                double grant_basic_total = 0;
                double grant_spay_total = 0;
                double grant_pp_total = 0;
                double grant_da_total = 0;
                double grant_grpay_total = 0;
                double grant_hra_total = 0;
                double grant_cca_total = 0;
                double grant_others_total = 0;
                double grant_gross_total = 0;
                double grant_it_total = 0;
                double grant_epf_total = 0;
                double grant_vpf_total = 0;
                double grant_proftax_total = 0;
                double grant_his_total = 0;
                double grant_fbf_total = 0;


                /*
                 * Start Regular Report
                 */
                PaySlip_Earn_Deduction_Model psedm = null;

                List<PaySlip_Earn_Deduction_Model> regularlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

//                List<PaySlip_Earn_Deduction_Model> suplist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                List<PaySlip_Earn_Deduction_Model> incrementlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                List<PaySlip_Earn_Deduction_Model> surrenderlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                List<PaySlip_Earn_Deduction_Model> daarrearlist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                List<PaySlip_Earn_Deduction_Model> bonuslist = new ArrayList<PaySlip_Earn_Deduction_Model>();

                int SMONTH = START_MONTH;
                int SYEAR = START_YEAR;
                int EMONTH = END_MONTH_REGULAR;
                int EYEAR = END_YEAR_REGULAR;

                String basic_temp = null;
                String spay_temp = null;
                String pp_temp = null;
                String da_temp = null;
                String grpay_temp = null;
                String hra_temp = null;
                String cca_temp = null;
                String others_temp = null;
                String gross_temp = null;
                String it_temp = null;
                String epf_temp = null;
                String vpf_temp = null;
                String proftax_temp = null;
                String his_temp = null;
                String fbf_temp = null;

                Iterator itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_REGULAR, END_YEAR_REGULAR).iterator();
                while (itr.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="REGULAR and SUPPLEMENTARY PART">
                    MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(mvo.getMonth() + "\"" + mvo.getYear());
                    int current_month = mvo.getMonth();

                    StringBuilder sbreg = new StringBuilder();

                    sbreg.append("( ");
                    sbreg.append("(select eet.earningmasterid, eet.amount, 'EARNINGS' as type from employeeearningstransactions eet ");
                    sbreg.append("left join payrollprocessingdetails ppd on ppd.id=eet.payrollprocessingdetailsid ");
                    sbreg.append("where ");
                    sbreg.append("eet.cancelled is false ");
//                    sbreg.append("and eet.earningmasterid in ('E01', 'E02', 'E03', 'E04', 'E06', 'E07', 'E25') ");
                    sbreg.append("and eet.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and ppd.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and ppd.process is true ");
                    sbreg.append("and ppd.employeeprovidentfundnumber='" + epfno + "' ");
                    sbreg.append("and year=" + mvo.getYear() + "  ");
                    sbreg.append("and month=" + mvo.getMonth() + ") ");
                    sbreg.append("union ");
                    sbreg.append("(select edt.deductionmasterid, edt.amount, 'DEDUCTION' as type from employeedeductionstransactions edt ");
                    sbreg.append("left join payrollprocessingdetails ppd on ppd.id=edt.payrollprocessingdetailsid ");
                    sbreg.append("where ");
                    sbreg.append("edt.cancelled is false ");
//                    sbreg.append("and edt.deductionmasterid in ('D12', 'D02', 'D03', 'L31') ");
                    //added for reugual/deputation EPF/GPF/CPS /HIS  /FBF/FBG 
                    //added for PROF.TAX-1;2;3;4
                    sbreg.append("and edt.deductionmasterid in ('D12', 'D02', 'D03', 'L31','D01','D31','D106','D107', 'L44','D35','D28','D06','L132','L131','L133','L134') ");
                    sbreg.append("and edt.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and ppd.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and ppd.process is true ");
                    sbreg.append("and ppd.employeeprovidentfundnumber='" + epfno + "' ");
                    sbreg.append("and year=" + mvo.getYear() + "  ");
                    sbreg.append("and month=" + mvo.getMonth() + ") ");
                    sbreg.append(") ");
                    sbreg.append("union ");
                    sbreg.append("( ");
                    sbreg.append("(select seet.earningmasterid, seet.amount, 'EARNINGS' as type from supplementaryemployeeearningstransactions seet ");
                    sbreg.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid ");
                    sbreg.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbreg.append("where ");
                    sbreg.append("seet.cancelled is false ");
                    sbreg.append("and seet.accregion='" + LoggedInRegion + "' ");
//                    sbreg.append("and seet.earningmasterid in ('E01', 'E02', 'E03', 'E04', 'E06', 'E07', 'E25') ");
                    sbreg.append("and sppd.cancelled is false ");
                    sbreg.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and sppd.calculatedyear=" + mvo.getYear() + "  ");
                    sbreg.append("and sppd.calculatedmonth=" + mvo.getMonth() + " ");
                    sbreg.append("and spb.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and spb.cancelled is false ");
                    sbreg.append("and spb.type='SUPLEMENTARYBILL' ");
                    sbreg.append("and spb.employeeprovidentfundnumber='" + epfno + "') ");
                    sbreg.append("union ");
                    sbreg.append("(select sedt.deductionmasterid, sedt.amount, 'DEDUCTION' as type from supplementaryemployeedeductionstransactions sedt ");
                    sbreg.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid ");
                    sbreg.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbreg.append("where ");
                    sbreg.append("sedt.cancelled is false ");
                    sbreg.append("and sedt.accregion='" + LoggedInRegion + "' ");
                    //sbreg.append("and sedt.deductionmasterid in ('D12', 'D02', 'D03', 'L31') ");
                    //added for reugual/deputation EPF/GPF/CPS /HIS  /FBF/FBG 
                    //added for PROF.TAX-1;2;3;4
                    sbreg.append("and sedt.deductionmasterid in ('D12', 'D02', 'D03', 'L31','D01','D31','D106','D107' , 'L44','D35','D28','D06','L132','L131','L133','L134' ) ");
                    sbreg.append("and sppd.cancelled is false ");
                    sbreg.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and sppd.calculatedyear=" + mvo.getYear() + "  ");
                    sbreg.append("and sppd.calculatedmonth=" + mvo.getMonth() + " ");
                    sbreg.append("and spb.accregion='" + LoggedInRegion + "' ");
                    sbreg.append("and spb.cancelled is false ");
                    sbreg.append("and spb.type='SUPLEMENTARYBILL' ");
                    sbreg.append("and spb.employeeprovidentfundnumber='" + epfno + "') ");
                    sbreg.append(") ");

//                    System.out.println("Regular Query = " + sbreg.toString());

                    SQLQuery RegularQuery = session.createSQLQuery(sbreg.toString());

                    Map<String, Double> regularmap = new HashMap<String, Double>();

                    psedm = new PaySlip_Earn_Deduction_Model();
                    double total = 0;
                    double otherstotal = 0;
                    for (ListIterator it1 = RegularQuery.list().listIterator(); it1.hasNext();) {
                        Object[] row1 = (Object[]) it1.next();
                        String id = (String) row1[0];
                        BigDecimal amo = (BigDecimal) row1[1];
                        double amount = amo.doubleValue();
                        String type = (String) row1[2];
                        //'E01', 'E02', 'E03', 'E04', 'E06', 'E07', 'E25'
                        regularmap.put(id, amount);
                        if (!(id.equals("E01") || id.equals("E02") || id.equals("E03") || id.equals("E04") || id.equals("E06") || id.equals("E07") || id.equals("E25")) && (type.equalsIgnoreCase("EARNINGS"))) {
                            otherstotal += amount;
                        }
                        if (type.equalsIgnoreCase("EARNINGS")) {
                            total += amount;
                        }
                    }
                    if (otherstotal > 0) {
                        regularmap.put("OTHERS", otherstotal);
                    }

                    psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));

                    //if (total == 0 && (current_month == 2 || current_month == 3)) {
                    if (total == 0 && ((current_month == 2 || (current_month == 3 && (!"S13".equalsIgnoreCase(sectionid) && !"S14".equalsIgnoreCase(sectionid)))))) {
                        psedm.setBasic(basic_temp);
                        psedm.setSpay(spay_temp);
                        psedm.setPp(pp_temp);
                        psedm.setDa(da_temp);
                        psedm.setGrpay(grpay_temp);
                        psedm.setHra(hra_temp);
                        psedm.setCca(cca_temp);
                        psedm.setOthers(others_temp);
                        psedm.setGross(gross_temp);
                        psedm.setItax(it_temp);
                        psedm.setEpf(epf_temp);
                        psedm.setVpf(vpf_temp);
                        psedm.setProftax(proftax_temp);
                        psedm.setHis(his_temp);
                        psedm.setFbf(fbf_temp);

                        if (basic_temp.length() > 0) {
                            basic_total += Double.valueOf(basic_temp);
                        }
                        if (spay_temp.length() > 0) {
                            spay_total += Double.valueOf(spay_temp);
                        }
                        if (pp_temp.length() > 0) {
                            pp_total += Double.valueOf(pp_temp);
                        }
                        if (da_temp.length() > 0) {
                            da_total += Double.valueOf(da_temp);
                        }
                        if (grpay_temp.length() > 0) {
                            grpay_total += Double.valueOf(grpay_temp);
                        }
                        if (hra_temp.length() > 0) {
                            hra_total += Double.valueOf(hra_temp);
                        }
                        if (cca_temp.length() > 0) {
                            cca_total += Double.valueOf(cca_temp);
                        }
                        if (others_temp.length() > 0) {
                            others_total += Double.valueOf(others_temp);
                        }
                        if (gross_temp.length() > 0) {
                            gross_total += Double.valueOf(gross_temp);
                        }
                        if (it_temp.length() > 0) {
                            it_total += Double.valueOf(it_temp);
                        }
                        if (epf_temp.length() > 0) {
                            epf_total += Double.valueOf(epf_temp);
                        }
                        if (vpf_temp.length() > 0) {
                            vpf_total += Double.valueOf(vpf_temp);
                        }
                        if (proftax_temp.length() > 0) {
                            proftax_total += Double.valueOf(proftax_temp);
                        }
                        if (his_temp.length() > 0) {
                            his_total += Double.valueOf(his_temp);
                        }
                        if (fbf_temp.length() > 0) {
                            fbf_total += Double.valueOf(fbf_temp);
                        }
                    } else {
                        //Gross
                        if (total == 0) {
                            psedm.setGross("");
                        } else {
                            psedm.setGross(decimalFormat.format(total));
                            gross_total += total;
                        }

                        //Basic
                        if (regularmap.get("E01") != null) {
                            psedm.setBasic(decimalFormat.format(regularmap.get("E01")));
                            basic_total += regularmap.get("E01");
                        } else {
                            psedm.setBasic("");
                        }
                        //Special Pay
                        if (regularmap.get("E02") != null) {
                            psedm.setSpay(decimalFormat.format(regularmap.get("E02")));
                            spay_total += regularmap.get("E02");
                        } else {
                            psedm.setSpay("");
                        }
                        //Per Pay
                        if (regularmap.get("E03") != null) {
                            psedm.setPp(decimalFormat.format(regularmap.get("E03")));
                            pp_total += regularmap.get("E03");
                        } else {
                            psedm.setPp("");
                        }
                        //DA
                        if (regularmap.get("E04") != null) {
                            psedm.setDa(decimalFormat.format(regularmap.get("E04")));
                            da_total += regularmap.get("E04");
                        } else {
                            psedm.setDa("");
                        }
                        //Grade Pay
                        if (regularmap.get("E25") != null) {
                            psedm.setGrpay(decimalFormat.format(regularmap.get("E25")));
                            grpay_total += regularmap.get("E25");
                        } else {
                            psedm.setGrpay("");
                        }
                        //HRA
                        if (regularmap.get("E06") != null) {
                            psedm.setHra(decimalFormat.format(regularmap.get("E06")));
                            hra_total += regularmap.get("E06");
                        } else {
                            psedm.setHra("");
                        }
                        //CCA
                        if (regularmap.get("E07") != null) {
                            psedm.setCca(decimalFormat.format(regularmap.get("E07")));
                            cca_total += regularmap.get("E07");
                        } else {
                            psedm.setCca("");
                        }
                        //Others
                        if (regularmap.get("OTHERS") != null) {
                            psedm.setOthers(decimalFormat.format(regularmap.get("OTHERS")));
                            others_total += regularmap.get("OTHERS");
                        } else {
                            psedm.setOthers("");
                        }


                        //EPF ('D12', 'D02', 'D03', 'L31','D01','D31','D106','D107' ) ") ; 'D06','D28'  FBF
                        if (regularmap.get("D02") != null) {
                            psedm.setEpf(decimalFormat.format(regularmap.get("D02")));
                            epf_total += regularmap.get("D02");
                        } else {
                            psedm.setEpf("");
                        }//VPF
                        if (regularmap.get("D03") != null) {
                            psedm.setVpf(decimalFormat.format(regularmap.get("D03")));
                            vpf_total += regularmap.get("D03");
                        } else {
                            psedm.setVpf("");
                        }//INCOME TAX
                        if (regularmap.get("D12") != null) {
                            psedm.setItax(decimalFormat.format(regularmap.get("D12")));
                            it_total += regularmap.get("D12");
                        } else {
                            psedm.setItax("");
                        }//PROFESSIONAL TAX 'L132','L131','L133','L134'
//                        if (regularmap.get("L31") != null) {
//                            psedm.setProftax(decimalFormat.format(regularmap.get("L31")));
//                            proftax_total += regularmap.get("L31");
//                        } else {
//                            psedm.setProftax("");
//                        }//PROFESSIONAL TAX1
                        if (regularmap.get("L131") != null) {
                            proftax_total0 += regularmap.get("L131");
                        } else {
                            proftax_total0 = 0.00;
                        }//PROFESSIONAL TAX1
                        if (regularmap.get("L132") != null) {
                            proftax_total1 += regularmap.get("L132");
                        } else {
                            proftax_total1 = 0.00;
                        }//PROFESSIONAL TAX2
                        if (regularmap.get("L133") != null) {
                            proftax_total2 += regularmap.get("L133");
                        } else {
                            proftax_total2 = 0.00;
                        }//PROFESSIONAL TAX3
                        if (regularmap.get("L134") != null) {
                            proftax_total3 += regularmap.get("L134");
                        } else {
                            proftax_total3 = 0.00;
                        }//PROFESSIONAL TAX4
                        if (regularmap.get("L31") != null) {
                            proftax_total4 += regularmap.get("L31");
                        } else {
                            proftax_total4 = 0.00;
                        }
                        if ((proftax_total0 != 0.00) || (proftax_total1 != 0.00) || (proftax_total2 != 0.00) || (proftax_total3 != 0.00) || (proftax_total4 != 0.00)) {
                            double prtax = proftax_total0 + proftax_total1 + proftax_total2 + proftax_total3 + proftax_total4;
                            psedm.setProftax(decimalFormat.format(prtax));
                            proftax_total += prtax;
                        } else {
                            psedm.setProftax("");
                        }
                        //GPF
                        if (regularmap.get("D01") != null) {
                            gpf = regularmap.get("D01");
                        } else {
                            gpf = 0.00;
                        }//CPS REGULAR
                        if (regularmap.get("D106") != null) {
                            cpsregular = regularmap.get("D106");
                        } else {
                            cpsregular = 0.00;
                        }//CPS RECOVERY
                        if (regularmap.get("D107") != null) {
                            cpsrecovery = regularmap.get("D107");
                        } else {
                            cpsrecovery = 0.00;
                        }//HIS
                        if (regularmap.get("D31") != null) {
                            psedm.setHis(decimalFormat.format(regularmap.get("D31")));
                            his_total += regularmap.get("D31");
                        } else {
                            psedm.setHis("");
                        }
                        if ("".equalsIgnoreCase(psedm.getEpf())) {
                            if ((gpf != 0.00) || (cpsregular != 0.00) || (cpsrecovery != 0.00)) {
                                psedm.setEpf(decimalFormat.format(gpf + cpsregular + cpsrecovery));
                                epf_total += gpf + cpsregular + cpsrecovery;
                            }
                        }//FBF/FBG
                        if (regularmap.get("L44") != null) {
                            fbfarrloan = regularmap.get("L44");
                        } else {
                            fbfarrloan = 0.00;
                        }
                        if (regularmap.get("D35") != null) {
                            fbfarr = regularmap.get("D35");
                        } else {
                            fbfarr = 0.00;
                        }
                        if (regularmap.get("D28") != null) {
                            fbfgovt = regularmap.get("D28");
                        } else {
                            fbfgovt = 0.00;
                        }
                        if (regularmap.get("D06") != null) {
                            fbfregular = regularmap.get("D06");
                        } else {
                            fbfregular = 0.00;
                        }
                        if ((fbfarrloan != 0.00) || (fbfarr != 0.00) || (fbfgovt != 0.00) || (fbfregular != 0.00)) {
                            double fbt = fbfarrloan + fbfarr + fbfgovt + fbfregular;
                            psedm.setFbf(decimalFormat.format(fbt));
                            fbf_total += fbt;
                        } else {
                            psedm.setFbf("");
                        }

                        basic_temp = psedm.getBasic();
                        spay_temp = psedm.getSpay();
                        pp_temp = psedm.getPp();
                        da_temp = psedm.getDa();
                        grpay_temp = psedm.getGrpay();
                        hra_temp = psedm.getHra();
                        cca_temp = psedm.getCca();
                        others_temp = psedm.getOthers();
                        gross_temp = psedm.getGross();
                        it_temp = psedm.getItax();
                        epf_temp = psedm.getEpf();
                        vpf_temp = psedm.getVpf();
                        proftax_temp = psedm.getProftax();
                        his_temp = psedm.getHis();
                        fbf_temp = psedm.getFbf();
                    }


                    regularlist.add(psedm);
                    // </editor-fold>
                }

                itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_REGULAR, END_YEAR_REGULAR).iterator();
                while (itr.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="LEAVE SURRENDAR PART">
                    MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(mvo.getMonth() + "\"" + mvo.getYear());

                    StringBuilder sbleave = new StringBuilder();

                    sbleave.append("(select seet.earningmasterid as payid, sum(seet.amount) as amount, 'EARNINGS' as stype from supplementaryemployeeearningstransactions seet ");
                    sbleave.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid ");
                    sbleave.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbleave.append("where ");
                    sbleave.append("seet.cancelled is false ");
                    sbleave.append("and seet.accregion='" + LoggedInRegion + "' ");
//                    sbleave.append("and seet.earningmasterid in ('E01', 'E02', 'E03', 'E04', 'E06', 'E07', 'E25') ");
                    sbleave.append("and sppd.cancelled is false ");
                    sbleave.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbleave.append("and date_part('month', spb.date)=" + mvo.getMonth() + " ");
                    sbleave.append("and date_part('year', spb.date)=" + mvo.getYear() + " ");
                    sbleave.append("and spb.accregion='" + LoggedInRegion + "' ");
                    sbleave.append("and spb.cancelled is false ");
                    sbleave.append("and spb.type='LEAVESURRENDER' ");
                    sbleave.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
                    sbleave.append("group by payid,stype) ");
                    sbleave.append("union ");
                    sbleave.append("(select sedt.deductionmasterid as payid, sum(sedt.amount) as amount, 'DEDUCTION' as stype from supplementaryemployeedeductionstransactions sedt ");
                    sbleave.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid ");
                    sbleave.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbleave.append("where ");
                    sbleave.append("sedt.cancelled is false ");
                    sbleave.append("and sedt.accregion='" + LoggedInRegion + "' ");
                    sbleave.append("and sedt.deductionmasterid in ('D12', 'D02', 'D03', 'L31') ");
                    sbleave.append("and sppd.cancelled is false ");
                    sbleave.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbleave.append("and date_part('month', spb.date)=" + mvo.getMonth() + "   ");
                    sbleave.append("and date_part('year', spb.date)=" + mvo.getYear() + "  ");
                    sbleave.append("and spb.accregion='" + LoggedInRegion + "' ");
                    sbleave.append("and spb.cancelled is false ");
                    sbleave.append("and spb.type='LEAVESURRENDER'  ");
                    sbleave.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
                    sbleave.append("group by payid,stype) ");

//                    System.out.println("Regular Query = " + sbreg.toString());

                    SQLQuery LeavesurrendarQuery = session.createSQLQuery(sbleave.toString());

                    if (LeavesurrendarQuery.list().size() > 0) {
                        Map<String, Double> surrendarmap = new HashMap<String, Double>();

                        psedm = new PaySlip_Earn_Deduction_Model();
                        double total = 0;
                        double otherstotal = 0;
                        for (ListIterator it1 = LeavesurrendarQuery.list().listIterator(); it1.hasNext();) {
                            Object[] row1 = (Object[]) it1.next();
                            String id = (String) row1[0];
                            BigDecimal amo = (BigDecimal) row1[1];
                            double amount = amo.doubleValue();
                            String type = (String) row1[2];
                            surrendarmap.put(id, amount);
                            if (!(id.equals("E01") || id.equals("E02") || id.equals("E03") || id.equals("E04") || id.equals("E06") || id.equals("E07") || id.equals("E25")) && (type.equalsIgnoreCase("EARNINGS"))) {
                                otherstotal += amount;
                            }
                            if (type.equalsIgnoreCase("EARNINGS")) {
                                total += amount;
                            }
                        }
                        if (otherstotal > 0) {
                            surrendarmap.put("OTHERS", otherstotal);
                        }

                        psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));

                        //Gross
                        if (total == 0) {
                            psedm.setGross("");
                        } else {
                            psedm.setGross(decimalFormat.format(total));
                            grant_gross_total += total;
                        }

                        //Basic
                        if (surrendarmap.get("E01") != null) {
                            psedm.setBasic(decimalFormat.format(surrendarmap.get("E01")));
                            grant_basic_total += surrendarmap.get("E01");
                        } else {
                            psedm.setBasic("");
                        }
                        //Special Pay
                        if (surrendarmap.get("E02") != null) {
                            psedm.setSpay(decimalFormat.format(surrendarmap.get("E02")));
                            grant_spay_total += surrendarmap.get("E02");
                        } else {
                            psedm.setSpay("");
                        }
                        //Per Pay
                        if (surrendarmap.get("E03") != null) {
                            psedm.setPp(decimalFormat.format(surrendarmap.get("E03")));
                            grant_pp_total += surrendarmap.get("E03");
                        } else {
                            psedm.setPp("");
                        }
                        //DA
                        if (surrendarmap.get("E04") != null) {
                            psedm.setDa(decimalFormat.format(surrendarmap.get("E04")));
                            grant_da_total += surrendarmap.get("E04");
                        } else {
                            psedm.setDa("");
                        }
                        //Grade Pay
                        if (surrendarmap.get("E25") != null) {
                            psedm.setGrpay(decimalFormat.format(surrendarmap.get("E25")));
                            grant_grpay_total += surrendarmap.get("E25");
                        } else {
                            psedm.setGrpay("");
                        }
                        //HRA
                        if (surrendarmap.get("E06") != null) {
                            psedm.setHra(decimalFormat.format(surrendarmap.get("E06")));
                            grant_hra_total += surrendarmap.get("E06");
                        } else {
                            psedm.setHra("");
                        }
                        //CCA
                        if (surrendarmap.get("E07") != null) {
                            psedm.setCca(decimalFormat.format(surrendarmap.get("E07")));
                            grant_cca_total += surrendarmap.get("E07");
                        } else {
                            psedm.setCca("");
                        }
                        //Others
                        if (surrendarmap.get("OTHERS") != null) {
                            psedm.setOthers(decimalFormat.format(surrendarmap.get("OTHERS")));
                            others_total += surrendarmap.get("OTHERS");
                        } else {
                            psedm.setOthers("");
                        }

                        //EPF
                        if (surrendarmap.get("D02") != null) {
                            psedm.setEpf(decimalFormat.format(surrendarmap.get("D02")));
                            grant_epf_total += surrendarmap.get("D02");
                        } else {
                            psedm.setEpf("");
                        }//VPF
                        if (surrendarmap.get("D03") != null) {
                            psedm.setVpf(decimalFormat.format(surrendarmap.get("D03")));
                            grant_vpf_total += surrendarmap.get("D03");
                        } else {
                            psedm.setVpf("");
                        }//INCOME TAX
                        if (surrendarmap.get("D12") != null) {
                            psedm.setItax(decimalFormat.format(surrendarmap.get("D12")));
                            grant_it_total += surrendarmap.get("D12");
                        } else {
                            psedm.setItax("");
                        }//PROFESSIONAL TAX
                        if (surrendarmap.get("L31") != null) {
                            psedm.setProftax(decimalFormat.format(surrendarmap.get("L31")));
                            grant_proftax_total += surrendarmap.get("L31");
                        } else {
                            psedm.setProftax("");
                        }
                        surrenderlist.add(psedm);
                    }
                    // </editor-fold>
                }

                itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_REGULAR, END_YEAR_REGULAR).iterator();
                while (itr.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="DA ARREAR PART">
                    MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(mvo.getMonth() + "\"" + mvo.getYear());

                    StringBuilder sbda = new StringBuilder();

                    sbda.append("(select seet.earningmasterid as payid, sum(seet.amount) as amount, 'EARNINGS' as stype from supplementaryemployeeearningstransactions seet ");
                    sbda.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid ");
                    sbda.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbda.append("where ");
                    sbda.append("seet.cancelled is false ");
                    sbda.append("and seet.accregion='" + LoggedInRegion + "' ");
                    sbda.append("and seet.earningmasterid in ('E01', 'E02', 'E03', 'E04', 'E06', 'E07', 'E25') ");
                    sbda.append("and sppd.cancelled is false ");
                    sbda.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbda.append("and date_part('month', spb.date)=" + mvo.getMonth() + " ");
                    sbda.append("and date_part('year', spb.date)=" + mvo.getYear() + " ");
                    sbda.append("and spb.accregion='" + LoggedInRegion + "' ");
                    sbda.append("and spb.cancelled is false ");
                    sbda.append("and spb.type in ('DAARREAR','DAMANUAL') ");
                    sbda.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
                    sbda.append("group by payid,stype) ");
                    sbda.append("union ");
                    sbda.append("(select sedt.deductionmasterid as payid, sum(sedt.amount) as amount, 'DEDUCTION' as stype from supplementaryemployeedeductionstransactions sedt ");
                    sbda.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid ");
                    sbda.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbda.append("where ");
                    sbda.append("sedt.cancelled is false ");
                    sbda.append("and sedt.accregion='" + LoggedInRegion + "' ");
                    sbda.append("and sedt.deductionmasterid in ('D12', 'D02', 'D03', 'L31') ");
                    sbda.append("and sppd.cancelled is false ");
                    sbda.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbda.append("and date_part('month', spb.date)=" + mvo.getMonth() + " ");
                    sbda.append("and date_part('year', spb.date)=" + mvo.getYear() + " ");
                    sbda.append("and spb.accregion='" + LoggedInRegion + "'");
                    sbda.append("and spb.cancelled is false ");
                    sbda.append("and spb.type in ('DAARREAR','DAMANUAL') ");
                    sbda.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
                    sbda.append("group by payid,stype) ");

//                    System.out.println("DAARREAR Query = " + sbda.toString());

                    SQLQuery DaareearQuery = session.createSQLQuery(sbda.toString());

                    if (DaareearQuery.list().size() > 0) {
                        Map<String, Double> damap = new HashMap<String, Double>();

                        psedm = new PaySlip_Earn_Deduction_Model();
                        double total = 0;
                        double otherstotal = 0;
                        for (ListIterator it1 = DaareearQuery.list().listIterator(); it1.hasNext();) {
                            Object[] row1 = (Object[]) it1.next();
                            String id = (String) row1[0];
                            BigDecimal amo = (BigDecimal) row1[1];
                            double amount = amo.doubleValue();
                            String type = (String) row1[2];
                            damap.put(id, amount);
                            if (!(id.equals("E01") || id.equals("E02") || id.equals("E03") || id.equals("E04") || id.equals("E06") || id.equals("E07") || id.equals("E25")) && (type.equalsIgnoreCase("EARNINGS"))) {
                                otherstotal += amount;
                            }
                            if (type.equalsIgnoreCase("EARNINGS")) {
                                total += amount;
                            }
                        }

                        if (otherstotal > 0) {
                            damap.put("OTHERS", otherstotal);
                        }

                        psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));

                        //Gross
                        if (total == 0) {
                            psedm.setGross("");
                        } else {
                            psedm.setGross(decimalFormat.format(total));
                            grant_gross_total += total;
                        }

                        //Basic
                        if (damap.get("E01") != null) {
                            psedm.setBasic(decimalFormat.format(damap.get("E01")));
                            grant_basic_total += damap.get("E01");
                        } else {
                            psedm.setBasic("");
                        }
                        //Special Pay
                        if (damap.get("E02") != null) {
                            psedm.setSpay(decimalFormat.format(damap.get("E02")));
                            grant_spay_total += damap.get("E02");
                        } else {
                            psedm.setSpay("");
                        }
                        //Per Pay
                        if (damap.get("E03") != null) {
                            psedm.setPp(decimalFormat.format(damap.get("E03")));
                            grant_pp_total += damap.get("E03");
                        } else {
                            psedm.setPp("");
                        }
                        //DA
                        if (damap.get("E04") != null) {
                            psedm.setDa(decimalFormat.format(damap.get("E04")));
                            grant_da_total += damap.get("E04");
                        } else {
                            psedm.setDa("");
                        }
                        //Grade Pay
                        if (damap.get("E25") != null) {
                            psedm.setGrpay(decimalFormat.format(damap.get("E25")));
                            grant_grpay_total += damap.get("E25");
                        } else {
                            psedm.setGrpay("");
                        }
                        //HRA
                        if (damap.get("E06") != null) {
                            psedm.setHra(decimalFormat.format(damap.get("E06")));
                            grant_hra_total += damap.get("E06");
                        } else {
                            psedm.setHra("");
                        }
                        //CCA
                        if (damap.get("E07") != null) {
                            psedm.setCca(decimalFormat.format(damap.get("E07")));
                            grant_cca_total += damap.get("E07");
                        } else {
                            psedm.setCca("");
                        }
                        //Others
                        if (damap.get("OTHERS") != null) {
                            psedm.setOthers(decimalFormat.format(damap.get("OTHERS")));
                            others_total += damap.get("OTHERS");
                        } else {
                            psedm.setOthers("");
                        }
//                        psedm.setOthers("");

                        //EPF
                        if (damap.get("D02") != null) {
                            psedm.setEpf(decimalFormat.format(damap.get("D02")));
                            grant_epf_total += damap.get("D02");
                        } else {
                            psedm.setEpf("");
                        }//VPF
                        if (damap.get("D03") != null) {
                            psedm.setVpf(decimalFormat.format(damap.get("D03")));
                            grant_vpf_total += damap.get("D03");
                        } else {
                            psedm.setVpf("");
                        }//INCOME TAX
                        if (damap.get("D12") != null) {
                            psedm.setItax(decimalFormat.format(damap.get("D12")));
                            grant_it_total += damap.get("D12");
                        } else {
                            psedm.setItax("");
                        }//PROFESSIONAL TAX
                        if (damap.get("L31") != null) {
                            psedm.setProftax(decimalFormat.format(damap.get("L31")));
                            grant_proftax_total += damap.get("L31");
                        } else {
                            psedm.setProftax("");
                        }
                        daarrearlist.add(psedm);
                    }
                    // </editor-fold>
                }

                itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_REGULAR, END_YEAR_REGULAR).iterator();
                while (itr.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="INCREMENT ARREAR PART">
                    MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(mvo.getMonth() + "\"" + mvo.getYear());

                    StringBuilder sbincr = new StringBuilder();

                    sbincr.append("(select seet.earningmasterid as payid, sum(seet.amount) as amount, 'EARNINGS' as stype from supplementaryemployeeearningstransactions seet ");
                    sbincr.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=seet.supplementarypayrollprocessingdetailsid ");
                    sbincr.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbincr.append("where ");
                    sbincr.append("seet.cancelled is false ");
                    sbincr.append("and seet.accregion='" + LoggedInRegion + "' ");
//                    sbincr.append("and seet.earningmasterid in ('E01', 'E02', 'E03', 'E04', 'E06', 'E07', 'E25') ");
                    sbincr.append("and sppd.cancelled is false ");
                    sbincr.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbincr.append("and date_part('month', spb.date)=" + mvo.getMonth() + " ");
                    sbincr.append("and date_part('year', spb.date)=" + mvo.getYear() + " ");
                    sbincr.append("and spb.accregion='" + LoggedInRegion + "' ");
                    sbincr.append("and spb.cancelled is false ");
                    sbincr.append("and spb.type = 'INCREMENTARREAR' ");
//                    sbincr.append("and spb.type in ('INCREMENTMANUAL', 'SLINCREMENTMANUAL', 'INCREMENTARREAR') ");
                    sbincr.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
                    sbincr.append("group by payid,stype) ");
                    sbincr.append("union ");
                    sbincr.append("(select sedt.deductionmasterid as payid, sum(sedt.amount) as amount, 'DEDUCTION' as stype from supplementaryemployeedeductionstransactions sedt ");
                    sbincr.append("left join supplementarypayrollprocessingdetails  sppd on sppd.id=sedt.supplementarypayrollprocessingdetailsid ");
                    sbincr.append("left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid ");
                    sbincr.append("where ");
                    sbincr.append("sedt.cancelled is false ");
                    sbincr.append("and sedt.accregion='" + LoggedInRegion + "' ");
                    sbincr.append("and sedt.deductionmasterid in ('D12', 'D02', 'D03', 'L31') ");
                    sbincr.append("and sppd.cancelled is false ");
                    sbincr.append("and sppd.accregion='" + LoggedInRegion + "' ");
                    sbincr.append("and date_part('month', spb.date)=" + mvo.getMonth() + " ");
                    sbincr.append("and date_part('year', spb.date)=" + mvo.getYear() + " ");
                    sbincr.append("and spb.accregion='" + LoggedInRegion + "'");
                    sbincr.append("and spb.cancelled is false ");
                    sbincr.append("and spb.type = 'INCREMENTARREAR' ");
//                    sbincr.append("and spb.type in ('INCREMENTMANUAL', 'SLINCREMENTMANUAL', 'INCREMENTARREAR') ");
                    sbincr.append("and spb.employeeprovidentfundnumber='" + epfno + "' ");
                    sbincr.append("group by payid,stype) ");


//                    System.out.println("::::::::::::: DAARREAR Query = " + sbincr.toString());

                    SQLQuery IncrementareearQuery = session.createSQLQuery(sbincr.toString());

                    if (IncrementareearQuery.list().size() > 0) {
                        Map<String, Double> incremap = new HashMap<String, Double>();

                        psedm = new PaySlip_Earn_Deduction_Model();
                        double total = 0;
                        double otherstotal = 0;
                        for (ListIterator it1 = IncrementareearQuery.list().listIterator(); it1.hasNext();) {
                            Object[] row1 = (Object[]) it1.next();
                            String id = (String) row1[0];
                            BigDecimal amo = (BigDecimal) row1[1];
                            double amount = amo.doubleValue();
                            String type = (String) row1[2];
                            incremap.put(id, amount);
                            if (!(id.equals("E01") || id.equals("E02") || id.equals("E03") || id.equals("E04") || id.equals("E06") || id.equals("E07") || id.equals("E25")) && (type.equalsIgnoreCase("EARNINGS"))) {
                                otherstotal += amount;
                            }
                            if (type.equalsIgnoreCase("EARNINGS")) {
                                total += amount;
                            }
                        }
                        if (otherstotal > 0) {
                            incremap.put("OTHERS", otherstotal);
                        }

                        psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));

                        //Gross
                        if (total == 0) {
                            psedm.setGross("");
                        } else {
                            psedm.setGross(decimalFormat.format(total));
                            grant_gross_total += total;
                        }

                        //Basic
                        if (incremap.get("E01") != null) {
                            psedm.setBasic(decimalFormat.format(incremap.get("E01")));
                            grant_basic_total += incremap.get("E01");
                        } else {
                            psedm.setBasic("");
                        }
                        //Special Pay
                        if (incremap.get("E02") != null) {
                            psedm.setSpay(decimalFormat.format(incremap.get("E02")));
                            grant_spay_total += incremap.get("E02");
                        } else {
                            psedm.setSpay("");
                        }
                        //Per Pay
                        if (incremap.get("E03") != null) {
                            psedm.setPp(decimalFormat.format(incremap.get("E03")));
                            grant_pp_total += incremap.get("E03");
                        } else {
                            psedm.setPp("");
                        }
                        //DA
                        if (incremap.get("E04") != null) {
                            psedm.setDa(decimalFormat.format(incremap.get("E04")));
                            grant_da_total += incremap.get("E04");
                        } else {
                            psedm.setDa("");
                        }
                        //Grade Pay
                        if (incremap.get("E25") != null) {
                            psedm.setGrpay(decimalFormat.format(incremap.get("E25")));
                            grant_grpay_total += incremap.get("E25");
                        } else {
                            psedm.setGrpay("");
                        }
                        //HRA
                        if (incremap.get("E06") != null) {
                            psedm.setHra(decimalFormat.format(incremap.get("E06")));
                            grant_hra_total += incremap.get("E06");
                        } else {
                            psedm.setHra("");
                        }
                        //CCA
                        if (incremap.get("E07") != null) {
                            psedm.setCca(decimalFormat.format(incremap.get("E07")));
                            grant_cca_total += incremap.get("E07");
                        } else {
                            psedm.setCca("");
                        }
                        //Others
//                        psedm.setOthers("");
                        if (incremap.get("OTHERS") != null) {
                            psedm.setOthers(decimalFormat.format(incremap.get("OTHERS")));
                            others_total += incremap.get("OTHERS");
                        } else {
                            psedm.setOthers("");
                        }

                        //EPF
                        if (incremap.get("D02") != null) {
                            psedm.setEpf(decimalFormat.format(incremap.get("D02")));
                            grant_epf_total += incremap.get("D02");
                        } else {
                            psedm.setEpf("");
                        }//VPF
                        if (incremap.get("D03") != null) {
                            psedm.setVpf(decimalFormat.format(incremap.get("D03")));
                            grant_vpf_total += incremap.get("D03");
                        } else {
                            psedm.setVpf("");
                        }//INCOME TAX
                        if (incremap.get("D12") != null) {
                            psedm.setItax(decimalFormat.format(incremap.get("D12")));
                            grant_it_total += incremap.get("D12");
                        } else {
                            psedm.setItax("");
                        }//PROFESSIONAL TAX
                        if (incremap.get("L31") != null) {
                            psedm.setProftax(decimalFormat.format(incremap.get("L31")));
                            grant_proftax_total += incremap.get("L31");
                        } else {
                            psedm.setProftax("");
                        }
                        incrementlist.add(psedm);
                    }
                    // </editor-fold>
                }

                itr = CommonUtility.getMonthandYearBetween(START_MONTH, START_YEAR, END_MONTH_REGULAR, END_YEAR_REGULAR).iterator();
                while (itr.hasNext()) {
                    // <editor-fold defaultstate="collapsed" desc="BONUS PART">
                    MonthValueObject mvo = (MonthValueObject) itr.next();
//                    System.out.println(mvo.getMonth() + "\"" + mvo.getYear());

                    StringBuilder sbbonus = new StringBuilder();

                    sbbonus.append("select earningsamount,deductionamount,bonustype from bonusdetails ");
                    sbbonus.append("where ");
                    sbbonus.append("region = '" + LoggedInRegion + "' ");
                    sbbonus.append("and year=" + mvo.getYear() + " ");
                    sbbonus.append("and month=" + mvo.getMonth() + " ");
                    sbbonus.append("and epfno='" + epfno + "'");

//                    System.out.println("DAARREAR Query = " + sbincr.toString());

                    SQLQuery BonusQuery = session.createSQLQuery(sbbonus.toString());

                    if (BonusQuery.list().size() > 0) {
                        Map<String, Double> incremap = new HashMap<String, Double>();

                        psedm = new PaySlip_Earn_Deduction_Model();
                        String bonustype = "";
                        double total = 0;
                        double bonusearningsamount = 0;
                        double bonusdeductionamount = 0;

                        for (ListIterator it1 = BonusQuery.list().listIterator(); it1.hasNext();) {
                            Object[] row1 = (Object[]) it1.next();
                            if (row1[0] != null) {
                                BigDecimal earamo = (BigDecimal) row1[0];
                                bonusearningsamount = earamo.doubleValue();
                                total += bonusearningsamount;

                            }
                            if (row1[1] != null) {
                                BigDecimal dedamo = (BigDecimal) row1[1];
                                bonusdeductionamount = dedamo.doubleValue();
                                grant_epf_total += bonusdeductionamount;
                            }
                            if (row1[2] != null) {
                                bonustype = (String) row1[2];
                                psedm.setPaycodename(bonustype);
                            }
                        }

                        psedm.setPeriod(months[mvo.getMonth() - 1] + "\"" + String.valueOf(mvo.getYear()).substring(2, 4));

                        //Gross
                        if (total == 0) {
                            psedm.setGross("");
                        } else {
                            psedm.setGross(decimalFormat.format(bonusearningsamount));
                            grant_gross_total += total;
                        }

                        if (bonusdeductionamount == 0) {
                            psedm.setEpf("");
                        } else {
                            psedm.setEpf(decimalFormat.format(bonusdeductionamount));
                        }
                        //Others
                        psedm.setOthers(decimalFormat.format(bonusearningsamount));
                        grant_others_total += bonusearningsamount;
                        bonuslist.add(psedm);
                    }
                    // </editor-fold>
                }

                grant_basic_total += basic_total;
                grant_spay_total += spay_total;
                grant_pp_total += pp_total;
                grant_da_total += da_total;
                grant_grpay_total += grpay_total;
                grant_hra_total += hra_total;
                grant_cca_total += cca_total;
                grant_others_total += others_total;
                grant_gross_total += gross_total;
                grant_it_total += it_total;
                grant_epf_total += epf_total;
                grant_vpf_total += vpf_total;
                grant_proftax_total += proftax_total;
                grant_his_total += his_total;
                grant_fbf_total += fbf_total;

                Map<String, String> totalMap = new HashMap<String, String>();
                totalMap.put("grant_basic_total", decimalFormat.format(grant_basic_total));
                totalMap.put("grant_spay_total", decimalFormat.format(grant_spay_total));
                totalMap.put("grant_pp_total", decimalFormat.format(grant_pp_total));
                totalMap.put("grant_da_total", decimalFormat.format(grant_da_total));
                totalMap.put("grant_grpay_total", decimalFormat.format(grant_grpay_total));
                totalMap.put("grant_hra_total", decimalFormat.format(grant_hra_total));
                totalMap.put("grant_cca_total", decimalFormat.format(grant_cca_total));
                totalMap.put("grant_others_total", decimalFormat.format(grant_others_total));
                totalMap.put("grant_gross_total", decimalFormat.format(grant_gross_total));
                totalMap.put("grant_it_total", decimalFormat.format(grant_it_total));
                totalMap.put("grant_epf_total", decimalFormat.format(grant_epf_total));
                totalMap.put("grant_vpf_total", decimalFormat.format(grant_vpf_total));
                totalMap.put("grant_proftax_total", decimalFormat.format(grant_proftax_total));
                totalMap.put("grant_his_total", decimalFormat.format(grant_his_total));
                totalMap.put("grant_fbf_total", decimalFormat.format(grant_fbf_total));

                totalMap.put("basic_total", decimalFormat.format(basic_total));
                totalMap.put("spay_total", decimalFormat.format(spay_total));
                totalMap.put("pp_total", decimalFormat.format(pp_total));
                totalMap.put("da_total", decimalFormat.format(da_total));
                totalMap.put("grpay_total", decimalFormat.format(grpay_total));
                totalMap.put("hra_total", decimalFormat.format(hra_total));
                totalMap.put("cca_total", decimalFormat.format(cca_total));
                totalMap.put("others_total", decimalFormat.format(others_total));
                totalMap.put("gross_total", decimalFormat.format(gross_total));
                totalMap.put("it_total", decimalFormat.format(it_total));
                totalMap.put("epf_total", decimalFormat.format(epf_total));
                totalMap.put("vpf_total", decimalFormat.format(vpf_total));
                totalMap.put("proftax_total", decimalFormat.format(proftax_total));
                totalMap.put("his_total", decimalFormat.format(his_total));
                totalMap.put("fbf_total", decimalFormat.format(fbf_total));

//                totalMap.put("da_basic_total", decimalFormat.format(da_basic_total));
//                totalMap.put("da_spay_total", decimalFormat.format(da_spay_total));
//                totalMap.put("da_pp_total", decimalFormat.format(da_pp_total));
//                totalMap.put("da_da_total", decimalFormat.format(da_da_total));
//                totalMap.put("da_grpay_total", decimalFormat.format(da_grpay_total));
//                totalMap.put("da_hra_total", decimalFormat.format(da_hra_total));
//                totalMap.put("da_cca_total", decimalFormat.format(da_cca_total));
//                totalMap.put("da_others_total", decimalFormat.format(da_others_total));
//                totalMap.put("da_gross_total", decimalFormat.format(da_gross_total));
//                totalMap.put("da_it_total", decimalFormat.format(da_it_total));
//                totalMap.put("da_epf_total", decimalFormat.format(da_epf_total));
//                totalMap.put("da_vpf_total", decimalFormat.format(da_vpf_total));
//                totalMap.put("da_proftax_total", decimalFormat.format(da_proftax_total));

                psm.setRegularlist(regularlist);
//                psm.setSupplementarylist(suplist);
                psm.setIncrementarrearlist(incrementlist);
                psm.setLeavesurrenderlist(surrenderlist);
                psm.setDaarrearlist(daarrearlist);
                psm.setBonuslist(bonuslist);

                psm.setTotalmap(totalMap);

//                System.out.println("psm.getRegularlist().size() = " + psm.getRegularlist().size());
//                System.out.println("psm.getSupplementarylist.size() = " + psm.getSupplementarylist().size());
//                System.out.println("psm.getIncrementarrearlist().size() = " + psm.getIncrementarrearlist().size());
//                System.out.println("psm.getLeavesurrenderlist().size() = " + psm.getLeavesurrenderlist().size());
//                System.out.println("psm.getDaarrearlist().size() = " + psm.getDaarrearlist().size());

                if (reporttype.equals("1")) {
                    taxReport.getIncomeTaxPrintWriter(psm, filePathwithName);
                }
                if (reporttype.equals("2")) {
                    excelReport.GenerateIncomeTaxExportXML(psm, filePathwithName);
                }
                if (reporttype.equals("3")) {
                    excelReport.GenerateIncomeTaxAbstractExportXML(psm, filePathwithName);
                }

                // </editor-fold>
            }

        } catch (Exception ex) {
            map.put("ERROR", "Deduction All Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    @Override
    public Map IncomeTaxParticularsPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startyear, String epfn, String reporttype, String filePathwithName, String sectionid) {
        Map map = new HashMap();
        try {
            System.out.println("********************* IncomeTaxServiceImpl class IncomeTaxParticularsPrintOut method is calling *****************");
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            IncomeTaxReport taxReport = new IncomeTaxReport();
            //IncomeTaxExcelReport excelReport = new IncomeTaxExcelReport();
            //int START_MONTH = 0;
            //int START_YEAR = Integer.valueOf(startyear);
            HashMap finalMap = new HashMap();
            String monthcond = "";
            int syear = 0;

            if ("1".equalsIgnoreCase(reporttype)) {
                syear = Integer.parseInt(startyear);
                monthcond = " between 4 and 6 ";
            } else if ("2".equalsIgnoreCase(reporttype)) {
                syear = Integer.parseInt(startyear);
                monthcond = " between 7 and 9 ";
            } else if ("3".equalsIgnoreCase(reporttype)) {
                syear = Integer.parseInt(startyear);
                monthcond = " between 10 and 12 ";
            } else if ("4".equalsIgnoreCase(reporttype)) {
                syear = Integer.parseInt(startyear) + 1;
                monthcond = " between 1 and 3 ";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT epfno, employeename, rm.regionname, sn.sectionname, dm.designation, process, em.pancardno FROM employeemaster em ");
            sb.append("left join regionmaster rm on em.region=rm.id ");
            sb.append("left join sectionmaster sn on sn.id=em.section ");
            sb.append("left join designationmaster dm on em.designation=dm.designationcode ");
            sb.append("where ");
            sb.append("sn.id='" + sectionid + "' ");
            sb.append("and em.region='" + LoggedInRegion + "' ");
            //if ((epfn != "") || (epfn.length() > 0)) {
            if  (epfn.length() > 0) {
                sb.append("and epfno='" + epfn + "' ");
            }
            sb.append("order by rm.id, sn.id, cast(dm.orderno as integer)");
            PaySlipModel psm = null;
            SQLQuery employeequery = session.createSQLQuery(sb.toString());
            int slipno = 1;
            for (ListIterator its = employeequery.list().listIterator(); its.hasNext();) {
                // <editor-fold desc="Start Employee Query .">
                psm = new PaySlipModel();
                Object[] rows = (Object[]) its.next();
                String epfno = (String) rows[0];
                String employeename = (String) rows[1];
                String region = (String) rows[2];
                String sectionname = (String) rows[3];
                String designation = (String) rows[4];
                String pancardno = (String) rows[6];

                psm.setSlipno(String.valueOf(slipno));
                psm.setPfno(epfno);
                psm.setEmployeename(employeename);
                psm.setBranch(region);
                psm.setSectionname(sectionname);
                psm.setDesignation(designation);
                psm.setPayslipstartingdate(startyear);
                psm.setPancardno(pancardno);

                StringBuilder inQuery = new StringBuilder();
                inQuery.append(" (select ('deduction'||row_number() over()) as ded, year,month,sum(ded.amount) as dedamt ");
                inQuery.append(" from payrollprocessingdetails as ppd ");
                inQuery.append(" left join employeedeductionstransactions as ded on ded.payrollprocessingdetailsid=ppd.id  and ded.deductionmasterid='D12' ");
                inQuery.append(" left join paycodemaster as pm1 on ded.deductionmasterid = pm1.paycode  ");
                inQuery.append(" where employeeprovidentfundnumber ='" + epfno + "' ");
                inQuery.append(" and year =" + syear + " and month " + monthcond + "  and ded.deductionmasterid='D12' ");
                inQuery.append(" and process=true group by month,year order by month)union ( ");
                inQuery.append(" select ('earning'||row_number() over()) as ern, year,month,sum(edt.amount) as earamt ");
                inQuery.append(" from payrollprocessingdetails as ppd ");
                inQuery.append(" left join employeeearningstransactions as edt on edt.payrollprocessingdetailsid=ppd.id  ");
                inQuery.append(" left join paycodemaster as pm on edt.earningmasterid = pm.paycode  ");
                inQuery.append(" where employeeprovidentfundnumber ='" + epfno + "' ");
                inQuery.append(" and year =" + syear + " and month " + monthcond + " and process=true  ");
                inQuery.append(" group by month,year order by month) ");

                SQLQuery earn_dedn_query = session.createSQLQuery(inQuery.toString());
                Map earn_ded_Map = new HashMap();
                String key = "";
                for (ListIterator its1 = earn_dedn_query.list().listIterator(); its1.hasNext();) {
                    Object[] rows1 = (Object[]) its1.next();
                    BigDecimal amo = (BigDecimal) rows1[3];
                    double amount = 0;
                    if (amo != null) {
                        amount = amo.doubleValue();
                    }
                    String edtext = (String) rows1[0];
                    //String year = (String) rows1[1];
                    int month =(Integer)rows1[2];
                    PaySlip_Earn_Deduction_Model pedm = new PaySlip_Earn_Deduction_Model();
                    if ("earning".equalsIgnoreCase(edtext.substring(0, 7))){
                        pedm.setEarningsname(edtext);
                        pedm.setEarningsamount(amount+"");
                        earn_ded_Map.put("earning1",pedm);
                    } else if ("deduction".equalsIgnoreCase(edtext.substring(0, 9))){
                        pedm.setDeductionname(edtext);
                        pedm.setDeductionamount(amount + "");
                    }
                    earn_ded_Map.put(edtext,pedm);
                }
                psm.setDeduction_map(earn_ded_Map);
                finalMap.put(slipno, psm);
                slipno++;
                // </editor-fold>
            }            
            taxReport.getITPrintWriter(finalMap, filePathwithName,reporttype);
        } catch (Exception ex) {
            map.put("ERROR", "I.T. Report Generated Error");
            ex.printStackTrace();
        } finally {
        }
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadSectionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map sectionMap = new LinkedHashMap();
        sectionMap.put("0", "--Select--");
        try {
            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction(" region  in ('ALL','" + LoggedInRegion + "' )"));
            secCrit.addOrder(Order.asc("sectionname"));
            List<Sectionmaster> secList = secCrit.list();
            resultMap = new TreeMap();
            for (Sectionmaster lbobj : secList) {
                sectionMap.put(lbobj.getId(), lbobj.getSectionname());
            }
            resultMap.put("sectionlist", sectionMap);
            resultMap.put("currentRegion", LoggedInRegion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
