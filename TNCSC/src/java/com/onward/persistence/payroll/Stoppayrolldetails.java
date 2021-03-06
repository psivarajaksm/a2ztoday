package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Stoppayrolldetails generated by hbm2java
 */
public class Stoppayrolldetails  implements java.io.Serializable {


     private String id;
     private Employeemaster employeemaster;
     private Date startdate;
     private Date enddate;
     private String reasoncode;
     private String remarks;
     private Boolean synchronized_;
     private String accregion;
     private Date createddate;
     private String createdby;
     private String oldremarks;

    public Stoppayrolldetails() {
    }

	
    public Stoppayrolldetails(String id) {
        this.id = id;
    }
    public Stoppayrolldetails(String id, Employeemaster employeemaster, Date startdate, Date enddate, String reasoncode, String remarks, Boolean synchronized_, String accregion, Date createddate, String createdby, String oldremarks) {
       this.id = id;
       this.employeemaster = employeemaster;
       this.startdate = startdate;
       this.enddate = enddate;
       this.reasoncode = reasoncode;
       this.remarks = remarks;
       this.synchronized_ = synchronized_;
       this.accregion = accregion;
       this.createddate = createddate;
       this.createdby = createdby;
       this.oldremarks = oldremarks;
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
    public String getReasoncode() {
        return this.reasoncode;
    }
    
    public void setReasoncode(String reasoncode) {
        this.reasoncode = reasoncode;
    }
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Boolean getSynchronized_() {
        return this.synchronized_;
    }
    
    public void setSynchronized_(Boolean synchronized_) {
        this.synchronized_ = synchronized_;
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
    public String getOldremarks() {
        return this.oldremarks;
    }
    
    public void setOldremarks(String oldremarks) {
        this.oldremarks = oldremarks;
    }




}


