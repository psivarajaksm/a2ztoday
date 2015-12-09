/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.util;

//import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author onward
 */
public class MainpowerUtil implements NewInterface {
//    @GlobalDBOpenCloseAndUserPrivilages
    public String test(Session session, String reqStatus) {       
        String classname = "";
        StringBuffer existingRequitionHTML = new StringBuffer();
//        int projecttableid = 0;
//        Criteria pdCrit = session.createCriteria(com.onward.persistence.manpowerrequirement.Manpowerrequirement.class);
//        pdCrit.add(Restrictions.sqlRestriction("requirementstatus " + reqStatus + " "));
//        pdCrit.addOrder(Order.desc("requestid"));
//        List pdList = pdCrit.list();
//        if (pdList.size() > 0) {
//            System.out.println("safgfsa  asg    asfgas asga asga ash");
//            existingRequitionHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\">");
//            for (int i = 0; i < pdList.size(); i++) {
//                if (i % 2 == 0) {
//                    classname = "rowColor1";
//                } else {
//                    classname = "rowColor1";
//                }
//
//                Manpowerrequirement manpowerrequirementObj = (Manpowerrequirement) pdList.get(i);
//                projecttableid++;
//                existingRequitionHTML.append("<tr id=\"PROJECT_ID_" + projecttableid + "\"  class = \"" + classname + "\" onclick=\"getRequitiosionDetails('" + manpowerrequirementObj.getRequestid() + "'," + projecttableid + ")\" >");
//                existingRequitionHTML.append("<td width=\"100%\" align=\"left\">" + (manpowerrequirementObj.getRequisitionname()) + "</td>");
//                existingRequitionHTML.append("</tr>");
//                manpowerrequirementObj = null;
//            }
//            existingRequitionHTML.append("</table>");
//        } else {
//            existingRequitionHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\">");
//            existingRequitionHTML.append("<tr class=\"datanotfound\"  >");
//            existingRequitionHTML.append("<td width=\"100%\" align=\"center\">Data not found</td>");
//            existingRequitionHTML.append("</tr>");
//            existingRequitionHTML.append("</table>");
//        }
//        existingRequitionHTML.append("<input type=\"hidden\" name=\"projecttableid\" id=\"projecttableid\" value=\"" + projecttableid + "\">");
//        System.out.println(existingRequitionHTML.toString());
        return existingRequitionHTML.toString();
    }
}
