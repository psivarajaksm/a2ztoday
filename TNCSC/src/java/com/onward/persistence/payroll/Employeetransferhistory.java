package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Employeetransferhistory generated by hbm2java
 */
public class Employeetransferhistory  implements java.io.Serializable {


     private String id;
     private Employeemaster employeemaster;
     private String regioncode;
     private Date startdate;
     private Date enddate;
     private boolean synchronized_;
     private String accregion;

    public Employeetransferhistory() {
    }

	
    public Employeetransferhistory(String id, Employeemaster employeemaster, boolean synchronized_) {
        this.id = id;
        this.employeemaster = employeemaster;
        this.synchronized_ = synchronized_;
    }
    public Employeetransferhistory(String id, Employeemaster employeemaster, String regioncode, Date startdate, Date enddate, boolean synchronized_, String accregion) {
       this.id = id;
       this.employeemaster = employeemaster;
       this.regioncode = regioncode;
       this.startdate = startdate;
       this.enddate = enddate;
       this.synchronized_ = synchronized_;
       this.accregion = accregion;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public Employeemaster getEmployeemaster() {
        return this.employeemaster;
    }
    
    public void setEmployeemaster(Employeemaster employeemaster) {
        this.employeemaster = employeemaster;
    }
    public String getRegioncode() {
        return this.regioncode;
    }
    
    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }
    public Date getStartdate() {
        return this.startdate;
    }
    
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }
    public Date getEnddate() {
        return this.enddate;
    }
    
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
    public boolean isSynchronized_() {
        return this.synchronized_;
    }
    
    public void setSynchronized_(boolean synchronized_) {
        this.synchronized_ = synchronized_;
    }
    public String getAccregion() {
        return this.accregion;
    }
    
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }




}


