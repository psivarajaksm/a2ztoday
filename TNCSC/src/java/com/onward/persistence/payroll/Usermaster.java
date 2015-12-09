package com.onward.persistence.payroll;
// Generated 24 Aug, 2015 3:09:20 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Usermaster generated by hbm2java
 */
public class Usermaster  implements java.io.Serializable {


     private String userid;
     private Employeemaster employeemaster;
     private Integer sequencenumber;
     private String salutationtype;
     private String username;
     private String usershortname;
     private String gender;
     private Date dateofbirth;
     private String address;
     private String mobilenumber;
     private String fathername;
     private String designation;
     private String emailid;
     private Integer userstatus;
     private Date nextpwddate;
     private String encryptpassword;
     private String secretekey;
     private String secretquestionone;
     private String youranswerone;
     private String secretquestiontwo;
     private String youranswertwo;
     private String favouritecolor;
     private String createdby;
     private Date createddate;
     private String employeeid;
     private String secretkey;
     private String updatedby;
     private Date updateddate;
     private String region;
     private Set useroperatingrightses = new HashSet(0);

    public Usermaster() {
    }

	
    public Usermaster(String userid, String region) {
        this.userid = userid;
        this.region = region;
    }
    public Usermaster(String userid, Employeemaster employeemaster, Integer sequencenumber, String salutationtype, String username, String usershortname, String gender, Date dateofbirth, String address, String mobilenumber, String fathername, String designation, String emailid, Integer userstatus, Date nextpwddate, String encryptpassword, String secretekey, String secretquestionone, String youranswerone, String secretquestiontwo, String youranswertwo, String favouritecolor, String createdby, Date createddate, String employeeid, String secretkey, String updatedby, Date updateddate, String region, Set useroperatingrightses) {
       this.userid = userid;
       this.employeemaster = employeemaster;
       this.sequencenumber = sequencenumber;
       this.salutationtype = salutationtype;
       this.username = username;
       this.usershortname = usershortname;
       this.gender = gender;
       this.dateofbirth = dateofbirth;
       this.address = address;
       this.mobilenumber = mobilenumber;
       this.fathername = fathername;
       this.designation = designation;
       this.emailid = emailid;
       this.userstatus = userstatus;
       this.nextpwddate = nextpwddate;
       this.encryptpassword = encryptpassword;
       this.secretekey = secretekey;
       this.secretquestionone = secretquestionone;
       this.youranswerone = youranswerone;
       this.secretquestiontwo = secretquestiontwo;
       this.youranswertwo = youranswertwo;
       this.favouritecolor = favouritecolor;
       this.createdby = createdby;
       this.createddate = createddate;
       this.employeeid = employeeid;
       this.secretkey = secretkey;
       this.updatedby = updatedby;
       this.updateddate = updateddate;
       this.region = region;
       this.useroperatingrightses = useroperatingrightses;
    }
   
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public Employeemaster getEmployeemaster() {
        return this.employeemaster;
    }
    
    public void setEmployeemaster(Employeemaster employeemaster) {
        this.employeemaster = employeemaster;
    }
    public Integer getSequencenumber() {
        return this.sequencenumber;
    }
    
    public void setSequencenumber(Integer sequencenumber) {
        this.sequencenumber = sequencenumber;
    }
    public String getSalutationtype() {
        return this.salutationtype;
    }
    
    public void setSalutationtype(String salutationtype) {
        this.salutationtype = salutationtype;
    }
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsershortname() {
        return this.usershortname;
    }
    
    public void setUsershortname(String usershortname) {
        this.usershortname = usershortname;
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
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getMobilenumber() {
        return this.mobilenumber;
    }
    
    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }
    public String getFathername() {
        return this.fathername;
    }
    
    public void setFathername(String fathername) {
        this.fathername = fathername;
    }
    public String getDesignation() {
        return this.designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getEmailid() {
        return this.emailid;
    }
    
    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }
    public Integer getUserstatus() {
        return this.userstatus;
    }
    
    public void setUserstatus(Integer userstatus) {
        this.userstatus = userstatus;
    }
    public Date getNextpwddate() {
        return this.nextpwddate;
    }
    
    public void setNextpwddate(Date nextpwddate) {
        this.nextpwddate = nextpwddate;
    }
    public String getEncryptpassword() {
        return this.encryptpassword;
    }
    
    public void setEncryptpassword(String encryptpassword) {
        this.encryptpassword = encryptpassword;
    }
    public String getSecretekey() {
        return this.secretekey;
    }
    
    public void setSecretekey(String secretekey) {
        this.secretekey = secretekey;
    }
    public String getSecretquestionone() {
        return this.secretquestionone;
    }
    
    public void setSecretquestionone(String secretquestionone) {
        this.secretquestionone = secretquestionone;
    }
    public String getYouranswerone() {
        return this.youranswerone;
    }
    
    public void setYouranswerone(String youranswerone) {
        this.youranswerone = youranswerone;
    }
    public String getSecretquestiontwo() {
        return this.secretquestiontwo;
    }
    
    public void setSecretquestiontwo(String secretquestiontwo) {
        this.secretquestiontwo = secretquestiontwo;
    }
    public String getYouranswertwo() {
        return this.youranswertwo;
    }
    
    public void setYouranswertwo(String youranswertwo) {
        this.youranswertwo = youranswertwo;
    }
    public String getFavouritecolor() {
        return this.favouritecolor;
    }
    
    public void setFavouritecolor(String favouritecolor) {
        this.favouritecolor = favouritecolor;
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
    public String getEmployeeid() {
        return this.employeeid;
    }
    
    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }
    public String getSecretkey() {
        return this.secretkey;
    }
    
    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }
    public String getUpdatedby() {
        return this.updatedby;
    }
    
    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }
    public Date getUpdateddate() {
        return this.updateddate;
    }
    
    public void setUpdateddate(Date updateddate) {
        this.updateddate = updateddate;
    }
    public String getRegion() {
        return this.region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    public Set getUseroperatingrightses() {
        return this.useroperatingrightses;
    }
    
    public void setUseroperatingrightses(Set useroperatingrightses) {
        this.useroperatingrightses = useroperatingrightses;
    }




}

