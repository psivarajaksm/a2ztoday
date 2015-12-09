/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.action.OnwardAction;
import com.onward.dao.AccountsMasterService;
import com.onward.dao.OeslModule;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author root
 */
public class AccountsMastersAction extends OnwardAction {

    OeslModule notOnWeekendsModule = new OeslModule();
    Injector injector = Guice.createInjector(notOnWeekendsModule);
    AccountsMasterService MasterServiceObj = (AccountsMasterService) injector.getInstance(AccountsMasterService.class);

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward accountHeadMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("accountHeadMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward bankledgerMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("bankledgerMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward bankChallanPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("bankChallanPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward cashChallanPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("cashChallanPage");
    }   
    
    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward partyledgerMasterPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("partyledgerMasterPage");
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward accountsBookPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("accountsBookPage");
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public Map loadAccountGroupHeadDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.loadAccountGroupHeadDetails(null, request, response, null, null);

    }

    /**
     *
     * @param accountcode
     * @param ledgercode
     * @param accountheadname
     * @param request
     * @param response
     * @return
     */
    public Map saveAccountHeadMaster(String accountcode, String ledgercode, String accountheadname, String serialno, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveAccountHeadMaster(null, request, response, null, null, accountcode, ledgercode, accountheadname, serialno);
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Details.
     */
    public Map loadRegionDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.loadRegionDetails(null, request, response, null, null);
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Bank Details.
     */
    public Map getRegionBankDetails(String regionid, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getRegionBankDetails(null, request, response, null, null, regionid);
    }

    /**
     *
     * @param regioncode
     * @param bankname
     * @param bankcode
     * @param request
     * @param response
     * @return
     */
    public Map saveBankLedgerMaster(String regioncode, String bankname, String bankcode, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.saveBankLedgerMaster(null, request, response, null, null, regioncode, bankname, bankcode);
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Details.
     */
    public Map getRegionPartiesDetails(String regionid, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getRegionPartiesDetails(null, request, response, null, null, regionid);
    }

    /**
     *
     * @param request
     * @param response
     * @return It Returns map.Map Contains List of existing Region Details.
     */
    public Map getAccountBooksDetails(HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.getAccountBooksDetails(null, request, response, null, null);
    }

    /**
     *
     * @param regioncode
     * @param bankname
     * @param bankcode
     * @param request
     * @param response
     * @return
     */
    public Map savePartyLedgerMaster(String regioncode, String bankname, String bankcode, String tinno, HttpServletRequest request, HttpServletResponse response) {
        return MasterServiceObj.savePartyLedgerMaster(null, request, response, null, null, regioncode, bankname, bankcode, tinno);
    }
}
