/* 

 * To change this template, choose Tools | Templates

 * and open the template in the editor.  

 */ 
package com.onward.action;

import com.onward.common.ApplicationOptions;

import com.onward.common.BinaryCodec;

import com.onward.common.HibernateUtil;
import com.onward.persistence.payroll.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.math.MathContext;

import java.math.RoundingMode;

import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Calendar;

import java.util.Date;

import java.util.HashMap;

import java.util.Map;

import java.util.StringTokenizer;

import java.util.logging.Level;

import java.util.logging.Logger;

import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;

import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.spec.SecretKeySpec;

import org.apache.struts.actions.DispatchAction;

import org.hibernate.SessionFactory;

import org.hibernate.Session;

import org.hibernate.Criteria;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import java.text.NumberFormat;

import java.text.DecimalFormat;

import java.util.LinkedHashMap;
import org.hibernate.criterion.Order;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/**
 *
 *
 *
 * @author Jagan Mohan. B
 *
 */
public class OnwardAction extends DispatchAction {

    public static final String DEFAULT_ENCODING = "UTF-8";
//    static BASE64Encoder enc = new BASE64Encoder();
//    static BASE64Decoder dec = new BASE64Decoder();

    /**
     *
     * This method used for convert the encrypted string to decrypted string.
     *
     *
     *
     * @param encryptedString Encrypted String
     *
     * @param keyToBeUsed Secret Key
     *
     * @return This method return Decrypt String value 
     *
     */
    public String decrypt(String encryptedString, SecretKeySpec keyToBeUsed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        String decryptedData = null;

        BinaryCodec bc = new BinaryCodec();

        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, keyToBeUsed, cipher.getParameters());

        byte[] toBeDecrypted = bc.toByteArray(encryptedString);

        byte[] original = cipher.doFinal(toBeDecrypted);

        decryptedData = new String(original);

        return decryptedData;

    }

     public static Sectionmaster getSection(Session session, String code) {
        Sectionmaster sectionmasterObj = null;
        Criteria empRegionCrit = session.createCriteria(Sectionmaster.class);
        empRegionCrit.add(Restrictions.sqlRestriction("id='" + code + "'"));
        List empRegionList = empRegionCrit.list();
        if (empRegionList.size() > 0) {
            sectionmasterObj = (Sectionmaster) empRegionList.get(0);
        }
        return sectionmasterObj;
    }
    /**
     *
     * This method used for conver the date format to string format
     * (ie.dd/MM/yyyy).
     *
     *
     *
     * @param date
     *
     * @return Sting return either string date value or empty.
     *
     */
    public String dateToString(Date date) {

        SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy");

        String strDate = "";

        if (date != null) {

            strDate = sdfOutput.format(date);

        }

        return strDate;

    }

    /**
     *
     *
     *
     * @param date
     *
     * @return
     *
     */
    public java.sql.Date postgresDate(String date) {

        java.sql.Date sqlDate = null;

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if (date != null && date.length() > 0) {

                java.util.Date utilDate = dateFormat.parse(date);

                sqlDate = new java.sql.Date(utilDate.getTime());

            } else {

                sqlDate = null;

            }

        } catch (ParseException ex) {

            Logger.getLogger(OnwardAction.class.getName()).log(Level.SEVERE, null, ex);

        }

        return sqlDate;

    }

    /**
     *
     *
     *
     * @param value
     *
     * @return
     *
     */
    public double doubleValue(String value) {

        if (value != null && !("".equals(value))) {

            return Double.parseDouble(value);

        } else {

            return 0.00d;

        }

    }

    /**
     *
     *
     *
     * @param value
     *
     * @return
     *
     */
    public long longValue(String value) {

        if (value != null && !("".equals(value))) {

            return Long.parseLong(value);

        } else {

            return 0;

        }

    }

    /**
     *
     *
     *
     * @param value
     *
     * @return
     *
     */
    public int intValue(String value) {

        if (value != null && !("".equals(value))) {

            return Integer.parseInt(value);

        } else {

            return 0;

        }

    }

    public Map loadApplicationReferenceCodes(String displayId, String parentCode) {

        return ApplicationOptions.getInstance().getMultipleReferenceCodes(displayId, parentCode);

    }

    /**
     *
     *
     *
     * @param value
     *
     * @return
     *
     */
    public String checkNull(String value) {

        if (value == null || value.isEmpty() || value.length() <= 0 || value.equalsIgnoreCase("null")) {

            value = "";

        }

        return value;

    }

    /**
     *
     *
     *
     * @param value
     *
     * @return
     *
     */
    public String checkNullReplaceZero(String value) {

        if (value == null || value.isEmpty() || value.length() <= 0 || value.equalsIgnoreCase("null")) {

            value = "0";

        }

        return value;

    }

    /**
     *
     *
     *
     * @param date
     *
     * @return
     *
     */
    public String checkDateNull(Date date) {

        SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String strDate = "";

        if (date != null) {

            strDate = sdfOutput.format(date);

        }

        return strDate;

    }

    /**
     *
     *
     *
     * @param dateFormat
     *
     * @return
     *
     */
    public static String getDateAndTime(String dateFormat) {

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        return sdf.format(cal.getTime());

    }

    /**
     *
     * This methos used for get the Today's date based on formatetype
     *
     * dd-MMM-yyyy hh:mm:ss
     *
     * @return
     *
     */
    public String getToday(String formateType) {

        String todaysDate = null;

        try {

            Calendar cal = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat(formateType);

            todaysDate = dateFormat.format(cal.getTime());

        } catch (Exception e) {

            e.printStackTrace();

        }

        return todaysDate;

    }

    /**
     *
     * This methos used for get the Today's date based on formatetype
     *
     * dd-MMM-yyyy hh:mm:ss
     *
     * @return
     *
     */
    public String getCreateDate() {

        String toDayDate = null;

        try {
            java.sql.Date dbToDaysDate = new java.sql.Date(new Date().getTime());

            toDayDate = dateToString(dbToDaysDate);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return toDayDate;

    }

    /**
     *
     *
     *
     * @param data
     *
     * @return
     *
     */
    public String stringToHex(String data) {

        StringBuffer buffer = new StringBuffer();

        int intValue;

        for (int x = 0; x < data.length(); x++) {

            int cursor = 0;

            intValue = data.charAt(x);

            String binaryChar = new String(Integer.toBinaryString(data.charAt(x)));

            for (int i = 0; i < binaryChar.length(); i++) {

                if (binaryChar.charAt(i) == '1') {

                    cursor += 1;

                }

            }

            if ((cursor % 2) > 0) {

                intValue += 128;

            }

            buffer.append(Integer.toHexString(intValue));

        }

        return buffer.toString();

    }

    /**
     *
     *
     *
     * @param data
     *
     */
    public String hexToString(String data) {

        int intValue;

        StringBuffer buffer = new StringBuffer();

        for (int x = 0; x < data.length(); x += 2) {

            intValue = new Integer(Integer.parseInt(data.substring(x, x + 2), 16));

            if (intValue > 128) {

                buffer.append((char) (intValue - 128));

            } else {

                buffer.append((char) (intValue));

            }

        }

        return buffer.toString();

    }

    /**
     *
     *
     *
     * @param objInstance
     *
     * @return
     *
     */
    public HashMap convertViewModelToMap(Object objInstance) {

        HashMap viewModelMap = new HashMap();

        try {



            Class<?> classObj = objInstance.getClass();

            Method[] methodList = classObj.getDeclaredMethods();

            Object args[] = null;

            int size = methodList.length;



            for (int i = 0; i < size; i++) {

                String methodName = methodList[i].getName();

                if (methodName.startsWith("get")) {

                    String value = String.valueOf(methodList[i].invoke(objInstance, args));

                    if (value.equals("null")) {

                        value = "";

                    }

                    if (methodList[i].getReturnType().getName().equalsIgnoreCase("java.util.Date")) {

                        value = convertdateformattodisplaytype(value);

                    }



                    String fieldName = methodName.substring(3);

                    String firstChar = "" + fieldName.charAt(0);

                    fieldName = fieldName.replaceFirst(firstChar, firstChar.toLowerCase());

                    viewModelMap.put(fieldName, value);

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return viewModelMap;

    }

    /**
     *
     *
     *
     * @param date
     *
     * @return
     *
     */
    public String convertdateformattodisplaytype(String date) {

        if (date == null || "".equals(date) || date.equals("null")) {

            return "";

        }



        StringTokenizer str = new StringTokenizer(date, "-");

        String year = str.nextToken();

        String month = str.nextToken();

        String day = str.nextToken();

        return day + "/" + month + "/" + year;

    }

    /**
     *
     *
     *
     * @param query
     *
     * @return
     *
     */
    public synchronized int get_MaxNumber(String query) {

        int runningNumber = 1;

        SessionFactory _factory = HibernateUtil.getSessionFactory();

        Session session = null;

        try {

            session = _factory.openSession();

            List imlist = (ArrayList) session.createSQLQuery(query).list();

            if (imlist.size() > 0 && imlist.get(0) != null) {

                runningNumber = Integer.parseInt(imlist.get(0).toString()) + 1;

            } else {

                runningNumber = 1;

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            if (session != null) {

                session.clear();

                session.close();

            }

        }

        return runningNumber;

    }

    public synchronized long get_MaxNumberAsLong(String query) {

        long runningNumber = 1;

        SessionFactory _factory = HibernateUtil.getSessionFactory();

        Session session = null;

        try {

            session = _factory.openSession();

            List imlist = (ArrayList) session.createSQLQuery(query).list();

            if (imlist.size() > 0 && imlist.get(0) != null) {

                runningNumber = Long.parseLong(imlist.get(0).toString()) + 1;

            } else {

                runningNumber = 1;

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            if (session != null) {

                session.clear();

                session.close();

            }

        }

        return runningNumber;

    }

    /**
     *
     *
     *
     * @param value
     *
     * @return
     *
     */
    public static BigDecimal bigDecimalValidate(String value) {

        BigDecimal bigDecimal = new BigDecimal("0");

        if (value != null && value.trim().length() >= 1) {

            bigDecimal = new BigDecimal(value);

        }

        return bigDecimal;

    }

    /**
     *
     *
     *
     * @param object
     *
     * @param pattern
     *
     * @return
     *
     */
    public String setPrecision(Object object, String pattern) {

        String value = "";

        NumberFormat numberFormat = new DecimalFormat(pattern);

        BigDecimal bigDecimal = null;

        if (object != null) {

            value = object.toString();

        }

        if ((value != null) && !("".equals(value))) {

            bigDecimal = new BigDecimal(value);

        }

        if (bigDecimal != null) {

            return numberFormat.format(bigDecimal);

        }

        return "0.00";

    }

    /**
     *
     * This method used for Add the Comma to Amount Field.
     *
     *
     *
     * @param amount
     *
     */
    public String addCommaToAmount(Long amount) {

        boolean minusFlag = false;

        String findalAmount = "";



        if (amount < 0) {

            minusFlag = true;

            amount = amount * (-1);

        }



        String strAmount = Long.toString(amount);

        int lenght = strAmount.length();

        StringBuffer amountsb = new StringBuffer(strAmount);



        if (amount > 999) {

            for (int i = lenght; i > 0; i = i - 2) {

                if (i == lenght) {

                    i = i - 3;

                    amountsb = amountsb.insert(i, ",");

                } else {

                    amountsb = amountsb.insert(i, ",");

                }

            }

        }

        findalAmount = amountsb.insert(amountsb.toString().length(), ".00").toString();

        if (minusFlag) {

            findalAmount = amountsb.insert(0, "-").toString();

        }



        return findalAmount;

    }

    /**
     *
     * This method used for remove the comma from amount.
     *
     *
     *
     * @param amount
     *
     * @return
     *
     */
    public String removeCommaFromAmount(String amount) {

        StringTokenizer st = new StringTokenizer(amount, ",", false);

        String findalAmount = "";

        while (st.hasMoreElements()) {

            findalAmount += st.nextElement();

        }

        return findalAmount;

    }

    /**
     *
     * This method used to get supplier name from supplier id.
     *
     *
     *
     * @param supplier id
     *
     * @return
     *
     */
    public static BigDecimal roundBigDecimal(final BigDecimal input) {

        return input.round(
                new MathContext(
                input.toBigInteger().toString().length(),
                RoundingMode.HALF_UP));

    }

    /**
     *
     *
     *
     * @param value
     *
     * @return
     *
     */
    public static BigDecimal StringtobigDecimal(String value) {

        NumberFormat numberFormat = new DecimalFormat("0.00");

        BigDecimal bigDecimal = new BigDecimal("0.00");



        if (value != null && value.trim().length() >= 1) {

            bigDecimal = new BigDecimal(value);

        }

        return new BigDecimal(numberFormat.format(bigDecimal));

    }

    public Employeemaster getEmployeemaster(Session session, String epfno, String LoggedInRegion) {

        Employeemaster employeemasterObj = null;

        try {

            Criteria lrCrit = session.createCriteria(Employeemaster.class);

            lrCrit.add(Restrictions.sqlRestriction("region = '" + LoggedInRegion + "' "));

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

    public Regionmaster getRegionmaster(Session session, String regionid) {

        Regionmaster regionmasterObj = null;

        try {

            Criteria lrCrit = session.createCriteria(Regionmaster.class);

            lrCrit.add(Restrictions.sqlRestriction("id = '" + regionid + "' "));

//            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));

            List ldList = lrCrit.list();

            if (ldList.size() > 0) {

                regionmasterObj = (Regionmaster) ldList.get(0);

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {
        }





        return regionmasterObj;

    }

    public Employeemaster getEmployeemasterWithoutRegion(Session session, String epfno) {

        Employeemaster employeeepfmasterObj = null;

        try {

            Criteria lrCrit = session.createCriteria(Employeemaster.class);

            lrCrit.add(Restrictions.sqlRestriction("epfno = '" + epfno + "' "));

            List ldList = lrCrit.list();

            if (ldList.size() > 0) {

                employeeepfmasterObj = (Employeemaster) ldList.get(0);

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {
        }





        return employeeepfmasterObj;

    }

    public int getCurrentAge(String DateofBirth) {

        int age = 0;

        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");

        try {

            fm.parse(DateofBirth);

            Calendar cal = fm.getCalendar();

            int BDay = cal.get(Calendar.DATE);

            int BMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month

            int BYear = cal.get(Calendar.YEAR);



            Calendar curcal = Calendar.getInstance();

            int currentYear = curcal.get(Calendar.YEAR);

            int currentMonth = curcal.get(Calendar.MONTH) + 1;

            int currentDay = curcal.get(Calendar.DAY_OF_MONTH);

            
            
              age = currentYear - BYear;
            
              if(currentMonth < BMonth){
                age = age - 1;
                }
              
               if(currentMonth == BMonth && currentDay < BDay){
                    age = age - 1;
                    }

          /*  if (currentMonth >= BMonth && currentDay >= BDay) {

                age = currentYear - BYear;

            } else {

                age = currentYear - (BYear + 1);

            }*/



        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {
        }

        

        return age;

    }

    public boolean isFirstMonthAfterBirthDate(String DateofBirth) {
        boolean status = false;
        int age = 0;

        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");

        try {

            fm.parse(DateofBirth);

            Calendar cal = fm.getCalendar();

            int BDay = cal.get(Calendar.DATE);

            int BMonth = cal.get(Calendar.MONTH) + 1; // In Current date Add 1 in month

            int BYear = cal.get(Calendar.YEAR);



            Calendar curcal = Calendar.getInstance();

            int currentYear = curcal.get(Calendar.YEAR);

            int currentMonth = curcal.get(Calendar.MONTH) + 1;

            int currentDay = curcal.get(Calendar.DAY_OF_MONTH);



           age = currentYear - BYear;

            if (currentMonth < BMonth) {
                age = age - 1;
            }

            if (currentMonth == BMonth && currentDay < BDay) {
                age = age - 1;
            }

            if (currentMonth == BMonth) {
                status = true;
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {
        }

        

        return status;

    }

//    public static String base64encode(String text) {
//        try {
//            String rez = enc.encode(text.getBytes(DEFAULT_ENCODING));
//            return rez;
//        } catch (UnsupportedEncodingException e) {
//            return null;
//        }
//    }//base64encode

//    public static String base64decode(String text) {
//
//        try {
//            return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
//        } catch (IOException e) {
//            return null;
//        }
//
//    }//base64decode

    public static String xorMessage(String message, String key) {
        try {
            if (message == null || key == null) {
                return null;
            }

            char[] keys = key.toCharArray();
            char[] mesg = message.toCharArray();

            int ml = mesg.length;
            int kl = keys.length;
            char[] newmsg = new char[ml];

            for (int i = 0; i < ml; i++) {
                newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
            }//for i
            mesg = null;
            keys = null;
            return new String(newmsg);
        } catch (Exception e) {
            return null;
        }
    }//xorMessage

    public static String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    public Paycodemaster getPaycodeMater(Session session, String paycode) {
        Paycodemaster masterObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Paycodemaster.class);
            lrCrit.add(Restrictions.sqlRestriction("paycode = '" + paycode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                masterObj = (Paycodemaster) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return masterObj;
    }

    public Designationmaster getDesignationMater(Session session, String designationcode) {
        Designationmaster masterObj = null;
        try {
            Criteria lrCrit = session.createCriteria(Designationmaster.class);
            lrCrit.add(Restrictions.sqlRestriction("designationcode = '" + designationcode + "' "));
            List ldList = lrCrit.list();
            if (ldList.size() > 0) {
                masterObj = (Designationmaster) ldList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return masterObj;
    }

    public List<String> JoinString(String txt, int len, String tokenformate) {
        List<String> list = new ArrayList<String>();
        String strapp = "";
        StringTokenizer st = new StringTokenizer(txt, tokenformate);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ((strapp.length() + token.length()) < len) {
                strapp = strapp + token + tokenformate;
            } else {
                list.add(strapp);
                strapp = token + tokenformate;
            }
        }
        if (strapp.endsWith(",")) {
            strapp = strapp.substring(0, strapp.length() - 1);
        }
        list.add(strapp);
        return list;
    }

    public synchronized java.sql.Date getCurrentDate() {

        java.sql.Date dbToDaysDate = null;

        try {
            dbToDaysDate = new java.sql.Date(new Date().getTime());


        } catch (Exception e) {

            e.printStackTrace();

        }

        return dbToDaysDate;

    }

    public synchronized Timestamp getCurrentDateTimeStamp() {

        Timestamp dbToDaysDate = null;

        try {
            dbToDaysDate = new Timestamp(System.currentTimeMillis());
//             dbToDaysDate = new java.sql.Date(new Date().getTime());


        } catch (Exception e) {

            e.printStackTrace();

        }

        return dbToDaysDate;

    }

    public String Align(String str, int len, String position) {
        int totlen = str.length();
        if (totlen >= len) {
            return str.substring(0, len);
        } else {
            StringBuffer strbuf = new StringBuffer();
            if (position.equalsIgnoreCase("center")) {
                int sp = len - totlen;
                sp = sp / 2;
                String space = null;
                for (int i = 0; i < sp; i++) {
                    strbuf.append(" ");
                }
                strbuf.append(str);
                for (int j = strbuf.length(); j < len; j++) {
                    strbuf.append(" ");
                }
            } else if (position.equalsIgnoreCase("left")) {
                int sp = len - totlen;
                strbuf.append(str);
                for (int i = 0; i < sp; i++) {
                    strbuf.append(" ");
                }
            } else if (position.equalsIgnoreCase("right")) {
                int sp = len - totlen;
                for (int i = 0; i < sp; i++) {
                    strbuf.append(" ");
                }
                strbuf.append(str);
            }
            return strbuf.toString();
        }

    }
    
    public java.util.Date javautilDate(String date) {
        java.util.Date utilDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (date != null && date.length() > 0) {
                utilDate = dateFormat.parse(date);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return utilDate;

    }
    
    public Map getAccountBooks(Session session) {
        Map acBookMap = new HashMap();
        Map acYearList = new LinkedHashMap();
        acYearList.put("0", "--Select--");
        
        try {
            Criteria accountYearCrit = session.createCriteria(Accountingyear.class);
            List accountYearList = accountYearCrit.list();
            if (accountYearList.size() > 0) {
                for (int i = 0; i < accountYearList.size(); i++) {
                    Accountingyear accountsbooksObj = (Accountingyear) accountYearList.get(i);
                    acYearList.put(accountsbooksObj.getId(), accountsbooksObj.getStartyear()+"-"+accountsbooksObj.getEndyear());
                }
            }
            acBookMap.put("accYearList", acYearList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return acBookMap;
    }
    
    public Map getRegions(Session session) {
        Map regionMap = new HashMap();
        Map regionMastersList = new LinkedHashMap();
        regionMastersList.put("0", "--Select--");
        try {
            Criteria regionMasterCrit = session.createCriteria(Regionmaster.class);
            regionMasterCrit.addOrder(Order.asc("regionname"));
            List regionMasterList = regionMasterCrit.list();
            if (regionMasterList.size() > 0) {
                for (int i = 0; i < regionMasterList.size(); i++) {
                    Regionmaster regionmasterObj = (Regionmaster) regionMasterList.get(i);
                    regionMastersList.put(regionmasterObj.getId(), regionmasterObj.getRegionname());
                }
            }
            regionMap.put("regionList", regionMastersList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return regionMap;
    }
    
    public Brsledger getBRSledger(Session session, String branchcode) {
        Brsledger masterObj = null;
        try {
            Criteria brsledgetcriteria = session.createCriteria(Brsledger.class);
            brsledgetcriteria.add(Restrictions.sqlRestriction("branchcode='" + branchcode + "'"));
            List branchlist = brsledgetcriteria.list();
            if (branchlist.size() > 0) {
                masterObj = (Brsledger) branchlist.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return masterObj;
    }
}
