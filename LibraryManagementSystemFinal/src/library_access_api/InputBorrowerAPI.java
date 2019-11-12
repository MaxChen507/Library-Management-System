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
public class InputBorrowerAPI {

    private static String FormatCheckInConditionSqlString(String ssn_Str) {
        String sqlString = "SELECT * FROM BORROWER WHERE Ssn = '"+ssn_Str+"';";
        return sqlString;
    }
    
    public static boolean get_InputBorrower_Conditions(String ssn_Str) {
        boolean totalCheck_outCondition = false;
        
        String sqlString = FormatCheckInConditionSqlString(ssn_Str);
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        
        try{
            if(!rs.next()){
                totalCheck_outCondition = true;
            }
        }
        catch(SQLException ex) 
        {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalCheck_outCondition;
    }

    public static String FormatInputBorrower_DoneSqlString(String name_Str, String ssn_Str, String pnumber_Str, String address_Str, boolean pnumber_Str_flag){
        String sqlString;
        
        if(pnumber_Str_flag == true){
            sqlString = 
                "INSERT INTO BORROWER (Ssn, Bname, Address, Phone)\n" +
                "VALUES ('"+ssn_Str+"', '"+name_Str+"', '"+address_Str+"', '"+pnumber_Str+"');";
        }
        else{
            sqlString = 
                "INSERT INTO BORROWER (Ssn, Bname, Address, Phone)\n" +
                "VALUES ('"+ssn_Str+"', '"+name_Str+"', '"+address_Str+"', NULL);";
        }
        return sqlString;
    }
    
    public static void CheckOut_Done(String name_Str, String ssn_Str, String pnumber_Str, String address_Str, boolean pnumber_Str_flag) {
        String sqlString = FormatInputBorrower_DoneSqlString(name_Str, ssn_Str, pnumber_Str, address_Str, pnumber_Str_flag);
        DatabaseAccessAPI.MyExecuteStmt(sqlString);
    }
    
}
