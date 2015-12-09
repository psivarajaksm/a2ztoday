/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

/**
 *
 * @author Prince vijayakumar M
 */
public class AmountInWords {

    public static String string;
    public static String a[] = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",};
    public static String b[] = {"hundred", "thousand", "lakh", "crore"};
    public static String c[] = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "ninteen",};
    public static String d[] = {"twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninty"};

    public static double round(double d, int decimalPlace) {
        // see the Javadoc about why we use a String in the constructor
        // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static String convertNumToWord(long number) {

        int c = 1;
        long rm;
        string = "";
        while (number != 0) {
            switch (c) {
                case 1:
                    rm = number % 100;
                    pass(rm);
                    if (number > 100 && number % 100 != 0) {
                        display("and ");
                    }
                    number /= 100;
                    break;

                case 2:
                    rm = number % 10;
                    if (rm != 0) {
                        display(" ");
                        display(b[0]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 10;
                    break;

                case 3:
                    rm = number % 100;
                    if (rm != 0) {
                        display(" ");
                        display(b[1]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 100;
                    break;

                case 4:
                    rm = number % 100;
                    if (rm != 0) {
                        display(" ");
                        display(b[2]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 100;
                    break;

                case 5:
                    rm = number % 100;
                    if (rm != 0) {
                        display(" ");
                        display(b[3]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 100;
                    break;
            }
            c++;
        }
        //string += " only";
        return string;
    }

    public static void pass(long no) {
        int rm, q;
        int number = Integer.parseInt(String.valueOf(no));
        if (number < 10) {
            display(a[number]);
        }

        if (number > 9 && number < 20) {
            display(c[number - 10]);
        }

        if (number > 19) {
            rm = number % 10;
            if (rm == 0) {
                q = number / 10;
                display(d[q - 2]);
            } else {
                q = number / 10;
                display(a[rm]);
                display(" ");
                display(d[q - 2]);
            }
        }
    }

    public static void display(String s) {
        String t;
        t = string;
        string = s;
        string += t;
    }

    public static String removeComma(String amount) {
        StringTokenizer st = new StringTokenizer(amount, ",");
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
        }
        return sb.toString();
    }

    public static String convertAmountintoWords(String amountValue, boolean flag) {
        String words = "";
        String ruppee = "";
        String paise = "";
        String amountInWords = "";

        String amount = removeComma(amountValue);

        StringTokenizer amountTokenizer = new StringTokenizer(amount, ".");

        if (amountTokenizer.hasMoreTokens()) {
            ruppee = amountTokenizer.nextToken();
            words = "Rupees " + convertNumToWord(Long.parseLong(ruppee));
        }
        if (amountTokenizer.hasMoreTokens()) {
            paise = amountTokenizer.nextToken();
            if (!paise.equals("0") && !paise.equals("00")) {
                words += " and paise " + convertNumToWord(Long.parseLong(paise));
            }
        }
        if (amount.equalsIgnoreCase("0")) {
            return "";
        } else {
            String customizedAmountInWords = "";
            String customizedToken = "";
            amountInWords = words + " only";

            if (flag) {
                customizedAmountInWords = amountInWords.replace("Rupees", " ");
            } else {
                customizedAmountInWords = amountInWords;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(customizedAmountInWords, " ");
            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                String firstLetter = token.substring(0, 1).toString();
                String remainingString = token.substring(1, token.length());
                customizedToken += firstLetter.toUpperCase().concat(remainingString) + " ";
            }
            // System.out.println("Amount in Words : " + customizedToken);
            return customizedToken;
        }
    }

    public static String convertAmountintoWords(String amountValue) {
        return convertAmountintoWords(amountValue, false);
    }

    public static String convertAmountintoWords(Double amountValue) {
        if (amountValue != 0) {
            return convertAmountintoWords(String.valueOf(Math.abs(amountValue)), false);
        } else {
            return "";
        }
    }

    public static String convertAmountintoWords(Double amountValue, boolean flag) {
        if (amountValue != 0) {
            return convertAmountintoWords(String.valueOf(Math.abs(amountValue)), flag);
        } else {
            return "";
        }
    }

    public static String setPrecision(Object object, String pattern) {
        String value = "";
        NumberFormat numberFormat = new DecimalFormat(pattern);
        BigDecimal bigDecimal = null;
        if (object != null) {
            value = object.toString();
        }
        if (value != null && !"".equals(value)) {
            bigDecimal = new BigDecimal(value);
        }
        if (bigDecimal != null) {
            return numberFormat.format(bigDecimal);
        } else {
            return pattern;
        }
    }

//    public static void main(String args[]) {
//        System.out.println(convertAmountintoWords("992345678.25"));
////        convertAmountintoWords("5000.00", true);
////        convertAmountintoWords(1.00);
////        convertAmountintoWords(1.00, true);
//    }
}
