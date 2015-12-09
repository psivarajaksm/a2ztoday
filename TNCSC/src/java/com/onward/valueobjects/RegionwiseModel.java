/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.valueobjects;

import java.util.List;

/**
 *
 * @author Prince vijayakumar M
 */
public class RegionwiseModel {

    private int slipno;
    private String pfno;
    private String employeename;
    private String designation;
    private String regionname;
    private List amountlist;
    private String type;

    public int getSlipno() {
        return slipno;
    }

    public void setSlipno(int slipno) {
        this.slipno = slipno;
    }

    public String getPfno() {
        return pfno;
    }

    public void setPfno(String pfno) {
        this.pfno = pfno;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public List getAmountlist() {
        return amountlist;
    }

    public void setAmountlist(List amountlist) {
        this.amountlist = amountlist;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
