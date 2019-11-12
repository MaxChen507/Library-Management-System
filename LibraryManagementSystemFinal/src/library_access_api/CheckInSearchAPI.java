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
public class CheckInSearchAPI {
    private static String FormatSearchLoanSqlString_(String isbn_Str, String cardId_Str, String borrower_Str){
        String sqlString = 
            "SELECT T1.Bname, T2.*\n" +
            "FROM\n" +
            "	(\n" +
            "		SELECT Card_id, Bname\n" +
            "		FROM BORROWER\n" +
            "		WHERE BORROWER.Bname LIKE '%"+borrower_Str+"%'\n" +
            "	) AS T1\n" +
            "INNER JOIN\n" +
            "	(\n" +
            "		SELECT *\n" +
            "		FROM BOOK_LOANS\n" +
            "		WHERE \n" +
            "			BOOK_LOANS.Isbn LIKE '%"+isbn_Str+"%' \n" +
            "			AND  \n" +
            "			BOOK_LOANS.Card_id LIKE '%"+cardId_Str+"%'\n" +
            "	) AS T2\n" +
            "ON T1.Card_id = T2.Card_id\n" +
            "WHERE Date_in IS NULL;";
        
        return sqlString;
    }

    public static ArrayList<CheckInSearch_Loan_Data> SearchLoans(String isbn_Str, String cardId_Str, String borrower_Str) {
        ArrayList<CheckInSearch_Loan_Data> loans = new ArrayList<CheckInSearch_Loan_Data>();

        // <editor-fold desc="Query for all available">
        String sqlString = FormatSearchLoanSqlString_(isbn_Str, cardId_Str, borrower_Str);

        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        if (rs!=null)
        {				
            try {
                while (rs.next()) {
                    // Populate field variables
                    
                    String Bname = rs.getString("Bname");
                    String LoanId = rs.getString("Loan_id"); 
                    String ISBN = rs.getString("Isbn");
                    String CardId = rs.getString("Card_id");
                    String DateOut = rs.getString("Date_out");
                    String DueDate = rs.getString("Due_date");
                    //String DateIn = rs.getString("Date_in");
                    String DateIn = "NULL";

                    CheckInSearch_Loan_Data l_temp = new CheckInSearch_Loan_Data(Bname, LoanId, ISBN, CardId, DateOut, DueDate, DateIn);
                    loans.add(l_temp);					
                }
            }
            catch(SQLException ex) 
            {
                    System.out.println("Error in connection: " + ex.getMessage());
            }	
        }
        // </editor-fold>
        
        return loans;
    }
}
