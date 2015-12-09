/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.dao;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(name = "budgetdetailsobjectmodel")
public class BudgetDetailsObjectModel {

    private String id;
    private String budget;
    private String ledgermaster;
    private String regionmaster;
    private String budgetestimate;
    private String revisedbudgetestimate;
    private String actualoffirsthalfyesr;
    private String actualofsecondhalfyesr;
    private String actual;
    private String probableforsecondhalfyear;
    private String hobudget;
    private String horevisedbudget;

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getActualoffirsthalfyesr() {
        return actualoffirsthalfyesr;
    }

    public void setActualoffirsthalfyesr(String actualoffirsthalfyesr) {
        this.actualoffirsthalfyesr = actualoffirsthalfyesr;
    }

    public String getActualofsecondhalfyesr() {
        return actualofsecondhalfyesr;
    }

    public void setActualofsecondhalfyesr(String actualofsecondhalfyesr) {
        this.actualofsecondhalfyesr = actualofsecondhalfyesr;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBudgetestimate() {
        return budgetestimate;
    }

    public void setBudgetestimate(String budgetestimate) {
        this.budgetestimate = budgetestimate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLedgermaster() {
        return ledgermaster;
    }

    public void setLedgermaster(String ledgermaster) {
        this.ledgermaster = ledgermaster;
    }

    public String getProbableforsecondhalfyear() {
        return probableforsecondhalfyear;
    }

    public void setProbableforsecondhalfyear(String probableforsecondhalfyear) {
        this.probableforsecondhalfyear = probableforsecondhalfyear;
    }

    public String getRegionmaster() {
        return regionmaster;
    }

    public void setRegionmaster(String regionmaster) {
        this.regionmaster = regionmaster;
    }

    public String getRevisedbudgetestimate() {
        return revisedbudgetestimate;
    }

    public void setRevisedbudgetestimate(String revisedbudgetestimate) {
        this.revisedbudgetestimate = revisedbudgetestimate;
    }

    /**
     * @return the hobudget
     */
    public String getHobudget() {
        return hobudget;
    }
   
    /**
     * @param hobudget the hobudget to set
     */
    public void setHobudget(String hobudget) {
        this.hobudget = hobudget;
}

    /**
     * @return the horevisedbudget
     */
    public String getHorevisedbudget() {
        return horevisedbudget;
    }

    /**
     * @param horevisedbudget the horevisedbudget to set
     */
    public void setHorevisedbudget(String horevisedbudget) {
        this.horevisedbudget = horevisedbudget;
    }


}
