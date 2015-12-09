/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.ApplicationConstants;
import com.onward.common.CommonUtility;
import com.onward.common.DateUtility;
import com.onward.persistence.payroll.Designationmaster;
import com.onward.persistence.payroll.Employeeepfmaster;
import com.onward.persistence.payroll.Employeemaster;
import com.onward.persistence.payroll.Logincount;
import com.onward.persistence.payroll.Menumaster;
import com.onward.persistence.payroll.Menuprivilages;
import com.onward.persistence.payroll.Passwordhistory;
import com.onward.persistence.payroll.PasswordhistoryId;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.persistence.payroll.Sectionmaster;
import com.onward.persistence.payroll.Usermaster;
import com.onward.valueobjects.UserViewModel;
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
import org.hibernate.criterion.Restrictions;
import java.io.FileReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author root
 */
public class EmployeeMasterServiceImpl extends OnwardAction implements EmployeeMasterService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        Map sectionMap = new LinkedHashMap();
        Map designationMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        sectionMap.put("0", "--Select--");
        designationMap.put("0", "--Select--");
        String regionid = "";
        String sectionid = "";
        String designationid = "";
        String regionname = "";
        String sectionname = "";
        String designationname = "";
        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.like("id", "R%"));
            rgnCrit.addOrder(Order.asc("regionname"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {

                regionid = lbobj.getId();
                regionname = lbobj.getRegionname();


                regionMap.put(regionid, regionname);
            }

            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction("id not in ('S13','S14')"));
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
            Criteria desgCrit = session.createCriteria(Designationmaster.class);
            desgCrit.addOrder(Order.asc("designation"));
            List<Designationmaster> desigList = desgCrit.list();
            resultMap = new TreeMap();
            for (Designationmaster lbobj : desigList) {

                designationid = lbobj.getDesignationcode();
                designationname = lbobj.getDesignation();


                designationMap.put(designationid, designationname);
            }
            resultMap.put("regionlist", regionMap);
            resultMap.put("sectionlist", sectionMap);
            resultMap.put("designationlist", designationMap);
            resultMap.put("currentRegion", LoggedInRegion);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetailsforDeputation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        Map sectionMap = new LinkedHashMap();
        Map designationMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        sectionMap.put("0", "--Select--");
        designationMap.put("0", "--Select--");
        String regionid = "";
        String sectionid = "";
        String designationid = "";
        String regionname = "";
        String sectionname = "";
        String designationname = "";
        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.like("id", "R%"));
            rgnCrit.addOrder(Order.asc("regionname"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {

                regionid = lbobj.getId();
                regionname = lbobj.getRegionname();


                regionMap.put(regionid, regionname);
            }

            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction("id in ('S13','S14')"));
//            secCrit.add(Restrictions.sqlRestriction(" (region='ALL' or region='" + LoggedInRegion + "')"));
//            secCrit.add(Restrictions.sqlRestriction(" parentcode!='0'"));
            secCrit.addOrder(Order.asc("sectionname"));
            List<Sectionmaster> secList = secCrit.list();
            resultMap = new TreeMap();
            for (Sectionmaster lbobj : secList) {

                sectionid = lbobj.getId();
                sectionname = lbobj.getSectionname();


                sectionMap.put(sectionid, sectionname);
            }
            Criteria desgCrit = session.createCriteria(Designationmaster.class);
            desgCrit.addOrder(Order.asc("designation"));
            List<Designationmaster> desigList = desgCrit.list();
            resultMap = new TreeMap();
            for (Designationmaster lbobj : desigList) {

                designationid = lbobj.getDesignationcode();
                designationname = lbobj.getDesignation();


                designationMap.put(designationid, designationname);
            }
            resultMap.put("regionlist", regionMap);
            resultMap.put("sectionlist", sectionMap);
            resultMap.put("designationlist", designationMap);
            resultMap.put("currentRegion", LoggedInRegion);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeEPFNo(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        try {
            Criteria checkCrit = session.createCriteria(Employeemaster.class);
//            checkCrit.add(Restrictions.sqlRestriction("(region='" + LoggedInRegion + "' OR region='TRANS')"));
//            checkCrit.add(Restrictions.sqlRestriction("region='TRANS'"));
            checkCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
            List<Employeemaster> chkList = checkCrit.list();
            if (chkList.size() > 0) {
                Employeemaster empObj = chkList.get(0);
                if (empObj.getRegion().equalsIgnoreCase("TRANS")) {
                    resultMap.put("ERROR", "Given EPF Number is Transferred.");
//                if (empObj.getEpfno().equals(epfno)) {

//                    resultMap.put("ERROR", "Given EPF Number is alreay assigned to " + empObj.getEmployeename().toUpperCase());
                } else {
                    resultMap.put("ERROR", "Given EPF Number is already  in Employee Master.Currently this Employee in " + getRegionmaster(session, empObj.getRegion()).getRegionname() + " Region.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeGPFNo(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        try {
            Criteria checkCrit = session.createCriteria(Employeemaster.class);
            checkCrit.add(Restrictions.sqlRestriction("(region='" + LoggedInRegion + "' OR region='TRANS')"));
            checkCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));
            List<Employeemaster> chkList = checkCrit.list();
            if (chkList.size() > 0) {
                Employeemaster empObj = chkList.get(0);
                if (empObj.getEpfno().equalsIgnoreCase(epfno) && empObj.getRegion().equalsIgnoreCase("TRANS")) {
                    resultMap.put("ERROR", "Given GPF Number is already transfered to another Region.");
                } else if (empObj.getEpfno().equalsIgnoreCase(epfno)) {
                    resultMap.put("ERROR", "Given GPF Number is already in Employee Master.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String fpfno, String employeename, String fathername, String gender, String dateofbirth, String region,
            String section, String dateofappoinment, String dateofprobation, String dateofconfirmation, String designation,
            String community, String pancardno, String paymentmode, String bankcode, String banksbaccount, String empcategory, String positiondate, String positiontime, String nativeregion) {
        Map resultMap = new HashMap();
        Transaction transaction = null;

        try {

            if (empcategory.equalsIgnoreCase("G")) {
                transaction = session.beginTransaction();
                Employeemaster masterobj = new Employeemaster();
                masterobj.setEpfno(epfno);
                masterobj.setFpfno(fpfno);
                masterobj.setEmployeename(employeename);
                masterobj.setFathername(fathername);
                masterobj.setGender(gender);
                masterobj.setDateofbirth(postgresDate(dateofbirth));
                masterobj.setRegion(region);
                masterobj.setProcess(Boolean.TRUE);
                masterobj.setSection(section);
                masterobj.setDateofappoinment(postgresDate(dateofappoinment));
                masterobj.setDateofprobation(postgresDate(dateofprobation));
                masterobj.setCategory("R");
                masterobj.setDesignation(designation);
                masterobj.setCommunity(community);
                masterobj.setPancardno(pancardno);
                masterobj.setPaymentmode(paymentmode);
                masterobj.setBankcode(bankcode);
                masterobj.setBanksbaccount(banksbaccount);
                masterobj.setEmployeecode(epfno);
                masterobj.setCreatedby(LoggedInUser);
                masterobj.setCreateddate(getCurrentDate());
                session.saveOrUpdate(masterobj);
                transaction.commit();

                if (transaction.wasCommitted()) {
                    try {
                        this.epfnoLoginCreation(session, masterobj, epfno, LoggedInRegion, LoggedInUser);
                    } catch (Exception e) {
                        transaction.rollback();
                    }
                }
//                resultMap.put("success", "Employee Master Successfully Saved.and Login Created for " + epfno + ". Password is " + ApplicationConstants.DEFAULT_PASSWORD);
                resultMap.put("success", "Employee Master Successfully Saved");

            } else {
                Criteria empDetailsCrit = session.createCriteria(Employeeepfmaster.class);
                empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "'"));
                List empDetailsList = empDetailsCrit.list();
                if (empDetailsList.size() > 0) {
                    transaction = session.beginTransaction();
                    Employeemaster masterobj = new Employeemaster();
                    masterobj.setEpfno(epfno);
                    masterobj.setFpfno(fpfno);
                    masterobj.setEmployeename(employeename);
                    masterobj.setFathername(fathername);
                    masterobj.setGender(gender);
                    masterobj.setDateofbirth(postgresDate(dateofbirth));
                    masterobj.setRegion(region);
                    masterobj.setProcess(Boolean.TRUE);
                    masterobj.setSection(section);
                    masterobj.setDateofappoinment(postgresDate(dateofappoinment));
                    masterobj.setDateofprobation(postgresDate(dateofprobation));
                    masterobj.setCategory(empcategory);
                    masterobj.setDesignation(designation);
                    masterobj.setCommunity(community);
                    masterobj.setPancardno(pancardno);
                    masterobj.setPaymentmode(paymentmode);
                    masterobj.setBankcode(bankcode);
                    masterobj.setBanksbaccount(banksbaccount);
                    masterobj.setEmployeecode(epfno);
                    masterobj.setCreatedby(LoggedInUser);
                    masterobj.setCreateddate(getCurrentDate());
                    masterobj.setCurrentpositionjoindate(postgresDate(positiondate));
                    masterobj.setCurrentpositionjointime(positiontime);
                    masterobj.setNativeregion(nativeregion);
                    session.saveOrUpdate(masterobj);
                    transaction.commit();
                    if (transaction.wasCommitted()) {
                        try {
                            this.epfnoLoginCreation(session, masterobj, epfno, LoggedInRegion, LoggedInUser);
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }


//                    resultMap.put("success", "Employee Master Successfully Saved.and Login Created for " + epfno + ". Password is " + ApplicationConstants.DEFAULT_PASSWORD);
                    resultMap.put("success", "Employee Master Successfully Saved");
                } else {
                    resultMap.put("ERROR", "Given EPF Number is not in EPF Master.");
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Employee Master Transaction Faild");
        }
        return resultMap;
    }

    public Boolean epfnoLoginCreation(Session session, Employeemaster form4Obj, String epfno, String LoggedInRegion, String LoggedInUser) {
        Boolean blnResult = false;
        Transaction transaction = null;
        Key key1 = null;
        try {
            Criteria empDetailsCrit = session.createCriteria(Usermaster.class);
            empDetailsCrit.add(Restrictions.sqlRestriction("userid = '" + epfno + "'"));
            List empDetailsList = empDetailsCrit.list();
            if (empDetailsList.size() > 0) {
                blnResult = true;
            } else {

                transaction = session.beginTransaction();
                java.sql.Date dbToDaysDate = new java.sql.Date(new Date().getTime());
                String toDayDate = dateToString(dbToDaysDate);
                // Encrypt the Password
                byte[] encryptPassword = null;
                byte[] encryptPasswordKey = null;
                KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
                if (keyGenerator != null) {
                    keyGenerator.init(112);
                    key1 = keyGenerator.generateKey();
                    encryptPasswordKey = key1.getEncoded();
                }
                Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key1);
                encryptPassword = cipher.doFinal(ApplicationConstants.DEFAULT_PASSWORD.getBytes());

                Usermaster userMasterObj = new Usermaster();

//            userMasterObj.setCreatedby(userid);
                userMasterObj.setEmployeemaster(form4Obj);
                userMasterObj.setUserid(epfno);
                userMasterObj.setSalutationtype("1");
                userMasterObj.setUsername(form4Obj.getEmployeename());
                userMasterObj.setGender(form4Obj.getGender());
                userMasterObj.setDateofbirth(form4Obj.getDateofbirth());
                userMasterObj.setFathername(form4Obj.getFathername());
                userMasterObj.setCreateddate(dbToDaysDate);
                userMasterObj.setNextpwddate(postgresDate(DateUtility.addToDate(toDayDate, Calendar.YEAR, 1)));
                userMasterObj.setSequencenumber(1);
                userMasterObj.setUserstatus(intValue(ApplicationConstants.NEW_STATUS));
                userMasterObj.setEncryptpassword(CommonUtility.convertByteToHexString(encryptPassword));
                userMasterObj.setSecretkey(CommonUtility.convertByteToHexString(encryptPasswordKey));
//            userMasterObj.setEmployeeepfmaster(getEmployeemasterWithoutRegion(session, epfno));
                userMasterObj.setDesignation(form4Obj.getDesignation());
                userMasterObj.setSecretquestionone("5");
                userMasterObj.setYouranswerone("tncsc");
                userMasterObj.setYouranswertwo("tncsc");
                userMasterObj.setSecretquestiontwo("13");
                userMasterObj.setCreatedby(LoggedInUser);
                userMasterObj.setRegion(form4Obj.getRegion());

                Passwordhistory passwordhistory = new Passwordhistory();
                passwordhistory.setId(new PasswordhistoryId(epfno, 1));
                passwordhistory.setCreatedby(LoggedInUser);
                passwordhistory.setCreateddate(dbToDaysDate);
                passwordhistory.setNextpwddate(postgresDate(DateUtility.addToDate(toDayDate, Calendar.YEAR, 1)));
                passwordhistory.setUserstatus(intValue(ApplicationConstants.ACTIVE_STATUS));
//            passwordhistory.setUsertype(ApplicationConstants.ADMIN_USER);
                passwordhistory.setEncryptpassword(encryptPassword);
                passwordhistory.setSecretekey(encryptPasswordKey);


                Logincount logincount = new Logincount();
                logincount.setUserid(epfno);
                logincount.setCount(0);
                logincount.setLogindate(dbToDaysDate);
                logincount.setCreateddate(dbToDaysDate);
                logincount.setCreatedby(LoggedInUser);


                BeanUtils.copyProperties(passwordhistory, userMasterObj);

                session.save(userMasterObj);
                session.save(passwordhistory);
                session.save(logincount);
                transaction.commit();

                blnResult = true;
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            blnResult = false;
        }
        return blnResult;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "'"));
        empDetailsCrit.add(Restrictions.sqlRestriction("section not in ('S13','S14')"));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("fpfno", empmasterObj.getFpfno());
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("fathername", empmasterObj.getFathername());
            resultMap.put("gender", empmasterObj.getGender());
            resultMap.put("designation", empmasterObj.getDesignation());
            resultMap.put("region", empmasterObj.getRegion());
            resultMap.put("section", empmasterObj.getSection());
            resultMap.put("community", empmasterObj.getCommunity());
            resultMap.put("pancardno", empmasterObj.getPancardno());
            resultMap.put("paymentmode", empmasterObj.getPaymentmode());
            resultMap.put("bankcode", empmasterObj.getBankcode());
            resultMap.put("banksbaccount", empmasterObj.getBanksbaccount());
            resultMap.put("dateofbirth", dateToString(empmasterObj.getDateofbirth()));
            resultMap.put("empcategory", empmasterObj.getCategory());
            resultMap.put("doa", dateToString(empmasterObj.getDateofappoinment()));
            resultMap.put("dateofprobation", dateToString(empmasterObj.getDateofprobation()));
            resultMap.put("positiondate", dateToString(empmasterObj.getCurrentpositionjoindate()));
            resultMap.put("positiontime", empmasterObj.getCurrentpositionjointime());
            resultMap.put("nativeregion", empmasterObj.getNativeregion());
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong in this region. ");

        }

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getGovernmentEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "'"));
        empDetailsCrit.add(Restrictions.sqlRestriction("section  in ('S13','S14')"));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("fpfno", empmasterObj.getFpfno());
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("fathername", empmasterObj.getFathername());
            resultMap.put("gender", empmasterObj.getGender());
            resultMap.put("designation", empmasterObj.getDesignation());
            resultMap.put("region", empmasterObj.getRegion());
            resultMap.put("section", empmasterObj.getSection());
            resultMap.put("community", empmasterObj.getCommunity());
            resultMap.put("pancardno", empmasterObj.getPancardno());
            resultMap.put("paymentmode", empmasterObj.getPaymentmode());
            resultMap.put("bankcode", empmasterObj.getBankcode());
            resultMap.put("banksbaccount", empmasterObj.getBanksbaccount());
            resultMap.put("dateofbirth", dateToString(empmasterObj.getDateofbirth()));
            resultMap.put("empcategory", empmasterObj.getCategory());
            resultMap.put("doa", dateToString(empmasterObj.getDateofappoinment()));
            resultMap.put("dateofprobation", dateToString(empmasterObj.getDateofprobation()));
        } else {
            resultMap.put("ERROR", "Given GPF Number is Wrong. ");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDetailsFromEPFMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeeepfmaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "'"));
//        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeeepfmaster empmasterObj = (Employeeepfmaster) empDetailsList.get(0);
            resultMap.put("fpfno", empmasterObj.getFpfno());
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("fathername", empmasterObj.getFathername());
            resultMap.put("gender", empmasterObj.getGender());
            resultMap.put("designation", empmasterObj.getDesignation());
            resultMap.put("region", LoggedInRegion);
            resultMap.put("section", empmasterObj.getSection());
            resultMap.put("community", empmasterObj.getCommunity());
//            resultMap.put("pancardno", empmasterObj.getPancardno());
//            resultMap.put("paymentmode", empmasterObj.getPaymentmode());
//            resultMap.put("bankcode", empmasterObj.getBankcode());
//            resultMap.put("banksbaccount", empmasterObj.getBanksbaccount());
            resultMap.put("dateofbirth", dateToString(empmasterObj.getDateofbirth()));
//            resultMap.put("empcategory", empmasterObj.getCategory());
            resultMap.put("doa", dateToString(empmasterObj.getDateofappoinment()));
            resultMap.put("dateofprobation", dateToString(empmasterObj.getDateofprobation()));
        } else {
            resultMap.put("ERROR", "Given EPF Number is not in EPF Master.Please Contact Head Office. ");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getMenuDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, UserViewModel userObj) {
        System.out.println("************************ EmployeeMasterServiceImpl getMenuDetails method is calling ***********************");
        Map resultMap = new HashMap();
        StringBuffer htmlMenu = new StringBuffer();
        StringBuffer htmlnewMenu = new StringBuffer();
        String currentusermodule = (String) request.getSession(false).getAttribute("currentusermodule");
        if (currentusermodule == null || currentusermodule.isEmpty() || currentusermodule.length() <= 0) {
            currentusermodule = "0";
        } else {
            currentusermodule = currentusermodule.replaceAll("type", "");
        }
        htmlMenu.append("<ul id=\"navbar\">");
        htmlMenu.append("<li><a href=\"#\" onclick=\"homePage();\">Home</a></li>");
        Criteria menulevelCrit = session.createCriteria(Menumaster.class);
        menulevelCrit.add(Restrictions.sqlRestriction("parentcode = 0"));
        menulevelCrit.addOrder(Order.asc("code"));
        List menuLevelList = menulevelCrit.list();
        if (menuLevelList.size() > 0) {
            for (int i = 0; i < menuLevelList.size(); i++) {
                Menumaster menuMasterObj = (Menumaster) menuLevelList.get(i);

                Criteria menuprivilagesCrit = session.createCriteria(Menuprivilages.class);
//                menuprivilagesCrit.add(Restrictions.sqlRestriction("usertype = 1"));
                menuprivilagesCrit.add(Restrictions.sqlRestriction("usertype = " + Integer.parseInt(currentusermodule)));
//                menuprivilagesCrit.add(Restrictions.sqlRestriction("usertype = "+Integer.parseInt(userObj.getUsertype())));
                menuprivilagesCrit.add(Restrictions.sqlRestriction("menumaster =" + menuMasterObj.getCode()));
                menuprivilagesCrit.add(Restrictions.sqlRestriction("accessright is true"));
                List menuprivilagesList = menuprivilagesCrit.list();
                if (menuprivilagesList.size() > 0) {
                    Menuprivilages menuMasterObj1 = (Menuprivilages) menuprivilagesList.get(0);
                    htmlMenu.append("<li><a href=\"#\">" + menuMasterObj1.getMenumaster().getLabelname() + "</a><ul>");
                    htmlnewMenu.append(getMenuUIandLI(session, menuMasterObj1.getMenumaster().getCode(), currentusermodule));
                    htmlMenu.append(htmlnewMenu.toString());
                    htmlMenu.append("</ul></li>");
                    htmlnewMenu = new StringBuffer();
                }


            }

        }

//        List results = session.createCriteria(Menumaster.class)[0
//                    .setProjection( Projections.alias( Projections.groupProperty("color"), "colr" ) )
//                    .addOrder( Order.asc("colr") )
//                    .list();


        htmlMenu.append("<li><a href=\"#\" onclick=\"logOut();\">Logout</a></li>");
        htmlMenu.append("</ul>");
        //System.out.println("htmlMenu==" + htmlMenu);
        resultMap.put("menuhtml", htmlMenu.toString());
        return resultMap;
    }

    public StringBuffer getMenuUIandLI(Session session, int menucode, String currentusermodule) {
        StringBuffer menubuffer = new StringBuffer();

        Criteria menulevelCrit = session.createCriteria(Menumaster.class);
        menulevelCrit.add(Restrictions.sqlRestriction("parentcode = " + menucode));
        menulevelCrit.add(Restrictions.sqlRestriction("isvisible is true"));
        menulevelCrit.addOrder(Order.asc("menuorder"));
        List menuLevelList = menulevelCrit.list();
        if (menuLevelList.size() > 0) {

            for (int i = 0; i < menuLevelList.size(); i++) {

                Menumaster menuMasterObj = (Menumaster) menuLevelList.get(i);

                Criteria menuprivilagesCrit = session.createCriteria(Menuprivilages.class);
//                menuprivilagesCrit.add(Restrictions.sqlRestriction("usertype = 1"));
                menuprivilagesCrit.add(Restrictions.sqlRestriction("usertype = " + Integer.parseInt(currentusermodule)));
//                menuprivilagesCrit.add(Restrictions.sqlRestriction("usertype = "+Integer.parseInt(userObj.getUsertype())));
                menuprivilagesCrit.add(Restrictions.sqlRestriction("menumaster =" + menuMasterObj.getCode()));
                menuprivilagesCrit.add(Restrictions.sqlRestriction("accessright is true"));
                List menuprivilagesList = menuprivilagesCrit.list();
                if (menuprivilagesList.size() > 0) {
                    Menuprivilages menuMasterObj1 = (Menuprivilages) menuprivilagesList.get(0);

                    Criteria menulevelCrit1 = session.createCriteria(Menumaster.class);
                    menulevelCrit1.add(Restrictions.sqlRestriction("parentcode = " + menuMasterObj1.getMenumaster().getCode()));
                    List menuLevelList1 = menulevelCrit1.list();
                    if (menuLevelList1.size() > 0) {
                        menubuffer.append("<li><a href=\"#\">" + menuMasterObj1.getMenumaster().getLabelname() + "</a><ul>");
                        menubuffer.append(getMenuUIandLI(session, menuMasterObj1.getMenumaster().getCode(), currentusermodule));
                        menubuffer.append("</ul></li>");
                    } else {
                        menubuffer.append(" <li><a href=\"#\" onclick=\"getMenuRequest('" + menuMasterObj1.getMenumaster().getActionname() + "','" + menuMasterObj1.getMenumaster().getMethodname() + "','')\">" + menuMasterObj1.getMenumaster().getLabelname() + "</a>");
                        menubuffer.append("</li>");
                    }
                }
            }
        } else {
            Criteria menulevelCrit1 = session.createCriteria(Menumaster.class);
            menulevelCrit1.add(Restrictions.sqlRestriction("code = " + menucode));
            menulevelCrit1.addOrder(Order.asc("code"));
            List menuLevelList1 = menulevelCrit1.list();
            if (menuLevelList1.size() > 0) {
                Menumaster menuMasterObj = (Menumaster) menuLevelList1.get(0);
                menubuffer.append(" <li><a href=\"#\" onclick=\"getMenuRequest('" + menuMasterObj.getActionname() + "','" + menuMasterObj.getMethodname() + "','')\">" + menuMasterObj.getLabelname() + "</a>");
                menubuffer.append("</li>");
            }
        }
        return menubuffer;
    }

    public static int factorial(int N) {
        if (N == 1) {
            return 1;
        }
        return N * factorial(N - 1);
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
//        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("fpfno", empmasterObj.getFpfno());
            resultMap.put("fathername", empmasterObj.getFathername());
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");

        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveChangeEpfno(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String empcategory, String epfno, String newepfno) {
        Map resultMap = new HashMap();
        String updateprocessDetails = "";
        Transaction transaction = null;
        Employeemaster empmasterObj = null;
        Employeemaster newepfnomasterObj = null;
        Employeeepfmaster employeeepfmasterObj = null;
        Boolean blnResult = false;
        try {

            if (empcategory.equalsIgnoreCase("G")) {

                Criteria newempDetailsCrit = session.createCriteria(Employeemaster.class);
                newempDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + newepfno + "' "));
                //                    empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                List newempDetailsList = newempDetailsCrit.list();
                if (newempDetailsList.size() > 0) {
                    resultMap.put("ERROR", "Given EPF Number is Already in Employee Master.Please Contact Administrator");
                } else {
                    empmasterObj = getEmployeemasterWithoutRegion(session, epfno);
                    transaction = session.beginTransaction();
                    Employeemaster masterobj = new Employeemaster();
                    masterobj.setEpfno(newepfno);
                    masterobj.setFpfno(empmasterObj.getFpfno());
                    masterobj.setEmployeename(empmasterObj.getEmployeename());
                    masterobj.setFathername(empmasterObj.getFathername());
                    masterobj.setGender(empmasterObj.getGender());
                    masterobj.setDateofbirth(empmasterObj.getDateofbirth());
                    masterobj.setRegion(empmasterObj.getRegion());
                    masterobj.setProcess(empmasterObj.isProcess());
                    masterobj.setDesignation(empmasterObj.getDesignation());
                    masterobj.setBanksbaccount(empmasterObj.getBanksbaccount());
                    masterobj.setBankcode(empmasterObj.getBankcode());
                    masterobj.setCategory(empmasterObj.getCategory());
                    masterobj.setDateofappoinment(empmasterObj.getDateofappoinment());
                    masterobj.setDateofconfirmation(empmasterObj.getDateofconfirmation());
                    masterobj.setDateofprobation(empmasterObj.getDateofprobation());
                    masterobj.setCommunity(empmasterObj.getCommunity());
                    masterobj.setPancardno(empmasterObj.getPancardno());
                    masterobj.setEmployeecode(empmasterObj.getEmployeecode());                    
                    masterobj.setPaymentmode(empmasterObj.getPaymentmode());
                    masterobj.setSection(empmasterObj.getSection());
                    masterobj.setSubsection(empmasterObj.getSubsection());
                    masterobj.setNativeregion(empmasterObj.getNativeregion());                    
                    masterobj.setCreatedby(LoggedInUser);
                    masterobj.setCreateddate(getCurrentDate());
                    session.save(masterobj);
                    transaction.commit();

                    if (transaction.wasCommitted()) {
                        try {
                            this.epfnoLoginCreation(session, masterobj, newepfno, LoggedInRegion, LoggedInUser);
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }

                    if (transaction.wasCommitted()) {
                        blnResult = this.changeEpfnoinTables(session, epfno, newepfno, empcategory);
                        if (blnResult) {
                            resultMap.put("success", "This " + epfno + " EPF Number is Changed to " + newepfno + " EPF Number ");
                        } else {
                            resultMap.put("ERROR", "Transaction Failed in Change New Epf Number");
                        }
                    } else {
                        resultMap.put("ERROR", "Transaction Failed in Change New Epf Number");
                    }

                }

            } else {
                newepfnomasterObj = getEmployeemasterWithoutRegion(session, newepfno);
                employeeepfmasterObj = CommonUtility.getEmployeeepfmaster(session, newepfno);
                if (newepfnomasterObj != null) {
                    resultMap.put("ERROR", "Given EPF Number is Already in Employee Master.Please Contact Administrator");
                } else if (employeeepfmasterObj == null) {
                    resultMap.put("ERROR", "Given EPF Number is not in EPF Master.Please Contact Head Office. ");
                } else {
                    empmasterObj = getEmployeemasterWithoutRegion(session, epfno);
                    transaction = session.beginTransaction();
                    Employeemaster masterobj = new Employeemaster();
                    masterobj.setEpfno(newepfno);
                    masterobj.setFpfno(empmasterObj.getFpfno());
                    masterobj.setEmployeename(empmasterObj.getEmployeename());
                    masterobj.setFathername(empmasterObj.getFathername());
                    masterobj.setDateofbirth(empmasterObj.getDateofbirth());
                    masterobj.setDesignation(empmasterObj.getDesignation());
                    masterobj.setBanksbaccount(empmasterObj.getBanksbaccount());
                    masterobj.setBankcode(empmasterObj.getBankcode());
                    masterobj.setCategory(empmasterObj.getCategory());
                    masterobj.setGender(empmasterObj.getGender());
                    masterobj.setPaymentmode(empmasterObj.getPaymentmode());
                    masterobj.setSection(empmasterObj.getSection());
                    masterobj.setSubsection(empmasterObj.getSubsection());
                    masterobj.setRegion(empmasterObj.getRegion());
                    masterobj.setProcess(empmasterObj.isProcess());
                    masterobj.setDateofappoinment(empmasterObj.getDateofappoinment());
                    masterobj.setDateofconfirmation(empmasterObj.getDateofconfirmation());
                    masterobj.setDateofprobation(empmasterObj.getDateofprobation());
                    masterobj.setCommunity(empmasterObj.getCommunity());
                    masterobj.setPancardno(empmasterObj.getPancardno());
                    masterobj.setEmployeecode(empmasterObj.getEmployeecode());
                    masterobj.setNativeregion(empmasterObj.getNativeregion());
                    masterobj.setCreatedby(LoggedInUser);
                    masterobj.setCreateddate(getCurrentDate());
                    session.save(masterobj);
                    transaction.commit();

                    if (transaction.wasCommitted()) {
                        try {
                            this.epfnoLoginCreation(session, masterobj, newepfno, LoggedInRegion, LoggedInUser);
                        } catch (Exception e) {
                            transaction.rollback();
                        }
                    }

                    if (transaction.wasCommitted()) {
                        blnResult = this.changeEpfnoinTables(session, epfno, newepfno, empcategory);
                        if (blnResult) {
                            resultMap.put("success", "This " + epfno + " EPF Number is Changed to " + newepfno + " EPF Number ");
                        } else {
                            resultMap.put("ERROR", "Transaction Failed in Change New Epf Number");
                        }
                    } else {
                        resultMap.put("ERROR", "Transaction Failed in Change New Epf Number");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Transaction Failed in Change New Epf Number");
        }
        return resultMap;
    }

    public Boolean changeEpfnoinTables(Session session, String oldepfno, String newepfno, String empcategory) {
        Boolean blnResult = false;
        Transaction transaction = null;

        try {

            try {
                transaction = session.beginTransaction();
                session.createSQLQuery("UPDATE employeeaddress  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE employeeattendance  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE employeedeductionaccountcode  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE employeedesignation  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE employeeloansandadvances  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE employeeprovidentfundothers  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE employeerecoveries  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE employeetransferhistory  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
//            if (empcategory.equalsIgnoreCase("R")) {
//                if (transaction.wasCommitted()) {
//                    transaction = session.beginTransaction();
//                    try {
//                        session.createSQLQuery("UPDATE epfloanapplication  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
//                        transaction.commit();
//                    } catch (Exception e) {
//                        transaction.rollback();
//                    }
//                }
//                if (transaction.wasCommitted()) {
//                    transaction = session.beginTransaction();
//                    try {
//                        session.createSQLQuery("UPDATE epfloanapplication  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
//                        transaction.commit();
//                    } catch (Exception e) {
//                        transaction.rollback();
//                    }
//                }
//                if (transaction.wasCommitted()) {
//                    transaction = session.beginTransaction();
//                    try {
//                        session.createSQLQuery("UPDATE epfopeningbalance  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
//                        transaction.commit();
//                    } catch (Exception e) {
//                        transaction.rollback();
//                    }
//                }
//            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE miscdeductions  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE payrollprocessingdetails  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE salarydeductionothers  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE salarystructure  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE salarystructureactual  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE stoppayrolldetails  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE supplementatypaybill  SET employeeprovidentfundnumber  = '" + newepfno + "'  WHERE employeeprovidentfundnumber='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE surrenderbill  SET epfno  = '" + newepfno + "'  WHERE epfno='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("delete from  passwordhistory   WHERE userid='" + oldepfno + "'").executeUpdate();
//                    session.createSQLQuery("UPDATE passwordhistory  SET userid  = '" + newepfno + "'  WHERE userid='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("delete from  logincount   WHERE userid='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }

            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("UPDATE useroperatingrights  SET userid  = '" + newepfno + "'  WHERE userid='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("delete from  usermaster   WHERE userid='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            if (transaction.wasCommitted()) {
                transaction = session.beginTransaction();
                try {
                    session.createSQLQuery("delete from  employeemaster   WHERE epfno='" + oldepfno + "'").executeUpdate();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
            blnResult = transaction.wasCommitted();

        } catch (Exception ex) {
            Logger.getLogger(EmployeeMasterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return blnResult;
    }
}
