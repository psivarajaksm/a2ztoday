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
public class DaArrearModel {

    private String epfno;
    private String employeename;
    private String region;
    private String section;
    private String designation;
    private String startmonth;
    private String endmonth;
    private int slipno;
    private List<DaArrearSubModel> daarrearlist;
    private String paymenttype;
    private String bankaccountno;
    private String daarrear;
    private String epf;
    private String net;
    private String chequeno;
    private String sectioncode;
    private String remarks;
    private boolean process;
    private String batchno;

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
     * @return the employeename
     */
    public String getEmployeename() {
        return employeename;
    }

    /**
     * @param employeename the employeename to set
     */
    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the section
     */
    public String getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return the startmonth
     */
    public String getStartmonth() {
        return startmonth;
    }

    /**
     * @param startmonth the startmonth to set
     */
    public void setStartmonth(String startmonth) {
        this.startmonth = startmonth;
    }

    /**
     * @return the endmonth
     */
    public String getEndmonth() {
        return endmonth;
    }

    /**
     * @param endmonth the endmonth to set
     */
    public void setEndmonth(String endmonth) {
        this.endmonth = endmonth;
    }

    /**
     * @return the slipno
     */
    public int getSlipno() {
        return slipno;
    }

    /**
     * @param slipno the slipno to set
     */
    public void setSlipno(int slipno) {
        this.slipno = slipno;
    }

    /**
     * @return the daarrearlist
     */
    public List<DaArrearSubModel> getDaarrearlist() {
        return daarrearlist;
    }

    /**
     * @param daarrearlist the daarrearlist to set
     */
    public void setDaarrearlist(List<DaArrearSubModel> daarrearlist) {
        this.daarrearlist = daarrearlist;
    }

    /**
     * @return the paymenttype
     */
    public String getPaymenttype() {
        return paymenttype;
    }

    /**
     * @param paymenttype the paymenttype to set
     */
    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    /**
     * @return the bankaccountno
     */
    public String getBankaccountno() {
        return bankaccountno;
    }

    /**
     * @param bankaccountno the bankaccountno to set
     */
    public void setBankaccountno(String bankaccountno) {
        this.bankaccountno = bankaccountno;
    }

    /**
     * @return the daarrear
     */
    public String getDaarrear() {
        return daarrear;
    }

    /**
     * @param daarrear the daarrear to set
     */
    public void setDaarrear(String daarrear) {
        this.daarrear = daarrear;
    }

    /**
     * @return the epf
     */
    public String getEpf() {
        return epf;
    }

    /**
     * @param epf the epf to set
     */
    public void setEpf(String epf) {
        this.epf = epf;
    }

    /**
     * @return the net
     */
    public String getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(String net) {
        this.net = net;
    }

    /**
     * @return the chequeno
     */
    public String getChequeno() {
        return chequeno;
    }

    /**
     * @param chequeno the chequeno to set
     */
    public void setChequeno(String chequeno) {
        this.chequeno = chequeno;
    }

    /**
     * @return the sectioncode
     */
    public String getSectioncode() {
        return sectioncode;
    }

    /**
     * @param sectioncode the sectioncode to set
     */
    public void setSectioncode(String sectioncode) {
        this.sectioncode = sectioncode;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the process
     */
    public boolean isProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(boolean process) {
        this.process = process;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }
    
}
