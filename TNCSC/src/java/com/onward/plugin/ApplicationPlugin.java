/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.plugin;

import com.onward.common.ApplicationOptions;
import com.onward.util.AppProps;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 *
 * @author Jagan Mohan. B
 */
public class ApplicationPlugin implements PlugIn {

    String todate = "";
    String webPath = "/WEB-INF/config";

    public ApplicationPlugin() {
        SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy");
        todate = sdfOutput.format(new Date());
    }

    public void destroy() {
        ApplicationOptions.getInstance().clearResources();
    }

    @Override
    public void init(ActionServlet arg0, ModuleConfig arg1) throws ServletException {
        // Date Converter
        java.util.Date defaultValue = null;
        DateConverter dateConverter = new DateConverter(defaultValue);
        dateConverter.setPattern("dd/MM/yyyy");
        ConvertUtils.register(dateConverter, java.util.Date.class);
        //BigDecimalConverter bdc=new BigDecimalConverter();
        //bdc.
        ConvertUtils.register(new BigDecimalConverter(null), java.math.BigDecimal.class);
        //System.out.println("DATE CONVERTER REGISTERED");

        // Application Option for Reference Codes
      //  ApplicationOptions.getInstance();

        // AppProps for get the Server Date
        AppProps.getInstance();
        AppProps.getInstance().setToDayDate(todate);

        String appContext = arg0.getServletContext().getRealPath("/");
        System.out.println("appContext===="+appContext);
        AppProps app = AppProps.getInstance();
        app.setProperty("appContext", appContext);
        app.loadProps();
    }
}
