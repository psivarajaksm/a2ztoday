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
public interface SupplementaryBillService {

    public Map loadEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);

    public Map saveSupplementaryBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String orderno, String billno);

    public Map saveLeaveSurrenderBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String surrenderdays, String orderno, String funtype, String surrenderdate, String designationname);

    public Map saveIncrementArrearBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String orderno, String startmonth, String startyear, String endmonth, String endyear, String earningCode, String earningAmount, String designationname);

    public Map getIncrementArrearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String billno, String[] earcode);

    public Map displayAddedSupplementaryDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String noofdays, String isnewmap);

    public Map deleteSupplementaryBillData(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String mapId);

    public Map modifySupplementaryBillDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype);

    public Map modifyIncrementArrears(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype);

    public Map ModifyIncrementArrearBill(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String orderno, String startmonth, String startyear, String endmonth, String endyear, String billno);

    public Map modifySurrenderLeaveDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype);

    public Map displaySupplementaryBillsData(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String billid);

    public Map displayAddModifyDataDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String mapId, String month, String year, String noofdays);

    public Map EmployeeSupplementaryBillPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String billtype, String filePath);

    public Map displaySupplementaryAbstractDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String billdate, String billtype);

    public Map EmployeeSupplementaryAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String[] billnumbers, String asondate, String billtype, String filePath);

    public Map getEmployeeRegionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map EmployeeSupplementaryEPFformPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeSupplementaryEPFformDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map getSupplementaryEmployeeDeductionList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser);

    public Map EmployeeSupplementaryDeductionAllPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String deductionid, String year, String month, String reporttype, String filePath);

    public Map deleteSurrenderLeaveDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String Billtype);

    public Map EmployeeSupplementaryLICSchedulePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map EmployeeSupplementaryPayDBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String year, String month, String filePath);

    public Map getEmployeeEarningsforIA(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno);

    public Map getIncrementEarnings(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String employeeProvidentFundNumber);

    public Map getDueDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String supsalstrucidid);

    public Map updateincrementarreardue(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String supsalstrucidid, String earningCode, String earningAmount);

    public Map getIncrementEarningsUpdation(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String asondate, String designationname);

    public Map updateincrementarrearmanual(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String supsalstrucidid, String earningCode, String earningAmount);

    public Map getManIncDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String processid);

    public Map saveIncrementArrearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate);

    public Map PrintIncrementArrearDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String asondate, String filePathwithName);

    public Map EmployeeSupplementaryDABillPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String epfno, String filePathwithName);

    public Map EmployeeSupplementaryDAAcquitanceCashPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String atype, String filePathwithName);

    public Map EmployeeSupplementaryDAAcquitanceChequePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String chequeno, String filePathwithName);

    public Map EmployeeSupplementaryIncrementArrearAcquitanceDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePathwithName);

    public Map getSupplementaryPaidDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String startingdate, String enddate, String supplementarytype, String epfno);

    public Map employeeSupplementaryAcquaintancePrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String billtype, String filePath);

    public Map employeeSupplementaryEarningsLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePath);

    public Map employeeSupplementaryDeductionLedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePath);
            
    public Map EmployeeSupplementaryIncrementArrearAbstractDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String asondate, String filePathwithName);

    public Map EmployeeSupplementaryDADBFPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String filePathwithName);

    public Map EmployeeSupplementaryDAAbstractPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String paymentmode, String amonth, String ayear, String bmonth, String byear, String filePathwithName);

    public Map EmployeeSupplementaryDALedgerPrintOut(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String dabatch, String month, String year, String filePathwithName);
}
