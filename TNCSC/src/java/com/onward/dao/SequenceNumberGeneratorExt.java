/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.persistence.payroll.Accountingserialno;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author 
 */
public class SequenceNumberGeneratorExt {

    public static synchronized String getMaxPaymentVoucherid(Session session, String regionCode, String voucherType, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getPaymentvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPaymentvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getMaxJournalVoucherid(Session session, String regionCode, String voucherType, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getJournalvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setJournalvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getMaxReceiptVoucherid(Session session, String regionCode, String voucherType, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getReceiptvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setReceiptvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getMaxBankVoucherid(Session session, String regionCode, String voucherType, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getPaymentvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPaymentvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getMaxVoucherdetailsid(Session session, String regionCode, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getVoucherdetails();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setVoucherdetails(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getReceiptpaymentdetailsid(Session session, String regionCode, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getReceiptpaymentdetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setReceiptpaymentdetailsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getMaxSeqNumberInterregionaccounts(Session session, String regionCode, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getInterregionaccountsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setInterregionaccountsid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getMaxSeqNumberInterregionaccountsreconcil(Session session, String regionCode, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getInterregionaccountsreconcil();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setInterregionaccountsreconcil(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }

    public static synchronized String getMaxPayrollVoucherid(Session session, String regionCode, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getPayrollvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPayrollvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }
    
    public static synchronized String getMaxSeqNumberBankChallan(Session session, String regionCode, String period) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Accountingserialno.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            lrCrit.add(Restrictions.sqlRestriction("accountingyearid = '" + period + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Accountingserialno regionmasterObj = (Accountingserialno) ldList.get(0);
                maxNoStr = regionmasterObj.getBankchallanid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setBankchallanid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }
            maxStr = regionCode + maxNoStr + "-" + period;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxStr;
    }
}
