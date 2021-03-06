/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

/**
 *
 * @author Prince vijayakumar M
 */
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

public interface AccountReportService {

    public Map paymentcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName);

    public Map paymentcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName,String selectedRegion);
    
    public Map receiptcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName, String selectedRegion);
    
    public Map bankcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName,String selectedRegion);
    
    public Map journalcashBookPrintoutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName,String selectedRegion);
    
    public Map LedgerPrintOutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbookno, String period, String fromdate, String todate, String reporttype, String filePathwithName, String selectedRegion);
    
    public Map consolidatedLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbookno, String period, String fromdate, String todate, String reporttype, String filePathwithName);
    
    public LinkedList trialBalancePrintOutByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode,String selectedRegion);
   
    public LinkedList consolidatedTrialBalancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode);
    
    public Map getVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String voucherno);

    public Map getVoucherDetailsByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String voucherno,String selectedRegion);
    
    public Map voucherPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherno, String filePathwithName);

    public Map getJournalDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String journalno);

    public Map journalPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String journalno, String filePathwithName);

    public Map getBankDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String bankno);

    public Map bankPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String bankno, String filePathwithName);

    public Map getReceiptDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbooktype, String receiptno);

    public Map ReceiptPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String receiptno, String filePathwithName);

    public String getBookType(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map receiptcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName);

    public Map bankcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName);

    public Map journalcashBookPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String cashbookname, String cashbooktype, String filePathwithName);

    public Map PurchasePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String breakuptype, String filePathwithName);

    public Map PurchaseAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName);

    public Map SalesTaxPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String breakuptype, String filePathwithName);

    public Map BankChallanCashPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String challanno, String filePathwithName);

    public Map BankChallanChequePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String challanno, String filePathwithName);

    public Map LedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountbookno, String period, String fromdate, String todate, String reporttype, String filePathwithName);

    public LinkedList trialBalancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode);

    public LinkedList progressivetrialBalancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode);

    public LinkedList ledgerPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode, String accountcode);

    public Map getReceiptHeadWiseAbstractPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode);

    public Map getCashBookAbstractPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode, String ctype, String accbook);

    public Map PurchaseVatBreakupPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String percentage, String filePathwithName);

    public Map SalesVatBreakupPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String percentage, String filePathwithName);

    public Map PurchaseVatAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName);

    public Map SalesVatAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName);

    public String getAccountBook(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String challantype, String challanno);

    public Map PurchaseVatExportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName);

    public Map SalesVatExportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName);

    public Map PaymentRealizationPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String periodsele, String voucherdatefrom, String voucherdateto, String book, String realizationstatus, String filePathwithName);

    public Map ReceiptRealizationPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String realizationstatus, String filePathwithName);

    public Map ChequeRegisterPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String periodsele, String voucherdatefrom, String voucherdateto, String book, String filePathwithName);

    public Map ReceiptRealizationReportPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String filePathwithName);
    
    public Map PaymentRealizationAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String periodsele, String voucherdatefrom, String voucherdateto, String book, String realizationstatus);
    
    public Map getRealizationAbstractPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String fromdate, String todate, String periodcode, String ctype, String accbook);
}
