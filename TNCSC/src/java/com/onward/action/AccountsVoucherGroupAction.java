/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.AccountVoucherService;
import com.onward.dao.OeslModule;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 
 */
public class AccountsVoucherGroupAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    AccountVoucherService accountVoucherService = (AccountVoucherService) injector.getInstance(AccountVoucherService.class);

    public Map getLedgerDetailsMap(String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************************AccountsVoucherAction***getLedgerDetailsMap*************");
        return accountVoucherService.getLedgerDetails(null, request, response, null, null, period, ledger, voucherdatefrom, voucherdateto, reporttype);
    }

    public Map getLedgerDetailsByRegionMap(String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype,String selectedRegion, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************************AccountsVoucherAction***getLedgerDetailsMap*************");
        return accountVoucherService.getLedgerDetailsByRegion(null, request, response, null, null, period, ledger, voucherdatefrom, voucherdateto, reporttype,selectedRegion);
    }
    
    public Map getConsolidatedLedgerDetailsMap(String period, String ledger, String voucherdatefrom, String voucherdateto, String reporttype, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************************AccountsVoucherAction***getLedgerDetailsMap*************");
        return accountVoucherService.getConsolidatedLedgerDetails(null, request, response, null, null, period, ledger, voucherdatefrom, voucherdateto, reporttype);
    }    
}
