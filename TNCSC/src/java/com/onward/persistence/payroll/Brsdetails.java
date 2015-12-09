package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * Brsdetails generated by hbm2java
 */
public class Brsdetails  implements java.io.Serializable {


     private String id;
     private Regionmaster regionmaster;
     private Accountsbooks accountsbooks;
     private Accountingyear accountingyear;
     private Date transactiondate;
     private Date valuedate;
     private String description;
     private String referenceno;
     private String branchcode;
     private Integer month;
     private Integer year;
     private BigDecimal debit;
     private BigDecimal credit;
     private BigDecimal balance;
     private Boolean isapproved;
     private Boolean cancelled;
     private String createdby;
     private Date createddate;

    public Brsdetails() {
    }

	
    public Brsdetails(String id, Accountsbooks accountsbooks) {
        this.id = id;
        this.accountsbooks = accountsbooks;
    }
    public Brsdetails(String id, Regionmaster regionmaster, Accountsbooks accountsbooks, Accountingyear accountingyear, Date transactiondate, Date valuedate, String description, String referenceno, String branchcode, Integer month, Integer year, BigDecimal debit, BigDecimal credit, BigDecimal balance, Boolean isapproved, Boolean cancelled, String createdby, Date createddate) {
       this.id = id;
       this.regionmaster = regionmaster;
       this.accountsbooks = accountsbooks;
       this.accountingyear = accountingyear;
       this.transactiondate = transactiondate;
       this.valuedate = valuedate;
       this.description = description;
       this.referenceno = referenceno;
       this.branchcode = branchcode;
       this.month = month;
       this.year = year;
       this.debit = debit;
       this.credit = credit;
       this.balance = balance;
       this.isapproved = isapproved;
       this.cancelled = cancelled;
       this.createdby = createdby;
       this.createddate = createddate;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Regionmaster getRegionmaster() {
        return this.regionmaster;
    }
    
    public void setRegionmaster(Regionmaster regionmaster) {
        this.regionmaster = regionmaster;
    }
    public Accountsbooks getAccountsbooks() {
        return this.accountsbooks;
    }
    
    public void setAccountsbooks(Accountsbooks accountsbooks) {
        this.accountsbooks = accountsbooks;
    }
    public Accountingyear getAccountingyear() {
        return this.accountingyear;
    }
    
    public void setAccountingyear(Accountingyear accountingyear) {
        this.accountingyear = accountingyear;
    }
    public Date getTransactiondate() {
        return this.transactiondate;
    }
    
    public void setTransactiondate(Date transactiondate) {
        this.transactiondate = transactiondate;
    }
    public Date getValuedate() {
        return this.valuedate;
    }
    
    public void setValuedate(Date valuedate) {
        this.valuedate = valuedate;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getReferenceno() {
        return this.referenceno;
    }
    
    public void setReferenceno(String referenceno) {
        this.referenceno = referenceno;
    }
    public String getBranchcode() {
        return this.branchcode;
    }
    
    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }
    public Integer getMonth() {
        return this.month;
    }
    
    public void setMonth(Integer month) {
        this.month = month;
    }
    public Integer getYear() {
        return this.year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    public BigDecimal getDebit() {
        return this.debit;
    }
    
    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }
    public BigDecimal getCredit() {
        return this.credit;
    }
    
    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
    public BigDecimal getBalance() {
        return this.balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public Boolean getIsapproved() {
        return this.isapproved;
    }
    
    public void setIsapproved(Boolean isapproved) {
        this.isapproved = isapproved;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public String getCreatedby() {
        return this.createdby;
    }
    
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
    public Date getCreateddate() {
        return this.createddate;
    }
    
    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }




}

