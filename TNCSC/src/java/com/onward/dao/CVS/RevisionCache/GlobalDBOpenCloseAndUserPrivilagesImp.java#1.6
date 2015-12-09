/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.action.OnwardAction;
import com.onward.common.HibernateUtil;
import com.onward.persistence.payroll.Regionmaster;
import com.onward.persistence.payroll.Usermaster;
import com.onward.persistence.payroll.Usertype;
import com.onward.valueobjects.UserViewModel;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sivaraja. P
 */
public class GlobalDBOpenCloseAndUserPrivilagesImp extends OnwardAction implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        SessionFactory _factory = HibernateUtil.getSessionFactory();        
        Session hibernate_session = _factory.openSession();
        invocation.getArguments()[0] = hibernate_session;
        HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[1];
        UserViewModel userObj = (UserViewModel) request.getSession(false).getAttribute("userDetails");
        String userid = userObj.getUserid();
        invocation.getArguments()[4] = userid;
        invocation.getArguments()[3] = getMyRegionCode(userid);
        try {
            result = invocation.proceed();
        } catch (Exception e) {
        } finally {
            hibernate_session.close();
        }

        return result;
    }

    public String getMyRegionCode(String userid) {
        String regionCode = "";
        SessionFactory _factory = HibernateUtil.getSessionFactory();
        Session hibernate_session = _factory.openSession();
        Criteria empRegionCrit = hibernate_session.createCriteria(Regionmaster.class);
        empRegionCrit.add(Restrictions.sqlRestriction("defaultregion is true"));
        List empRegionList = empRegionCrit.list();
        if (empRegionList.size() > 0) {
            if (empRegionList.size() == 1) {
                Regionmaster regionmasterObj = (Regionmaster) empRegionList.get(0);
                regionCode = regionmasterObj.getId();
            } else {
                Criteria empUserCrit = hibernate_session.createCriteria(Usermaster.class);
                empUserCrit.add(Restrictions.sqlRestriction("userid='" + userid + "'"));
                List empUserList = empUserCrit.list();
                if (empUserList.size() > 0) {
                    Usermaster userObj = (Usermaster) empUserList.get(0);
                    regionCode = userObj.getRegion();
                }
            }
        }
        hibernate_session.close();
        return regionCode;
    }
}
