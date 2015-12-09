/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.budget.dao;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "budgetdetailobject")
public class BudgetDetailObject {
    private List<BudgetDetailsObjectModel> BudgetDetailsObjectModelList;
    private String id;
    private String region;
    private String startyear;
    private String endyear;

    public String getEndyear() {
        return endyear;
    }

    public void setEndyear(String endyear) {
        this.endyear = endyear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStartyear() {
        return startyear;
    }

    public void setStartyear(String startyear) {
        this.startyear = startyear;
    }
   
    

    public List<BudgetDetailsObjectModel> getBudgetDetailsObjectModelList() {
        return BudgetDetailsObjectModelList;
    }

    public void setBudgetDetailsObjectModelList(List<BudgetDetailsObjectModel> BudgetDetailsObjectModelList) {
        this.BudgetDetailsObjectModelList = BudgetDetailsObjectModelList;
    }
    
}
