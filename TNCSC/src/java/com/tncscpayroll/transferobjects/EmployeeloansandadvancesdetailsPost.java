/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Sivaraja.P
 */
@XmlRootElement(namespace = "employeeloansandadvancesdetailspost")
public class EmployeeloansandadvancesdetailsPost {

    private String id;
    private String nthinstallment;    
    private String installmentamount;
    private String loanbalance;
    private String cancelled;
    private String payrollprocessingdetailsId;
    private String loansandadvancesid;

    public String getLoansandadvancesid() {
        return loansandadvancesid;
    }

    public void setLoansandadvancesid(String loansandadvancesid) {
        this.loansandadvancesid = loansandadvancesid;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstallmentamount() {
        return installmentamount;
    }

    public void setInstallmentamount(String installmentamount) {
        this.installmentamount = installmentamount;
    }

    public String getLoanbalance() {
        return loanbalance;
    }

    public void setLoanbalance(String loanbalance) {
        this.loanbalance = loanbalance;
    }

    public String getNthinstallment() {
        return nthinstallment;
    }

    public void setNthinstallment(String nthinstallment) {
        this.nthinstallment = nthinstallment;
    }

    public String getPayrollprocessingdetailsId() {
        return payrollprocessingdetailsId;
    }

    public void setPayrollprocessingdetailsId(String payrollprocessingdetailsId) {
        this.payrollprocessingdetailsId = payrollprocessingdetailsId;
    }

    
}
