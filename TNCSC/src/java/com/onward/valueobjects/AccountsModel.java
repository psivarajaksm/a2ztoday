/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.valueobjects;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Prince vijayakumar M
 */
public class AccountsModel {

    private String region;
    private String cashbook;
    private String fromdate;
    private String todate;
    private String pageno;
    private String slipno;
    private String accdate;
    private String sanctioneddate;
    private String voucherno;
    private String compno;
    private String details;
    private String payment;
    private String adjustment;
    private String sanctionedby;
    private String fileno;
    private String printed;
    private String netpayment;
    private List<AccountsSubModel> paymentlist;
    private List<AccountsSubModel> adjustmentlist;
    private List<String> narrationlist;
    private String[] infavourof;
    private String adjustmenttotal;
    private String accountingperiod;
    private String accountingmonthandyear;
    private String debittotal;
    private String credittotal;
    private List<AccountsSubModel> accountdetailslist;
    private List<AccountsSubModel> chequedetailslist;
    private String receivername;
    private String amount;
    private String cashbookname;
    private List<AccountsSubModel> creditlist;
    private Map<String, List> paymentmap;
    private Map<String, List> adjustmentmap;
    private Map<String, List> creditmap;
    private String companyname;
    private String tinno;
    private String billno;
    private String commoditycode;
    private String commodityname;
    private String quantity;
    private String rate;
    private String vatamount;
    private String taxpercentage;
    private String taxamount;
    private String totalamount;
    private String value;
    private String breakuptype;
    private String accountbook;
    private String challanno;
    private String bankname;
    private String accountcode;
    private String accountname;
    private String groupcode;
    private String groupname;
    private String voucheroption;
    private Map<String, String> commoditymap;
    private Map<String, List> AllregionCommodityMap;
    private String branchname;
    private String debitamount;
    private String creditamount;
    private String chequeno;
    private String chequedate;
    private String casherno;
    private String voucherapproveddate;
    private String realizationdate;
    private String partyname;
    private List banklist;
    private String paymentmode;
    private String remittancedate;
    private String bankaccountno;

    public String getBankaccountno() {
        return bankaccountno;
    }

    public void setBankaccountno(String bankaccountno) {
        this.bankaccountno = bankaccountno;
    }


    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the cashbook
     */
    public String getCashbook() {
        return cashbook;
    }

    /**
     * @param cashbook the cashbook to set
     */
    public void setCashbook(String cashbook) {
        this.cashbook = cashbook;
    }

    /**
     * @return the fromdate
     */
    public String getFromdate() {
        return fromdate;
    }

    /**
     * @param fromdate the fromdate to set
     */
    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    /**
     * @return the todate
     */
    public String getTodate() {
        return todate;
    }

    /**
     * @param todate the todate to set
     */
    public void setTodate(String todate) {
        this.todate = todate;
    }

    /**
     * @return the pageno
     */
    public String getPageno() {
        return pageno;
    }

    /**
     * @param pageno the pageno to set
     */
    public void setPageno(String pageno) {
        this.pageno = pageno;
    }

    /**
     * @return the slipno
     */
    public String getSlipno() {
        return slipno;
    }

    /**
     * @param slipno the slipno to set
     */
    public void setSlipno(String slipno) {
        this.slipno = slipno;
    }

    /**
     * @return the accdate
     */
    public String getAccdate() {
        return accdate;
    }

    /**
     * @param accdate the accdate to set
     */
    public void setAccdate(String accdate) {
        this.accdate = accdate;
    }

    /**
     * @return the sanctioneddate
     */
    public String getSanctioneddate() {
        return sanctioneddate;
    }

    /**
     * @param sanctioneddate the sanctioneddate to set
     */
    public void setSanctioneddate(String sanctioneddate) {
        this.sanctioneddate = sanctioneddate;
    }

    /**
     * @return the voucherno
     */
    public String getVoucherno() {
        return voucherno;
    }

    /**
     * @param voucherno the voucherno to set
     */
    public void setVoucherno(String voucherno) {
        this.voucherno = voucherno;
    }

    /**
     * @return the compno
     */
    public String getCompno() {
        return compno;
    }

    /**
     * @param compno the compno to set
     */
    public void setCompno(String compno) {
        this.compno = compno;
    }

    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return the payment
     */
    public String getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(String payment) {
        this.payment = payment;
    }

    /**
     * @return the adjustment
     */
    public String getAdjustment() {
        return adjustment;
    }

    /**
     * @param adjustment the adjustment to set
     */
    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

    /**
     * @return the sanctionedby
     */
    public String getSanctionedby() {
        return sanctionedby;
    }

    /**
     * @param sanctionedby the sanctionedby to set
     */
    public void setSanctionedby(String sanctionedby) {
        this.sanctionedby = sanctionedby;
    }

    /**
     * @return the fileno
     */
    public String getFileno() {
        return fileno;
    }

    /**
     * @param fileno the fileno to set
     */
    public void setFileno(String fileno) {
        this.fileno = fileno;
    }

    /**
     * @return the printed
     */
    public String getPrinted() {
        return printed;
    }

    /**
     * @param printed the printed to set
     */
    public void setPrinted(String printed) {
        this.printed = printed;
    }

    /**
     * @return the netpayment
     */
    public String getNetpayment() {
        return netpayment;
    }

    /**
     * @param netpayment the netpayment to set
     */
    public void setNetpayment(String netpayment) {
        this.netpayment = netpayment;
    }

    /**
     * @return the paymentlist
     */
    public List<AccountsSubModel> getPaymentlist() {
        return paymentlist;
    }

    /**
     * @param paymentlist the paymentlist to set
     */
    public void setPaymentlist(List<AccountsSubModel> paymentlist) {
        this.paymentlist = paymentlist;
    }

    /**
     * @return the adjustmentlist
     */
    public List<AccountsSubModel> getAdjustmentlist() {
        return adjustmentlist;
    }

    /**
     * @param adjustmentlist the adjustmentlist to set
     */
    public void setAdjustmentlist(List<AccountsSubModel> adjustmentlist) {
        this.adjustmentlist = adjustmentlist;
    }

    /**
     * @return the narrationlist
     */
    public List<String> getNarrationlist() {
        return narrationlist;
    }

    /**
     * @param narrationlist the narrationlist to set
     */
    public void setNarrationlist(List<String> narrationlist) {
        this.narrationlist = narrationlist;
    }

    /**
     * @return the infavourof
     */
    public String[] getInfavourof() {
        return infavourof;
    }

    /**
     * @param infavourof the infavourof to set
     */
    public void setInfavourof(String[] infavourof) {
        this.infavourof = infavourof;
    }

    /**
     * @return the adjustmenttotal
     */
    public String getAdjustmenttotal() {
        return adjustmenttotal;
    }

    /**
     * @param adjustmenttotal the adjustmenttotal to set
     */
    public void setAdjustmenttotal(String adjustmenttotal) {
        this.adjustmenttotal = adjustmenttotal;
    }

    /**
     * @return the accountingperiod
     */
    public String getAccountingperiod() {
        return accountingperiod;
    }

    /**
     * @param accountingperiod the accountingperiod to set
     */
    public void setAccountingperiod(String accountingperiod) {
        this.accountingperiod = accountingperiod;
    }

    /**
     * @return the accountingmonthandyear
     */
    public String getAccountingmonthandyear() {
        return accountingmonthandyear;
    }

    /**
     * @param accountingmonthandyear the accountingmonthandyear to set
     */
    public void setAccountingmonthandyear(String accountingmonthandyear) {
        this.accountingmonthandyear = accountingmonthandyear;
    }

    /**
     * @return the debittotal
     */
    public String getDebittotal() {
        return debittotal;
    }

    /**
     * @param debittotal the debittotal to set
     */
    public void setDebittotal(String debittotal) {
        this.debittotal = debittotal;
    }

    /**
     * @return the credittotal
     */
    public String getCredittotal() {
        return credittotal;
    }

    /**
     * @param credittotal the credittotal to set
     */
    public void setCredittotal(String credittotal) {
        this.credittotal = credittotal;
    }

    /**
     * @return the accountdetailslist
     */
    public List<AccountsSubModel> getAccountdetailslist() {
        return accountdetailslist;
    }

    /**
     * @param accountdetailslist the accountdetailslist to set
     */
    public void setAccountdetailslist(List<AccountsSubModel> accountdetailslist) {
        this.accountdetailslist = accountdetailslist;
    }

    /**
     * @return the chequedetailslist
     */
    public List<AccountsSubModel> getChequedetailslist() {
        return chequedetailslist;
    }

    /**
     * @param chequedetailslist the chequedetailslist to set
     */
    public void setChequedetailslist(List<AccountsSubModel> chequedetailslist) {
        this.chequedetailslist = chequedetailslist;
    }

    /**
     * @return the receivername
     */
    public String getReceivername() {
        return receivername;
    }

    /**
     * @param receivername the receivername to set
     */
    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the cashbookname
     */
    public String getCashbookname() {
        return cashbookname;
    }

    /**
     * @param cashbookname the cashbookname to set
     */
    public void setCashbookname(String cashbookname) {
        this.cashbookname = cashbookname;
    }

    /**
     * @return the creditlist
     */
    public List<AccountsSubModel> getCreditlist() {
        return creditlist;
    }

    /**
     * @param creditlist the creditlist to set
     */
    public void setCreditlist(List<AccountsSubModel> creditlist) {
        this.creditlist = creditlist;
    }

    /**
     * @return the paymentmap
     */
    public Map<String, List> getPaymentmap() {
        return paymentmap;
    }

    /**
     * @param paymentmap the paymentmap to set
     */
    public void setPaymentmap(Map<String, List> paymentmap) {
        this.paymentmap = paymentmap;
    }

    /**
     * @return the adjustmentmap
     */
    public Map<String, List> getAdjustmentmap() {
        return adjustmentmap;
    }

    /**
     * @param adjustmentmap the adjustmentmap to set
     */
    public void setAdjustmentmap(Map<String, List> adjustmentmap) {
        this.adjustmentmap = adjustmentmap;
    }

    /**
     * @return the creditmap
     */
    public Map<String, List> getCreditmap() {
        return creditmap;
    }

    /**
     * @param creditmap the creditmap to set
     */
    public void setCreditmap(Map<String, List> creditmap) {
        this.creditmap = creditmap;
    }

    /**
     * @return the companyname
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * @param companyname the companyname to set
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    /**
     * @return the tinno
     */
    public String getTinno() {
        return tinno;
    }

    /**
     * @param tinno the tinno to set
     */
    public void setTinno(String tinno) {
        this.tinno = tinno;
    }

    /**
     * @return the billno
     */
    public String getBillno() {
        return billno;
    }

    /**
     * @param billno the billno to set
     */
    public void setBillno(String billno) {
        this.billno = billno;
    }

    /**
     * @return the commoditycode
     */
    public String getCommoditycode() {
        return commoditycode;
    }

    /**
     * @param commoditycode the commoditycode to set
     */
    public void setCommoditycode(String commoditycode) {
        this.commoditycode = commoditycode;
    }

    /**
     * @return the commodityname
     */
    public String getCommodityname() {
        return commodityname;
    }

    /**
     * @param commodityname the commodityname to set
     */
    public void setCommodityname(String commodityname) {
        this.commodityname = commodityname;
    }

    /**
     * @return the quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the rate
     */
    public String getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(String rate) {
        this.rate = rate;
    }

    /**
     * @return the vatamount
     */
    public String getVatamount() {
        return vatamount;
    }

    /**
     * @param vatamount the vatamount to set
     */
    public void setVatamount(String vatamount) {
        this.vatamount = vatamount;
    }

    /**
     * @return the taxpercentage
     */
    public String getTaxpercentage() {
        return taxpercentage;
    }

    /**
     * @param taxpercentage the taxpercentage to set
     */
    public void setTaxpercentage(String taxpercentage) {
        this.taxpercentage = taxpercentage;
    }

    /**
     * @return the taxamount
     */
    public String getTaxamount() {
        return taxamount;
    }

    /**
     * @param taxamount the taxamount to set
     */
    public void setTaxamount(String taxamount) {
        this.taxamount = taxamount;
    }

    /**
     * @return the totalamount
     */
    public String getTotalamount() {
        return totalamount;
    }

    /**
     * @param totalamount the totalamount to set
     */
    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the breakuptype
     */
    public String getBreakuptype() {
        return breakuptype;
    }

    /**
     * @param breakuptype the breakuptype to set
     */
    public void setBreakuptype(String breakuptype) {
        this.breakuptype = breakuptype;
    }

    /**
     * @return the accountbook
     */
    public String getAccountbook() {
        return accountbook;
    }

    /**
     * @param accountbook the accountbook to set
     */
    public void setAccountbook(String accountbook) {
        this.accountbook = accountbook;
    }

    /**
     * @return the challanno
     */
    public String getChallanno() {
        return challanno;
    }

    /**
     * @param challanno the challanno to set
     */
    public void setChallanno(String challanno) {
        this.challanno = challanno;
    }

    /**
     * @return the bankname
     */
    public String getBankname() {
        return bankname;
    }

    /**
     * @param bankname the bankname to set
     */
    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    /**
     * @return the accountcode
     */
    public String getAccountcode() {
        return accountcode;
    }

    /**
     * @param accountcode the accountcode to set
     */
    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }

    /**
     * @return the accountname
     */
    public String getAccountname() {
        return accountname;
    }

    /**
     * @param accountname the accountname to set
     */
    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    /**
     * @return the groupcode
     */
    public String getGroupcode() {
        return groupcode;
    }

    /**
     * @param groupcode the groupcode to set
     */
    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    /**
     * @return the groupname
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * @param groupname the groupname to set
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    /**
     * @return the voucheroption
     */
    public String getVoucheroption() {
        return voucheroption;
    }

    /**
     * @param voucheroption the voucheroption to set
     */
    public void setVoucheroption(String voucheroption) {
        this.voucheroption = voucheroption;
    }

    /**
     * @return the commoditymap
     */
    public Map<String, String> getCommoditymap() {
        return commoditymap;
    }

    /**
     * @param commoditymap the commoditymap to set
     */
    public void setCommoditymap(Map<String, String> commoditymap) {
        this.commoditymap = commoditymap;
    }

    /**
     * @return the AllregionCommodityMap
     */
    public Map<String, List> getAllregionCommodityMap() {
        return AllregionCommodityMap;
    }

    /**
     * @param AllregionCommodityMap the AllregionCommodityMap to set
     */
    public void setAllregionCommodityMap(Map<String, List> AllregionCommodityMap) {
        this.AllregionCommodityMap = AllregionCommodityMap;
    }

    /**
     * @return the branchname
     */
    public String getBranchname() {
        return branchname;
    }

    /**
     * @param branchname the branchname to set
     */
    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    /**
     * @return the debitamount
     */
    public String getDebitamount() {
        return debitamount;
    }

    /**
     * @param debitamount the debitamount to set
     */
    public void setDebitamount(String debitamount) {
        this.debitamount = debitamount;
    }

    /**
     * @return the creditamount
     */
    public String getCreditamount() {
        return creditamount;
    }

    /**
     * @param creditamount the creditamount to set
     */
    public void setCreditamount(String creditamount) {
        this.creditamount = creditamount;
    }

    /**
     * @return the chequeno
     */
    public String getChequeno() {
        return chequeno;
    }

    /**
     * @param chequeno the chequeno to set
     */
    public void setChequeno(String chequeno) {
        this.chequeno = chequeno;
    }

    /**
     * @return the chequedate
     */
    public String getChequedate() {
        return chequedate;
    }

    /**
     * @param chequedate the chequedate to set
     */
    public void setChequedate(String chequedate) {
        this.chequedate = chequedate;
    }

    /**
     * @return the casherno
     */
    public String getCasherno() {
        return casherno;
    }

    /**
     * @param casherno the casherno to set
     */
    public void setCasherno(String casherno) {
        this.casherno = casherno;
    }

    /**
     * @return the voucherapproveddate
     */
    public String getVoucherapproveddate() {
        return voucherapproveddate;
    }

    /**
     * @param voucherapproveddate the voucherapproveddate to set
     */
    public void setVoucherapproveddate(String voucherapproveddate) {
        this.voucherapproveddate = voucherapproveddate;
    }

    /**
     * @return the realizationdate
     */
    public String getRealizationdate() {
        return realizationdate;
    }

    /**
     * @param realizationdate the realizationdate to set
     */
    public void setRealizationdate(String realizationdate) {
        this.realizationdate = realizationdate;
    }

    /**
     * @return the partyname
     */
    public String getPartyname() {
        return partyname;
    }

    /**
     * @param partyname the partyname to set
     */
    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    /**
     * @return the banklist
     */
    public List getBanklist() {
        return banklist;
    }

    /**
     * @param banklist the banklist to set
     */
    public void setBanklist(List banklist) {
        this.banklist = banklist;
    }

    /**
     * @return the paymentmode
     */
    public String getPaymentmode() {
        return paymentmode;
    }

    /**
     * @param paymentmode the paymentmode to set
     */
    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    /**
     * @return the remittancedate
     */
    public String getRemittancedate() {
        return remittancedate;
    }

    /**
     * @param remittancedate the remittancedate to set
     */
    public void setRemittancedate(String remittancedate) {
        this.remittancedate = remittancedate;
    }
    
}
