/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onward.dao;
import com.onward.common.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Jagan Mohan. B
 */
public class OnwardDAO {
    Logger logger = null;
    
    public OnwardDAO() {
        logger = Logger.getLogger("com.empower");
    }
    public Connection getConnection() {
        try {
            return DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            logger.error("Exception in GMDAO.getConnection(): " + e.getMessage());
            return null;
        }
    }

    public java.sql.Date postgresDate(String date) {
        java.sql.Date sqlDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if(date != null && date.length()>0) {
                java.util.Date utilDate = dateFormat.parse(date);
                sqlDate = new java.sql.Date(utilDate.getTime());
            }
            else
                sqlDate = null;
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(OnwardDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return sqlDate;
    }

    public void closeConnection(Connection con, ResultSet rs, PreparedStatement ps)  {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if ((con != null) && (!con.isClosed())) {
                con.close();
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(OnwardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double doubleValue(String value) {
        if (value != null && !("".equals(value))) {
            return Double.parseDouble(value);
        } else {
            return 0.00d;
        }
    }
    public long longValue(String value) {
        if (value != null && !("".equals(value))) {
            return Long.parseLong(value);
        } else {
            return 0;
        }
    }
    public int intValue(String value) {
        if (value != null && !("".equals(value))) {
            return Integer.parseInt(value);
        } else {
            return 0;
        }
    }

    public String getMonthName(String monthname){
        if(monthname.equalsIgnoreCase("1") || monthname.equalsIgnoreCase("01"))     monthname = "January";
        else if(monthname.equalsIgnoreCase("2") || monthname.equalsIgnoreCase("02"))     monthname = "February";
        else if(monthname.equalsIgnoreCase("3") || monthname.equalsIgnoreCase("03"))     monthname = "March";
        else if(monthname.equalsIgnoreCase("4") || monthname.equalsIgnoreCase("04"))     monthname = "April";
        else if(monthname.equalsIgnoreCase("5") || monthname.equalsIgnoreCase("05"))     monthname = "May";
        else if(monthname.equalsIgnoreCase("6") || monthname.equalsIgnoreCase("06"))     monthname = "June";
        else if(monthname.equalsIgnoreCase("7") || monthname.equalsIgnoreCase("07"))     monthname = "July";
        else if(monthname.equalsIgnoreCase("8") || monthname.equalsIgnoreCase("08"))     monthname = "August";
        else if(monthname.equalsIgnoreCase("9") || monthname.equalsIgnoreCase("09"))     monthname = "September";
        else if(monthname.equalsIgnoreCase("10"))     monthname = "October";
        else if(monthname.equalsIgnoreCase("11"))     monthname = "November";
        else if(monthname.equalsIgnoreCase("12"))     monthname = "December";
        return monthname;
    }

}
