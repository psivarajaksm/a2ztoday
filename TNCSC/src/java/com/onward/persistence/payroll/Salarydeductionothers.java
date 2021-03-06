package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * Salarydeductionothers generated by hbm2java
 */
public class Salarydeductionothers  implements java.io.Serializable {


     private String id;
     private Paycodemaster paycodemaster;
     private Employeemaster employeemaster;
     private Integer deductionmode;
     private Integer deductionmonth;
     private Integer deductionyear;
     private BigDecimal amountornoofdays;
     private Boolean synchronized_;
     private Integer intallmenttype;
     private String accregion;
     private Boolean cancelled;
     private Date createddate;
     private String createdby;

    public Salarydeductionothers() {
    }

	
    public Salarydeductionothers(String id, Paycodemaster paycodemaster) {
        this.id = id;
        this.paycodemaster = paycodemaster;
    }
    public Salarydeductionothers(String id, Paycodemaster paycodemaster, Employeemaster employeemaster, Integer deductionmode, Integer deductionmonth, Integer deductionyear, BigDecimal amountornoofdays, Boolean synchronized_, Integer intallmenttype, String accregion, Boolean cancelled, Date createddate, String createdby) {
       this.id = id;
       this.paycodemaster = paycodemaster;
       this.employeemaster = employeemaster;
       this.deductionmode = deductionmode;
       this.deductionmonth = deductionmonth;
       this.deductionyear = deductionyear;
       this.amountornoofdays = amountornoofdays;
       this.synchronized_ = synchronized_;
       this.intallmenttype = intallmenttype;
       this.accregion = accregion;
       this.cancelled = cancelled;
       this.createddate = createddate;
       this.createdby = createdby;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Paycodemaster getPaycodemaster() {
        return this.paycodemaster;
    }
    
    public void setPaycodemaster(Paycodemaster paycodemaster) {
        this.paycodemaster = paycodemaster;
    }
    public Employeemaster getEmployeemaster() {
        return this.employeemaster;
    }
    
    public void setEmployeemaster(Employeemaster employeemaster) {
        this.employeemaster = employeemaster;
    }
    public Integer getDeductionmode() {
        return this.deductionmode;
    }
    
    public void setDeductionmode(Integer deductionmode) {
        this.deductionmode = deductionmode;
    }
    public Integer getDeductionmonth() {
        return this.deductionmonth;
    }
    
    public void setDeductionmonth(Integer deductionmonth) {
        this.deductionmonth = deductionmonth;
    }
    public Integer getDeductionyear() {
        return this.deductionyear;
    }
    
    public void setDeductionyear(Integer deductionyear) {
        this.deductionyear = deductionyear;
    }
    public BigDecimal getAmountornoofdays() {
        return this.amountornoofdays;
    }
    
    public void setAmountornoofdays(BigDecimal amountornoofdays) {
        this.amountornoofdays = amountornoofdays;
    }
    public Boolean getSynchronized_() {
        return this.synchronized_;
    }
    
    public void setSynchronized_(Boolean synchronized_) {
        this.synchronized_ = synchronized_;
    }
    public Integer getIntallmenttype() {
        return this.intallmenttype;
    }
    
    public void setIntallmenttype(Integer intallmenttype) {
        this.intallmenttype = intallmenttype;
    }
    public String getAccregion() {
        return this.accregion;
    }
    
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
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


