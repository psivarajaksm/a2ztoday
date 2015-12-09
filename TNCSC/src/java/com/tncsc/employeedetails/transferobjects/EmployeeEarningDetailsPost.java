/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncsc.employeedetails.transferobjects;

import com.onward.persistence.payroll.Salarystructure;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "employeeearningdetailspost")
public class EmployeeEarningDetailsPost {

    private String id;
    private String salarystructure;
    private String earningmasterid;
    private String amount;
    private String percentage;
    private String ispercentage;
    private String cancelled;
    private String accregion;

    public String getAccregion() {
        return accregion;
    }

    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }

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

    public String getIspercentage() {
        return ispercentage;
    }

    public void setIspercentage(String ispercentage) {
        this.ispercentage = ispercentage;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getSalarystructure() {
        return salarystructure;
    }

    public void setSalarystructure(String salarystructure) {
        this.salarystructure = salarystructure;
    }
}
