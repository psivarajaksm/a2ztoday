/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@XmlRootElement(namespace = "DesignationMasterValueObjectModel")
public class DesignationMasterValueObjectModel {

    private String designationcode;
    private String designation;
    private String payscalecode;
    private String remarks;
    private String orderno;

    /**
     * @return the designationcode
     */
    public String getDesignationcode() {
        return designationcode;
    }

    /**
     * @param designationcode the designationcode to set
     */
    public void setDesignationcode(String designationcode) {
        this.designationcode = designationcode;
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
     * @return the payscalecode
     */
    public String getPayscalecode() {
        return payscalecode;
    }

    /**
     * @param payscalecode the payscalecode to set
     */
    public void setPayscalecode(String payscalecode) {
        this.payscalecode = payscalecode;
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
     * @return the orderno
     */
    public String getOrderno() {
        return orderno;
    }

    /**
     * @param orderno the orderno to set
     */
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
