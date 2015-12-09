/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.action.OnwardAction;
import com.onward.persistence.payroll.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class EmployeeLoansAndAdvancesImpl extends OnwardAction implements EmployeeLoansAndAdvances {

    String classname = "";

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDetailsNLoanDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();
        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "'"));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
            resultMap.put("designation", empmasterObj.getDesignation());
            resultMap.put("fathername", empmasterObj.getFathername());
            resultMap.put("dateofbirth", dateToString(empmasterObj.getDateofbirth()));
            resultMap.put("doa", dateToString(empmasterObj.getDateofappoinment()));
            resultMap.put("dateofprobation", dateToString(empmasterObj.getDateofprobation()));

            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getRegion() + "'"));

            List<Regionmaster> rgnList = rgnCrit.list();
            Regionmaster lbobj = rgnList.get(0);
            resultMap.put("branchname", lbobj.getRegionname());

            Criteria secCrit = session.createCriteria(Sectionmaster.class);
//            Criteria secCrit = session.createCriteria(Sectionmaster.class).add(Restrictions.sqlRestriction("id='"+empmasterObj.getSection()+"'"));
            secCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getSection() + "'"));
            List secList = secCrit.list();
            Sectionmaster secobj = (Sectionmaster) secList.get(0);
            resultMap.put("section", secobj.getSectionname());


            Criteria desCrit = session.createCriteria(Designationmaster.class);
            desCrit.add(Restrictions.sqlRestriction("designationcode = '" + empmasterObj.getDesignation() + "'"));
            List<Designationmaster> desList = desCrit.list();
            Designationmaster desobj = desList.get(0);
            resultMap.put("designation", desobj.getDesignation());

            resultMap.put("loandetails", getEmployeeLoanDetails(session, epfno, LoggedInRegion).toString());;
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");

        }

        return resultMap;
    }

    public String getEmployeeLoanDetails(Session session, String epfno, String LoggedInRegion) {
        String payCodeName = "";
        StringBuffer resultHTML = new StringBuffer();
        Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
        empLoanCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
        empLoanCrit.addOrder(Order.desc("loanbalance"));
        empLoanCrit.addOrder(Order.asc("loandate"));
//        empLoanCrit.add(Restrictions.sqlRestriction("accregion = '" + LoggedInRegion + "' "));
        List empLoanList = empLoanCrit.list();
        if (empLoanList.size() > 0) {
            resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Loan Name</td>").append("<td>Loan Date</td>").append("<td>Loan Amt</td>").append("<td>Tot Ins.</td>").append("<td>Cur Ins.</td>").append("<td>Inst Amt</td>").append("<td>First Inst</td>").append("<td>Loan Balance</td>").append("<td>Region</td>").append("<td>File No</td>").append("<td>modify</td>").append("</tr>");

            for (int i = 0; i < empLoanList.size(); i++) {
                if (i % 2 == 0) {
                    classname = "rowColor1";
                } else {
                    classname = "rowColor2";
                }
                Employeeloansandadvances emploansObj = (Employeeloansandadvances) empLoanList.get(i);

                Criteria LoannameCrit = session.createCriteria(Paycodemaster.class);
                LoannameCrit.add(Restrictions.sqlRestriction("paycode = '" + emploansObj.getDeductioncode() + "' "));
                List<Paycodemaster> loannameList = LoannameCrit.list();
                if (loannameList.size() > 0) {
                    Paycodemaster loannameObj = loannameList.get(0);
                    payCodeName = loannameObj.getPaycodename();
                }

                resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"left\">" + payCodeName + "</td>").append("<td align=\"center\">" + dateToString(emploansObj.getLoandate()) + "</td>").append("<td align=\"right\">" + emploansObj.getLoanamount() + "</td>").append("<td align=\"center\">" + emploansObj.getTotalinstallment() + "</td>").append("<td align=\"right\">" + emploansObj.getCurrentinstallment() + "</td>").append("<td align=\"right\">" + emploansObj.getInstallmentamount() + "</td>").append("<td align=\"right\">" + emploansObj.getFirstinstallmentamount() + "</td>").append("<td align=\"right\">" + emploansObj.getLoanbalance() + "</td>");

                Criteria rgnCrit = session.createCriteria(Regionmaster.class);
                rgnCrit.add(Restrictions.sqlRestriction("id = '" + emploansObj.getRegionno() + "' "));
                List<Regionmaster> rgnList = rgnCrit.list();
                String regionstr = "--";
                if (rgnList.size() > 0) {
                    Regionmaster rgnObj = rgnList.get(0);
                    if (rgnObj.getRegionname().equalsIgnoreCase(null) || rgnObj.getRegionname().equalsIgnoreCase("")) {
                        regionstr = "--";
                    } else {
                        regionstr = rgnObj.getRegionname();
                    }

                }
                resultHTML.append("<td align=\"center\">" + regionstr + "</td>");

                resultHTML.append("<td align=\"center\">" + emploansObj.getFileno() + "</td>");
                Calendar curcal = Calendar.getInstance();
                int currentYear = curcal.get(Calendar.YEAR);
                int currentMonth = curcal.get(Calendar.MONTH) + 1;            
                String processingMonth = getProcessingMonth(session, emploansObj.getId(), epfno);
                String processingmonyear = processingMonth+"-"+getProcessingYear(session, emploansObj.getId(), epfno);
                String currentmonyear = currentMonth+"-"+currentYear;
                java.math.BigDecimal loanbalance = new BigDecimal("0.00");
                if ((loanbalance.compareTo(emploansObj.getLoanbalance()) == 0)&&(!currentmonyear.equalsIgnoreCase(processingmonyear)))  {
                    resultHTML.append("<td align=\"center\">Completed</td>");
                } else {
                    resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"loancode\" id=\"" + "paycodeObj.getPaycode()" + "\" onclick=\"setLoanId('" + emploansObj.getId() + "','" + isProcessinProgress(session, emploansObj.getId(), epfno) + "')\"></td>");
                }
                resultHTML.append("</tr>");
            }

        } else {
            resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Loan Name</td>").append("<td>Loan Date</td>").append("<td>Loan Amt</td>").append("<td>Tot Ins.</td>").append("<td>Cur Ins.</td>").append("<td>Inst Amt</td>").append("<td>First Inst</td>").append("<td>Region</td>").append("<td>File No</td>").append("</tr>");
            resultHTML.append("</table>");
            resultHTML.append("</td></tr>");
        }
        resultHTML.append("</table>");

        return resultHTML.toString();
    }

    public boolean isProcessinProgress(Session session, String loanId, String epfno) {
        boolean status = false;
        String queryStr = "select count(*) from employeeloansandadvancesdetails lad "
                + " left join employeeloansandadvances la on la.id=lad.employeeloansandadvancesid "
                + " left join payrollprocessingdetails prp on prp.id=payrollprocessingdetailsid "
                + " left join payrollprocessing pp on pp.id=payrollprocessingid "
                + " where prp.employeeprovidentfundnumber ='" + epfno + "' and la.id='" + loanId + "' and pp.isopen=true and lad.cancelled=false";
        //System.out.println(queryStr);
        List proList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (proList.size() > 0) {
            BigInteger proces = (BigInteger) proList.get(0);

            if (proces.compareTo(BigInteger.ZERO) == 1) {
                status = true;
            }
        }


        return status;
    }

    public String getProcessingId(Session session, String loanId, String epfno) {
        String processingDetailsId = "";
        String queryStr = "select prp.id from employeeloansandadvancesdetails lad "
                + " left join employeeloansandadvances la on la.id=lad.employeeloansandadvancesid "
                + " left join payrollprocessingdetails prp on prp.id=payrollprocessingdetailsid "
                + " left join payrollprocessing pp on pp.id=payrollprocessingid "
                + " where prp.employeeprovidentfundnumber ='" + epfno + "' and la.id='" + loanId + "' and pp.isopen=true and lad.cancelled = false ";

        //System.out.println(queryStr);
        List proList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (proList.size() > 0) {
            processingDetailsId = (String) proList.get(0);
        }
        return processingDetailsId;
    }

    public String getProcessingMonth(Session session, String loanId, String epfno) {
        int processingMonth = 0;
        String queryStr = "select prp.month from employeeloansandadvancesdetails lad "
                + " left join employeeloansandadvances la on la.id=lad.employeeloansandadvancesid "
                + " left join payrollprocessingdetails prp on prp.id=payrollprocessingdetailsid "
                + " left join payrollprocessing pp on pp.id=payrollprocessingid "
                + " where prp.employeeprovidentfundnumber ='" + epfno + "' and la.id='" + loanId + "' and pp.isopen=true and lad.cancelled = false ";
        //System.out.println("Processing Month Query" + queryStr);
        List proList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (proList.size() > 0) {
            processingMonth = (Integer) proList.get(0);

        }
        return String.valueOf(processingMonth);
    }

    public String getProcessingYear(Session session, String loanId, String epfno) {
        int processingYear = 0;
        String queryStr = "select prp.year from employeeloansandadvancesdetails lad "
                + " left join employeeloansandadvances la on la.id=lad.employeeloansandadvancesid "
                + " left join payrollprocessingdetails prp on prp.id=payrollprocessingdetailsid "
                + " left join payrollprocessing pp on pp.id=payrollprocessingid "
                + " where prp.employeeprovidentfundnumber ='" + epfno + "' and la.id='" + loanId + "' and pp.isopen=true and lad.cancelled = false ";
        List proList = (ArrayList) session.createSQLQuery(queryStr).list();
        if (proList.size() > 0) {
            processingYear = (Integer) proList.get(0);
        }
        //System.out.println("processingYear" + processingYear);
        return String.valueOf(processingYear);
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getLoanDetailsForModification(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String loanId, String epfno) {
        Map resultMap = new HashMap();
        Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
        empLoanCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
        empLoanCrit.add(Restrictions.sqlRestriction("id = '" + loanId + "' "));
        List empLoanList = empLoanCrit.list();
        if (empLoanList.size() > 0) {
            Employeeloansandadvances employeeloansandadvancesObj = (Employeeloansandadvances) empLoanList.get(0);
            resultMap.put("loanname", employeeloansandadvancesObj.getDeductioncode());
            resultMap.put("loandate", dateToString(employeeloansandadvancesObj.getLoandate()));
            resultMap.put("loanamount", employeeloansandadvancesObj.getLoanamount());
            resultMap.put("noofinstallment", employeeloansandadvancesObj.getTotalinstallment());
            resultMap.put("firstinstallmentamt", employeeloansandadvancesObj.getFirstinstallmentamount());
            resultMap.put("successiveinstallmentamt", employeeloansandadvancesObj.getInstallmentamount());
            resultMap.put("fileno", employeeloansandadvancesObj.getFileno());
            resultMap.put("loanid", employeeloansandadvancesObj.getId());
            if (isProcessinProgress(session, loanId, epfno)) {
                resultMap.put("loanbalance", getLoanBalance(session, loanId, epfno).toString());
            } else {
                resultMap.put("loanbalance", employeeloansandadvancesObj.getLoanbalance());
            }
            if (isProcessinProgress(session, loanId, epfno)) {
                resultMap.put("completedins", getLoanIns(session, loanId, epfno));
            } else {
                resultMap.put("completedins", employeeloansandadvancesObj.getCurrentinstallment());
            }

        }
        return resultMap;
    }

    public BigDecimal getLoanBalance(Session session, String loanId, String epfno) {
        BigDecimal loanBalance = null;
        Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
        Employeeloansandadvances employeeloansandadvancesObje;
        Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
        empLoanDetailsCrit.add(Restrictions.sqlRestriction("employeeloansandadvancesid='" + loanId + "'"));
        empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + getProcessingId(session, loanId, epfno) + "'"));
        empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
        List empLoanDetailsList = empLoanDetailsCrit.list();
        if (empLoanDetailsList.size() > 0) {
            employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(0);
            //System.out.println("stage 1 loanId" + loanId);
            Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
            empLoanCrit.add(Restrictions.sqlRestriction("id='" + employeeloansandadvancesdetailsObje.getEmployeeloansandadvances().getId() + "'"));
            List empLoanList = empLoanCrit.list();
            if (empLoanList.size() > 0) {
                employeeloansandadvancesObje = (Employeeloansandadvances) empLoanList.get(0);
                loanBalance = employeeloansandadvancesObje.getLoanbalance().add(employeeloansandadvancesdetailsObje.getInstallmentamount());
                //System.out.println("stage 2 loanBalance" + loanBalance);
            }
        }
        return loanBalance;
    }

    public Integer getLoanIns(Session session, String loanId, String epfno) {
        Integer loanIns = null;
        Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
        Employeeloansandadvances employeeloansandadvancesObje;
        Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
        empLoanDetailsCrit.add(Restrictions.sqlRestriction("employeeloansandadvancesid='" + loanId + "'"));
        empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + getProcessingId(session, loanId, epfno) + "'"));
        empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
        List empLoanDetailsList = empLoanDetailsCrit.list();
        if (empLoanDetailsList.size() > 0) {
            employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(0);
            //System.out.println("stage 1 loanId" + loanId);
            Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
            empLoanCrit.add(Restrictions.sqlRestriction("id='" + employeeloansandadvancesdetailsObje.getEmployeeloansandadvances().getId() + "'"));
            List empLoanList = empLoanCrit.list();
            if (empLoanList.size() > 0) {
                employeeloansandadvancesObje = (Employeeloansandadvances) empLoanList.get(0);
                loanIns = employeeloansandadvancesObje.getCurrentinstallment() - 1;
                //System.out.println("stage 2 loanBalance" + loanIns);
            }
        }
        return loanIns;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getEmployeeDetailsNRecoveryDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno) {
        Map resultMap = new HashMap();

        Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
        empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
        empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
        List empDetailsList = empDetailsCrit.list();
        if (empDetailsList.size() > 0) {
            Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);
            resultMap.put("employeename", empmasterObj.getEmployeename());
//            resultMap.put("designation", empmasterObj.getDesignation());
            resultMap.put("fathername", empmasterObj.getFathername());
            resultMap.put("dateofbirth", dateToString(empmasterObj.getDateofbirth()));
            resultMap.put("doa", dateToString(empmasterObj.getDateofappoinment()));
            resultMap.put("dateofprobation", dateToString(empmasterObj.getDateofprobation()));

            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getRegion() + "' "));

            List<Regionmaster> rgnList = rgnCrit.list();
            Regionmaster lbobj = rgnList.get(0);
            resultMap.put("branchname", lbobj.getRegionname());

            Criteria secCrit = session.createCriteria(Sectionmaster.class);
            secCrit.add(Restrictions.sqlRestriction("id = '" + empmasterObj.getSection() + "' "));
            List<Sectionmaster> secList = secCrit.list();
            Sectionmaster secobj = secList.get(0);
            resultMap.put("section", secobj.getSectionname());

            Criteria desCrit = session.createCriteria(Designationmaster.class);
            desCrit.add(Restrictions.sqlRestriction("designationcode = '" + empmasterObj.getDesignation() + "' "));
            List<Designationmaster> desList = desCrit.list();
            Designationmaster desobj = desList.get(0);
            resultMap.put("designation", desobj.getDesignation());

            resultMap.put("recoverydetails", getEmployeeRecoveryDetails(session, epfno).toString());
        } else {
            resultMap.put("ERROR", "Given EPF Number is Wrong. ");

        }

        return resultMap;
    }

    public String getEmployeeRecoveryDetails(Session session, String epfno) {
        StringBuffer resultHTML = new StringBuffer();
        String paycodeName = "";

        Criteria emprecoveryCrit = session.createCriteria(Employeerecoveries.class);
        emprecoveryCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber = '" + epfno + "' "));
        List empRecoveryList = emprecoveryCrit.list();
        if (empRecoveryList.size() > 0) {
            resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Recovery Name</td>").append("<td>Recovery Date</td>").append("<td>Recovery Amt</td>").append("<td>Tot Ins.</td>").append("<td>Cur Ins.</td>").append("<td>Inst Amt</td>").append("<td>First Inst</td>").append("<td>Loan Balance</td>").append("<td>Region</td>").append("<td>File No</td>").append("</tr>");
            for (int i = 0; i < empRecoveryList.size(); i++) {
                if (i % 2 == 0) {
                    classname = "rowColor1";
                } else {
                    classname = "rowColor2";
                }
                Employeerecoveries emploansObj = (Employeerecoveries) empRecoveryList.get(i);

                Criteria recoverynameCrit = session.createCriteria(Paycodemaster.class);
                recoverynameCrit.add(Restrictions.sqlRestriction("paycode = '" + emploansObj.getDeductioncode() + "' "));
                List<Paycodemaster> recoverynameList = recoverynameCrit.list();
                if (recoverynameList.size() > 0) {
                    Paycodemaster loannameObj = recoverynameList.get(0);
                    paycodeName = loannameObj.getPaycodename();
                }

                resultHTML.append("<tr class=\"" + classname + "\">").append("<td align=\"center\">" + (i + 1) + "</td>").append("<td align=\"left\">" + paycodeName + "</td>").append("<td align=\"center\">" + dateToString(emploansObj.getLoandate()) + "</td>").append("<td align=\"right\">" + emploansObj.getLoanamount() + "</td>").append("<td align=\"center\">" + emploansObj.getTotalinstallment() + "</td>").append("<td align=\"right\">" + emploansObj.getCurrentinstallment() + "</td>").append("<td align=\"right\">" + emploansObj.getInstallmentamount() + "</td>").append("<td align=\"right\">" + emploansObj.getFirstinstallmentamount() + "</td>");

                Criteria rgnCrit = session.createCriteria(Regionmaster.class);
                rgnCrit.add(Restrictions.sqlRestriction("id = '" + emploansObj.getRegionno() + "' "));
                List<Regionmaster> rgnList = rgnCrit.list();
                if (rgnList.size() > 0) {
                    Regionmaster rgnObj = rgnList.get(0);
                    if (rgnObj.getRegionname().equalsIgnoreCase(null) || rgnObj.getRegionname().equalsIgnoreCase("")) {
                        rgnObj.setRegionname("--");
                    }
                    resultHTML.append("<td align=\"center\">" + rgnObj.getRegionname() + "</td>");
                }

                resultHTML.append("<td align=\"center\">" + emploansObj.getFileno() + "</td>").append("</tr>");
            }

        } else {
            resultHTML.append("<table width=\"95%\" align=\"center\" class=\"tableBorder\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
            resultHTML.append("<tr><td valign=\"top\">").append("<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">").append("<tr class=\"gridmenu\">").append("<td>S.No</td>").append("<td>Recovery Name</td>").append("<td>Recovery Date</td>").append("<td>Recovery Amt</td>").append("<td>Tot Ins.</td>").append("<td>Cur Ins.</td>").append("<td>Inst Amt</td>").append("<td>First Inst</td>").append("<td>Region</td>").append("<td>File No</td>").append("</tr>");
            resultHTML.append("</table>");
            resultHTML.append("</td></tr>");
        }

        resultHTML.append("</table>");

        return resultHTML.toString();
    }

    public Employeeloansandadvances getEmployeeloansandadvancesObj(Session session, String id) {
        Employeeloansandadvances employeeloansandadvancesobj = null;
        Criteria checkCrit = session.createCriteria(Employeeloansandadvances.class);
        checkCrit.add(Restrictions.sqlRestriction("id='" + id + "' "));
        List checkList = checkCrit.list();
        if (checkList.size() > 0) {
            employeeloansandadvancesobj = (Employeeloansandadvances) checkList.get(0);
        }
        return employeeloansandadvancesobj;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeLoan(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regionCode, String loanid, String epfno, String paycode, String loandate, String loanamount, String noofinstallment, String firstinstallmentamt, String successiveinstallmentamt, String fileno, String completedins, String loanbalance) {
        Map resultMap = new HashMap();
        Boolean blnresult = true;
        Transaction transaction = null;
//        System.out.println("Emoloyee Loan Details" + epfno + loanname + loandate + loanamount + noofinstallment + firstinstallmentamt + successiveinstallmentamt + fileno);
        try {
            if (loanid.trim().length() <= 0) {
                Criteria checkCrit = session.createCriteria(Employeeloansandadvances.class);
                checkCrit.add(Restrictions.sqlRestriction("employeeprovidentfundnumber='" + epfno + "' "));
                checkCrit.add(Restrictions.sqlRestriction("deductioncode='" + paycode + "' "));
                checkCrit.add(Restrictions.sqlRestriction("loanbalance!=0 "));
                List checkList = checkCrit.list();
                if (checkList.size() > 0) {
                    resultMap.put("ERROR", "This EPF No " + epfno + " Already have Loan on this Loan Type");
                    blnresult = false;
                }
            }
            if (blnresult) {
                Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
                empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
                empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
                List empDetailsList = empDetailsCrit.list();
                if (empDetailsList.size() > 0) {
                    Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);

                    Employeeloansandadvances employeeloansandadvancesObj;// = new Employeeloansandadvances();
                    if (loanid.trim().length() > 0) {
                        String procid = getProcessingId(session, loanid, epfno);
                        boolean ispro = isProcessinProgress(session, loanid, epfno);
//                        System.out.println("procid" + procid);
//                        System.out.println("ispro" + ispro);
//                        System.out.println("isProcessinProgress(session, loanid, epfno)" + isProcessinProgress(session, loanid, epfno));
                        String mon = getProcessingMonth(session, loanid, epfno);
                        if (mon.length() == 1) {
                            mon = "0" + mon;
                        }
                        String yea = getProcessingYear(session, loanid, epfno);
                        if (isProcessinProgress(session, loanid, epfno)) {
                            adjustLoan(session, procid);
                        }
                        employeeloansandadvancesObj = getEmployeeloansandadvancesObj(session, loanid);
                        employeeloansandadvancesObj.setDeductioncode(paycode);
                        employeeloansandadvancesObj.setLoandate(postgresDate(loandate));
                        employeeloansandadvancesObj.setLoanamount(new BigDecimal(loanamount));
                        employeeloansandadvancesObj.setTotalinstallment(Integer.parseInt(noofinstallment));
                        employeeloansandadvancesObj.setFirstinstallmentamount(new BigDecimal(firstinstallmentamt));
                        employeeloansandadvancesObj.setInstallmentamount(new BigDecimal(successiveinstallmentamt));
//                        employeeloansandadvancesObj.setRegionno(regionCode);
                        employeeloansandadvancesObj.setFileno(fileno);
                        employeeloansandadvancesObj.setLoanbalance(new BigDecimal(loanbalance));
                        employeeloansandadvancesObj.setCurrentinstallment(Integer.parseInt(completedins));
                        employeeloansandadvancesObj.setAccregion(LoggedInRegion);
                        transaction = session.beginTransaction();
                        session.update(employeeloansandadvancesObj);
                        transaction.commit();

                        if (ispro) {
//                            System.out.println("recalculating pay roll");

                            String proDate = "05/" + mon + "/" + yea;
                            EmployeePayBillProcessImpl employeePayBillProcessObj = new EmployeePayBillProcessImpl();
                            Map x = employeePayBillProcessObj.payRollProcess(session, request, response, LoggedInRegion, LoggedInUser, proDate, "0", epfno);


                        }
                    } else {
                        employeeloansandadvancesObj = new Employeeloansandadvances();
                        String id = getMaxSeqNumberEmployeeLoansNAdvances(session, LoggedInRegion);
                        employeeloansandadvancesObj.setId(id);
                        employeeloansandadvancesObj.setDeductioncode(paycode);
                        employeeloansandadvancesObj.setLoandate(postgresDate(loandate));
                        employeeloansandadvancesObj.setLoanamount(new BigDecimal(loanamount));
                        employeeloansandadvancesObj.setLoanbalance(new BigDecimal(loanamount));
                        employeeloansandadvancesObj.setTotalinstallment(Integer.parseInt(noofinstallment));
                        employeeloansandadvancesObj.setFirstinstallmentamount(new BigDecimal(firstinstallmentamt));
                        employeeloansandadvancesObj.setInstallmentamount(new BigDecimal(successiveinstallmentamt));
                        employeeloansandadvancesObj.setCurrentinstallment(0);
                        employeeloansandadvancesObj.setRegionno(LoggedInRegion);
                        employeeloansandadvancesObj.setFileno(fileno);
                        employeeloansandadvancesObj.setEmployeemaster(empmasterObj);
                        employeeloansandadvancesObj.setAccregion(LoggedInRegion);

                        transaction = session.beginTransaction();
                        session.save(employeeloansandadvancesObj);
                        transaction.commit();


                    }
                }
                resultMap.put("loandetails", getEmployeeLoanDetails(session, epfno, LoggedInRegion).toString());

                resultMap.put("message", "Loan Saved Successfully");

            }


        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return resultMap;
    }

    public void adjustLoan(Session session, String payrollprocessingid) {
        Employeeloansandadvancesdetails employeeloansandadvancesdetailsObje;
        Employeeloansandadvances employeeloansandadvancesObje;
        Transaction transaction = null;
        try {
            Criteria empLoanDetailsCrit = session.createCriteria(Employeeloansandadvancesdetails.class);
            empLoanDetailsCrit.add(Restrictions.sqlRestriction("payrollprocessingdetailsid='" + payrollprocessingid + "'"));
            empLoanDetailsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
            List empLoanDetailsList = empLoanDetailsCrit.list();
            if (empLoanDetailsList.size() > 0) {
                for (int i = 0; i < empLoanDetailsList.size(); i++) {
                    transaction = session.beginTransaction();
                    employeeloansandadvancesdetailsObje = (Employeeloansandadvancesdetails) empLoanDetailsList.get(i);

                    employeeloansandadvancesdetailsObje.setCancelled(Boolean.TRUE);
                    session.update(employeeloansandadvancesdetailsObje);

                    Criteria empLoanCrit = session.createCriteria(Employeeloansandadvances.class);
                    empLoanCrit.add(Restrictions.sqlRestriction("id='" + employeeloansandadvancesdetailsObje.getEmployeeloansandadvances().getId() + "'"));
                    List empLoanList = empLoanCrit.list();
                    if (empLoanList.size() > 0) {
                        employeeloansandadvancesObje = (Employeeloansandadvances) empLoanList.get(0);
                        employeeloansandadvancesObje.setCurrentinstallment(employeeloansandadvancesObje.getCurrentinstallment() - 1);
                        employeeloansandadvancesObje.setLoanbalance(employeeloansandadvancesObje.getLoanbalance().add(employeeloansandadvancesdetailsObje.getInstallmentamount()));
                        session.update(employeeloansandadvancesObje);
                    }
                    transaction.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public synchronized String getMaxSeqNumberEmployeeLoansNAdvances(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeeloansandadvancesid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeeloansandadvancesid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
//        System.out.println("bill processing serial no" + maxStr);
        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveEmployeeRecovery(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regionCode, String epfno, String recoveryname, String recoverydate, String recoveryamount, String noofinstallment, String firstinstallmentamt, String successiveinstallmentamt, String fileno) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
//        System.out.println("Emoloyee Loan Details" + epfno + recoveryname + recoverydate + recoveryamount + noofinstallment + firstinstallmentamt + successiveinstallmentamt + fileno);

        try {
            Criteria empDetailsCrit = session.createCriteria(Employeemaster.class);
            empDetailsCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));
            empDetailsCrit.add(Restrictions.sqlRestriction("region='" + LoggedInRegion + "'"));
            List empDetailsList = empDetailsCrit.list();
            if (empDetailsList.size() > 0) {
                Employeemaster empmasterObj = (Employeemaster) empDetailsList.get(0);

                Employeerecoveries employeerecoveriesObj = new Employeerecoveries();
                String id = getMaxSeqNumberEmployeeRecoveries(session, regionCode);
                employeerecoveriesObj.setId(id);
                employeerecoveriesObj.setDeductioncode(recoveryname);
                employeerecoveriesObj.setLoandate(postgresDate(recoverydate));
                employeerecoveriesObj.setLoanamount(new BigDecimal(recoveryamount));
                employeerecoveriesObj.setLoanbalance(new BigDecimal(recoveryamount));
                employeerecoveriesObj.setTotalinstallment(Integer.parseInt(noofinstallment));
                employeerecoveriesObj.setCurrentinstallment(0);
                employeerecoveriesObj.setFirstinstallmentamount(new BigDecimal(firstinstallmentamt));
                employeerecoveriesObj.setInstallmentamount(new BigDecimal(successiveinstallmentamt));
                employeerecoveriesObj.setRegionno(regionCode);
                employeerecoveriesObj.setFileno(fileno);
                employeerecoveriesObj.setAccregion(LoggedInRegion);
                employeerecoveriesObj.setEmployeemaster(empmasterObj);
                employeerecoveriesObj.setAccregion(LoggedInRegion);
                transaction = session.beginTransaction();
                session.save(employeerecoveriesObj);
                transaction.commit();
            }
            resultMap.put("recoverydetails", getEmployeeRecoveryDetails(session, epfno).toString());
            resultMap.put("message", "Recoveries Saved Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return resultMap;
    }

    public synchronized String getMaxSeqNumberEmployeeRecoveries(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getEmployeerecoveriesid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setEmployeerecoveriesid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        
        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadLoanNames(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String paycodetype) {
        Map resultMap = new HashMap();
        Map loanMap = new LinkedHashMap();
        loanMap.put("0", "--Select--");
        String loancode = "";
        String loanname = "";
        try {
            Criteria loanNameCrit = session.createCriteria(Paycodemaster.class);
            loanNameCrit.add(Restrictions.sqlRestriction("paycodetype = '" + paycodetype.toUpperCase() + "' "));
            loanNameCrit.addOrder(Order.asc("paycodename"));
            List<Paycodemaster> loanNameList = loanNameCrit.list();
            resultMap = new TreeMap();
            for (Paycodemaster lbobj : loanNameList) {
                loancode = lbobj.getPaycode();
                loanname = lbobj.getPaycodename();
                loanMap.put(loancode, loanname);
            }

            resultMap.put("loanlist", loanMap);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
   
}
