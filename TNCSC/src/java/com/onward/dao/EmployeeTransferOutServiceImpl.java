/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.DateParser;
import com.onward.persistence.payroll.*;
import com.onward.valueobjects.PaySlip_Earn_Deduction_Model;
import com.tncsc.employeedetails.transferobjects.EmployeeDeductionDetailsPost;
import com.tncsc.employeedetails.transferobjects.EmployeeDetailsModel;
import com.tncsc.employeedetails.transferobjects.EmployeeEarningDetailsPost;
import com.tncsc.employeedetails.transferobjects.EmployeePayCodeAccountDetailsPost;
import com.tncscpayroll.transferobjects.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.struts.upload.FormFile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class EmployeeTransferOutServiceImpl extends OnwardAction implements EmployeeTransferOutService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        Map sectionMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        sectionMap.put("0", "--Select--");
        String regionid = "";
        String regionname = "";

        try {
            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction(" region  in ('ALL','" + LoggedInRegion + "' )"));
            secCrit.addOrder(Order.asc("sectionname"));
            List<Sectionmaster> secList = secCrit.list();
            resultMap = new TreeMap();
            for (Sectionmaster lbobj : secList) {
                sectionMap.put(lbobj.getId(), lbobj.getSectionname());
            }
            resultMap.put("sectionlist", sectionMap);
            
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.like("id", "R%"));
            rgnCrit.addOrder(Order.asc("id"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {
                regionid = lbobj.getId();
                regionname = lbobj.getRegionname();
                regionMap.put(regionid, regionname);
            }
            resultMap.put("regionlist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("currentregion", empmasterObj.getRegion());
            resultMap.put("section", getSection(session, empmasterObj.getSection()).getSectionname());
//            if (!empmasterObj.getRegion().equalsIgnoreCase("TRANS")) {
//                resultMap.put("ERROR", "Stop the pay roll process and download");
//            } else {
//                if (empmasterObj.getRegion().equalsIgnoreCase("TRANS")) {
//                    resultMap.put("currentregion", "Transferred");
//                    resultMap.put("employeename", empmasterObj.getEmployeename());
//                } else {
//                    resultMap.put("employeename", empmasterObj.getEmployeename());
//                    resultMap.put("currentregioncode", empmasterObj.getRegion());
//                    Criteria rgnCrit = session.createCriteria(Regionmaster.class);
//                    rgnCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getRegion() + "'"));
//                    List<Regionmaster> rgnList = rgnCrit.list();
//                    if (rgnList.size() > 0) {
//                        Regionmaster lbobj = rgnList.get(0);
//                        resultMap.put("currentregion", lbobj.getRegionname());
//                    }
//                }
//            }

        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");

        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadTransferEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='TRANS'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("designation", getDesignationMater(session, empmasterObj.getDesignation()).getDesignation());
            resultMap.put("section", getSection(session, empmasterObj.getSection()).getId());
//            resultMap.put("currentregion", empmasterObj.getRegion());

        } else {
            resultMap.put("ERROR", "Given EPF Number is Not Transferred. ");

        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeTransfer(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String regioncode) {
        Map resultMap = new HashMap();
        String updateEmployeeTransfer = "";
        String updateEmployeeUserMaster = "";
        String updateEmployeeUserRights = "";
        try {
            Transaction transaction = session.beginTransaction();
            Employeemaster empObj = getEmployeemaster(session, epfno, LoggedInRegion);
            if (empObj != null) {

                updateEmployeeTransfer = " UPDATE employeetransferhistory SET synchronized='false',enddate='" + getCurrentDate() + "' where   epfno='" + epfno + "' and enddate is null";
                session.createSQLQuery(updateEmployeeTransfer).executeUpdate();

//            updateEmployeeTransfer = " UPDATE employeemaster SET synchronized='false',region='" + regioncode + "' where   epfno='" + epfno + "'";
                //CR - for DEPUTATION(S13) region to be change as region='DEPUT' - 24feb2015
                if("S13".equalsIgnoreCase(empObj.getSection())){
                    updateEmployeeTransfer = " UPDATE employeemaster SET process='FALSE',region='DEPUT' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    updateEmployeeUserMaster = " UPDATE usermaster SET region='DEPUT' where   userid='" + epfno + "' and region='" + LoggedInRegion + "'";
                }else{
                    updateEmployeeTransfer = " UPDATE employeemaster SET process='FALSE',region='TRANS' where   epfno='" + epfno + "' and region='" + LoggedInRegion + "'";
                    updateEmployeeUserMaster = " UPDATE usermaster SET region='TRANS' where   userid='" + epfno + "' and region='" + LoggedInRegion + "'";
                }
                session.createSQLQuery(updateEmployeeTransfer).executeUpdate();
                
                session.createSQLQuery(updateEmployeeUserMaster).executeUpdate();

                updateEmployeeUserRights = " UPDATE useroperatingrights SET accessright='false' where   userid='" + epfno + "'";
                session.createSQLQuery(updateEmployeeUserRights).executeUpdate();

                Employeetransferhistory employeetransferhistoryobj = new Employeetransferhistory();


                employeetransferhistoryobj.setId(getmaxofEmployeeTransfer(session));
                employeetransferhistoryobj.setEmployeemaster(empObj);
                employeetransferhistoryobj.setStartdate(getCurrentDate());
                employeetransferhistoryobj.setRegioncode(regioncode);
                employeetransferhistoryobj.setAccregion(LoggedInRegion);
//            employeetransferhistoryobj.setCreatedby(LoggedInUser);
//            employeetransferhistoryobj.setCreateddate(getCurrentDate());
                session.save(employeetransferhistoryobj);
                transaction.commit();

                if("S13".equalsIgnoreCase(empObj.getSection())){  //DEPUTATION
                    resultMap.put("success", "Mr/Mrs." + empObj.getEmployeename() + " is transferred ");
                }else{
                    resultMap.put("success", "Mr/Mrs." + empObj.getEmployeename() + " is transferred to " + getRegionmaster(session, regioncode).getRegionname() + " Region.");
                }
            }else{
                resultMap.put("ERROR", "This " + epfno + " EPF No is not in this Region.");
            }


        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", " Transaction Faild");
        }
        return resultMap;
    }
    
    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveTransferedEmployee(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String section) {
        Map resultMap = new HashMap();
        String updateEmployeeTransfer = "";
        String updateEmployeeLoansandadvances = "";
        String updateEmployeeUserMaster = "";
        
        try {
            Transaction transaction = session.beginTransaction();
            Employeemaster empObj = getEmployeemaster(session, epfno);
            if (empObj != null) {
                

                updateEmployeeTransfer = " UPDATE employeemaster SET process='true',region='"+LoggedInRegion+"',section='"+section+"' where   epfno='" + epfno + "' and region='TRANS'";
                session.createSQLQuery(updateEmployeeTransfer).executeUpdate();
                // CR - for loansandadvances also region to be update - 24feb2015
                updateEmployeeLoansandadvances = " UPDATE employeeloansandadvances SET accregion='"+LoggedInRegion+"' where loanbalance!=0 and employeeprovidentfundnumber='" + epfno + "' ";
                session.createSQLQuery(updateEmployeeLoansandadvances).executeUpdate();
                
                updateEmployeeUserMaster = " UPDATE usermaster SET region='"+LoggedInRegion+"' where   userid='" + epfno + "' and region='TRANS'";
                session.createSQLQuery(updateEmployeeUserMaster).executeUpdate();
                transaction.commit();

                resultMap.put("success", "Mr/Mrs." + empObj.getEmployeename()+" is Successfully Transfer In");
            }else{
                resultMap.put("ERROR", "This " + epfno + " EPF No is Wrong.");
            }


        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", " Transaction Failed");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map createEmployeeDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filePathwithName, String epfno) {
        Map map = new HashMap();
        try {
            PrintWriter pw = null;
            File file = new File(filePathwithName);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pw.print(getEmployeeDetails(session, epfno));
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public String getEmployeeDetails(Session session, String epfno) {
        String result = "";
        EmployeeDetailsModel employeeDetailsObject = new EmployeeDetailsModel();
        List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostList = new ArrayList<EmployeeLoansandAdvancesPost>();
        List<EmployeeLoansandAdvancesPost> employeeLoansandAdvancesPostModiList = new ArrayList<EmployeeLoansandAdvancesPost>();
        List<PayrollprocessingdetailsPost> payrollprocessingdetailsPostList = new ArrayList<PayrollprocessingdetailsPost>();
        List<SupplementatypaybillPost> supplementatypaybillPostList = new ArrayList<SupplementatypaybillPost>();


        Employeemaster employeeMaster = getEmployeemaster(session, epfno);
        employeeDetailsObject.setEpfno(epfno);
        employeeDetailsObject.setFpfno(employeeMaster.getFpfno());
        employeeDetailsObject.setEmployeename(employeeMaster.getEmployeename());
        employeeDetailsObject.setFathername(employeeMaster.getFathername());
        employeeDetailsObject.setGender(employeeMaster.getGender());
        if (employeeMaster.getDateofbirth() != null) {
            employeeDetailsObject.setDateofbirth(employeeMaster.getDateofbirth().toString());
        }
        if (employeeMaster.getDateofappoinment() != null) {
            employeeDetailsObject.setDateofappoinment(employeeMaster.getDateofappoinment().toString());
        }
        if (employeeMaster.getDateofprobation() != null) {
            employeeDetailsObject.setDateofprobation(employeeMaster.getDateofprobation().toString());
        }
        if (employeeMaster.getDateofconfirmation() != null) {
            employeeDetailsObject.setDateofconfirmation(employeeMaster.getDateofconfirmation().toString());
        }
        employeeDetailsObject.setSection(employeeMaster.getSection());
        employeeDetailsObject.setDesignation(employeeMaster.getDesignation());
        employeeDetailsObject.setEmpSta(employeeMaster.getEmpSta());
        employeeDetailsObject.setPaymentmode(employeeMaster.getPaymentmode());
        employeeDetailsObject.setBanksbaccount(employeeMaster.getBanksbaccount());
        employeeDetailsObject.setBankcode(employeeMaster.getBankcode());
        employeeDetailsObject.setPancardno(employeeMaster.getPancardno());
        employeeDetailsObject.setCommunity(employeeMaster.getCommunity());
        if (employeeMaster.getEslp() != null) {
            employeeDetailsObject.setEslp(employeeMaster.getEslp().toString());
        }
        employeeDetailsObject.setEmployeecode(employeeMaster.getEmployeecode());
        employeeDetailsObject.setCategory(employeeMaster.getCategory());
        employeeDetailsObject.setSubsection(employeeMaster.getSubsection());

        Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvances.class);
        empLoanDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));
        List empLoanDetailsList = empLoanDetailsCrit.list();

        employeeDetailsObject.setNewloans(String.valueOf(empLoanDetailsList.size()));
        if (empLoanDetailsList.size() > 0) {
            for (int i = 0; i < empLoanDetailsList.size(); i++) {
                Employeeloansandadvances employeeloansandadvancesObj = (Employeeloansandadvances) empLoanDetailsList.get(i);


                EmployeeLoansandAdvancesPost employeeLoansandAdvancesPostObj = new EmployeeLoansandAdvancesPost();
                if (employeeloansandadvancesObj.getId() != null) {
                    employeeLoansandAdvancesPostObj.setId(employeeloansandadvancesObj.getId());
                }
                if (employeeloansandadvancesObj.getLoandate() != null) {
                    employeeLoansandAdvancesPostObj.setLoandate(employeeloansandadvancesObj.getLoandate().toString());
                }
                if (employeeloansandadvancesObj.getLoanamount().toString() != null) {
                    employeeLoansandAdvancesPostObj.setLoanamount(employeeloansandadvancesObj.getLoanamount().toString());
                }
                if (employeeloansandadvancesObj.getTotalinstallment() != null) {
                    employeeLoansandAdvancesPostObj.setTotalinstallment(employeeloansandadvancesObj.getTotalinstallment().toString());
                }
                if (employeeloansandadvancesObj.getEmployeemaster().getEpfno() != null) {
                    employeeLoansandAdvancesPostObj.setEpfno(employeeloansandadvancesObj.getEmployeemaster().getEpfno());
                }
                if (employeeloansandadvancesObj.getCurrentinstallment() != null) {
                    employeeLoansandAdvancesPostObj.setCurrentinstallment(employeeloansandadvancesObj.getCurrentinstallment().toString());
                }
                if (employeeloansandadvancesObj.getDeductioncode() != null) {
                    employeeLoansandAdvancesPostObj.setDeductioncode(employeeloansandadvancesObj.getDeductioncode());
                }
                if (employeeloansandadvancesObj.getLoanbalance() != null) {
                    employeeLoansandAdvancesPostObj.setLoanbalance(employeeloansandadvancesObj.getLoanbalance().toString());
                }
                if (employeeloansandadvancesObj.getLoantype() != null) {
                    employeeLoansandAdvancesPostObj.setLoantype(employeeloansandadvancesObj.getLoantype());
                }
                if (employeeloansandadvancesObj.getStatus() != null) {
                    employeeLoansandAdvancesPostObj.setStatus(employeeloansandadvancesObj.getStatus());
                }
                if (employeeloansandadvancesObj.getRegionno() != null) {
                    employeeLoansandAdvancesPostObj.setRegionno(employeeloansandadvancesObj.getRegionno());
                }
                if (employeeloansandadvancesObj.getFileno() != null) {
                    employeeLoansandAdvancesPostObj.setFileno(employeeloansandadvancesObj.getFileno());
                }
                if (employeeloansandadvancesObj.getFirstinstallmentamount() != null) {
                    employeeLoansandAdvancesPostObj.setFirstinstallmentamount(employeeloansandadvancesObj.getFirstinstallmentamount().toString());
                }
                if (employeeloansandadvancesObj.getInstallmentamount() != null) {
                    employeeLoansandAdvancesPostObj.setInstallmentamount(employeeloansandadvancesObj.getInstallmentamount().toString());
                }
                employeeLoansandAdvancesPostObj.setAccregion(employeeloansandadvancesObj.getAccregion());

                employeeLoansandAdvancesPostList.add(employeeLoansandAdvancesPostObj);
            }
        }
        employeeDetailsObject.setEmployeeLoansandAdvancesPostList(employeeLoansandAdvancesPostList);

        //PayrollprocessingdetailsPost
        Criteria empPayProcessDetailsCrit = session.createCriteria(Payrollprocessingdetails.class);
//        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("process is true"));
        empPayProcessDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));

        List empPayProcessDetails = empPayProcessDetailsCrit.list();
        employeeDetailsObject.setProcesscount(String.valueOf(empPayProcessDetails.size()));
        if (empPayProcessDetails.size() > 0) {
            for (int i = 0; i < empPayProcessDetails.size(); i++) {
                Payrollprocessingdetails payrollprocessingdetailsObj = (Payrollprocessingdetails) empPayProcessDetails.get(i);

                PayrollprocessingdetailsPost payrollprocessingdetailsPostObj = new PayrollprocessingdetailsPost();
                payrollprocessingdetailsPostObj.setId(payrollprocessingdetailsObj.getId());
                if (payrollprocessingdetailsObj.getEmployeemaster() != null) {
                    payrollprocessingdetailsPostObj.setEmployeemasterId(payrollprocessingdetailsObj.getEmployeemaster().getEpfno());
                }
                if (payrollprocessingdetailsObj.getEnddate() != null) {
                    payrollprocessingdetailsPostObj.setEnddate(payrollprocessingdetailsObj.getEnddate().toString());
                }
                if (payrollprocessingdetailsObj.getLeaveavailed() != null) {
                    payrollprocessingdetailsPostObj.setLeaveavailed(payrollprocessingdetailsObj.getLeaveavailed().toString());
                }
                if (payrollprocessingdetailsObj.getLeaveeligible() != null) {
                    payrollprocessingdetailsPostObj.setLeaveeligible(payrollprocessingdetailsObj.getLeaveeligible().toString());
                }
                if (payrollprocessingdetailsObj.getMonth() != null) {
                    payrollprocessingdetailsPostObj.setMonth(payrollprocessingdetailsObj.getMonth().toString());
                }
                if (payrollprocessingdetailsObj.getPayrollprocessingid() != null) {
                    payrollprocessingdetailsPostObj.setPayrollprocessingid(payrollprocessingdetailsObj.getPayrollprocessingid().toString());
                }
                if (payrollprocessingdetailsObj.getProcess() != null) {
                    payrollprocessingdetailsPostObj.setProcess(payrollprocessingdetailsObj.getProcess().toString());
                }
                if (payrollprocessingdetailsObj.getProcessedregular() != null) {
                    payrollprocessingdetailsPostObj.setProcessedregular(payrollprocessingdetailsObj.getProcessedregular().toString());
                }
                if (payrollprocessingdetailsObj.getSalarystructureid() != null) {
                    payrollprocessingdetailsPostObj.setSalarystructureid(payrollprocessingdetailsObj.getSalarystructureid().toString());
                }
                if (payrollprocessingdetailsObj.getStartdate() != null) {
                    payrollprocessingdetailsPostObj.setStartdate(payrollprocessingdetailsObj.getStartdate().toString());
                }
                if (payrollprocessingdetailsObj.getWorkedday() != null) {
                    payrollprocessingdetailsPostObj.setWorkedday(payrollprocessingdetailsObj.getWorkedday().toString());
                }
                if (payrollprocessingdetailsObj.getWorkingday() != null) {
                    payrollprocessingdetailsPostObj.setWorkingday(payrollprocessingdetailsObj.getWorkingday().toString());
                }
                if (payrollprocessingdetailsObj.getYear() != null) {
                    payrollprocessingdetailsPostObj.setYear(payrollprocessingdetailsObj.getYear().toString());
                }
                if (payrollprocessingdetailsObj.getAccregion() != null) {
                    payrollprocessingdetailsPostObj.setRegion(payrollprocessingdetailsObj.getAccregion().toString());
                }
                payrollprocessingdetailsPostObj.setEmployeecategory(payrollprocessingdetailsObj.getEmployeecategory());

                Criteria empEarnDetailsCrit = session.createCriteria(Employeeearningstransactions.class);
                empEarnDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                List empEarnDetailsList = empEarnDetailsCrit.list();
                if (empEarnDetailsList.size() > 0) {
                    List<EmployeeearningstransactionsPost> employeeearningstransactionsPostList = new ArrayList<EmployeeearningstransactionsPost>();
                    for (int j = 0; j < empEarnDetailsList.size(); j++) {
                        Employeeearningstransactions employeeearningstransactionsObj = (Employeeearningstransactions) empEarnDetailsList.get(j);


                        EmployeeearningstransactionsPost employeeearningstransactionsPostObj = new EmployeeearningstransactionsPost();
                        employeeearningstransactionsPostObj.setId(employeeearningstransactionsObj.getId());
                        employeeearningstransactionsPostObj.setEarningmasterid(employeeearningstransactionsObj.getEarningmasterid());
                        employeeearningstransactionsPostObj.setPayrollprocessingdetailsId(employeeearningstransactionsObj.getPayrollprocessingdetails().getId());
                        if (employeeearningstransactionsObj.getAmount() != null) {
                            employeeearningstransactionsPostObj.setAmount(employeeearningstransactionsObj.getAmount().toString());
                        }
                        if (employeeearningstransactionsObj.getCancelled() != null) {
                            employeeearningstransactionsPostObj.setCancelled(employeeearningstransactionsObj.getCancelled().toString());
                        }
                        employeeearningstransactionsPostList.add(employeeearningstransactionsPostObj);
                    }
                    payrollprocessingdetailsPostObj.setEmployeeearningstransactionsPostList(employeeearningstransactionsPostList);
                }

                Criteria empDeducDetailsCrit = session.createCriteria(Employeedeductionstransactions.class);
                empDeducDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                List empDeducDetailsList = empDeducDetailsCrit.list();
                if (empDeducDetailsList.size() > 0) {
                    List<EmployeedeductionstransactionsPost> employeedeductionstransactionsPostList = new ArrayList<EmployeedeductionstransactionsPost>();
                    for (int j = 0; j < empDeducDetailsList.size(); j++) {
                        Employeedeductionstransactions employeedeductionstransactionsObj = (Employeedeductionstransactions) empDeducDetailsList.get(j);

                        EmployeedeductionstransactionsPost employeedeductionstransactionsPostObj = new EmployeedeductionstransactionsPost();
                        employeedeductionstransactionsPostObj.setId(employeedeductionstransactionsObj.getId());
                        if (employeedeductionstransactionsObj.getAmount() != null) {
                            employeedeductionstransactionsPostObj.setAmount(employeedeductionstransactionsObj.getAmount().toString());
                        }
                        if (employeedeductionstransactionsObj.getCancelled() != null) {
                            employeedeductionstransactionsPostObj.setCancelled(employeedeductionstransactionsObj.getCancelled().toString());
                        }
                        employeedeductionstransactionsPostObj.setDeductionmasterid(employeedeductionstransactionsObj.getDeductionmasterid());
                        if (employeedeductionstransactionsObj.getPayrollprocessingdetails() != null) {
                            employeedeductionstransactionsPostObj.setPayrollprocessingdetailsId(employeedeductionstransactionsObj.getPayrollprocessingdetails().getId());
                        }
                        if (employeedeductionstransactionsObj.getType() != null) {
                            employeedeductionstransactionsPostObj.setType(employeedeductionstransactionsObj.getType().toString());
                        }

                        employeedeductionstransactionsPostList.add(employeedeductionstransactionsPostObj);
                    }
                    payrollprocessingdetailsPostObj.setEmployeedeductionstransactionsPostList(employeedeductionstransactionsPostList);
                }

                Criteria empLoanDeducDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
                empLoanDeducDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingdetailsObj.getId() + "'"));
                empLoanDeducDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false "));
                List empLoanDeducDetailsList = empLoanDeducDetailsCrit.list();
                if (empLoanDeducDetailsList.size() > 0) {
                    List<EmployeeloansandadvancesdetailsPost> employeeloansandadvancesdetailsPostList = new ArrayList<EmployeeloansandadvancesdetailsPost>();
                    for (int j = 0; j < empLoanDeducDetailsList.size(); j++) {
                        Employeeloansandadvancesdetails employeeloansandadvancesdetailsObj = (Employeeloansandadvancesdetails) empLoanDeducDetailsList.get(j);


                        EmployeeloansandadvancesdetailsPost employeeloansandadvancesdetailsPostObj = new EmployeeloansandadvancesdetailsPost();
                        employeeloansandadvancesdetailsPostObj.setId(employeeloansandadvancesdetailsObj.getId());
                        if (employeeloansandadvancesdetailsObj.getCancelled() != null) {
                            employeeloansandadvancesdetailsPostObj.setCancelled(employeeloansandadvancesdetailsObj.getCancelled().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getInstallmentamount() != null) {
                            employeeloansandadvancesdetailsPostObj.setInstallmentamount(employeeloansandadvancesdetailsObj.getInstallmentamount().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getLoanbalance() != null) {
                            employeeloansandadvancesdetailsPostObj.setLoanbalance(employeeloansandadvancesdetailsObj.getLoanbalance().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getNthinstallment() != null) {
                            employeeloansandadvancesdetailsPostObj.setNthinstallment(employeeloansandadvancesdetailsObj.getNthinstallment().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getPayrollprocessingdetails() != null) {
                            employeeloansandadvancesdetailsPostObj.setPayrollprocessingdetailsId(employeeloansandadvancesdetailsObj.getPayrollprocessingdetails().getId());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances() != null) {
                            employeeloansandadvancesdetailsPostObj.setLoansandadvancesid(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                        }

                        EmployeeLoansandAdvancesPost employeeLoansandAdvancesPostObj = new EmployeeLoansandAdvancesPost();

                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment() != null) {
                            employeeLoansandAdvancesPostObj.setCurrentinstallment(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment().toString());
                        }
                        employeeLoansandAdvancesPostObj.setDeductioncode(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getDeductioncode());
                        employeeLoansandAdvancesPostObj.setEpfno(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getEmployeemaster().getEpfno());
                        employeeLoansandAdvancesPostObj.setFileno(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFileno());
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount() != null) {
                            employeeLoansandAdvancesPostObj.setFirstinstallmentamount(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount().toString());
                        }
                        employeeLoansandAdvancesPostObj.setId(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount() != null) {
                            employeeLoansandAdvancesPostObj.setInstallmentamount(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount() != null) {
                            employeeLoansandAdvancesPostObj.setLoanamount(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance() != null) {
                            employeeLoansandAdvancesPostObj.setLoanbalance(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance().toString());
                        }
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate() != null) {
                            employeeLoansandAdvancesPostObj.setLoandate(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate().toString());
                        }
                        employeeLoansandAdvancesPostObj.setLoantype(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoantype());
                        employeeLoansandAdvancesPostObj.setRegionno(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getRegionno());
                        employeeLoansandAdvancesPostObj.setStatus(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getStatus());
                        if (employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment() != null) {
                            employeeLoansandAdvancesPostObj.setTotalinstallment(employeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment().toString());
                        }

                        employeeLoansandAdvancesPostModiList.add(employeeLoansandAdvancesPostObj);
                        employeeloansandadvancesdetailsPostList.add(employeeloansandadvancesdetailsPostObj);
                    }
                    payrollprocessingdetailsPostObj.setEmployeeloansandadvancesdetailsPostList(employeeloansandadvancesdetailsPostList);
                }

                payrollprocessingdetailsPostList.add(payrollprocessingdetailsPostObj);
            }

        }
        employeeDetailsObject.setPayrollprocessingdetailsPostList(payrollprocessingdetailsPostList);

        //Supplementarybill
        Criteria suppPaybillCrit = session.createCriteria(Supplementatypaybill.class);

        suppPaybillCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "'"));

        List suppPaybillList = suppPaybillCrit.list();
        employeeDetailsObject.setSuppcount(String.valueOf(suppPaybillList.size()));
        if (suppPaybillList.size() > 0) {
            for (int i = 0; i < suppPaybillList.size(); i++) {
                Supplementatypaybill supplementatypaybillObj = (Supplementatypaybill) suppPaybillList.get(i);

                SupplementatypaybillPost supplementatypaybillPostObj = new SupplementatypaybillPost();
                supplementatypaybillPostObj.setId(supplementatypaybillObj.getId());
                supplementatypaybillPostObj.setAccregion(supplementatypaybillObj.getAccregion());
                if (supplementatypaybillObj.getCancelled() != null) {
                    supplementatypaybillPostObj.setCancelled(supplementatypaybillObj.getCancelled().toString());
                }
                if (supplementatypaybillObj.getDate() != null) {
                    supplementatypaybillPostObj.setDate(supplementatypaybillObj.getDate().toString());
                }
                if (supplementatypaybillObj.getEmployeemaster() != null) {
                    supplementatypaybillPostObj.setEmployeemasterId(supplementatypaybillObj.getEmployeemaster().getEpfno());
                }
                if (supplementatypaybillObj.getNoofdays() != null) {
                    supplementatypaybillPostObj.setNoofdays(supplementatypaybillObj.getNoofdays().toString());
                }
                supplementatypaybillPostObj.setEmployeecategory(supplementatypaybillObj.getEmployeecategory());
                supplementatypaybillPostObj.setPaymentmode(supplementatypaybillObj.getPaymentmode());
                supplementatypaybillPostObj.setSection(supplementatypaybillObj.getSection());
                if (supplementatypaybillObj.getSldate() != null) {
                    supplementatypaybillPostObj.setSldate(supplementatypaybillObj.getSldate().toString());
                }
                supplementatypaybillPostObj.setSubsection(supplementatypaybillObj.getSubsection());
                supplementatypaybillPostObj.setType(supplementatypaybillObj.getType());

                Criteria suppPaybillProcessCrit = session.createCriteria(Supplementarypayrollprocessingdetails.class);
                suppPaybillProcessCrit.add(Restrictions.sqlRestriction("supplementatypaybillid='" + supplementatypaybillObj.getId() + "'"));
                List suppPaybillProcessList = suppPaybillProcessCrit.list();
                if (suppPaybillProcessList.size() > 0) {
                    List<SupplementarypayrollprocessingdetailsPost> supplementarypayrollprocessingdetailsPostList = new ArrayList<SupplementarypayrollprocessingdetailsPost>();
                    for (int j = 0; j < suppPaybillProcessList.size(); j++) {
                        Supplementarypayrollprocessingdetails supplementarypayrollprocessingdetailsObj = (Supplementarypayrollprocessingdetails) suppPaybillProcessList.get(j);


                        SupplementarypayrollprocessingdetailsPost supplementarypayrollprocessingdetailsPostObj = new SupplementarypayrollprocessingdetailsPost();
                        supplementarypayrollprocessingdetailsPostObj.setId(supplementarypayrollprocessingdetailsObj.getId());
                        supplementarypayrollprocessingdetailsPostObj.setAccregion(supplementarypayrollprocessingdetailsObj.getAccregion());
                        if (supplementarypayrollprocessingdetailsObj.getCalculatedmonth() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setCalculatedmonth(supplementarypayrollprocessingdetailsObj.getCalculatedmonth().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getCalculatedyear() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setCalculatedyear(supplementarypayrollprocessingdetailsObj.getCalculatedyear().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getCancelled() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setCancelled(supplementarypayrollprocessingdetailsObj.getCancelled().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getNooddayscalculated() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setNooddayscalculated(supplementarypayrollprocessingdetailsObj.getNooddayscalculated().toString());
                        }
                        if (supplementarypayrollprocessingdetailsObj.getSupplementatypaybill() != null) {
                            supplementarypayrollprocessingdetailsPostObj.setSupplementatypaybillId(supplementarypayrollprocessingdetailsObj.getSupplementatypaybill().getId());
                        }

                        Criteria suppEarTranCrit = session.createCriteria(Supplementaryemployeeearningstransactions.class);
                        suppEarTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        List suppEarTranList = suppEarTranCrit.list();
                        if (suppEarTranList.size() > 0) {
                            List<SupplementaryemployeeearningstransactionsPost> SupplementaryemployeeearningstransactionsPostList = new ArrayList<SupplementaryemployeeearningstransactionsPost>();
                            for (int k = 0; k < suppEarTranList.size(); k++) {
                                Supplementaryemployeeearningstransactions supplementaryemployeeearningstransactionsObj = (Supplementaryemployeeearningstransactions) suppEarTranList.get(k);

                                SupplementaryemployeeearningstransactionsPost supplementaryemployeeearningstransactionsPostObj = new SupplementaryemployeeearningstransactionsPost();
                                supplementaryemployeeearningstransactionsPostObj.setId(supplementaryemployeeearningstransactionsObj.getId());
                                supplementaryemployeeearningstransactionsPostObj.setAccregion(supplementaryemployeeearningstransactionsObj.getAccregion());
                                if (supplementaryemployeeearningstransactionsObj.getAmount() != null) {
                                    supplementaryemployeeearningstransactionsPostObj.setAmount(supplementaryemployeeearningstransactionsObj.getAmount().toString());
                                }
                                if (supplementaryemployeeearningstransactionsObj.getCancelled() != null) {
                                    supplementaryemployeeearningstransactionsPostObj.setCancelled(supplementaryemployeeearningstransactionsObj.getCancelled().toString());
                                }
                                supplementaryemployeeearningstransactionsPostObj.setEarningmasterid(supplementaryemployeeearningstransactionsObj.getEarningmasterid());
                                if (supplementaryemployeeearningstransactionsObj.getSupplementarypayrollprocessingdetails() != null) {
                                    supplementaryemployeeearningstransactionsPostObj.setSupplementarypayrollprocessingdetailsId(supplementaryemployeeearningstransactionsObj.getSupplementarypayrollprocessingdetails().getId());
                                }

                                SupplementaryemployeeearningstransactionsPostList.add(supplementaryemployeeearningstransactionsPostObj);
                            }
                            supplementarypayrollprocessingdetailsPostObj.setSupplementaryemployeeearningstransactionsPostList(SupplementaryemployeeearningstransactionsPostList);
                        }


                        Criteria suppDedTranCrit = session.createCriteria(Supplementaryemployeedeductionstransactions.class);
                        suppDedTranCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        List suppDedTranList = suppDedTranCrit.list();
                        if (suppDedTranList.size() > 0) {
                            List<SupplementaryemployeedeductionstransactionsPost> supplementaryemployeedeductionstransactionsPostList = new ArrayList<SupplementaryemployeedeductionstransactionsPost>();
                            for (int k = 0; k < suppDedTranList.size(); k++) {
                                Supplementaryemployeedeductionstransactions supplementaryemployeedeductionstransactionsObj = (Supplementaryemployeedeductionstransactions) suppDedTranList.get(k);

                                SupplementaryemployeedeductionstransactionsPost supplementaryemployeedeductionstransactionsPostObj = new SupplementaryemployeedeductionstransactionsPost();
                                supplementaryemployeedeductionstransactionsPostObj.setId(supplementaryemployeedeductionstransactionsObj.getId());
                                supplementaryemployeedeductionstransactionsPostObj.setAccregion(supplementaryemployeedeductionstransactionsObj.getAccregion());
                                if (supplementaryemployeedeductionstransactionsObj.getAmount() != null) {
                                    supplementaryemployeedeductionstransactionsPostObj.setAmount(supplementaryemployeedeductionstransactionsObj.getAmount().toString());
                                }
                                if (supplementaryemployeedeductionstransactionsObj.getCancelled() != null) {
                                    supplementaryemployeedeductionstransactionsPostObj.setCancelled(supplementaryemployeedeductionstransactionsObj.getCancelled().toString());
                                }
                                supplementaryemployeedeductionstransactionsPostObj.setDeductionmasterid(supplementaryemployeedeductionstransactionsObj.getDeductionmasterid());
                                if (supplementaryemployeedeductionstransactionsObj.getSupplementarypayrollprocessingdetails() != null) {
                                    supplementaryemployeedeductionstransactionsPostObj.setSupplementarypayrollprocessingdetailsId(supplementaryemployeedeductionstransactionsObj.getSupplementarypayrollprocessingdetails().getId());
                                }
                                supplementaryemployeedeductionstransactionsPostObj.setType(supplementaryemployeedeductionstransactionsObj.getType());

                                supplementaryemployeedeductionstransactionsPostList.add(supplementaryemployeedeductionstransactionsPostObj);
                            }
                            supplementarypayrollprocessingdetailsPostObj.setSupplementaryemployeedeductionstransactionsPostList(supplementaryemployeedeductionstransactionsPostList);
                        }



                        Criteria suppLoanDedCrit = session.createCriteria(Supplementaryemployeeloansandadvancesdetails.class);
                        suppLoanDedCrit.add(Restrictions.sqlRestriction("supplementarypayrollprocessingdetailsid='" + supplementarypayrollprocessingdetailsObj.getId() + "'"));
                        List suppLoanDedList = suppLoanDedCrit.list();
                        if (suppLoanDedList.size() > 0) {
                            List<SupplementaryemployeeloansandadvancesdetailsPost> supplementaryemployeeloansandadvancesdetailsPostList = new ArrayList<SupplementaryemployeeloansandadvancesdetailsPost>();
                            for (int k = 0; k < suppLoanDedList.size(); k++) {
                                Supplementaryemployeeloansandadvancesdetails supplementaryemployeeloansandadvancesdetailsObj = (Supplementaryemployeeloansandadvancesdetails) suppLoanDedList.get(k);

                                SupplementaryemployeeloansandadvancesdetailsPost supplementaryemployeeloansandadvancesdetailsPostObj = new SupplementaryemployeeloansandadvancesdetailsPost();
                                supplementaryemployeeloansandadvancesdetailsPostObj.setId(supplementaryemployeeloansandadvancesdetailsObj.getId());
                                supplementaryemployeeloansandadvancesdetailsPostObj.setAccregion(supplementaryemployeeloansandadvancesdetailsObj.getAccregion());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getCancelled() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setCancelled(supplementaryemployeeloansandadvancesdetailsObj.getCancelled().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setEmployeeloansandadvancesId(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getInstallmentamount() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setInstallmentamount(supplementaryemployeeloansandadvancesdetailsObj.getInstallmentamount().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getLoanbalance() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setLoanbalance(supplementaryemployeeloansandadvancesdetailsObj.getLoanbalance().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getNthinstallment() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setNthinstallment(supplementaryemployeeloansandadvancesdetailsObj.getNthinstallment().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getSupplementarypayrollprocessingdetails() != null) {
                                    supplementaryemployeeloansandadvancesdetailsPostObj.setSupplementarypayrollprocessingdetailsId(supplementaryemployeeloansandadvancesdetailsObj.getSupplementarypayrollprocessingdetails().getId());
                                }

                                EmployeeLoansandAdvancesPost employeeLoansandAdvancesPostObj = new EmployeeLoansandAdvancesPost();
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment() != null) {
                                    employeeLoansandAdvancesPostObj.setCurrentinstallment(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getCurrentinstallment().toString());
                                }
                                employeeLoansandAdvancesPostObj.setDeductioncode(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getDeductioncode());
                                employeeLoansandAdvancesPostObj.setEpfno(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getEmployeemaster().getEpfno());
                                employeeLoansandAdvancesPostObj.setFileno(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFileno());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount() != null) {
                                    employeeLoansandAdvancesPostObj.setFirstinstallmentamount(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getFirstinstallmentamount().toString());
                                }
                                employeeLoansandAdvancesPostObj.setId(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getId());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount() != null) {
                                    employeeLoansandAdvancesPostObj.setInstallmentamount(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getInstallmentamount().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount() != null) {
                                    employeeLoansandAdvancesPostObj.setLoanamount(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanamount().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance() != null) {
                                    employeeLoansandAdvancesPostObj.setLoanbalance(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoanbalance().toString());
                                }
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate() != null) {
                                    employeeLoansandAdvancesPostObj.setLoandate(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoandate().toString());
                                }
                                employeeLoansandAdvancesPostObj.setLoantype(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getLoantype());
                                employeeLoansandAdvancesPostObj.setRegionno(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getRegionno());
                                employeeLoansandAdvancesPostObj.setStatus(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getStatus());
                                if (supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment() != null) {
                                    employeeLoansandAdvancesPostObj.setTotalinstallment(supplementaryemployeeloansandadvancesdetailsObj.getEmployeeloansandadvances().getTotalinstallment().toString());
                                }

                                employeeLoansandAdvancesPostModiList.add(employeeLoansandAdvancesPostObj);

                                supplementaryemployeeloansandadvancesdetailsPostList.add(supplementaryemployeeloansandadvancesdetailsPostObj);
                            }
                            supplementarypayrollprocessingdetailsPostObj.setSupplementaryemployeeloansandadvancesdetailsPostList(supplementaryemployeeloansandadvancesdetailsPostList);
                        }


                        supplementarypayrollprocessingdetailsPostList.add(supplementarypayrollprocessingdetailsPostObj);

                    }
                    supplementatypaybillPostObj.setSupplementarypayrollprocessingdetailsPostList(supplementarypayrollprocessingdetailsPostList);
                }

                supplementatypaybillPostList.add(supplementatypaybillPostObj);

            }
        }

        //Salary Structure        
        Criteria salStrucCrit = session.createCriteria(Salarystructureactual.class);
        salStrucCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
        salStrucCrit.add(Restrictions.sqlRestriction("periodto is null"));
        List salStrucList = salStrucCrit.list();
        if (salStrucList.size() > 0) {
            Salarystructureactual salarystructureactualObj = (Salarystructureactual) salStrucList.get(0);
            employeeDetailsObject.setSalarystructureaccregion(salarystructureactualObj.getAccregion());
            employeeDetailsObject.setSalarystructureid(salarystructureactualObj.getId());
            employeeDetailsObject.setSalarystructureorderno(salarystructureactualObj.getOrderno());
            if (salarystructureactualObj.getPeriodfrom() != null) {
                employeeDetailsObject.setSalarystructureperiodfrom(salarystructureactualObj.getPeriodfrom().toString());
            }
            if (salarystructureactualObj.getPeriodto() != null) {
                employeeDetailsObject.setSalarystructureperiodto(salarystructureactualObj.getPeriodto().toString());
            }
            List<EmployeeEarningDetailsPost> employeeEarningDetailsPostList = new ArrayList<EmployeeEarningDetailsPost>();
            Criteria earDetailsCrit = session.createCriteria(Employeeearningsdetailsactual.class);
            earDetailsCrit.add(Restrictions.sqlRestriction("salarystructureid = '" + salarystructureactualObj.getId() + "' "));
            earDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List earDetailsList = earDetailsCrit.list();
            if (earDetailsList.size() > 0) {
                for (int j = 0; j < earDetailsList.size(); j++) {
                    Employeeearningsdetailsactual employeeearningsdetailsactualObj = (Employeeearningsdetailsactual) earDetailsList.get(j);

                    EmployeeEarningDetailsPost employeeEarningDetailsPostObj = new EmployeeEarningDetailsPost();
                    employeeEarningDetailsPostObj.setAccregion(employeeearningsdetailsactualObj.getAccregion());
                    if (employeeearningsdetailsactualObj.getAmount() != null) {
                        employeeEarningDetailsPostObj.setAmount(employeeearningsdetailsactualObj.getAmount().toString());
                    }
                    if (employeeearningsdetailsactualObj.getCancelled() != null) {
                        employeeEarningDetailsPostObj.setCancelled(employeeearningsdetailsactualObj.getCancelled().toString());
                    }
                    employeeEarningDetailsPostObj.setEarningmasterid(employeeearningsdetailsactualObj.getEarningmasterid());
                    employeeEarningDetailsPostObj.setId(employeeearningsdetailsactualObj.getId());
                    if (employeeearningsdetailsactualObj.getIspercentage() != null) {
                        employeeEarningDetailsPostObj.setIspercentage(employeeearningsdetailsactualObj.getIspercentage().toString());
                    }
                    if (employeeearningsdetailsactualObj.getPercentage() != null) {
                        employeeEarningDetailsPostObj.setPercentage(employeeearningsdetailsactualObj.getPercentage().toString());
                    }
                    employeeEarningDetailsPostObj.setSalarystructure(employeeearningsdetailsactualObj.getSalarystructureactual().getId());
                    employeeEarningDetailsPostList.add(employeeEarningDetailsPostObj);
                }
            }
            List<EmployeeDeductionDetailsPost> employeeDeductionDetailsPostList = new ArrayList<EmployeeDeductionDetailsPost>();
            Criteria dedDetailsCrit = session.createCriteria(Employeedeductiondetailsactual.class);
            dedDetailsCrit.add(Restrictions.sqlRestriction("salarystructureactualid = '" + salarystructureactualObj.getId() + "' "));
            dedDetailsCrit.add(Restrictions.sqlRestriction("cancelled is false"));
            List dedDetailsList = dedDetailsCrit.list();
            if (dedDetailsList.size() > 0) {
                for (int j = 0; j < dedDetailsList.size(); j++) {
                    Employeedeductiondetailsactual employeedeductiondetailsObj = (Employeedeductiondetailsactual) dedDetailsList.get(j);

                    EmployeeDeductionDetailsPost employeeDeductionDetailsPostObj = new EmployeeDeductionDetailsPost();
                    employeeDeductionDetailsPostObj.setAccregion(employeedeductiondetailsObj.getAccregion());
                    if (employeedeductiondetailsObj.getAmount() != null) {
                        employeeDeductionDetailsPostObj.setAmount(employeedeductiondetailsObj.getAmount().toString());
                    }
                    if (employeedeductiondetailsObj.getCancelled() != null) {
                        employeeDeductionDetailsPostObj.setCancelled(employeedeductiondetailsObj.getCancelled().toString());
                    }
                    employeeDeductionDetailsPostObj.setDednNo(employeedeductiondetailsObj.getDednNo());
                    employeeDeductionDetailsPostObj.setId(employeedeductiondetailsObj.getId());
                    employeeDeductionDetailsPostObj.setDeductionmasterid(employeedeductiondetailsObj.getDeductionmasterid());
                    if (employeedeductiondetailsObj.getIspercentage() != null) {
                        employeeDeductionDetailsPostObj.setIspercentage(employeedeductiondetailsObj.getIspercentage().toString());
                    }
                    if (employeedeductiondetailsObj.getPercentage() != null) {
                        employeeDeductionDetailsPostObj.setPercentage(employeedeductiondetailsObj.getPercentage().toString());
                    }
                    employeeDeductionDetailsPostObj.setSalarystructureactual(employeedeductiondetailsObj.getSalarystructureactual().getId());


                    employeeDeductionDetailsPostList.add(employeeDeductionDetailsPostObj);
                }
            }



            employeeDetailsObject.setEmployeeDeductionDetailsPostList(employeeDeductionDetailsPostList);
            employeeDetailsObject.setEmployeeEarningDetailsPostList(employeeEarningDetailsPostList);

        }
        //Deduction Account Code
        List<EmployeePayCodeAccountDetailsPost> employeePayCodeAccountDetailsPostList = new ArrayList<EmployeePayCodeAccountDetailsPost>();
        Criteria dedCodeDetailsCrit = session.createCriteria(Employeedeductionaccountcode.class);
        dedCodeDetailsCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
        List dedCodeDetailsList = dedCodeDetailsCrit.list();
        if (dedCodeDetailsList.size() > 0) {
            for (int j = 0; j < dedCodeDetailsList.size(); j++) {
                Employeedeductionaccountcode employeedeductionaccountcodeObj = (Employeedeductionaccountcode) dedCodeDetailsList.get(j);

                EmployeePayCodeAccountDetailsPost employeePayCodeAccountDetailsPostObj = new EmployeePayCodeAccountDetailsPost();
                employeePayCodeAccountDetailsPostObj.setDeductionaccountcode(employeedeductionaccountcodeObj.getDeductionaccountcode());
                employeePayCodeAccountDetailsPostObj.setId(employeedeductionaccountcodeObj.getId());
                employeePayCodeAccountDetailsPostObj.setPaycodemaster(employeedeductionaccountcodeObj.getPaycodemaster().getPaycode());
                employeePayCodeAccountDetailsPostList.add(employeePayCodeAccountDetailsPostObj);
            }
        }



        employeeDetailsObject.setEmployeePayCodeAccountDetailsPostList(employeePayCodeAccountDetailsPostList);
        //Deduction Account Code ends

        employeeDetailsObject.setSupplementatypaybillPostList(supplementatypaybillPostList);
        employeeDetailsObject.setEmployeeLoansandAdvancesPostModiList(employeeLoansandAdvancesPostModiList);
        employeeDetailsObject.setModiloans(String.valueOf(employeeLoansandAdvancesPostModiList.size()));


        try {
            JAXBContext context = JAXBContext.newInstance(EmployeeDetailsModel.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(employeeDetailsObject, stringWriter);
            result = stringWriter.toString();
        } catch (Exception e) {
        }

        return result;
    }

    public Employeemaster getEmployeemaster(Session session, String epfno) {
        Employeemaster employeemasterObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Employeemaster.class);
            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                employeemasterObj = (Employeemaster) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }


        return employeemasterObj;
    }

    public synchronized String getmaxofEmployeeTransfer(Session session) {
        int maxSequenceNumber = 1;

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(id as int)) as maxsequencenumber from employeetransferhistory");
            List imlist = session.createSQLQuery(sb.toString()).list();
            if (imlist.size() > 0) {
                if (imlist.get(0) != null) {
                    maxSequenceNumber = (Integer) imlist.get(0);
                    maxSequenceNumber = maxSequenceNumber + 1;
                } else {
                    maxSequenceNumber = 1;
                }
            } else {
                maxSequenceNumber = 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return String.valueOf(maxSequenceNumber);
    }

    public Map checkuploadexists(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map map = new HashMap();

        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map upLoadTxtFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, FormFile filename, String epfno) {
        Map resultMap = new HashMap();
        FileOutputStream fop = null;
        try {
            String respectivePath = getFilePath(request, epfno);

            FormFile formFile = (FormFile) filename;

            byte[] byteFile = formFile.getFileData();
            File f = new File(respectivePath);
            fop = new FileOutputStream(f);
            fop.write(byteFile);
            fop.close();
            System.out.println("Sairam, " + filename + "File is uploaded successfully");

            resultMap.put("filepath", epfno);
            resultMap.put("message", "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMap.put("message", "Failed");
//            request.setAttribute("message", "Failed to upload");
        }

        return resultMap;
    }

    private String getFilePath(HttpServletRequest request, String epfno) {
        String path = request.getRealPath("/");
        //System.out.println("path====" + path);
        String sessionid = request.getSession(false).getId();
        String osName = System.getProperty("os.name");
        String fileName = "salaryfile";
        String folder = path + epfno + "_" + sessionid;

        File dir = new File(folder);
        dir.delete();
        if (!dir.exists()) {
            dir.mkdir();
        }

        String resPath = "";
        if (osName.equalsIgnoreCase("Linux") || osName.equalsIgnoreCase("Unix")) {
            resPath = folder + "/" + fileName + ".txt";
        } else {
            resPath = folder + "\\" + fileName + ".txt";
        }
        //System.out.println(" resPath : " + resPath);
        return resPath;
    }

    public Map displayFileDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filename) {
        Map map = new HashMap();
        String content = "";
        StringBuffer resultHTML = new StringBuffer();
        try {
            String respectivePath = getFilePath(request, filename);
            content = readFileAsString(respectivePath);
            JAXBContext context;
            context = JAXBContext.newInstance(EmployeeDetailsModel.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            EmployeeDetailsModel employeeDetailsModelObj = (EmployeeDetailsModel) unmarshaller.unmarshal(new StringReader(content));

            resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">");
            resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td colspan=\"4\">Details of Employee</td>");
            resultHTML.append("</tr>");

            resultHTML.append("<tr class = \"rowColor1\" >");
            resultHTML.append("<td>Epf No</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getEpfno() + "</td>");
            resultHTML.append("<td>Employee Name</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getEmployeename() + "</td>");
            resultHTML.append("</tr>");

            resultHTML.append("<tr class = \"rowColor2\" >");
            resultHTML.append("<td>Designation</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getDesignation() + "</td>");
            resultHTML.append("<td>PF No</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getFpfno() + "</td>");
            resultHTML.append("</tr>");

            resultHTML.append("<tr class = \"rowColor1\" >");
            resultHTML.append("<td>Branch</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getRegion() + "</td>");
            resultHTML.append("<td>Section Name</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getSection() + "</td>");
            resultHTML.append("</tr>");

            resultHTML.append("<tr class = \"rowColor2\" >");
            resultHTML.append("<td>Date of Birth</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getDateofbirth() + "</td>");
            resultHTML.append("<td>Date of Appointment</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getDateofappoinment() + "</td>");
            resultHTML.append("</tr>");

            resultHTML.append("<tr class = \"rowColor1\" >");
            resultHTML.append("<td>Date of Confirmation</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getDateofconfirmation() + "</td>");
            resultHTML.append("<td>Last EL Surrender Date</td>");
            resultHTML.append("<td>" + employeeDetailsModelObj.getEslp() + "</td>");
            resultHTML.append("</tr>");

            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td colspan=\"2\">Earnings</td>");
            resultHTML.append("<td colspan=\"2\">Deductions</td>");
            resultHTML.append("</tr>");
            String classname = "";
            resultHTML.append("<tr>");
            resultHTML.append("<td colspan=\"2\">");
            resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList() != null) {
                for (int i = 0; i < employeeDetailsModelObj.getEmployeeEarningDetailsPostList().size(); i++) {
                    if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getCancelled().equalsIgnoreCase("FALSE")) {
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }
                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td>" + employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getEarningmasterid() + "</td>");
                        resultHTML.append("<td align=\"right\">" + employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getAmount() + "</td>");
                        resultHTML.append("</tr>");
                    }
                }
            }
            resultHTML.append("</table>");
            resultHTML.append("</td>");

            resultHTML.append("<td colspan=\"2\">");
            resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList() != null) {
                for (int i = 0; i < employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().size(); i++) {
                    if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getCancelled().equalsIgnoreCase("FALSE")) {
                        if (i % 2 == 0) {
                            classname = "rowColor1";
                        } else {
                            classname = "rowColor2";
                        }
                        resultHTML.append("<tr class=\"" + classname + "\">");
                        resultHTML.append("<td>" + employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getDeductionmasterid() + "</td>");
                        resultHTML.append("<td align=\"right\">" + employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getAmount() + "</td>");
                        resultHTML.append("</tr>");
                    }
                }
            }
            resultHTML.append("</table>");
            resultHTML.append("</td>");
            resultHTML.append("</tr>");

            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td colspan=\"4\">Loan Details</td>");
            resultHTML.append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td>" + "LOAN" + "</td>");
            resultHTML.append("<td>" + "LOAN AMOUNT" + "</td>");
            resultHTML.append("<td>" + "LOAN BALANCE" + "</td>");
            resultHTML.append("<td>" + "INSTALLMENT DETAILS" + "</td>");
            resultHTML.append("</tr>");
            if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList() != null) {
                for (int i = 0; i < employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td>" + employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getDeductioncode() + "</td>");
                    resultHTML.append("<td align=\"right\">" + employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoanamount() + "</td>");
                    resultHTML.append("<td align=\"right\">" + employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoanbalance() + "</td>");
                    resultHTML.append("<td>" + employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getTotalinstallment() + "/" + employeeDetailsModelObj.getEmployeeLoansandAdvancesPostModiList().get(i).getCurrentinstallment() + "</td>");
                    resultHTML.append("</tr>");

                }
            }
            resultHTML.append("</table>");
            resultHTML.append("</td>");
            resultHTML.append("</tr>");

            resultHTML.append("</table>");
            resultHTML.append("<tr></td>");
            resultHTML.append("</table>");


        } catch (JAXBException ex) {
            Logger.getLogger(EmployeeTransferOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeTransferOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        map.put("details", resultHTML.toString());
        return map;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveUploadedDatas(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String filename) {
        Map map = new HashMap();
        String content = "";
        StringBuffer resultHTML = new StringBuffer();
        Transaction transaction;
        try {
            String respectivePath = getFilePath(request, filename);
            content = readFileAsString(respectivePath);
            JAXBContext context;
            context = JAXBContext.newInstance(EmployeeDetailsModel.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            EmployeeDetailsModel employeeDetailsModelObj = (EmployeeDetailsModel) unmarshaller.unmarshal(new StringReader(content));

            Employeemaster employeeMaster = new Employeemaster();
            employeeMaster.setEpfno(employeeDetailsModelObj.getEpfno());
            employeeMaster.setFpfno(employeeDetailsModelObj.getFpfno());
            employeeMaster.setEmployeename(employeeDetailsModelObj.getEmployeename());
            employeeMaster.setFathername(employeeDetailsModelObj.getFathername());
            employeeMaster.setGender(employeeDetailsModelObj.getGender());
            if (employeeDetailsModelObj.getDateofbirth() != null) {
                employeeMaster.setDateofbirth(DateParser.postgresDate1(employeeDetailsModelObj.getDateofbirth()));
            }
            if (employeeDetailsModelObj.getDateofappoinment() != null) {
                employeeMaster.setDateofappoinment(DateParser.postgresDate1(employeeDetailsModelObj.getDateofappoinment()));
            }
            if (employeeDetailsModelObj.getDateofprobation() != null) {
                employeeMaster.setDateofprobation(DateParser.postgresDate1(employeeDetailsModelObj.getDateofprobation()));
            }
            if (employeeDetailsModelObj.getDateofconfirmation() != null) {
                employeeMaster.setDateofconfirmation(DateParser.postgresDate1(employeeDetailsModelObj.getDateofconfirmation()));
            }
            employeeMaster.setRegion(LoggedInRegion);
            employeeMaster.setSection(employeeDetailsModelObj.getSection());
            employeeMaster.setDesignation(employeeDetailsModelObj.getDesignation());
            employeeMaster.setEmpSta(employeeDetailsModelObj.getEmpSta());
            employeeMaster.setPaymentmode(employeeDetailsModelObj.getPaymentmode());
            employeeMaster.setBanksbaccount(employeeDetailsModelObj.getBanksbaccount());
            employeeMaster.setBankcode(employeeDetailsModelObj.getBankcode());
            employeeMaster.setPancardno(employeeDetailsModelObj.getPancardno());
            employeeMaster.setCommunity(employeeDetailsModelObj.getCommunity());
            if (employeeDetailsModelObj.getEslp() != null) {
                employeeMaster.setEslp(DateParser.postgresDate1(employeeDetailsModelObj.getEslp()));
            }
            employeeMaster.setProcess(Boolean.TRUE);
            employeeMaster.setCategory(employeeDetailsModelObj.getCategory());
            employeeMaster.setSubsection(employeeDetailsModelObj.getSubsection());

            transaction = session.beginTransaction();
            session.merge(employeeMaster);
            transaction.commit();


            if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList() != null) {
                for (int i = 0; i < employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().size(); i++) {

                    Employeeloansandadvances employeeloansandadvancesObj = new Employeeloansandadvances();
                    employeeloansandadvancesObj.setId(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getId());
                    employeeloansandadvancesObj.setEmployeemaster(employeeMaster);
                    employeeloansandadvancesObj.setDeductioncode(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getDeductioncode());
                    employeeloansandadvancesObj.setLoandate(DateParser.postgresDate1(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoandate()));
                    if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoanamount() != null) {
                        employeeloansandadvancesObj.setLoanamount(new BigDecimal(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoanamount()));
                    } else {
                        employeeloansandadvancesObj.setLoanamount(BigDecimal.ZERO);
                    }
                    if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getTotalinstallment() != null) {
                        employeeloansandadvancesObj.setTotalinstallment(Integer.parseInt(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getTotalinstallment()));
                    }
                    if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getCurrentinstallment() != null) {
                        employeeloansandadvancesObj.setCurrentinstallment(Integer.parseInt(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getCurrentinstallment()));
                    }
                    if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getInstallmentamount() != null) {
                        employeeloansandadvancesObj.setInstallmentamount(new BigDecimal(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getInstallmentamount()));
                    } else {
                        employeeloansandadvancesObj.setInstallmentamount(BigDecimal.ZERO);
                    }
                    if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getFirstinstallmentamount() != null) {
                        employeeloansandadvancesObj.setFirstinstallmentamount(new BigDecimal(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getFirstinstallmentamount()));
                    } else {
                        employeeloansandadvancesObj.setFirstinstallmentamount(BigDecimal.ZERO);
                    }
                    if (employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoanbalance() != null) {
                        employeeloansandadvancesObj.setLoanbalance(new BigDecimal(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoanbalance()));
                    } else {
                        employeeloansandadvancesObj.setLoanbalance(BigDecimal.ZERO);
                    }
                    employeeloansandadvancesObj.setLoantype(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getLoantype());
                    employeeloansandadvancesObj.setStatus(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getStatus());
                    employeeloansandadvancesObj.setFileno(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getFileno());
                    employeeloansandadvancesObj.setRegionno(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getRegionno());
                    employeeloansandadvancesObj.setAccregion(employeeDetailsModelObj.getEmployeeLoansandAdvancesPostList().get(i).getAccregion());


                    transaction = session.beginTransaction();
                    session.merge(employeeloansandadvancesObj);
                    transaction.commit();
                }

            }

            Salarystructureactual salarystructureactualObj = new Salarystructureactual();
            salarystructureactualObj.setId(employeeDetailsModelObj.getSalarystructureid());
            salarystructureactualObj.setEmployeemaster(employeeMaster);
            if (employeeDetailsModelObj.getSalarystructureperiodfrom() != null) {
                salarystructureactualObj.setPeriodfrom(DateParser.postgresDate1(employeeDetailsModelObj.getSalarystructureperiodfrom()));
            }
            if (employeeDetailsModelObj.getSalarystructureperiodto() != null) {
                salarystructureactualObj.setPeriodto(DateParser.postgresDate1(employeeDetailsModelObj.getSalarystructureperiodto()));
            }
            salarystructureactualObj.setOrderno(employeeDetailsModelObj.getSalarystructureorderno());

            transaction = session.beginTransaction();
            session.merge(salarystructureactualObj);
            transaction.commit();

            if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList() != null) {
                for (int i = 0; i < employeeDetailsModelObj.getEmployeeEarningDetailsPostList().size(); i++) {
                    Employeeearningsdetailsactual employeeearningsdetailsactualObj = new Employeeearningsdetailsactual();

                    employeeearningsdetailsactualObj.setId(employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getId());
                    employeeearningsdetailsactualObj.setSalarystructureactual(salarystructureactualObj);
                    employeeearningsdetailsactualObj.setEarningmasterid(employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getEarningmasterid());
                    if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getAmount() != null) {
                        employeeearningsdetailsactualObj.setAmount(new BigDecimal(employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getAmount()));
                    } else {
                        employeeearningsdetailsactualObj.setAmount(BigDecimal.ZERO);
                    }
                    if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getPercentage() != null) {
                        employeeearningsdetailsactualObj.setPercentage(new BigDecimal(employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getPercentage()));
                    }
                    if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getIspercentage() != null) {
                        if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getIspercentage().equalsIgnoreCase("TRUE")) {
                            employeeearningsdetailsactualObj.setIspercentage(Boolean.TRUE);
                        } else {
                            employeeearningsdetailsactualObj.setIspercentage(Boolean.FALSE);
                        }
                    }
                    if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getCancelled() != null) {
                        if (employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getCancelled().equalsIgnoreCase("TRUE")) {
                            employeeearningsdetailsactualObj.setCancelled(Boolean.TRUE);
                        } else {
                            employeeearningsdetailsactualObj.setCancelled(Boolean.FALSE);
                        }
                    }
                    employeeearningsdetailsactualObj.setAccregion(employeeDetailsModelObj.getEmployeeEarningDetailsPostList().get(i).getAccregion());

                    transaction = session.beginTransaction();
                    session.merge(employeeearningsdetailsactualObj);
                    transaction.commit();
                }

            }

            if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList() != null) {
                for (int i = 0; i < employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().size(); i++) {
                    Employeedeductiondetailsactual employeedeductiondetailsactualObj = new Employeedeductiondetailsactual();

                    employeedeductiondetailsactualObj.setId(employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getId());
                    employeedeductiondetailsactualObj.setSalarystructureactual(salarystructureactualObj);
                    employeedeductiondetailsactualObj.setDeductionmasterid(employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getDeductionmasterid());
                    if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getAmount() != null) {
                        employeedeductiondetailsactualObj.setAmount(new BigDecimal(employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getAmount()));
                    } else {
                        employeedeductiondetailsactualObj.setAmount(BigDecimal.ZERO);
                    }
                    if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getPercentage() != null) {
                        employeedeductiondetailsactualObj.setPercentage(new BigDecimal(employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getPercentage()));
                    } else {
                        employeedeductiondetailsactualObj.setPercentage(BigDecimal.ZERO);
                    }
                    if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getIspercentage() != null) {
                        if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getIspercentage().equalsIgnoreCase("TRUE")) {
                            employeedeductiondetailsactualObj.setIspercentage(Boolean.TRUE);
                        } else {
                            employeedeductiondetailsactualObj.setIspercentage(Boolean.FALSE);
                        }
                    }

                    employeedeductiondetailsactualObj.setDednNo(employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getDednNo());
                    if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getCancelled() != null) {
                        if (employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getCancelled().equalsIgnoreCase("TRUE")) {
                            employeedeductiondetailsactualObj.setCancelled(Boolean.TRUE);
                        } else {
                            employeedeductiondetailsactualObj.setCancelled(Boolean.FALSE);
                        }
                    }
                    employeedeductiondetailsactualObj.setAccregion(employeeDetailsModelObj.getEmployeeDeductionDetailsPostList().get(i).getAccregion());





                    transaction = session.beginTransaction();
                    session.merge(employeedeductiondetailsactualObj);
                    transaction.commit();
                }
            }




        } catch (JAXBException ex) {
            Logger.getLogger(EmployeeTransferOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeTransferOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        map.put("details", "Updated Successfully");
        return map;
    }
}
