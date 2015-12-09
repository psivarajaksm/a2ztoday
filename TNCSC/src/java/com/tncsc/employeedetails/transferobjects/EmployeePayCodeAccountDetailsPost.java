/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncsc.employeedetails.transferobjects;

import com.onward.persistence.payroll.Employeemaster;
import com.onward.persistence.payroll.Paycodemaster;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "employeepaycodeaccountdetailspost")
public class EmployeePayCodeAccountDetailsPost {
     private String id;
     private String paycodemaster;    
     private String deductionaccountcode;

    public String getDeductionaccountcode() {
        return deductionaccountcode;
    }

    public void setDeductionaccountcode(String deductionaccountcode) {
        this.deductionaccountcode = deductionaccountcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaycodemaster() {
        return paycodemaster;
    }

    public void setPaycodemaster(String paycodemaster) {
        this.paycodemaster = paycodemaster;
    }
}
