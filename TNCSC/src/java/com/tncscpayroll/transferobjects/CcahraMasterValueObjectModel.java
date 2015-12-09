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
@XmlRootElement(namespace = "CcahraMasterValueObjectModel")
public class CcahraMasterValueObjectModel {
    private String ccahraid;
    private String ccahra;
    private String paycode;

    /**
     * @return the ccahraid
     */
    public String getCcahraid() {
        return ccahraid;
    }

    /**
     * @param ccahraid the ccahraid to set
     */
    public void setCcahraid(String ccahraid) {
        this.ccahraid = ccahraid;
    }

    /**
     * @return the ccahra
     */
    public String getCcahra() {
        return ccahra;
    }

    /**
     * @param ccahra the ccahra to set
     */
    public void setCcahra(String ccahra) {
        this.ccahra = ccahra;
    }

    /**
     * @return the paycode
     */
    public String getPaycode() {
        return paycode;
    }

    /**
     * @param paycode the paycode to set
     */
    public void setPaycode(String paycode) {
        this.paycode = paycode;
    }

}
