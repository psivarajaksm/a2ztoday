/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sivaraja.P
 */
@XmlRootElement(namespace = "employeeloansandadvancespost")
public class EmployeeLoansandAdvancesPost {

    private String id;
    private String deductioncode;
    private String loandate;
    private String loanamount;
    private String totalinstallment;
    private String currentinstallment;
    private String installmentamount;
    private String firstinstallmentamount;
    private String loanbalance;
    private String loantype;
    private String status;
    private String fileno;
    private String regionno;
    private String synchronized1;
    private String epfno;
    private String accregion;

    public String getAccregion() {
        return accregion;
    }

    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }

    public String getEpfno() {
        return epfno;
    }

    public void setEpfno(String epfno) {
        this.epfno = epfno;
    }

    public String getCurrentinstallment() {
        return currentinstallment;
    }

    public void setCurrentinstallment(String currentinstallment) {
        this.currentinstallment = currentinstallment;
    }

    public String getDeductioncode() {
        return deductioncode;
    }

    public void setDeductioncode(String deductioncode) {
        this.deductioncode = deductioncode;
    }

    public String getFileno() {
        return fileno;
    }

    public void setFileno(String fileno) {
        this.fileno = fileno;
    }

    public String getFirstinstallmentamount() {
        return firstinstallmentamount;
    }

    public void setFirstinstallmentamount(String firstinstallmentamount) {
        this.firstinstallmentamount = firstinstallmentamount;
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

    public String getLoanamount() {
        return loanamount;
    }

    public void setLoanamount(String loanamount) {
        this.loanamount = loanamount;
    }

    public String getLoanbalance() {
        return loanbalance;
    }

    public void setLoanbalance(String loanbalance) {
        this.loanbalance = loanbalance;
    }

    public String getLoandate() {
        return loandate;
    }

    public void setLoandate(String loandate) {
        this.loandate = loandate;
    }

    public String getLoantype() {
        return loantype;
    }

    public void setLoantype(String loantype) {
        this.loantype = loantype;
    }

    public String getRegionno() {
        return regionno;
    }

    public void setRegionno(String regionno) {
        this.regionno = regionno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynchronized1() {
        return synchronized1;
    }

    public void setSynchronized1(String synchronized1) {
        this.synchronized1 = synchronized1;
    }

    public String getTotalinstallment() {
        return totalinstallment;
    }

    public void setTotalinstallment(String totalinstallment) {
        this.totalinstallment = totalinstallment;
    }

    
}
