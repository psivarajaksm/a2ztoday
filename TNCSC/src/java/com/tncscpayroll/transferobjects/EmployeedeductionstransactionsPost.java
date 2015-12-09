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
@XmlRootElement(namespace = "employeedeductionstransactionspost")
public class EmployeedeductionstransactionsPost {

    private String id;
    private String deductionmasterid;
    private String amount;
    private String cancelled;
    private String type;
    private String payrollprocessingdetailsId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getDeductionmasterid() {
        return deductionmasterid;
    }

    public void setDeductionmasterid(String deductionmasterid) {
        this.deductionmasterid = deductionmasterid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayrollprocessingdetailsId() {
        return payrollprocessingdetailsId;
    }

    public void setPayrollprocessingdetailsId(String payrollprocessingdetailsId) {
        this.payrollprocessingdetailsId = payrollprocessingdetailsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
}
