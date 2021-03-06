package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Accountgroups generated by hbm2java
 */
public class Accountgroups  implements java.io.Serializable {


     private String id;
     private String grpcode;
     private String facode;
     private String groupname;
     private String orderno;
     private Long accheadstartingno;
     private Long accheadendingno;
     private Long accheadrunningno;
     private Set accountsheadses = new HashSet(0);

    public Accountgroups() {
    }

	
    public Accountgroups(String id) {
        this.id = id;
    }
    public Accountgroups(String id, String grpcode, String facode, String groupname, String orderno, Long accheadstartingno, Long accheadendingno, Long accheadrunningno, Set accountsheadses) {
       this.id = id;
       this.grpcode = grpcode;
       this.facode = facode;
       this.groupname = groupname;
       this.orderno = orderno;
       this.accheadstartingno = accheadstartingno;
       this.accheadendingno = accheadendingno;
       this.accheadrunningno = accheadrunningno;
       this.accountsheadses = accountsheadses;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getGrpcode() {
        return this.grpcode;
    }
    
    public void setGrpcode(String grpcode) {
        this.grpcode = grpcode;
    }
    public String getFacode() {
        return this.facode;
    }
    
    public void setFacode(String facode) {
        this.facode = facode;
    }
    public String getGroupname() {
        return this.groupname;
    }
    
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
    public String getOrderno() {
        return this.orderno;
    }
    
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
    public Long getAccheadstartingno() {
        return this.accheadstartingno;
    }
    
    public void setAccheadstartingno(Long accheadstartingno) {
        this.accheadstartingno = accheadstartingno;
    }
    public Long getAccheadendingno() {
        return this.accheadendingno;
    }
    
    public void setAccheadendingno(Long accheadendingno) {
        this.accheadendingno = accheadendingno;
    }
    public Long getAccheadrunningno() {
        return this.accheadrunningno;
    }
    
    public void setAccheadrunningno(Long accheadrunningno) {
        this.accheadrunningno = accheadrunningno;
    }
    public Set getAccountsheadses() {
        return this.accountsheadses;
    }
    
    public void setAccountsheadses(Set accountsheadses) {
        this.accountsheadses = accountsheadses;
    }




}


