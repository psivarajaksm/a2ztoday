/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import com.onward.persistence.payroll.Employeemaster;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "supplementatypaybillpost")
public class SupplementatypaybillPost {

    private String id;
    private String employeemasterId;
    private String accregion;
    private String noofdays;
    private String date;
    private String type;
    private String cancelled;
    private String section;
    private String subsection;
    private String paymentmode;
    private String sldate;
    private List<SupplementarypayrollprocessingdetailsPost> supplementarypayrollprocessingdetailsPostList;
    private String employeecategory;

    public String getEmployeecategory() {
        return employeecategory;
    }

    public void setEmployeecategory(String employeecategory) {
        this.employeecategory = employeecategory;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmployeemasterId() {
        return employeemasterId;
    }

    public void setEmployeemasterId(String employeemasterId) {
        this.employeemasterId = employeemasterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoofdays() {
        return noofdays;
    }

    public void setNoofdays(String noofdays) {
        this.noofdays = noofdays;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSldate() {
        return sldate;
    }

    public void setSldate(String sldate) {
        this.sldate = sldate;
    }

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public List<SupplementarypayrollprocessingdetailsPost> getSupplementarypayrollprocessingdetailsPostList() {
        return supplementarypayrollprocessingdetailsPostList;
    }

    public void setSupplementarypayrollprocessingdetailsPostList(List<SupplementarypayrollprocessingdetailsPost> supplementarypayrollprocessingdetailsPostList) {
        this.supplementarypayrollprocessingdetailsPostList = supplementarypayrollprocessingdetailsPostList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
