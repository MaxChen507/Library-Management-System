/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library_access_api;

import java.sql.*;
import java.text.*;
import java.util.*;

/**
 *
 * @author Max
 */
public class DatabaseAccessAPI {

    private static String _databaseConnectionStr="jdbc:mysql://localhost:3306/";
    private static String _userId="root";
    private static String _userPwd="";
    private static String _use_databaseStmt="USE Library;";
    private static Connection _connection = null;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static java.util.Date todayDate = new java.util.Date();

    public DatabaseAccessAPI()
    {

    }

    public static void MyExecuteStmt(String stmt_String)
    {
        try
        {
            /* Create a connection to the local MySQL server, with the NO database selected. */
            _connection = DriverManager.getConnection(_databaseConnectionStr, _userId,_userPwd);

            /* Create a SQL statement object and execute the query. */
            Statement stmt = _connection.createStatement();

            /* Set the current database, if not already set in the getConnection */
            /* Execute a SQL statement */
            stmt.execute(_use_databaseStmt);

            /* Execute a SQL query using SQL as a String object */
            /* 
             * Only the columns listed in the query will be available in the ResultSet object
             * Note: If 'AS' is used to alias column name, these will be the names in the ResultSet object
             */
            stmt.execute(stmt_String);                
        }
        catch(SQLException ex) 
        {
                System.out.println("Error in connection: " + ex.getMessage());
        }
    }

    public static ResultSet MyExecuteQuery(String queryString)
    {
        try
        {
            /* Create a connection to the local MySQL server, with the NO database selected. */
            _connection = DriverManager.getConnection(_databaseConnectionStr, _userId,_userPwd);

            /* Create a SQL statement object and execute the query. */
            Statement stmt = _connection.createStatement();

            /* Set the current database, if not already set in the getConnection */
            /* Execute a SQL statement */
            stmt.execute(_use_databaseStmt);

            /* Execute a SQL query using SQL as a String object */
            /* 
             * Only the columns listed in the query will be available in the ResultSet object
             * Note: If 'AS' is used to alias column name, these will be the names in the ResultSet object
             */
            ResultSet rs = stmt.executeQuery(queryString);

            return rs;
        }
        catch(SQLException ex) 
        {
                System.out.println("Error in connection: " + ex.getMessage());
        }	
        return null;
    }

    private static java.util.Date addDays(java.util.Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String getToday(){
        todayDate = java.sql.Date.valueOf(java.time.LocalDate.now());

        String todayDate_str = sdf.format(todayDate);

        return todayDate_str;
    }

    public static String getDue_FromToday(int days){
        todayDate = java.sql.Date.valueOf(java.time.LocalDate.now());

        java.util.Date dueDate = new java.util.Date();
        dueDate = addDays(todayDate, 14);
        String dueDate_str = sdf.format(dueDate);

        return dueDate_str;
    }
           
}
