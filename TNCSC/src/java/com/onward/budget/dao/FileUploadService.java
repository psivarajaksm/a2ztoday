/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onward.budget.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.upload.FormFile;
import org.hibernate.Session;

/**
 *
 * @author root
 */
public interface FileUploadService {
    public Map loadRegionDetails(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
//    public Map displayTextFileDatas(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
//    public Map uploadTxtFileDatatoDB(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String isolddata,String region,String month,String year);
//    public Map upLoadTxtFile(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,FormFile filename);
//    public Map getExistingDatas(Session session,HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String region,String month,String year);
}
