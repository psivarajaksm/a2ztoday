/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.dao;

import com.onward.action.OnwardAction;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;

import com.onward.persistence.payroll.Regionmaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.upload.FormFile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class FileUploadServiceImpl extends OnwardAction implements FileUploadService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        String regionid = "";        
        String regionname = "";
        
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
            resultMap.put("regionlist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map getExistingDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String month, String year) {
//        Map resultMap = new HashMap();
//        StringBuffer resultHTML = new StringBuffer();
//        String classname = "";
//        try {
//
//            Criteria epfCrit = session.createCriteria(Epf.class);
//            epfCrit.add(Restrictions.sqlRestriction("regioncode = '" + region + "'"));
//            epfCrit.add(Restrictions.sqlRestriction("month ='" + month.toString()+"'"));
////            epfCrit.add(Restrictions.sqlRestriction("month =" + Integer.parseInt(month.toString())));
//            epfCrit.add(Restrictions.sqlRestriction("year ='" + year.toString()+"'"));
////            epfCrit.add(Restrictions.sqlRestriction("year =" + Integer.parseInt(month.toString())));
//            epfCrit.addOrder(Order.asc("epfno"));
//            List epfList = epfCrit.list();
//            if (epfList.size() > 0) {
//                    resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr><td valign=\"top\">")
//                        .append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">")
//                        .append("<tr class=\"gridmenu\">")
//                        .append("<td>S.No</td>")        //                        .append("<td style=\"cursor:pointer;\">EPF No</td>")
//                        .append("<td>Region Name</td>")
//                        .append("<td>Month</td>")
//                        .append("<td>EpfNo</td>")
//                        .append("<td>emp_sal</td>")
//                        .append("<td>emp_epf</td>")
//                        .append("<td>emp_fpf</td>")
//                        .append("<td>emp_rl</td>")
//                        .append("<td>emp_rfl</td>")
//                        .append("<td>emp_vpf</td>")
//                        .append("<td>emp_davpf</td>")
//                        .append("<td>emp_ecpf</td>")
//                        .append("<td>emp_ecfb</td>")
//                        .append("<td>emp_nrl</td>")
//                        .append("</tr>");
//                    for (int i = 0; i < epfList.size(); i++) {
//                        Epf edfObj = (Epf) epfList.get(i);
//
//                        if (i % 2 == 0) {
//                            classname = "rowColor1";
//                        } else {
//                            classname = "rowColor2";
//                        }
////                        System.out.println("name===" + regObj.getRegionname());
//                         resultHTML.append("<tr class=\"" + classname + "\">");
//                         resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
//                         resultHTML.append("<td align=\"center\">" + edfObj.getRegionmaster().getRegionname()+ "</td>");
//                         resultHTML.append("<td align=\"center\">" + edfObj.getMonth()+"'"+edfObj.getYear() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEpfno() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpSal() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpEpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpFpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpRl() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpRfl() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpVpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpDavpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpEcpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpEcfb() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpNrl() + "</td>");
//                         resultHTML.append("</tr>");
//                    }
//                resultHTML.append("</table>");
//                resultHTML.append("</tr></td>");
//                resultHTML.append("</table>");
//
//                resultMap.put("display", resultHTML.toString());
//                resultMap.put("isRecord", "Yes");
//            }else{
//                resultMap.put("ERROR", "No Records to found on this month");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return resultMap;
//    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map upLoadTxtFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, FormFile filename) {
//        Map resultMap = new HashMap();
//        Map displayMap = new HashMap();
//        System.out.println("========" + filename);
//        Epf epfObj = null;
//        int i = 1;
//
//        FileOutputStream fop = null;
//        try {
//
//            String respectivePath = getFilePath(request, LoggedInRegion);
//
//            FormFile formFile = (FormFile) filename;
//
//            byte[] byteFile = formFile.getFileData();
//            File f = new File(respectivePath);
//            fop = new FileOutputStream(f);
//            fop.write(byteFile);
//            fop.close();
//
//            Scanner scanner = new Scanner(new FileReader(f));
//
//            try {
//                while (scanner.hasNextLine()) {
//                    String lineByline=checkNull(scanner.nextLine());
//                    if(lineByline.trim()!=""){
//                        epfObj = processLine(lineByline, session);
//                        displayMap.put(i, epfObj);
//                        i++;
//                    }
//                }
//                request.getSession().setAttribute("displayFileMap", displayMap);
////                resultMap.put("displayHTML", getDetailsInHTML(displayMap));
//                resultMap.put("message", "success");
//            } finally {
//                scanner.close();
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            resultMap.put("message", "Failed");
////            request.setAttribute("message", "Failed to upload");
//        } finally {
//        }
//
//
//
////        resultMap.put("success", "success");
//        return resultMap;
//    }

    private String getFilePath(HttpServletRequest request, String LoggedInRegion) {
        String path = request.getRealPath("/");
        System.out.println("path====" + path);
        String sessionid = request.getSession(false).getId();
        String osName = System.getProperty("os.name");
        String fileName = "testexcel";
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
        System.out.println(" resPath : " + resPath);
        return resPath;
    }

//    protected Epf processLine(String aLine, Session session) {
//        //use a second Scanner to parse the content of each line
//        System.out.println("====" + aLine);
//        Epf epfobj = new Epf();
//        try {
//            Scanner scanner = new Scanner(aLine);
//            while (scanner.hasNext()) {
//                scanner.useDelimiter(",");
//                Regionmaster regionObj= new Regionmaster();
//                regionObj.setId(scanner.next().trim());
////                Transaction transaction = session.beginTransaction();
//
//                epfobj.setRegionmaster(regionObj);
//                epfobj.setMonth(scanner.next().trim());
//                epfobj.setYear(scanner.next().trim());
//                epfobj.setEpfno(scanner.next().trim());
//                epfobj.setEmpSal(scanner.next().trim());
//                epfobj.setEmpEpf(scanner.next().trim());
//                epfobj.setEmpFpf(scanner.next().trim());
//                epfobj.setEmpRl(scanner.next().trim());
//                epfobj.setEmpRfl(scanner.next().trim());
//                epfobj.setEmpVpf(scanner.next().trim());
//                epfobj.setEmpDavpf(scanner.next().trim());
//                epfobj.setEmpEcpf(scanner.next().trim());
//                epfobj.setEmpEcfb(scanner.next().trim());
//                epfobj.setEmpNrl(scanner.next().trim());
////                session.saveOrUpdate(epfobj);
////                transaction.commit();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return epfobj;
//
//    }

//    public String displayTextFileDatas(Map displayMap) {
//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map displayTextFileDatas(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser){
//        Map resultMap = new HashMap();
//        int i = 0;
//        String classname = "";
//        StringBuffer resultHTML = new StringBuffer();
//        Map displayMap = new HashMap();
//        try {
//            displayMap = (Map) request.getSession().getAttribute("displayFileMap");
//
//            TreeSet<Integer> keys = new TreeSet<Integer>(displayMap.keySet());
//                resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
//                resultHTML.append("<tr><td valign=\"top\">")
//                        .append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">")
//                        .append("<tr class=\"gridmenu\">")
//                        .append("<td>S.No</td>")        //                        .append("<td style=\"cursor:pointer;\">EPF No</td>")
//                        .append("<td>Region Name</td>")
//                        .append("<td>Month</td>")
//                        .append("<td>EpfNo</td>")
//                        .append("<td>emp_sal</td>")
//                        .append("<td>emp_epf</td>")
//                        .append("<td>emp_fpf</td>")
//                        .append("<td>emp_rl</td>")
//                        .append("<td>emp_rfl</td>")
//                        .append("<td>emp_vpf</td>")
//                        .append("<td>emp_davpf</td>")
//                        .append("<td>emp_ecpf</td>")
//                        .append("<td>emp_ecfb</td>")
//                        .append("<td>emp_nrl</td>")
//                        .append("</tr>");
//            if (displayMap.size() > 0) {
//                 for (Integer objkey : keys) {
//                        Epf edfObj = (Epf) displayMap.get(objkey);
//                        Regionmaster regObj=null;
//                        Criteria regCrit = session.createCriteria(Regionmaster.class);
//                        regCrit.add(Restrictions.sqlRestriction("id='"+edfObj.getRegionmaster().getId()+"'"));
//                        List regList = regCrit.list();
//                        if (regList.size() > 0) {
//                            regObj= (Regionmaster) regList.get(0);
//                        }
//
//                        System.out.println("edfObj.getEpfno()=="+edfObj.getEpfno());
//                        if (i % 2 == 0) {
//                            classname = "rowColor1";
//                        } else {
//                            classname = "rowColor2";
//                        }
//                        System.out.println("name===" + regObj.getRegionname());
//                         resultHTML.append("<tr class=\"" + classname + "\">");
//                         resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
//                         resultHTML.append("<td align=\"center\">" + regObj.getRegionname()+ "</td>");
//                         resultHTML.append("<td align=\"center\">" + edfObj.getMonth()+"'"+edfObj.getYear() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEpfno() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpSal() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpEpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpFpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpRl() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpRfl() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpVpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpDavpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpEcpf() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpEcfb() + "</td>");
//                         resultHTML.append("<td align=\"right\">" + edfObj.getEmpNrl() + "</td>");
//                         resultHTML.append("</tr>");
//
//                     i++;
//                 }
//            }
////            resultHTML.append("<tr><td><input type=\"button\" class=\"submitbu\" name=\"upload\" id=\"upload\" value=\"Upload Data\" onclick=\"uploadFile()\"></td></tr>");
//            resultHTML.append("</table>");
//            resultHTML.append("</tr></td>");
//            resultHTML.append("</table>");
//
//            resultMap.put("display", resultHTML.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return resultMap;
//    }

//    @GlobalDBOpenCloseAndUserPrivilages
//    public Map uploadTxtFileDatatoDB(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String isolddata,String region,String month,String year) {
//        Map resultMap = new HashMap();
//        Map displayMap = new HashMap();
//        try {
//            displayMap = (Map) request.getSession().getAttribute("displayFileMap");
//            if(isolddata.equalsIgnoreCase("1")){
//                Criteria epfCrit = session.createCriteria(Epf.class);
//                epfCrit.add(Restrictions.sqlRestriction("regioncode = '" + region + "'"));
//                epfCrit.add(Restrictions.sqlRestriction("month ='" + month.toString()+"'"));
//                epfCrit.add(Restrictions.sqlRestriction("year ='" + year.toString()+"'"));
//                List epfList = epfCrit.list();
//                if (epfList.size() > 0) {
//                    for (int i = 0; i < epfList.size(); i++) {
//                        Transaction transaction = session.beginTransaction();
//                        Epf edfObj = (Epf) epfList.get(i);
//                        edfObj.setCancelled(Boolean.TRUE);
//                        session.update(edfObj);
//                        transaction.commit();
//                    }
//                }
//            }
//
//            TreeSet<Integer> keys = new TreeSet<Integer>(displayMap.keySet());
//            if (displayMap.size() > 0) {
//                for (Integer objkey : keys) {
//                    Epf edfObj = (Epf) displayMap.get(objkey);
//                    Transaction transaction = session.beginTransaction();
//                    session.save(edfObj);
//                    transaction.commit();
//
//                }
//                resultMap.put("SUCCESS", "Successfully uploaded");
//                request.getSession().removeAttribute("displayFileMap");
//            }else{
//                resultMap.put("ERROR", " Transaction Failed");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultMap;
//    }
}
