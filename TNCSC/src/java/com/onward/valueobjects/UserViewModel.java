package com.onward.valueobjects;


import com.onward.persistence.payroll.Usermaster;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jagan Mohan. B
 */
public class UserViewModel {

    private String functiontype = "";
    private String errorMessage = "";
    private String userid = "";
    private String username = "";
    private String mobilenumber = "";
    private String fathername = "";
    private String emailid = "";
    private String usertype = "";
    private String gender = "";
    private String dateofbirth = "";
    private String toDayDate = "";
    private long customerid = 0;
    private String firstName = "";
    private String password = "";
    private byte[] encryptPassword = null;
    private String buildingfloor = "";
    private String streetname = "";
    private String housenumber = "";
    private String pincode = "";
    private String country = "";
    private String state = "";
    private String city = "";
    private String logindate = "";
    private String ipaddress = "";
    private String producttype = "";
    private String productdescription = "";    
    private Usermaster usermaster = null;
    private String regionname = "";
    private String regioncode = "";


    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getFunctiontype() {
        return functiontype;
    }

    public void setFunctiontype(String functiontype) {
        this.functiontype = functiontype;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getToDayDate() {
        return toDayDate;
    }

    public void setToDayDate(String toDayDate) {
        this.toDayDate = toDayDate;
    }

    public long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(long customerid) {
        this.customerid = customerid;
    }

    public String getBuildingfloor() {
        return buildingfloor;
    }

    public void setBuildingfloor(String buildingfloor) {
        this.buildingfloor = buildingfloor;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(byte[] encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usermaster getUsermaster() {
        return usermaster;
    }

    public void setUsermaster(Usermaster usermaster) {
        this.usermaster = usermaster;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    /**
     * @return the regionname
     */
    public String getRegionname() {
        return regionname;
    }

    /**
     * @param regionname the regionname to set
     */
    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    /**
     * @return the regioncode
     */
    public String getRegioncode() {
        return regioncode;
    }

    /**
     * @param regioncode the regioncode to set
     */
    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    
    
}
