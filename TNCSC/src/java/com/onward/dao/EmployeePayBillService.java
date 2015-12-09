/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author root
 */
public interface EmployeePayBillService {

    public Map getEmployeeEarningsandDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber);

    public Map getEmployeeEarningsAndDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber);

    public Map getEmptyEarningsAndDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map getEmployeeEarningsAndDeductionsInc(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber, String asondate);

    public Map getEmployeeDeduction(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber, String dateofeffect, String ordernumber);

    public Map getEmployeeListForAttendance(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String section, String epfno);

    public Map getEmployeeListForLLP(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String region, String section, String epfno, String attendancemonth);

    public Map getEmployeeMiscDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String deductioncode);

    public Map getEmployeePreviousMiscDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String deductioncode);

    public Map saveEmployeeListForAttendance(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String epfno, String name, String present, String woff, String eldays, String mldays, String cldays, String ndays, String susdays, String others, String llp, String ulep, String totdays, String curRec, String totRec);

    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map saveEmployeeSalaryStructure(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String type, String employeeid, String salarystructureid, String orderno, String salarystruceffeftfrom, String code, String amt, String actcode, String curRec, String totRec);

    public Map saveEmployeeLLP(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String code, String llpdays, String curRec, String totRec);

    public Map saveEmployeeMiscDeductions(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String deductioncode, String code, String llpdays, String curRec, String totRec);

    public Map loadDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionType);

    public Map EmployeePayBillPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String sectionname, String year, String month, String filePath, String paymenttype);

    public Map EmployeeAcquitanceSlipPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype);

    public Map EmployeeEarningsLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeDeductionLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeSalaryAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype);

    public Map EmployeeEPFformPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeEPFformDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeLICSchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String reporttype);

    public Map EmployeeHBASchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeHBAScheduleConsolidatedPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeHBAInterestSchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeVehicleAdvancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeePLISchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeBankFloppyPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype);

    public Map EmployeeBankTextPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype);

    public Map EmployeeBankTextChequePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath, String paymenttype, String chequeno);

    public Map getEmployeeDeductionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map EmployeeDeductionAllPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionid, String year, String month, String reporttype, String filePath);

    public Map EmployeePayBillDownloandFileCreation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String filePathwithName);

    public Map EmployeeFestivalAdvancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String type, String filePathwithName);

    public Map EmployeeThrftSocietyPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeStorageLossPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String grouptype, String filePath);

    public Map employeeDBFPrintout(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePath, String reportType, String empcategory);

    public Map saveEmployeeBonusdetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String attendancemonth, String bonustype, String code, String earningAmount, String deductionAmount, String curRec, String totRec);

    public Map getEmployeePayStructureDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);

    public Map getEmployeePayStructureDetailsEarningDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);

    public Map getEmployeePayStructureDetailsDeductionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);

    public String getEmployeeEarningDetailsById(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeearningsdetailsactualid);

    public String getEmployeeDeductionDetailsById(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeedeductiondetailsactualid);

    public Map updateEmployeeearningsdetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String employeeearningsdetailsactualid, String earningcodemodify, String earningamountmodify);

    public Map updateEmployeedeductiondetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String employeedeductiondetailsactualid, String deductioncodemodify, String deductionamountmodify,String deductionaccountmodify);

    public Map saveEmployeeearningsdetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String salarystructureid, String earningnameadd, String earningamountadd);

    public Map saveEmployeedeductiondetailsactual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String salarystructureid, String deductionnameadd, String deductionamountadd, String deductionaccountadd);
}
