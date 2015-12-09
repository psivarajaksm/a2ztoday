/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onward.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author root
 */
public interface AccountsMasterService {
    public Map loadAccountGroupHeadDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map saveAccountHeadMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountcode, String ledgercode, String accountheadname,String serialno);
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
    public Map getRegionBankDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String regionid);
    public Map saveBankLedgerMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regioncode, String bankname,String bankcode);
    public Map getRegionPartiesDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String regionid);
    public Map savePartyLedgerMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regioncode, String bankname,String bankcode,String tinno);
    public Map getAccountBooksDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);
}
