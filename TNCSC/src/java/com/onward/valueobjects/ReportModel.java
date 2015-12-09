/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.valueobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author root
 */
public class ReportModel {

    private String ledgername = "";
    private String firstactual = "";
    private String secondactual = "";
    private String thirdactual = "";
    private String average = "";
    private String budgetestimate = "";
    private String actualfirsthalf = "";
    private String probablesecondhalf = "";
    private String revisedestimate = "";
    private String budgetestimatenext = "";
    private String horevisedestimate = "";
    private String hobudgetestimate = "";
    private String ledgergroupname = "";
    private String firststartyear = "";
    private String firstendyear = "";
    private String secondstartyear = "";
    private String secondendyear = "";
    private String thirdstartyear = "";
    private String thirdendyear = "";
    private int slipno = 0;
    private int startyear = 0;
    private int endyear = 0;
    private String regionname = "";
    private String ledgerid = "";
    private String acccode = "";
    private String accountingperiod = "";
    private String actual = "";
    private String currentbudget = "";
    private String estimatedbudget = "";
    private String budgetbalance = "";
    private String budgetmonthandyear = "";
    private String budgetperiod = "";
    private String uptothebudget = "";
    private String excessexpense = "";
    private String fma = "";    
    private String hofma = "";

    public String getLedgername() {
        return ledgername;
    }

    public void setLedgername(String ledgername) {
        this.ledgername = ledgername;
    }

    public String getFirstactual() {
        return firstactual;
    }

    public void setFirstactual(String firstactual) {
        this.firstactual = firstactual;
    }

    public String getSecondactual() {
        return secondactual;
    }

    public void setSecondactual(String secondactual) {
        this.secondactual = secondactual;
    }

    public String getThirdactual() {
        return thirdactual;
    }

    public void setThirdactual(String thirdactual) {
        this.thirdactual = thirdactual;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getBudgetestimate() {
        return budgetestimate;
    }

    public void setBudgetestimate(String budgetestimate) {
        this.budgetestimate = budgetestimate;
    }

    public String getActualfirsthalf() {
        return actualfirsthalf;
    }

    public void setActualfirsthalf(String actualfirsthalf) {
        this.actualfirsthalf = actualfirsthalf;
    }

    public String getProbablesecondhalf() {
        return probablesecondhalf;
    }

    public void setProbablesecondhalf(String probablesecondhalf) {
        this.probablesecondhalf = probablesecondhalf;
    }

    public String getRevisedestimate() {
        return revisedestimate;
    }

    public void setRevisedestimate(String revisedestimate) {
        this.revisedestimate = revisedestimate;
    }

    public String getBudgetestimatenext() {
        return budgetestimatenext;
    }

    public void setBudgetestimatenext(String budgetestimatenext) {
        this.budgetestimatenext = budgetestimatenext;
    }

    public String getHorevisedestimate() {
        return horevisedestimate;
    }

    public void setHorevisedestimate(String horevisedestimate) {
        this.horevisedestimate = horevisedestimate;
    }

    public String getHobudgetestimate() {
        return hobudgetestimate;
    }

    public void setHobudgetestimate(String hobudgetestimate) {
        this.hobudgetestimate = hobudgetestimate;
    }

    public String getLedgergroupname() {
        return ledgergroupname;
    }

    public void setLedgergroupname(String ledgergroupname) {
        this.ledgergroupname = ledgergroupname;
    }

    public String getFirststartyear() {
        return firststartyear;
    }

    public void setFirststartyear(String firststartyear) {
        this.firststartyear = firststartyear;
    }

    public String getFirstendyear() {
        return firstendyear;
    }

    public void setFirstendyear(String firstendyear) {
        this.firstendyear = firstendyear;
    }

    public String getSecondstartyear() {
        return secondstartyear;
    }

    public void setSecondstartyear(String secondstartyear) {
        this.secondstartyear = secondstartyear;
    }

    public String getSecondendyear() {
        return secondendyear;
    }

    public void setSecondendyear(String secondendyear) {
        this.secondendyear = secondendyear;
    }

    public String getThirdstartyear() {
        return thirdstartyear;
    }

    public void setThirdstartyear(String thirdstartyear) {
        this.thirdstartyear = thirdstartyear;
    }

    public String getThirdendyear() {
        return thirdendyear;
    }

    public void setThirdendyear(String thirdendyear) {
        this.thirdendyear = thirdendyear;
    }

    public int getSlipno() {
        return slipno;
    }

    public void setSlipno(int slipno) {
        this.slipno = slipno;
    }

    public int getStartyear() {
        return startyear;
    }

    public void setStartyear(int startyear) {
        this.startyear = startyear;
    }

    public int getEndyear() {
        return endyear;
    }

    public void setEndyear(int endyear) {
        this.endyear = endyear;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public String getLedgerid() {
        return ledgerid;
    }

    public void setLedgerid(String ledgerid) {
        this.ledgerid = ledgerid;
    }

    public String getAcccode() {
        return acccode;
    }

    public void setAcccode(String acccode) {
        this.acccode = acccode;
    }

    public String getAccountingperiod() {
        return accountingperiod;
    }

    public void setAccountingperiod(String accountingperiod) {
        this.accountingperiod = accountingperiod;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getCurrentbudget() {
        return currentbudget;
    }

    public void setCurrentbudget(String currentbudget) {
        this.currentbudget = currentbudget;
    }

    public String getEstimatedbudget() {
        return estimatedbudget;
    }

    public void setEstimatedbudget(String estimatedbudget) {
        this.estimatedbudget = estimatedbudget;
    }

    public String getBudgetbalance() {
        return budgetbalance;
    }

    public void setBudgetbalance(String budgetbalance) {
        this.budgetbalance = budgetbalance;
    }

    public String getBudgetmonthandyear() {
        return budgetmonthandyear;
    }

    public void setBudgetmonthandyear(String budgetmonthandyear) {
        this.budgetmonthandyear = budgetmonthandyear;
    }

    public String getBudgetperiod() {
        return budgetperiod;
    }

    public void setBudgetperiod(String budgetperiod) {
        this.budgetperiod = budgetperiod;
    }

    public String getUptothebudget() {
        return uptothebudget;
    }

    public void setUptothebudget(String uptothebudget) {
        this.uptothebudget = uptothebudget;
    }

    public String getExcessexpense() {
        return excessexpense;
    }

    public void setExcessexpense(String excessexpense) {
        this.excessexpense = excessexpense;
    }
    
    public String getFma() {
        return fma;
    }

    public void setFma(String fma) {
        this.fma = fma;
    }

    public String getHofma() {
        return hofma;
    }

    public void setHofma(String hofma) {
        this.hofma = hofma;
    }
}
