package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Voucher generated by hbm2java
 */
public class Voucher  implements java.io.Serializable {


     private String id;
     private Regionmaster regionmaster;
     private Accountsbooks accountsbooks;
     private Accountingyear accountingyear;
     private String vouchertype;
     private Date voucherdate;
     private String sanctionedby;
     private String fileno;
     private String narration;
     private Boolean cancelled;
     private Date sanctiondate;
     private Boolean printed;
     private String voucherno;
     private Date voucherapproveddate;
     private Date createddate;
     private String createdby;
     private Boolean locked;
     private Set voucherhistories = new HashSet(0);
     private Set voucherdetailshistories = new HashSet(0);
     private Set voucherdetailses = new HashSet(0);
     private Set receiptpaymentdetailshistories = new HashSet(0);
     private Set receiptpaymentdetailses = new HashSet(0);

    public Voucher() {
    }

	
    public Voucher(String id, Regionmaster regionmaster, Accountsbooks accountsbooks, Accountingyear accountingyear, String vouchertype) {
        this.id = id;
        this.regionmaster = regionmaster;
        this.accountsbooks = accountsbooks;
        this.accountingyear = accountingyear;
        this.vouchertype = vouchertype;
    }
    public Voucher(String id, Regionmaster regionmaster, Accountsbooks accountsbooks, Accountingyear accountingyear, String vouchertype, Date voucherdate, String sanctionedby, String fileno, String narration, Boolean cancelled, Date sanctiondate, Boolean printed, String voucherno, Date voucherapproveddate, Date createddate, String createdby, Boolean locked, Set voucherhistories, Set voucherdetailshistories, Set voucherdetailses, Set receiptpaymentdetailshistories, Set receiptpaymentdetailses) {
       this.id = id;
       this.regionmaster = regionmaster;
       this.accountsbooks = accountsbooks;
       this.accountingyear = accountingyear;
       this.vouchertype = vouchertype;
       this.voucherdate = voucherdate;
       this.sanctionedby = sanctionedby;
       this.fileno = fileno;
       this.narration = narration;
       this.cancelled = cancelled;
       this.sanctiondate = sanctiondate;
       this.printed = printed;
       this.voucherno = voucherno;
       this.voucherapproveddate = voucherapproveddate;
       this.createddate = createddate;
       this.createdby = createdby;
       this.locked = locked;
       this.voucherhistories = voucherhistories;
       this.voucherdetailshistories = voucherdetailshistories;
       this.voucherdetailses = voucherdetailses;
       this.receiptpaymentdetailshistories = receiptpaymentdetailshistories;
       this.receiptpaymentdetailses = receiptpaymentdetailses;
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
    public String getVouchertype() {
        return this.vouchertype;
    }
    
    public void setVouchertype(String vouchertype) {
        this.vouchertype = vouchertype;
    }
    public Date getVoucherdate() {
        return this.voucherdate;
    }
    
    public void setVoucherdate(Date voucherdate) {
        this.voucherdate = voucherdate;
    }
    public String getSanctionedby() {
        return this.sanctionedby;
    }
    
    public void setSanctionedby(String sanctionedby) {
        this.sanctionedby = sanctionedby;
    }
    public String getFileno() {
        return this.fileno;
    }
    
    public void setFileno(String fileno) {
        this.fileno = fileno;
    }
    public String getNarration() {
        return this.narration;
    }
    
    public void setNarration(String narration) {
        this.narration = narration;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Date getSanctiondate() {
        return this.sanctiondate;
    }
    
    public void setSanctiondate(Date sanctiondate) {
        this.sanctiondate = sanctiondate;
    }
    public Boolean getPrinted() {
        return this.printed;
    }
    
    public void setPrinted(Boolean printed) {
        this.printed = printed;
    }
    public String getVoucherno() {
        return this.voucherno;
    }
    
    public void setVoucherno(String voucherno) {
        this.voucherno = voucherno;
    }
    public Date getVoucherapproveddate() {
        return this.voucherapproveddate;
    }
    
    public void setVoucherapproveddate(Date voucherapproveddate) {
        this.voucherapproveddate = voucherapproveddate;
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
    public Boolean getLocked() {
        return this.locked;
    }
    
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
    public Set getVoucherhistories() {
        return this.voucherhistories;
    }
    
    public void setVoucherhistories(Set voucherhistories) {
        this.voucherhistories = voucherhistories;
    }
    public Set getVoucherdetailshistories() {
        return this.voucherdetailshistories;
    }
    
    public void setVoucherdetailshistories(Set voucherdetailshistories) {
        this.voucherdetailshistories = voucherdetailshistories;
    }
    public Set getVoucherdetailses() {
        return this.voucherdetailses;
    }
    
    public void setVoucherdetailses(Set voucherdetailses) {
        this.voucherdetailses = voucherdetailses;
    }
    public Set getReceiptpaymentdetailshistories() {
        return this.receiptpaymentdetailshistories;
    }
    
    public void setReceiptpaymentdetailshistories(Set receiptpaymentdetailshistories) {
        this.receiptpaymentdetailshistories = receiptpaymentdetailshistories;
    }
    public Set getReceiptpaymentdetailses() {
        return this.receiptpaymentdetailses;
    }
    
    public void setReceiptpaymentdetailses(Set receiptpaymentdetailses) {
        this.receiptpaymentdetailses = receiptpaymentdetailses;
    }




}

