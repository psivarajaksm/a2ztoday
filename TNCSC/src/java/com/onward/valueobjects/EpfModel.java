/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.valueobjects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Prince vijayakumar M
 */
@XmlRootElement(name = "epfModelObj")
@XmlType(propOrder = {"id", "epfno", "month", "year", "salary", "epfwhole", "fbf", "rl", "vpf", "dvpf", "ecpf", "ecfb", "nrl", "subs", "contributions", "empcategory", "payrollcategory", "cancelled", "smonth", "syear", "accregion", "supprocessid", "regprocessid"})
public class EpfModel {

    private String id;
    private String epfno;
    private String month;
    private String year;
    private String salary;
    private String epfwhole;
    private String fbf;
    private String rl;
    private String vpf;
    private String dvpf;
    private String ecpf;
    private String ecfb;
    private String nrl;
    private String subs;
    private String contributions;
    private String empcategory;
    private String payrollcategory;
    private String cancelled;
    private String smonth;
    private String syear;
    private String accregion;
    private String supprocessid;
    private String regprocessid;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the epfno
     */
    public String getEpfno() {
        return epfno;
    }

    /**
     * @param epfno the epfno to set
     */
    public void setEpfno(String epfno) {
        this.epfno = epfno;
    }

    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the salary
     */
    public String getSalary() {
        return salary;
    }

    /**
     * @param salary the salary to set
     */
    public void setSalary(String salary) {
        this.salary = salary;
    }

    /**
     * @return the epfwhole
     */
    public String getEpfwhole() {
        return epfwhole;
    }

    /**
     * @param epfwhole the epfwhole to set
     */
    public void setEpfwhole(String epfwhole) {
        this.epfwhole = epfwhole;
    }

    /**
     * @return the fbf
     */
    public String getFbf() {
        return fbf;
    }

    /**
     * @param fbf the fbf to set
     */
    public void setFbf(String fbf) {
        this.fbf = fbf;
    }

    /**
     * @return the rl
     */
    public String getRl() {
        return rl;
    }

    /**
     * @param rl the rl to set
     */
    public void setRl(String rl) {
        this.rl = rl;
    }

    /**
     * @return the vpf
     */
    public String getVpf() {
        return vpf;
    }

    /**
     * @param vpf the vpf to set
     */
    public void setVpf(String vpf) {
        this.vpf = vpf;
    }

    /**
     * @return the dvpf
     */
    public String getDvpf() {
        return dvpf;
    }

    /**
     * @param dvpf the dvpf to set
     */
    public void setDvpf(String dvpf) {
        this.dvpf = dvpf;
    }

    /**
     * @return the ecpf
     */
    public String getEcpf() {
        return ecpf;
    }

    /**
     * @param ecpf the ecpf to set
     */
    public void setEcpf(String ecpf) {
        this.ecpf = ecpf;
    }

    /**
     * @return the ecfb
     */
    public String getEcfb() {
        return ecfb;
    }

    /**
     * @param ecfb the ecfb to set
     */
    public void setEcfb(String ecfb) {
        this.ecfb = ecfb;
    }

    /**
     * @return the nrl
     */
    public String getNrl() {
        return nrl;
    }

    /**
     * @param nrl the nrl to set
     */
    public void setNrl(String nrl) {
        this.nrl = nrl;
    }

    /**
     * @return the subs
     */
    public String getSubs() {
        return subs;
    }

    /**
     * @param subs the subs to set
     */
    public void setSubs(String subs) {
        this.subs = subs;
    }

    /**
     * @return the contributions
     */
    public String getContributions() {
        return contributions;
    }

    /**
     * @param contributions the contributions to set
     */
    public void setContributions(String contributions) {
        this.contributions = contributions;
    }

    /**
     * @return the empcategory
     */
    public String getEmpcategory() {
        return empcategory;
    }

    /**
     * @param empcategory the empcategory to set
     */
    public void setEmpcategory(String empcategory) {
        this.empcategory = empcategory;
    }

    /**
     * @return the payrollcategory
     */
    public String getPayrollcategory() {
        return payrollcategory;
    }

    /**
     * @param payrollcategory the payrollcategory to set
     */
    public void setPayrollcategory(String payrollcategory) {
        this.payrollcategory = payrollcategory;
    }

    /**
     * @return the cancelled
     */
    public String getCancelled() {
        return cancelled;
    }

    /**
     * @param cancelled the cancelled to set
     */
    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * @return the smonth
     */
    public String getSmonth() {
        return smonth;
    }

    /**
     * @param smonth the smonth to set
     */
    public void setSmonth(String smonth) {
        this.smonth = smonth;
    }

    /**
     * @return the syear
     */
    public String getSyear() {
        return syear;
    }

    /**
     * @param syear the syear to set
     */
    public void setSyear(String syear) {
        this.syear = syear;
    }

    /**
     * @return the accregion
     */
    public String getAccregion() {
        return accregion;
    }

    /**
     * @param accregion the accregion to set
     */
    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }

    /**
     * @return the supprocessid
     */
    public String getSupprocessid() {
        return supprocessid;
    }

    /**
     * @param supprocessid the supprocessid to set
     */
    public void setSupprocessid(String supprocessid) {
        this.supprocessid = supprocessid;
    }

    /**
     * @return the regprocessid
     */
    public String getRegprocessid() {
        return regprocessid;
    }

    /**
     * @param regprocessid the regprocessid to set
     */
    public void setRegprocessid(String regprocessid) {
        this.regprocessid = regprocessid;
    }
}
