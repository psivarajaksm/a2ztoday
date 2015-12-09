package com.onward.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 *
 * @author Jagan Mohan. B
 */

public class DBConnection implements Serializable {
    private static DBConnection dBConnection = null;
	private static DataSource dataSource;
       
    private DBConnection() throws SQLException {
        initialize();
    }
        
    private void initialize() throws SQLException {
		String jndiName   = "java:e2sJndi";
		//String jndiName   = "java:e2sReportsJndi";
        Context context = null;
		try {
            context = new InitialContext();
            dataSource = (DataSource)context.lookup(jndiName);
		} catch(NameNotFoundException ex) {
            ex.printStackTrace();
		} catch (NamingException ex) {
            ex.printStackTrace();
		} catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(context != null)
                    context.close();
            }catch(NamingException ex) {
                ex.printStackTrace();
            }
		}
	}
    
    public static synchronized DBConnection getInstance() throws SQLException {
        if (dBConnection == null) {
            dBConnection = new DBConnection();
        }
        return dBConnection;
    }  
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();			
    }

}
