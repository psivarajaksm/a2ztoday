/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.persistence.payroll.Earningslapdetails;
import com.onward.persistence.payroll.Paycodemaster;
import com.onward.persistence.payroll.Payrollprocessing;
import com.onward.persistence.payroll.Professionaltaxslap;
import com.onward.persistence.payroll.Regionmaster;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class EarningSlapDetailsServiceImpl extends OnwardAction implements EarningSlapDetailsService {

    String classname = "";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoadEarningsTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map earningsMap = new LinkedHashMap();
        earningsMap.put("0", "--Select--");
        String paycode = "";
        String paycodename = "";
//        System.out.println("LoggedInRegion===="+LoggedInRegion);
        try {
            Criteria ernCrit = session.createCriteria(Paycodemaster.class);
            ernCrit.add(Restrictions.sqlRestriction("paycodetype in ('E','D')"));
            ernCrit.addOrder(Order.asc("paycodename"));
            List<Paycodemaster> ernList = ernCrit.list();
            resultMap = new TreeMap();
            for (Paycodemaster lbobj : ernList) {
                paycode = lbobj.getPaycode();
                paycodename = lbobj.getPaycodename();
                earningsMap.put(paycode, paycodename);
            }
            resultMap.put("earningsList", earningsMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoadDedutionTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map earningsMap = new LinkedHashMap();
        earningsMap.put("0", "--Select--");
        Map regionMap = new LinkedHashMap();
//        regionMap.put("0", "--Select--");
        String paycode = "";
        String regioncode = "";
        String paycodename = "";
        String regionname = "";

        try {
            Criteria ernCrit = session.createCriteria(Paycodemaster.class);
            ernCrit.add(Restrictions.sqlRestriction("paycode='L31'"));
            ernCrit.addOrder(Order.asc("paycodename"));
            List<Paycodemaster> ernList = ernCrit.list();
            resultMap = new TreeMap();
            for (Paycodemaster lbobj : ernList) {
                paycode = lbobj.getPaycode();
                paycodename = lbobj.getPaycodename();
                earningsMap.put(paycode, paycodename);
            }
            resultMap.put("earningsList", earningsMap);

            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.sqlRestriction("id='"+LoggedInRegion+"'"));
            rgnCrit.addOrder(Order.asc("regionname"));
            List<Regionmaster> rgnList = rgnCrit.list();            
            for (Regionmaster lbobj1 : rgnList) {
                regioncode = lbobj1.getId();
                regionname = lbobj1.getRegionname();
                regionMap.put(regioncode, regionname);
            }
            resultMap.put("regionList", regionMap);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map createRowinHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String noofrows) {
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        
        try {
            resultHTML.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">")
                        .append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">")
                        .append("<tr class=\"gridmenu\">")
                        .append("<td>S.No</td>")
                        .append("<td>Amount Range From</td>")
                        .append("<td>Amount Range To</td>")
                        .append("<td>Amount</td>")
                        .append("<td>Percentage</td>")
                        .append("</tr>");
            for(int i=1;i<=Integer.parseInt(noofrows.toString());i++){
                if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                resultHTML.append("<tr class=\"" + classname + "\">")
                        .append("<td align=\"center\">" + i + "</td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"amountrangefrom"+i+"\"  onblur=\"checkFloat(this.id,'Amount Range From');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"amountrangeto"+i+"\"  onblur=\"checkFloat(this.id,'Amount Range To');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"slapamount"+i+"\"  onblur=\"checkFloat(this.id,'Salp Amount');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"slappercentage"+i+"\"  value=\"0\" onblur=\"checkPercentage(this.id,'Slap Percentage');\" size=\"20\"/>&nbsp;&nbsp;%</td>")
                        .append("</tr>");

            }
            resultHTML.append("</table>");
            resultHTML.append("</td></tr>");
            resultHTML.append("</table>");

            resultMap.put("createrows", resultHTML.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDatasForModify(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String earningcode) {
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        String orderNo="";
        String effectDate="";
        try {
            resultHTML.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">")
                        .append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">")
                        .append("<tr class=\"gridmenu\">")
                        .append("<td>S.No</td>")
                        .append("<td>Amount Range From</td>")
                        .append("<td>Amount Range To</td>")
                        .append("<td>Amount</td>")
                        .append("<td>Percentage</td>")
                        .append("</tr>");

            Criteria ernCrit = session.createCriteria(Earningslapdetails.class);
            ernCrit.add(Restrictions.sqlRestriction("earningcode='"+earningcode+"'"));
            ernCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            ernCrit.addOrder(Order.asc("amount"));
            ernCrit.addOrder(Order.asc("percentage"));
            List ernList = ernCrit.list();
//            System.out.println("=="+ernList.size());
            for(int i=0;i<ernList.size();i++){
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Earningslapdetails earObje = (Earningslapdetails) ernList.get(i);
                    orderNo=earObje.getOrderno();
                    effectDate=dateToString(earObje.getPeriodfrom());
                    resultHTML.append("<tr class=\"" + classname + "\">")
                        .append("<td align=\"center\">" + (i + 1) + "</td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getAmountrangefrom()+"\" id=\"amountrangefrom"+(i + 1)+"\"  onblur=\"checkFloat(this.id,'Amount Range From');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getAmountrangeto()+"\"  id=\"amountrangeto"+(i + 1)+"\"  onblur=\"checkFloat(this.id,'Amount Range To');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getAmount()+"\"  id=\"slapamount"+(i + 1)+"\"  onblur=\"checkFloat(this.id,'Salp Amount');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getPercentage()+"\"  id=\"slappercentage"+(i + 1)+"\"  value=\"0\" onblur=\"checkPercentage(this.id,'Slap Percentage');\" size=\"20\"/>&nbsp;&nbsp;%<input type=\"hidden\" id=\"hiddedid"+(i + 1)+"\" value=\""+earObje.getId()+"\"></td>")
                        .append("</tr>");
            }
            resultHTML.append("</table>");
            resultHTML.append("</td></tr>");
            resultHTML.append("</table>");

            resultMap.put("orderNo", orderNo);
            resultMap.put("effectDate", effectDate);
            resultMap.put("totalrows", ernList.size());
            resultMap.put("createrows", resultHTML.toString());
        }catch(Exception e){

        }
        return resultMap;
    }
    
    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPTDatasForModify(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductioncode) {
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        String orderNo="";
        String effectDate="";
        try {
            resultHTML.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">")
                        .append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">")
                        .append("<tr class=\"gridmenu\">")
                        .append("<td>S.No</td>")
                        .append("<td>Amount Range From</td>")
                        .append("<td>Amount Range To</td>")
                        .append("<td>Amount</td>")
                        .append("<td>Percentage</td>")
                        .append("</tr>");

            Criteria ernCrit = session.createCriteria(Professionaltaxslap.class);
            ernCrit.add(Restrictions.sqlRestriction("deductioncode='"+deductioncode+"'"));
            ernCrit.add(Restrictions.sqlRestriction("regioncode='"+LoggedInRegion+"'"));
            ernCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            ernCrit.addOrder(Order.asc("amount"));
            ernCrit.addOrder(Order.asc("percentage"));
            List ernList = ernCrit.list();
//            System.out.println("=="+ernList.size());
            for(int i=0;i<ernList.size();i++){
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Professionaltaxslap earObje = (Professionaltaxslap) ernList.get(i);
                    orderNo=earObje.getOrderno();
                    effectDate=dateToString(earObje.getPeriodfrom());
                    resultHTML.append("<tr class=\"" + classname + "\">")
                        .append("<td align=\"center\">" + (i + 1) + "</td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getAmountrangefrom()+"\" id=\"amountrangefrom"+(i + 1)+"\"  onblur=\"checkFloat(this.id,'Amount Range From');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getAmountrangeto()+"\"  id=\"amountrangeto"+(i + 1)+"\"  onblur=\"checkFloat(this.id,'Amount Range To');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getAmount()+"\"  id=\"slapamount"+(i + 1)+"\"  onblur=\"checkFloat(this.id,'Salp Amount');\" size=\"20\"/></td>")
                        .append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\""+earObje.getPercentage()+"\"  id=\"slappercentage"+(i + 1)+"\"  value=\"0\" onblur=\"checkPercentage(this.id,'Slap Percentage');\" size=\"20\"/>&nbsp;&nbsp;%<input type=\"hidden\" id=\"hiddedid"+(i + 1)+"\" value=\""+earObje.getId()+"\"></td>")
                        .append("</tr>");
            }
            resultHTML.append("</table>");
            resultHTML.append("</td></tr>");
            resultHTML.append("</table>");

            resultMap.put("orderNo", orderNo);
            resultMap.put("effectDate", effectDate);
            resultMap.put("totalrows", ernList.size());
            resultMap.put("createrows", resultHTML.toString());
        }catch(Exception e){

        }
        return resultMap;
    }
    

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEarningsSlapDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String earningstypes, String effectdate, String[] amountrangefrom, String[] amountrangeto, String[] slapamount, String[] slappercentage, String orderno, String totalrows, String[] hiddeniarray, String funtype) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            if (funtype.equalsIgnoreCase("1")) {
                Criteria ernCrit = session.createCriteria(Earningslapdetails.class);
                ernCrit.add(Restrictions.sqlRestriction("earningcode='" + earningstypes + "'"));
                ernCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List<Earningslapdetails> ernList = ernCrit.list();
                for (Earningslapdetails lbobj : ernList) {
                    transaction = session.beginTransaction();
                    lbobj.setCancelled(Boolean.TRUE);
                    lbobj.setPeriodto(postgresDate(effectdate));
                    session.update(lbobj);
                    transaction.commit();

                }
                for (int i = 1; i <= Integer.parseInt(totalrows.toString()); i++) {
                    transaction = session.beginTransaction();
                    Earningslapdetails masterobj = new Earningslapdetails();
                    masterobj.setId(getmaxSequenceNumber(session));
                    masterobj.setEarningcode(earningstypes);
                    masterobj.setPeriodfrom(postgresDate(effectdate));
                    masterobj.setAmount(StringtobigDecimal(slapamount[i]));
                    masterobj.setAmountrangefrom(StringtobigDecimal(amountrangefrom[i]));
                    masterobj.setAmountrangeto(StringtobigDecimal(amountrangeto[i]));
                    masterobj.setPercentage(StringtobigDecimal(slappercentage[i]));
                    masterobj.setOrderno(orderno);
                    masterobj.setCancelled(Boolean.FALSE);
                    session.save(masterobj);
                    transaction.commit();
                }
            }else{
                for (int i = 1; i <= Integer.parseInt(totalrows.toString()); i++) {
                    transaction = session.beginTransaction();
                    Earningslapdetails masterobj = new Earningslapdetails();
                    masterobj.setId(hiddeniarray[i]);
                    masterobj.setEarningcode(earningstypes);
                    masterobj.setPeriodfrom(postgresDate(effectdate));
                    masterobj.setAmount(StringtobigDecimal(slapamount[i]));
                    masterobj.setAmountrangefrom(StringtobigDecimal(amountrangefrom[i]));
                    masterobj.setAmountrangeto(StringtobigDecimal(amountrangeto[i]));
                    masterobj.setPercentage(StringtobigDecimal(slappercentage[i]));
                    masterobj.setOrderno(orderno);
                    masterobj.setCancelled(Boolean.FALSE);
                    session.update(masterobj);
                    transaction.commit();
                }
            }

            resultMap.put("success", "Slap Master Successfully Saved");

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map savePTSlapDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductioncode, String effectdate, String[] amountrangefrom, String[] amountrangeto, String[] slapamount, String[] slappercentage, String orderno, String totalrows, String[] hiddeniarray, String funtype) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            if (funtype.equalsIgnoreCase("1")) {
                Criteria ernCrit = session.createCriteria(Professionaltaxslap.class);
                ernCrit.add(Restrictions.sqlRestriction("deductioncode='" + deductioncode + "'"));
                ernCrit.add(Restrictions.sqlRestriction("regioncode='"+LoggedInRegion+"'"));
                ernCrit.add(Restrictions.sqlRestriction("cancelled is false"));
                List<Professionaltaxslap> ernList = ernCrit.list();
                for (Professionaltaxslap lbobj : ernList) {
                    transaction = session.beginTransaction();
                    lbobj.setCancelled(Boolean.TRUE);
                    lbobj.setPeriodto(postgresDate(effectdate));
                    session.update(lbobj);
                    transaction.commit();

                }
                for (int i = 1; i <= Integer.parseInt(totalrows.toString()); i++) {
                    transaction = session.beginTransaction();
                    Professionaltaxslap masterobj = new Professionaltaxslap();
                    masterobj.setId(getPTmaxSequenceNumber(session));
                    masterobj.setDeductioncode(deductioncode);
                    masterobj.setPeriodfrom(postgresDate(effectdate));
                    masterobj.setAmount(StringtobigDecimal(slapamount[i]));
                    masterobj.setAmountrangefrom(StringtobigDecimal(amountrangefrom[i]));
                    masterobj.setAmountrangeto(StringtobigDecimal(amountrangeto[i]));
                    masterobj.setPercentage(StringtobigDecimal(slappercentage[i]));
                    masterobj.setOrderno(orderno);
                    masterobj.setCancelled(Boolean.FALSE);
                    masterobj.setRegioncode(LoggedInRegion);
                    session.save(masterobj);
                    transaction.commit();
                }
            }else{
                for (int i = 1; i <= Integer.parseInt(totalrows.toString()); i++) {
                    transaction = session.beginTransaction();
                    Professionaltaxslap masterobj = new Professionaltaxslap();
                    masterobj.setId(hiddeniarray[i]);
                    masterobj.setDeductioncode(deductioncode);
                    masterobj.setPeriodfrom(postgresDate(effectdate));
                    masterobj.setAmount(StringtobigDecimal(slapamount[i]));
                    masterobj.setAmountrangefrom(StringtobigDecimal(amountrangefrom[i]));
                    masterobj.setAmountrangeto(StringtobigDecimal(amountrangeto[i]));
                    masterobj.setPercentage(StringtobigDecimal(slappercentage[i]));
                    masterobj.setOrderno(orderno);
                    masterobj.setCancelled(Boolean.FALSE);
                    masterobj.setRegioncode(LoggedInRegion);
                    session.update(masterobj);
                    transaction.commit();
                }
            }

            resultMap.put("success", "Professtion Tax Slap Master Successfully Saved");

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return resultMap;
    }

    public String getmaxSequenceNumber(Session session) {
        int maxSequenceNumber = 1;
        
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(id as int)) as maxsequencenumber from earningslapdetails");
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
    public String getPTmaxSequenceNumber(Session session) {
        int maxSequenceNumber = 1;

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(id as int)) as maxsequencenumber from professionaltaxslap");
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
    
    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveStopProcess(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String processtype, String processmonth, String processyear) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            if (processtype.equalsIgnoreCase("1")) {
                Criteria processCrit = session.createCriteria(Payrollprocessing.class);
                processCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
                processCrit.add(Restrictions.sqlRestriction("month=" + Integer.parseInt(processmonth.toString())));
                processCrit.add(Restrictions.sqlRestriction("year=" + Integer.parseInt(processyear.toString())));
                List<Payrollprocessing> processList = processCrit.list();
                System.out.println("processList=="+processList.size());
                for (Payrollprocessing lbobj : processList) {
                     if (lbobj.getIsopen()) {
                        transaction = session.beginTransaction();
                        lbobj.setIsopen(Boolean.FALSE);    
                        lbobj.setAccregion(LoggedInRegion);
                        lbobj.setRegionmaster(CommonUtility.getRegion(session, LoggedInRegion));
                        session.update(lbobj);
                        transaction.commit();
                        resultMap.put("success", "Pay Roll Process Successfully Stopped");
                    } else {
                        resultMap.put("ERROR", "Already Processed and closed");

                    }                    
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

}
