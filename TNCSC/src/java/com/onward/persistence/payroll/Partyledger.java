package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Partyledger generated by hbm2java
 */
public class Partyledger  implements java.io.Serializable {


     private String code;
     private Regionmaster regionmaster;
     private String partyname;
     private String tinno;
     private Set receiptpaymentdetailses = new HashSet(0);
     private Set vatonpurchases = new HashSet(0);
     private Set vatonsaleses = new HashSet(0);
     private Set receiptpaymentdetailshistories = new HashSet(0);

    public Partyledger() {
    }

	
    public Partyledger(String code) {
        this.code = code;
    }
    public Partyledger(String code, Regionmaster regionmaster, String partyname, String tinno, Set receiptpaymentdetailses, Set vatonpurchases, Set vatonsaleses, Set receiptpaymentdetailshistories) {
       this.code = code;
       this.regionmaster = regionmaster;
       this.partyname = partyname;
       this.tinno = tinno;
       this.receiptpaymentdetailses = receiptpaymentdetailses;
       this.vatonpurchases = vatonpurchases;
       this.vatonsaleses = vatonsaleses;
       this.receiptpaymentdetailshistories = receiptpaymentdetailshistories;
    }
   
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    public Regionmaster getRegionmaster() {
        return this.regionmaster;
    }
    
    public void setRegionmaster(Regionmaster regionmaster) {
        this.regionmaster = regionmaster;
    }
    public String getPartyname() {
        return this.partyname;
    }
    
    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }
    public String getTinno() {
        return this.tinno;
    }
    
    public void setTinno(String tinno) {
        this.tinno = tinno;
    }
    public Set getReceiptpaymentdetailses() {
        return this.receiptpaymentdetailses;
    }
    
    public void setReceiptpaymentdetailses(Set receiptpaymentdetailses) {
        this.receiptpaymentdetailses = receiptpaymentdetailses;
    }
    public Set getVatonpurchases() {
        return this.vatonpurchases;
    }
    
    public void setVatonpurchases(Set vatonpurchases) {
        this.vatonpurchases = vatonpurchases;
    }
    public Set getVatonsaleses() {
        return this.vatonsaleses;
    }
    
    public void setVatonsaleses(Set vatonsaleses) {
        this.vatonsaleses = vatonsaleses;
    }
    public Set getReceiptpaymentdetailshistories() {
        return this.receiptpaymentdetailshistories;
    }
    
    public void setReceiptpaymentdetailshistories(Set receiptpaymentdetailshistories) {
        this.receiptpaymentdetailshistories = receiptpaymentdetailshistories;
    }




}


