/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library_access_api;

import java.sql.*;
import java.util.*;

/**
 *
 * @author Max
 */
public class ResolveFinesAPI {
        
    private static String FormatBorrowerFines_SqlString(){
        String sqlString = 
            "SELECT T2.Card_id, SUM(T2.Fine_amt) AS Sum_Fines\n" +
            "FROM\n" +
            "	(\n" +
            "	SELECT Card_id, Fine_amt\n" +
            "	FROM\n" +
            "		(\n" +
            "		SELECT Card_id, Fine_amt, Paid \n" +
            "		FROM BOOK_LOANS\n" +
            "		LEFT JOIN FINES ON BOOK_LOANS.Loan_id = FINES.Loan_id\n" +
            "		) AS T1\n" +
            "	WHERE Paid = false\n" +
            "	) AS T2\n" +
            "GROUP BY T2.Card_id";
        return sqlString;
    }
    
    public static ArrayList<ResolveFines_Data> BorrowerFines_Data_Done(){
        ArrayList<ResolveFines_Data> rf_data = new ArrayList<ResolveFines_Data>();
        
        String sqlString = FormatBorrowerFines_SqlString();
        
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        if (rs!=null){
            try{
                while (rs.next()){
                    // Populate field variables

                    String Card_id = rs.getString("Card_id");
                    String SumFineAmt = rs.getString("Sum_Fines");
                    
                    ResolveFines_Data uf_temp = new ResolveFines_Data(Card_id, SumFineAmt);

                    rf_data.add(uf_temp);
                }
            }
            catch(SQLException ex){
                System.out.println("Error in connection: " + ex.getMessage());
            }
        }
        return rf_data;
    }

    private static String FormatResolveFines_ConditionsSqlString(String Card_Id_Str){
        String sqlString = 
            "SELECT *\n" +
            "FROM BOOK_LOANS\n" +
            "LEFT JOIN FINES ON BOOK_LOANS.Loan_id = FINES.Loan_id\n" +
            "WHERE Card_id = '"+Card_Id_Str+"' AND Paid = false;";
        return sqlString;
    }

    public static boolean get_ResolveFines_CardID_ExistsCondition(String Card_Id_Str) {
        boolean ResolveFines_Conditions = false;
        
        String sqlString = FormatResolveFines_ConditionsSqlString(Card_Id_Str);
        
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        
        try {
            if(rs.next()){
                ResolveFines_Conditions = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        return ResolveFines_Conditions;      
    }
    
    private static String FormatBorrowerFines_CardID(String Card_Id_Str){
        String sqlString = 
            "SELECT BOOK_LOANS.Loan_id, Card_id, Fine_amt, Paid\n" +
            "FROM BOOK_LOANS\n" +
            "LEFT JOIN FINES ON BOOK_LOANS.Loan_id = FINES.Loan_id\n" +
            "WHERE Card_id = '"+Card_Id_Str+"' AND Paid = false;";
        return sqlString;
    }
    
    public static ArrayList<String> BorrowerFines_getLoanID_Data_Done(String Card_Id_Str){
        ArrayList<String> loanIdList = new ArrayList<String>();
        
        String sqlString = FormatBorrowerFines_CardID(Card_Id_Str);
        
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        if (rs!=null){
            try{
                while (rs.next()){
                    // Populate field variables

                    String loanId = rs.getString("Loan_id");

                    loanIdList.add(loanId);
                }
            }
            catch(SQLException ex){
                System.out.println("Error in connection: " + ex.getMessage());
            }
        }
        return loanIdList;
    }
    
    public static String FormatResolveFines_UpdateTuple(String Card_Id_Str){
        String sqlString = 
            "UPDATE FINES\n" +
            "SET Paid = true\n" +
            "WHERE Loan_id = '"+Card_Id_Str+"';";
        return sqlString;
    }
    
    public static void ResolveFines_Done(String Card_Id_Str) {
        ArrayList<String> Loan_idToResolveList = BorrowerFines_getLoanID_Data_Done(Card_Id_Str);
        
        for(int i = 0; i < Loan_idToResolveList.size(); i++){
            String sqlString = FormatResolveFines_UpdateTuple(Loan_idToResolveList.get(i));
            DatabaseAccessAPI.MyExecuteStmt(sqlString);
        }
    }
}
