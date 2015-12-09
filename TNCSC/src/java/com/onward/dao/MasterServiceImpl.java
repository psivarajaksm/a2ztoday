/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.DateParser;
import com.onward.persistence.payroll.Ccahra;
import com.onward.persistence.payroll.Paycodemaster;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.persistence.payroll.Sectionmaster;
import com.onward.persistence.payroll.Designationmaster;
import com.onward.persistence.payroll.Earningslapdetails;
import com.onward.persistence.payroll.Employeedeductionaccountcode;
import com.onward.persistence.payroll.Employeemaster;
import com.onward.persistence.payroll.Stoppayrolldetails;
import com.tncscpayroll.transferobjects.CcahraMasterValueObjectModel;
import com.tncscpayroll.transferobjects.DesignationMasterValueObjectModel;
import com.tncscpayroll.transferobjects.EarningsSlapDetailsValueObjectModel;
import com.tncscpayroll.transferobjects.PaycodeMasterValueObjectModel;
import com.tncscpayroll.transferobjects.RegionMasterValueObjectModel;
import com.tncscpayroll.transferobjects.SectionMasterValueObjectModel;
import com.tncscpayroll.transferobjects.masterDetails;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class MasterServiceImpl extends OnwardAction implements MasterService {

    String classname = "";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getPayCodeSerial(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String paycodetype, String orderbyString) {
        Map resultMap = new HashMap();
        String paycode = "";
        int incValue = 1;
        try {
            String strPay = " SELECT max(cast(REPLACE(TRANSLATE(paycode, REPLACE(TRANSLATE(paycode,'0123456789', RPAD('#',LENGTH(paycode),'#')),'#',''), "
                    + " RPAD('#',LENGTH(paycode),'#')),'#','') as INTEGER)) AS foo FROM paycodemaster where paycodetype='" + paycodetype + "' ";
            //Query query = session.createSQLQuery("select max(paycode) from paycodemaster where paycodetype='" + paycodetype + "'");
            Query query = session.createSQLQuery(strPay);
            List payList = query.list();
            System.out.println("Sairam Size is " + payList.size());
            if (payList.size() > 0 && payList.get(0) != null) {
                paycode = payList.get(0).toString();
                incValue = Integer.parseInt(paycode) + 1;
//                if (String.valueOf(incValue).length() == 1) {
//                    resultMap.put("paycode", paycodetype + "0" + incValue);
//                } else {
//                    resultMap.put("paycode", paycodetype + "" + incValue);
//                }
            }
            resultMap.put("paycode", paycodetype + incValue);
            resultMap.put("paycodeDetails", getPayCodeDetailsinHTML(paycodetype, session, orderbyString).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map savePayCodeMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String paycodetype, String paycode, String paycodename, String paypercentage) {
        Map resultMap = new HashMap();
        Transaction transaction = null;

//        System.out.println(""+epfno+"=="+fpfno+"=="+employeename+"=="+fathername+"=="+
//                                    gender+"=="+dateofbirth+"=="+region+"=="+section+"=="+dateofappoinment+"=="+dateofprobation+"=="+dateofconfirmation+"=="+designation+"=="+
//                                    community+"=="+pancardno+"=="+paymentmode+"=="+bankcode+"=="+banksbaccount);
//

        try {
            transaction = session.beginTransaction();
            Paycodemaster masterobj = new Paycodemaster();
            masterobj.setPaycode(paycode);
            masterobj.setPaycodename(paycodename.toUpperCase());
            masterobj.setPaycodetype(paycodetype.toCharArray()[0]);
            masterobj.setPaypercentage(BigDecimal.valueOf(Long.parseLong(checkNullReplaceZero(paypercentage))));
            session.saveOrUpdate(masterobj);
            transaction.commit();
            resultMap.put("paycodeDetails", getPayCodeDetailsinHTML(paycodetype, session, "paycode").toString());
            resultMap.put("success", "PayCode Master Successfully Saved");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            resultMap.put("ERROR", "PayCode Master Transaction Faild");
        }
        return resultMap;
    }

    public String getRegionCodeSerial(Session session) {
        String regionCodeSerial = "";
        String regiontype = "R";
        String regioncode = "";
        int incValue = 1;

        try {

            Query query = session.createSQLQuery("select max(id) from regionmaster  where id like '" + regiontype + "%'");
            List regList = query.list();
//            System.out.println("regList==" + regList.size());
            if (regList.size() > 0 && regList.get(0) != null) {
                regioncode = regList.get(0).toString();
                incValue = Integer.parseInt(regioncode.split(regiontype)[1]) + 1;
                if (String.valueOf(incValue).length() == 1) {
                    regionCodeSerial = regiontype + "0" + incValue;

                } else {
                    regionCodeSerial = regiontype + "" + incValue;
                }
            } else {
                regionCodeSerial = regiontype + "0" + incValue;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return regionCodeSerial;
    }
//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map getRegionCodeSerial(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regiontype) {
//        Map resultMap = new HashMap();
//        String regioncode = "";
//        int incValue = 1;
//
//        try {
//
//            Query query = session.createSQLQuery("select max(id) from regionmaster  where id like '" + regiontype + "%'");
//            List regList = query.list();
////            System.out.println("regList==" + regList.size());
//            if (regList.size() > 0 && regList.get(0) != null) {
//                regioncode = regList.get(0).toString();
//                incValue = Integer.parseInt(regioncode.split(regiontype)[1]) + 1;
//                if (String.valueOf(incValue).length() == 1) {
//                    resultMap.put("regioncode", regiontype + "0" + incValue);
//                } else {
//                    resultMap.put("regioncode", regiontype + "" + incValue);
//                }
//            } else {
//                resultMap.put("regioncode", regiontype + "0" + incValue);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultMap;
//    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveRegionMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regioncode, String regionname) {
        Map resultMap = new HashMap();
        try {
            Transaction transaction = session.beginTransaction();
            Regionmaster masterobj = new Regionmaster();
            masterobj.setId(regioncode);
            masterobj.setRegionname(regionname.toUpperCase());
            session.saveOrUpdate(masterobj);
            transaction.commit();
            resultMap.put("regionDetails", getRegionDetailsinHTML(session).toString());
            resultMap.put("regioncode", getRegionCodeSerial(session));
            resultMap.put("success", "Region Master Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Region Master Transaction Faild");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveSectionMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String parentcode, String sectioncode, String sectionname) {
        Map resultMap = new HashMap();
        try {
            Transaction transaction = session.beginTransaction();
//            if (parentcode.equalsIgnoreCase("0")) {
//                Sectionmaster masterobj = new Sectionmaster();
//                masterobj.setId(sectioncode);
//                masterobj.setSectionname(sectionname.toUpperCase());
//                masterobj.setParentcode("0");
//                masterobj.setRegion(region);
//                session.saveOrUpdate(masterobj);
//                transaction.commit();
//            } else {
            Sectionmaster masterobj = new Sectionmaster();
            masterobj.setId(sectioncode);
            masterobj.setSectionname(sectionname.toUpperCase());
            masterobj.setParentcode(parentcode);
            masterobj.setRegion(region);
            session.saveOrUpdate(masterobj);
            transaction.commit();
//            }

//            Sectionmaster masterobj = new Sectionmaster();
//            masterobj.setId(sectioncode);
//            masterobj.setSectionname(sectionname.toUpperCase());
//            session.saveOrUpdate(masterobj);
//            transaction.commit();

            resultMap.put("sectionDetails", getSectionDetailsinHTML(session, region, parentcode).toString());
            resultMap.put("sectioncode", getSectionCodeSerialNo(session));
            resultMap.put("success", "Section Master Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Section Master Transaction Faild");
        }
        return resultMap;
    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map getSectionCodeSerial(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String sectiontype) {
//        Map resultMap = new HashMap();
//        String sectioncode = "";
//        int incValue = 1;
//
//        try {
//
//            Query query = session.createSQLQuery("select max(id) from sectionmaster");
//            List secList = query.list();
////            System.out.println("secList==" + secList.size());
//            if (secList.size() > 0 && secList.get(0) != null) {
//                sectioncode = secList.get(0).toString();
//                incValue = Integer.parseInt(sectioncode.split(sectiontype)[1]) + 1;
//                if (String.valueOf(incValue).length() == 1) {
//                    resultMap.put("sectioncode", sectiontype + "0" + incValue);
//                } else {
//                    resultMap.put("sectioncode", sectiontype + "" + incValue);
//                }
//
//            } else {
//                resultMap.put("sectioncode", sectiontype + "0" + incValue);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultMap;
//    }
//
//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map saveSectionMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String sectioncode, String sectionname) {
//        Map resultMap = new HashMap();
//        Transaction transaction=null;
//        try {
//            transaction = session.beginTransaction();
//            Sectionmaster masterobj = new Sectionmaster();
//            masterobj.setId(sectioncode);
//            masterobj.setSectionname(sectionname.toUpperCase());
//            session.saveOrUpdate(masterobj);
//            transaction.commit();
//
//            resultMap.put("regionandsectionDetails", getRegionAndSectionDetailsinHTML(session).toString());
//            resultMap.put("success", "Section Master Successfully Saved");
//        } catch (Exception e) {
//            e.printStackTrace();
//            if(transaction != null)  transaction.rollback();
//            resultMap.put("ERROR", "Section Master Transaction Faild");
//        }
//        return resultMap;
//    }
    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDesignationCodeSerial(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String cadretype, String wingtype) {
        Map resultMap = new HashMap();
        String sectioncode = "";
        int incValue = 1;
        String destype = cadretype + "" + wingtype;
        try {

            Query query = session.createSQLQuery("select max(designationcode) from designationmaster where designationcode like '" + destype + "%'");
            List desList = query.list();
            if (desList.size() > 0 && desList.get(0) != null) {
                sectioncode = desList.get(0).toString();
                incValue = Integer.parseInt(sectioncode.split(destype)[1]) + 1;

                if (String.valueOf(incValue).length() == 1) {
                    resultMap.put("designationcode", destype + "0" + incValue);
                } else {
                    resultMap.put("designationcode", destype + "" + incValue);
                }


            } else {
                resultMap.put("designationcode", destype + "0" + incValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveDesignationMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String designationcode, String designationname, String payscalecode, String orderno) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Designationmaster masterobj = new Designationmaster();
            masterobj.setDesignationcode(designationcode);
            masterobj.setDesignation(designationname.toUpperCase());
            masterobj.setPayscalecode(payscalecode);
            masterobj.setOrderno(orderno);
            session.saveOrUpdate(masterobj);
            transaction.commit();
            resultMap.put("designationDetails", getDesignationDetailsinHTML(session).toString());
            resultMap.put("success", "Section Master Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Section Master Transaction Faild");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getDesignationDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        resultMap.put("designationDetails", getDesignationDetailsinHTML(session).toString());
        return resultMap;
    }

    public String getDesignationDetailsinHTML(Session session) {
        StringBuffer resultHTML = new StringBuffer();
        try {

            Criteria designationCrit = session.createCriteria(Designationmaster.class);
            designationCrit.addOrder(Order.asc("designationcode"));
            List designationList = designationCrit.list();
            if (designationList.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Designation Code</td>").append("<td>Designation Name</td>").append("<td>Pay Scale</td>").append("<td>Order no</td>").append("<td>Modify</td>").append("</tr>");
                for (int i = 0; i < designationList.size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Designationmaster designationObj = (Designationmaster) designationList.get(i);
                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"center\">" + designationObj.getDesignationcode() + "</td>").append("<td align=\"left\">" + designationObj.getDesignation() + "</td>").append("<td align=\"center\">" + designationObj.getPayscalecode() + "</td>").append("<td align=\"center\">" + designationObj.getOrderno() + "</td>") //                            .append("<td align=\"center\">" + designationObj.getOrderno() + "</td>")
                            .append("<td align=\"center\"><input type=\"radio\" name=\"designationcode\" id=\"" + designationObj.getDesignationcode() + "\" onclick=\"getDesignationMasterDetailsForModify('" + designationObj.getDesignationcode() + "','" + designationObj.getDesignation() + "','" + designationObj.getPayscalecode() + "','" + designationObj.getOrderno() + "')\"></td>").append("</tr>");
                }

            } else {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Designation Code</td>").append("<td>Designation Name</td>").append("<td>Pay Scale</td>").append("<td>Order no</td>").append("<td>Modify</td>").append("</tr>");
                resultHTML.append("</table>");
                resultHTML.append("</td></tr>");
            }
            resultHTML.append("</table>");


        } catch (Exception e) {
            e.printStackTrace();

        }
        return resultHTML.toString();
    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map getRegionAndSectionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
//        Map resultMap = new HashMap();
//        resultMap.put("regionandsectionDetails", getRegionAndSectionDetailsinHTML(session).toString());
//        return resultMap;
//    }
//
//    public String getRegionAndSectionDetailsinHTML(Session session) {
//        StringBuffer resultHTML = new StringBuffer();
//        try {
//            resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//            resultHTML.append("<tr><td valign=\"top\" width=\"50%\">");
//            Criteria regionCrit = session.createCriteria(Regionmaster.class);
//            regionCrit.addOrder(Order.asc("id"));
//            List regionList = regionCrit.list();
//            if (regionList.size() > 0) {
//                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Region Code</td>").append("<td>Region Name</td>").append("<td>Modify</td>").append("</tr>");
//                for (int i = 0; i < regionList.size(); i++) {
//                    if (i % 2 == 0) {
//                        classname = "rowColor1";
//                    } else {
//                        classname = "rowColor2";
//                    }
//                    Regionmaster regionObj = (Regionmaster) regionList.get(i);
//                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"center\">" + regionObj.getId() + "</td>").append("<td align=\"left\">" + regionObj.getRegionname() + "</td>").append("<td align=\"center\"><input type=\"radio\" name=\"regioncode\" id=\"" + regionObj.getId() + "\" onclick=\"getRegionMasterDetailsForModify('" + regionObj.getId() + "','" + regionObj.getRegionname() + "')\"></td>").append("</tr>");
//                }
//
//            } else {
//                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Region Code</td>").append("<td>Region Name</td>").append("<td>Modify</td>").append("</tr>");
//            }
//            resultHTML.append("</table></td>");
//            resultHTML.append("<td  valign=\"top\" width=\"50%\">");
//            Criteria secCrit = session.createCriteria(Sectionmaster.class);
//            secCrit.addOrder(Order.asc("id"));
//            List secList = secCrit.list();
//            if (secList.size() > 0) {
//                resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Section Code</td>").append("<td>Section Name</td>").append("<td>Modify</td>").append("</tr>");
//                for (int i = 0; i < secList.size(); i++) {
//                    if (i % 2 == 0) {
//                        classname = "rowColor1";
//                    } else {
//                        classname = "rowColor2";
//                    }
//                    Sectionmaster sectionnObj = (Sectionmaster) secList.get(i);
//                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"center\">" + sectionnObj.getId() + "</td>").append("<td align=\"left\">" + sectionnObj.getSectionname() + "</td>").append("<td align=\"center\"><input type=\"radio\" name=\"sectioncode\" id=\"" + sectionnObj.getId() + "\" onclick=\"getSectionMasterDetailsForModify('" + sectionnObj.getId() + "','" + sectionnObj.getSectionname() + "')\"></td>").append("</tr>");
//                }
//
//            } else {
//                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Section Code</td>").append("<td>Section Name</td>").append("<td>Modify</td>").append("</tr>");
//            }
//            resultHTML.append("</table></td></tr>");
//            resultHTML.append("</table>");
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        return resultHTML.toString();
//    }
    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        resultMap.put("regionDetails", getRegionDetailsinHTML(session).toString());
        resultMap.put("regioncode", getRegionCodeSerial(session));
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getSectionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        resultMap.put("sectionDetails", getSectionDetailsinHTML(session, "ALL", "0").toString());
        resultMap.put("sectioncode", getSectionCodeSerialNo(session));
        resultMap.put("regionlist", loadRegionDetails(session).get("regionlist"));
        resultMap.put("sectionlist", getLoadSections(session, LoggedInRegion).get("sectionlist"));
        return resultMap;
    }

    public String getSectionCodeSerialNo(Session session) {
//        Map resultMap = new HashMap();
        String sectionCodeSerial = "";
        String sectiontype = "S";
        String sectioncode = "";
        int incValue = 1;

        try {

            String strSectionID = " SELECT max(cast(REPLACE(TRANSLATE(id , REPLACE(TRANSLATE(id ,'0123456789', RPAD('#',LENGTH(id ),'#')),'#',''),"
                    + " RPAD('#',LENGTH(id ),'#')),'#','') as INTEGER)) AS foo FROM sectionmaster  ";
            Query query = session.createSQLQuery(strSectionID);
            List secList = query.list();
            if (secList.size() > 0 && secList.get(0) != null) {
                sectioncode = secList.get(0).toString();
                incValue = Integer.parseInt(sectioncode) + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sectiontype + incValue;
    }

    public String getRegionDetailsinHTML(Session session) {
        StringBuffer resultHTML = new StringBuffer();
        try {
            resultHTML.append("<FONT SIZE=2>");
            Criteria regionCrit = session.createCriteria(Regionmaster.class);
            regionCrit.addOrder(Order.asc("id"));
            List regionList = regionCrit.list();
            resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"regiontable\">").append("<thead>").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Region Code</td>").append("<td>Region Name</td>").append("<td>Modify</td>").append("</tr>").append("</thead>");
            resultHTML.append("<tbody>");
            if (regionList.size() > 0) {

                for (int i = 0; i < regionList.size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Regionmaster regionObj = (Regionmaster) regionList.get(i);
                    resultHTML.append("<tr>").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"center\">" + regionObj.getId() + "</td>").append("<td align=\"left\">" + regionObj.getRegionname() + "</td>").append("<td align=\"center\"><input type=\"radio\" name=\"regioncode\" id=\"" + regionObj.getId() + "\" onclick=\"getRegionMasterDetailsForModify('" + regionObj.getId() + "','" + regionObj.getRegionname() + "')\"></td>").append("</tr>");
                }

            }
            resultHTML.append("</tbody>");
            resultHTML.append("</table>");
            resultHTML.append("</FONT>");

        } catch (Exception e) {
            e.printStackTrace();

        }
        return resultHTML.toString();
    }

    public String getSectionDetailsinHTML(Session session, String region, String sectionid) {
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();
        try {
            resultHTML.append("<FONT SIZE=2>");
            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction("( region='ALL' or region='" + region + "')"));
            if (sectionid.equalsIgnoreCase("0")) {
                secCrit.add(Restrictions.sqlRestriction(" parentcode!='0'"));
            } else {
                secCrit.add(Restrictions.sqlRestriction(" parentcode='" + sectionid + "'"));
            }

            secCrit.addOrder(Order.asc("id"));
            List secList = secCrit.list();
            resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"sectiontable\">");
            resultHTML.append("<thead>");
            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td>S.No</td>");
            resultHTML.append("<td>Section Code</td>");
            resultHTML.append("<td>Section Name</td>");
            resultHTML.append("<td>Region Name</td>");
            resultHTML.append("<td>Modify</td>");
            resultHTML.append("</tr>");
            resultHTML.append("</thead>");
            resultHTML.append("<tbody>");
            if (secList.size() > 0) {

                for (int i = 0; i < secList.size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Sectionmaster sectionnObj = (Sectionmaster) secList.get(i);
                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    resultHTML.append("<td align=\"center\">" + sectionnObj.getId() + "</td>");
                    resultHTML.append("<td align=\"left\">" + sectionnObj.getSectionname() + "</td>");
                    if (sectionnObj.getRegion().equalsIgnoreCase("ALL")) {
                        resultHTML.append("<td align=\"left\">" + sectionnObj.getRegion() + "</td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + getRegionmaster(session, sectionnObj.getRegion()).getRegionname() + "</td>");
                    }

                    resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"sectioncode\" id=\"" + sectionnObj.getId() + "\" onclick=\"getSectionMasterDetailsForModify('" + sectionnObj.getId() + "','" + sectionnObj.getSectionname() + "','" + sectionnObj.getParentcode() + "','" + sectionnObj.getRegion() + "')\"></td>");
                    resultHTML.append("</tr>");
                }

            }
            resultHTML.append("</tbody>");
            resultHTML.append("</table>");
            resultHTML.append("</FONT>");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return resultHTML.toString();
    }

    public String getPayCodeDetailsinHTML(String paycodetype, Session session, String orderbyString) {
        StringBuffer resultHTML = new StringBuffer();
        try {

            Criteria paycodeCrit = session.createCriteria(Paycodemaster.class);
            paycodeCrit.add(Restrictions.sqlRestriction("paycodetype = '" + paycodetype + "'"));
//            paycodeCrit.addOrder(Order.asc("paycodetype"));
            paycodeCrit.addOrder(Order.asc(orderbyString));
            List paycodeList = paycodeCrit.list();
            if (paycodeList.size() > 0) {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td style=\"cursor:pointer;\" onclick=\"getPayCode('" + paycodetype + "','paycode');\">Pay Code</td>").append("<td style=\"cursor:pointer;\" onclick=\"getPayCode('" + paycodetype + "','paycodename');\">PayCode Name</td>").append("<td>Percentage</td>").append("<td>Modify</td>").append("</tr>");
                for (int i = 0; i < paycodeList.size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Paycodemaster paycodeObj = (Paycodemaster) paycodeList.get(i);
                    resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"center\">" + paycodeObj.getPaycode() + "</td>").append("<td align=\"left\">" + paycodeObj.getPaycodename() + "</td>").append("<td align=\"center\">" + paycodeObj.getPaypercentage() + "</td>").append("<td align=\"center\"><input type=\"radio\" name=\"paycode\" id=\"" + paycodeObj.getPaycode() + "\" onclick=\"getPaycodeMasterDetailsForModify('" + paycodeObj.getPaycode() + "','" + paycodeObj.getPaycodename() + "','" + paycodeObj.getPaypercentage() + "')\"></td>").append("</tr>");
                }

            } else {
                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
                resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Pay Code</td>").append("<td>PayCode Name</td>").append("<td>Percentage</td>").append("<td>Modify</td>").append("</tr>");
                resultHTML.append("</table>");
                resultHTML.append("</td></tr>");
            }
            resultHTML.append("</table>");


        } catch (Exception e) {
            e.printStackTrace();

        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoadEarningsDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map earningsMap = new LinkedHashMap();
        earningsMap.put("0", "--Select--");
        String paycode = "";
        String paycodename = "";
        try {
            Criteria ernCrit = session.createCriteria(Paycodemaster.class);
            ernCrit.add(Restrictions.sqlRestriction("paycodetype='E'"));
            ernCrit.addOrder(Order.asc("paycode"));
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
    public Map getLoadAssignedPayCodes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String paytype) {
        Map resultMap = new HashMap();
        resultMap.put("paycodeDetails", getAssignedPayCodesinHTML(session, paytype).toString());
        return resultMap;
    }

    public String getAssignedPayCodesinHTML(Session session, String paytype) {
        StringBuffer resultHTML = new StringBuffer();
        String paycodeIds = "";
        try {
            Criteria ccahraCrit = session.createCriteria(Ccahra.class);
            ccahraCrit.add(Restrictions.sqlRestriction("ccahra='" + paytype.toUpperCase() + "'"));
            List ccahraList = ccahraCrit.list();
            if (ccahraList.size() > 0) {
                for (int i = 0; i < ccahraList.size(); i++) {
//                    resultHTML.append("<td align=\"left\" valign=\"top\">");
                    Ccahra ccahraObj = (Ccahra) ccahraList.get(i);
                    paycodeIds = paycodeIds + ccahraObj.getPaycodemaster().getPaycode() + "$";
                    resultHTML.append("<div class=\"item icart\" width=\"20px;\">").append("" + ccahraObj.getPaycodemaster().getPaycodename() + " ").append("&nbsp;&nbsp;<a onclick=\"remove(this)\" class=\"remove '" + ccahraObj.getPaycodemaster().getPaycode() + "'\" id=\"" + ccahraObj.getPaycodemaster().getPaycode() + "\" >&times;</a>").append("</div>");

//                    resultHTML.append("<div class=\"item icart\" width=\"20px;\">")
//                            .append(""+ccahraObj.getPaycodemaster().getPaycodename()+"<div class=\"divrm\" width=\"20px;\">")
//                            .append("<a onclick=\"remove(this)\" class=\"remove '"+ccahraObj.getPaycodemaster().getPaycode()+"'\">&times;</a>")
//                            .append("<div/></div>");
//                            .append("</td>");
//                    System.out.println("ccahraObj.getPaycodemaster().getPaycodename()==" + ccahraObj.getPaycodemaster().getPaycodename());
                }
                System.out.println("===" + paycodeIds);

//                resultHTML.append("</tr></table>");

            }
            resultHTML.append("<input type=\"hidden\" name=\"paycodeIds\" id=\"paycodeIds\" value=\"" + paycodeIds + "\">");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveCCAHRAMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String paytype, String paycodeIds) {
        Map resultMap = new HashMap();
        String[] paycodes;
        Transaction transaction = null;
        try {
//            System.out.println("paycodeIds==" + paycodeIds);
            paycodes = paycodeIds.replace("$", "#").split("#");

            transaction = session.beginTransaction();
            String deletepaytypeDetails = " delete from ccahra where ccahra='" + paytype + "'";
            session.createSQLQuery(deletepaytypeDetails).executeUpdate();
            try {
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }

            if (paycodes.length > 0) {
                for (int i = 0; i < paycodes.length; i++) {
                    transaction = session.beginTransaction();
//                    System.out.println("paycodes[i]===" + paycodes[i]);
                    Paycodemaster payObj = new Paycodemaster();
                    payObj.setPaycode(paycodes[i]);

                    Ccahra masterobj = new Ccahra();
                    masterobj.setCcahraid("" + getmaxSequenceNumber(session));
                    masterobj.setCcahra(paytype);
                    masterobj.setPaycodemaster(payObj);
                    session.saveOrUpdate(masterobj);
                    try {
                        transaction.commit();
                    } catch (Exception e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        resultMap.put("ERROR", "Section Master Transaction Faild");
                    }

                }
            }

            resultMap.put("success", "CCA OR HRA Master Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Section Master Transaction Faild");
        }
        return resultMap;
    }

    public int getmaxSequenceNumber(Session session) {
        int maxSequenceNumber = 1;
        String temp = "0";
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(ccahraid as int)) as maxsequencenumber from ccahra");
            List imlist = session.createSQLQuery(sb.toString()).list();
            if (imlist.size() > 0) {
                if (imlist.get(0) != null) {
                    maxSequenceNumber = (Integer) imlist.get(0);
                    maxSequenceNumber = maxSequenceNumber + 1;
//                    temp = (String) imlist.get(0);
//                    maxSequenceNumber = Integer.parseInt(temp) + 1;
                } else {
                    maxSequenceNumber = 1;
                }
            } else {
                maxSequenceNumber = 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return maxSequenceNumber;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();

        Criteria stopPayCrit = session.createCriteria(Stoppayrolldetails.class);
        stopPayCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
        stopPayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
        stopPayCrit.add(Restrictions.sqlRestriction("enddate is null"));
//        stopPayCrit.add(Restrictions.sqlRestriction("reasoncode not in ('RETIRED','RESIGNED')"));
        List stopPayList = stopPayCrit.list();
        if (stopPayList.size() > 0) {
            Stoppayrolldetails stoppayrolldetailsObj = (Stoppayrolldetails) stopPayList.get(0);
            if (stoppayrolldetailsObj.getReasoncode().equalsIgnoreCase("RETIRED")) {
                resultMap.put("ERROR", "Given EPF Number is Aleady Retired. ");
            } else if (stoppayrolldetailsObj.getReasoncode().equalsIgnoreCase("RESIGNED")) {
                resultMap.put("ERROR", "Given EPF Number is Aleady Resigned. ");
            } else if (stoppayrolldetailsObj.getReasoncode().equalsIgnoreCase("TRANSFERRED")) {
                resultMap.put("ERROR", "Given EPF Number in Transfer.");
            } else if (stoppayrolldetailsObj.getReasoncode().equalsIgnoreCase("Death")) {
                resultMap.put("ERROR", "Given EPF Number Employee is Dead.");
            } else {
                Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
                empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
                empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                List empDetailsList = empDetailsCrit.list();
                if (empDetailsList.size() > 0) {
                    Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
                    resultMap.put("employeename", empmasterObj.getEmployeename());
                } else {
                    resultMap.put("ERROR", "Please Enter the Valid Employee EPF No ");
                }
            }
        } else {
            Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
            empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
            empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List empDetailsList = empDetailsCrit.list();
            if (empDetailsList.size() > 0) {
                Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
                resultMap.put("employeename", empmasterObj.getEmployeename());
            } else {
                resultMap.put("ERROR", "Please Enter the Valid Employee EPF No ");
            }
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveStopPayment(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String reasontype, String reasondate, String remarks) {
        Map resultMap = new HashMap();
        String updateprocessDetails = "";
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Criteria stopPayCrit = session.createCriteria(Stoppayrolldetails.class);
            stopPayCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
            stopPayCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
            stopPayCrit.add(Restrictions.sqlRestriction("enddate is null"));
            List stopPayList = stopPayCrit.list();
            if (stopPayList.size() > 0) {
                Stoppayrolldetails empmasterObj = (Stoppayrolldetails) stopPayList.get(0);
                if (empmasterObj.getReasoncode().equalsIgnoreCase("REVOKE")) {
                    resultMap.put("ERROR", "Given EPF No is already  " + empmasterObj.getReasoncode() + ".");
                } else if (reasontype.equalsIgnoreCase("Revoke")) {
                    updateprocessDetails = " UPDATE employeemaster SET process='TRUE' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    session.createSQLQuery(updateprocessDetails).executeUpdate();

                    empmasterObj.setEnddate(postgresDate(reasondate));
                    empmasterObj.setOldremarks(empmasterObj.getReasoncode() + "  ---- " + empmasterObj.getRemarks());
                    empmasterObj.setReasoncode(reasontype.toUpperCase());
                    empmasterObj.setRemarks(remarks.toString());
                    empmasterObj.setAccregion(LoggedInRegion);
                    session.update(empmasterObj);
                    transaction.commit();

                    resultMap.put("success", "Stop Payment Successfully Revoked Successfully");
                } else {
                    resultMap.put("ERROR", "Given EPF No is already in " + empmasterObj.getReasoncode() + ".Please Revoke First.");
                }
            } else {
                if (reasontype.toUpperCase().equalsIgnoreCase("REVOKE")) {
                    resultMap.put("ERROR", "There is no record found in stop payment for given EPFNO");
                } else {
                    Stoppayrolldetails masterobj = new Stoppayrolldetails();

                    if (reasontype.toUpperCase().equalsIgnoreCase("TRANSFERRED")) {
                        updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='TRANS' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    } else if (reasontype.toUpperCase().equalsIgnoreCase("RETIRED")) {
                        updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='RET' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    } else if (reasontype.toUpperCase().equalsIgnoreCase("RESIGNED")) {
                        updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='RES' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    } else if (reasontype.toUpperCase().equalsIgnoreCase("REMOVED FROM SERVICE")) {
                        updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='RFS' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    } else if (reasontype.toUpperCase().equalsIgnoreCase("DEATH")) {
                        updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='DTH' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    } else {
                        updateprocessDetails = " UPDATE employeemaster SET process='FALSE' where   epfno='" + epfno + "'and region='" + LoggedInRegion + "'";

                    }
                    session.createSQLQuery(updateprocessDetails).executeUpdate();

                    masterobj.setStartdate(postgresDate(reasondate));
                    Employeemaster empObj = new Employeemaster();
                    empObj.setEpfno(epfno);
                    masterobj.setId(getmaxSequenceNumberFORstopPayment(session));
                    masterobj.setEmployeemaster(empObj);
                    masterobj.setReasoncode(reasontype.toUpperCase());
                    masterobj.setRemarks(remarks.toString());
                    masterobj.setAccregion(LoggedInRegion);
                    masterobj.setCreatedby(LoggedInUser);
                    masterobj.setCreateddate(getCurrentDate());
                    session.save(masterobj);
                    transaction.commit();
                    resultMap.put("success", "Stop Payment Successfully Saved");
                }
            }
//            if (reasontype.equalsIgnoreCase("Revoke")) {
//
//                updateprocessDetails = " UPDATE employeemaster SET process='TRUE' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
//                session.createSQLQuery(updateprocessDetails).executeUpdate();
//
//                Criteria empDetailsCrit = session.createCriteria(Stoppayrolldetails.class);
//                empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
//                empDetailsCrit.add(Restrictions.sqlRestriction("accregion='" + LoggedInRegion + "'"));
//                empDetailsCrit.add(Restrictions.sqlRestriction("enddate is null"));
//                List empDetailsList = empDetailsCrit.list();
//                if (empDetailsList.size() > 0) {
//                    Stoppayrolldetails empmasterObj = (Stoppayrolldetails) empDetailsList.get(0);
//                    empmasterObj.setEnddate(postgresDate(reasondate));
//                    empmasterObj.setOldremarks(empmasterObj.getReasoncode() + "\n  ---- " + empmasterObj.getRemarks());
//                    empmasterObj.setReasoncode(reasontype.toUpperCase());
//                    empmasterObj.setRemarks(remarks.toString());
//                    empmasterObj.setAccregion(LoggedInRegion);
//                    session.update(empmasterObj);
//                    transaction.commit();
//
//                }
//
//            } else {
//                Stoppayrolldetails masterobj = new Stoppayrolldetails();
//
//                if (reasontype.equalsIgnoreCase("Transferred")) {
//                    updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='TRANS' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
//                } else if (reasontype.equalsIgnoreCase("RETIRED")) {
//                    updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='RET' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
//                } else if (reasontype.equalsIgnoreCase("RESIGNED")) {
//                    updateprocessDetails = " UPDATE employeemaster SET process='FALSE',region='RES' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
//                } else {
//                    updateprocessDetails = " UPDATE employeemaster SET process='FALSE' where   epfno='" + epfno + "'and region='" + LoggedInRegion + "'";
//
//                }
//                session.createSQLQuery(updateprocessDetails).executeUpdate();
//
//                masterobj.setStartdate(postgresDate(reasondate));
//                Employeemaster empObj = new Employeemaster();
//                empObj.setEpfno(epfno);
//                masterobj.setId(getmaxSequenceNumberFORstopPayment(session));
//                masterobj.setEmployeemaster(empObj);
//                masterobj.setReasoncode(reasontype.toUpperCase());
//                masterobj.setRemarks(remarks.toString());
//                masterobj.setAccregion(LoggedInRegion);
//                masterobj.setCreatedby(LoggedInUser);
//                masterobj.setCreateddate(getCurrentDate());
//                session.save(masterobj);
//                transaction.commit();
//            }
//            if (reasontype.equalsIgnoreCase("Revoke")) {
//                resultMap.put("success", "Stop Payment Successfully Revoked Successfully");
//            } else {
//                resultMap.put("success", "Stop Payment Successfully Saved");
//            }


        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Stop Payment Transaction Faild");
        }
        return resultMap;
    }

    public synchronized String getmaxSequenceNumberFORstopPayment(Session session) {
        int maxSequenceNumber = 1;

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(id as int)) as maxsequencenumber from stoppayrolldetails");
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
    public Map loadEmployeeAccountDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String deductioncode) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());

            Criteria empAccCodeCrit = session.createCriteria(Employeedeductionaccountcode.class);
            empAccCodeCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
            empAccCodeCrit.add(Restrictions.sqlRestriction("paycode = '" + deductioncode + "' "));
            List empAccCodeList = empAccCodeCrit.list();
            if (empAccCodeList.size() > 0) {
                Employeedeductionaccountcode empAccountObj = (Employeedeductionaccountcode) empAccCodeList.get(0);
                resultMap.put("accountcode", empAccountObj.getDeductionaccountcode());
                resultMap.put("serialno", empAccountObj.getId());
            } else {
                resultMap.put("accountcode", "");
                resultMap.put("serialno", "0");
            }
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();

        Map deductionlist = new LinkedHashMap();
        deductionlist.put("0", "--Select--");
        Paycodemaster paycodemasterObj;
        Criteria lrCrit = session.createCriteria(Paycodemaster.class);
        lrCrit.add(Restrictions.sqlRestriction("paycodetype in ('D')"));
        lrCrit.addOrder(Order.asc("paycodename"));
        List ldList = lrCrit.list();
        if (ldList.size() > 0) {
            for (int i = 0; i < ldList.size(); i++) {
                paycodemasterObj = (Paycodemaster) ldList.get(i);
                deductionlist.put(paycodemasterObj.getPaycode(), paycodemasterObj.getPaycodename());

            }

        }
        resultMap.put("deductionlist", deductionlist);
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeAccountCode(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductioncode, String epfno, String empaccountcode, String serialno) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            Employeedeductionaccountcode masterobj = new Employeedeductionaccountcode();
            if (serialno.equalsIgnoreCase("0")) {
                masterobj.setId(getMaxEmployeeAccountCodeid(session, LoggedInRegion));
            } else {
                masterobj.setId(serialno);
            }
            Paycodemaster paycodeObj = new Paycodemaster();
            paycodeObj.setPaycode(deductioncode);
            masterobj.setEmployeemaster(getEmployeemaster(session, epfno, LoggedInRegion));
            masterobj.setDeductionaccountcode(empaccountcode);
            masterobj.setPaycodemaster(paycodeObj);
            transaction = session.beginTransaction();
            session.saveOrUpdate(masterobj);
            transaction.commit();

            resultMap.put("success", "Employee Account Code Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Employee Account Code Transaction Faild");
        }
        return resultMap;
    }

    public synchronized String getMaxEmployeeAccountCodeid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeedeductionaccountcodeid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeedeductionaccountcodeid(maxNoStr);
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
        } finally {
        }
        return maxStr;
    }

    private String getFilePath(HttpServletRequest request, String LoggedInRegion) {
        String path = request.getRealPath("/");
        //System.out.println("path====" + path);
        String sessionid = request.getSession(false).getId();
        String osName = System.getProperty("os.name");
        String fileName = "budgetfile";
        String folder = path + LoggedInRegion + "_" + sessionid;
//        String folder = path+userid+"_"+sessionid;

        File dir = new File(folder);
        dir.delete();
        if (!dir.exists()) {
            dir.mkdir();
        }

        String resPath = "";
        if (osName.equalsIgnoreCase("Linux") || osName.equalsIgnoreCase("Unix")) {
            resPath = folder + "/" + fileName + ".txt";
        } else {
            resPath = folder + "\\" + fileName + ".txt";
        }
        //System.out.println(" resPath : " + resPath);
        return resPath;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map MasterDataUpdata(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePathwithName) {
        Map resultMap = new HashMap();
        String content = null;
        String result = "";
        masterDetails masterDetailsObj = null;
        try {
//            content = readFileAsString(filePathwithName);
//
//            String key = "sairam";
//            String txt = base64decode(content);
//            result = xorMessage(txt, key);
//            System.out.println("----------" + result);            
            JAXBContext context;
            context = JAXBContext.newInstance(masterDetails.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
//            masterDetailsObj = (masterDetails) unmarshaller.unmarshal(new StringReader(result));
            masterDetailsObj = (masterDetails) unmarshaller.unmarshal(new FileReader(filePathwithName));

            List<RegionMasterValueObjectModel> regionMasterPostObjectList = masterDetailsObj.getRegionMasterPostList();
            for (int i = 0; i < regionMasterPostObjectList.size(); i++) {
                RegionMasterValueObjectModel regionmasterObjectModelObj = (RegionMasterValueObjectModel) regionMasterPostObjectList.get(i);

                Criteria regionMasterCrit = session.createCriteria(Regionmaster.class);
                regionMasterCrit.add(Restrictions.sqlRestriction("id='" + regionmasterObjectModelObj.getId() + "'"));

                List<Regionmaster> regionMasterList = regionMasterCrit.list();
                Regionmaster regionMasterObj = null;
                if (regionMasterList.size() > 0) {
                    regionMasterObj = (Regionmaster) regionMasterList.get(0);
                    regionMasterObj.setRegionname(regionmasterObjectModelObj.getRegionname());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.update(regionMasterObj);
                    transaction.commit();
                } else {
                    regionMasterObj = new Regionmaster();

                    regionMasterObj.setId(regionmasterObjectModelObj.getId());
                    regionMasterObj.setRegionname(regionmasterObjectModelObj.getRegionname());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.save(regionMasterObj);
                    transaction.commit();

                }
            }

            List<SectionMasterValueObjectModel> sectionMasterPostObjectList = masterDetailsObj.getSectionMasterPostList();
            for (int i = 0; i < sectionMasterPostObjectList.size(); i++) {
                SectionMasterValueObjectModel sectionmasterObjectModelObj = (SectionMasterValueObjectModel) sectionMasterPostObjectList.get(i);

                Criteria sectionMasterCrit = session.createCriteria(Sectionmaster.class);
                sectionMasterCrit.add(Restrictions.sqlRestriction("id='" + sectionmasterObjectModelObj.getId() + "'"));

                List<Sectionmaster> sectionMasterList = sectionMasterCrit.list();
                Sectionmaster sectionMasterObj = null;
                if (sectionMasterList.size() > 0) {
                    sectionMasterObj = (Sectionmaster) sectionMasterList.get(0);
                    sectionMasterObj.setSectionname(sectionmasterObjectModelObj.getSectionname());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.update(sectionMasterObj);
                    transaction.commit();
                } else {
                    sectionMasterObj = new Sectionmaster();

                    sectionMasterObj.setId(sectionmasterObjectModelObj.getId());
                    sectionMasterObj.setSectionname(sectionmasterObjectModelObj.getSectionname());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.save(sectionMasterObj);
                    transaction.commit();
                }
            }

            List<DesignationMasterValueObjectModel> designationMasterValueObjectList = masterDetailsObj.getDesignationMasterPostList();
            for (int i = 0; i < designationMasterValueObjectList.size(); i++) {
                DesignationMasterValueObjectModel designationmasterObjectModelObj = (DesignationMasterValueObjectModel) designationMasterValueObjectList.get(i);

                Criteria designationMasterCrit = session.createCriteria(Designationmaster.class);
                designationMasterCrit.add(Restrictions.sqlRestriction("designationcode='" + designationmasterObjectModelObj.getDesignationcode() + "'"));

                List<Designationmaster> designationMasterList = designationMasterCrit.list();
                Designationmaster designationMasterObj = null;
                if (designationMasterList.size() > 0) {
                    designationMasterObj = (Designationmaster) designationMasterList.get(0);
                    designationMasterObj.setDesignation(designationmasterObjectModelObj.getDesignation());
                    designationMasterObj.setPayscalecode(designationmasterObjectModelObj.getPayscalecode());
                    designationMasterObj.setRemarks(designationmasterObjectModelObj.getRemarks());
                    designationMasterObj.setOrderno(designationmasterObjectModelObj.getOrderno());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.update(designationMasterObj);
                    transaction.commit();
                } else {
                    designationMasterObj = new Designationmaster();

                    designationMasterObj.setDesignationcode(designationmasterObjectModelObj.getDesignationcode());
                    designationMasterObj.setDesignation(designationmasterObjectModelObj.getDesignation());
                    designationMasterObj.setPayscalecode(designationmasterObjectModelObj.getPayscalecode());
                    designationMasterObj.setRemarks(designationmasterObjectModelObj.getRemarks());
                    designationMasterObj.setOrderno(designationmasterObjectModelObj.getOrderno());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.save(designationMasterObj);
                    transaction.commit();
                }

            }

            List<PaycodeMasterValueObjectModel> paycodeMasterValueObjectList = masterDetailsObj.getPaycodeMasterPostList();
            for (int i = 0; i < paycodeMasterValueObjectList.size(); i++) {
                PaycodeMasterValueObjectModel paycodemasterObjectModelObj = (PaycodeMasterValueObjectModel) paycodeMasterValueObjectList.get(i);

                Criteria paycodeMasterCrit = session.createCriteria(Paycodemaster.class);
                paycodeMasterCrit.add(Restrictions.sqlRestriction("paycode='" + paycodemasterObjectModelObj.getPaycode() + "'"));

                List<Paycodemaster> paycodeMasterList = paycodeMasterCrit.list();
                Paycodemaster paycodeMasterObj = null;
                if (paycodeMasterList.size() > 0) {
                    paycodeMasterObj = (Paycodemaster) paycodeMasterList.get(0);
                    paycodeMasterObj.setPaycodename(paycodemasterObjectModelObj.getPaycodename());
                    paycodeMasterObj.setPaycodetype(new Character(paycodemasterObjectModelObj.getPaycodetype().charAt(0)));
                    paycodeMasterObj.setPaypercentage(new BigDecimal(paycodemasterObjectModelObj.getPaypercentage()));
                    paycodeMasterObj.setGrouphead(paycodemasterObjectModelObj.getGrouphead());
                    paycodeMasterObj.setPaycodeserial(paycodemasterObjectModelObj.getPaycodeserial());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.update(paycodeMasterObj);
                    transaction.commit();
                } else {
                    paycodeMasterObj = new Paycodemaster();

                    paycodeMasterObj.setPaycode(paycodemasterObjectModelObj.getPaycode());
                    paycodeMasterObj.setPaycodename(paycodemasterObjectModelObj.getPaycodename());
                    paycodeMasterObj.setPaycodetype(new Character(paycodemasterObjectModelObj.getPaycodetype().charAt(0)));
                    paycodeMasterObj.setPaypercentage(new BigDecimal(paycodemasterObjectModelObj.getPaypercentage()));
                    paycodeMasterObj.setGrouphead(paycodemasterObjectModelObj.getGrouphead());
                    paycodeMasterObj.setPaycodeserial(paycodemasterObjectModelObj.getPaycodeserial());

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.save(paycodeMasterObj);
                    transaction.commit();
                }

            }

            List<CcahraMasterValueObjectModel> ccahraMasterPostObjectList = masterDetailsObj.getCcahraMasterPostList();
            for (int i = 0; i < ccahraMasterPostObjectList.size(); i++) {
                CcahraMasterValueObjectModel ccahraObjectModelObj = (CcahraMasterValueObjectModel) ccahraMasterPostObjectList.get(i);

                Criteria ccahraMasterCrit = session.createCriteria(Ccahra.class);
                ccahraMasterCrit.add(Restrictions.sqlRestriction("ccahraid='" + ccahraObjectModelObj.getCcahraid() + "'"));

                List<Ccahra> ccahraMasterList = ccahraMasterCrit.list();
                Ccahra ccahraMasterObj = null;
                if (ccahraMasterList.size() > 0) {
                    ccahraMasterObj = (Ccahra) ccahraMasterList.get(0);
                    ccahraMasterObj.setCcahra(ccahraObjectModelObj.getCcahra());
                    Paycodemaster paycodemasterObj = new Paycodemaster();
                    paycodemasterObj.setPaycode(ccahraObjectModelObj.getPaycode());
                    ccahraMasterObj.setPaycodemaster(paycodemasterObj);

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.update(ccahraMasterObj);
                    transaction.commit();
                } else {
                    ccahraMasterObj = new Ccahra();

                    ccahraMasterObj.setCcahraid(ccahraObjectModelObj.getCcahraid());
                    ccahraMasterObj.setCcahra(ccahraObjectModelObj.getCcahra());
                    Paycodemaster paycodemasterObj = new Paycodemaster();
                    paycodemasterObj.setPaycode(ccahraObjectModelObj.getPaycode());
                    ccahraMasterObj.setPaycodemaster(paycodemasterObj);

                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.save(ccahraMasterObj);
                    transaction.commit();
                }
            }

            List<EarningsSlapDetailsValueObjectModel> earningsSlapsPostObjectList = masterDetailsObj.getEarningsSlapDetailsPostList();
            for (int i = 0; i < earningsSlapsPostObjectList.size(); i++) {
                EarningsSlapDetailsValueObjectModel earningsSlapsObjectModelObj = (EarningsSlapDetailsValueObjectModel) earningsSlapsPostObjectList.get(i);

                Criteria earningsSlapsMasterCrit = session.createCriteria(Earningslapdetails.class);
                earningsSlapsMasterCrit.add(Restrictions.sqlRestriction("id='" + earningsSlapsObjectModelObj.getId() + "'"));

                List<Earningslapdetails> earningsSlapMasterList = earningsSlapsMasterCrit.list();
                Earningslapdetails earningsSlapsMasterObj = null;
                if (earningsSlapMasterList.size() > 0) {
                    earningsSlapsMasterObj = (Earningslapdetails) earningsSlapMasterList.get(0);
                    earningsSlapsMasterObj.setEarningcode(earningsSlapsObjectModelObj.getEarningcode());
                    if (earningsSlapsObjectModelObj.getPeriodfrom() != null) {
                        earningsSlapsMasterObj.setPeriodfrom(DateParser.postgresDate1(earningsSlapsObjectModelObj.getPeriodfrom()));
                    }
                    if (earningsSlapsObjectModelObj.getPeriodto() != null) {
                        earningsSlapsMasterObj.setPeriodto(DateParser.postgresDate1(earningsSlapsObjectModelObj.getPeriodto()));
                    }
                    if (earningsSlapsObjectModelObj.getAmount() != null) {
                        earningsSlapsMasterObj.setAmount(new BigDecimal(earningsSlapsObjectModelObj.getAmount()));
                    } else {
                        earningsSlapsMasterObj.setAmount(BigDecimal.ZERO);
                    }
                    earningsSlapsMasterObj.setOrderno(earningsSlapsObjectModelObj.getOrderno());


                    if (earningsSlapsObjectModelObj.getAmountrangefrom() != null) {
                        earningsSlapsMasterObj.setAmountrangefrom(new BigDecimal(earningsSlapsObjectModelObj.getAmountrangefrom()));
                    } else {
                        earningsSlapsMasterObj.setAmountrangefrom(BigDecimal.ZERO);
                    }

                    if (earningsSlapsObjectModelObj.getAmountrangeto() != null) {
                        earningsSlapsMasterObj.setAmountrangeto(new BigDecimal(earningsSlapsObjectModelObj.getAmountrangeto()));
                    } else {
                        earningsSlapsMasterObj.setAmountrangeto(BigDecimal.ZERO);
                    }

                    if (earningsSlapsObjectModelObj.getPercentage() != null) {
                        earningsSlapsMasterObj.setPercentage(new BigDecimal(earningsSlapsObjectModelObj.getPercentage()));
                    } else {
                        earningsSlapsMasterObj.setPercentage(BigDecimal.ZERO);
                    }

                    if (earningsSlapsObjectModelObj.getSynchronized1() != null) {
                        if (earningsSlapsObjectModelObj.getSynchronized1().equalsIgnoreCase("TRUE")) {
                            earningsSlapsMasterObj.setSynchronized_(Boolean.TRUE);
                        } else {
                            earningsSlapsMasterObj.setSynchronized_(Boolean.FALSE);
                        }
                    }
                    if (earningsSlapsObjectModelObj.getCancelled() != null) {
                        if (earningsSlapsObjectModelObj.getCancelled().equalsIgnoreCase("TRUE")) {
                            earningsSlapsMasterObj.setCancelled(Boolean.TRUE);
                        } else {
                            earningsSlapsMasterObj.setCancelled(Boolean.FALSE);
                        }
                    }


                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.update(earningsSlapsMasterObj);
                    transaction.commit();
                } else {
                    earningsSlapsMasterObj = new Earningslapdetails();

                    earningsSlapsMasterObj.setId(earningsSlapsObjectModelObj.getId());
                    earningsSlapsMasterObj.setEarningcode(earningsSlapsObjectModelObj.getEarningcode());

                    if (earningsSlapsObjectModelObj.getPeriodfrom() != null) {
                        earningsSlapsMasterObj.setPeriodfrom(DateParser.postgresDate1(earningsSlapsObjectModelObj.getPeriodfrom()));
                    }
                    if (earningsSlapsObjectModelObj.getPeriodto() != null) {
                        earningsSlapsMasterObj.setPeriodto(DateParser.postgresDate1(earningsSlapsObjectModelObj.getPeriodto()));
                    }
                    if (earningsSlapsObjectModelObj.getAmount() != null) {
                        earningsSlapsMasterObj.setAmount(new BigDecimal(earningsSlapsObjectModelObj.getAmount()));
                    } else {
                        earningsSlapsMasterObj.setAmount(BigDecimal.ZERO);
                    }
                    earningsSlapsMasterObj.setOrderno(earningsSlapsObjectModelObj.getOrderno());


                    if (earningsSlapsObjectModelObj.getAmountrangefrom() != null) {
                        earningsSlapsMasterObj.setAmountrangefrom(new BigDecimal(earningsSlapsObjectModelObj.getAmountrangefrom()));
                    } else {
                        earningsSlapsMasterObj.setAmountrangefrom(BigDecimal.ZERO);
                    }

                    if (earningsSlapsObjectModelObj.getAmountrangeto() != null) {
                        earningsSlapsMasterObj.setAmountrangeto(new BigDecimal(earningsSlapsObjectModelObj.getAmountrangeto()));
                    } else {
                        earningsSlapsMasterObj.setAmountrangeto(BigDecimal.ZERO);
                    }

                    if (earningsSlapsObjectModelObj.getPercentage() != null) {
                        earningsSlapsMasterObj.setPercentage(new BigDecimal(earningsSlapsObjectModelObj.getPercentage()));
                    } else {
                        earningsSlapsMasterObj.setPercentage(BigDecimal.ZERO);
                    }


                    if (earningsSlapsObjectModelObj.getSynchronized1() != null) {
                        if (earningsSlapsObjectModelObj.getSynchronized1().equalsIgnoreCase("TRUE")) {
                            earningsSlapsMasterObj.setSynchronized_(Boolean.TRUE);
                        } else {
                            earningsSlapsMasterObj.setSynchronized_(Boolean.FALSE);
                        }
                    }
                    if (earningsSlapsObjectModelObj.getCancelled() != null) {
                        if (earningsSlapsObjectModelObj.getCancelled().equalsIgnoreCase("TRUE")) {
                            earningsSlapsMasterObj.setCancelled(Boolean.TRUE);
                        } else {
                            earningsSlapsMasterObj.setCancelled(Boolean.FALSE);
                        }
                    }


                    Transaction transaction;
                    transaction = session.beginTransaction();
                    session.save(earningsSlapsMasterObj);
                    transaction.commit();
                }
            }

//            System.out.println("================="+"Given Text File uploaded");

            resultMap.put("message", "success");
        } catch (IOException ex) {
            resultMap.put("message", "Given Text File Corrupted");
            System.out.println("=================" + "Given Text File Corrupted");
            Logger.getLogger(MasterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            System.out.println("************" + "Given Text File Corrupted");
            resultMap.put("message", "Given Text File corrupted");
            Logger.getLogger(MasterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoadEarningsTypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map earningsMap = new LinkedHashMap();
        earningsMap.put("0", "--Select--");
        String paycode = "";
        String paycodename = "";
        System.out.println("LoggedInRegion====" + LoggedInRegion);
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
    public Map createRowinHTML(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String noofrows) {
        Map resultMap = new HashMap();
        StringBuffer resultHTML = new StringBuffer();

        try {
            resultHTML.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Amount Range From</td>").append("<td>Amount Range To</td>").append("<td>Amount</td>").append("<td>Percentage</td>").append("</tr>");
            for (int i = 1; i <= Integer.parseInt(noofrows.toString()); i++) {
                if (i % 2 == 0) {
                    classname = "rowColor1";
                } else {
                    classname = "rowColor2";
                }
                resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + i + "</td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"amountrangefrom" + i + "\"  onblur=\"checkFloat(this.id,'Amount Range From');\" size=\"20\"/></td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"amountrangeto" + i + "\"  onblur=\"checkFloat(this.id,'Amount Range To');\" size=\"20\"/></td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"slapamount" + i + "\"  onblur=\"checkFloat(this.id,'Salp Amount');\" size=\"20\"/></td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\"  id=\"slappercentage" + i + "\"  value=\"0\" onblur=\"checkPercentage(this.id,'Slap Percentage');\" size=\"20\"/>&nbsp;&nbsp;%</td>").append("</tr>");

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
        String orderNo = "";
        String effectDate = "";
        try {
            resultHTML.append("<table width=\"100%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Amount Range From</td>").append("<td>Amount Range To</td>").append("<td>Amount</td>").append("<td>Percentage</td>").append("</tr>");

            Criteria ernCrit = session.createCriteria(Earningslapdetails.class);
            ernCrit.add(Restrictions.sqlRestriction("earningcode='" + earningcode + "'"));
            ernCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            ernCrit.addOrder(Order.asc("amount"));
            ernCrit.addOrder(Order.asc("percentage"));
            List ernList = ernCrit.list();
            System.out.println("==" + ernList.size());
            for (int i = 0; i < ernList.size(); i++) {
                if (i % 2 == 0) {
                    classname = "rowColor1";
                } else {
                    classname = "rowColor2";
                }
                Earningslapdetails earObje = (Earningslapdetails) ernList.get(i);
                orderNo = earObje.getOrderno();
                effectDate = dateToString(earObje.getPeriodfrom());
                resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\"" + earObje.getAmountrangefrom() + "\" id=\"amountrangefrom" + (i + 1) + "\"  onblur=\"checkFloat(this.id,'Amount Range From');\" size=\"20\"/></td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\"" + earObje.getAmountrangeto() + "\"  id=\"amountrangeto" + (i + 1) + "\"  onblur=\"checkFloat(this.id,'Amount Range To');\" size=\"20\"/></td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\"" + earObje.getAmount() + "\"  id=\"slapamount" + (i + 1) + "\"  onblur=\"checkFloat(this.id,'Salp Amount');\" size=\"20\"/></td>").append("<td align=\"center\"><input type=\"text\" class=\"amounttextbox\" value=\"" + earObje.getPercentage() + "\"  id=\"slappercentage" + (i + 1) + "\"  value=\"0\" onblur=\"checkPercentage(this.id,'Slap Percentage');\" size=\"20\"/>&nbsp;&nbsp;%<input type=\"hidden\" id=\"hiddedid" + (i + 1) + "\" value=\"" + earObje.getId() + "\"></td>").append("</tr>");
            }
            resultHTML.append("</table>");
            resultHTML.append("</td></tr>");
            resultHTML.append("</table>");

            resultMap.put("orderNo", orderNo);
            resultMap.put("effectDate", effectDate);
            resultMap.put("totalrows", ernList.size());
            resultMap.put("createrows", resultHTML.toString());
        } catch (Exception e) {
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEarningsSlapDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String earningstypes, String effectdate, String[] amountrangefrom, String[] amountrangeto, String[] slapamount, String[] slappercentage, String orderno, String totalrows, String[] hiddeniarray, String funtype) {
        Map resultMap = new HashMap();
        Transaction transaction;
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
                    masterobj.setId(getmaxSequenceNumberFORslap(session));
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
            } else {
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
        }
        return resultMap;
    }

    public String getmaxSequenceNumberFORslap(Session session) {
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

    public Map loadRegionDetails(Session session) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        regionMap.put("ALL", "ALL");
        String regionid = "";
        String regionname = "";

        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.addOrder(Order.asc("id"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {
                regionid = lbobj.getId();
                regionname = lbobj.getRegionname();
                regionMap.put(regionid, regionname);
            }
            resultMap.put("regionlist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

//    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoadSections(Session session, String LoggedInRegion) {
//    public Map getLoadSections(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map sectionMap = new LinkedHashMap();
        sectionMap.put("0", "--Select--");
        try {
            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction(" (region='ALL' or region='" + LoggedInRegion + "')"));
            secCrit.add(Restrictions.sqlRestriction(" parentcode='0'"));
            secCrit.addOrder(Order.asc("id"));
            List<Sectionmaster> secList = secCrit.list();
            resultMap = new TreeMap();
            for (Sectionmaster lbobj : secList) {
                sectionMap.put(lbobj.getId(), lbobj.getSectionname());
            }
            resultMap.put("sectionlist", sectionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoadRegionSections(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regionid, String sectionid) {
        Map resultMap = new HashMap();
        resultMap.put("sectionDetails", getSectionDetailsinHTML(session, regionid, sectionid).toString());
        resultMap.put("sectionlist", getLoadSections(session, regionid).get("sectionlist"));
        resultMap.put("sectionid", sectionid);
        return resultMap;
    }
}
