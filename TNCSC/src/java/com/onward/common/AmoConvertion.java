/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.common;

/**
 *
 * @author Prince vijayakumar M
 */
public class AmoConvertion {

    private String string = "";
    private String a[] = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",};
    private String b[] = {"hundred", "thousand", "lakh", "crore"};
    private String c[] = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "ninteen",};
    private String d[] = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    public String ConvertNumToStr(long num) {
//        long num = Long.valueOf(str);
        while (num != 0) {
            int len = String.valueOf(num).length();
            int rem;
            switch (len) {
                // <editor-fold defaultstate="collapsed" desc="Employee Details">
                case 1:
                    rem = Integer.parseInt(String.valueOf(num));
                    string += a[rem];
                    num /= 10;
                    break;

                case 2:
                    rem = Integer.parseInt(String.valueOf(num));
                    if (rem > 9 && rem < 20) {
                        string += c[rem - 10];
                        num /= 100;
                    } else if (rem % 10 == 0) {
                        string += d[(rem / 10) - 2];
                        num /= 100;
                    } else {
                        string += d[(rem / 10) - 2];
                        string += " ";
                        num %= 10;
                    }
                    break;

                case 3:
                    rem = Integer.parseInt(String.valueOf(num));
                    if (rem % 100 == 0) {
                        string += a[rem / 100];
                        string += " " + b[0];
                        num %= 100;
                    } else {
                        string += a[rem / 100];
                        string += " " + b[0] + " and ";
                        num %= 100;
                    }
                    break;

                case 4:
                    rem = Integer.parseInt(String.valueOf(num));
                    if (rem % 1000 == 0) {
                        string += a[rem / 1000];
                        string += " " + b[1];
                        num %= 1000;
                    } else {
                        string += a[rem / 1000];
                        string += " " + b[1] + " ";
                        num %= 1000;
                    }
                    break;

                case 5:
                    rem = Integer.parseInt(String.valueOf(num));
                    if (rem % 1000 == 0) {
                        Convert(rem / 1000);
                        string += " " + b[1];
                        num %= 1000;
                    } else {
                        Convert(rem / 1000);
                        string += " " + b[1] + " ";
                        num %= 1000;
                    }
                    break;

                case 6:
                    rem = (int) (num / 100000);
                    if (num % 100000 == 0) {
                        string += a[rem];
                        string += " " + b[2];
                        num %= 100000;
                    } else {
                        string += a[rem];
                        string += " " + b[2] + " ";
                        num %= 100000;
                    }
                    break;

                case 7:
                    if (num % 100000 == 0) {
                        Convert((int) num / 100000);
                        string += " " + b[2];
                        num %= 100000;
                    } else {
                        Convert((int) num / 100000);
                        string += " " + b[2] + " ";
                        num %= 100000;
                    }
                    break;

                case 8:
                    rem = (int) (num / 10000000);
                    if (num % 10000000 == 0) {
                        string += a[rem];
                        string += " " + b[3];
                        num %= 10000000;
                    } else {
                        string += a[rem];
                        string += " " + b[3] + " ";
                        num %= 10000000;
                    }
                    break;

                case 9:
                    if (num % 10000000 == 0) {
                        Convert((int) num / 10000000);
                        string += " " + b[3];
                        num %= 10000000;
                    } else {
                        Convert((int) num / 10000000);
                        string += " " + b[3] + " ";
                        num %= 10000000;
                    }
                    break;

                case 10:
                    rem = (int) (num / 1000000000);
                    if (num % 1000000000 == 0) {
                        string += a[rem];
                        string += " " + b[0];
                        string += " " + b[3];
                        num %= 1000000000;
                    } else {
                        string += a[rem];
                        string += " " + b[0] + " and ";
                        num %= 1000000000;
                    }
                    break;

                case 11:
                    rem = (int) (num / 10000000000l);
                    if (num % 10000000000l == 0) {
                        string += a[rem];
                        string += " " + b[1];
                        string += " " + b[3];
                        num %= 10000000000l;
                    } else {
                        string += a[rem];
                        string += " " + b[1] + " ";
                        num %= 10000000000l;
                    }
                    break;

                case 12:
                    rem = (int) (num / 10000000000l);
                    if (num % 10000000000l == 0) {
                        Convert(rem);
                        string += " " + b[1];
                        string += " " + b[3];
                        num %= 10000000000l;
                    } else {
                        Convert(rem);
                        string += " " + b[1] + " ";
                        num %= 10000000000l;
                    }
                    break;
                // </editor-fold>
            }
        }
//        string += " Only.";
        return string;
    }

    public void Convert(int no) {
        while (no != 0) {
            if (no < 10) {
                string += a[no];
                no /= 10;
            } else if (no > 9 && no < 20) {
                string += c[no - 10];
                no /= 100;
            } else if (no % 10 == 0) {
                string += d[(no / 10) - 2];
                no /= 100;
            } else {
                string += d[(no / 10) - 2];
                string += " ";
                no %= 10;
            }
        }
    }

//    public static void main(String[] args) {
//        String amo = "123456789000";
////        String amo = "979";
//        System.out.println(amo + " Amount of Rupees " + ConvertNumToStr(amo));
//    }
}
