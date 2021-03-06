package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * Employeeprovidentfundothers generated by hbm2java
 */
public class Employeeprovidentfundothers  implements java.io.Serializable {


     private String id;
     private Employeemaster employeemaster;
     private Integer month;
     private Integer year;
     private BigDecimal salary;
     private BigDecimal epfwhole;
     private BigDecimal fbf;
     private BigDecimal rl;
     private BigDecimal vpf;
     private BigDecimal dvpf;
     private BigDecimal ecpf;
     private BigDecimal ecfb;
     private BigDecimal nrl;
     private BigDecimal subs;
     private BigDecimal contributions;
     private String empcategory;
     private String payrollcategory;
     private Boolean cancelled;
     private Integer smonth;
     private Integer syear;
     private String accregion;
     private String supprocessid;
     private String regprocessid;
     private Date createddate;
     private String createdby;

    public Employeeprovidentfundothers() {
    }

	
    public Employeeprovidentfundothers(String id) {
        this.id = id;
    }
    public Employeeprovidentfundothers(String id, Employeemaster employeemaster, Integer month, Integer year, BigDecimal salary, BigDecimal epfwhole, BigDecimal fbf, BigDecimal rl, BigDecimal vpf, BigDecimal dvpf, BigDecimal ecpf, BigDecimal ecfb, BigDecimal nrl, BigDecimal subs, BigDecimal contributions, String empcategory, String payrollcategory, Boolean cancelled, Integer smonth, Integer syear, String accregion, String supprocessid, String regprocessid, Date createddate, String createdby) {
       this.id = id;
       this.employeemaster = employeemaster;
       this.month = month;
       this.year = year;
       this.salary = salary;
       this.epfwhole = epfwhole;
       this.fbf = fbf;
       this.rl = rl;
       this.vpf = vpf;
       this.dvpf = dvpf;
       this.ecpf = ecpf;
       this.ecfb = ecfb;
       this.nrl = nrl;
       this.subs = subs;
       this.contributions = contributions;
       this.empcategory = empcategory;
       this.payrollcategory = payrollcategory;
       this.cancelled = cancelled;
       this.smonth = smonth;
       this.syear = syear;
       this.accregion = accregion;
       this.supprocessid = supprocessid;
       this.regprocessid = regprocessid;
       this.createddate = createddate;
       this.createdby = createdby;
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
    public BigDecimal getSalary() {
        return this.salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    public BigDecimal getEpfwhole() {
        return this.epfwhole;
    }
    
    public void setEpfwhole(BigDecimal epfwhole) {
        this.epfwhole = epfwhole;
    }
    public BigDecimal getFbf() {
        return this.fbf;
    }
    
    public void setFbf(BigDecimal fbf) {
        this.fbf = fbf;
    }
    public BigDecimal getRl() {
        return this.rl;
    }
    
    public void setRl(BigDecimal rl) {
        this.rl = rl;
    }
    public BigDecimal getVpf() {
        return this.vpf;
    }
    
    public void setVpf(BigDecimal vpf) {
        this.vpf = vpf;
    }
    public BigDecimal getDvpf() {
        return this.dvpf;
    }
    
    public void setDvpf(BigDecimal dvpf) {
        this.dvpf = dvpf;
    }
    public BigDecimal getEcpf() {
        return this.ecpf;
    }
    
    public void setEcpf(BigDecimal ecpf) {
        this.ecpf = ecpf;
    }
    public BigDecimal getEcfb() {
        return this.ecfb;
    }
    
    public void setEcfb(BigDecimal ecfb) {
        this.ecfb = ecfb;
    }
    public BigDecimal getNrl() {
        return this.nrl;
    }
    
    public void setNrl(BigDecimal nrl) {
        this.nrl = nrl;
    }
    public BigDecimal getSubs() {
        return this.subs;
    }
    
    public void setSubs(BigDecimal subs) {
        this.subs = subs;
    }
    public BigDecimal getContributions() {
        return this.contributions;
    }
    
    public void setContributions(BigDecimal contributions) {
        this.contributions = contributions;
    }
    public String getEmpcategory() {
        return this.empcategory;
    }
    
    public void setEmpcategory(String empcategory) {
        this.empcategory = empcategory;
    }
    public String getPayrollcategory() {
        return this.payrollcategory;
    }
    
    public void setPayrollcategory(String payrollcategory) {
        this.payrollcategory = payrollcategory;
    }
    public Boolean getCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Integer getSmonth() {
        return this.smonth;
    }
    
    public void setSmonth(Integer smonth) {
        this.smonth = smonth;
    }
    public Integer getSyear() {
        return this.syear;
    }
    
    public void setSyear(Integer syear) {
        this.syear = syear;
    }
    public String getAccregion() {
        return this.accregion;
    }
    
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }
    public String getSupprocessid() {
        return this.supprocessid;
    }
    
    public void setSupprocessid(String supprocessid) {
        this.supprocessid = supprocessid;
    }
    public String getRegprocessid() {
        return this.regprocessid;
    }
    
    public void setRegprocessid(String regprocessid) {
        this.regprocessid = regprocessid;
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


