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
@XmlRootElement(namespace = "employeeearningstransactionspost")
public class EmployeeearningstransactionsPost {

    private String id;
    private String earningmasterid;
    private String amount;
    private String cancelled;
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

    public String getEarningmasterid() {
        return earningmasterid;
    }

    public void setEarningmasterid(String earningmasterid) {
        this.earningmasterid = earningmasterid;
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

    
}
