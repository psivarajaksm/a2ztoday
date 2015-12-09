/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.dao;

import com.onward.persistence.payroll.Receiptpaymentdetails;
import com.onward.persistence.payroll.Regionmaster;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sivaraja_p
 */
public class SequenceNumberGenerator {

    public static synchronized String getMaxPaymentVoucherid(Session session, String regionCode, String voucherType) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getPaymentvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPaymentvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public static synchronized String getMaxJournalVoucherid(Session session, String regionCode, String voucherType) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getJournalvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setJournalvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public static synchronized String getMaxReceiptVoucherid(Session session, String regionCode, String voucherType) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getReceiptvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setReceiptvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public static synchronized String getMaxBankVoucherid(Session session, String regionCode, String voucherType) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getPaymentvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPaymentvoucherid(maxNoStr);
                transaction = session.beginTransaction();
                session.update(regionmasterObj);
                transaction.commit();
            }

            maxStr = regionCode + voucherType.toUpperCase() + maxNoStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return maxStr;
    }

    public static synchronized String getMaxVoucherdetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getVoucherdetails();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setVoucherdetails(maxNoStr);
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

    public static synchronized String getReceiptpaymentdetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getReceiptpaymentdetailsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setReceiptpaymentdetailsid(maxNoStr);
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

    public static synchronized String getMaxSeqNumberVatonSales(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getVatonsales();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setVatonsales(maxNoStr);
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

    public static synchronized String getMaxSeqNumberVatonPurchase(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getVatonpurchase();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setVatonpurchase(maxNoStr);
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

    public static synchronized String getMaxSeqNumberBankChallan(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getBankchallanid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setBankchallanid(maxNoStr);
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

    public static synchronized String getMaxSeqNumberAncillaryAdjustAmtid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getAncillaryadjaccid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setAncillaryadjaccid(maxNoStr);
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

    public static synchronized String getMaxHistoryVoucherid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getVoucherhistoryid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setVoucherhistoryid(maxNoStr);
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

    public static synchronized String getMaxHistoryVoucherDetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getVoucherdetailshistoryid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setVoucherdetailshistoryid(maxNoStr);
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

    public static synchronized String getMaxHistoryReceiptPaymentDetailsid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getReceiptpaymentdetailshistoryid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setReceiptpaymentdetailshistoryid(maxNoStr);
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

    public static synchronized String getMaxSeqNumberTaxonCST(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getTaxoncst();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setTaxoncst(maxNoStr);
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

    public static synchronized String getMaxSeqNumberInterregionaccounts(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getInterregionaccountsid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setInterregionaccountsid(maxNoStr);
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

    public static synchronized String getMaxSeqNumberInterregionaccountsreconcil(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getInterregionaccountsreconcil();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setInterregionaccountsreconcil(maxNoStr);
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

    public static synchronized String getMaxPayrollVoucherid(Session session, String regionCode) {
        long maxNoStr = 0;
        String maxStr = "";
        Transaction transaction = null;
        try {
            Criteria lrCrit = session.createCriteria(Regionmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionCode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                Regionmaster regionmasterObj = (Regionmaster) ldList.get(0);
                maxNoStr = regionmasterObj.getPayrollvoucherid();
                maxNoStr = maxNoStr + 1;
                regionmasterObj.setPayrollvoucherid(maxNoStr);
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
}
