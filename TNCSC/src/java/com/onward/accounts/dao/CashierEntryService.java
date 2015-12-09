/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author root
 */
public interface CashierEntryService {

    public Map getVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherno);

    public Map getVoucherDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid);

    public Map getAccountsCodeListPartyDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

//    public Map ModifyVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode);
    public Map saveModifiedVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherType, String voucherid, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String vouchernumber, String voucherdate, String chequedate, String partyalias);

    public Map getAccountBook(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getVoucherAccountsDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accounttype, String accountdate, String accountbooktype);

    public Map getVoucherDetailsForCancel(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accounttype, String accountdate, String accountbooktype);

    public Map saveAccountBookChange(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbooktype, String voucherids, String accountdate);

    public Map saveVoucherCancellation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbooktype, String voucherids, String accountdate, String vouchertype);

    public Map getReceiptDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map saveReceiptRealizationDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String realizationdate);

    public Map getPaymentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto, String book);

    public Map savePaymentRealizationDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String realizationdate, String period, String voucherdatefrom, String voucherdateto, String book);

    public Map changeChequeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String chequedate, String period, String voucherdatefrom, String voucherdateto, String book,String chequeno,String accbank);

    public Map getChequeRegister(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto, String book);

    public Map getVoucherDetailsForModify(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String accountbooktype);

    public Map changeVoucherDateandnoDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid, String voucherno, String voucherapproveddate, String fromdate, String todate, String accbook);

    public Map loadBanks(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getRemittanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String fromdate,String todate);

    public Map getRemittanceEntryDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser,String fromdate,String todate);

    public Map saveRemittanceDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String challanid, String remittancedate,String fromdate,String todate);
    
    public Map getPaymentAbstractDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String book);
    
    public Map savePaymentRealizationAbstractDate(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String recpayid, String realizationdate, String period, String book, String dbtype);
}
