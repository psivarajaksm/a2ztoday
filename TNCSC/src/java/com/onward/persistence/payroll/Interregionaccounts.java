package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Interregionaccounts generated by hbm2java
 */
public class Interregionaccounts  implements java.io.Serializable {


     private String id;
     private Regionmaster regionmaster;
     private String fileno;
     private Date date;
     private Set interregionaccountsreconcils = new HashSet(0);

    public Interregionaccounts() {
    }

	
    public Interregionaccounts(String id) {
        this.id = id;
    }
    public Interregionaccounts(String id, Regionmaster regionmaster, String fileno, Date date, Set interregionaccountsreconcils) {
       this.id = id;
       this.regionmaster = regionmaster;
       this.fileno = fileno;
       this.date = date;
       this.interregionaccountsreconcils = interregionaccountsreconcils;
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
    public String getFileno() {
        return this.fileno;
    }
    
    public void setFileno(String fileno) {
        this.fileno = fileno;
    }
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    public Set getInterregionaccountsreconcils() {
        return this.interregionaccountsreconcils;
    }
    
    public void setInterregionaccountsreconcils(Set interregionaccountsreconcils) {
        this.interregionaccountsreconcils = interregionaccountsreconcils;
    }




}


