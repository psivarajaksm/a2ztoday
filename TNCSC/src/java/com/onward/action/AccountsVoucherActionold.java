/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.AccountVoucherService;
import com.onward.dao.BonusBillService;
import com.onward.dao.OeslModule;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import javax.servlet.ServletOutputStream;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author root
 */
public class AccountsVoucherActionold extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    AccountVoucherService accountVoucherService = (AccountVoucherService) injector.getInstance(AccountVoucherService.class);

    public ActionForward employeeBonusPDPPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("employeeBonusPDPPage");
    }

    public Map getAccountsCodeListPartyDetails(HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getAccountsCodeListPartyDetails(null, request, response, null, null);
    }

    public Map getVoucherEmptyTable(String type, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getVoucherEmptyTable(null, request, response, null, null, type);
    }

    public Map getBankChallanEmptyTable(HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getBankChallanEmptyTable(null, request, response, null, null);
    }

    public Map getChallanDetails(String type, String dateofchallan, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getChallanDetails(null, request, response, null, null, type, dateofchallan);
    }

    public Map createBankChallan(String type, String checkBoxValues, String dateofchallan, String bank, String remarks, String period, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.createBankChallan(null, request, response, null, null, type, checkBoxValues, dateofchallan, bank, remarks, period);
    }

    public Map loadChallanDetails(String type, String challanId, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.loadChallanDetails(null, request, response, null, null, type, challanId);
    }

    public Map removeFromChallan(String type, String recpId, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.removeFromChallan(null, request, response, null, null, type, recpId);
    }

    public Map addToChallanDetails(String type, String recId, String challanno, String challandate, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.addToChallanDetails(null, request, response, null, null, type, recId, challanno, challandate);
    }

    public Map getVoucherDetails(String book, String period, String voucherdate, String vouchertype, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getVoucherDetails(null, request, response, null, null, book, period, voucherdate, vouchertype);
    }

    public Map AdjustmentDetails(String chkarray, String chkarray1, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.AdjustmentDetails(null, request, response, null, null, chkarray, chkarray1);
    }

    public Map applyAdjustmentDetails(String chkarray, String chkarray1, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.applyAdjustmentDetails(null, request, response, null, null, chkarray, chkarray1);
    }

    public Map saveVoucherDetails(String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("updationtype===" + updationtype);
        if (updationtype.equalsIgnoreCase("ADD")) {
            return accountVoucherService.saveVoucherDetails(null, request, response, null, null, period, booktype, voucherdate, voucherType, updationtype, voucherid, naaration, accountscode, accountsamount, accountsoption, partycode, partyamount, partypaymentmode, refno, bankcode, sanctionedby, fileno, partyalias, chequedate);
        } else {
            return accountVoucherService.ModifyVoucherDetails(null, request, response, null, null, period, booktype, voucherdate, voucherType, updationtype, voucherid, naaration, accountscode, accountsamount, accountsoption, partycode, partyamount, partypaymentmode, refno, bankcode, sanctionedby, fileno, partyalias, chequedate);
        }

    }

    public Map ModifyVoucherDetails(String period, String booktype, String voucherdate, String voucherType, String updationtype, String voucherid, String naaration, String accountscode, String accountsamount, String accountsoption, String partycode, String partyamount, String partypaymentmode, String refno, String bankcode, String sanctionedby, String fileno, String partyalias, String chequedate, String otherregion, HttpServletRequest request, HttpServletResponse response) {
        if (updationtype.equalsIgnoreCase("ADD")) {
            return accountVoucherService.saveVoucherDetailsALLRegion(null, request, response, null, null, period, booktype, voucherdate, voucherType, updationtype, voucherid, naaration, accountscode, accountsamount, accountsoption, partycode, partyamount, partypaymentmode, refno, bankcode, sanctionedby, fileno, partyalias, chequedate, otherregion);
        } else {
            return accountVoucherService.saveModifyVoucherDetailsinALLRegion(null, request, response, null, null, period, booktype, voucherdate, voucherType, updationtype, voucherid, naaration, accountscode, accountsamount, accountsoption, partycode, partyamount, partypaymentmode, refno, bankcode, sanctionedby, fileno, partyalias, chequedate, otherregion);
        }

    }

    public Map saveAncillaryDetails(String voucherdetailsid, String ancillaryid, String calamount, String ancillarycode, String ancillarytaxpercentage, String ancillaryamount, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.saveAncillaryDetails(null, request, response, null, null, voucherdetailsid, ancillaryid, calamount, ancillarycode, ancillarytaxpercentage, ancillaryamount);
    }

    public Map getAncillaryDetailsforModi(String voucherid, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getAncillaryDetailsforModi(null, request, response, null, null, voucherid);
    }

    public Map getVoucherDetailsForModi(String voucherid, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getVoucherDetailsForModi(null, request, response, null, null, voucherid);
    }

    public Map getLedgerDetailsMap(String period, String ledger, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************************AccountsVoucherAction***getLedgerDetailsMap*************");
        return accountVoucherService.getLedgerDetails(null, request, response, null, null, period, ledger, voucherdatefrom, voucherdateto, "2");
    }
    public Map getLedgerMap(String period, String ledger, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************************AccountsVoucherAction***getLedgerMap*************");
        return accountVoucherService.getLedgerDetails(null, request, response, null, null, period, ledger, voucherdatefrom, voucherdateto, "2");
    }

    public Map getIRSLedgerDetails(String period, String ledger, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getIRSLedgerDetails(null, request, response, null, null, period, ledger, voucherdatefrom, voucherdateto);
    }

    public Map getIRSReconciledLedgerDetails(String period, String ledger, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getIRSReconciledLedgerDetails(null, request, response, null, null, period, ledger, voucherdatefrom, voucherdateto);
    }
    public Map getLedgerDetails(String period, String ledger, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************************AccountsVoucherAction***getLedgerDetails*************" );
        return accountVoucherService.getLedgerDetails(null, request, response, null, null,  period, ledger, voucherdatefrom, voucherdateto,"1");
    }
    public Map getLedgerGroupDetails(String period, String ledger, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************************AccountsVoucherAction***getLedgerGroupDetails*************");
        return accountVoucherService.getLedgerDetails(null, request, response, null, null,  period, ledger, voucherdatefrom, voucherdateto,"2");
    }

    public Map getLedgerEmptyTable(HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getLedgerEmptyTable(null, request, response, null, null);
    }

    public Map getIRSLedgerEmptyTable(HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getIRSLedgerEmptyTable(null, request, response, null, null);
    }

    public Map loadLedgerAccountsHeads(HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.loadLedgerAccountsHeads(null, request, response, null, null);
    }

    public Map getTrailBalanceEmptyTable(HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getTrailBalanceEmptyTable(null, request, response, null, null);
    }

    public Map getTrailBalanceDetails(String period, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getTrailBalanceDetails(null, request, response, null, null, period, voucherdatefrom, voucherdateto);
    }

    public Map getProgressiveTrailBalanceDetails(String period, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getProgressiveTrailBalanceDetails(null, request, response, null, null, period, voucherdateto);
    }
//    public Map getTrailBalanceDetails(String book, String period, String voucherdatefrom, String voucherdateto, HttpServletRequest request, HttpServletResponse response) {
//        return accountVoucherService.getTrailBalanceDetails(null, request, response, null, null, book, period, voucherdatefrom, voucherdateto);
//    }

    public Map getPartyLedgerEmptyTable(HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getPartyLedgerEmptyTable(null, request, response, null, null);
    }

    public Map getVoucherDetailsAllRegion(String book, String period, String voucherdate, String vouchertype, String selectionregion, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getVoucherModifyDetails(null, request, response, null, null, book, period, voucherdate, vouchertype, selectionregion);
    }

    public Map getPaybillVoucherDetails(String book, String period, String voucherdate, String vouchertype, String month, String year, String paybilltype, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.getPaybillVoucherDetails(null, request, response, null, null, book, period, voucherdate, vouchertype, month, year, paybilltype);
    }

    public Map SavePayrollVoucherDetails(String year, String month, String category, String voucherno, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.SavePayrollVoucherDetails(null, request, response, null, null, year, month, category, voucherno);
    }

    public Map checkPayrollVoucherDetails(String year, String month, String category, HttpServletRequest request, HttpServletResponse response) {
        return accountVoucherService.checkPayrollVoucherDetails(null, request, response, null, null, year, month, category);
    }
}
