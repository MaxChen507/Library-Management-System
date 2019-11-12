/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library_access_api;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Max
 */
public class CheckInAPI {
    private static String FormatCheckInConditionSqlString(String str_loan_ID){
        String sqlString = "SELECT * FROM BOOK_LOANS WHERE Loan_id = '"+str_loan_ID+"' AND Date_in IS NULL;";
        return sqlString;
    }
    
    public static boolean get_CheckIn_Conditions(String str_loan_ID){
        boolean totalCheck_outCondition = false;

        String sqlString = FormatCheckInConditionSqlString(str_loan_ID);
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);

        try {
            if (rs.next()){
                totalCheck_outCondition = true;
            }

        } 
        catch(SQLException ex) 
        {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalCheck_outCondition;
    }
    
    public static String FormatCheckIn_DoneSqlString(String str_loan_ID){
        String sqlString = 
            "UPDATE BOOK_LOANS\n" +
            "SET Date_in = '"+DatabaseAccessAPI.getToday()+"'\n" +
            "WHERE Loan_id = '"+str_loan_ID+"';";
        return sqlString;
    }
    
    public static void CheckOut_Done(String str_loan_ID) {
        String sqlString = FormatCheckIn_DoneSqlString(str_loan_ID);
        DatabaseAccessAPI.MyExecuteStmt(sqlString);
    }
}
