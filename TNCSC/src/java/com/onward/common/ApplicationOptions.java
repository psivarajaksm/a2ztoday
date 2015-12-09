package com.onward.common;

//import com.onward.persistence.options.LocalbodyReference;
import com.onward.persistence.payroll.Referencecodes;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
//import com.onward.persistence.options.RevenuelocalbodyReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.hibernate.criterion.Order;

public class ApplicationOptions {
//
    private static ApplicationOptions instance;
    private Map hreferenceCodeMap = null;
//    private Map districtMap = null;
//    private Map localbodyMap = null;
//    private Map revenuelocalbodyMap = new HashMap();
//    private Map revenuelocalbodyVillageMap = new HashMap();
//    private Map revenuelocalbodyTalukMap = new HashMap();
//    private Map localbodyDescriptionMap = null;
//    private Map revenuelocalbodyDescriptionMap = null;
//
//    private Map revenueLocalBodyMaps = null;
    private List notInOrderList = new LinkedList();
//    Logger logger = Logger.getLogger("com.onward.common.ApplicationOptions");
//
    private ApplicationOptions() {
        /*notInOrderList.add("31");
        notInOrderList.add("4");
        notInOrderList.add("50");
        notInOrderList.add("49");
        notInOrderList.add("34");*/

        hreferenceCodeMap = getReferenceCodeMap();
//        districtMap = loadDistrictMap();
//        localbodyMap = getLocalbodyReferenceMap("childcode", "1");
//        revenueLocalBodyMaps = getRevenueLocalbodyReferenceMap("childcode", "1");
////        getRevenueLocalbodyVillageReferenceMap("childcode", "32");
//        localbodyDescriptionMap = getLocalbodyReferenceDescriptionMap();
//        revenuelocalbodyDescriptionMap = getRevenueLocalbodyReferenceDescriptionMap();
//        loadFrequentlyUserMap();
    }
//
//    public void reloadLocalBodyReferenceCodes()
//    {
//        localbodyMap = getLocalbodyReferenceMap("childcode", "1");
//        localbodyDescriptionMap = getLocalbodyReferenceDescriptionMap();
//    }
    public final Map getReferenceCodeMap() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        Map resultMap = new LinkedHashMap();
        Criteria crit = session.createCriteria(com.onward.persistence.payroll.Referencecodes.class);
        crit.add(Restrictions.sqlRestriction("childcode='0'"));
        List<Referencecodes> refcodelist = crit.list();
        for (Referencecodes refcodeobj : refcodelist) {
            String parentcode = refcodeobj.getId().getParentcode();
            crit = session.createCriteria(com.onward.persistence.payroll.Referencecodes.class);
            crit.add(Restrictions.sqlRestriction("parentcode ='" + refcodeobj.getId().getParentcode() + "' and childcode!='0'"));
            crit.addOrder(Order.asc("childof"));
            List<Referencecodes> childlist = crit.list();
            for (Referencecodes childobj : childlist) {
                if (resultMap.containsKey(parentcode)) {
                    Map childList = (Map) resultMap.get(parentcode);
                    childList.put(childobj.getId().getChildcode(), childobj.getDescription());
                    resultMap.put(parentcode, childList);
                } else {
                    Map childList = new LinkedHashMap();
                    if (notInOrderList.contains(parentcode)) {
                        childList.put("", "- - - Select One - - -");
                    }
                    childList.put(childobj.getId().getChildcode(), childobj.getDescription());
                    resultMap.put(parentcode, childList);
                }
            }
        }
        return resultMap;
    }

    public Map getOptionMap(String parentcode) {
        if (notInOrderList.contains(parentcode)) {
            return (Map) hreferenceCodeMap.get(parentcode);
        } else {
            return getSortedMap((Map) hreferenceCodeMap.get(parentcode));
        }
    }

    public static synchronized ApplicationOptions getInstance() {
        synchronized (ApplicationOptions.class) {
            ApplicationOptions inst = instance;
            if (inst == null) {
                synchronized (ApplicationOptions.class) {
                    instance = new ApplicationOptions();
                }
            }
        }
        return instance;
    }

    public void clearResources() {
        hreferenceCodeMap = null;
        instance = null;
    }

    public Map getMultipleReferenceCodes(String displayId, String parentCode) {
        Map referenceCodeDetails = new LinkedHashMap();
        Map comboValues = null;
        StringTokenizer displayIdTokenizer = new StringTokenizer(displayId, ",");
        while (displayIdTokenizer.hasMoreElements()) {
            StringTokenizer parentCodeTokenizer = new StringTokenizer(parentCode, ",");
            while (parentCodeTokenizer.hasMoreElements()) {
                comboValues = getOptionMap(parentCodeTokenizer.nextToken());
                referenceCodeDetails.put(displayIdTokenizer.nextToken(), comboValues);
            }
        }
        displayIdTokenizer = null;

        return referenceCodeDetails;
    }
//
//    //Coding added by Thirumalai to increase the response time for the candidate profile jsp page
//    private static Map frequentlyUsedCodes = new LinkedHashMap();
//
//    private void loadFrequentlyUserMap() {
//        String displayId = "gender,maritalstatus,religion,community,caste,category,district,employmentstatus,degree,group_degree,";
//        displayId += "localbody,experience_code,sector_type,experience_gained,certificate_type,typeOfPriority,skillLanguage,skillLevel,";
//        displayId += "courseDetails,knownLanguages,otherCertificateType,technical_medium,technical_institute_type,technical_certificate_type,";
//        displayId += "boardtype,HSCBoardType,BSSLCBoardType,skillsType,degreeClass,stdCode,SSLC_CBSE,shandLevelMap,differentlyAbledType,";
//        displayId += "differentlyAbledPercentage,certifyingauthority,licenceType";
//        //Set the relavant combo Id parent code here.
//        String parentCode = "2,3,4,5,7,8,1,6,23,30,";
//        parentCode += "25,32,33,12,38,34,31,36,";
//        parentCode += "19,31,21,31,20,38,";
//        parentCode += "47,48,49,50,51,52,53,54,55,";
//        parentCode += "18,57,58";
//        frequentlyUsedCodes = getMultipleReferenceCodes(displayId, parentCode);
//    }
//
//    public Map getFrequentlyUsedCodes() {
//        return frequentlyUsedCodes;
//    }

    public static Map getSortedMap(Map map) {
        if (map != null) {
            List entrylist = new LinkedList(map.entrySet());
            Collections.sort(entrylist, new Comparator() {

                public int compare(Object obj1, Object obj2) {
                    return ((Comparable) ((Map.Entry) (obj1)).getValue()).compareTo(((Map.Entry) (obj2)).getValue());
                }
            });

            Map result = new LinkedHashMap();
            result.put("", "- - - - Select One - - - -");
            for (Iterator it = entrylist.iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }
        return null;
    }
//
//    /**
//     * Get the Child Description from Reference Code Table.
//     *
//     * @param parentCode Parent Code
//     * @param childCode Child Code
//     * @return String - Child Description
//     */
//    public String getReferenceDescription(String parentCode, String childCode) {
//        Map refDes = getOptionMap(parentCode);
//        String childDescription = "";
//        if (refDes != null && !refDes.isEmpty() && refDes.size() > 0) {
//            if (childCode != null && !childCode.isEmpty() && childCode.length() > 0) {
//                childDescription = (String) refDes.get(childCode);
//            }
//        }
//        return childDescription;
//    }
//
//    /**
//     *
//     * @param parentCode
//     * @return
//     */
//    public String getRevenueLocalBodyReferenceDescription(String parentCode) {
//        String description = (String) revenuelocalbodyDescriptionMap.get(parentCode);
//        if(description==null)   description = "";
//        return description;
//    }
//    /**
//     *
//     * @param parentCode
//     * @return
//     */
//    public String getLocalBodyReferenceDescription(String parentCode) {
//        String description = (String) localbodyDescriptionMap.get(parentCode);
//        if(description==null)   description = "";
//        return description;
//    }
//
//    public Map getDistrictMap() {
//        return districtMap;
//    }
//
//    public final Map loadDistrictMap() {
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        Session session = factory.openSession();
//        Criteria crit = session.createCriteria(com.onward.persistence.options.LocalbodyReference.class);
//        crit.add(Restrictions.eq("childcode","1"));
//        List<LocalbodyReference> localbodylist = crit.list();
//        districtMap = new TreeMap();
//        for (LocalbodyReference lbobj : localbodylist) {
//            districtMap.put(lbobj.getParentcode(), lbobj.getDescription());
//        }
//        return getSortedMap(districtMap);
//    }
//
//    public Map getLocalbodyMap(String districtcode) {
//        Map resultMap = null;
//        try {
//            if (localbodyMap.get(districtcode) != null) {
//                resultMap = (Map) localbodyMap.get(districtcode);
//            }
//        } catch (NumberFormatException ne) {
//            logger.info(ne.getMessage());
//        }
//        return resultMap;
//    }
//    public Map getLocalBodyRevenueMap(String districtcode) {
//        Map resultMap = null;
//        try {
//            if (revenueLocalBodyMaps.get(districtcode) != null) {
//                resultMap = (Map) revenueLocalBodyMaps.get(districtcode);
//            }
//        } catch (NumberFormatException ne) {
//            logger.info(ne.getMessage());
//        }
//        return resultMap;
//    }
//
//    public final Map getLocalbodyReferenceMap(String fieldname, Object value) {
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        Session session = factory.openSession();
//        Criteria crit = session.createCriteria(com.onward.persistence.options.LocalbodyReference.class);
//        Map resultMap = new LinkedHashMap();
//        //crit.add(Restrictions.eq("childcode", new Long(1)));
//        crit.add(Restrictions.eq(fieldname, value));
//        List<LocalbodyReference> localbodylist = crit.list();
//        for (LocalbodyReference lbobj : localbodylist) {
//            Map directMap = new LinkedHashMap();
//            crit = session.createCriteria(com.onward.persistence.options.LocalbodyReference.class);
//            crit.add(Restrictions.eq("childcode", lbobj.getParentcode()));
//            List<LocalbodyReference> parentlist = crit.list();
//            for (LocalbodyReference childobj : parentlist) {
//                String lbtype = childobj.getEntityType();
//                if (directMap.containsKey(lbtype)) {
//                    Map childList = (Map) directMap.get(lbtype);
//                    childList.put(childobj.getParentcode(), childobj.getDescription());
//                    directMap.put(lbtype, childList);
//                } else {
//                    Map childList = new LinkedHashMap();
//                    childList.put(childobj.getParentcode(), childobj.getDescription());
//                    directMap.put(lbtype, childList);
//                }
//            }
//            //for sorting based on values
//            Set<String> ks = directMap.keySet();
//            for (String key : ks) {
//                directMap.put(key, getSortedMap((Map) directMap.get(key)));
//            }
//            resultMap.put(lbobj.getParentcode(), directMap);
//            crit = null;
//        }
//        return resultMap;
//    }
//    public final Map getRevenueLocalbodyReferenceMap(String fieldname, Object value) {
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        Session session = factory.openSession();
//        Criteria crit = session.createCriteria(com.onward.persistence.options.RevenuelocalbodyReference.class);
//        Map resultMap = new LinkedHashMap();
//        //crit.add(Restrictions.eq("childcode", new Long(1)));
//        crit.add(Restrictions.eq(fieldname, value));
//        List<RevenuelocalbodyReference> localbodylist = crit.list();
//        for (RevenuelocalbodyReference lbobj : localbodylist) {
//            Map directMap = new LinkedHashMap();
//            crit = session.createCriteria(com.onward.persistence.options.RevenuelocalbodyReference.class);
//            crit.add(Restrictions.eq("childcode", lbobj.getParentcode()));
//            List<RevenuelocalbodyReference> parentlist = crit.list();
//            for (RevenuelocalbodyReference childobj : parentlist) {
//                String lbtype = childobj.getEntityType();
//                if (directMap.containsKey(lbtype)) {
//                    Map childList = (Map) directMap.get(lbtype);
//                    childList.put(childobj.getParentcode(), childobj.getDescription());
//                    directMap.put(lbtype, childList);
//                } else {
//                    Map childList = new LinkedHashMap();
//                    childList.put(childobj.getParentcode(), childobj.getDescription());
//                    directMap.put(lbtype, childList);
//                }
//            }
//            //for sorting based on values
//            Set<String> ks = directMap.keySet();
//            for (String key : ks) {
//                directMap.put(key, getSortedMap((Map) directMap.get(key)));
//            }
//            resultMap.put(lbobj.getParentcode(), directMap);
//            crit = null;
//        }
//        return resultMap;
//    }
//
//    public Map getRevenueLocalbodyMap(String talukid) {
//        Map resultMap = null;
//        try {
//            if (revenuelocalbodyMap.get(talukid) != null) {
//                resultMap = (Map) revenuelocalbodyMap.get(talukid);
//            }
//        } catch (NumberFormatException ne) {
//            logger.info(ne.getMessage());
//        }
//        return resultMap;
//    }
//
//    public final void getRevenueLocalbodyVillageReferenceMap(String fieldname, Object value) {
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        Session session = factory.openSession();
//        Criteria crit = session.createCriteria(com.onward.persistence.options.RevenuelocalbodyReference.class);
//        Map resultMap = new LinkedHashMap();
//        crit.add(Restrictions.eq(fieldname, value));
//        Map villagedirectMap = new LinkedHashMap();
//        List<RevenuelocalbodyReference> localbodylist = crit.list();
//        for (RevenuelocalbodyReference lbobj : localbodylist) {
//            Map directMap1 = new LinkedHashMap();
//            crit = session.createCriteria(com.onward.persistence.options.RevenuelocalbodyReference.class);
//            crit.add(Restrictions.eq("childcode", lbobj.getParentcode()));
//            List<RevenuelocalbodyReference> parentlist = crit.list();
//            for (RevenuelocalbodyReference villagelbobj : parentlist) {
//                Map directMap = new LinkedHashMap();
//                crit = session.createCriteria(com.onward.persistence.options.RevenuelocalbodyReference.class);
//                crit.add(Restrictions.eq("childcode", villagelbobj.getParentcode()));
//                List<RevenuelocalbodyReference> villageparentlist = crit.list();
//                for (RevenuelocalbodyReference childobj : villageparentlist) {
//                    String lbtype = childobj.getEntityType();
//                    if (directMap.containsKey(lbtype)) {
//                        Map childList = (Map) directMap.get(lbtype);
//                        childList.put(childobj.getParentcode(), childobj.getDescription());
//                        directMap.put(lbtype, childList);
//                    } else {
//                        Map childList = new LinkedHashMap();
//                        childList.put(childobj.getParentcode(), childobj.getDescription());
//                        directMap.put(lbtype, childList);
//                    }
//                    if (villagedirectMap.containsKey(lbtype) && lbtype.equalsIgnoreCase("V")) {
//                        Map childList = (Map) villagedirectMap.get(lbtype);
//                        childList.put(childobj.getParentcode(), childobj.getDescription());
//                        villagedirectMap.put(lbtype, childList);
//                    } else {
//                        Map childList = new LinkedHashMap();
//                        childList.put(childobj.getParentcode(), childobj.getDescription());
//                        villagedirectMap.put(lbtype, childList);
//                    }
//                }
//                Set<String> ks = directMap.keySet();
//                for (String key : ks) {
//                    directMap.put(key, getSortedMap((Map) directMap.get(key)));
//                }
//                revenuelocalbodyVillageMap.put(villagelbobj.getParentcode(), directMap);
//                String lbtype = villagelbobj.getEntityType();
//                if (directMap1.containsKey(lbtype)) {
//                    Map childList = (Map) directMap1.get(lbtype);
//                    childList.put(villagelbobj.getParentcode(), villagelbobj.getDescription());
//                    directMap1.put(lbtype, childList);
//                } else {
//                    Map childList = new LinkedHashMap();
//                    childList.put(villagelbobj.getParentcode(), villagelbobj.getDescription());
//                    directMap1.put(lbtype, childList);
//                }
//            }
//            Set<String> ks = directMap1.keySet();
//            for (String key : ks) {
//                directMap1.put(key, getSortedMap((Map) directMap1.get(key)));
//                if(key.equalsIgnoreCase("V"))
//                {
//                    villagedirectMap.put(key, getSortedMap((Map) villagedirectMap.get(key)));
//                }
//            }
//            crit = null;
//            revenuelocalbodyMap.put(lbobj.getParentcode(), directMap1);
//            revenuelocalbodyTalukMap.put(lbobj.getParentcode(), villagedirectMap);
//        }
//    }
//
//    public Map getRevenueVillageLocalbodyMap(String blockid) {
//        Map resultMap = null;
//        try {
//            if (revenuelocalbodyVillageMap.get(blockid) != null) {
//                resultMap = (Map) revenuelocalbodyVillageMap.get(blockid);
//            }
//        } catch (NumberFormatException ne) {
//            logger.info(ne.getMessage());
//        }
//        return resultMap;
//    }
//
//    public Map getRevenueVillageLocalbodyTalukMap(String talukid) {
//        Map resultMap = null;
//        try {
//            if (revenuelocalbodyTalukMap.get(talukid) != null) {
//                resultMap = (Map) revenuelocalbodyTalukMap.get(talukid);
//            }
//        } catch (NumberFormatException ne) {
//            logger.info(ne.getMessage());
//        }
//        return resultMap;
//    }
//    /**
//     *
//     * @return
//     */
//    public final Map getRevenueLocalbodyReferenceDescriptionMap() {
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        Session session = factory.openSession();
//        Criteria crit = session.createCriteria(com.onward.persistence.options.RevenuelocalbodyReference.class);
//        Map resultMap = new LinkedHashMap();
//        List<RevenuelocalbodyReference> localbodylist = crit.list();
//        for (RevenuelocalbodyReference lbobj : localbodylist) {
//            resultMap.put(lbobj.getParentcode(), lbobj.getDescription());
//        }
//        return resultMap;
//    }
//    /**
//     *
//     * @return
//     */
//    public final Map getLocalbodyReferenceDescriptionMap() {
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        Session session = factory.openSession();
//        Criteria crit = session.createCriteria(com.onward.persistence.options.LocalbodyReference.class);
//        Map resultMap = new LinkedHashMap();
//        List<LocalbodyReference> localbodylist = crit.list();
//        for (LocalbodyReference lbobj : localbodylist) {
//            resultMap.put(lbobj.getParentcode(), lbobj.getDescription());
//        }
//        return resultMap;
//    }
//
//    public void refreshLocalBodyReferenceMap(){
//        localbodyMap = getLocalbodyReferenceMap("childcode", "1");
//        localbodyDescriptionMap = getLocalbodyReferenceDescriptionMap();
//    }

}
