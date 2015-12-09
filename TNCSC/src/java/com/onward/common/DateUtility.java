/*
 * DateUtility.java
 *
 * Created on August 26, 2007, 12:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.onward.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public final class DateUtility {

    public final static int YEAR = 1;
    public final static int MONTH = 2;
    public final static int DAY = 5;
    public static int year = 0;
    public static int month = 0;
    public static int date = 0;

    public static String addToDate(String Datestring, int fieldtoadd, int addValue) throws ParseException {
        Calendar c1 = getCalender(Dtformat1, Datestring);
        c1.add(fieldtoadd, addValue);
        return new SimpleDateFormat(Dtformat1).format(c1.getTime());
    }


    public static String addToDate1(String Datestring, int fieldtoadd, int addValue) throws ParseException {
        Calendar c1 = getCalender(Dtformat, Datestring);
        c1.add(fieldtoadd, addValue);
        return new SimpleDateFormat(Dtformat).format(c1.getTime());
    }

    public static int lastDateofMonth(String Dtformat1, String actualdate) {
        StringTokenizer st = new StringTokenizer(actualdate, "/");
        String dateval = st.nextToken();
        String monthval = st.nextToken();
        String yearval = st.nextToken();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Integer.parseInt(monthval) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(yearval));
        return calendar.getActualMaximum(Calendar.DATE);
    }
         public static String lastDateofMonth(String actualdate)
        {
            StringTokenizer st=new StringTokenizer(actualdate,"/");
            String dateval=st.nextToken();
            String monthval=st.nextToken();
            String yearval=st.nextToken();
            Calendar calendar = Calendar.getInstance();     
            calendar.set(Calendar.MONTH, Integer.parseInt(monthval)-1);
            calendar.set(Calendar.YEAR, Integer.parseInt(yearval));
            return String.valueOf(calendar.getActualMaximum(Calendar.DATE))+"/"+monthval+"/"+yearval;
        }
    public static String subtractFromDate(String Datestring, int fieldtosub, int subValue) throws ParseException {
        if (subValue > 0) {
            subValue = subValue * -1;
        }
        return addToDate(Datestring, fieldtosub, subValue);
    }

    public static String subtractFromDate1(String Datestring, int fieldtosub, int subValue) throws ParseException {
        if (subValue > 0) {
            subValue = subValue * -1;
        }
        return addToDate1(Datestring, fieldtosub, subValue);
    }

    public static void main(String args[]) throws Exception{
        Calendar calendar = new GregorianCalendar();
      String  auctionDate = DateUtility.subtractFromDate("31/03/2013", calendar.DATE, 90);
        System.out.println("auctionDate=="+auctionDate);

    }
    public static long daysBetweenDates(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat1, DateString1);
            Calendar c2 = getCalender(Dtformat1, DateString2);
            return (Math.abs(((c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000))));
        } catch (ParseException e) {
            return 0;
        }
    }

    // DIFFERENCE HOURS BETWEEN TWO DATES JAGAN
    public static long hoursBetweenDates(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat1, DateString1);
            Calendar c2 = getCalender(Dtformat1, DateString2);
            return (Math.abs(((c2.getTime().getTime() - c1.getTime().getTime()) / (3600 * 1000))));
        } catch (ParseException e) {
            return 0;
        }
    }

    // DIFFERENCE MINUTES BETWEEN TWO DATES JAGAN
    public static long minutesBetweenDates(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat1, DateString1);
            Calendar c2 = getCalender(Dtformat1, DateString2);
            return (Math.abs(((c2.getTime().getTime() - c1.getTime().getTime()) / (1000))));
        } catch (ParseException e) {
            return 0;
        }
    }

    public static long daysBetweenDates1(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat, DateString1);
            Calendar c2 = getCalender(Dtformat, DateString2);
            return (Math.abs(((c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000))));
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String MonthsAndDaysBetweenDates(String Dtformat1, String DateString1, String DateString2) throws ParseException {
        String monthdate = "";
        Calendar c1 = getCalender(Dtformat1, DateString1);
        Calendar c2 = getCalender(Dtformat1, DateString2);
        int bigyear = c2.get(Calendar.YEAR);
        int bigmonth = c2.get(Calendar.MONTH);
        int bigdate = c2.get(Calendar.DATE);
        int smallyear = c1.get(Calendar.YEAR);
        int smallmonth = c1.get(Calendar.MONTH);
        int smalldate = c1.get(Calendar.DATE);
        year = bigyear - smallyear;
        if (bigmonth >= smallmonth) {
            month = bigmonth - smallmonth;
        } else {
            year = year - 1;
            month = 12 + bigmonth - smallmonth;
        }
        if (bigdate >= smalldate) {
            date = bigdate - smalldate;
        } else {
            month = month - 1;
            date = 31 + bigdate - smalldate;
            if (month < 0) {
                month = 11;
                year = year - 1;
            }
        }
        int months = year * 12 + month;
        String addDate = addToDate(DateString1, Calendar.MONTH, months);

        Calendar c3 = getCalender(Dtformat1, addDate);
        long days = Math.abs(((c2.getTime().getTime() - c3.getTime().getTime()) / (24 * 3600 * 1000)));
        monthdate = String.valueOf(months) + "/" + String.valueOf(days);
        return monthdate;
    }

    public static long daysBetweenDatesForInterest(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat1, DateString1);
            Calendar c2 = getCalender(Dtformat1, DateString2);
            return (Math.abs(((c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000))));
        } catch (ParseException e) {
            return 0;
        }
    }

    public static long monthsBetweenDates(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat1, DateString1);
            Calendar c2 = getCalender(Dtformat1, DateString2);
            return (Math.abs(((c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12))) + 1);
        } catch (ParseException e) {
            return 0;
        }
    }
    public static long monthsBetweenDates1(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat, DateString1);
            Calendar c2 = getCalender(Dtformat, DateString2);
            return (Math.abs(((c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12))));
        } catch (ParseException e) {
            return 0;
        }
    }
    public static long yearsBetweenDates(String DateString1, String DateString2) {
        try {
            Calendar c1 = getCalender(Dtformat1, DateString1);
            Calendar c2 = getCalender(Dtformat1, DateString2);
            return (Math.abs((c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR))));
        } catch (ParseException e) {
            return 0;
        }
    }

    public static java.sql.Date nextDate(long today, long noofdays) {
        long millisInDay = 1000 * 60 * 60 * 24;
        long nextDate = today + noofdays * millisInDay; // after 30 days  
        return new java.sql.Date(nextDate);

    }

//	natarajan.a-chn-16-10-2006-beg	
    public static String Get_Date_Format(String Datestring, String Curr_Format, String New_Format) throws ParseException {
        Calendar c1 = getCalender(Curr_Format, Datestring);
        return new SimpleDateFormat(New_Format).format(c1.getTime());
    }
//	natarajan.a-chn-16-10-2006-end
    private static Calendar getCalender(String DateFt, String DateObj) throws ParseException {
        SimpleDateFormat fm = new SimpleDateFormat(DateFt);
        fm.setLenient(false);
        fm.parse(DateObj);
        return fm.getCalendar();
    }

    public static boolean compareDates(String DateFt, String DateString1, String DateString2, int tocheck) {
        try {
            Calendar c1 = getCalender(DateFt, DateString1);
            Calendar c2 = getCalender(DateFt, DateString2);

            switch (tocheck) {
                case DATEGREATERTHAN:
                    if (c1.after(c2)) {
                        return true;
                    }
                    break;
                case DATEGREATERTHANOREQUAL:
                    if (c1.after(c2) || (c1.equals(c2))) {
                        return true;
                    }
                    break;
                case DATELESSTHAN:
                    if (c1.before(c2)) {
                        return true;
                    }
                    break;
                case DATELESSTHANOREQUAL:
                    if (c1.before(c2) || (c1.equals(c2))) {
                        return true;
                    }
                    break;
                case DATEEQUAL:
                    if (c1.equals(c2)) {
                        return true;
                    }
                    break;
                default:
                    return false;
            }
            return false;
        } catch (ParseException e) {
            return false;
        }
    }

    // default Date Format - dd-MM-YYYY
    public static boolean DateGreaterThan(String DateString1, String DateString2) {
        return compareDates(Dtformat1, DateString1, DateString2, DATEGREATERTHAN);
    }

    public static boolean DateGreaterThan(String DateString1, String DateString2, String DateFormat) {
        return compareDates(DateFormat, DateString1, DateString2, DATEGREATERTHAN);
    }

    public static boolean DateGreaterThanOrEqual(String DateString1, String DateString2) {
        return compareDates(Dtformat1, DateString1, DateString2, DATEGREATERTHANOREQUAL);
    }

    public static boolean DateGreaterThanOrEqual(String DateString1, String DateString2, String DateFormat) {
        return compareDates(DateFormat, DateString1, DateString2, DATEGREATERTHANOREQUAL);
    }

    public static boolean DateLessThan(String DateString1, String DateString2) {
        return compareDates(Dtformat1, DateString1, DateString2, DATELESSTHAN);
    }

    public static boolean DateLessThan(String DateString1, String DateString2, String DateFormat) {
        return compareDates(DateFormat, DateString1, DateString2, DATELESSTHAN);
    }

    public static boolean DateLessThanOrEqual(String DateString1, String DateString2) {
        return compareDates(Dtformat1, DateString1, DateString2, DATELESSTHANOREQUAL);
    }

    public static boolean DateLessThanOrEqual(String DateString1, String DateString2, String DateFormat) {
        return compareDates(DateFormat, DateString1, DateString2, DATELESSTHANOREQUAL);
    }

    public static boolean DateEqual(String DateString1, String DateString2) {
        return compareDates(Dtformat1, DateString1, DateString2, DATEEQUAL);
    }

    public static boolean DateEqual(String DateString1, String DateString2, String DateFormat) {
        return compareDates(DateFormat, DateString1, DateString2, DATEEQUAL);
    }

    public static boolean isLeapYear(int year) {
        if ((year % 100 != 0) || (year % 400 == 0)) {
            return true;
        }
        return false;
    }

    public static boolean isValidDate(String date) {
        return isValidDate(date, "yyyy-MM-dd");	    // default date format("dd-MM-yyyy")
    }

    public static boolean isValidDate(String date, String fmt) {
        // set date format, this can be changed to whatever format
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        sdf.setLenient(false);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;

    }

    public static String convertDbDateToUserFormat(Date pDate) {
        String date = "";
        try {
            if (pDate != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                date = formatter.format(pDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }





    public static int daysInCurrentMonth(GregorianCalendar calendar) {
        int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        daysInMonths[1] += calendar.isLeapYear(calendar.get(GregorianCalendar.YEAR)) ? 1 : 0;
        return daysInMonths[calendar.get(GregorianCalendar.MONTH)];
    }
    public final static String Dtformat = "yyyy-MM-dd";
    public final static String Dtformat1 = "dd/MM/yyyy";
    public static final int DATEGREATERTHAN = 500;
    public static final int DATEGREATERTHANOREQUAL = 501;
    public static final int DATELESSTHAN = 502;
    public static final int DATELESSTHANOREQUAL = 503;
    public static final int DATEEQUAL = 504;

    public DateUtility() {
    }

    
    

;
}
