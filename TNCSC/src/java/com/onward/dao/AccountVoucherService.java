/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

public interface AccountVoucherService {

    public Map getAccountsCodeListPartyDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String book, String period, String voucherdate, String vouchertype);

    public Map getVoucherReceiptDetailDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate);

    public Map getVoucherReceiptDetailDetailsPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String filePathwithName);

    public Map getLedgerDetailsByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype, String selectedRegion);
    
    public Map getConsolidatedLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype);

    public Map getTrailBalanceDetailsByRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto, String selectedRegion);

    public Map getConsolidatedTrailBalanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto);

    public Map getVoucherModifyDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String book, String period, String voucherdate, String vouchertype, String otherregion);

    public Map getVoucherEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String booktype);

    public Map saveVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate);

    public Map saveVoucherDetailsALLRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate, String otherregion);

    public Map saveAncillaryDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherdetailsid, String ancillaryid, String calamount, String ancillarycode, String ancillarytaxpercentage, String ancillaryamount);

    public Map getAncillaryDetailsforModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid);

    public Map getVoucherDetailsForModi(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String voucherid);

    public Map ModifyVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate);

    public Map saveModifyVoucherDetailsinALLRegion(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate, String otherregion);

    public Map getLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype);

    public Map getIRSLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto);

    public Map getIRSReconciledLedgerDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto);
    
    public Map getIRSReconciledLedgerDetailsExcellWrite(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String ledger, String voucherdatefrom, String voucherdateto, String filePath);

    public Map getLedgerEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getIRSLedgerEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map loadLedgerAccountsHeads(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getTrailBalanceEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getTrailBalanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdatefrom, String voucherdateto);

    public Map getProgressiveTrailBalanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String period, String voucherdateto);

    public Map getPartyLedgerEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getBankChallanEmptyTable(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String dateofchallan);

    public Map createBankChallan(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String checkBoxValues, String dateofchallan, String bank, String remarks, String periodcode);

    public Map loadChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String challanId);

    public Map removeFromChallan(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String recpId);

    public Map addToChallanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String recId, String challanno, String challandate);

    public Map AdjustmentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String chkarray, String chkarray1);

    public Map applyAdjustmentDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String chkarray, String chkarray1);

    public Map getPaybillVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String book, String period, String voucherdate, String vouchertype, String month, String year, String paybilltype);

    public String getPayrollVoucherCategory(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map SavePayrollVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String category, String voucherno);

    public Map checkPayrollVoucherDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String category);

    public Map prepareTrialBalanceCSV(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountingperiod, String accountingperiodfrom, String accountingperiodto, String filePathwithName);
}
