package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Employeerecoveries generated by hbm2java
 */
public class Employeerecoveries  implements java.io.Serializable {


     private String id;
     private Employeemaster employeemaster;
     private String deductioncode;
     private Date loandate;
     private BigDecimal loanamount;
     private Integer totalinstallment;
     private Integer currentinstallment;
     private BigDecimal installmentamount;
     private BigDecimal firstinstallmentamount;
     private BigDecimal loanbalance;
     private String loantype;
     private String status;
     private String fileno;
     private String regionno;
     private Boolean synchronized_;
     private String accregion;
     private Set employeerecoverydetailses = new HashSet(0);

    public Employeerecoveries() {
    }

	
    public Employeerecoveries(String id) {
        this.id = id;
    }
    public Employeerecoveries(String id, Employeemaster employeemaster, String deductioncode, Date loandate, BigDecimal loanamount, Integer totalinstallment, Integer currentinstallment, BigDecimal installmentamount, BigDecimal firstinstallmentamount, BigDecimal loanbalance, String loantype, String status, String fileno, String regionno, Boolean synchronized_, String accregion, Set employeerecoverydetailses) {
       this.id = id;
       this.employeemaster = employeemaster;
       this.deductioncode = deductioncode;
       this.loandate = loandate;
       this.loanamount = loanamount;
       this.totalinstallment = totalinstallment;
       this.currentinstallment = currentinstallment;
       this.installmentamount = installmentamount;
       this.firstinstallmentamount = firstinstallmentamount;
       this.loanbalance = loanbalance;
       this.loantype = loantype;
       this.status = status;
       this.fileno = fileno;
       this.regionno = regionno;
       this.synchronized_ = synchronized_;
       this.accregion = accregion;
       this.employeerecoverydetailses = employeerecoverydetailses;
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
    public String getDeductioncode() {
        return this.deductioncode;
    }
    
    public void setDeductioncode(String deductioncode) {
        this.deductioncode = deductioncode;
    }
    public Date getLoandate() {
        return this.loandate;
    }
    
    public void setLoandate(Date loandate) {
        this.loandate = loandate;
    }
    public BigDecimal getLoanamount() {
        return this.loanamount;
    }
    
    public void setLoanamount(BigDecimal loanamount) {
        this.loanamount = loanamount;
    }
    public Integer getTotalinstallment() {
        return this.totalinstallment;
    }
    
    public void setTotalinstallment(Integer totalinstallment) {
        this.totalinstallment = totalinstallment;
    }
    public Integer getCurrentinstallment() {
        return this.currentinstallment;
    }
    
    public void setCurrentinstallment(Integer currentinstallment) {
        this.currentinstallment = currentinstallment;
    }
    public BigDecimal getInstallmentamount() {
        return this.installmentamount;
    }
    
    public void setInstallmentamount(BigDecimal installmentamount) {
        this.installmentamount = installmentamount;
    }
    public BigDecimal getFirstinstallmentamount() {
        return this.firstinstallmentamount;
    }
    
    public void setFirstinstallmentamount(BigDecimal firstinstallmentamount) {
        this.firstinstallmentamount = firstinstallmentamount;
    }
    public BigDecimal getLoanbalance() {
        return this.loanbalance;
    }
    
    public void setLoanbalance(BigDecimal loanbalance) {
        this.loanbalance = loanbalance;
    }
    public String getLoantype() {
        return this.loantype;
    }
    
    public void setLoantype(String loantype) {
        this.loantype = loantype;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFileno() {
        return this.fileno;
    }
    
    public void setFileno(String fileno) {
        this.fileno = fileno;
    }
    public String getRegionno() {
        return this.regionno;
    }
    
    public void setRegionno(String regionno) {
        this.regionno = regionno;
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
    public Set getEmployeerecoverydetailses() {
        return this.employeerecoverydetailses;
    }
    
    public void setEmployeerecoverydetailses(Set employeerecoverydetailses) {
        this.employeerecoverydetailses = employeerecoverydetailses;
    }




}


