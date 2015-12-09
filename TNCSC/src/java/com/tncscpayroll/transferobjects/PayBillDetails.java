/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncscpayroll.transferobjects;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "paybilldetails")
public class PayBillDetails {

    private String month;
    private String year;    
    private List<PayrollprocessingdetailsPost> payrollprocessingdetailsPostList;
    private List<SupplementatypaybillPost> supplementatypaybillPostList;
    private List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostList;
    private List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostModiList;
    private String region;
    private String processcount;
    private String suppcount;
    private String newloans;
    private String modiloans;

    public String getModiloans() {
        return modiloans;
    }

    public void setModiloans(String modiloans) {
        this.modiloans = modiloans;
    }

    public String getNewloans() {
        return newloans;
    }

    public void setNewloans(String newloans) {
        this.newloans = newloans;
    }

    public String getProcesscount() {
        return processcount;
    }

    public void setProcesscount(String processcount) {
        this.processcount = processcount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSuppcount() {
        return suppcount;
    }

    public void setSuppcount(String suppcount) {
        this.suppcount = suppcount;
    }

    public List<EmployeeLoansandAdvancesPost> getEmployeeLoansandAdvancesPostModiList() {
        return employeeLoansandAdvancesPostModiList;
    }

    public void setEmployeeLoansandAdvancesPostModiList(List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostModiList) {
        this.employeeLoansandAdvancesPostModiList = employeeLoansandAdvancesPostModiList;
    }

    public List<EmployeeLoansandAdvancesPost> getEmployeeLoansandAdvancesPostList() {
        return employeeLoansandAdvancesPostList;
    }

    public void setEmployeeLoansandAdvancesPostList(List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostList) {
        this.employeeLoansandAdvancesPostList = employeeLoansandAdvancesPostList;
    }
  

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<PayrollprocessingdetailsPost> getPayrollprocessingdetailsPostList() {
        return payrollprocessingdetailsPostList;
    }

    public void setPayrollprocessingdetailsPostList(List<PayrollprocessingdetailsPost> payrollprocessingdetailsPostList) {
        this.payrollprocessingdetailsPostList = payrollprocessingdetailsPostList;
    }

    public List<SupplementatypaybillPost> getSupplementatypaybillPostList() {
        return supplementatypaybillPostList;
    }

    public void setSupplementatypaybillPostList(List<SupplementatypaybillPost> supplementatypaybillPostList) {
        this.supplementatypaybillPostList = supplementatypaybillPostList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
