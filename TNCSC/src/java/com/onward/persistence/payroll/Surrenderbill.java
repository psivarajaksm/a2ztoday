package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Surrenderbill generated by hbm2java
 */
public class Surrenderbill  implements java.io.Serializable {


     private String id;
     private Employeemaster employeemaster;
     private String typeofsupplementary;
     private Integer noofleavesurrender;
     private String orderno;
     private Date currentdate;
     private boolean synchronized_;

    public Surrenderbill() {
    }

	
    public Surrenderbill(String id, Employeemaster employeemaster, boolean synchronized_) {
        this.id = id;
        this.employeemaster = employeemaster;
        this.synchronized_ = synchronized_;
    }
    public Surrenderbill(String id, Employeemaster employeemaster, String typeofsupplementary, Integer noofleavesurrender, String orderno, Date currentdate, boolean synchronized_) {
       this.id = id;
       this.employeemaster = employeemaster;
       this.typeofsupplementary = typeofsupplementary;
       this.noofleavesurrender = noofleavesurrender;
       this.orderno = orderno;
       this.currentdate = currentdate;
       this.synchronized_ = synchronized_;
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
    public String getTypeofsupplementary() {
        return this.typeofsupplementary;
    }
    
    public void setTypeofsupplementary(String typeofsupplementary) {
        this.typeofsupplementary = typeofsupplementary;
    }
    public Integer getNoofleavesurrender() {
        return this.noofleavesurrender;
    }
    
    public void setNoofleavesurrender(Integer noofleavesurrender) {
        this.noofleavesurrender = noofleavesurrender;
    }
    public String getOrderno() {
        return this.orderno;
    }
    
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
    public Date getCurrentdate() {
        return this.currentdate;
    }
    
    public void setCurrentdate(Date currentdate) {
        this.currentdate = currentdate;
    }
    public boolean isSynchronized_() {
        return this.synchronized_;
    }
    
    public void setSynchronized_(boolean synchronized_) {
        this.synchronized_ = synchronized_;
    }




}


