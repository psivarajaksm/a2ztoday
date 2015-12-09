package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * Employeedeductionstransactions generated by hbm2java
 */
public class Employeedeductionstransactions  implements java.io.Serializable {


     private String id;
     private Payrollprocessingdetails payrollprocessingdetails;
     private String deductionmasterid;
     private BigDecimal amount;
     private Boolean cancelled;
     private String type;
     private String accregion;
     private Date createddate;
     private String createdby;

    public Employeedeductionstransactions() {
    }

	
    public Employeedeductionstransactions(String id) {
        this.id = id;
    }
    public Employeedeductionstransactions(String id, Payrollprocessingdetails payrollprocessingdetails, String deductionmasterid, BigDecimal amount, Boolean cancelled, String type, String accregion, Date createddate, String createdby) {
       this.id = id;
       this.payrollprocessingdetails = payrollprocessingdetails;
       this.deductionmasterid = deductionmasterid;
       this.amount = amount;
       this.cancelled = cancelled;
       this.type = type;
       this.accregion = accregion;
       this.createddate = createddate;
       this.createdby = createdby;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Payrollprocessingdetails getPayrollprocessingdetails() {
        return this.payrollprocessingdetails;
    }
    
    public void setPayrollprocessingdetails(Payrollprocessingdetails payrollprocessingdetails) {
        this.payrollprocessingdetails = payrollprocessingdetails;
    }
    public String getDeductionmasterid() {
        return this.deductionmasterid;
    }
    
    public void setDeductionmasterid(String deductionmasterid) {
        this.deductionmasterid = deductionmasterid;
    }
    public BigDecimal getAmount() {
        return this.amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getAccregion() {
        return this.accregion;
    }
    
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }
    public Date getCreateddate() {
        return this.createddate;
    }
    
    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }
    public String getCreatedby() {
        return this.createdby;
    }
    
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }




}

