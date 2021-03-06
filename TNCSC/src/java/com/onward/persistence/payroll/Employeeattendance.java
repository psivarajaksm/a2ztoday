package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA



/**
 * Employeeattendance generated by hbm2java
 */
public class Employeeattendance  implements java.io.Serializable {


     private String id;
     private Employeemaster employeemaster;
     private Integer year;
     private Integer month;
     private Integer present;
     private Integer woff;
     private Integer eldays;
     private Integer mldays;
     private Integer cldays;
     private Integer mdays;
     private Integer suspsdays;
     private Integer others;
     private Integer llp;
     private Integer uelp;
     private Integer totdays;
     private Boolean synchronized_;
     private String accregion;

    public Employeeattendance() {
    }

	
    public Employeeattendance(String id) {
        this.id = id;
    }
    public Employeeattendance(String id, Employeemaster employeemaster, Integer year, Integer month, Integer present, Integer woff, Integer eldays, Integer mldays, Integer cldays, Integer mdays, Integer suspsdays, Integer others, Integer llp, Integer uelp, Integer totdays, Boolean synchronized_, String accregion) {
       this.id = id;
       this.employeemaster = employeemaster;
       this.year = year;
       this.month = month;
       this.present = present;
       this.woff = woff;
       this.eldays = eldays;
       this.mldays = mldays;
       this.cldays = cldays;
       this.mdays = mdays;
       this.suspsdays = suspsdays;
       this.others = others;
       this.llp = llp;
       this.uelp = uelp;
       this.totdays = totdays;
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
    public Integer getYear() {
        return this.year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getMonth() {
        return this.month;
    }
    
    public void setMonth(Integer month) {
        this.month = month;
    }
    public Integer getPresent() {
        return this.present;
    }
    
    public void setPresent(Integer present) {
        this.present = present;
    }
    public Integer getWoff() {
        return this.woff;
    }
    
    public void setWoff(Integer woff) {
        this.woff = woff;
    }
    public Integer getEldays() {
        return this.eldays;
    }
    
    public void setEldays(Integer eldays) {
        this.eldays = eldays;
    }
    public Integer getMldays() {
        return this.mldays;
    }
    
    public void setMldays(Integer mldays) {
        this.mldays = mldays;
    }
    public Integer getCldays() {
        return this.cldays;
    }
    
    public void setCldays(Integer cldays) {
        this.cldays = cldays;
    }
    public Integer getMdays() {
        return this.mdays;
    }
    
    public void setMdays(Integer mdays) {
        this.mdays = mdays;
    }
    public Integer getSuspsdays() {
        return this.suspsdays;
    }
    
    public void setSuspsdays(Integer suspsdays) {
        this.suspsdays = suspsdays;
    }
    public Integer getOthers() {
        return this.others;
    }
    
    public void setOthers(Integer others) {
        this.others = others;
    }
    public Integer getLlp() {
        return this.llp;
    }
    
    public void setLlp(Integer llp) {
        this.llp = llp;
    }
    public Integer getUelp() {
        return this.uelp;
    }
    
    public void setUelp(Integer uelp) {
        this.uelp = uelp;
    }
    public Integer getTotdays() {
        return this.totdays;
    }
    
    public void setTotdays(Integer totdays) {
        this.totdays = totdays;
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




}


