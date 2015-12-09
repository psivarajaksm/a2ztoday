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
@XmlRootElement(namespace = "PaycodeMasterValueObjectModel")
public class PaycodeMasterValueObjectModel {

    private String paycode;
    private String paycodename;
    private String paycodetype;
    private String paypercentage;
    private String grouphead;
    private String paycodeserial;

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

    /**
     * @return the paycodename
     */
    public String getPaycodename() {
        return paycodename;
    }

    /**
     * @param paycodename the paycodename to set
     */
    public void setPaycodename(String paycodename) {
        this.paycodename = paycodename;
    }

    /**
     * @return the paycodetype
     */
    public String getPaycodetype() {
        return paycodetype;
    }

    /**
     * @param paycodetype the paycodetype to set
     */
    public void setPaycodetype(String paycodetype) {
        this.paycodetype = paycodetype;
    }

    /**
     * @return the paypercentage
     */
    public String getPaypercentage() {
        return paypercentage;
    }

    /**
     * @param paypercentage the paypercentage to set
     */
    public void setPaypercentage(String paypercentage) {
        this.paypercentage = paypercentage;
    }

    /**
     * @return the grouphead
     */
    public String getGrouphead() {
        return grouphead;
    }

    /**
     * @param grouphead the grouphead to set
     */
    public void setGrouphead(String grouphead) {
        this.grouphead = grouphead;
    }

    /**
     * @return the paycodeserial
     */
    public String getPaycodeserial() {
        return paycodeserial;
    }

    /**
     * @param paycodeserial the paycodeserial to set
     */
    public void setPaycodeserial(String paycodeserial) {
        this.paycodeserial = paycodeserial;
    }

}
