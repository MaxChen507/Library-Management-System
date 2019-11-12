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
public class CheckOutAPI {
    private static String FormatCheckOutConditionSqlString_B_not_reg(String str_card_ID){
        String sqlString = "SELECT * FROM BORROWER WHERE Card_id = '"+str_card_ID+"';";
        return sqlString;
    }
    
    private static String FormatCheckOutConditionSqlString_Bk_already_out(String str_Isbn){
        String sqlString = "SELECT * FROM view_books_available WHERE Available_isbn = '"+str_Isbn+"';";
        return sqlString;
    }
    
    private static String FormatCheckOutConditionSqlString_B_no_loan(String str_card_ID){
        String sqlString = "SELECT * FROM BOOK_LOANS WHERE BOOK_LOANS.Card_id = '"+str_card_ID+"' ;";
        return sqlString;
    }
    
    private static String FormatCheckOutConditionSqlString_loan_out(String str_card_ID){
        String sqlString = 
                "SELECT Card_id, COUNT(Loan_id) AS Num_books_checkedOut \n" +
"		FROM BOOK_LOANS \n" +
"		WHERE\n" +
"			(Card_id = '"+str_card_ID+"') \n" +
"			AND\n" +
"			(BOOK_LOANS.Date_out IS NOT NULL AND BOOK_LOANS.Date_in IS NULL)\n" +
"		GROUP BY Card_id";
        return sqlString;
    }
    
    private static String FormatCheckOutConditionSqlString_B_max_three(String str_card_ID){
        String sqlString = 
                "SELECT * \n" +
                "FROM\n" +
                "	(\n" +
                "		SELECT Card_id, COUNT(Loan_id) AS Num_books_checkedOut \n" +
                "		FROM book_loans \n" +
                "		WHERE\n" +
                "			(Card_id = '"+str_card_ID+"') \n" +
                "			AND\n" +
                "			(BOOK_LOANS.Date_out IS NOT NULL AND BOOK_LOANS.Date_in IS NULL)\n" +
                "		GROUP BY Card_id\n" +
                "	) AS T1\n" +
                "WHERE T1.Num_books_checkedOut < 3;";
        return sqlString;
    }
         
    public static boolean get_CheckOut_Conditions(String str_card_ID, String str_Isbn){
        
        boolean totalCheck_outCondition = false;
        
        String sqlString1 = FormatCheckOutConditionSqlString_B_not_reg(str_card_ID);
	String sqlString2 = FormatCheckOutConditionSqlString_Bk_already_out(str_Isbn);
        String sqlString3 = FormatCheckOutConditionSqlString_B_no_loan(str_card_ID);
        String sqlString4 = FormatCheckOutConditionSqlString_loan_out(str_card_ID);
        String sqlString5 = FormatCheckOutConditionSqlString_B_max_three(str_card_ID);
        
        // may not need to calculate all result sets before hand
        // rename result sets (meaningful)
	ResultSet rs1 = DatabaseAccessAPI.MyExecuteQuery(sqlString1);
        ResultSet rs2 = DatabaseAccessAPI.MyExecuteQuery(sqlString2);
        ResultSet rs3 = DatabaseAccessAPI.MyExecuteQuery(sqlString3);
        ResultSet rs4 = DatabaseAccessAPI.MyExecuteQuery(sqlString4);
        ResultSet rs5 = DatabaseAccessAPI.MyExecuteQuery(sqlString5);
        
        boolean C_Borrower_Is_Registered = false;
        boolean C_Book_Is_Avail = false;
        boolean C_Borrower_Has_Less_Three = false;
        
        try {
            if (rs1.next()){
                C_Borrower_Is_Registered = true;
            }
            
            if (rs2.next()){
                C_Book_Is_Avail = true;
            }
            
            if (rs3.next()){
                if(rs4.next()){
                    if(rs5.next()){
                        C_Borrower_Has_Less_Three = true;
                    }
                }
                else{
                    C_Borrower_Has_Less_Three = true;
                }
            }
            else{
                C_Borrower_Has_Less_Three = true;
            }
        } 
        catch(SQLException ex) 
        {
            System.out.println("Error: " + ex.getMessage());
        }	
        
        //System.out.println("Condition 1: " + C_Borrower_Is_Registered);
        //System.out.println("Condition 2: " + C_Book_Is_Avail);
        //System.out.println("Condition 3: " + C_Borrower_Has_Less_Three);
        
        if(C_Borrower_Is_Registered && C_Book_Is_Avail && C_Borrower_Has_Less_Three){
            totalCheck_outCondition = true;
            //System.out.println("CAN check_out");
        }
        else{
            totalCheck_outCondition = false;
            //System.out.println("can NOT check_out");
        }
        
        return totalCheck_outCondition;
    }
    
    public static String FormatCheckOut_DoneSqlString(String isbn_Str, String cardId_Str){
        String sqlString = 
                "INSERT INTO BOOK_LOANS (Isbn, Card_id, Date_out, Due_date, Date_in)\n" +
                "VALUES ("+isbn_Str+", "+cardId_Str+", '"+DatabaseAccessAPI.getToday()+"', '"+DatabaseAccessAPI.getDue_FromToday(14)+"', NULL);";
        return sqlString;
    }
    
    public static void CheckOut_Done(String isbn_Str, String cardId_Str) {
        String sqlString = FormatCheckOut_DoneSqlString(isbn_Str,cardId_Str);
        DatabaseAccessAPI.MyExecuteStmt(sqlString);
    }
    
}
