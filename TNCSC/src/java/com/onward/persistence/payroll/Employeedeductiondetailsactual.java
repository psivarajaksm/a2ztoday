package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * Employeedeductiondetailsactual generated by hbm2java
 */
public class Employeedeductiondetailsactual  implements java.io.Serializable {


     private String id;
     private Salarystructureactual salarystructureactual;
     private String deductionmasterid;
     private BigDecimal amount;
     private BigDecimal percentage;
     private Boolean ispercentage;
     private String dednNo;
     private Boolean cancelled;
     private String accregion;

    public Employeedeductiondetailsactual() {
    }

	
    public Employeedeductiondetailsactual(String id) {
        this.id = id;
    }
    public Employeedeductiondetailsactual(String id, Salarystructureactual salarystructureactual, String deductionmasterid, BigDecimal amount, BigDecimal percentage, Boolean ispercentage, String dednNo, Boolean cancelled, String accregion) {
       this.id = id;
       this.salarystructureactual = salarystructureactual;
       this.deductionmasterid = deductionmasterid;
       this.amount = amount;
       this.percentage = percentage;
       this.ispercentage = ispercentage;
       this.dednNo = dednNo;
       this.cancelled = cancelled;
       this.accregion = accregion;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Salarystructureactual getSalarystructureactual() {
        return this.salarystructureactual;
    }
    
    public void setSalarystructureactual(Salarystructureactual salarystructureactual) {
        this.salarystructureactual = salarystructureactual;
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
    public BigDecimal getPercentage() {
        return this.percentage;
    }
    
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
    public Boolean getIspercentage() {
        return this.ispercentage;
    }
    
    public void setIspercentage(Boolean ispercentage) {
        this.ispercentage = ispercentage;
    }
    public String getDednNo() {
        return this.dednNo;
    }
    
    public void setDednNo(String dednNo) {
        this.dednNo = dednNo;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public String getAccregion() {
        return this.accregion;
    }
    
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }




}

