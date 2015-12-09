/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.persistence.payroll.*;
import com.onward.valueobjects.UserViewModel;
import java.util.*;
import java.math.BigDecimal;
import org.hibernate.Query;
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
public class UserTypeServiceImpl extends OnwardAction implements UserTypeService {

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getUsertypes(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map usertypeMap = new LinkedHashMap();
        usertypeMap.put("0", "--Select--");
        try {
            Criteria usertypeCrit = session.createCriteria(Usertype.class);
            usertypeCrit.add(Restrictions.sqlRestriction("parentcode=0"));
            usertypeCrit.addOrder(Order.asc("id"));
            List<Usertype> usertypeList = usertypeCrit.list();
            resultMap = new TreeMap();
            for (Usertype lbobj : usertypeList) {
                usertypeMap.put(lbobj.getId(), lbobj.getUsertypename());
            }
            resultMap.put("usertypes", usertypeMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getUsertypesforModify1(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String usertypeid) {
        Map resultMap = new HashMap();
        Map usertypeMap = new LinkedHashMap();
        usertypeMap.put("0", "--Select--");
        try {
            Criteria usertypeCrit = session.createCriteria(Usertype.class);
            usertypeCrit.add(Restrictions.sqlRestriction("parentcode=" + Integer.parseInt(usertypeid)));
            usertypeCrit.addOrder(Order.asc("id"));
            List<Usertype> usertypeList = usertypeCrit.list();
            resultMap = new TreeMap();
            for (Usertype lbobj : usertypeList) {
                usertypeMap.put(lbobj.getId(), lbobj.getUsertypename());
            }
            resultMap.put("usertypes", usertypeMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveUserType(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String funtype, String usertypeid, String usertype) {
        Map resultMap = new HashMap();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Usertype masterobj = new Usertype();
            if (usertypeid.equalsIgnoreCase("0")) {
                masterobj.setId(getSequenceId(session));
            } else {
                masterobj.setId(Integer.parseInt(usertypeid));
            }
            masterobj.setUsertypename(usertype);
            masterobj.setParentcode(0);
            session.saveOrUpdate(masterobj);
            transaction.commit();

            resultMap.put("success", "User Type Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "User Type Transaction Faild");
        }
        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map saveMenuAssign(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String usertypes, String menucodes) {
        Map resultMap = new HashMap();
        String[] strArray = null;
        String delimiter = "-";
        Transaction transaction = null;
        try {

            Criteria menuCrit = session.createCriteria(Menuprivilages.class);
            menuCrit.add(Restrictions.sqlRestriction("usertype=" + Integer.parseInt(usertypes)));
            List menuList = menuCrit.list();
            if (menuList.size() > 0) {
                for (int i = 0; i < menuList.size(); i++) {
                    Menuprivilages menuObj = (Menuprivilages) menuList.get(i);
                    transaction = session.beginTransaction();
                    menuObj.setAccessright(Boolean.FALSE);
                    session.update(menuObj);
                    transaction.commit();
                }

                if (!menucodes.equalsIgnoreCase("") && menucodes != null) {
                    strArray = menucodes.split(java.util.regex.Pattern.quote(delimiter));
                    for (String menuId : strArray) {
                        Criteria menuCrit1 = session.createCriteria(Menuprivilages.class);
                        menuCrit1.add(Restrictions.sqlRestriction("usertype=" + Integer.parseInt(usertypes)));
                        menuCrit1.add(Restrictions.sqlRestriction("menumaster=" + Integer.parseInt(menuId)));
                        List menuList1 = menuCrit1.list();
                        if (menuList1.size() > 0) {
                            transaction = session.beginTransaction();
                            Menuprivilages menuObj = (Menuprivilages) menuList1.get(0);
                            menuObj.setAccessright(Boolean.TRUE);
                            session.update(menuObj);
                            transaction.commit();

                            this.updateParent(session, menuObj.getMenumaster().getParentcode(), menuObj.getUsertype().getId());

                        } else {
                            transaction = session.beginTransaction();

                            Usertype typeobj = new Usertype();
                            typeobj.setId(Integer.parseInt(usertypes));

                            Menumaster menuMasterObj = new Menumaster();
                            menuMasterObj.setCode(Integer.parseInt(menuId));

                            Menuprivilages menuObj = new Menuprivilages();
                            menuObj.setId(String.valueOf(getSequenceNumber(session)));
                            menuObj.setUsertype(typeobj);
                            menuObj.setMenumaster(menuMasterObj);
                            menuObj.setAccessright(Boolean.TRUE);
                            session.saveOrUpdate(menuObj);
                            transaction.commit();

                            this.insertParent(session, Integer.parseInt(menuId), Integer.parseInt(usertypes));
                        }
                    }
                }

            } else {
                Criteria criteria = session.createCriteria(Menumaster.class);
//                criteria.add(Restrictions.ne("parentcode", 0));
                criteria.addOrder(Order.asc("menuorder"));
                List menumasterlist = criteria.list();
                if (menumasterlist.size() > 0) {
                    for (int i = 0; i < menumasterlist.size(); i++) {
                        Menumaster menuMasterObj = (Menumaster) menumasterlist.get(i);

                        transaction = session.beginTransaction();

                        Usertype typeobj = new Usertype();
                        typeobj.setId(Integer.parseInt(usertypes));

                        Menuprivilages menuObj = new Menuprivilages();
                        menuObj.setId(String.valueOf(getSequenceNumber(session)));
                        menuObj.setUsertype(typeobj);
                        menuObj.setMenumaster(menuMasterObj);
                        menuObj.setAccessright(Boolean.FALSE);
                        session.save(menuObj);
                        transaction.commit();
                    }
                }

                if (!menucodes.equalsIgnoreCase("") && menucodes != null) {
                    strArray = menucodes.split(java.util.regex.Pattern.quote(delimiter));
                    for (String menuId : strArray) {
                        Criteria menuCrit1 = session.createCriteria(Menuprivilages.class);
                        menuCrit1.add(Restrictions.sqlRestriction("usertype=" + Integer.parseInt(usertypes)));
                        menuCrit1.add(Restrictions.sqlRestriction("menumaster=" + Integer.parseInt(menuId)));
                        List menuList1 = menuCrit1.list();
                        if (menuList1.size() > 0) {
                            transaction = session.beginTransaction();
                            Menuprivilages menuObj = (Menuprivilages) menuList1.get(0);
                            menuObj.setAccessright(Boolean.TRUE);
                            session.update(menuObj);
                            transaction.commit();

                            this.updateParent(session, menuObj.getMenumaster().getParentcode(), menuObj.getUsertype().getId());

                        } else {
                            transaction = session.beginTransaction();

                            Usertype typeobj = new Usertype();
                            typeobj.setId(Integer.parseInt(usertypes));

                            Menumaster menuMasterObj = new Menumaster();
                            menuMasterObj.setCode(Integer.parseInt(menuId));

                            Menuprivilages menuObj = new Menuprivilages();
                            menuObj.setId(String.valueOf(getSequenceNumber(session)));
                            menuObj.setUsertype(typeobj);
                            menuObj.setMenumaster(menuMasterObj);
                            menuObj.setAccessright(Boolean.TRUE);
                            session.save(menuObj);
                            transaction.commit();
                            this.insertParent(session, Integer.parseInt(menuId), Integer.parseInt(usertypes));
                        }
                    }
                }
            }

            resultMap.put("success", "Successfully Menus Assigned");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            resultMap.put("ERROR", "Menus Assigning Faild");
        }
        return resultMap;
    }

    public void updateParent(Session session, int menuId, int usertypes) {
        Transaction transaction = null;
        try {
            Criteria menuCrit3 = session.createCriteria(Menuprivilages.class);
            menuCrit3.add(Restrictions.sqlRestriction("usertype=" + usertypes));
            menuCrit3.add(Restrictions.sqlRestriction("menumaster=" + menuId));
            List menuList3 = menuCrit3.list();
            if (menuList3.size() > 0) {
                transaction = session.beginTransaction();
                Menuprivilages menuObj3 = (Menuprivilages) menuList3.get(0);
                menuObj3.setAccessright(Boolean.TRUE);
                session.update(menuObj3);
                transaction.commit();

                this.updateParent(session, menuObj3.getMenumaster().getParentcode(), usertypes);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    public void insertParent(Session session, int menuId, int usertypes) {
        Transaction transaction = null;
        try {
            Criteria menuCrit = session.createCriteria(Menumaster.class);
            menuCrit.add(Restrictions.sqlRestriction("code=" + menuId));
            List menuList = menuCrit.list();
            if (menuList.size() > 0) {
                Menumaster menuObj = (Menumaster) menuList.get(0);

                Criteria menuCrit1 = session.createCriteria(Menuprivilages.class);
                menuCrit1.add(Restrictions.sqlRestriction("usertype=" + usertypes));
                menuCrit1.add(Restrictions.sqlRestriction("menumaster=" + menuObj.getParentcode()));
                List menuList1 = menuCrit1.list();
                if (menuList1.size() <= 0) {
                    transaction = session.beginTransaction();

                    Usertype typeobj = new Usertype();
                    typeobj.setId(usertypes);

                    Menumaster menuMasterObj = new Menumaster();
                    menuMasterObj.setCode(menuObj.getParentcode());

                    Menuprivilages menuObj2 = new Menuprivilages();
                    menuObj2.setId(String.valueOf(getSequenceNumber(session)));
                    menuObj2.setUsertype(typeobj);
                    menuObj2.setMenumaster(menuMasterObj);
                    menuObj2.setAccessright(Boolean.TRUE);
                    session.save(menuObj2);
                    transaction.commit();

                    this.insertParent(session, menuObj.getParentcode(), usertypes);
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getAssignedMenus(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String usertypes) {
        Map resultMap = new HashMap();
        Map menuMap = new LinkedHashMap();
        try {
            Criteria menuCrit = session.createCriteria(Menuprivilages.class);
            menuCrit.add(Restrictions.sqlRestriction("usertype=" + Integer.parseInt(usertypes)));
            menuCrit.add(Restrictions.sqlRestriction("accessright is true"));
            List menuList = menuCrit.list();
            if (menuList.size() > 0) {
                for (int i = 0; i < menuList.size(); i++) {
                    Menuprivilages menuObj = (Menuprivilages) menuList.get(i);

                    Criteria criteria = session.createCriteria(Menumaster.class);
                    criteria.add(Restrictions.ne("parentcode", 0));
                    criteria.add(Restrictions.eq("code", menuObj.getMenumaster().getCode()));
                    criteria.add(Restrictions.ne("methodname", ""));
//                        criteria.add(Restrictions.sqlRestriction("code=" + menuObj.getMenumaster().getCode()+" and parentcode!=0"));
                    List menulist = criteria.list();
                    if (menulist.size() > 0) {
                        menuMap.put("accessright" + menuObj.getMenumaster().getCode(), "accessright" + menuObj.getMenumaster().getCode());
                    }

                }
                resultMap.put("menuMap", menuMap);
            } else {
                resultMap.put("ERROR", "Not Assigned");
            }

        } catch (Exception e) {
        }


        return resultMap;
    }

    public synchronized int getSequenceId(Session session) {
        int maxSequenceNumber = 1;

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(id as int)) as maxsequencenumber from usertype");
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

        return maxSequenceNumber;
    }

    public int getSequenceNumber(Session session) {
        int maxSequenceNumber = 1;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select max(cast(id as INTEGER)) as maxsequencenumber from menuprivilages");
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
        return maxSequenceNumber;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map getUserTypeSize(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        String isAccountModule = "No";
        Map financialYearMap = new LinkedHashMap();
        financialYearMap.put("0", "--Select--");
        UserViewModel userObj = (UserViewModel) request.getSession(false).getAttribute("userDetails");

        Criteria rcCriteria = session.createCriteria(Useroperatingrights.class);
        rcCriteria.add(Restrictions.sqlRestriction("userid = '" + userObj.getUserid() + "'"));
        rcCriteria.add(Restrictions.sqlRestriction("accessright is true"));
        rcCriteria.addOrder(Order.asc("usertypeid"));
        List rcList = rcCriteria.list();
        resultMap.put("typesize", rcList.size());


        Criteria fyearsCriteria = session.createCriteria(Accountingyear.class);
        List fyearsList = fyearsCriteria.list();
        if (fyearsList.size() > 0) {
            for (int i = 0; i < fyearsList.size(); i++) {
                Accountingyear accountingyearObj = (Accountingyear) fyearsList.get(i);
                financialYearMap.put(accountingyearObj.getId(), (accountingyearObj.getStartyear() + "-" + accountingyearObj.getEndyear()));
            }
        }
        resultMap.put("financialYearMap", financialYearMap);

        if (rcList.size() == 1) {
            Useroperatingrights useroperatingrightsObj = (Useroperatingrights) rcList.get(0);


            Criteria privileageCriteria = session.createCriteria(Menuprivilages.class);
            privileageCriteria.add(Restrictions.sqlRestriction("usertype = " + useroperatingrightsObj.getUsertypeid()));
            privileageCriteria.add(Restrictions.sqlRestriction("accessright is true"));
            List privileageList = privileageCriteria.list();
            if (privileageList.size() > 0) {
                for (int j = 0; j < privileageList.size(); j++) {
                    Menuprivilages menuprivilagesObj = (Menuprivilages) privileageList.get(j);
//                    System.out.println("menuprivilagesObj.getMenumaster().getGrouptype()==" + menuprivilagesObj.getMenumaster().getGrouptype());
                    if (menuprivilagesObj.getMenumaster().getGrouptype() == null) {
                    } else if (menuprivilagesObj.getMenumaster().getGrouptype() == 'A') {
                        isAccountModule = "Yes";
                        break;
                    }

                }
            }
        }
        resultMap.put("isAccountModule", isAccountModule);

        return resultMap;
    }

    @GlobalDBOpenCloseAndUserPrivilages
    public Map loadRegionDetails(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser) {
        Map resultMap = new HashMap();
        Map regionMap = new LinkedHashMap();
        regionMap.put("0", "----Select One----");
        String regionid = "";
        String regionname = "";
        try {
            Criteria rgnCrit = session.createCriteria(Regionmaster.class);
            rgnCrit.add(Restrictions.sqlRestriction("defaultregion is true"));
            rgnCrit.addOrder(Order.asc("regionname"));
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
    public LinkedList duplicatePaySlipPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String billtype) {
//        System.out.println("***************************** UserTypeServiceImpl class duplicatePaySlipPrint method is calling ********************************");
        Map map = new HashMap();
        Map cardDetails = null;
        String regionname = "";
        String employeename = "";
        String designationname = "";
        String billingtype = "";
        LinkedList dupslipprintlist = new LinkedList();
        StringBuffer dupslipquery = new StringBuffer();
        try {

            if (!epfno.trim().equalsIgnoreCase("null") && epfno.trim().length() > 0) {
                
                regionname = getRegionmaster(session, LoggedInRegion).getRegionname();
                employeename = getEmployeemaster(session, epfno, LoggedInRegion).getEmployeename();
                designationname = getDesignationMater(session, getEmployeemaster(session, epfno, LoggedInRegion).getDesignation()).getDesignation();

                if (billtype.equalsIgnoreCase("1")) {
                    dupslipquery.append(" select paycode,paycodename,earnings,deduction,loan as loanbalance,nthinstallment,totalinstallment from (  ");
                    dupslipquery.append(" (select pm.paycode,pm.paycodename,cast(et.amount as text) as earnings,'' as deduction,'' as loan  ,'' as nthinstallment  ,'' as totalinstallment    ");
                    dupslipquery.append(" from payrollprocessingdetails ppd  ");
                    dupslipquery.append(" left join employeeearningstransactions et on et.payrollprocessingdetailsid=ppd.id   ");
                    dupslipquery.append(" join paycodemaster pm on pm.paycode=et.earningmasterid   ");
                    dupslipquery.append(" where ppd.employeeprovidentfundnumber='" + epfno + "'  and ppd.month =" + month + " and ppd.year=" + year + "   ");
                    dupslipquery.append(" and  et.cancelled is false   ");
                    dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,deduction,loan,nthinstallment,totalinstallment  ");
                    dupslipquery.append(" order by pm.paycode)  ");
                    dupslipquery.append(" union  all(    ");
                    dupslipquery.append(" select pm.paycode,pm.paycodename,'' as earnings,cast(dt.amount as text) as deduction,coalesce(cast(el.loanbalance  as text),'') as loan  ");
                    dupslipquery.append(" ,coalesce(cast(el.nthinstallment as text),'') as nthinstallment  ");
                    dupslipquery.append(" ,coalesce(cast(el.totalinstallment as text),'') as totalinstallment  ");
                    dupslipquery.append(" from payrollprocessingdetails ppd   ");
                    dupslipquery.append(" join employeedeductionstransactions dt on dt.payrollprocessingdetailsid=ppd.id  ");
                    dupslipquery.append(" join paycodemaster pm on   pm.paycode=dt.deductionmasterid  ");
                    dupslipquery.append(" left  join (select payrollprocessingdetailsid,deductioncode,nthinstallment,totalinstallment,eld.loanbalance from employeeloansandadvancesdetails eld ");
                    dupslipquery.append(" join  employeeloansandadvances as la on la.id=eld.employeeloansandadvancesid ");
                    dupslipquery.append(" where la.employeeprovidentfundnumber='" + epfno + "') as el on el.payrollprocessingdetailsid=ppd.id and   el.deductioncode=dt.deductionmasterid   ");

                    //                dupslipquery.append(" left join employeeloansandadvances ln on ln.employeeprovidentfundnumber='"+epfno+"'  and ln.deductioncode=dt.deductionmasterid   and ln.id!='R01334'  ");
                    //                dupslipquery.append(" left join employeeloansandadvancesdetails  ld on ld.employeeloansandadvancesid=ln.id and ld.payrollprocessingdetailsid=ppd.id  ");
                    dupslipquery.append(" where ppd.employeeprovidentfundnumber='" + epfno + "'  and ppd.month =" + month + " and ppd.year=" + year + "   ");
                    dupslipquery.append(" and  dt.cancelled is false  ");
                    dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,deduction,loan,el.nthinstallment,el.totalinstallment  ");
                    dupslipquery.append(" order by pm.paycode)) as x  ");
                } else if (billtype.equalsIgnoreCase("2")) {
                        billingtype = "SUPLEMENTARYBILL";
                        dupslipquery.append(" select paycode,paycodename,earnings,deduction,loan as loanbalance,nthinstallment,totalinstallment from (  ");
                        dupslipquery.append(" (select pm.paycode,pm.paycodename,cast(sum(et.amount) as text) as earnings,'' as deduction,'' as loan  ,'' as nthinstallment    ");
                        dupslipquery.append(" ,'' as totalinstallment    from supplementatypaybill sb ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeeearningstransactions et on et.supplementarypayrollprocessingdetailsid=sppd.id   ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=et.earningmasterid   ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='" + billingtype + "' and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,et.amount,deduction,loan,nthinstallment,totalinstallment  ");
                        dupslipquery.append(" order by pm.paycode)  ");
                        dupslipquery.append(" union  all(    ");
                        dupslipquery.append(" select pm.paycode,pm.paycodename,'' as earnings,cast(sum(dt.amount) as text) as deduction,coalesce(cast(el.loanbalance  as text),'') as loan  ");
                        dupslipquery.append(" ,coalesce(cast(el.nthinstallment as text),'') as nthinstallment  ");
                        dupslipquery.append(" ,coalesce(cast(el.totalinstallment as text),'') as totalinstallment  ");
                        dupslipquery.append(" from supplementatypaybill sb   ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeedeductionstransactions dt on dt.supplementarypayrollprocessingdetailsid=sppd.id  ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=dt.deductionmasterid ");
                        dupslipquery.append(" left  join (select supplementarypayrollprocessingdetailsid,deductioncode,nthinstallment,totalinstallment,eld.loanbalance  ");
                        dupslipquery.append(" from supplementaryemployeeloansandadvancesdetails eld  join  employeeloansandadvances as la on la.id=eld.employeeloansandadvancesid   ");
                        dupslipquery.append(" where la.employeeprovidentfundnumber='" + epfno + "') as el on el.supplementarypayrollprocessingdetailsid=sppd.id and   el.deductioncode=dt.deductionmasterid    ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='" + billingtype + "'  and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,dt.amount,loan,el.nthinstallment,el.totalinstallment  ");
                        dupslipquery.append(" order by pm.paycode)) as x  ");
                } else if (billtype.equalsIgnoreCase("3")) {
                        dupslipquery.append(" select paycode,paycodename,earnings,deduction,loan as loanbalance,nthinstallment,totalinstallment from (  ");
                        dupslipquery.append(" (select pm.paycode,pm.paycodename,cast(sum(et.amount) as text) as earnings,'' as deduction,'' as loan  ,'' as nthinstallment    ");
                        dupslipquery.append(" ,'' as totalinstallment    from supplementatypaybill sb ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeeearningstransactions et on et.supplementarypayrollprocessingdetailsid=sppd.id   ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=et.earningmasterid   ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='LEAVESURRENDER' and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,et.amount,deduction,loan,nthinstallment,totalinstallment  ");
                        dupslipquery.append(" order by pm.paycode)  ");
                        dupslipquery.append(" union  all(    ");
                        dupslipquery.append(" select pm.paycode,pm.paycodename,'' as earnings,cast(sum(dt.amount) as text) as deduction,coalesce(cast(el.loanbalance  as text),'') as loan  ");
                        dupslipquery.append(" ,coalesce(cast(el.nthinstallment as text),'') as nthinstallment  ");
                        dupslipquery.append(" ,coalesce(cast(el.totalinstallment as text),'') as totalinstallment  ");
                        dupslipquery.append(" from supplementatypaybill sb   ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeedeductionstransactions dt on dt.supplementarypayrollprocessingdetailsid=sppd.id  ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=dt.deductionmasterid ");
                        dupslipquery.append(" left  join (select supplementarypayrollprocessingdetailsid,deductioncode,nthinstallment,totalinstallment,eld.loanbalance  ");
                        dupslipquery.append(" from supplementaryemployeeloansandadvancesdetails eld  join  employeeloansandadvances as la on la.id=eld.employeeloansandadvancesid   ");
                        dupslipquery.append(" where la.employeeprovidentfundnumber='" + epfno + "') as el on el.supplementarypayrollprocessingdetailsid=sppd.id and   el.deductioncode=dt.deductionmasterid    ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='LEAVESURRENDER'  and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,dt.amount,loan,el.nthinstallment,el.totalinstallment  ");
                        dupslipquery.append(" order by pm.paycode)) as x  ");
//                        billingtype="LEAVESURRENDER";
                    }


                Query ledgerQuery = session.createSQLQuery(dupslipquery.toString());
                List ledgerList = ledgerQuery.list();

                for (ListIterator its = ledgerList.listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();

                    cardDetails = new HashMap();
                    cardDetails.put("paycode", (String) rows[0]);
                    cardDetails.put("paycodename", (String) rows[1]);
                    cardDetails.put("earnings", (String) rows[2]);
                    cardDetails.put("deduction", (String) rows[3]);
                    cardDetails.put("loanbalance", (String) rows[4]);
                    cardDetails.put("nthinstallment", (String) rows[5]);
                    cardDetails.put("totalinstallment", (String) rows[6]);
                    cardDetails.put("regionname", regionname);
                    cardDetails.put("empname", employeename);
                    cardDetails.put("designation", designationname);
                    
                    dupslipprintlist.add(cardDetails);
                }
            }
            
        } catch (Exception ex) {
            map.put("ERROR", "duplicatePaySlipPrint Report Generated Error");
            ex.printStackTrace();
        }
        return dupslipprintlist;
    }
    @GlobalDBOpenCloseAndUserPrivilages
    public Map LPCPrint(Session session, HttpServletRequest request, HttpServletResponse response, String LoggedInRegion, String LoggedInUser, String epfno, String month, String year, String billtype) {
        Map map = new HashMap();
        Map cardDetails = null;
        Map reportMap = new HashMap();
        String regionname = "";
        String employeename = "";
        String designationname = "";
        String billingtype = "";
        LinkedList dupslipprintlist = new LinkedList();
        LinkedList dupslipprintloanlist = new LinkedList();
        StringBuffer dupslipquery = new StringBuffer();
        try {

            if (!epfno.trim().equalsIgnoreCase("null") && epfno.trim().length() > 0) {                
                regionname = getRegionmaster(session, LoggedInRegion).getRegionname();
                employeename = getEmployeemaster(session, epfno, LoggedInRegion).getEmployeename();
                designationname = getDesignationMater(session, getEmployeemaster(session, epfno, LoggedInRegion).getDesignation()).getDesignation();

                if (billtype.equalsIgnoreCase("1")) {
                    dupslipquery.append(" select paycode,paycodename,coalesce(earnings,'0') as earnings,coalesce(deduction,'0') as deduction,loan as loanbalance,nthinstallment,totalinstallment,loanamount from (  ");
                    dupslipquery.append(" (select pm.paycode,pm.paycodename,cast(et.amount as text) as earnings,'' as deduction,'' as loan  ,'' as nthinstallment  ,'' as totalinstallment,'' as loanamount  ");
                    dupslipquery.append(" from payrollprocessingdetails ppd  ");
                    dupslipquery.append(" left join employeeearningstransactions et on et.payrollprocessingdetailsid=ppd.id   ");
                    dupslipquery.append(" join paycodemaster pm on pm.paycode=et.earningmasterid   ");
                    dupslipquery.append(" where ppd.employeeprovidentfundnumber='" + epfno + "'  and ppd.month =" + month + " and ppd.year=" + year + "   ");
                    dupslipquery.append(" and  et.cancelled is false   ");
                    dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,deduction,loan,nthinstallment,totalinstallment  ");
                    dupslipquery.append(" order by pm.paycode)  ");
                    dupslipquery.append(" union  all(    ");
                    dupslipquery.append(" select pm.paycode,pm.paycodename,'' as earnings,cast(dt.amount as text) as deduction,coalesce(cast(el.loanbalance  as text),'') as loan  ");
                    dupslipquery.append(" ,coalesce(cast(el.nthinstallment as text),'') as nthinstallment  ");
                    dupslipquery.append(" ,coalesce(cast(el.totalinstallment as text),'') as totalinstallment  ");
                    dupslipquery.append(" ,coalesce(cast(el.loanamount  as text),'') as loanamount  ");
                    dupslipquery.append(" from payrollprocessingdetails ppd   ");
                    dupslipquery.append(" join employeedeductionstransactions dt on dt.payrollprocessingdetailsid=ppd.id  ");
                    dupslipquery.append(" join paycodemaster pm on   pm.paycode=dt.deductionmasterid  ");
                    dupslipquery.append(" left  join (select payrollprocessingdetailsid,deductioncode,nthinstallment,totalinstallment,eld.loanbalance,la.loanamount from employeeloansandadvancesdetails eld ");
                    dupslipquery.append(" join  employeeloansandadvances as la on la.id=eld.employeeloansandadvancesid ");
                    dupslipquery.append(" where la.employeeprovidentfundnumber='" + epfno + "') as el on el.payrollprocessingdetailsid=ppd.id and   el.deductioncode=dt.deductionmasterid   ");
                    dupslipquery.append(" where ppd.employeeprovidentfundnumber='" + epfno + "'  and ppd.month =" + month + " and ppd.year=" + year + "   ");
                    dupslipquery.append(" and  dt.cancelled is false  ");
                    dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,deduction,loan,el.nthinstallment,el.totalinstallment,el.loanamount  ");
                    dupslipquery.append(" order by pm.paycode)) as x  ");
                } else if (billtype.equalsIgnoreCase("2")) {
                        billingtype = "SUPLEMENTARYBILL";
                        dupslipquery.append(" select paycode,paycodename,earnings,deduction,loan as loanbalance,nthinstallment,totalinstallment,loanamount from (  ");
                        dupslipquery.append(" (select pm.paycode,pm.paycodename,cast(sum(et.amount) as text) as earnings,'' as deduction,'' as loan  ,'' as nthinstallment    ");
                        dupslipquery.append(" ,'' as totalinstallment,'' as loanamount from supplementatypaybill sb ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeeearningstransactions et on et.supplementarypayrollprocessingdetailsid=sppd.id   ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=et.earningmasterid   ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='" + billingtype + "' and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,et.amount,deduction,loan,nthinstallment,totalinstallment  ");
                        dupslipquery.append(" order by pm.paycode)  ");
                        dupslipquery.append(" union  all(    ");
                        dupslipquery.append(" select pm.paycode,pm.paycodename,'' as earnings,cast(sum(dt.amount) as text) as deduction,coalesce(cast(el.loanbalance  as text),'') as loan  ");
                        dupslipquery.append(" ,coalesce(cast(el.nthinstallment as text),'') as nthinstallment  ");
                        dupslipquery.append(" ,coalesce(cast(el.totalinstallment as text),'') as totalinstallment  ");
                        dupslipquery.append(" ,coalesce(cast(el.loanamount  as text),'') as loanamount ");
                        dupslipquery.append(" from supplementatypaybill sb   ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeedeductionstransactions dt on dt.supplementarypayrollprocessingdetailsid=sppd.id  ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=dt.deductionmasterid ");
                        dupslipquery.append(" left  join (select supplementarypayrollprocessingdetailsid,deductioncode,nthinstallment,totalinstallment,eld.loanbalance ,la.loanamount ");
                        dupslipquery.append(" from supplementaryemployeeloansandadvancesdetails eld  join  employeeloansandadvances as la on la.id=eld.employeeloansandadvancesid   ");
                        dupslipquery.append(" where la.employeeprovidentfundnumber='" + epfno + "') as el on el.supplementarypayrollprocessingdetailsid=sppd.id and   el.deductioncode=dt.deductionmasterid    ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='" + billingtype + "'  and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,dt.amount,loan,el.nthinstallment,el.totalinstallment,el.loanamount  ");
                        dupslipquery.append(" order by pm.paycode)) as x  ");
                } else if (billtype.equalsIgnoreCase("3")) {
                        dupslipquery.append(" select paycode,paycodename,earnings,deduction,loan as loanbalance,nthinstallment,totalinstallment,loanamount from (  ");
                        dupslipquery.append(" (select pm.paycode,pm.paycodename,cast(sum(et.amount) as text) as earnings,'' as deduction,'' as loan  ,'' as nthinstallment    ");
                        dupslipquery.append(" ,'' as totalinstallment,'' as loanamount  from supplementatypaybill sb ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeeearningstransactions et on et.supplementarypayrollprocessingdetailsid=sppd.id   ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=et.earningmasterid   ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='LEAVESURRENDER' and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,et.amount,deduction,loan,nthinstallment,totalinstallment  ");
                        dupslipquery.append(" order by pm.paycode)  ");
                        dupslipquery.append(" union  all(    ");
                        dupslipquery.append(" select pm.paycode,pm.paycodename,'' as earnings,cast(sum(dt.amount) as text) as deduction,coalesce(cast(el.loanbalance  as text),'') as loan  ");
                        dupslipquery.append(" ,coalesce(cast(el.nthinstallment as text),'') as nthinstallment  ");
                        dupslipquery.append(" ,coalesce(cast(el.totalinstallment as text),'') as totalinstallment  ");
                        dupslipquery.append(" ,coalesce(cast(el.loanamount  as text),'') as loanamount ");
                        dupslipquery.append(" from supplementatypaybill sb   ");
                        dupslipquery.append(" join supplementarypayrollprocessingdetails  sppd on sb.id= sppd.supplementatypaybillid and sppd.cancelled is false  ");
                        dupslipquery.append(" left join supplementaryemployeedeductionstransactions dt on dt.supplementarypayrollprocessingdetailsid=sppd.id  ");
                        dupslipquery.append(" join paycodemaster pm on pm.paycode=dt.deductionmasterid ");
                        dupslipquery.append(" left  join (select supplementarypayrollprocessingdetailsid,deductioncode,nthinstallment,totalinstallment,eld.loanbalance,la.loanamount ");
                        dupslipquery.append(" from supplementaryemployeeloansandadvancesdetails eld  join  employeeloansandadvances as la on la.id=eld.employeeloansandadvancesid   ");
                        dupslipquery.append(" where la.employeeprovidentfundnumber='" + epfno + "') as el on el.supplementarypayrollprocessingdetailsid=sppd.id and   el.deductioncode=dt.deductionmasterid    ");
                        dupslipquery.append(" where sb.employeeprovidentfundnumber='" + epfno + "' and sb.type='LEAVESURRENDER'  and EXTRACT(MONTH FROM date)=" + month + " and EXTRACT(YEAR FROM date)=" + year + "   ");
                        dupslipquery.append(" group by pm.paycode,pm.paycodename,earnings,dt.amount,loan,el.nthinstallment,el.totalinstallment,el.loanamount ");
                        dupslipquery.append(" order by pm.paycode)) as x  ");
                    }
                Query ledgerQuery = session.createSQLQuery(dupslipquery.toString());
                List ledgerList = ledgerQuery.list();
                int earrowid = 1;
                int dedrowid = 1;
                int lonrowid = 1;
                Map earDetails = new HashMap();
                Map dedDetails = new HashMap();
                Map lonDetails = new HashMap();
                for (ListIterator its = ledgerList.listIterator(); its.hasNext();) {
                    Object[] rows = (Object[]) its.next();
                    String paycode = (String) rows[0];                    
                    cardDetails = new HashMap();
                    if (paycode.startsWith("E")) {
                        earDetails.put("row" + earrowid, (String) rows[1] + "~" + (String) rows[2]);
                        earrowid++;
                    } else if (paycode.startsWith("D")) {
                        dedDetails.put("row" + dedrowid, (String) rows[1] + "~" + (String) rows[3]);
                        dedrowid++;
                    } else if (paycode.startsWith("L")) {
                        lonDetails.put("row" + lonrowid, (String) rows[0] + "~" +(String) rows[1] + "~" + (String) rows[2] + "~" + (String) rows[3] + "~" + (String) rows[4] + "~" + (String) rows[5]+ "~" + (String) rows[6]+ "~" + (String) rows[7]);
                        lonrowid++;
                    }
                }
                int earsize = earDetails.size();
                int dedsize = dedDetails.size();                
                int size = 0;
                if (earsize <= dedsize) {
                    size = dedsize;
                } else {
                    size = earsize;
                }
                for (int loop = 1; loop < size; loop++) {
                    cardDetails = new HashMap();
                    if (earDetails.get("row" + loop) != null) {
                        String e[] = ((String) earDetails.get("row" + loop)).split("~");
                        cardDetails.put("paycodenameear", e[0]);
                        cardDetails.put("earnings", e[1]);
                    }
                    if (dedDetails.get("row" + loop) != null) {
                        String d[] = ((String) dedDetails.get("row" + loop)).split("~");
                        cardDetails.put("paycodenameded", d[0]);
                        cardDetails.put("deduction", d[1]);
                    }
                    cardDetails.put("regionname", regionname);
                    cardDetails.put("empname", employeename);
                    cardDetails.put("designation", designationname);
                    dupslipprintlist.add(cardDetails);
                }
                for (int loop = 1; loop <= lonDetails.size(); loop++) {
                    if (lonDetails.get("row" + loop) != null) {
                        cardDetails = new HashMap();
                        String l[] = ((String) lonDetails.get("row" + loop)).split("~");
                        cardDetails.put("paycodenamelon", l[1]);
                        cardDetails.put("deductionlon", l[2]);
                        cardDetails.put("loanbalance", Double.parseDouble(l[3]));
                        cardDetails.put("nthinstallment", Double.parseDouble(l[4]));
                        cardDetails.put("totalinstallment", Double.parseDouble(l[5]));
                        cardDetails.put("totalinstallment1", Double.parseDouble(l[6]));
                        cardDetails.put("loanamount", Double.parseDouble(l[7]));
                        cardDetails.put("regionname", regionname);
                        cardDetails.put("empname", employeename);
                        cardDetails.put("designation", designationname);
                        dupslipprintloanlist.add(cardDetails);
                    }
                }
                reportMap.put("list",dupslipprintlist);
                reportMap.put("loanmap",dupslipprintloanlist);
                reportMap.put("regionname",regionname);
            }
        } catch (Exception ex) {
            map.put("ERROR", "duplicatePaySlipPrint Report Generated Error");
            ex.printStackTrace();
        }
        return reportMap;
    }
}
