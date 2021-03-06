package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Employeeepfmaster generated by hbm2java
 */
public class Employeeepfmaster  implements java.io.Serializable {


     private String epfno;
     private String fpfno;
     private String employeename;
     private String fathername;
     private String gender;
     private Date dateofbirth;
     private Date dateofappoinment;
     private Date dateofprobation;
     private Date dateofconfirmation;
     private String region;
     private String section;
     private String designation;
     private String createdby;
     private Date createddate;
     private String community;
     private String employeecode;
     private Boolean synchronized_;
     private Boolean cancelled;
     private Set epfloanapplications = new HashSet(0);
     private Set epfopeningbalances = new HashSet(0);

    public Employeeepfmaster() {
    }

	
    public Employeeepfmaster(String epfno) {
        this.epfno = epfno;
    }
    public Employeeepfmaster(String epfno, String fpfno, String employeename, String fathername, String gender, Date dateofbirth, Date dateofappoinment, Date dateofprobation, Date dateofconfirmation, String region, String section, String designation, String createdby, Date createddate, String community, String employeecode, Boolean synchronized_, Boolean cancelled, Set epfloanapplications, Set epfopeningbalances) {
       this.epfno = epfno;
       this.fpfno = fpfno;
       this.employeename = employeename;
       this.fathername = fathername;
       this.gender = gender;
       this.dateofbirth = dateofbirth;
       this.dateofappoinment = dateofappoinment;
       this.dateofprobation = dateofprobation;
       this.dateofconfirmation = dateofconfirmation;
       this.region = region;
       this.section = section;
       this.designation = designation;
       this.createdby = createdby;
       this.createddate = createddate;
       this.community = community;
       this.employeecode = employeecode;
       this.synchronized_ = synchronized_;
       this.cancelled = cancelled;
       this.epfloanapplications = epfloanapplications;
       this.epfopeningbalances = epfopeningbalances;
    }
   
    public String getEpfno() {
        return this.epfno;
    }
    
    public void setEpfno(String epfno) {
        this.epfno = epfno;
    }
    public String getFpfno() {
        return this.fpfno;
    }
    
    public void setFpfno(String fpfno) {
        this.fpfno = fpfno;
    }
    public String getEmployeename() {
        return this.employeename;
    }
    
    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }
    public String getFathername() {
        return this.fathername;
    }
    
    public void setFathername(String fathername) {
        this.fathername = fathername;
    }
    public String getGender() {
        return this.gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Date getDateofbirth() {
        return this.dateofbirth;
    }
    
    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
    public Date getDateofappoinment() {
        return this.dateofappoinment;
    }
    
    public void setDateofappoinment(Date dateofappoinment) {
        this.dateofappoinment = dateofappoinment;
    }
    public Date getDateofprobation() {
        return this.dateofprobation;
    }
    
    public void setDateofprobation(Date dateofprobation) {
        this.dateofprobation = dateofprobation;
    }
    public Date getDateofconfirmation() {
        return this.dateofconfirmation;
    }
    
    public void setDateofconfirmation(Date dateofconfirmation) {
        this.dateofconfirmation = dateofconfirmation;
    }
    public String getRegion() {
        return this.region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    public String getSection() {
        return this.section;
    }
    
    public void setSection(String section) {
        this.section = section;
    }
    public String getDesignation() {
        return this.designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
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
    public String getCommunity() {
        return this.community;
    }
    
    public void setCommunity(String community) {
        this.community = community;
    }
    public String getEmployeecode() {
        return this.employeecode;
    }
    
    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }
    public Boolean getSynchronized_() {
        return this.synchronized_;
    }
    
    public void setSynchronized_(Boolean synchronized_) {
        this.synchronized_ = synchronized_;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Set getEpfloanapplications() {
        return this.epfloanapplications;
    }
    
    public void setEpfloanapplications(Set epfloanapplications) {
        this.epfloanapplications = epfloanapplications;
    }
    public Set getEpfopeningbalances() {
        return this.epfopeningbalances;
    }
    
    public void setEpfopeningbalances(Set epfopeningbalances) {
        this.epfopeningbalances = epfopeningbalances;
    }




}


