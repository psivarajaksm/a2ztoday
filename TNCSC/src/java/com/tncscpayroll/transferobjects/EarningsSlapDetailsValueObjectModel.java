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

@XmlRootElement(namespace = "EarningsSlapDetailsValueObjectModel")
public class EarningsSlapDetailsValueObjectModel {

    private String id;
    private String earningcode;
    private String periodfrom;
    private String periodto;
    private String amount;
    private String orderno;
    private String amountrangefrom;
    private String amountrangeto;
    private String percentage;
    private String synchronized1;
    private String cancelled;

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
     * @return the earningcode
     */
    public String getEarningcode() {
        return earningcode;
    }

    /**
     * @param earningcode the earningcode to set
     */
    public void setEarningcode(String earningcode) {
        this.earningcode = earningcode;
    }

    /**
     * @return the periodfrom
     */
    public String getPeriodfrom() {
        return periodfrom;
    }

    /**
     * @param periodfrom the periodfrom to set
     */
    public void setPeriodfrom(String periodfrom) {
        this.periodfrom = periodfrom;
    }

    /**
     * @return the periodto
     */
    public String getPeriodto() {
        return periodto;
    }

    /**
     * @param periodto the periodto to set
     */
    public void setPeriodto(String periodto) {
        this.periodto = periodto;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
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

    /**
     * @return the amountrangefrom
     */
    public String getAmountrangefrom() {
        return amountrangefrom;
    }

    /**
     * @param amountrangefrom the amountrangefrom to set
     */
    public void setAmountrangefrom(String amountrangefrom) {
        this.amountrangefrom = amountrangefrom;
    }

    /**
     * @return the amountrangeto
     */
    public String getAmountrangeto() {
        return amountrangeto;
    }

    /**
     * @param amountrangeto the amountrangeto to set
     */
    public void setAmountrangeto(String amountrangeto) {
        this.amountrangeto = amountrangeto;
    }

    /**
     * @return the percentage
     */
    public String getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the synchronized1
     */
    public String getSynchronized1() {
        return synchronized1;
    }

    /**
     * @param synchronized1 the synchronized1 to set
     */
    public void setSynchronized1(String synchronized1) {
        this.synchronized1 = synchronized1;
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
}
