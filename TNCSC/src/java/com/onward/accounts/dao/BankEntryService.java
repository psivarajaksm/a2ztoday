/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import java.io.File;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.upload.FormFile;
import org.hibernate.Session;

/**
 *
 * @author root
 */
public interface BankEntryService {

    public Map getBankDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map uploadReconciliationFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String bankname, String serverPath);
}
