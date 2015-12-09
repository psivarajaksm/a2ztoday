/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.CommonUtility;
import com.onward.persistence.payroll.Accountgroups;
import com.onward.persistence.payroll.Accountsheads;
import com.onward.persistence.payroll.Bankledger;
import com.onward.persistence.payroll.Ledgermaster;
import com.onward.persistence.payroll.Partyledger;
import com.onward.persistence.payroll.Regionmaster;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author root
 */
public class AccountsMasterServiceImpl extends OnwardAction implements AccountsMasterService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadAccountGroupHeadDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();

        Map accountgrouplist = new LinkedHashMap();
        Map ledgerlist = new LinkedHashMap();
        accountgrouplist.put("0", "--Select--");
        ledgerlist.put("0", "--Select--");

        Accountgroups masterObj;
        Criteria lrCrit = session.createCriteria(Accountgroups.class);
        lrCrit.addOrder(Order.asc("groupname"));
        List ldList = lrCrit.list();
        if (ldList.size() > 0) {
            for (int i = 0; i < ldList.size(); i++) {
                masterObj = (Accountgroups) ldList.get(i);
                accountgrouplist.put(masterObj.getId(), masterObj.getGroupname());

            }

        }

        Ledgermaster ledgermasterObj;
        Criteria lrCrit1 = session.createCriteria(Ledgermaster.class);
        lrCrit1.addOrder(Order.asc("ledgername"));
        List ldList1 = lrCrit1.list();
        if (ldList1.size() > 0) {
            for (int i = 0; i < ldList1.size(); i++) {
                ledgermasterObj = (Ledgermaster) ldList1.get(i);
                ledgerlist.put(ledgermasterObj.getId(), ledgermasterObj.getLedgername());

            }

        }
        resultMap.put("accountgrouplist", accountgrouplist);
        resultMap.put("ledgerlist", ledgerlist);
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveAccountHeadMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String accountcode, String ledgercode, String accountheadname, String serialno) {
        Map resultMap = new HashMap();
//        Transaction transaction;
        try {

            Criteria lrCrit = session.createCriteria(Accountsheads.class);
            lrCrit.add(Restrictions.sqlRestriction("ledgercode = '" + ledgercode + "'"));
            lrCrit.add(Restrictions.sqlRestriction("groupcode = '" + accountcode + "'"));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountsheads accountsheadsObj = (Accountsheads) ldList.get(0);

                Accountgroups accountgroupsObj = new Accountgroups();
                accountgroupsObj.setId(accountcode);
                Ledgermaster ledgermasterObj = new Ledgermaster();
                ledgermasterObj.setId(ledgercode);

                accountsheadsObj.setAccountgroups(accountgroupsObj);
                accountsheadsObj.setLedgermaster(ledgermasterObj);
                accountsheadsObj.setAccname(accountheadname);

                Transaction transaction = session.beginTransaction();
                try {
                    session.update(accountsheadsObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }

            } else {
                Accountsheads masterobj = new Accountsheads();
//                if (serialno.equalsIgnoreCase("0")) {
                masterobj.setAcccode(getMaxAccountHeadMasterid(session, accountcode));
//                } else {
//                    masterobj.setAcccode(serialno);
//                }
                Accountgroups accountgroupsObj = new Accountgroups();
                accountgroupsObj.setId(accountcode);

                Ledgermaster ledgermasterObj = new Ledgermaster();
                ledgermasterObj.setId(ledgercode);

                masterobj.setAccountgroups(accountgroupsObj);
                masterobj.setLedgermaster(ledgermasterObj);
                masterobj.setAccname(accountheadname);

                Transaction transaction = session.beginTransaction();
                try {
                    session.save(masterobj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }

            }


            resultMap.put("success", "Account Head Master  Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Account Head Master Transaction Faild");
        }


        return resultMap;

    }

    public synchronized String getMaxAccountHeadMasterid(Session session, String id) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Accountgroups.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + id + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountgroups accountgroupsObj = (Accountgroups) ldList.get(0);
                maxNoStr = accountgroupsObj.getAccheadrunningno();
                maxNoStr = maxNoStr + 1;
                accountgroupsObj.setAccheadrunningno(maxNoStr);
                Transaction transaction = session.beginTransaction();
                try {
                    session.update(accountgroupsObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }

            maxStr = "" + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        regionMap.put("0", "--Select--");
        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.addOrder(Order.asc("regionname"));
            List<Regionmaster> rgnList = rgnCrit.list();
            resultMap = new TreeMap();
            for (Regionmaster lbobj : rgnList) {
                regionMap.put(lbobj.getId(), lbobj.getRegionname());
            }
            resultMap.put("regionlist", regionMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionBankDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regionid) {
        Map resultMap = new HashMap();
        resultMap.put("bankDetails", getRegionBankDetailsinHTML(session, regionid).toString());
        return resultMap;
    }

    public String getRegionBankDetailsinHTML(Session session, String regionid) {

        String classname = "";
        StringBuffer resultHTML = new StringBuffer();
        try {
            resultHTML.append("<FONT SIZE=2>");
            Criteria bankledgerCrit = session.createCriteria(Bankledger.class);
            bankledgerCrit.add(Restrictions.sqlRestriction("region='" + regionid + "'"));
            bankledgerCrit.addOrder(Order.asc("code"));
            List bankLedgerList = bankledgerCrit.list();
            resultHTML.append("<table cellpadding=\"0\" cellspacing=\"1\" border=\"0\" class=\"display\" id=\"banksledgertable\">");
            resultHTML.append("<thead>");
            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td>S.No</td>");
            resultHTML.append("<td>Region Name</td>");
            resultHTML.append("<td>Bank Name</td>");
            resultHTML.append("<td>Modify</td>");
            resultHTML.append("</tr>");
            resultHTML.append("</thead>");
            resultHTML.append("<tbody>");
            if (bankLedgerList.size() > 0) {

                for (int i = 0; i < bankLedgerList.size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Bankledger bankObj = (Bankledger) bankLedgerList.get(i);
                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    resultHTML.append("<td align=\"center\">" + bankObj.getRegionmaster().getRegionname() + "</td>");
                    resultHTML.append("<td align=\"left\">" + bankObj.getBankname() + "</td>");
                    resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"bankcode\" id=\"" + bankObj.getCode() + "\" onclick=\"ModifyBankName('" + bankObj.getCode() + "','" + bankObj.getRegionmaster().getId() + "','" + bankObj.getBankname() + "')\"></td>");
                    resultHTML.append("</tr>");
                }

            }
            resultHTML.append("</tbody>");
            resultHTML.append("</table>");
            resultHTML.append("</FONT>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveBankLedgerMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regioncode, String bankname, String bankcode) {
        Map resultMap = new HashMap();
        try {
            if (bankcode.equalsIgnoreCase("0")) {
                Bankledger masterobj = new Bankledger();
                masterobj.setCode(getBankLedgerMasterid(session, regioncode));

                masterobj.setRegionmaster(CommonUtility.getRegion(session, regioncode));
                masterobj.setBankname(bankname.toUpperCase());

                Transaction transaction = session.beginTransaction();
                try {
                    session.save(masterobj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            } else {
                Criteria lrCrit = session.createCriteria(Bankledger.class);
                lrCrit.add(Restrictions.sqlRestriction("code = '" + bankcode + "'"));
                List ldList = lrCrit.list();
                if (ldList.size() > 0) {
                    Bankledger bankledgerObj = (Bankledger) ldList.get(0);
//                    bankledgerObj.setCode(bankcode);
                    bankledgerObj.setRegionmaster(CommonUtility.getRegion(session, regioncode));
                    bankledgerObj.setBankname(bankname.toUpperCase());

                    Transaction transaction = session.beginTransaction();
                    try {
                        session.update(bankledgerObj);
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                    }

                }
            }

            resultMap.put("bankDetails", getRegionBankDetailsinHTML(session, regioncode).toString());

            resultMap.put("success", "Bank Ledger Master Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Bank Ledger Master Transaction Faild");
        }
        return resultMap;
    }

    public synchronized String getBankLedgerMasterid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getBankledgerid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setBankledgerid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                try {
                    session.update(regionmasterObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getRegionPartiesDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regionid) {
        Map resultMap = new HashMap();
        resultMap.put("partyDetails", getRegionPartiesDetailsinHTML(session, regionid).toString());
        return resultMap;
    }

    public String getRegionPartiesDetailsinHTML(Session session, String regionid) {

        String classname = "";
        StringBuffer resultHTML = new StringBuffer();
        try {
            resultHTML.append("<FONT SIZE=2>");
            Criteria partyledgerCrit = session.createCriteria(Partyledger.class);
            partyledgerCrit.add(Restrictions.sqlRestriction("region='" + regionid + "'"));
            partyledgerCrit.addOrder(Order.asc("code"));
            List partyLedgerList = partyledgerCrit.list();
            resultHTML.append("<table cellpadding=\"0\" cellspacing=\"1\" border=\"0\" class=\"display\" id=\"partyledgertable\">");
            resultHTML.append("<thead>");
            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td>S.No</td>");
            resultHTML.append("<td>Region Name</td>");
            resultHTML.append("<td>Party Name</td>");
            resultHTML.append("<td>Modify</td>");
            resultHTML.append("</tr>");
            resultHTML.append("</thead>");
            resultHTML.append("<tbody>");
            if (partyLedgerList.size() > 0) {

                for (int i = 0; i < partyLedgerList.size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Partyledger partyledgerObj = (Partyledger) partyLedgerList.get(i);
                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    resultHTML.append("<td align=\"center\">" + partyledgerObj.getRegionmaster().getRegionname() + "</td>");
                    resultHTML.append("<td align=\"left\">" + partyledgerObj.getPartyname() + "</td>");
                    resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"partycode\" id=\"" + partyledgerObj.getCode() + "\" onclick=\"ModifyPartyName('" + partyledgerObj.getCode() + "','" + partyledgerObj.getRegionmaster().getId() + "','" + partyledgerObj.getPartyname() + "','" + partyledgerObj.getTinno() + "')\"></td>");
                    resultHTML.append("</tr>");
                }

            }
            resultHTML.append("</tbody>");
            resultHTML.append("</table>");
            resultHTML.append("</FONT>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map savePartyLedgerMaster(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String regioncode, String partyname, String partycode, String tinno) {
        Map resultMap = new HashMap();
        try {
            if (partycode.equalsIgnoreCase("0")) {
                Partyledger masterobj = new Partyledger();
                masterobj.setCode(getPartyLedgerMasterid(session, regioncode));

                masterobj.setRegionmaster(CommonUtility.getRegion(session, regioncode));
                masterobj.setPartyname(partyname.toUpperCase());
                 masterobj.setTinno(tinno);

                Transaction transaction = session.beginTransaction();
                try {
                    session.save(masterobj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            } else {
                Criteria lrCrit = session.createCriteria(Partyledger.class);
                lrCrit.add(Restrictions.sqlRestriction("code = '" + partycode + "'"));
                List ldList = lrCrit.list();
                if (ldList.size() > 0) {
                    Partyledger partyledgerObj = (Partyledger) ldList.get(0);
//                    bankledgerObj.setCode(bankcode);
                    partyledgerObj.setRegionmaster(CommonUtility.getRegion(session, regioncode));
                    partyledgerObj.setPartyname(partyname.toUpperCase());
                    partyledgerObj.setTinno(tinno);
                    Transaction transaction = session.beginTransaction();
                    
                    try {
                        session.update(partyledgerObj);
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                    }
                }
            }

            resultMap.put("partyDetails", getRegionPartiesDetailsinHTML(session, regioncode).toString());

            resultMap.put("success", "Party Ledger Master Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ERROR", "Party Ledger Master Transaction Faild");
        }
        return resultMap;
    }

    public synchronized String getPartyLedgerMasterid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getPartyledgerid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPartyledgerid(maxNoStr);
                Transaction transaction = session.beginTransaction();
                try {
                    session.update(regionmasterObj);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }

            maxStr = regionCode + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return maxStr;
    }
    
    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAccountBooksDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        resultMap.put("bookDetails", getAccountBooksDetails(session).toString());
        return resultMap;
    }

    public String getAccountBooksDetails(Session session) {

        String classname = "";
        StringBuffer resultHTML = new StringBuffer();
        try {
            resultHTML.append("<FONT SIZE=2>");
            Criteria partyledgerCrit = session.createCriteria(Partyledger.class);           
            partyledgerCrit.addOrder(Order.asc("code"));
            List partyLedgerList = partyledgerCrit.list();
            resultHTML.append("<table cellpadding=\"0\" cellspacing=\"1\" border=\"0\" class=\"display\" id=\"booktable\">");
            resultHTML.append("<thead>");
            resultHTML.append("<tr class=\"gridmenu\">");
            resultHTML.append("<td>S.No</td>");
            resultHTML.append("<td>Region Name</td>");
            resultHTML.append("<td>Party Name</td>");
            resultHTML.append("<td>Modify</td>");
            resultHTML.append("</tr>");
            resultHTML.append("</thead>");
            resultHTML.append("<tbody>");
            if (partyLedgerList.size() > 0) {

                for (int i = 0; i < partyLedgerList.size(); i++) {
                    if (i % 2 == 0) {
                        classname = "rowColor1";
                    } else {
                        classname = "rowColor2";
                    }
                    Partyledger partyledgerObj = (Partyledger) partyLedgerList.get(i);
                    resultHTML.append("<tr class=\"" + classname + "\">");
                    resultHTML.append("<td align=\"center\">" + (i + 1) + "</td>");
                    resultHTML.append("<td align=\"center\">" + partyledgerObj.getRegionmaster().getRegionname() + "</td>");
                    resultHTML.append("<td align=\"left\">" + partyledgerObj.getPartyname() + "</td>");
                    resultHTML.append("<td align=\"center\"><input type=\"radio\" name=\"partycode\" id=\"" + partyledgerObj.getCode() + "\" onclick=\"ModifyPartyName('" + partyledgerObj.getCode() + "','" + partyledgerObj.getRegionmaster().getId() + "','" + partyledgerObj.getPartyname() + "')\"></td>");
                    resultHTML.append("</tr>");
                }

            }
            resultHTML.append("</tbody>");
            resultHTML.append("</table>");
            resultHTML.append("</FONT>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHTML.toString();
    }
    
}
