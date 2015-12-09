package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * Supplementaryemployeeearningsdetails generated by hbm2java
 */
public class Supplementaryemployeeearningsdetails  implements java.io.Serializable {


     private String id;
     private Supplementarysalarystructure supplementarysalarystructure;
     private String earningmasterid;
     private BigDecimal amount;
     private Boolean cancelled;
     private String accregion;

    public Supplementaryemployeeearningsdetails() {
    }

	
    public Supplementaryemployeeearningsdetails(String id) {
        this.id = id;
    }
    public Supplementaryemployeeearningsdetails(String id, Supplementarysalarystructure supplementarysalarystructure, String earningmasterid, BigDecimal amount, Boolean cancelled, String accregion) {
       this.id = id;
       this.supplementarysalarystructure = supplementarysalarystructure;
       this.earningmasterid = earningmasterid;
       this.amount = amount;
       this.cancelled = cancelled;
       this.accregion = accregion;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Supplementarysalarystructure getSupplementarysalarystructure() {
        return this.supplementarysalarystructure;
    }
    
    public void setSupplementarysalarystructure(Supplementarysalarystructure supplementarysalarystructure) {
        this.supplementarysalarystructure = supplementarysalarystructure;
    }
    public String getEarningmasterid() {
        return this.earningmasterid;
    }
    
    public void setEarningmasterid(String earningmasterid) {
        this.earningmasterid = earningmasterid;
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
    public String getAccregion() {
        return this.accregion;
    }
    
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }




}

