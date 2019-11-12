/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library_access_api;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * @author Max
 */
public class test_driver {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        /*
        ArrayList<String> s_AList1 = new ArrayList<String>();	
        
        String s1 = "harry";
        String s2 = "rowling";

        s_AList1.add(s1);
        //s_AList1.add(s2);

        ArrayList<HomeSearchBook> book1;
        book1 = HomeSearchAPI.SearchBooks(s_AList1);

        for(int i = 0; i < book1.size(); i++) {
                System.out.println(i+1 + ") " + "ISBN: " + book1.get(i)._ISBN + " Title: " + book1.get(i)._Title + " Authors: " + book1.get(i)._Authors + " Availability: " + book1.get(i)._Availability_Flag);
        }
        */
        
        /*
        boolean btest = CheckOutAPI.get_CheckOut_Conditions("2", "9780001047976");
        System.out.println(btest);
        
        System.out.println(DatabaseAccessAPI.getToday());
        System.out.println(DatabaseAccessAPI.getDue_FromToday(14));
        
        System.out.println(CheckOutAPI.FormatCheckOut_DoneSqlString("9780439139595", "1"));
        */
        
        /*
        String isbn_Str = "978"; 
        String cardId_Str = "1"; 
        String borrower_Str = "Mark";
        
        String searchStr = "Matched strings for (ISBN, '" + isbn_Str + "'), (Card Id, '" + cardId_Str + "'), (Borrower, '" + borrower_Str + "')";
        System.out.println(searchStr);
        
        
        String sqlString = 
            "SELECT T2.*\n" +
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
        
        System.out.println(sqlString);
        */
        
        
        ArrayList<UpdateFines_Data> U = UpdateFinesAPI.UpdateFines_Total_Data_Done();
        
        for(int i = 0; i < U.size(); i++){
            System.out.println("LoanID: " + U.get(i)._Loan_id + " FineAMT: " + U.get(i)._FineAmt/.25);
        }
        
    }
}
