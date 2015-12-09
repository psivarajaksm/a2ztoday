/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import com.onward.persistence.payroll.Employeeloansandadvances;
import com.onward.persistence.payroll.Supplementarypayrollprocessingdetails;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "supplementaryemployeeloansandadvancesdetailspost")
public class SupplementaryemployeeloansandadvancesdetailsPost {

    private String id;
    private String supplementarypayrollprocessingdetailsId;
    private String employeeloansandadvancesId;
    private String nthinstallment;
    private String installmentamount;
    private String loanbalance;
    private String cancelled;
    private String accregion;

    public String getAccregion() {
        return accregion;
    }

    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getEmployeeloansandadvancesId() {
        return employeeloansandadvancesId;
    }

    public void setEmployeeloansandadvancesId(String employeeloansandadvancesId) {
        this.employeeloansandadvancesId = employeeloansandadvancesId;
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

    public String getSupplementarypayrollprocessingdetailsId() {
        return supplementarypayrollprocessingdetailsId;
    }

    public void setSupplementarypayrollprocessingdetailsId(String supplementarypayrollprocessingdetailsId) {
        this.supplementarypayrollprocessingdetailsId = supplementarypayrollprocessingdetailsId;
    }
}
