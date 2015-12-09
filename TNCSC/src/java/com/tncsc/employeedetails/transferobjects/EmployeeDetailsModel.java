/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tncsc.employeedetails.transferobjects;

import com.onward.persistence.payroll.Employeemaster;
import com.tncscpayroll.transferobjects.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement(namespace = "employeedetailsmodel")
public class EmployeeDetailsModel {

    private List<PayrollprocessingdetailsPost> payrollprocessingdetailsPostList;
    private List<SupplementatypaybillPost> supplementatypaybillPostList;
    private List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostList;
    private List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostModiList;
    private String region;
    private String processcount;
    private String suppcount;
    private String newloans;
    private String modiloans;
    private String epfno;
    private String salarystructureid;
    private String salarystructureperiodfrom;
    private String salarystructureperiodto;
    private String salarystructureamount;
    private String salarystructurepercentage;
    private String salarystructureispercentage;
    private String salarystructureorderno;
    private String salarystructuresynchronized_;
    private String salarystructureaccregion;
    private List<EmployeeDeductionDetailsPost> employeeDeductionDetailsPostList;
    private List<EmployeeEarningDetailsPost> employeeEarningDetailsPostList;
    private List<EmployeePayCodeAccountDetailsPost> employeePayCodeAccountDetailsPostList;
    private String fpfno;
    private String employeename;
    private String fathername;
    private String gender;
    private String dateofbirth;
    private String dateofappoinment;
    private String dateofprobation;
    private String dateofconfirmation;
    private String section;
    private String designation;
    private String createdby;
    private String createddate;
    private String empSta;
    private String paymentmode;
    private String banksbaccount;
    private String bankcode;
    private String pancardno;
    private String community;
    private String eslp;
    private String employeecode;
    private String process;
    private String synchronized_;
    private String category;
    private String subsection;

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getBanksbaccount() {
        return banksbaccount;
    }

    public void setBanksbaccount(String banksbaccount) {
        this.banksbaccount = banksbaccount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getDateofappoinment() {
        return dateofappoinment;
    }

    public void setDateofappoinment(String dateofappoinment) {
        this.dateofappoinment = dateofappoinment;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getDateofconfirmation() {
        return dateofconfirmation;
    }

    public void setDateofconfirmation(String dateofconfirmation) {
        this.dateofconfirmation = dateofconfirmation;
    }

    public String getDateofprobation() {
        return dateofprobation;
    }

    public void setDateofprobation(String dateofprobation) {
        this.dateofprobation = dateofprobation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmpSta() {
        return empSta;
    }

    public void setEmpSta(String empSta) {
        this.empSta = empSta;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getEslp() {
        return eslp;
    }

    public void setEslp(String eslp) {
        this.eslp = eslp;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getFpfno() {
        return fpfno;
    }

    public void setFpfno(String fpfno) {
        this.fpfno = fpfno;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPancardno() {
        return pancardno;
    }

    public void setPancardno(String pancardno) {
        this.pancardno = pancardno;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getSynchronized_() {
        return synchronized_;
    }

    public void setSynchronized_(String synchronized_) {
        this.synchronized_ = synchronized_;
    }

    public List<EmployeeDeductionDetailsPost> getEmployeeDeductionDetailsPostList() {
        return employeeDeductionDetailsPostList;
    }

    public void setEmployeeDeductionDetailsPostList(List<EmployeeDeductionDetailsPost> employeeDeductionDetailsPostList) {
        this.employeeDeductionDetailsPostList = employeeDeductionDetailsPostList;
    }

    public List<EmployeeEarningDetailsPost> getEmployeeEarningDetailsPostList() {
        return employeeEarningDetailsPostList;
    }

    public void setEmployeeEarningDetailsPostList(List<EmployeeEarningDetailsPost> employeeEarningDetailsPostList) {
        this.employeeEarningDetailsPostList = employeeEarningDetailsPostList;
    }

    public List<EmployeePayCodeAccountDetailsPost> getEmployeePayCodeAccountDetailsPostList() {
        return employeePayCodeAccountDetailsPostList;
    }

    public void setEmployeePayCodeAccountDetailsPostList(List<EmployeePayCodeAccountDetailsPost> employeePayCodeAccountDetailsPostList) {
        this.employeePayCodeAccountDetailsPostList = employeePayCodeAccountDetailsPostList;
    }

    public String getSalarystructureaccregion() {
        return salarystructureaccregion;
    }

    public void setSalarystructureaccregion(String salarystructureaccregion) {
        this.salarystructureaccregion = salarystructureaccregion;
    }

    public String getSalarystructureamount() {
        return salarystructureamount;
    }

    public void setSalarystructureamount(String salarystructureamount) {
        this.salarystructureamount = salarystructureamount;
    }

    public String getSalarystructureid() {
        return salarystructureid;
    }

    public void setSalarystructureid(String salarystructureid) {
        this.salarystructureid = salarystructureid;
    }

    public String getSalarystructureispercentage() {
        return salarystructureispercentage;
    }

    public void setSalarystructureispercentage(String salarystructureispercentage) {
        this.salarystructureispercentage = salarystructureispercentage;
    }

    public String getSalarystructureorderno() {
        return salarystructureorderno;
    }

    public void setSalarystructureorderno(String salarystructureorderno) {
        this.salarystructureorderno = salarystructureorderno;
    }

    public String getSalarystructurepercentage() {
        return salarystructurepercentage;
    }

    public void setSalarystructurepercentage(String salarystructurepercentage) {
        this.salarystructurepercentage = salarystructurepercentage;
    }

    public String getSalarystructureperiodfrom() {
        return salarystructureperiodfrom;
    }

    public void setSalarystructureperiodfrom(String salarystructureperiodfrom) {
        this.salarystructureperiodfrom = salarystructureperiodfrom;
    }

    public String getSalarystructureperiodto() {
        return salarystructureperiodto;
    }

    public void setSalarystructureperiodto(String salarystructureperiodto) {
        this.salarystructureperiodto = salarystructureperiodto;
    }

    public String getSalarystructuresynchronized_() {
        return salarystructuresynchronized_;
    }

    public void setSalarystructuresynchronized_(String salarystructuresynchronized_) {
        this.salarystructuresynchronized_ = salarystructuresynchronized_;
    }

    public String getEpfno() {
        return epfno;
    }

    public void setEpfno(String epfno) {
        this.epfno = epfno;
    }

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
}
