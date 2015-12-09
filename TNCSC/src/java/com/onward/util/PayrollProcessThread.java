/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onward.dao.EmployeePayBillProcess;
import com.onward.dao.OeslModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Onward
 */
public class PayrollProcessThread implements Runnable {

    private Thread t;
    private String threadName;
    public HttpServletRequest request;
    public HttpServletResponse response;

    public PayrollProcessThread(String name, HttpServletRequest request, HttpServletResponse response) {
        threadName = name;
        this.request = request;
        this.response = response;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running " + threadName);
        try {
            OeslModule notOnWeekendsModule = new OeslModule();
            Injector injector = Guice.createInjector(notOnWeekendsModule);
            EmployeePayBillProcess employeePayBillProcessObj = (EmployeePayBillProcess) injector.getInstance(EmployeePayBillProcess.class);
            System.out.println("request "+ request);
            employeePayBillProcessObj.startPayRollProcess(null, request, response, null, null);
//            for (int i = 4000; i > 0; i--) {
//                System.out.println("Thread: " + threadName + ", " + i);
//                // Let the thread sleep for a while.
            Thread.sleep(1);
//            }
        } catch (Exception e) {
            System.out.println("Thread " + threadName + " interrupted.");
            e.printStackTrace();
        }
        System.out.println("Thread " + threadName + " exiting.");
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
