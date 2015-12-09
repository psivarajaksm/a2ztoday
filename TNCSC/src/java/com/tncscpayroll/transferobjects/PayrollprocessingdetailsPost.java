/* * To change this template, choose Tools | Templates * and open the template in the editor. */package com.tncscpayroll.transferobjects;import java.util.Date;import java.util.List;import javax.xml.bind.annotation.XmlRootElement;/** * * @author Sivaraja.P */@XmlRootElement(namespace = "payrollprocessingdetailspost")public class PayrollprocessingdetailsPost {    private String id;    private String startdate;    private String enddate;    private String workingday;    private String workedday;    private String leaveeligible;    private String leaveavailed;    private String process;    private String processedregular;    private String year;    private String month;    private String salarystructureid;    private String payrollprocessingid;    private Boolean synchronized1;    private List<EmployeeearningstransactionsPost> employeeearningstransactionsPostList;    private List<EmployeedeductionstransactionsPost> employeedeductionstransactionsPostList;    private List<EmployeeloansandadvancesdetailsPost> employeeloansandadvancesdetailsPostList;    private String region;    private String employeemasterId;    private String employeecategory;    private String designation;    public String getDesignation() {        return designation;    }    public void setDesignation(String designation) {        this.designation = designation;    }        public String getEmployeecategory() {        return employeecategory;    }    public void setEmployeecategory(String employeecategory) {        this.employeecategory = employeecategory;    }    public String getEmployeemasterId() {        return employeemasterId;    }    public void setEmployeemasterId(String employeemasterId) {        this.employeemasterId = employeemasterId;    }    public String getRegion() {        return region;    }    public void setRegion(String region) {        this.region = region;    }    public List<EmployeedeductionstransactionsPost> getEmployeedeductionstransactionsPostList() {        return employeedeductionstransactionsPostList;    }    public void setEmployeedeductionstransactionsPostList(List<EmployeedeductionstransactionsPost> employeedeductionstransactionsPostList) {        this.employeedeductionstransactionsPostList = employeedeductionstransactionsPostList;    }    public List<EmployeeearningstransactionsPost> getEmployeeearningstransactionsPostList() {        return employeeearningstransactionsPostList;    }    public void setEmployeeearningstransactionsPostList(List<EmployeeearningstransactionsPost> employeeearningstransactionsPostList) {        this.employeeearningstransactionsPostList = employeeearningstransactionsPostList;    }    public List<EmployeeloansandadvancesdetailsPost> getEmployeeloansandadvancesdetailsPostList() {        return employeeloansandadvancesdetailsPostList;    }    public void setEmployeeloansandadvancesdetailsPostList(List<EmployeeloansandadvancesdetailsPost> employeeloansandadvancesdetailsPostList) {        this.employeeloansandadvancesdetailsPostList = employeeloansandadvancesdetailsPostList;    }    public String getEnddate() {        return enddate;    }    public void setEnddate(String enddate) {        this.enddate = enddate;    }    public String getId() {        return id;    }    public void setId(String id) {        this.id = id;    }    public String getLeaveavailed() {        return leaveavailed;    }    public void setLeaveavailed(String leaveavailed) {        this.leaveavailed = leaveavailed;    }    public String getLeaveeligible() {        return leaveeligible;    }    public void setLeaveeligible(String leaveeligible) {        this.leaveeligible = leaveeligible;    }    public String getMonth() {        return month;    }    public void setMonth(String month) {        this.month = month;    }    public String getPayrollprocessingid() {        return payrollprocessingid;    }    public void setPayrollprocessingid(String payrollprocessingid) {        this.payrollprocessingid = payrollprocessingid;    }    public String getProcess() {        return process;    }    public void setProcess(String process) {        this.process = process;    }    public String getProcessedregular() {        return processedregular;    }    public void setProcessedregular(String processedregular) {        this.processedregular = processedregular;    }    public String getSalarystructureid() {        return salarystructureid;    }    public void setSalarystructureid(String salarystructureid) {        this.salarystructureid = salarystructureid;    }    public String getStartdate() {        return startdate;    }    public void setStartdate(String startdate) {        this.startdate = startdate;    }    public Boolean getSynchronized1() {        return synchronized1;    }    public void setSynchronized1(Boolean synchronized1) {        this.synchronized1 = synchronized1;    }    public String getWorkedday() {        return workedday;    }    public void setWorkedday(String workedday) {        this.workedday = workedday;    }    public String getWorkingday() {        return workingday;    }    public void setWorkingday(String workingday) {        this.workingday = workingday;    }    public String getYear() {        return year;    }    public void setYear(String year) {        this.year = year;    }    }