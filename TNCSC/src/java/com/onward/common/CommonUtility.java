/* * To change this template, choose Tools | Templates * and open the template in the editor.  */package com.onward.common;  import com.onward.persistence.payroll.*;import com.onward.valueobjects.MonthValueObject;import java.util.ArrayList;import java.util.List;import org.hibernate.Criteria;import org.hibernate.Session;import org.hibernate.criterion.Restrictions; /** * * * * @author onwarduser * */public class CommonUtility {     public static byte[] getPostgres9ByteaFormat(byte[] byteValue) {        String temp = new String(byteValue);        byte[] formatedByteValue;//                    temp = temp.substring(1);        boolean isHex = temp.matches("[0-9A-Fa-f]+");        if (isHex) {            formatedByteValue = hexStringToByteArray(temp);        } else {            formatedByteValue = byteValue;        }        return formatedByteValue;    }    public static byte[] getPostgres9ByteaFormatForHiber(byte[] byteValue) {        String temp = new String(byteValue);        byte[] formatedByteValue;        temp = temp.substring(1);        boolean isHex = temp.matches("[0-9A-Fa-f]+");        System.out.println("isHex = " + isHex);        if (isHex) {            formatedByteValue = hexStringToByteArray(temp);        } else {            formatedByteValue = byteValue;        }        return formatedByteValue;    }    public static byte[] hexStringToByteArray(String hexValue) {//        String s = "";//  String hex1 = Integer.toHexString(str.charAt(0));//  s = hex1 + str.substring(1);//        s = str;        int len = hexValue.length();        byte[] data = new byte[len / 2];        for (int i = 0; i < len; i += 2) {            data[i / 2] = (byte) ((Character.digit(hexValue.charAt(i), 16) << 4)                    + Character.digit(hexValue.charAt(i + 1), 16));        }        return data;    }    public static String convertByteToHexString(byte[] byteValue) {        StringBuffer hexString = new StringBuffer();        for (int i = 0; i < byteValue.length; i++) {            String str = Integer.toHexString(0xFF & byteValue[i]);            if (str.length() == 1) {                str = "0" + str;            }            System.out.println("Integer.toHexString(0xFF & byteValue[i]) = " + str);            hexString.append(str);        }        return hexString.toString();    }    public static Regionmaster getRegion(Session session, String regionCode) {        Regionmaster regionmasterObj = null;        Criteria empRegionCrit = session.createCriteria(Regionmaster.class);        empRegionCrit.add(Restrictions.sqlRestriction("id='" + regionCode + "'"));        List empRegionList = empRegionCrit.list();        if (empRegionList.size() > 0) {            regionmasterObj = (Regionmaster) empRegionList.get(0);        }        return regionmasterObj;    }    public static String getRegionIRSCode(Session session, String regionCode) {        Regionmaster regionmasterObj = null;        String irscode = "";        Criteria empRegionCrit = session.createCriteria(Regionmaster.class);        empRegionCrit.add(Restrictions.sqlRestriction("id='" + regionCode + "'"));        List empRegionList = empRegionCrit.list();        if (empRegionList.size() > 0) {            regionmasterObj = (Regionmaster) empRegionList.get(0);            irscode = regionmasterObj.getIrscode();        }        return irscode;    }    public static Regionmaster getRegionByIRSCode(Session session, String irscode) {        Regionmaster regionmasterObj = null;        Criteria empRegionCrit = session.createCriteria(Regionmaster.class);        empRegionCrit.add(Restrictions.sqlRestriction("irscode='" + irscode + "'"));        List empRegionList = empRegionCrit.list();        if (empRegionList.size() > 0) {            regionmasterObj = (Regionmaster) empRegionList.get(0);        }        return regionmasterObj;    }    public static Sectionmaster getSection(Session session, String code) {        Sectionmaster sectionmasterObj = null;        Criteria empRegionCrit = session.createCriteria(Sectionmaster.class);        empRegionCrit.add(Restrictions.sqlRestriction("id='" + code + "'"));        List empRegionList = empRegionCrit.list();        if (empRegionList.size() > 0) {            sectionmasterObj = (Sectionmaster) empRegionList.get(0);        }        return sectionmasterObj;    }    public static Bankledger getBankledger(Session session, String id) {        Bankledger bankledgerObj = null;        Criteria bankCrit = session.createCriteria(Bankledger.class);        bankCrit.add(Restrictions.sqlRestriction("code='" + id + "'"));        List bankList = bankCrit.list();        if (bankList.size() > 0) {            bankledgerObj = (Bankledger) bankList.get(0);        }        return bankledgerObj;    }    public static Bankchallan getBankchallan(Session session, String id) {        Bankchallan bankChallanObj = null;        Criteria challanCrit = session.createCriteria(Bankchallan.class);        challanCrit.add(Restrictions.sqlRestriction("id='" + id + "'"));        List challanList = challanCrit.list();        if (challanList.size() > 0) {            bankChallanObj = (Bankchallan) challanList.get(0);        }        return bankChallanObj;    }    public static Employeeepfmaster getEmployeeepfmaster(Session session, String epfno) {        Employeeepfmaster employeeepfmasterObj = null;        Criteria empEpfCrit = session.createCriteria(Employeeepfmaster.class);        empEpfCrit.add(Restrictions.sqlRestriction("epfno='" + epfno + "'"));        List empEpfList = empEpfCrit.list();        if (empEpfList.size() > 0) {            employeeepfmasterObj = (Employeeepfmaster) empEpfList.get(0);        }        return employeeepfmasterObj;    }    public static Epfloantypes getEpfloantypes(Session session, String id) {        Epfloantypes epfloantypesObj = null;        Criteria empEpfCrit = session.createCriteria(Epfloantypes.class);        empEpfCrit.add(Restrictions.sqlRestriction("id='" + id+"'"));        List empEpfList = empEpfCrit.list();        if (empEpfList.size() > 0) {            epfloantypesObj = (Epfloantypes) empEpfList.get(0);        }        return epfloantypesObj;    }    public static Epfloanapplication getEpfloanapplication(Session session, String id) {        Epfloanapplication epfloanapplicationObj = null;        Criteria empEpfCrit = session.createCriteria(Epfloanapplication.class);        empEpfCrit.add(Restrictions.sqlRestriction("id='" + id + "'"));        List empEpfList = empEpfCrit.list();        if (empEpfList.size() > 0) {            epfloanapplicationObj = (Epfloanapplication) empEpfList.get(0);        }        return epfloanapplicationObj;    }    public static Accountsheads geAccountsheads(Session session, String code) {        Accountsheads accountsheadsObj = null;        Criteria accCrit = session.createCriteria(Accountsheads.class);        accCrit.add(Restrictions.sqlRestriction("acccode='" + code + "'"));        List accList = accCrit.list();        if (accList.size() > 0) {            accountsheadsObj = (Accountsheads) accList.get(0);        }        return accountsheadsObj;    }    public static Bankledger geBankledger(Session session, String code) {        Bankledger bankledgerObj = null;        Criteria bankCrit = session.createCriteria(Bankledger.class);        bankCrit.add(Restrictions.sqlRestriction("code='" + code + "'"));        List bankList = bankCrit.list();        if (bankList.size() > 0) {            bankledgerObj = (Bankledger) bankList.get(0);        }        return bankledgerObj;    }    public static Paymentmode getPaymentmode(Session session, String code) {        Paymentmode paymentmodeObj = null;        Criteria paymodeCrit = session.createCriteria(Paymentmode.class);        paymodeCrit.add(Restrictions.sqlRestriction("code='" + code + "'"));        List paymodeList = paymodeCrit.list();        if (paymodeList.size() > 0) {            paymentmodeObj = (Paymentmode) paymodeList.get(0);        }        return paymentmodeObj;    }    public static Partyledger getPartyledger(Session session, String code) {        Partyledger partyledgerObj = null;        Criteria partyCrit = session.createCriteria(Partyledger.class);        partyCrit.add(Restrictions.sqlRestriction("code='" + code + "'"));        List partyList = partyCrit.list();        if (partyList.size() > 0) {            partyledgerObj = (Partyledger) partyList.get(0);        }        return partyledgerObj;    }    public static Accountingyear getAccountingyear(Session session, String id) {        Accountingyear accountingyearObj = null;        Criteria accYearCrit = session.createCriteria(Accountingyear.class);        accYearCrit.add(Restrictions.sqlRestriction("id='" + id + "'"));        List accYearList = accYearCrit.list();        if (accYearList.size() > 0) {            accountingyearObj = (Accountingyear) accYearList.get(0);        }        return accountingyearObj;    }    public static String getVoucherNarration(Session session, String id) {        Voucher voucherObj = null;        String narration = "";        Criteria vouCrit = session.createCriteria(Voucher.class);        vouCrit.add(Restrictions.sqlRestriction("id='" + id + "'"));        List vouList = vouCrit.list();        if (vouList.size() > 0) {            voucherObj = (Voucher) vouList.get(0);            narration = voucherObj.getNarration();        }        return narration;    }    public static Accountsbooks getAccountBook(Session session, String bookNo) {        Accountsbooks accountsbooksObj = null;        try {            Criteria accCrit = session.createCriteria(Accountsbooks.class);            accCrit.add(Restrictions.sqlRestriction("code='" + bookNo + "'"));            List accList = accCrit.list();            if (accList.size() > 0) {                accountsbooksObj = (Accountsbooks) accList.get(0);            }        } catch (Exception e) {            System.out.println(e.toString());        }        return accountsbooksObj;    }    public static Dabatchdetails getDabatchdetails(Session session, String id) {        Dabatchdetails accountsbooksObj = null;        try {            Criteria accCrit = session.createCriteria(Dabatchdetails.class);            accCrit.add(Restrictions.sqlRestriction("id='" + id + "'"));            List accList = accCrit.list();            if (accList.size() > 0) {                accountsbooksObj = (Dabatchdetails) accList.get(0);            }        } catch (Exception e) {            System.out.println(e.toString());        }        return accountsbooksObj;    }    public static Commoditymaster geCommoditymaster(Session session, String commodityId) {        Commoditymaster commoditymasterObj = null;        try {            Criteria commodityCrit = session.createCriteria(Commoditymaster.class);            commodityCrit.add(Restrictions.sqlRestriction("id='" + commodityId + "'"));            List commodityList = commodityCrit.list();            if (commodityList.size() > 0) {                commoditymasterObj = (Commoditymaster) commodityList.get(0);            }        } catch (Exception e) {            System.out.println(e.toString());        }        return commoditymasterObj;    }    public static String getMonthAndYear(int month, int year) {        String monthandyear = "";        try {            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};            monthandyear = months[month] + " " + year;        } catch (Exception e) {            System.out.println(e.toString());        }        return monthandyear;    }    public static List getMonthandYearBetween(int startmonth, int startyear, int endmonth, int endyear) {        List<MonthValueObject> list = new ArrayList<MonthValueObject>();        MonthValueObject mvo = null;        int START_MONTH = startmonth;        int START_YEAR = startyear;        int END_MONTH_REGULAR = endmonth;        int END_YEAR_REGULAR = endyear;        int SMONTH = START_MONTH;        int SYEAR = START_YEAR;        int EMONTH = END_MONTH_REGULAR;        int EYEAR = END_YEAR_REGULAR;        if (START_YEAR == END_YEAR_REGULAR) {            while (SMONTH <= EMONTH) {                mvo = new MonthValueObject();                mvo.setMonth(SMONTH);                mvo.setYear(SYEAR);                list.add(mvo);                SMONTH++;            }        } else if (START_YEAR < END_YEAR_REGULAR) {            int YEAR_DIFF = (EYEAR - SYEAR) - 1;            int TOTAL_MONTH = ((12 - SMONTH) + EMONTH) + (YEAR_DIFF * 12);            for (int i = 0; i <= TOTAL_MONTH; i++) {                mvo = new MonthValueObject();                mvo.setMonth(SMONTH);                mvo.setYear(SYEAR);                list.add(mvo);                SMONTH++;                if (SMONTH > 12) {                    SMONTH = 1;                    SYEAR++;                }            }        }        return list;    }}