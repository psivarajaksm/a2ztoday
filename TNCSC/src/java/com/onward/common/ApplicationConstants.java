/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.common;

/**
 *
 * @author Jagan Mohan. B
 */
public class ApplicationConstants {
    public ApplicationConstants() {
    }

    /**
     * Below Constants Path's
     */
    public static final String APP_CONTEXT = "appContext";
    public static final String STATIC_IP_PATH = "tncscpath";
    public static final String CONTEXT_NAME = "contextname";

    /**
     * Below Constants are Type of User
     */
    public static final String SUPER_USER = "O";
    public static final String ADMIN_USER = "A";
    public static final String DEPARTMENT_USER = "D";
    public static final String ELCOT_ACCOUNT_USER = "E";
    public static final String PAYMENT_APPROVAL_USER = "P";
    public static final String SUPPLIER_USER = "S";
    public static final String FACTORY_INSPECTOR_USER = "F";
    public static final String DELIVERY_INSPECTOR_USER = "I";
    public static final String REPORTS_USER = "R";
    public static final String DATAENTRY_USER = "X";
    public static final String REFER_PRODUCT_DETAILS = "PD";

    /**
     * Below Constants are Type of User status
     */
    public static final String NEW_STATUS = "1";
    public static final String ACTIVE_STATUS = "2";
    public static final String DISABLED_STATUS = "3";
    public static final String DELETE_STATUS = "4";

    /**
     * Below Constants is Default Password
     */
    public static final String DEFAULT_PASSWORD = "pass@123";

    /**
     * Below Constants are for Transaction Purpose
     */
    public static final String ALLOTTED = "1";
    public static final String LINE_INSPECTION = "2";
    public static final String PRODUCT_INSPECTION = "3";
    public static final String PACKING_INSPECTION = "4";
    public static final String SUPPLIED = "5";
    public static final String ACCEPTED = "6";
    public static final String REJECTED = "7";
    public static final String COMPLETED = "8"; // This flag common for All process completion.
    public static final String DELETED = "9";

    public static final String LABTEST_COMPLETED = "Y"; // System Identified

    public static final String DEMO_COMPLETED = "Y";
    public static final String DEMO_NOT_COMPLETED = "N";
    public static final String ACCEPTEDSTATUS = "A";
    public static final String REJECTEDSTATUS = "R";
    public static final String TESTTYPESTATUS = "P";
    public static final String ACCEPTANCETYPESTATUS = "Q";
    public static final String FIRSTSAMPLETEMPORARY = "X";
    public static final String SECONDSAMPLETEMPORARY = "Y";
    public static final String TEMPORARY = "T";
    public static final String FIRSTSAMPLE = "F";
    public static final String SECONDSAMPLE = "S";
    public static final String SUPPLIERDELIVERY = "S";
    public static final String PAYMENT_INITIATION = "I";
    public static final String PAYMENT_APPROVED = "A";
    public static final String PAYMENT_RELEASED = "R";
    public static final String DEMO_STATUS = "Y";
    public static final String TESTTYPE = "Test";
    public static final String ACCEPTANCETYPE = "Acceptance";
    public static final String ISBENEFICIARY_UPDATE_YES = "Y";
    public static final String ISBENEFICIARY_UPDATE_NO = "N";

    public static final String FAN = "TF";
    public static final String MIXIE = "FM";
    public static final String GRINDER = "WG";

    //Product Detail status

    public static final String V = "Verified";
    public static final String D = "Deleted";
}
