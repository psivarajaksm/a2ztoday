/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.common.DateUtility;
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
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class DAIncrementArrearServiceImpl extends OnwardAction implements DAIncrementArrearService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Employeemaster employeemasterObj;
        Criteria empCrit = session.createCriteria(Employeemaster.class);
        empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
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

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveDAIncrementProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String asondate, String dapercentage, String serialno, String epfno, String batchno) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Employeemaster employeemasterObje = null;
        Daarrear daarrearObj = null;

        Criteria daCrit = session.createCriteria(Daarrear.class);
        daCrit.add(Restrictions.sqlRestriction("active is true and accregion='" + LoggedInRegion + "' "));
        List daList = daCrit.list();
        if (daList.size() > 0) {
            daarrearObj = (Daarrear) daList.get(0);
        }

        if (batchno.trim().length() <= 0) {
            batchno = getMaxSeqNumberDABatchId(session, LoggedInRegion);
            Dabatchdetails dabatchdetailsObj = new Dabatchdetails();
            dabatchdetailsObj.setId(batchno);
            dabatchdetailsObj.setDaarrear(daarrearObj);
            dabatchdetailsObj.setCancelled(Boolean.FALSE);
            dabatchdetailsObj.setActive(Boolean.TRUE);
            dabatchdetailsObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
            transaction = session.beginTransaction();
            session.save(dabatchdetailsObj);
            transaction.commit();
        }

        int serialNumber = Integer.parseInt(serialno) + 1;

        Calendar cal;
        long totalMonthsProcess = 0L;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(fromdate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        SimpleDateFormat tm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tm.parse(todate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        SimpleDateFormat cm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cm.parse(asondate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        cal = fm.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH) + 1;
        cal = tm.getCalendar();
        int edyear = cal.get(Calendar.YEAR);
        int edmon = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        cal = cm.getCalendar();
        totalMonthsProcess = DateUtility.monthsBetweenDates(fromdate, todate);


        long[] subtotal = {0, 0, 0, 0, 0};
        long tots = 0;
        long totd = 0;
        String[] earcodes = {"E01", "E03", "E04", "E25"};

        try {
            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
            empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List empList = empCrit.list();
            if (empList.size() > 0) {
                resultMap.put("proceed", "yes");
                resultMap.put("batchno", batchno);
                resultMap.put("serialno", serialNumber);
                employeemasterObje = (Employeemaster) empList.get(0);
                Supplementatypaybill supplementatypaybillObj;
                Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj;
                Criteria supPayrollCrit = session.createCriteria(Supplementatypaybill.class);
                supPayrollCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                supPayrollCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                supPayrollCrit.add(Restrictions.sqlRestriction("type='DAARREAR'"));
                supPayrollCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
                List supPayrollList = supPayrollCrit.list();
                if (supPayrollList.size() > 0) {
                    supplementatypaybillObj = (Supplementatypaybill) supPayrollList.get(0);

                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setDate(postgresDate(asondate));
                    supplementatypaybillObj.setEmployeemaster(employeemasterObje);
                    supplementatypaybillObj.setType("DAARREAR");
                    supplementatypaybillObj.setSection(employeemasterObje.getSection());
                    supplementatypaybillObj.setSubsection(employeemasterObje.getSubsection());
                    supplementatypaybillObj.setPaymentmode(employeemasterObje.getPaymentmode());
                    supplementatypaybillObj.setEmployeecategory(employeemasterObje.getCategory());
                    supplementatypaybillObj.setDesignation(employeemasterObje.getDesignation());
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setCancelled(Boolean.FALSE);
                    supplementatypaybillObj.setDabatchdetails(CommonUtility.getDabatchdetails(session, batchno));
                    supplementatypaybillObj.setCreatedby(LoggedInUser);
                    supplementatypaybillObj.setCreateddate(getCurrentDate());

                } else {
                    supplementatypaybillObj = new Supplementatypaybill();
                    String supPayBillId = getSupplementatypaybillid(session, LoggedInRegion);
                    supplementatypaybillObj.setId(supPayBillId);
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setDate(postgresDate(asondate));
                    supplementatypaybillObj.setEmployeemaster(employeemasterObje);
                    supplementatypaybillObj.setType("DAARREAR");
                    supplementatypaybillObj.setSection(employeemasterObje.getSection());
                    supplementatypaybillObj.setSubsection(employeemasterObje.getSubsection());
                    supplementatypaybillObj.setPaymentmode(employeemasterObje.getPaymentmode());
                    supplementatypaybillObj.setEmployeecategory(employeemasterObje.getCategory());
                    supplementatypaybillObj.setDesignation(employeemasterObje.getDesignation());
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setCancelled(Boolean.FALSE);
                    supplementatypaybillObj.setDabatchdetails(CommonUtility.getDabatchdetails(session, batchno));
                    supplementatypaybillObj.setCreatedby(LoggedInUser);
                    supplementatypaybillObj.setCreateddate(getCurrentDate());

                    transaction = session.beginTransaction();
                    session.save(supplementatypaybillObj);
                    transaction.commit();
                }


//                transaction = session.beginTransaction();
//                session.createSQLQuery("UPDATE supplementarypayrollprocessingdetails  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "'  and supplementatypaybillid='" + supplementatypaybillObj.getId() + "'").executeUpdate();
//                transaction.commit();
//
//                transaction = session.beginTransaction();
//                session.createSQLQuery("UPDATE supplementaryemployeeearningsdetails  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and supplementarysalarystructureid in (select sss.id from supplementaryemployeeearningsdetails seed left join supplementarysalarystructure sss on sss.id=supplementarysalarystructureid left join supplementarypayrollprocessingdetails spp on spp.id=sss.supplementarypayrollprocessingdetailsid  left join supplementatypaybill spb on spb.id=spp.supplementatypaybillid  where spb.accregion='" + LoggedInRegion + "'  and spb.date='" + postgresDate(asondate) + "' and spb.type='DAARREAR' and spb.employeeprovidentfundnumber='" + epfno + "' )").executeUpdate();
//                transaction.commit();
//
//                transaction = session.beginTransaction();
//                session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and supplementarypayrollprocessingdetailsid in (select spp.id from supplementaryemployeeearningstransactions seet left join supplementarypayrollprocessingdetails spp on spp.id=seet.supplementarypayrollprocessingdetailsid left join supplementatypaybill spb on spb.id=spp.supplementatypaybillid where spb.accregion='" + LoggedInRegion + "'  and spb.date='" + postgresDate(asondate) + "' and spb.type='DAARREAR' and spb.employeeprovidentfundnumber='" + epfno + "') ").executeUpdate();
//                transaction.commit();



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
                        //supplementarypayrollprocessingdetailsObj.setNooddayscalculated(30);
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
                        supplementarysalarystructureObj.setSupplementarypayrollprocessingdetailsid(supplementarypayrollprocessingdetailsObj.getId());
                        supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                        supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                        supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(supplementarysalarystructureObj);
                        transaction.commit();
                    }
                    // Drawn Calculation
                    for (int p = 0; p < subtotal.length; p++) {
                        subtotal[p] = 0;
                    }

                    float epftotal = 0;
                    float datotal = 0;
                    //Regular
                    System.out.println("epfno " + epfno + " month" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " year " + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                    Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                    List empPayProcessDetails = empPayProcessDetailsCrit.list();
                    if (empPayProcessDetails.size() > 0) {
                        Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(0);
                        tots = 0;
                        totd = 0;
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
                                    if (!earcodes[k].equalsIgnoreCase("E04")) {
                                        subtotal[k] = subtotal[k] + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                        tots = tots + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                        System.out.println("Other than Da regular");
                                    }
                                    if (earcodes[k].equalsIgnoreCase("E04")) {
                                        System.out.println("Da regular" + subtotal[k]);
                                        totd = totd + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                    }
                                    System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                }
                            }
                        }
                        float sDa = (tots * Float.parseFloat(dapercentage) / 100);
                        BigDecimal x1 = new BigDecimal(sDa);
                        x1=x1.setScale(0, RoundingMode.HALF_UP);
                        float sNewDa = x1.floatValue() - totd;
                        System.out.println("Due" + sDa + "   Drawn   " + totd + "  Arrear     " + sNewDa);
//                        System.out.println("Drawn" + totd);
//                        System.out.println("Arrear" + sNewDa);
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
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa).setScale(0, RoundingMode.HALF_UP));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa).setScale(0, RoundingMode.HALF_UP));
                            if (isCalculateEpf(session, epfno)) {
                                incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                            }
                            BigDecimal x = new BigDecimal(sNewDa);
                            x=x.setScale(0, RoundingMode.HALF_UP);
                            datotal = datotal + x.floatValue();
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(Math.round(totd)));
                            incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                            incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                            incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                            transaction = session.beginTransaction();
                            session.update(incrementarrearreferenceObj);
                            transaction.commit();

                        } else {
                            Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                            SupplementaryBillServiceImpl supplementaryBillServiceImplObj = new SupplementaryBillServiceImpl();
                            incrementarrearreferenceObj.setId(supplementaryBillServiceImplObj.getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                            incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                            incrementarrearreferenceObj.setProcessid(payrollprocessingdetailsObj.getId());
                            incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                            incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                            incrementarrearreferenceObj.setType("REGULAR");
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(totd).setScale(0, RoundingMode.HALF_UP));
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa).setScale(0, RoundingMode.HALF_UP));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa).setScale(0, RoundingMode.HALF_UP));
                            if (isCalculateEpf(session, epfno)) {
                                incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                            }
                            BigDecimal x = new BigDecimal(sNewDa);
                            x=x.setScale(0, RoundingMode.HALF_UP);
                            datotal = datotal + x.floatValue();
                            incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                            incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                            incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                            transaction = session.beginTransaction();
                            session.save(incrementarrearreferenceObj);
                            transaction.commit();
                        }
                        //Incrementarrearreference

                    }
                    //Regular end
                    //Supplementaty
                    StringBuffer query = new StringBuffer();
                    query.append("( select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
//                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and (spb.type='SUPLEMENTARYBILL' or spb.type='INCREMENTARREAR' ))");
                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and spb.type='SUPLEMENTARYBILL' )");
                    query.append(" union ");
                    query.append("( select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
//                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  EXTRACT(YEAR FROM spb.date) =" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and EXTRACT(MONTH FROM spb.date) =" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and (spb.type='INCREMENTARREAR' or spb.type='LEAVESURRENDER' or spb.type='DAARREAR'  )) ");
                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth = " + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and (spb.type='INCREMENTARREAR'  ))");

                    query.append(" union ");
                    query.append("( select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  EXTRACT(YEAR FROM spb.date) =" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and EXTRACT(MONTH FROM spb.date) =" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and  spb.type='DAARREAR'  )");
                    query.append(" union ");
                    query.append("( select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  EXTRACT(YEAR FROM spb.sldate) =" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and EXTRACT(MONTH FROM spb.sldate) =" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and spb.type='LEAVESURRENDER' )");

//                    System.out.println("query====" + query);
                    SQLQuery subpayquery = session.createSQLQuery(query.toString());
                    String type = "";
                    for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                        Object[] rows = (Object[]) its.next();
                        String payprocesid = (String) rows[0];
                        type = (String) rows[1];


                        tots = 0;
                        totd = 0;
                        for (int k = 0; k < earcodes.length; k++) {
                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false "));
                            List empEarnDetailsList = empEarnDetailsCrit.list();
                            if (empEarnDetailsList.size() > 0) {

                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                if (!earcodes[k].equalsIgnoreCase("E04")) {
                                    System.out.println("Other than Da sub");
                                    subtotal[k] = subtotal[k] + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                    tots = tots + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                }
                                if (earcodes[k].equalsIgnoreCase("E04")) {
                                    System.out.println("Da sub" + subtotal[k]);

                                    totd = totd + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                }


                            }


                        }

                        float sDa = (tots * Float.parseFloat(dapercentage) / 100);
                        BigDecimal x1 = new BigDecimal(sDa);
                        x1=x1.setScale(0, RoundingMode.HALF_UP);
                        float sNewDa = x1.floatValue() - totd;

                        System.out.println("Due" + sDa + "   Drawn   " + totd + "  Arrear     " + sNewDa);
//                        System.out.println("Due" + sDa);
//                        System.out.println("Drawn" + totd);
//                        System.out.println("Arrear" + sNewDa);

                        //Incrementarrearreference
                        Criteria empIncRefCrit = session.createCriteria(Incrementarrearreference.class);
                        empIncRefCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empIncRefCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supplementatypaybillObj.getId() + "'"));
                        //empIncRefCrit.add(Restrictions.sqlRestriction("type='" + "SUPPLEMENTARY" + "'"));
                        empIncRefCrit.add(Restrictions.sqlRestriction("processid='" + payprocesid + "'"));
                        List empIncRefList = empIncRefCrit.list();
                        if (empIncRefList.size() > 0) {
                            Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) empIncRefList.get(0);
                            incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                            incrementarrearreferenceObj.setProcessid(payprocesid);
                            incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                            incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                            incrementarrearreferenceObj.setType(type);
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(totd).setScale(0, RoundingMode.HALF_UP));
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa).setScale(0, RoundingMode.HALF_UP));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa).setScale(0, RoundingMode.HALF_UP));
                            if (!type.equalsIgnoreCase("LEAVESURRENDER")) {
                                if (isCalculateEpf(session, epfno)) {
                                    incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                    epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                                }
                            } else {
                                incrementarrearreferenceObj.setEpf(BigDecimal.ZERO);
                            }
                            BigDecimal x = new BigDecimal(sNewDa);
                            x=x.setScale(0, RoundingMode.HALF_UP);
                            datotal = datotal + x.floatValue();
                            incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                            incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                            incrementarrearreferenceObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(incrementarrearreferenceObj);
                            transaction.commit();
                        } else {
                            Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                            SupplementaryBillServiceImpl supplementaryBillServiceImplObj = new SupplementaryBillServiceImpl();
                            incrementarrearreferenceObj.setId(supplementaryBillServiceImplObj.getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                            incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                            incrementarrearreferenceObj.setProcessid(payprocesid);
                            incrementarrearreferenceObj.setType(type);
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(totd).setScale(0, RoundingMode.HALF_UP));
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa).setScale(0, RoundingMode.HALF_UP));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa).setScale(0, RoundingMode.HALF_UP));
                            if (!type.equalsIgnoreCase("LEAVESURRENDER")) {
                                if (isCalculateEpf(session, epfno)) {
                                    incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                    epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                                }
                            } else {
                                incrementarrearreferenceObj.setEpf(BigDecimal.ZERO);
                            }
                            BigDecimal x = new BigDecimal(sNewDa);
                            x=x.setScale(0, RoundingMode.HALF_UP);
                            datotal = datotal + x.floatValue();
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

                    }


                    //Supplementaty end
                    float totalDADrawn = 0.00f;
                    float otherEarningTotal = 0.0f;
                    for (int k = 0; k < earcodes.length; k++) {
                        if (earcodes[k].equalsIgnoreCase("E04")) {
                            totalDADrawn = subtotal[k];
                        } else {
                            otherEarningTotal = otherEarningTotal + subtotal[k];
                            System.out.println("other earing " + subtotal[k]);
                        }

                    }
                    float totalDa = (otherEarningTotal * Float.parseFloat(dapercentage) / 100);
                    float newDa = totalDa - totalDADrawn;

                    System.out.println("totalDa=====" + totalDa);
                    //Drawn Calculation ends
                    Supplementaryemployeeearningsdetails earningsdetObj;
                    Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                    earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                    earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + "E04" + "' "));
                    List earList = earDe.list();
                    if (earList.size() > 0) {
                        earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                        earningsdetObj.setCancelled(Boolean.FALSE);
                        earningsdetObj.setAmount(new BigDecimal(totalDa));

                        transaction = session.beginTransaction();
                        session.update(earningsdetObj);
                        transaction.commit();

                    } else {

                        earningsdetObj = new Supplementaryemployeeearningsdetails();

                        earningsdetObj.setCancelled(Boolean.FALSE);
                        earningsdetObj.setAmount(new BigDecimal(totalDa));
                        earningsdetObj.setEarningmasterid("E04");
                        earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId));
                        earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                        earningsdetObj.setAccregion(LoggedInRegion);
                        transaction = session.beginTransaction();
                        session.save(earningsdetObj);
                        transaction.commit();

                    }

                    System.out.println("datotal=====" + datotal);
                    System.out.println("newDa=====" + newDa);
                    System.out.println("epftotal=====" + epftotal);

                    Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
                    Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + "E04" + "'"));
                    List empEarnDetailsList = empEarnDetailsCrit.list();
                    if (empEarnDetailsList.size() > 0) {
                        employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                        employeeearningstransactionsObj.setAmount(new BigDecimal(Math.round(datotal)));
                        employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                        employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                        employeeearningstransactionsObj.setEarningmasterid("E04");
                        employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                        employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                        employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        session.update(employeeearningstransactionsObj);
                        transaction.commit();
                    } else {
                        employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();

                        employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                        employeeearningstransactionsObj.setAmount(new BigDecimal(Math.round(datotal)));
                        employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                        employeeearningstransactionsObj.setEarningmasterid("E04");
                        String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                        employeeearningstransactionsObj.setId(earningsTransactionId);
                        employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                        employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                        employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        session.save(employeeearningstransactionsObj);
                        transaction.commit();
                    }

                    Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                    empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                    empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                    List empDeductDetailsList = empdeductDetailsCrit.list();
                    if (empDeductDetailsList.size() > 0) {
                        Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
                        employeedeductionstransactionsObj.setAmount(new BigDecimal(Math.round(epftotal)));
                        employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                        employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
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
                        employeedeductionstransactionsObj.setAmount(new BigDecimal(Math.round(epftotal)));
                        employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                        employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                        employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                        employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(employeedeductionstransactionsObj);
                        transaction.commit();

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


            } else {
                resultMap.put("proceed", "no");
                resultMap.put("reason", "Processed Successfully");
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
    public Map saveDAIncrementReProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String asondate, String dapercentage, String serialno, String epfno, String batchno) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Employeemaster employeemasterObje = null;
        Daarrear daarrearObj = null;
        Criteria daCrit = session.createCriteria(Daarrear.class);
        daCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        daCrit.add(Restrictions.sqlRestriction("active is true"));
        List daList = daCrit.list();
        if (daList.size() > 0) {
            daarrearObj = (Daarrear) daList.get(0);
        }

        if (batchno.trim().length() <= 0) {
            batchno = getMaxSeqNumberDABatchId(session, LoggedInRegion);
            Dabatchdetails dabatchdetailsObj = new Dabatchdetails();
            dabatchdetailsObj.setId(batchno);
            dabatchdetailsObj.setDaarrear(daarrearObj);
            dabatchdetailsObj.setCancelled(Boolean.FALSE);
            dabatchdetailsObj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
            transaction = session.beginTransaction();
            session.save(dabatchdetailsObj);
            transaction.commit();
        }

        int serialNumber = Integer.parseInt(serialno) + 1;

        Calendar cal;
        long totalMonthsProcess = 0L;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fm.parse(fromdate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        SimpleDateFormat tm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tm.parse(todate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        SimpleDateFormat cm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cm.parse(asondate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeePayBillProcessImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        cal = fm.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH) + 1;
        cal = tm.getCalendar();
        int edyear = cal.get(Calendar.YEAR);
        int edmon = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month
        cal = cm.getCalendar();
        totalMonthsProcess = DateUtility.monthsBetweenDates(fromdate, todate);


        long[] subtotal = {0, 0, 0, 0, 0};
        long tots = 0;
        long totd = 0;
        String[] earcodes = {"E01", "E03", "E04", "E25"};

        try {
            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
            empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List empList = empCrit.list();
            if (empList.size() > 0) {
                resultMap.put("proceed", "yes");
                resultMap.put("batchno", batchno);
                resultMap.put("serialno", serialNumber);
                employeemasterObje = (Employeemaster) empList.get(0);
                Supplementatypaybill supplementatypaybillObj;
                Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj;
                Criteria supPayrollCrit = session.createCriteria(Supplementatypaybill.class);
                supPayrollCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
                supPayrollCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                supPayrollCrit.add(Restrictions.sqlRestriction("type='DAARREAR'"));
                supPayrollCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
                List supPayrollList = supPayrollCrit.list();
                if (supPayrollList.size() > 0) {
                    supplementatypaybillObj = (Supplementatypaybill) supPayrollList.get(0);

                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setDate(postgresDate(asondate));
                    supplementatypaybillObj.setEmployeemaster(employeemasterObje);
                    supplementatypaybillObj.setType("DAARREAR");
                    supplementatypaybillObj.setSection(employeemasterObje.getSection());
                    supplementatypaybillObj.setSubsection(employeemasterObje.getSubsection());
                    supplementatypaybillObj.setPaymentmode(employeemasterObje.getPaymentmode());
                    supplementatypaybillObj.setEmployeecategory(employeemasterObje.getCategory());
                    supplementatypaybillObj.setDesignation(employeemasterObje.getDesignation());
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setCancelled(Boolean.FALSE);
                    supplementatypaybillObj.setDabatchdetails(CommonUtility.getDabatchdetails(session, batchno));
                    supplementatypaybillObj.setCreatedby(LoggedInUser);
                    supplementatypaybillObj.setCreateddate(getCurrentDate());

                } else {
                    supplementatypaybillObj = new Supplementatypaybill();
                    String supPayBillId = getSupplementatypaybillid(session, LoggedInRegion);
                    supplementatypaybillObj.setId(supPayBillId);
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setDate(postgresDate(asondate));
                    supplementatypaybillObj.setEmployeemaster(employeemasterObje);
                    supplementatypaybillObj.setType("DAARREAR");
                    supplementatypaybillObj.setSection(employeemasterObje.getSection());
                    supplementatypaybillObj.setSubsection(employeemasterObje.getSubsection());
                    supplementatypaybillObj.setPaymentmode(employeemasterObje.getPaymentmode());
                    supplementatypaybillObj.setEmployeecategory(employeemasterObje.getCategory());
                    supplementatypaybillObj.setDesignation(employeemasterObje.getDesignation());
                    supplementatypaybillObj.setAccregion(LoggedInRegion);
                    supplementatypaybillObj.setCancelled(Boolean.FALSE);
                    supplementatypaybillObj.setDabatchdetails(CommonUtility.getDabatchdetails(session, batchno));
                    supplementatypaybillObj.setCreatedby(LoggedInUser);
                    supplementatypaybillObj.setCreateddate(getCurrentDate());

                    transaction = session.beginTransaction();
                    session.save(supplementatypaybillObj);
                    transaction.commit();
                }


                transaction = session.beginTransaction();
                session.createSQLQuery("UPDATE supplementarypayrollprocessingdetails  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "'  and supplementatypaybillid='" + supplementatypaybillObj.getId() + "'").executeUpdate();
                transaction.commit();

                transaction = session.beginTransaction();
                session.createSQLQuery("UPDATE supplementaryemployeeearningsdetails  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and supplementarysalarystructureid in (select sss.id from supplementaryemployeeearningsdetails seed left join supplementarysalarystructure sss on sss.id=supplementarysalarystructureid left join supplementarypayrollprocessingdetails spp on spp.id=sss.supplementarypayrollprocessingdetailsid  left join supplementatypaybill spb on spb.id=spp.supplementatypaybillid  where spb.accregion='" + LoggedInRegion + "'  and spb.date='" + postgresDate(asondate) + "' and spb.type='DAARREAR' and spb.employeeprovidentfundnumber='" + epfno + "' )").executeUpdate();
                transaction.commit();

                transaction = session.beginTransaction();
                session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions  SET cancelled  = true WHERE accregion='" + LoggedInRegion + "' and supplementarypayrollprocessingdetailsid in (select spp.id from supplementaryemployeeearningstransactions seet left join supplementarypayrollprocessingdetails spp on spp.id=seet.supplementarypayrollprocessingdetailsid left join supplementatypaybill spb on spb.id=spp.supplementatypaybillid where spb.accregion='" + LoggedInRegion + "'  and spb.date='" + postgresDate(asondate) + "' and spb.type='DAARREAR' and spb.employeeprovidentfundnumber='" + epfno + "') ").executeUpdate();
                transaction.commit();


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
                        //supplementarypayrollprocessingdetailsObj.setNooddayscalculated(30);
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
                        supplementarysalarystructureObj.setSupplementarypayrollprocessingdetailsid(supplementarypayrollprocessingdetailsObj.getId());
                        supplementarysalarystructureObj.setAccregion(LoggedInRegion);
                        supplementarysalarystructureObj.setCreatedby(LoggedInUser);
                        supplementarysalarystructureObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(supplementarysalarystructureObj);
                        transaction.commit();
                    }
                    // Drawn Calculation
                    for (int p = 0; p < subtotal.length; p++) {
                        subtotal[p] = 0;
                    }
                    float epftotal = 0;
                    //Regular
                    System.out.println("epfno " + epfno + " month" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " year " + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                    Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
                    empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                    List empPayProcessDetails = empPayProcessDetailsCrit.list();
                    if (empPayProcessDetails.size() > 0) {
                        Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(0);
                        tots = 0;
                        totd = 0;
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
                                    if (!earcodes[k].equalsIgnoreCase("E04")) {
                                        subtotal[k] = subtotal[k] + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                        tots = tots + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                        System.out.println("Other than Da regular");
                                    }
                                    if (earcodes[k].equalsIgnoreCase("E04")) {
                                        System.out.println("Da regular" + subtotal[k]);
                                        totd = totd + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                    }
                                    System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
                                }
                            }
                        }
                        float sDa = (tots * Float.parseFloat(dapercentage) / 100);
                        float sNewDa = sDa - totd;
                        System.out.println("Due" + sDa);
                        System.out.println("Drawn" + totd);
                        System.out.println("Arrear" + sNewDa);
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
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa));
                            if (isCalculateEpf(session, epfno)) {
                                incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                            }
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(totd));
                            incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                            incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                            incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                            transaction = session.beginTransaction();
                            session.update(incrementarrearreferenceObj);
                            transaction.commit();

                        } else {
                            Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                            SupplementaryBillServiceImpl supplementaryBillServiceImplObj = new SupplementaryBillServiceImpl();
                            incrementarrearreferenceObj.setId(supplementaryBillServiceImplObj.getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                            incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                            incrementarrearreferenceObj.setProcessid(payrollprocessingdetailsObj.getId());
                            incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                            incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                            incrementarrearreferenceObj.setType("REGULAR");
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(totd));
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa));
                            if (isCalculateEpf(session, epfno)) {
                                incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                            }
                            incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                            incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                            incrementarrearreferenceObj.setCreateddate(getCurrentDate());

                            transaction = session.beginTransaction();
                            session.save(incrementarrearreferenceObj);
                            transaction.commit();
                        }
                        //Incrementarrearreference

                    }
                    //Regular end
                    //Supplementaty
                    StringBuffer query = new StringBuffer();
                    query.append("( select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and spb.type='SUPLEMENTARYBILL' )");
                    query.append(" union ");
                    query.append("( select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
                    query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
                    query.append(" where spb.accregion='" + LoggedInRegion + "' and  EXTRACT(YEAR FROM spb.date) =" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and EXTRACT(MONTH FROM spb.date) =" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "' and (spb.type='INCREMENTARREAR' or spb.type='LEAVESURRENDER' or spb.type='DAARREAR'  )) ");


                    SQLQuery subpayquery = session.createSQLQuery(query.toString());

                    for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
                        Object[] rows = (Object[]) its.next();
                        String payprocesid = (String) rows[0];
                        String type = (String) rows[1];


                        tots = 0;
                        totd = 0;
                        for (int k = 0; k < earcodes.length; k++) {
                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
                            empEarnDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
                            List empEarnDetailsList = empEarnDetailsCrit.list();
                            if (empEarnDetailsList.size() > 0) {

                                Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                                if (!earcodes[k].equalsIgnoreCase("E04")) {
                                    System.out.println("Other than Da sub");
                                    subtotal[k] = subtotal[k] + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                    tots = tots + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                }
                                if (earcodes[k].equalsIgnoreCase("E04")) {
                                    System.out.println("Da sub");
                                    totd = totd + (long) employeeearningstransactionsObj.getAmount().floatValue();
                                }


                            }


                        }

                        float sDa = (tots * Float.parseFloat(dapercentage) / 100);
                        float sNewDa = sDa - totd;

                        System.out.println("Due" + sDa);
                        System.out.println("Drawn" + totd);
                        System.out.println("Arrear" + sNewDa);

                        //Incrementarrearreference
                        Criteria empIncRefCrit = session.createCriteria(Incrementarrearreference.class);
                        empIncRefCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                        empIncRefCrit.add(Restrictions.sqlRestriction("suppaybillid='" + supplementatypaybillObj.getId() + "'"));
                        //empIncRefCrit.add(Restrictions.sqlRestriction("type='" + "SUPPLEMENTARY" + "'"));
                        empIncRefCrit.add(Restrictions.sqlRestriction("processid='" + payprocesid + "'"));
                        List empIncRefList = empIncRefCrit.list();
                        if (empIncRefList.size() > 0) {
                            Incrementarrearreference incrementarrearreferenceObj = (Incrementarrearreference) empIncRefList.get(0);
                            incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                            incrementarrearreferenceObj.setProcessid(payprocesid);
                            incrementarrearreferenceObj.setMonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth());
                            incrementarrearreferenceObj.setYear(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
                            incrementarrearreferenceObj.setType(type);
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(totd));
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa));
                            if (isCalculateEpf(session, epfno) && !type.equalsIgnoreCase("LEAVESURRENDER")) {
                                incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                            }
                            incrementarrearreferenceObj.setAccregion(LoggedInRegion);
                            incrementarrearreferenceObj.setCreatedby(LoggedInUser);
                            incrementarrearreferenceObj.setCreateddate(getCurrentDate());
                            transaction = session.beginTransaction();
                            session.update(incrementarrearreferenceObj);
                            transaction.commit();
                        } else {
                            Incrementarrearreference incrementarrearreferenceObj = new Incrementarrearreference();
                            SupplementaryBillServiceImpl supplementaryBillServiceImplObj = new SupplementaryBillServiceImpl();
                            incrementarrearreferenceObj.setId(supplementaryBillServiceImplObj.getMaxIncrementarrearreferenceid(session, LoggedInRegion));
                            incrementarrearreferenceObj.setSuppaybillid(supplementatypaybillObj.getId());
                            incrementarrearreferenceObj.setProcessid(payprocesid);
                            incrementarrearreferenceObj.setType(type);
                            incrementarrearreferenceObj.setDrawn(new BigDecimal(totd));
                            incrementarrearreferenceObj.setArrear(new BigDecimal(sNewDa));
                            incrementarrearreferenceObj.setDue(new BigDecimal(sDa));
                            if (isCalculateEpf(session, epfno) && !type.equalsIgnoreCase("LEAVESURRENDER")) {
                                incrementarrearreferenceObj.setEpf(getEPFAmount(session, sNewDa));
                                epftotal = epftotal + getEPFAmount(session, sNewDa).floatValue();
                            }
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

                    }


                    //Supplementaty end
                    float totalDADrawn = 0.00f;
                    float otherEarningTotal = 0.0f;
                    for (int k = 0; k < earcodes.length; k++) {
                        if (earcodes[k].equalsIgnoreCase("E04")) {
                            totalDADrawn = subtotal[k];
                        } else {
                            otherEarningTotal = otherEarningTotal + subtotal[k];
                            System.out.println("other earing " + subtotal[k]);
                        }

                    }

                    float totalDa = (otherEarningTotal * Float.parseFloat(dapercentage) / 100);
                    float newDa = totalDa - totalDADrawn;
                    //Drawn Calculation ends
                    Supplementaryemployeeearningsdetails earningsdetObj;
                    Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
                    earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + subSalaryStructureId + "' "));
                    earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + "E04" + "' "));
                    List earList = earDe.list();
                    if (earList.size() > 0) {
                        earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
                        earningsdetObj.setCancelled(Boolean.FALSE);
                        earningsdetObj.setAmount(new BigDecimal(totalDa));

                        transaction = session.beginTransaction();
                        session.update(earningsdetObj);
                        transaction.commit();

                    } else {

                        earningsdetObj = new Supplementaryemployeeearningsdetails();

                        earningsdetObj.setCancelled(Boolean.FALSE);
                        earningsdetObj.setAmount(new BigDecimal(totalDa));
                        earningsdetObj.setEarningmasterid("E04");
                        earningsdetObj.setSupplementarysalarystructure(getSupplementarysalarystructure(session, subSalaryStructureId));
                        earningsdetObj.setId(getSupplementaryemployeeearningsdetailsid(session, LoggedInRegion));
                        earningsdetObj.setAccregion(LoggedInRegion);
                        transaction = session.beginTransaction();
                        session.save(earningsdetObj);
                        transaction.commit();

                    }



                    Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
                    Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + "E04" + "'"));
                    List empEarnDetailsList = empEarnDetailsCrit.list();
                    if (empEarnDetailsList.size() > 0) {
                        employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
                        employeeearningstransactionsObj.setAmount(new BigDecimal(newDa));
                        employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                        employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                        employeeearningstransactionsObj.setEarningmasterid("E04");
                        employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                        employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                        employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        session.update(employeeearningstransactionsObj);
                        transaction.commit();
                    } else {
                        employeeearningstransactionsObj = new Supplementaryemployeeearningstransactions();

                        employeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                        employeeearningstransactionsObj.setAmount(new BigDecimal(newDa));
                        employeeearningstransactionsObj.setSupplementarypayrollprocessingdetails(supplementarypayrollprocessingdetailsObj);
                        employeeearningstransactionsObj.setEarningmasterid("E04");
                        String earningsTransactionId = getSupplementaryemployeeearningstransactionsid(session, LoggedInRegion);
                        employeeearningstransactionsObj.setId(earningsTransactionId);
                        employeeearningstransactionsObj.setAccregion(LoggedInRegion);
                        employeeearningstransactionsObj.setCreatedby(LoggedInUser);
                        employeeearningstransactionsObj.setCreateddate(getCurrentDate());

                        transaction = session.beginTransaction();
                        session.save(employeeearningstransactionsObj);
                        transaction.commit();
                    }

                    Criteria empdeductDetailsCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                    empdeductDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
                    empdeductDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                    empdeductDetailsCrit.add(Restrictions.sqlRestriction("deductionmasterid='D02'"));
                    List empDeductDetailsList = empdeductDetailsCrit.list();
                    if (empDeductDetailsList.size() > 0) {
                        Supplementaryemployeedeductionstransactions employeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) empDeductDetailsList.get(0);
                        employeedeductionstransactionsObj.setAmount(new BigDecimal(epftotal));
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
                        employeedeductionstransactionsObj.setAmount(new BigDecimal(epftotal));
                        employeedeductionstransactionsObj.setAccregion(LoggedInRegion);
                        employeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                        employeedeductionstransactionsObj.setCreatedby(LoggedInUser);
                        employeedeductionstransactionsObj.setCreateddate(getCurrentDate());
                        transaction = session.beginTransaction();
                        session.save(employeedeductionstransactionsObj);
                        transaction.commit();

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


            } else {
                resultMap.put("proceed", "no");
                resultMap.put("reason", "Processed Successfully");
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
    public Map saveDAArrearBatchCreation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fileno, String batchperiod) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Daarrear daarrearObj = null;
        int daarrearid = 0;
        try {
            Criteria daCrit = session.createCriteria(Daarrear.class);
            daCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            daCrit.add(Restrictions.sqlRestriction("active is true"));
            List daList = daCrit.list();
            if (daList.size() > 0) {
                daarrearObj = (Daarrear) daList.get(0);
                daarrearid = Integer.parseInt(daarrearObj.getId());
                transaction = session.beginTransaction();
                session.createSQLQuery("UPDATE daarrear SET active=FALSE WHERE accregion='" + LoggedInRegion + "'  and id='" + daarrearid + "'").executeUpdate();
                transaction.commit();

                Daarrear daarrearObj1 = new Daarrear();
                daarrearObj1.setId(daarrearid + 1 + "");
                daarrearObj1.setFileno(fileno + "/" + batchperiod);
                daarrearObj1.setCancelled(Boolean.FALSE);
                daarrearObj1.setActive(Boolean.TRUE);
                daarrearObj1.setName("DA Arrear");
                daarrearObj1.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                transaction = session.beginTransaction();
                session.save(daarrearObj1);
                transaction.commit();
                resultMap.put("success", "DA Arrear Batch Created");
            } else {
                resultMap.put("ERROR", "No Active DA Arrear is found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    public synchronized String getSupplementatypaybillid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
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
            if (ldList.size() > 0) {
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
            if (ldList.size() > 0) {
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

    public Supplementarysalarystructure getSupplementarysalarystructure(Session session, String supplementarySalaryStructureId) {
        Supplementarysalarystructure supplementarysalarystructureObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Supplementarysalarystructure.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + supplementarySalaryStructureId + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                supplementarysalarystructureObj = (Supplementarysalarystructure) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }


        return supplementarysalarystructureObj;
    }

    public synchronized String getSupplementaryemployeeearningsdetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
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

    public synchronized String getSupplementaryemployeeearningstransactionsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
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
            if (ldList.size() > 0) {
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

    public BigDecimal getEPFAmount(Session session, float totalDA) {
//        float total = 0;
        float earamt = 0;
        Criteria earSlapCrit = session.createCriteria(Earningslapdetails.class);
        earSlapCrit.add(Restrictions.sqlRestriction("earningcode='D02'"));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangefrom <=" + totalDA));
        earSlapCrit.add(Restrictions.sqlRestriction("amountrangeto >=" + totalDA));
        List earSlapList = earSlapCrit.list();
        if (earSlapList.size() > 0) {
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
    public Map getEmployeeListHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String sectionid) {
        Map resultMap = new HashMap();

        Criteria daCrit = session.createCriteria(Daarrear.class);
        daCrit.add(Restrictions.sqlRestriction("active is true"));
        List daList = daCrit.list();
        if (daList.size() > 0) {
            Daarrear daarrearObj = (Daarrear) daList.get(0);

            resultMap.put("daname", daarrearObj.getName());

            StringBuffer stringBuff = new StringBuffer();
            String qry = "select  spb.employeeprovidentfundnumber from supplementatypaybill spb  left join dabatchdetails dab on dab.id=spb.dabatch left join daarrear da on da.id=daarrear "
                    + " where da.active is true and spb.accregion='" + LoggedInRegion + "' and da.accregion='" + LoggedInRegion + "' and dab.accregion='" + LoggedInRegion + "' and spb.cancelled is false and da.cancelled is false and dab.cancelled is false ";

            Employeemaster employeemasterObj;
            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
//            empCrit.add(Restrictions.sqlRestriction("section='" + sectionid + "'"));
            empCrit.add(Restrictions.sqlRestriction("epfno not in (" + qry + ")"));
            empCrit.addOrder(Order.asc("epfno"));

            List empList = empCrit.list();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"employeetable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
//            stringBuff.append("<th width=\"20%\"></th>");
            stringBuff.append("<th width=\"20%\"><input type=\"checkbox\" id=\"daselectall\" name=\"daselectall\" value=\"\" onclick=\"checkAll(this.value)\" /></th>");
            stringBuff.append("<th width=\"20%\">Epf No</th>");
            stringBuff.append("<th width=\"20%\">Name</th>");
            stringBuff.append("<th width=\"20%\">Designation</th>");
            stringBuff.append("<th width=\"20%\">Department</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    employeemasterObj = (Employeemaster) empList.get(i);
                    stringBuff.append("<tr >");
                    stringBuff.append("<td ><input type=\"checkbox\" id=\"epfnos\" name=\"epfnos\" value=\"" + employeemasterObj.getEpfno() + "\"  /></td>");
                    stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                    stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    stringBuff.append("<td >" + getSection(session, employeemasterObj.getSection()).getSectionname() + "</td>");
                    stringBuff.append("</tr>");
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            resultMap.put("employeelist", stringBuff.toString());


            Map dabatchlist = new LinkedHashMap();
            dabatchlist.put("0", "--Select--");
            Dabatchdetails dabatchdetailsObj;
            Criteria lrCrit = session.createCriteria(Dabatchdetails.class);
            lrCrit.add(Restrictions.sqlRestriction("daarrear='" + daarrearObj.getId() + "'"));
            lrCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            lrCrit.add(Restrictions.sqlRestriction("active is true"));
            lrCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));

            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                for (int i = 0; i < ldList.size(); i++) {
                    dabatchdetailsObj = (Dabatchdetails) ldList.get(i);
                    dabatchlist.put(dabatchdetailsObj.getId(), dabatchdetailsObj.getId());
                }
                resultMap.put("dabatchlist", dabatchlist);
            }



        } else {
            resultMap.put("employeelist", "");
            resultMap.put("daname", "");
        }

        return resultMap;

    }

    public synchronized String getMaxSeqNumberDABatchId(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getDabatchid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setDabatchid(maxNoStr);
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
    public Map loadDADetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();

        Map daarrearlist = new LinkedHashMap();
        daarrearlist.put("0", "--Select--");
        Daarrear daarrearObj;
        Criteria lrCrit = session.createCriteria(Daarrear.class);
        lrCrit.add(Restrictions.sqlRestriction("active is true"));
        lrCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        List ldList = lrCrit.list();
        if (ldList.size() > 0) {
            for (int i = 0; i < ldList.size(); i++) {
                daarrearObj = (Daarrear) ldList.get(i);
                daarrearlist.put(daarrearObj.getId(), daarrearObj.getName());
            }

        }
        resultMap.put("daarrearlist", daarrearlist);
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadbatchDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String daarrearid) {
        Map resultMap = new HashMap();

        Map dabatchlist = new LinkedHashMap();
        dabatchlist.put("0", "--Select--");
        Dabatchdetails dabatchdetailsObj;
        Criteria lrCrit = session.createCriteria(Dabatchdetails.class);
        lrCrit.add(Restrictions.sqlRestriction("daarrear='" + daarrearid.trim() + "'"));
        lrCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        lrCrit.add(Restrictions.sqlRestriction("active is true"));
        lrCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));

        List ldList = lrCrit.list();
        if (ldList.size() > 0) {
            for (int i = 0; i < ldList.size(); i++) {
                dabatchdetailsObj = (Dabatchdetails) ldList.get(i);
                dabatchlist.put(dabatchdetailsObj.getId(), dabatchdetailsObj.getId());
            }
            resultMap.put("dabatchlist", dabatchlist);
        } else {
            resultMap.put("ERROR", "No Batchs Creation");
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBatchEmployeeListHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid) {
        Map resultMap = new HashMap();

        Criteria daCrit = session.createCriteria(Daarrear.class);
        daCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        daCrit.add(Restrictions.sqlRestriction("active is true"));
        List daList = daCrit.list();
        if (daList.size() > 0) {
            Daarrear daarrearObj = (Daarrear) daList.get(0);

            resultMap.put("daname", daarrearObj.getName());

            StringBuffer stringBuff = new StringBuffer();
            String qry = "select  spb.employeeprovidentfundnumber from supplementatypaybill spb  left join dabatchdetails dab on dab.id=spb.dabatch left join daarrear da on da.id=daarrear "
                    + " where spb.dabatch='" + batchid.trim() + "' and type='DAARREAR' and  da.active is true and spb.accregion='" + LoggedInRegion + "' and da.accregion='" + LoggedInRegion + "' and dab.accregion='" + LoggedInRegion + "' and spb.cancelled is false and da.cancelled is false and dab.cancelled is false ";

            System.out.println("qry===" + qry);
            Employeemaster employeemasterObj;
            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            empCrit.add(Restrictions.sqlRestriction("epfno  in (" + qry + ")"));
            empCrit.addOrder(Order.asc("epfno"));

            List empList = empCrit.list();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"employeetable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
//            stringBuff.append("<th width=\"20%\"></th>");
            stringBuff.append("<th width=\"10%\"><input type=\"checkbox\" id=\"dabatchselectall\" name=\"dabatchselectall\" value=\"\" onclick=\"checkAll(this.value)\" /></th>");
            stringBuff.append("<th width=\"10%\">Epf No</th>");
            stringBuff.append("<th width=\"20%\">Name</th>");
            stringBuff.append("<th width=\"20%\">Designation</th>");
            stringBuff.append("<th width=\"20%\">Department</th>");
            stringBuff.append("<th width=\"10%\">Department</th>");
            stringBuff.append("<th width=\"10%\">Modification</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    employeemasterObj = (Employeemaster) empList.get(i);
                    stringBuff.append("<tr >");
                    stringBuff.append("<td ><input type=\"checkbox\" id=\"daepfnos\" name=\"daepfnos\" value=\"" + employeemasterObj.getEpfno() + "\"  /></td>");
                    stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                    stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    stringBuff.append("<td >" + getSection(session, employeemasterObj.getSection()).getSectionname() + "</td>");
                    stringBuff.append("<td ><input type=\"button\" class=\"submitbu\" id=\"remove\" value=\"Remove\" onclick=\"removeFromDaIncrement('" + batchid.trim() + "','" + employeemasterObj.getEpfno() + "');\" ></td>");
                    stringBuff.append("<td ><input type=\"radio\" name=\"daepfno\" id=\"" + "daepfno" + batchid + "\" onclick=\"modifyDADetails('" + employeemasterObj.getEpfno() + "','" + batchid + "')\"></td>");
                    stringBuff.append("</tr>");
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            resultMap.put("employeelist", stringBuff.toString());

        } else {
            resultMap.put("employeelist", "");
            resultMap.put("daname", "");
        }
        return resultMap;

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadSectionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map sectionMap = new LinkedHashMap();
        sectionMap.put("0", "--Select--");
        String sectionid = "";
        String sectionname = "";
        try {
            Criteria secCrit = session.createCriteria(Sectionmaster.class);
//            secCrit.add(Restrictions.sqlRestriction("id not in ('S13','S14')"));
            secCrit.add(Restrictions.sqlRestriction(" (region='ALL' or region='" + LoggedInRegion + "')"));
            secCrit.add(Restrictions.sqlRestriction(" parentcode!='0'"));
            secCrit.addOrder(Order.asc("sectionname"));
            List<Sectionmaster> secList = secCrit.list();
            resultMap = new TreeMap();
            for (Sectionmaster lbobj : secList) {

                sectionid = lbobj.getId();
                sectionname = lbobj.getSectionname();


                sectionMap.put(sectionid, sectionname);
            }
            resultMap.put("sectionlist", sectionMap);
            resultMap.put("currentRegion", LoggedInRegion);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public boolean isCalculateEpf(Session session, String epfno) {
        boolean stat = false;
        String queryStr = "select edda.amount from employeedeductiondetailsactual edda "
                + " left join salarystructureactual ssa on ssa.id=edda.salarystructureactualid  "
                + " where ssa.periodto is null and ssa.employeeprovidentfundnumber='" + epfno + "' and deductionmasterid='D02'";
        List subSalaryStruList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (subSalaryStruList.size() > 0) {
            stat = true;
        } else {
            stat = false;
        }

        return stat;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map removeFromDa(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid, String epfno) {
        Transaction transaction = null;

        try {

            String epfnos = epfno.substring(0, epfno.length() - 1);
            epfnos = epfnos.replaceAll(",", "','");

//                System.out.println("epfnos==="+epfnos);

            transaction = session.beginTransaction();
            session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions SET  cancelled=true  WHERE supplementarypayrollprocessingdetailsid in (select sppd.id from supplementarypayrollprocessingdetails sppd left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid where  spb.dabatch='" + batchid + "' and spb.employeeprovidentfundnumber in ('" + epfnos + "') )").executeUpdate();
//            session.createSQLQuery("UPDATE supplementaryemployeeearningstransactions SET  cancelled=true  WHERE supplementarypayrollprocessingdetailsid in (select sppd.id from supplementarypayrollprocessingdetails sppd left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid where  spb.dabatch='" + batchid + "' and spb.employeeprovidentfundnumber ='" + epfno + "')").executeUpdate();
            transaction.commit();

            transaction = session.beginTransaction();
            session.createSQLQuery("UPDATE supplementaryemployeedeductionstransactions SET  cancelled=true WHERE supplementarypayrollprocessingdetailsid in (select sppd.id from supplementarypayrollprocessingdetails sppd left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid where spb.dabatch='" + batchid + "' and spb.employeeprovidentfundnumber in ('" + epfnos + "'))").executeUpdate();
//            session.createSQLQuery("UPDATE supplementaryemployeedeductionstransactions SET  cancelled=true WHERE supplementarypayrollprocessingdetailsid in (select sppd.id from supplementarypayrollprocessingdetails sppd left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid where spb.dabatch='" + batchid + "' and spb.employeeprovidentfundnumber='" + epfno + "')").executeUpdate();
            transaction.commit();

            transaction = session.beginTransaction();
            session.createSQLQuery("UPDATE supplementarypayrollprocessingdetails SET  cancelled=true WHERE supplementatypaybillid in (select id from supplementatypaybill WHERE dabatch='" + batchid + "' and employeeprovidentfundnumber in ('" + epfnos + "'))").executeUpdate();
//            session.createSQLQuery("UPDATE supplementarypayrollprocessingdetails SET  cancelled=true WHERE supplementatypaybillid in (select id from supplementatypaybill WHERE dabatch='" + batchid + "' and employeeprovidentfundnumber='" + epfno + "')").executeUpdate();
            transaction.commit();

            transaction = session.beginTransaction();
            session.createSQLQuery("UPDATE supplementatypaybill SET  cancelled=true,dabatch=null WHERE dabatch='" + batchid + "' and employeeprovidentfundnumber in ('" + epfnos + "')").executeUpdate();
//            session.createSQLQuery("UPDATE supplementatypaybill SET  cancelled=true,dabatch=null WHERE dabatch='" + batchid + "' and employeeprovidentfundnumber='" + epfno + "'").executeUpdate();
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Map resultMap = new HashMap();

        Criteria daCrit = session.createCriteria(Daarrear.class);
        daCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        daCrit.add(Restrictions.sqlRestriction("active is true"));
        List daList = daCrit.list();
        if (daList.size() > 0) {
            Daarrear daarrearObj = (Daarrear) daList.get(0);

            resultMap.put("daname", daarrearObj.getName());

            StringBuffer stringBuff = new StringBuffer();
            String qry = "select  spb.employeeprovidentfundnumber from supplementatypaybill spb  left join dabatchdetails dab on dab.id=spb.dabatch left join daarrear da on da.id=daarrear "
                    + " where spb.dabatch='" + batchid.trim() + "' and type='DAARREAR' and  da.active is true and spb.accregion='" + LoggedInRegion + "' and da.accregion='" + LoggedInRegion + "' and dab.accregion='" + LoggedInRegion + "' and spb.cancelled is false and da.cancelled is false and dab.cancelled is false ";

            System.out.println("qry===" + qry);
            Employeemaster employeemasterObj;
            Criteria empCrit = session.createCriteria(Employeemaster.class);
            empCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            empCrit.add(Restrictions.sqlRestriction("epfno  in (" + qry + ")"));
            empCrit.addOrder(Order.asc("epfno"));

            List empList = empCrit.list();
            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"employeetable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
//            stringBuff.append("<th width=\"20%\"></th>");
            stringBuff.append("<th width=\"10%\"><input type=\"checkbox\" id=\"daselectall\" name=\"daselectall\" value=\"\" onclick=\"checkAll(this.value)\" /></th>");
            stringBuff.append("<th width=\"20%\">Epf No</th>");
            stringBuff.append("<th width=\"20%\">Name</th>");
            stringBuff.append("<th width=\"20%\">Designation</th>");
            stringBuff.append("<th width=\"20%\">Department</th>");
            stringBuff.append("<th width=\"10%\">Department</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");
            if (empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    employeemasterObj = (Employeemaster) empList.get(i);
                    stringBuff.append("<tr >");
                    stringBuff.append("<td ><input type=\"checkbox\" id=\"epfnos\" name=\"epfnos\" value=\"" + employeemasterObj.getEpfno() + "\"  /></td>");
                    stringBuff.append("<td >" + employeemasterObj.getEpfno() + "</td>");
                    stringBuff.append("<td >" + employeemasterObj.getEmployeename() + "</td>");
                    stringBuff.append("<td >" + getDesignationMater(session, employeemasterObj.getDesignation()).getDesignation() + "</td>");
                    stringBuff.append("<td >" + getSection(session, employeemasterObj.getSection()).getSectionname() + "</td>");
                    stringBuff.append("<td ><input type=\"button\" class=\"submitbu\" id=\"remove\" value=\"Remove\" onclick=\"removeFromDaIncrement('" + batchid.trim() + "','" + employeemasterObj.getEpfno() + "');\" ></td>");
                    stringBuff.append("</tr>");
                }

            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            resultMap.put("employeelist", stringBuff.toString());

        } else {
            resultMap.put("employeelist", "");
            resultMap.put("daname", "");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map modifyDADetailsinHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid, String epfno) {
        Transaction transaction = null;
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        Criteria supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
        supPayBillCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
        supPayBillCrit.add(Restrictions.sqlRestriction("dabatch = '" + batchid + "' "));
        supPayBillCrit.add(Restrictions.sqlRestriction("cancelled is false"));
        List<Supplementatypaybill> supPayBillList = supPayBillCrit.list();
        if (supPayBillList.size() > 0) {
            for (int i = 0; i < supPayBillList.size(); i++) {
                Supplementatypaybill supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(i);

                Criteria supPayBillProcessCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                supPayBillProcessCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                supPayBillProcessCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List<Supplementarypayrollprocessingdetails> supPayBillProcessList = supPayBillProcessCrit.list();
                resultHTML.append("<table>");

                resultHTML.append("<tr class=\"gridmenu\">");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append("EPFNO");
                resultHTML.append("</td>");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append(epfno);
                resultHTML.append("</td>");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append("BATCH ID");
                resultHTML.append("</td>");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append(batchid);
                resultHTML.append("</td>");
                resultHTML.append("</tr>");

                resultHTML.append("<tr class=\"gridmenu\">");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append("Month & year");
                resultHTML.append("</td>");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append("Arrear");
                resultHTML.append("</td>");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append("EPF");
                resultHTML.append("</td>");
                resultHTML.append("<td width=\"25%\">");
                resultHTML.append("");
                resultHTML.append("</td>");
                resultHTML.append("</tr>");
                if (supPayBillProcessList.size() > 0) {
                    for (int j = 0; j < supPayBillProcessList.size(); j++) {
                        Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayBillProcessList.get(j);
                        resultHTML.append("<tr>");
                        resultHTML.append("<td width=\"15%\">");
                        resultHTML.append(CommonUtility.getMonthAndYear(supplementarypayrollprocessingdetailsObj.getCalculatedmonth() - 1, supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
                        resultHTML.append("</td>");
//                        resultHTML.append("<td>");
//                        resultHTML.append(supplementarypayrollprocessingdetailsObj.getCalculatedyear());
//                        resultHTML.append("</td>");
//                        resultHTML.append("<td width=\"10%\">");
//                        resultHTML.append("");
//                        resultHTML.append("</td>");


                        Criteria supEarCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                        supEarCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));
                        List<Supplementaryemployeeearningstransactions> supEarList = supEarCrit.list();
                        if (supEarList.size() > 0) {
                            Supplementaryemployeeearningstransactions supplementaryemployeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) supEarList.get(0);
                            resultHTML.append("<td>");
                            resultHTML.append(supplementaryemployeeearningstransactionsObj.getAmount());
                            resultHTML.append("</td>");
                        } else {
                            resultHTML.append("<td>");
                            resultHTML.append("</td>");
                        }

                        Criteria supDedCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        supDedCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));
                        List<Supplementaryemployeedeductionstransactions> supDedList = supDedCrit.list();
                        if (supDedList.size() > 0) {
                            Supplementaryemployeedeductionstransactions supplementaryemployeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) supDedList.get(0);
                            resultHTML.append("<td>");
                            resultHTML.append(supplementaryemployeedeductionstransactionsObj.getAmount());
                            resultHTML.append("</td>");
                        } else {
                            resultHTML.append("<td>");
                            resultHTML.append("</td>");
                        }
                        resultHTML.append("<td ><input type=\"radio\" name=\"daepfno\" id=\"" + "daepfno" + batchid + "\" onclick=\"addDADetails('" + supplementatypaybillObj.getId() + "','" + supplementarypayrollprocessingdetailsObj.getId() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + batchid + "')\"></td>");

                        resultHTML.append("</tr>");
                    }

                }
                resultHTML.append("</table>");

            }
        }
        resultMap.put("daincrementhtml", resultHTML.toString());
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDADetailsCompManual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String subpaybillid, String processingdetailsid, String month, String year, String epfno, String dabatchno) {
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        Criteria incArrCrit = session.createCriteria(Incrementarrearreference.class);
        incArrCrit.add(Restrictions.sqlRestriction("suppaybillid = '" + subpaybillid + "' "));
        incArrCrit.add(Restrictions.sqlRestriction("month = " + month));
        incArrCrit.add(Restrictions.sqlRestriction("year = " + year));
        List<Incrementarrearreference> incArrList = incArrCrit.list();
        resultHTML.append("<table>");
//        resultHTML.append("<tr>");
        resultHTML.append("<tr class=\"gridmenu\">");
        resultHTML.append("<td width=\"10%\">");
        resultHTML.append("Month & year");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Basic");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Per Pay");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Grade Pay");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Due");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Drawn");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Arrear");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Epf");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Net");
        resultHTML.append("</td>");
        resultHTML.append("</tr>");
        for (int i = 0; i < incArrList.size(); i++) {
            Incrementarrearreference tncrementarrearreferenceObj = (Incrementarrearreference) incArrList.get(i);
            resultHTML.append("<tr>");
            resultHTML.append("<td>");

            resultHTML.append(CommonUtility.getMonthAndYear(tncrementarrearreferenceObj.getMonth() - 1, tncrementarrearreferenceObj.getYear()));

            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append("");
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append("");
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append("");
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(tncrementarrearreferenceObj.getDue());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(tncrementarrearreferenceObj.getDrawn());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(tncrementarrearreferenceObj.getArrear());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(tncrementarrearreferenceObj.getEpf());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append("");
            resultHTML.append("</td>");
            resultHTML.append("</tr>");

        }
        resultHTML.append("</table>");

        Criteria daArrCrit = session.createCriteria(Daarreardiffentry.class);
        daArrCrit.add(Restrictions.sqlRestriction("supplementarypayroll = '" + subpaybillid + "' "));
        daArrCrit.add(Restrictions.sqlRestriction("month = " + month));
        daArrCrit.add(Restrictions.sqlRestriction("year = " + year));
        List<Daarreardiffentry> daDiffList = daArrCrit.list();
        resultHTML.append("<table>");
        resultHTML.append("<tr class=\"gridmenu\">");
//        resultHTML.append("<tr>");
        resultHTML.append("<td width=\"10%\">");
        resultHTML.append("Month & year");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Basic");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Per Pay");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Grade Pay");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Due");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Drawn");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Arrear");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Epf");
        resultHTML.append("</td>");
        resultHTML.append("<td width=\"8%\">");
        resultHTML.append("Net");
        resultHTML.append("</td>");
        resultHTML.append("</tr>");
        for (int i = 0; i < daDiffList.size(); i++) {
            Daarreardiffentry daarreardiffentryObj = (Daarreardiffentry) daDiffList.get(i);
            resultHTML.append("<tr>");
            resultHTML.append("<td>");
//            resultHTML.append(daarreardiffentryObj.getMonth() + " " + daarreardiffentryObj.getYear());

            resultHTML.append(CommonUtility.getMonthAndYear(daarreardiffentryObj.getMonth() - 1, daarreardiffentryObj.getYear()));

            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(daarreardiffentryObj.getBasic());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(daarreardiffentryObj.getPerpay());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(daarreardiffentryObj.getGradepay());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(daarreardiffentryObj.getDue());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(daarreardiffentryObj.getDrawn());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(daarreardiffentryObj.getArrear());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append(daarreardiffentryObj.getEpf());
            resultHTML.append("</td>");
            resultHTML.append("<td>");
            resultHTML.append("");
            resultHTML.append("</td>");
            resultHTML.append("</tr>");

        }
        resultHTML.append("<tr class=\"gridmenu\">");
        resultHTML.append("<td colspan=\"9\" >");
//        resultHTML.append("Add");
        resultHTML.append(" <input type=\"button\" CLASS=\"submitbu\" name=\"adddaManual\" id=\"adddaManual\" value=\"Add DA\"  onclick=\"showDaManualInput('" + subpaybillid + "','" + epfno + "','" + dabatchno + "');\"  >");
        resultHTML.append("</td>");
        resultHTML.append("</tr>");
        resultHTML.append("</table>");

        resultMap.put("dabreakups", resultHTML.toString());
        return resultMap;
    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map getDAIncrementArrearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String billno) {
//        Map resultMap = new HashMap();
//        Transaction transaction = null;
//        String fromdate = "";
//        String enddate = "";
//        Supplementatypaybill supplementatypaybillObj = null;
//        String[] earcodes = {"E01", "E03", "E04", "E06", "E07", "E25"};
//        long[] total = {0, 0, 0, 0, 0, 0};
//        long[] subtotal = {0, 0, 0, 0, 0, 0};
//        try {
//            Criteria supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
//            supPayBillCrit.add(Restrictions.sqlRestriction("id = '" + billno + "' "));
//            supPayBillCrit.add(Restrictions.sqlRestriction("date = '" + postgresDate(asondate) + "' "));
//            supPayBillCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
//            supPayBillCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
//            supPayBillCrit.add(Restrictions.sqlRestriction("type='INCREMENTARREAR'"));
//
//            List<Supplementatypaybill> supPayBillList = supPayBillCrit.list();
//            if (supPayBillList.size() > 0) {
//                supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(0);
//
//                StringBuffer resultHTML = new StringBuffer();
//                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
//                resultHTML.append("<tr class=\"gridmenu\">");
//                resultHTML.append("<td align=\"right\" >");
//                resultHTML.append("Particulars");
//                resultHTML.append("</td>");
//                for (int k = 0; k < earcodes.length; k++) {
//                    resultHTML.append("<td align=\"right\" >");
//                    resultHTML.append(getPaycodeMater(session, earcodes[k]).getPaycodename());
//                    resultHTML.append("</td>");
//                }
//                resultHTML.append("<td align=\"right\" >");
//                resultHTML.append("Balance");
//                resultHTML.append("</td>");
//                resultHTML.append("</tr>");
//
//                Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = null;
//                Criteria supPayrollProcesCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
//                supPayrollProcesCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
//                supPayrollProcesCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
//                supPayrollProcesCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
//                supPayrollProcesCrit.addOrder(Order.asc("calculatedyear"));
//                supPayrollProcesCrit.addOrder(Order.asc("calculatedmonth"));
//                List<Supplementarypayrollprocessingdetails> supPayProcList = supPayrollProcesCrit.list();
//                if (supPayProcList.size() > 0) {
//                    for (int i = 0; i < supPayProcList.size(); i++) {
//
//
//                        for (int p = 0; p < subtotal.length; p++) {
//                            subtotal[p] = 0;
//                        }
//                        supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayProcList.get(i);
//
//                        if (i == 0) {
//                            System.out.println("Start Date sai" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "-" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "-01");
//                            if (supplementarypayrollprocessingdetailsObj.getCalculatedmonth().toString().trim().length() > 1) {
//                                fromdate = supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "-" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "-01";
//                            } else {
//                                fromdate = supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "-0" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "-01";
//                            }
//                        }
//                        if (i == supPayProcList.size() - 1) {
//                            System.out.println("End Date sai" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "-" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "-" + getLastDate("01/" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
//                            if (supplementarypayrollprocessingdetailsObj.getCalculatedmonth().toString().trim().length() > 1) {
//                                enddate = supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "-" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "-" + getLastDate("01/" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
//                            } else {
//                                enddate = supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "-0" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "-" + getLastDate("01/" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
//                            }
//
//                        }
//
//                        resultHTML.append("<tr class=\"gridmenu\">");
//                        resultHTML.append("<td colspan=\"" + (earcodes.length + 1) + "\" align=\"left\" >");
//                        resultHTML.append(supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "/" + supplementarypayrollprocessingdetailsObj.getCalculatedyear());
//                        resultHTML.append("</td>");
//                        resultHTML.append("<td>");
//                        resultHTML.append(" <input type=\"button\" CLASS=\"submitbu\" name=\"adddrawn\" id=\"adddrawn\" value=\"+ Drawn\"  onclick=\"showdrwandetailsforupdation('" + epfno + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "');\"  >");
//                        resultHTML.append("</td>");
//                        resultHTML.append("</tr>");
//
//                        Supplementarysalarystructure supplementarysalarystructureObj;
//                        Criteria supSalStruCrit = session.createCriteria(Supplementarysalarystructure.class);
//                        supSalStruCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));
//                        supSalStruCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
//
//                        List<Supplementarysalarystructure> supSalStruList = supSalStruCrit.list();
//                        if (supSalStruList.size() > 0) {
//                            supplementarysalarystructureObj = (Supplementarysalarystructure) supSalStruList.get(0);
//                            resultHTML.append("<tr >");
//                            resultHTML.append("<td align=\"left\" >");
//                            resultHTML.append("Due");
//                            resultHTML.append("</td>");
//                            for (int k = 0; k < earcodes.length; k++) {
//                                Supplementaryemployeeearningsdetails earningsdetObj;
//                                Criteria earDe = session.createCriteria(Supplementaryemployeeearningsdetails.class);
//                                earDe.add(Restrictions.sqlRestriction("supplementarysalarystructureid = '" + supplementarysalarystructureObj.getId() + "' "));
//                                earDe.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
//                                earDe.add(Restrictions.sqlRestriction("earningmasterid = '" + earcodes[k] + "' "));
//                                List<Supplementaryemployeeearningsdetails> earList = earDe.list();
//                                if (earList.size() > 0) {
//                                    earningsdetObj = (Supplementaryemployeeearningsdetails) earList.get(0);
//                                    resultHTML.append("<td align=\"right\" >");
//                                    resultHTML.append(earningsdetObj.getAmount().setScale(2));
//                                    subtotal[k] = subtotal[k] + (long) earningsdetObj.getAmount().floatValue();
//                                    resultHTML.append("</td>");
//                                } else {
//                                    resultHTML.append("<td align=\"right\" >");
//                                    resultHTML.append("0.00");
//                                    resultHTML.append("</td>");
//                                }
//                            }
//                            resultHTML.append("<td>");
//                            resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showDueDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + supplementarysalarystructureObj.getId() + "')\">");
//                            resultHTML.append("</td>");
//                            resultHTML.append("</tr>");
//
//                            //Regular
//                            Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
//                            empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
//                            empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
//                            empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("month=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth()));
//                            empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("year=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear()));
//                            List empPayProcessDetails = empPayProcessDetailsCrit.list();
//                            if (empPayProcessDetails.size() > 0) {
//                                resultHTML.append("<tr>");
//                                resultHTML.append("<td align=\"left\" >");
//                                resultHTML.append("Drawn");
//                                resultHTML.append("</td>");
//
//                                for (int k = 0; k < earcodes.length; k++) {
//
//                                    Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(0);
//                                    Criteria empEarnDetailsCrit = session.createCriteria(Employeeearningstransactions.class);
//                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
//                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
//                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
//                                    List empEarnDetailsList = empEarnDetailsCrit.list();
//                                    if (empEarnDetailsList.size() > 0) {
//
//                                        for (int j = 0; j < empEarnDetailsList.size(); j++) {
//                                            Employeeearningstransactions employeeearningstransactionsObj = (Employeeearningstransactions) empEarnDetailsList.get(j);
//
//                                            resultHTML.append("<td align=\"right\" >");
//                                            resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
//                                            subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
//                                            System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
//                                            resultHTML.append("</td>");
//
//                                        }
//
//                                    } else {
//                                        resultHTML.append("<td align=\"right\" >");
//                                        resultHTML.append("0.00");
//                                        resultHTML.append("</td>");
//                                    }
//
//                                }
//                                resultHTML.append("<td align=\"right\" >");
//                                resultHTML.append("</td>");
//                                resultHTML.append("</tr>");
//                            }
//
//                            //Regular end
//                            //Supplementaty
//                            StringBuffer query = new StringBuffer();
//                            query.append("select sppd.id, spb.type from supplementarypayrollprocessingdetails sppd ");
//                            query.append(" left join supplementatypaybill spb on spb.id=sppd.supplementatypaybillid  ");
//                            query.append(" where sppd.accregion='" + LoggedInRegion + "' and sppd.calculatedyear=" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "  and sppd.calculatedmonth=" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + " and spb.employeeprovidentfundnumber= '" + epfno + "' and spb.id!='" + supplementatypaybillObj.getId() + "'");
//                            SQLQuery subpayquery = session.createSQLQuery(query.toString());
//                            System.out.println("size" + subpayquery.list().size());
//                            for (ListIterator its = subpayquery.list().listIterator(); its.hasNext();) {
//                                Object[] rows = (Object[]) its.next();
//                                String payprocesid = (String) rows[0];
//                                String type = (String) rows[1];
//                                System.out.println(payprocesid + type);
//                                resultHTML.append("<tr>");
//                                resultHTML.append("<td align=\"left\" >");
//                                resultHTML.append("Drawn");
//                                resultHTML.append("</td>");
//
//                                for (int k = 0; k < earcodes.length; k++) {
//                                    System.out.println(earcodes[k]);
//                                    Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
//                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + payprocesid + "'"));
//                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
//                                    empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[k] + "'"));
//                                    List empEarnDetailsList = empEarnDetailsCrit.list();
//                                    if (empEarnDetailsList.size() > 0) {
//                                        Supplementaryemployeeearningstransactions employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
//
//                                        resultHTML.append("<td align=\"right\" >");
//                                        resultHTML.append(employeeearningstransactionsObj.getAmount().setScale(2));
//                                        subtotal[k] = subtotal[k] - (long) employeeearningstransactionsObj.getAmount().floatValue();
//                                        System.out.print(employeeearningstransactionsObj.getEarningmasterid() + "" + employeeearningstransactionsObj.getAmount());
//                                        resultHTML.append("</td>");
//                                    } else {
//                                        resultHTML.append("<td align=\"right\" > ");
//                                        resultHTML.append("0.00");
//                                        resultHTML.append("</td>");
//                                    }
//
//                                }
//                                resultHTML.append("<td align=\"left\" >");
//                                if (type.equalsIgnoreCase("INCREMENTMANUAL")) {
//                                    resultHTML.append("<input type=\"radio\" name=\"duecode\" id=\"" + "due" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "\" onclick=\"showManIncDetails('" + supplementarypayrollprocessingdetailsObj.getCalculatedmonth() + "','" + supplementarypayrollprocessingdetailsObj.getCalculatedyear() + "','" + epfno + "','" + payprocesid + "')\">");
//                                }
//                                resultHTML.append("</td>");
//                                resultHTML.append("</tr>");
//                            }
//                            //Supplementaty end
//                        }
//                        resultHTML.append("<tr>");
//                        resultHTML.append("<td align=\"left\" >");
//                        resultHTML.append("Balance");
//                        resultHTML.append("</td>");
//                        float subtot = 0;
//                        for (int p = 0; p < subtotal.length; p++) {
//                            Supplementaryemployeeearningstransactions employeeearningstransactionsObj;
//                            Criteria empEarnDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
//                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
//                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
//                            empEarnDetailsCrit.add(Restrictions.sqlRestriction("earningmasterid='" + earcodes[p] + "'"));
//                            List empEarnDetailsList = empEarnDetailsCrit.list();
//                            if (empEarnDetailsList.size() > 0) {
//                                employeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) empEarnDetailsList.get(0);
//                                employeeearningstransactionsObj.setAmount(new BigDecimal(subtotal[p]));
//                                transaction = session.beginTransaction();
//                                session.update(employeeearningstransactionsObj);
//                                transaction.commit();
//                            } else {
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
//                            }
//                            resultHTML.append("<td align=\"right\" >");
//                            subtot = subtot + employeeearningstransactionsObj.getAmount().floatValue();
//                            resultHTML.append(employeeearningstransactionsObj.getAmount());
//                            resultHTML.append("</td>");
//
//
//                        }
//                        resultHTML.append("<td align=\"right\" >");
//                        resultHTML.append(subtot);
//                        resultHTML.append("</td>");
//                        resultHTML.append("</tr>");
//
//                    }
//                }
//
//                String qry = " select cast(generate_series as date),employeeprovidentfundnumber,id from "
//                        + " generate_series('" + fromdate + "',   '" + enddate + "', cast('1 day' as interval)) generate_series "
//                        + " join supplementatypaybill as sp on sp.sldate=generate_series "
//                        + " where employeeprovidentfundnumber='" + epfno + "'";
//
//                System.out.println("from date " + fromdate);
//                System.out.println("Todate " + enddate);
//
//
//                SQLQuery misquery = session.createSQLQuery(qry);
//                for (ListIterator it = misquery.list().listIterator(); it.hasNext();) {
//                    Object[] row = (Object[]) it.next();
//                    String id = (String) row[2];
//                    System.out.println("Leave Surrender id" + id);
//                }
//
//                resultHTML.append("</table>");
//                resultMap.put("incrementhtml", resultHTML.toString());
//            } else {
//                StringBuffer resultHTML = new StringBuffer();
//                resultHTML.append("Increment Arear not Made");
//                resultMap.put("incrementhtml", resultHTML.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (transaction != null) {
//                transaction.rollback();
//            }
//        }
//        return resultMap;
//    }
    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDAIncrementEarningsUpdation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String batchid) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        Employeemaster employeemasterObj = null;

        try {
            employeemasterObj = getEmployeemaster(session, epfno, LoggedInRegion);
            Supplementatypaybill supplementatypaybillObj = new Supplementatypaybill();
            String supPayBillId = getSupplementatypaybillid(session, LoggedInRegion);
            supplementatypaybillObj.setId(supPayBillId);
            supplementatypaybillObj.setAccregion(LoggedInRegion);
            supplementatypaybillObj.setDate(getCurrentDate());
            supplementatypaybillObj.setEmployeemaster(employeemasterObj);
            supplementatypaybillObj.setType("DAMANUAL");
            supplementatypaybillObj.setSection(employeemasterObj.getSection());
            supplementatypaybillObj.setSubsection(employeemasterObj.getSubsection());
            supplementatypaybillObj.setPaymentmode(employeemasterObj.getPaymentmode());
            supplementatypaybillObj.setEmployeecategory(employeemasterObj.getCategory());
            supplementatypaybillObj.setDesignation(employeemasterObj.getDesignation());
            supplementatypaybillObj.setAccregion(LoggedInRegion);
            supplementatypaybillObj.setCancelled(Boolean.FALSE);
            supplementatypaybillObj.setCreateddate(getCurrentDate());
            supplementatypaybillObj.setCreatedby(LoggedInUser);
            supplementatypaybillObj.setDabatchdetails(CommonUtility.getDabatchdetails(session, batchid));

            transaction = session.beginTransaction();
            session.save(supplementatypaybillObj);
            transaction.commit();

            Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = new Supplementarypayrollprocessingdetails();
            String id = getSupplementarypayrollprocessingdetailsid(session, LoggedInRegion);
            supplementarypayrollprocessingdetailsObj.setId(id);
            supplementarypayrollprocessingdetailsObj.setSupplementatypaybill(supplementatypaybillObj);
            supplementarypayrollprocessingdetailsObj.setAccregion(LoggedInRegion);
            supplementarypayrollprocessingdetailsObj.setCalculatedmonth(Integer.parseInt(month));
            supplementarypayrollprocessingdetailsObj.setCalculatedyear(Integer.parseInt(year));
            supplementarypayrollprocessingdetailsObj.setCancelled(Boolean.FALSE);


            transaction = session.beginTransaction();
            session.persist(supplementarypayrollprocessingdetailsObj);
            transaction.commit();

            String[] earcodes = {"E01", "E03", "E25"};
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
            resultMap.put("employeeearningslist", getDAIncrementManual(session, supplementarypayrollprocessingdetailsObj.getId(), LoggedInRegion));
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
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

    public Map getDAIncrementManual(Session session, String suppayprocesid, String LoggedInRegion) {
//        System.out.println(suppayprocesid);
        Map resultMap = new HashMap();
        int slno = 0;
        try {
            Criteria earDetailsCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
            earDetailsCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
            earDetailsCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + suppayprocesid + "' "));
            List earDetailsList = earDetailsCrit.list();
            resultMap.put("employeeearningslength", earDetailsList.size());
            if (earDetailsList.size() > 0) {
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
    public Map saveDaManual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String subpaybillid, String dmonthid, String dyearid, String basicamount, String perpayamt, String gradepayamt, String dueamt, String drawnamt, String arrearamt, String epfamt, String billtype) {

        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Daarreardiffentry daarreardiffentryObj = new Daarreardiffentry();

            daarreardiffentryObj.setId(getmaxSequenceNumberforDA(session));
            daarreardiffentryObj.setMonth(Integer.parseInt(dmonthid));
            daarreardiffentryObj.setYear(Integer.parseInt(dyearid));
            daarreardiffentryObj.setSupplementarypayroll(subpaybillid);

            if (basicamount.trim().length() > 0) {
                daarreardiffentryObj.setBasic(new BigDecimal(basicamount.trim()));
            } else {
                daarreardiffentryObj.setBasic(BigDecimal.ZERO);
            }
            if (perpayamt.trim().length() > 0) {
                daarreardiffentryObj.setPerpay(new BigDecimal(perpayamt.trim()));
            } else {
                daarreardiffentryObj.setPerpay(BigDecimal.ZERO);
            }
            if (gradepayamt.trim().length() > 0) {
                daarreardiffentryObj.setGradepay(new BigDecimal(gradepayamt.trim()));
            } else {
                daarreardiffentryObj.setGradepay(BigDecimal.ZERO);
            }
            if (dueamt.trim().length() > 0) {
                daarreardiffentryObj.setDue(new BigDecimal(dueamt.trim()));
            } else {
                daarreardiffentryObj.setDue(BigDecimal.ZERO);
            }
            if (drawnamt.trim().length() > 0) {
                daarreardiffentryObj.setDrawn(new BigDecimal(drawnamt.trim()));
            } else {
                daarreardiffentryObj.setDrawn(BigDecimal.ZERO);
            }
            if (arrearamt.trim().length() > 0) {
                daarreardiffentryObj.setArrear(new BigDecimal(arrearamt.trim()));
            } else {
                daarreardiffentryObj.setArrear(BigDecimal.ZERO);
            }
            if (epfamt.trim().length() > 0) {
                daarreardiffentryObj.setEpf(new BigDecimal(epfamt.trim()));
            } else {
                daarreardiffentryObj.setEpf(BigDecimal.ZERO);
            }

//                    String type[] = {"REGULAR", "SUPPLEMENTARY", "INCREMENTARREAR", "LEAVESURRENDER", "SUPLEMENTARYBILL"};
            if (billtype.equalsIgnoreCase("1")) {
                daarreardiffentryObj.setType("SUPPLEMENTARY");
            } else if (billtype.equalsIgnoreCase("2")) {
                daarreardiffentryObj.setType("LEAVESURRENDER");
            } else if (billtype.equalsIgnoreCase("3")) {
                daarreardiffentryObj.setType("INCREMENTARREAR");
            }
            session.save(daarreardiffentryObj);
            transaction.commit();

//            }
            resultMap.put("success", "successfully saved");


            String arrearStr = "SELECT sum(coalesce(arrear,'0')) FROM daarreardiffentry where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and supplementarypayroll='" + subpaybillid + "'";
            String epfStr = "SELECT sum(coalesce(epf,'0')) FROM daarreardiffentry where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and supplementarypayroll='" + subpaybillid + "'";

            BigDecimal arear = new BigDecimal(0.00);
            List arearList = (ArrayList) session.createSQLQuery(arrearStr).list();
            if (arearList.size() > 0) {
                arear = (BigDecimal) arearList.get(0);
            } else {
                arear = new BigDecimal(0.00);
            }

            BigDecimal epf = new BigDecimal(0.00);
            List epfList = (ArrayList) session.createSQLQuery(epfStr).list();
            if (epfList.size() > 0) {
                epf = (BigDecimal) epfList.get(0);
            } else {
                epf = new BigDecimal(0.00);
            }

            String arrearStr1 = "SELECT sum(coalesce(arrear,'0')) FROM incrementarrearreference where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and suppaybillid='" + subpaybillid + "'";
            String epfStr1 = "SELECT sum(coalesce(epf,'0')) FROM incrementarrearreference where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and suppaybillid='" + subpaybillid + "'";

            BigDecimal arear1 = new BigDecimal(0.00);
            List arearList1 = (ArrayList) session.createSQLQuery(arrearStr1).list();
            if (arearList1.size() > 0) {
                arear1 = (BigDecimal) arearList1.get(0);
            } else {
                arear1 = new BigDecimal(0.00);
            }

            BigDecimal epf1 = new BigDecimal(0.00);
            List epfList1 = (ArrayList) session.createSQLQuery(epfStr1).list();
            if (epfList1.size() > 0) {
                epf1 = (BigDecimal) epfList1.get(0);
            } else {
                epf1 = new BigDecimal(0.00);
            }
            System.out.println("result");
            System.out.println(arear);
            System.out.println(arear1);
            System.out.println("epf = " + epf);
            System.out.println("epf1 = " + epf1);
            if (arear1 == null) {
                arear1 = new BigDecimal(0.00);
            }
            if (epf1 == null) {
                epf1 = new BigDecimal(0.00);
            }

            if (arear == null) {
                arear = new BigDecimal(0.00);
            }
            if (epf == null) {
                epf = new BigDecimal(0.00);
            }

            updateDaManual(session, subpaybillid, dmonthid, dyearid, arear.add(arear1), epf.add(epf1));

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Transaction fails");
        }

        return resultMap;
    }

    public String getmaxSequenceNumberforDA(Session session) {
        int maxSequenceNumber = 1;

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(id as int)) as maxsequencenumber from daarreardiffentry");
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

        return String.valueOf(maxSequenceNumber);
    }

    private void updateDaManual(Session session, String id, String month, String year, BigDecimal arear, BigDecimal epf) {

        System.out.println("id = " + id);
        System.out.println("month = " + month);
        System.out.println("year = " + year);
        System.out.println("arear = " + id);
        System.out.println("epf = " + epf);

        Transaction transaction = null;
        Criteria supPayBillCrit = session.createCriteria(Supplementatypaybill.class);
        supPayBillCrit.add(Restrictions.sqlRestriction("id = '" + id + "' "));
        List<Supplementatypaybill> supPayBillList = supPayBillCrit.list();
        if (supPayBillList.size() > 0) {
            for (int i = 0; i < supPayBillList.size(); i++) {
                Supplementatypaybill supplementatypaybillObj = (Supplementatypaybill) supPayBillList.get(i);

                Criteria supPayBillProcessCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                supPayBillProcessCrit.add(Restrictions.sqlRestriction("supplementatypaybillid = '" + supplementatypaybillObj.getId() + "' "));
                supPayBillProcessCrit.add(Restrictions.sqlRestriction("calculatedmonth = " + month));
                supPayBillProcessCrit.add(Restrictions.sqlRestriction("calculatedyear = " + year));
                List<Supplementarypayrollprocessingdetails> supPayBillProcessList = supPayBillProcessCrit.list();

                if (supPayBillProcessList.size() > 0) {
                    for (int j = 0; j < supPayBillProcessList.size(); j++) {
                        Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) supPayBillProcessList.get(j);

                        Criteria supEarCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                        supEarCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));
                        List<Supplementaryemployeeearningstransactions> supEarList = supEarCrit.list();
                        if (supEarList.size() > 0) {
                            transaction = session.beginTransaction();
                            Supplementaryemployeeearningstransactions supplementaryemployeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) supEarList.get(0);
                            supplementaryemployeeearningstransactionsObj.setAmount(arear);
                            supplementaryemployeeearningstransactionsObj.setCancelled(Boolean.FALSE);
                            session.update(supplementaryemployeeearningstransactionsObj);
                            transaction.commit();
                        }

                        Criteria supDedCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        supDedCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid = '" + supplementarypayrollprocessingdetailsObj.getId() + "' "));
                        List<Supplementaryemployeedeductionstransactions> supDedList = supDedCrit.list();
                        if (supDedList.size() > 0) {
                            transaction = session.beginTransaction();
                            Supplementaryemployeedeductionstransactions supplementaryemployeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) supDedList.get(0);
                            System.out.println("epf amount = " + epf);
                            supplementaryemployeedeductionstransactionsObj.setAmount(epf);
                            supplementaryemployeedeductionstransactionsObj.setCancelled(Boolean.FALSE);
                            session.update(supplementaryemployeedeductionstransactionsObj);
                            transaction.commit();

                        }
                    }

                }

            }
        }
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBatchEmployeeList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String batchid) {
        Map resultMap = new HashMap();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Criteria daCrit = session.createCriteria(Daarrear.class);
        daCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        daCrit.add(Restrictions.sqlRestriction("active is true"));
        List daList = daCrit.list();
        if (daList.size() > 0) {
            Daarrear daarrearObj = (Daarrear) daList.get(0);

            resultMap.put("daname", daarrearObj.getName());

            StringBuilder sb = new StringBuilder();
//            sb.append("select  spb.employeeprovidentfundnumber, em.employeename, em.designation, em.section, spb.id from supplementatypaybill spb ");
//            sb.append("left join dabatchdetails dab on dab.id=spb.dabatch ");
//            sb.append("left join daarrear da on da.id=daarrear  ");
//            sb.append("left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber ");
//            sb.append("where ");
//            sb.append("spb.dabatch='" + batchid.trim() + "' ");
//            sb.append("and type='DAARREAR' ");
//            sb.append("and  da.active is true ");
//            sb.append("and spb.accregion='" + LoggedInRegion + "' ");
//            sb.append("and da.accregion='" + LoggedInRegion + "' ");
//            sb.append("and dab.accregion='" + LoggedInRegion + "' ");
//            sb.append("and spb.cancelled is false ");
//            sb.append("and da.cancelled is false ");
//            sb.append("and dab.cancelled is false ");
//            sb.append("and em.region='" + LoggedInRegion + "' order by em.epfno");

            sb.append("select spb.employeeprovidentfundnumber, em.employeename, em.designation, em.section, spb.id as supplementarypaybillid, ");
            sb.append("dad.id as daarrerdiffid, dad.month, dad.year, dad.type from daarreardiffentry dad ");
            sb.append("left join supplementatypaybill spb on spb.id=dad.supplementarypayroll ");
            sb.append("left join employeemaster em on em.epfno=spb.employeeprovidentfundnumber ");
            sb.append("where ");
            sb.append("spb.cancelled is false ");
            sb.append("and spb.type='DAARREAR' ");
            sb.append("and spb.dabatch='" + batchid.trim() + "' ");
            sb.append("and spb.accregion='" + LoggedInRegion + "' ");
            sb.append("order by spb.employeeprovidentfundnumber, dad.month, dad.year,dad.id ");

//            System.out.println("query ->" + sb.toString());

            SQLQuery query = session.createSQLQuery(sb.toString());

            StringBuffer stringBuff = new StringBuffer();

            stringBuff.append("<FONT SIZE=2>");
            stringBuff.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"employeetable\">");
            stringBuff.append("<thead>");
            stringBuff.append("<tr>");
            stringBuff.append("<th width=\"10%\">Epf No</th>");
            stringBuff.append("<th width=\"25%\">Name</th>");
            stringBuff.append("<th width=\"20%\">Designation</th>");
            stringBuff.append("<th width=\"20%\">Section</th>");
            stringBuff.append("<th width=\"13%\">Type</th>");
            stringBuff.append("<th width=\"7%\">Period</th>");
            stringBuff.append("<th width=\"5%\">Modification</th>");
            stringBuff.append("</tr>");
            stringBuff.append("</thead>");
            stringBuff.append("<tbody>");

            String epfno = null;
            String employeename = null;
            String designation = null;
            String section = null;
            String supplementarypaybillid = null;
            String daarreardiffid = null;
            String period = null;
            String type = null;

            if (query.list().size() > 0) {
                for (ListIterator its = query.list().listIterator(); its.hasNext();) {
                    // <editor-fold defaultstate="collapsed" desc="Voucher">
                    Object[] rows = (Object[]) its.next();
                    epfno = (String) rows[0];
                    employeename = (String) rows[1];
                    designation = (String) rows[2];
                    section = (String) rows[3];
                    supplementarypaybillid = (String) rows[4];
                    daarreardiffid = (String) rows[5];
                    period = months[(Integer.valueOf(rows[6].toString())) - 1] + " - " + rows[7].toString();
                    type = (String) rows[8];

                    //                    System.out.println("daarreardiffid = " + daarreardiffid);
                    //                    System.out.println("supplementarypaybillid = " + supplementarypaybillid);

                    stringBuff.append("<tr >");
                    stringBuff.append("<td >" + epfno + "</td>");
                    stringBuff.append("<td >" + employeename + "</td>");
                    stringBuff.append("<td >" + getDesignationMater(session, designation).getDesignation() + "</td>");
                    stringBuff.append("<td >" + getSection(session, section).getSectionname() + "</td>");
                    stringBuff.append("<td >" + type + "</td>");
                    stringBuff.append("<td >" + period + "</td>");
                    stringBuff.append("<td ><input type=\"radio\" name=\"daepfno\" id=\"" + "daepfno" + batchid + "\" onclick=\"modifyDADetails('" + epfno + "','" + batchid + "','" + daarreardiffid + "')\"></td>");
                    stringBuff.append("</tr>");
                    //</editor-fold>
                }
            }
            stringBuff.append("</tbody>");
            stringBuff.append("</table>");
            stringBuff.append("</FONT>");
            resultMap.put("employeelist", stringBuff.toString());
        } else {
            resultMap.put("employeelist", "");
            resultMap.put("daname", "");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDaIncrementArrearManualFormDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String batchid, String daareardiffid) {
        System.out.println("******************** DAIncrementArrearServiceImpl Class getDaIncrementArrearManualFormDetails method is calling ******************");
        Map resultMap = new HashMap();
        try {
            Criteria daCrit = session.createCriteria(Daarreardiffentry.class);
            daCrit.add(Restrictions.sqlRestriction("id='" + daareardiffid + "'"));
            List daList = daCrit.list();
            System.out.println("daList.size() = " + daList.size());
            if (daList.size() > 0) {
                Daarreardiffentry daarreardiffentry = (Daarreardiffentry) daList.get(0);
                String epfnobatchno = epfno + " / " + batchid;
                resultMap.put("epfnobatchno", epfnobatchno);
                resultMap.put("damanualmonth", daarreardiffentry.getMonth());
                resultMap.put("damanualyear", daarreardiffentry.getYear());
                String billtype = daarreardiffentry.getType();
                if (billtype.equalsIgnoreCase("WRONGENTRY")) {
                    resultMap.put("billtype", "0");
                } else if (billtype.equalsIgnoreCase("SUPPLEMENTARY")) {
                    resultMap.put("billtype", "1");
                } else if (billtype.equalsIgnoreCase("LEAVESURRENDER")) {
                    resultMap.put("billtype", "2");
                } else if (billtype.equalsIgnoreCase("INCREMENTARREAR")) {
                    resultMap.put("billtype", "3");
                }
                resultMap.put("oldda", "");
                resultMap.put("newda", "");
                resultMap.put("basicamount", daarreardiffentry.getBasic());
                resultMap.put("perpayamt", daarreardiffentry.getPerpay());
                resultMap.put("gradepayamt", daarreardiffentry.getGradepay());
                resultMap.put("dueamt", "");
                resultMap.put("drawnamt", "");
                resultMap.put("arrearamt", "");
                resultMap.put("epfamt", "");
                resultMap.put("epfno", epfno);
                resultMap.put("batchid", batchid);
                resultMap.put("daareardiffid", daareardiffid);

                resultMap.put("ERROR", null);
            } else {
                resultMap.put("ERROR", "No Records for Selection!");
            }
        } catch (Exception ex) {
            resultMap.put("ERROR", "Get DA Increment Arrear Manual Problem!");
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map DaManualFormUpdation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,
            String supplementarypaybillid,
            String dmonthid,
            String dyearid,
            String basicamount,
            String perpayamt,
            String gradepayamt,
            String dueamt,
            String drawnamt,
            String arrearamt,
            String epfamt,
            String billtype,
            String epfno,
            String batchid,
            String daareardiffid) {
        System.out.println("******************** DAIncrementArrearServiceImpl Class DaManualFormUpdation method is calling ******************");
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Criteria daCrit = session.createCriteria(Daarreardiffentry.class);
            daCrit.add(Restrictions.sqlRestriction("id='" + daareardiffid + "'"));
            List daList = daCrit.list();
            System.out.println("daList.size() = " + daList.size());
            if (daList.size() > 0) {
                Daarreardiffentry daarreardiffentry = (Daarreardiffentry) daList.get(0);
                String subpaybillid = daarreardiffentry.getSupplementarypayroll();

                daarreardiffentry.setMonth(Integer.parseInt(dmonthid));
                daarreardiffentry.setYear(Integer.parseInt(dyearid));

                if (basicamount.trim().length() > 0) {
                    daarreardiffentry.setBasic(new BigDecimal(basicamount.trim()));
                } else {
                    daarreardiffentry.setBasic(BigDecimal.ZERO);
                }
                if (perpayamt.trim().length() > 0) {
                    daarreardiffentry.setPerpay(new BigDecimal(perpayamt.trim()));
                } else {
                    daarreardiffentry.setPerpay(BigDecimal.ZERO);
                }
                if (gradepayamt.trim().length() > 0) {
                    daarreardiffentry.setGradepay(new BigDecimal(gradepayamt.trim()));
                } else {
                    daarreardiffentry.setGradepay(BigDecimal.ZERO);
                }
                if (dueamt.trim().length() > 0) {
                    daarreardiffentry.setDue(new BigDecimal(dueamt.trim()));
                } else {
                    daarreardiffentry.setDue(BigDecimal.ZERO);
                }
                if (drawnamt.trim().length() > 0) {
                    daarreardiffentry.setDrawn(new BigDecimal(drawnamt.trim()));
                } else {
                    daarreardiffentry.setDrawn(BigDecimal.ZERO);
                }
                if (arrearamt.trim().length() > 0) {
                    daarreardiffentry.setArrear(new BigDecimal(arrearamt.trim()));
                } else {
                    daarreardiffentry.setArrear(BigDecimal.ZERO);
                }
                if (epfamt.trim().length() > 0) {
                    daarreardiffentry.setEpf(new BigDecimal(epfamt.trim()));
                } else {
                    daarreardiffentry.setEpf(BigDecimal.ZERO);
                }

                if (billtype.equalsIgnoreCase("1")) {
                    daarreardiffentry.setType("SUPPLEMENTARY");
                } else if (billtype.equalsIgnoreCase("2")) {
                    daarreardiffentry.setType("LEAVESURRENDER");
                } else if (billtype.equalsIgnoreCase("3")) {
                    daarreardiffentry.setType("INCREMENTARREAR");
                }
                session.update(daarreardiffentry);
                transaction.commit();

                String arrearStr = "SELECT sum(coalesce(arrear,'0')) FROM daarreardiffentry where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and supplementarypayroll='" + subpaybillid + "'";
                String epfStr = "SELECT sum(coalesce(epf,'0')) FROM daarreardiffentry where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and supplementarypayroll='" + subpaybillid + "'";

                BigDecimal arear = new BigDecimal(0.00);
                List arearList = (ArrayList) session.createSQLQuery(arrearStr).list();
                if (arearList.size() > 0) {
                    arear = (BigDecimal) arearList.get(0);
                } else {
                    arear = new BigDecimal(0.00);
                }

                BigDecimal epf = new BigDecimal(0.00);
                List epfList = (ArrayList) session.createSQLQuery(epfStr).list();
                if (epfList.size() > 0) {
                    epf = (BigDecimal) epfList.get(0);
                } else {
                    epf = new BigDecimal(0.00);
                }

                String arrearStr1 = "SELECT sum(coalesce(arrear,'0')) FROM incrementarrearreference where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and suppaybillid='" + subpaybillid + "'";
                String epfStr1 = "SELECT sum(coalesce(epf,'0')) FROM incrementarrearreference where month=" + Integer.parseInt(dmonthid) + " and year=" + Integer.parseInt(dyearid) + " and suppaybillid='" + subpaybillid + "'";

                BigDecimal arear1 = new BigDecimal(0.00);
                List arearList1 = (ArrayList) session.createSQLQuery(arrearStr1).list();
                if (arearList1.size() > 0) {
                    arear1 = (BigDecimal) arearList1.get(0);
                } else {
                    arear1 = new BigDecimal(0.00);
                }

                BigDecimal epf1 = new BigDecimal(0.00);
                List epfList1 = (ArrayList) session.createSQLQuery(epfStr1).list();
                if (epfList1.size() > 0) {
                    epf1 = (BigDecimal) epfList1.get(0);
                } else {
                    epf1 = new BigDecimal(0.00);
                }
                System.out.println("result");
                System.out.println(arear);
                System.out.println(arear1);
                System.out.println("epf = " + epf);
                System.out.println("epf1 = " + epf1);
                if (arear1 == null) {
                    arear1 = new BigDecimal(0.00);
                }
                if (epf1 == null) {
                    epf1 = new BigDecimal(0.00);
                }

                if (arear == null) {
                    arear = new BigDecimal(0.00);
                }
                if (epf == null) {
                    epf = new BigDecimal(0.00);
                }

                updateDaManual(session, subpaybillid, dmonthid, dyearid, arear.add(arear1), epf.add(epf1));

                resultMap.put("success", "successfully saved");

                resultMap.put("ERROR", null);
            } else {
                resultMap.put("ERROR", "No Records for Selection!");
            }
        } catch (Exception ex) {
            resultMap.put("ERROR", "DA Increment Arrear Manual Update Problem!");
            ex.printStackTrace();
        }
        return resultMap;
    }
}
