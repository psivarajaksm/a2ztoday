/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import com.onward.persistence.payroll.Supplementarypayrollprocessingdetails;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "supplementaryemployeeearningstransactionspost")
public class SupplementaryemployeeearningstransactionsPost {

    private String id;
    private String supplementarypayrollprocessingdetailsId;
    private String earningmasterid;
    private String amount;
    private String cancelled;
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

    public String getEarningmasterid() {
        return earningmasterid;
    }

    public void setEarningmasterid(String earningmasterid) {
        this.earningmasterid = earningmasterid;
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
}
