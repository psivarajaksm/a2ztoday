/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.util;

import com.onward.common.ApplicationConstants;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author Jagan Mohan. B
 */
public class AppProps {

    String webPath = "/WEB-INF/config";
    private Properties props = null;
    private static AppProps instance = null;
    private String sessionid = null;
    private String toDayDate = null;
    private String horizontalscroll = null;
    private String verticalscroll = null;
    Logger logger = Logger.getLogger("com.onward.util.AppProps");
    private String fileName = "/AppProps.properties";
    private String realPath = "";
    private String jbossHome = "";

    public void loadProps() {
        System.out.println("============loadProps=======");
        FileInputStream in = null;
        try {
            fileName = this.props.getProperty(ApplicationConstants.APP_CONTEXT) + webPath + fileName;
            realPath = this.props.getProperty(ApplicationConstants.APP_CONTEXT);
            in = new FileInputStream(fileName);
            props.load(in);
            in.close();

            jbossHome = System.getenv("jboss_location");

            if (realPath != null) {
                System.out.println("#############################################realPath: " + realPath);
                jbossHome = realPath.substring(0, realPath.indexOf("default") + 7);
                System.out.println("#############################################JBoss Home: " + jbossHome);
                fileName = jbossHome + "/deploy/tncscProps.properties";
                in = new FileInputStream(fileName);
                props.load(in);
                in.close();
            }

        } catch (IOException ie) {
            logger.info("IO Exception: " + ie.toString());
        } catch (Exception e) {
            logger.info("Exception: " + e.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private AppProps() {
        props = new Properties();
    }

    public static AppProps getInstance() {
        if (instance == null) {
            instance = new AppProps();
        }
        return instance;
    }

    public void setProperty(String key, String value) {
        this.props.setProperty(key, value);
    }

    public String getProperty(String key) {
        return this.props.getProperty(key);
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getToDayDate() {
        return toDayDate;
    }

    public void setToDayDate(String toDayDate) {
        this.toDayDate = toDayDate;
    }

    public String getHorizontalscroll() {
        return horizontalscroll;
    }

    public void setHorizontalscroll(String horizontalscroll) {
        this.horizontalscroll = horizontalscroll;
    }

    public String getVerticalscroll() {
        return verticalscroll;
    }

    public void setVerticalscroll(String verticalscroll) {
        this.verticalscroll = verticalscroll;
    }
}
