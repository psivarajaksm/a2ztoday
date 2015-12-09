/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "supplementaryemployeedeductionstransactionspost")
public class SupplementaryemployeedeductionstransactionsPost {

    private String id;
    private String supplementarypayrollprocessingdetailsId;
    private String deductionmasterid;
    private String amount;
    private String cancelled;
    private String type;
    private String accregion;

    public String getAccregion() {
        return accregion;
    }

    public void setAccregion(String accregion) {
        this.accregion = accregion;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getDeductionmasterid() {
        return deductionmasterid;
    }

    public void setDeductionmasterid(String deductionmasterid) {
        this.deductionmasterid = deductionmasterid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplementarypayrollprocessingdetailsId() {
        return supplementarypayrollprocessingdetailsId;
    }

    public void setSupplementarypayrollprocessingdetailsId(String supplementarypayrollprocessingdetailsId) {
        this.supplementarypayrollprocessingdetailsId = supplementarypayrollprocessingdetailsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
