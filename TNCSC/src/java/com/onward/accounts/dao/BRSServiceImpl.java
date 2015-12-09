/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.accounts.dao;

import com.onward.action.OnwardAction;
import com.onward.common.*;
import com.onward.dao.GlobalDBOpenCloseAndUserPrivilages;
import com.onward.persistence.payroll.*;
import com.onward.reports.accounts.BRSRegionReport;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Karthikeyan S
 */
public class BRSServiceImpl extends OnwardAction implements BRSService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAccountBook(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        System.out.println("************************** BRSServiceImpl Class getAccountBook method is calling *********************************");
        Map acBookMap = new HashMap();
        Map acBookList = new LinkedHashMap();
        LinkedHashMap regionMap = new LinkedHashMap();
        acBookList.put("0", "--Select--");
        try {
            Criteria accountBookCrit = session.createCriteria(Accountsbooks.class);
            List accountBookList = accountBookCrit.list();
            if (accountBookList.size() > 0) {
                for (int i = 0; i < accountBookList.size(); i++) {
                    Accountsbooks accountsbooksObj = (Accountsbooks) accountBookList.get(i);
                    acBookList.put(accountsbooksObj.getCode(), accountsbooksObj.getBookname());
                }
            }
            acBookMap.put("accountbookList", acBookList);
            acBookMap.put("accountYearList", getAccountBooks(session).get("accYearList"));
            regionMap = (LinkedHashMap) getRegions(session).get("regionList");
            regionMap.put("NR", "Null Region");
            acBookMap.put("regionList", regionMap);
//            acBookMap.put("regionList", getRegions(session).get("regionList"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return acBookMap;
    }

    public synchronized String getBrsDetailsId(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getBrsdetailsid();
//                System.out.println("maxStr = " + maxNoStr);
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setBrsdetailsid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + maxNoStr;
//            System.out.println("BudgetID = " + maxStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }

    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map uploadReconciliationFile(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String serverPath) {
        System.out.println("************************** BRSServiceImpl Class uploadReconciliationFile method is calling *********************************");
        Map resultMap = new HashMap();
//        System.out.println("month   =  " + month);
//        System.out.println("year   =  " + year);
//        System.out.println("accountbook   =  " + accountbook);
//        System.out.println("accountingperiod   =  " + accountingperiod);
//        System.out.println("serverPath   =  " + serverPath);
        int MONTH = Integer.valueOf(month) + 1;
        int YEAR = Integer.valueOf(year);

//        System.out.println("Month = " + MONTH);
//        System.out.println("YEAR = " + YEAR);

        Brsdetails brsdetails = null;
        ReadxlsFile readxlsFile = new ReadxlsFile();
        SessionFactory _factory = HibernateUtil.getSessionFactory();
        Session hibernate_session = _factory.openSession();
        Transaction transaction = hibernate_session.beginTransaction();

        try {
            File file = new File(serverPath);
            FileInputStream fis = new FileInputStream(file);

            LinkedHashMap excelmap = readxlsFile.getReadXLSFile(fis);
            int TOTALRECORDS = (Integer) excelmap.get("totalrows");
            LinkedHashMap xlsmap = (LinkedHashMap) excelmap.get("map");
//            System.out.println("TOTALRECORDS = " + TOTALRECORDS);
//            System.out.println("xlsmap size = " + xlsmap.size());
            int count = 0;
            Iterator itr = xlsmap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next();
                int key = (Integer) me.getKey();
//                System.out.println("KEY = " + key);
                brsdetails = new Brsdetails();
                List listval = (List) me.getValue();
                String transactiondate = ((String) listval.get(0)).trim();
                String valuedate = ((String) listval.get(1)).trim();
                String description = ((String) listval.get(2)).trim();
                String chequeno = ((String) listval.get(3)).trim();
                String branchcode = ((String) listval.get(4)).trim();
                String debit = ((String) listval.get(5)).trim();
                String credit = ((String) listval.get(6)).trim();
                String balance = ((String) listval.get(7)).trim();
                String regioncode = null;

                Criteria brsledgetcriteria = session.createCriteria(Brsledger.class);
                brsledgetcriteria.add(Restrictions.sqlRestriction("branchcode='" + branchcode + "'"));
                List branchlist = brsledgetcriteria.list();
                if (branchlist.size() > 0) {
                    Brsledger brsledger = (Brsledger) branchlist.get(0);
                    regioncode = brsledger.getRegion();
                }

                double doubledebit = Double.valueOf((debit.length() == 0) ? "0.00" : debit);
                BigDecimal bddoubledebit = new BigDecimal(doubledebit);

                double doublecredit = Double.valueOf((credit.length() == 0) ? "0.00" : credit);
                BigDecimal bddoublecredit = new BigDecimal(doublecredit);

                double doublebalance = Double.valueOf((balance.length() == 0) ? "0.00" : balance);
                BigDecimal bddoublebalance = new BigDecimal(doublebalance);

                brsdetails.setId(getBrsDetailsId(session, LoggedInRegion));
                brsdetails.setTransactiondate(DateParser.postgresDate(transactiondate));
                brsdetails.setValuedate(DateParser.postgresDate(valuedate));
                brsdetails.setDescription(description);
                brsdetails.setReferenceno(chequeno);
                brsdetails.setBranchcode(branchcode);
                brsdetails.setRegionmaster(getRegionmaster(session, regioncode));
                brsdetails.setMonth(MONTH);
                brsdetails.setYear(YEAR);
                brsdetails.setDebit(bddoubledebit);
                brsdetails.setCredit(bddoublecredit);
                brsdetails.setBalance(bddoublebalance);
                brsdetails.setAccountingyear(CommonUtility.getAccountingyear(session, accountingperiod));
                brsdetails.setAccountsbooks(CommonUtility.getAccountBook(session, accountbook));
                brsdetails.setIsapproved(false);
                brsdetails.setCancelled(false);
                brsdetails.setCreatedby(LoggedInUser);
                brsdetails.setCreateddate(getCurrentDate());
//                System.out.println("***************************   " + key + "   *****************************");
//                System.out.println("brsdetails.getBranchcode() = " + brsdetails.getBranchcode());
//                System.out.println("bddoubledebit = " + bddoubledebit);
                hibernate_session.save(brsdetails);

                if (++count % 50 == 0) {
                    //flush a batch of updates and release memory:
                    hibernate_session.flush();
                    hibernate_session.clear();
                }
            }
            if (!transaction.wasCommitted()) {
                transaction.commit();
            }
            resultMap.put("ERROR", null);
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Upload Excel File Format is corrupted!");
            e.printStackTrace();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Upload Excel File Format is corrupted!");
            ex.printStackTrace();
        } finally {
            hibernate_session.close();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionWiseBrsDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region) {
        System.out.println("*************************** BRSServiceImpl class getRegionWiseBrsDetails method is calling *********************");
        Map resultMap = new HashMap();

        try {
            Criteria brsCrit = session.createCriteria(Brsdetails.class);
            brsCrit.add(Restrictions.sqlRestriction(" month=" + month));
            brsCrit.add(Restrictions.sqlRestriction(" year=" + year));
            brsCrit.add(Restrictions.sqlRestriction(" accountingperiod='" + accountingperiod + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" accountbook='" + accountbook + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
            brsCrit.add(Restrictions.sqlRestriction(" isapproved is false"));
            if (region.equalsIgnoreCase("NR")) {
                brsCrit.add(Restrictions.sqlRestriction(" region is null or region=''"));
            } else {
                brsCrit.add(Restrictions.sqlRestriction(" region='" + region + "'"));
            }
            List<Brsdetails> brsList = brsCrit.list();
            if (brsList.size() > 0) {
                StringBuffer resultHTML = new StringBuffer();
                String classname = "";

                resultHTML.append("<FONT SIZE=2>");
                resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"brstable\">");
                resultHTML.append("<thead>");
                resultHTML.append("<tr>");
                resultHTML.append("<td align=\"center\">S.No</td>");
                resultHTML.append("<td align=\"center\">Region Name</td>");
                resultHTML.append("<td align=\"center\">Transaction Date</td>");
                resultHTML.append("<td align=\"center\">Value Date</td>");
                resultHTML.append("<td align=\"center\">Description</td>");
                resultHTML.append("<td align=\"center\">Ref No./Cheque No.</td>");
                resultHTML.append("<td align=\"center\">Branch Name</td>");
                resultHTML.append("<td align=\"center\">Debit</td>");
                resultHTML.append("<td align=\"center\">Credit</td>");
                resultHTML.append("<td align=\"center\">Balance</td>");
                resultHTML.append("<td align=\"center\">Approval</td>");
                resultHTML.append("</tr>");
                resultHTML.append("</thead>");
                resultHTML.append("<tbody>");
                for (int i = 0; i < brsList.size(); i++) {
                    Brsdetails brsdetailsObj = (Brsdetails) brsList.get(i);
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    Regionmaster regionmasterObj = brsdetailsObj.getRegionmaster();
                    if (regionmasterObj == null) {
                        resultHTML.append("<td align=\"center\">  </td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + brsdetailsObj.getRegionmaster().getRegionname() + "</td>");
                    }
                    resultHTML.append("<td align=\"center\">" + dateToString(brsdetailsObj.getTransactiondate()) + "</td>");
                    resultHTML.append("<td align=\"center\">" + dateToString(brsdetailsObj.getValuedate()) + "</td>");
                    resultHTML.append("<td align=\"left\">" + brsdetailsObj.getDescription().trim() + "</td>");
                    resultHTML.append("<td align=\"left\">" + brsdetailsObj.getReferenceno().trim() + "</td>");

                    Brsledger brsledgerObj = getBRSledger(session, brsdetailsObj.getBranchcode());
                    if (brsledgerObj == null) {
                        resultHTML.append("<td align=\"center\"> " + brsdetailsObj.getBranchcode() + " </td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + brsdetailsObj.getBranchcode() + " - " + brsledgerObj.getBranchname() + "</td>");
                    }
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getDebit() + "</td>");
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getCredit() + "</td>");
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getBalance() + "</td>");
                    resultHTML.append("<td  align=\"center\">" + "<input type=\"checkbox\" name=\"brsApprovalName\" id=\"" + brsdetailsObj.getId() + "\" value=\"" + brsdetailsObj.getId() + "\" >" + "</td>");



                }
                resultHTML.append("</tbody>");
                resultHTML.append("<tfoot>");
                resultHTML.append("<td  align=\"center\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"savebutton\" id=\"savebutton\" value=\"Approve\"  onclick=\"saveApprovalList();\"  >" + "</td>");
                resultHTML.append("</tfoot>");
                resultHTML.append("</table>");
                resultHTML.append("</FONT>");
                resultMap.put("brsdetails", resultHTML.toString());
            } else {
                resultMap.put("ERROR", "No Record Found");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveBRSApprovalList(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region, String approvalarray) {
        System.out.println("*************************** BRSServiceImpl class saveBRSApprovalList method is calling *********************");
        Map resultMap = new HashMap();
        String[] strArray = null;
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            String brsids = approvalarray.substring(0, approvalarray.length() - 1);
            brsids = brsids.replaceAll(",", "','");
            StringBuffer qryStr = new StringBuffer();
            qryStr.append("UPDATE brsdetails  SET isapproved  = true WHERE id in ('" + brsids + "') ");
            qryStr.append(" and cancelled is false and  accountingperiod='" + accountingperiod + "' ");
            qryStr.append(" and  accountbook='" + accountbook + "' and  month=" + month + " and  year=" + year + "");

            session.createSQLQuery(qryStr.toString()).executeUpdate();
//            session.createSQLQuery("UPDATE brsdetails  SET cancelled  = true WHERE id in ('" + brsids + "') and cancelled is false and  accountingperiod='" + accountingperiod + "' and  accountbook='" + accountbook + "' and  month=" + month + " and  year=" + year + "").executeUpdate();
            transaction.commit();

            resultMap.put("success", "Selected List Approved");
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMap.put("ERROR", " Approval List Transaction Faild");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionWiseBrsReportDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region, String status) {
        System.out.println("*************************** BRSServiceImpl class getRegionWiseBrsReportDetails method is calling *********************");
        Map resultMap = new HashMap();
        BigDecimal debit = new BigDecimal(0.00);
        BigDecimal credit = new BigDecimal(0.00);

        try {

            Criteria brsCrit = session.createCriteria(Brsdetails.class);
            brsCrit.add(Restrictions.sqlRestriction(" month=" + month));
            brsCrit.add(Restrictions.sqlRestriction(" year=" + year));
            brsCrit.add(Restrictions.sqlRestriction(" accountingperiod='" + accountingperiod + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" accountbook='" + accountbook + "'"));
//            brsCrit.add(Restrictions.sqlRestriction(" region='" + region + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
            if (status.equalsIgnoreCase("1")) {
                brsCrit.add(Restrictions.sqlRestriction(" isapproved is true"));
            } else {
                brsCrit.add(Restrictions.sqlRestriction(" isapproved is false"));
            }
            if (region.equalsIgnoreCase("NR")) {
                brsCrit.add(Restrictions.sqlRestriction(" region is null or region=''"));
            } else {
                brsCrit.add(Restrictions.sqlRestriction(" region='" + region + "'"));
            }
            List<Brsdetails> brsList = brsCrit.list();
            if (brsList.size() > 0) {
                StringBuffer resultHTML = new StringBuffer();
                String classname = "";

                resultHTML.append("<FONT SIZE=2>");
                resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"brstable\">");
                resultHTML.append("<thead>");
                resultHTML.append("<tr>");
                resultHTML.append("<td align=\"center\">S.No</td>");
                resultHTML.append("<td align=\"center\">Region Name</td>");
                resultHTML.append("<td align=\"center\">Transaction Date</td>");
                resultHTML.append("<td align=\"center\">Value Date</td>");
                resultHTML.append("<td align=\"center\">Description</td>");
                resultHTML.append("<td align=\"center\">Ref No./Cheque No.</td>");
                resultHTML.append("<td align=\"center\">Branch Name</td>");
                resultHTML.append("<td align=\"center\">Debit</td>");
                resultHTML.append("<td align=\"center\">Credit</td>");
                resultHTML.append("<td align=\"center\">Balance</td>");
                resultHTML.append("<td align=\"center\">Status</td>");
                resultHTML.append("</tr>");
                resultHTML.append("</thead>");
                resultHTML.append("<tbody>");
                for (int i = 0; i < brsList.size(); i++) {
                    Brsdetails brsdetailsObj = (Brsdetails) brsList.get(i);
                    debit = debit.add(brsdetailsObj.getDebit());
                    credit = credit.add(brsdetailsObj.getCredit());
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }

                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    Regionmaster regionmasterObj = brsdetailsObj.getRegionmaster();
                    if (regionmasterObj == null) {
                        resultHTML.append("<td align=\"center\">  </td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + brsdetailsObj.getRegionmaster().getRegionname() + "</td>");
                    }
                    resultHTML.append("<td align=\"center\">" + dateToString(brsdetailsObj.getTransactiondate()) + "</td>");
                    resultHTML.append("<td align=\"center\">" + dateToString(brsdetailsObj.getValuedate()) + "</td>");
                    resultHTML.append("<td align=\"left\">" + brsdetailsObj.getDescription().trim() + "</td>");
                    resultHTML.append("<td align=\"left\">" + brsdetailsObj.getReferenceno().trim() + "</td>");

                    Brsledger brsledgerObj = getBRSledger(session, brsdetailsObj.getBranchcode());
                    if (brsledgerObj == null) {
                        resultHTML.append("<td align=\"center\"> " + brsdetailsObj.getBranchcode() + " </td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + brsdetailsObj.getBranchcode() + " - " + brsledgerObj.getBranchname() + "</td>");
                    }
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getDebit() + "</td>");
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getCredit() + "</td>");
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getBalance() + "</td>");
                    if (brsdetailsObj.getIsapproved()) {
                        resultHTML.append("<td align=\"right\"> Approved </td>");
                    } else {
                        resultHTML.append("<td align=\"right\"> Pending </td>");
                    }

                }
                resultHTML.append("</tbody>");
                resultHTML.append("<tfoot>");
                resultHTML.append("<td colspan=\"6\" >&nbsp;&nbsp;</td>");
                resultHTML.append("<td align=\"center\" >" + "<input type=\"button\" CLASS=\"submitbu\" name=\"savebutton\" id=\"savebutton\" value=\"Export Excel\"  onclick=\"exportBRSdetailsRegionWise();\"  >" + "</td>");
                resultHTML.append("<td align=\"right\" >" + debit + "</td>");
                resultHTML.append("<td align=\"right\" >" + credit + "</td>");
                resultHTML.append("<td colspan=\"2\">&nbsp;&nbsp;</td>");
                resultHTML.append("</tfoot>");
                resultHTML.append("</table>");
                resultHTML.append("</FONT>");
                resultMap.put("brsdetails", resultHTML.toString());

                resultMap.put("month", month);
                resultMap.put("year", year);
                resultMap.put("accountbook", accountbook);
                resultMap.put("accountingperiod", accountingperiod);
                resultMap.put("region", region);
                resultMap.put("status", status);
            } else {
                resultMap.put("ERROR", "No Record Found");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getBrsReportDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod) {
        System.out.println("*************************** BRSServiceImpl class getBrsReportDetails method is calling *********************");
        Map resultMap = new HashMap();
        BigDecimal debit = new BigDecimal(0.00);
        BigDecimal credit = new BigDecimal(0.00);
        try {

            Criteria brsCrit = session.createCriteria(Brsdetails.class);
            brsCrit.add(Restrictions.sqlRestriction(" month=" + month));
            brsCrit.add(Restrictions.sqlRestriction(" year=" + year));
            brsCrit.add(Restrictions.sqlRestriction(" accountingperiod='" + accountingperiod + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" accountbook='" + accountbook + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
            List<Brsdetails> brsList = brsCrit.list();
            if (brsList.size() > 0) {
                StringBuffer resultHTML = new StringBuffer();
                String classname = "";

                resultHTML.append("<FONT SIZE=2>");
                resultHTML.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"brstable\">");
                resultHTML.append("<thead>");
                resultHTML.append("<tr>");
                resultHTML.append("<td align=\"center\">S.No</td>");
                resultHTML.append("<td align=\"center\">Region Name</td>");
                resultHTML.append("<td align=\"center\">Transaction Date</td>");
                resultHTML.append("<td align=\"center\">Value Date</td>");
                resultHTML.append("<td align=\"center\">Description</td>");
                resultHTML.append("<td align=\"center\">Ref No./Cheque No.</td>");
                resultHTML.append("<td align=\"center\">Branch Name</td>");
                resultHTML.append("<td align=\"center\">Debit</td>");
                resultHTML.append("<td align=\"center\">Credit</td>");
                resultHTML.append("<td align=\"center\">Balance</td>");
                resultHTML.append("<td align=\"center\">Status</td>");
                resultHTML.append("</tr>");
                resultHTML.append("</thead>");
                resultHTML.append("<tbody>");
                for (int i = 0; i < brsList.size(); i++) {
                    Brsdetails brsdetailsObj = (Brsdetails) brsList.get(i);
                    debit = debit.add(brsdetailsObj.getDebit());
                    credit = credit.add(brsdetailsObj.getCredit());
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Regionmaster regionmasterObj = brsdetailsObj.getRegionmaster();

                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    if (regionmasterObj == null) {
                        resultHTML.append("<td align=\"center\">  </td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + brsdetailsObj.getRegionmaster().getRegionname() + "</td>");
                    }
                    resultHTML.append("<td align=\"center\">" + dateToString(brsdetailsObj.getTransactiondate()) + "</td>");
                    resultHTML.append("<td align=\"center\">" + dateToString(brsdetailsObj.getValuedate()) + "</td>");
                    resultHTML.append("<td align=\"left\">" + brsdetailsObj.getDescription().trim() + "</td>");
                    resultHTML.append("<td align=\"left\">" + brsdetailsObj.getReferenceno().trim() + "</td>");

                    Brsledger brsledgerObj = getBRSledger(session, brsdetailsObj.getBranchcode());
                    if (brsledgerObj == null) {
                        resultHTML.append("<td align=\"center\"> " + brsdetailsObj.getBranchcode() + " </td>");
                    } else {
                        resultHTML.append("<td align=\"left\">" + brsdetailsObj.getBranchcode() + " - " + brsledgerObj.getBranchname() + "</td>");
                    }
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getDebit() + "</td>");
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getCredit() + "</td>");
                    resultHTML.append("<td align=\"right\">" + brsdetailsObj.getBalance() + "</td>");
                    if (brsdetailsObj.getIsapproved()) {
                        resultHTML.append("<td align=\"right\"> Approved </td>");
                    } else {
                        resultHTML.append("<td align=\"right\"> Pending </td>");
                    }

                }
                resultHTML.append("</tbody>");
                resultHTML.append("<tfoot>");
                resultHTML.append("<td colspan=\"7\" >&nbsp;&nbsp;</td>");
                resultHTML.append("<td align=\"right\" >" + debit + "</td>");
                resultHTML.append("<td align=\"right\" >" + credit + "</td>");
                resultHTML.append("<td colspan=\"2\" >&nbsp;&nbsp;</td>");
                resultHTML.append("</tfoot>");
                resultHTML.append("</table>");
                resultHTML.append("</FONT>");
                resultMap.put("brsdetails", resultHTML.toString());
            } else {
                resultMap.put("ERROR", "No Record Found");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map BRSRegionWiseReport(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String month, String year, String accountbook, String accountingperiod, String region, String status, String filePathwithName) {
        System.out.println("*************************** BRSServiceImpl class BRSRegionWiseReport method is calling *********************");
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        BRSRegionReport bRSRegionReport = new BRSRegionReport();
        Map resultMap = new HashMap();
        BigDecimal debit = new BigDecimal(0.00);
        BigDecimal credit = new BigDecimal(0.00);
        BigDecimal balance = new BigDecimal(0.00);
        Map brsdetailsMap = new HashMap();

        System.out.println("month = " + month);
        System.out.println("year = " + year);
        System.out.println("accountbook = " + accountbook);
        System.out.println("accountingperiod = " + accountingperiod);
        System.out.println("region = " + region);
        System.out.println("status = " + status);
        System.out.println("filePathwithName = " + filePathwithName);

        try {

            Criteria brsCrit = session.createCriteria(Brsdetails.class);
            brsCrit.add(Restrictions.sqlRestriction(" month=" + month));
            brsCrit.add(Restrictions.sqlRestriction(" year=" + year));
            brsCrit.add(Restrictions.sqlRestriction(" accountingperiod='" + accountingperiod + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" accountbook='" + accountbook + "'"));
//            brsCrit.add(Restrictions.sqlRestriction(" region='" + region + "'"));
            brsCrit.add(Restrictions.sqlRestriction(" cancelled is false"));
            if (status.equalsIgnoreCase("1")) {
                brsCrit.add(Restrictions.sqlRestriction(" isapproved is true"));
            } else {
                brsCrit.add(Restrictions.sqlRestriction(" isapproved is false"));
            }
            if (region.equalsIgnoreCase("NR")) {
                brsCrit.add(Restrictions.sqlRestriction(" region is null or region=''"));
            } else {
                brsCrit.add(Restrictions.sqlRestriction(" region='" + region + "'"));
            }
            System.out.println("query = " + brsCrit.toString());
            List<Brsdetails> brsList = brsCrit.list();

            System.out.println("brsList.size() = " + brsList.size());
            if (brsList.size() > 0) {

                List rowList = null;
                List<List> brsDetailsList = new ArrayList<List>();

                for (int i = 0; i < brsList.size(); i++) {
                    // <editor-fold defaultstate="collapsed" desc="brsList for loop">
                    rowList = new ArrayList();
                    Brsdetails brsdetailsObj = (Brsdetails) brsList.get(i);
                    debit = debit.add(brsdetailsObj.getDebit());
                    credit = credit.add(brsdetailsObj.getCredit());
                    balance = balance.add(debit.subtract(credit));

                    rowList.add(String.valueOf(i + 1));
                    Regionmaster regionmasterObj = brsdetailsObj.getRegionmaster();
                    if (regionmasterObj == null) {
                        rowList.add("");
                    } else {
                        rowList.add(brsdetailsObj.getRegionmaster().getRegionname());
                    }

                    rowList.add(dateToString(brsdetailsObj.getTransactiondate()));
                    rowList.add(dateToString(brsdetailsObj.getValuedate()));
                    rowList.add(brsdetailsObj.getDescription().trim());
                    rowList.add(brsdetailsObj.getReferenceno().trim());

                    Brsledger brsledgerObj = getBRSledger(session, brsdetailsObj.getBranchcode());
                    if (brsledgerObj == null) {
                        rowList.add("");
                    } else {
                        rowList.add(brsdetailsObj.getBranchcode());
                    }

//                    rowList.add(brsdetailsObj.getDebit());
//                    rowList.add(brsdetailsObj.getCredit());
//                    rowList.add(brsdetailsObj.getBalance());

                    double douDebit = brsdetailsObj.getDebit().doubleValue();
                    double douCredit = brsdetailsObj.getCredit().doubleValue();
                    double douBalance = brsdetailsObj.getBalance().doubleValue();
                    String strDebit = decimalFormat.format(douDebit);
                    String strCredit = decimalFormat.format(douCredit);
                    String strBalance = decimalFormat.format(douBalance);
                    rowList.add(strDebit);
                    rowList.add(strCredit);
                    rowList.add(strBalance);


                    if (brsdetailsObj.getIsapproved()) {
                        rowList.add("Approved");
                    } else {
                        rowList.add("Pending");
                    }
                    brsDetailsList.add(rowList);
                    // </editor-fold>
                }
                rowList = new ArrayList();
                rowList.add("");
                rowList.add("");
                rowList.add("");
                rowList.add("");
                rowList.add("");
                rowList.add("");
                rowList.add("");
                rowList.add(decimalFormat.format(debit.doubleValue()));
                rowList.add(decimalFormat.format(credit.doubleValue()));
                rowList.add(decimalFormat.format(balance.doubleValue()));
                rowList.add("");
                brsDetailsList.add(rowList);

                String monthyear = months[(Integer.valueOf(month)) - 1] + "\"" + year;

                brsdetailsMap.put("regionname", getRegionmaster(session, LoggedInRegion).getRegionname());
                brsdetailsMap.put("monthandyear", monthyear);
                brsdetailsMap.put("brsstatus", status);
                brsdetailsMap.put("brsDetailsList", brsDetailsList);

                bRSRegionReport.GenerateBRSRegionReportExcel(brsdetailsMap, filePathwithName);


            } else {
                resultMap.put("ERROR", "No Record Found");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }
}
