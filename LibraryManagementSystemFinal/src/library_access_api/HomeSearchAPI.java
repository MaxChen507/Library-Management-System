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
public class HomeSearchAPI {
    private static String FormatSearchBookSqlString_Avail(ArrayList<String> str)
    {
        String sqlString = 
            "SELECT T3.*\r\n" + 
            "FROM\r\n" + 
            "	(\r\n" + 
            "		SELECT T1.*\r\n" + 
            "		FROM view_isbn_title_listauthors AS T1, view_books_available AS T2\r\n" + 
            "		WHERE T1.isbn = T2.available_isbn\r\n" + 
            "	) AS T3\r\n" +
            " WHERE ";

        for(int i = 0; i < str.size(); i++) {
            if(i == str.size() -1) {
                    sqlString = sqlString + "(T3.Isbn LIKE '%" + str.get(i) + "%' OR T3.Title LIKE '%" + str.get(i) + "%' OR T3.Author_List LIKE '%" + str.get(i) + "%');";
            }
            else {
                    sqlString = sqlString + "(T3.Isbn LIKE '%" + str.get(i) + "%' OR T3.Title LIKE '%" + str.get(i) + "%' OR T3.Author_List LIKE '%" + str.get(i) + "%') AND ";
            }
        }

        return sqlString;
    }

    private static String FormatSearchBookSqlString_UnAvail(ArrayList<String> str) 
    {
        String sqlString =  
            "SELECT T3.*\r\n" +
            "FROM\r\n" +
            "	(\r\n" +
            "		SELECT T1.*\r\n" +
            "		FROM view_isbn_title_listauthors AS T1, view_books_unavailable AS T2\r\n" +
            "		WHERE T1.isbn = T2.unavailable_isbn\r\n" +
            "	) AS T3\r\n" +
            "WHERE "; 

        for(int i = 0; i < str.size(); i++) {
            if(i == str.size() -1) {
                    sqlString = sqlString + "(T3.Isbn LIKE '%" + str.get(i) + "%' OR T3.Title LIKE '%" + str.get(i) + "%' OR T3.Author_List LIKE '%" + str.get(i) + "%');";
            }
            else {
                    sqlString = sqlString + "(T3.Isbn LIKE '%" + str.get(i) + "%' OR T3.Title LIKE '%" + str.get(i) + "%' OR T3.Author_List LIKE '%" + str.get(i) + "%') AND ";
            }
        }

        return sqlString;
    }

    public static ArrayList<HomeSearch_Book_Data> SearchBooks(ArrayList<String> str)
    {
        ArrayList<HomeSearch_Book_Data> books = new ArrayList<HomeSearch_Book_Data>();

        // <editor-fold desc="Query1 for all available">
        String sqlString1 = FormatSearchBookSqlString_Avail(str);

        ResultSet rs1 = DatabaseAccessAPI.MyExecuteQuery(sqlString1);
        if (rs1!=null)
        {				
            try {
                while (rs1.next()) {
                    // Populate field variables

                    String Isbn = rs1.getString("Isbn");
                    String Title = rs1.getString("Title");
                    String Author_List = rs1.getString("Author_List");
                    Boolean Availability_Flag = true;

                    HomeSearch_Book_Data b_temp = new HomeSearch_Book_Data(Isbn, Title, Author_List, Availability_Flag);

                    books.add(b_temp);					
                }
            }
            catch(SQLException ex) 
            {
                System.out.println("Error in connection: " + ex.getMessage());
            }	
        }
        // </editor-fold>

        // <editor-fold desc="Query2 for all available">
        String sqlString2 = FormatSearchBookSqlString_UnAvail(str);

        ResultSet rs2 = DatabaseAccessAPI.MyExecuteQuery(sqlString2);
        if (rs2!=null)
        {				
            try {
                while (rs2.next()) {
                    // Populate field variables

                    String Isbn = rs2.getString("Isbn");
                    String Title = rs2.getString("Title");
                    String Author_List = rs2.getString("Author_List");
                    Boolean Availability_Flag = false;

                    HomeSearch_Book_Data b_temp = new HomeSearch_Book_Data(Isbn, Title, Author_List, Availability_Flag);

                    books.add(b_temp);					
                }
            }
            catch(SQLException ex) 
            {
                    System.out.println("Error in connection: " + ex.getMessage());
            }	
        }
        // </editor-fold>

        return books;
    }
}
