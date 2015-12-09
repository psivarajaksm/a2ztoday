package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * Ancillaruadjdetails generated by hbm2java
 */
public class Ancillaruadjdetails  implements java.io.Serializable {


     private String id;
     private Regionmaster regionmaster;
     private Ancillaryaccountadjcode ancillaryaccountadjcode;
     private Voucherdetails voucherdetails;
     private BigDecimal tax;
     private BigDecimal amount;
     private String acgroup;
     private Boolean cancelled;
     private Integer serialno;
     private BigDecimal paymentamount;
     private Date createddate;
     private String createdby;

    public Ancillaruadjdetails() {
    }

	
    public Ancillaruadjdetails(String id) {
        this.id = id;
    }
    public Ancillaruadjdetails(String id, Regionmaster regionmaster, Ancillaryaccountadjcode ancillaryaccountadjcode, Voucherdetails voucherdetails, BigDecimal tax, BigDecimal amount, String acgroup, Boolean cancelled, Integer serialno, BigDecimal paymentamount, Date createddate, String createdby) {
       this.id = id;
       this.regionmaster = regionmaster;
       this.ancillaryaccountadjcode = ancillaryaccountadjcode;
       this.voucherdetails = voucherdetails;
       this.tax = tax;
       this.amount = amount;
       this.acgroup = acgroup;
       this.cancelled = cancelled;
       this.serialno = serialno;
       this.paymentamount = paymentamount;
       this.createddate = createddate;
       this.createdby = createdby;
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
    public Ancillaryaccountadjcode getAncillaryaccountadjcode() {
        return this.ancillaryaccountadjcode;
    }
    
    public void setAncillaryaccountadjcode(Ancillaryaccountadjcode ancillaryaccountadjcode) {
        this.ancillaryaccountadjcode = ancillaryaccountadjcode;
    }
    public Voucherdetails getVoucherdetails() {
        return this.voucherdetails;
    }
    
    public void setVoucherdetails(Voucherdetails voucherdetails) {
        this.voucherdetails = voucherdetails;
    }
    public BigDecimal getTax() {
        return this.tax;
    }
    
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    public BigDecimal getAmount() {
        return this.amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getAcgroup() {
        return this.acgroup;
    }
    
    public void setAcgroup(String acgroup) {
        this.acgroup = acgroup;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Integer getSerialno() {
        return this.serialno;
    }
    
    public void setSerialno(Integer serialno) {
        this.serialno = serialno;
    }
    public BigDecimal getPaymentamount() {
        return this.paymentamount;
    }
    
    public void setPaymentamount(BigDecimal paymentamount) {
        this.paymentamount = paymentamount;
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


