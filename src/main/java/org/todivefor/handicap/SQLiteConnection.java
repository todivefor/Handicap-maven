/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.todivefor.handicap;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author peterream
 */
public class SQLiteConnection
{
//	Connection conn = null;
    	public static Connection connection;				// DB connection
        
    public static Connection dbConnector(String dbPath)
    {
        try
        {
            //  SQLite JDBC driver
            //  Video 4 - https://www.youtube.com/watch?v=l7IDevUUa3A&index=7&list=PLS1QulWo1RIbYMA5Ijb72QHaHvCrPKfS2

            Class.forName("org.sqlite.JDBC");                               // Return Class object
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath); // Open DB path
            if (HandicapMain.debug)                                         // Debug?
            {
                JOptionPane.showMessageDialog(null, "Handicap Connection was successful");  // Yes, show message
            }
            return conn;                                                    // Class object
        } 
        catch (HeadlessException | ClassNotFoundException | SQLException e) // Exception unknown
        {
            JOptionPane.showMessageDialog(null, e);
            if (HandicapMain.debug)
            {
                e.printStackTrace();
            }
            return null;                                                    // Just get out
        }
    }

/*
 *  Beginning of common methods within SQLiteConnection class
 */
    
    /**
     * This method will close the Scores and Courses Database
     *
     * @param connection
     */
    public static void closeHandicapDB(Connection connection)
    {
        try
        {
            connection.close();                                         // Close connection
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
}
