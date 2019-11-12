/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library_access_api;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Max
 */
public class UpdateFinesAPI {
    private static String FormatUpdateFines_Returned_SqlString(){
        String sqlString = "SELECT Loan_id, Due_date, Date_in FROM BOOK_LOANS WHERE Date_in IS NOT NULL;";
        return sqlString;
    }
    
    public static ArrayList<UpdateFines_Data> UpdateFines_Returned_Data_Done(){
        ArrayList<UpdateFines_Data> uf_data_r = new ArrayList<UpdateFines_Data>();
        
        String sqlString = FormatUpdateFines_Returned_SqlString();
        
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        if (rs!=null){
            try{
                while (rs.next()){
                    // Populate field variables

                    String Loan_id = rs.getString("Loan_id");
                    java.util.Date Due_date = rs.getDate("Due_date");
                    java.util.Date Date_in = rs.getDate("Date_in");
                    
                    long diff = Date_in.getTime() - Due_date.getTime();
                    long days_diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    
                    double FineAmt = days_diff*.25;
                    
                    UpdateFines_Data uf_temp = new UpdateFines_Data(Loan_id, FineAmt);

                    uf_data_r.add(uf_temp);
                }
            }
            catch(SQLException ex){
                System.out.println("Error in connection: " + ex.getMessage());
            }
        }
        return uf_data_r;
    }
    
    private static String FormatUpdateFines_NotReturned_SqlString(){
        String sqlString = "SELECT Loan_id, Due_date, Date_in FROM BOOK_LOANS WHERE Date_in IS NULL;";
        return sqlString;
    }
    
    public static ArrayList<UpdateFines_Data> UpdateFines_NotReturned_Data_Done(){
        ArrayList<UpdateFines_Data> uf_data_nr = new ArrayList<UpdateFines_Data>();
        
        String sqlString = FormatUpdateFines_NotReturned_SqlString();
        
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        if (rs!=null){
            try{
                while (rs.next()){
                    // Populate field variables

                    String Loan_id = rs.getString("Loan_id");
                    java.util.Date Due_date = rs.getDate("Due_date");
                    
                    java.util.Date Today_date = new java.util.Date();
                    
                    long diff = Today_date.getTime() - Due_date.getTime();
                    long days_diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    
                    //System.out.println("Days difference is: " + days_diff);
                    double FineAmt = days_diff*.25;
                    
                    UpdateFines_Data uf_temp = new UpdateFines_Data(Loan_id, FineAmt);

                    uf_data_nr.add(uf_temp);
                }
            }
            catch(SQLException ex){
                System.out.println("Error in connection: " + ex.getMessage());
            }
        }
        return uf_data_nr;
    }
    
    public static ArrayList<UpdateFines_Data> UpdateFines_Total_Data_Done(){
        ArrayList<UpdateFines_Data> uf_data = new ArrayList<UpdateFines_Data>();
        
        ArrayList<UpdateFines_Data> uf_data_r = UpdateFines_Returned_Data_Done();
        ArrayList<UpdateFines_Data> uf_data_nr = UpdateFines_NotReturned_Data_Done();
        
        for(int i = 0; i < uf_data_r.size(); i++ ){
            uf_data.add(uf_data_r.get(i));
        }
        
        for(int i = 0; i < uf_data_nr.size(); i++ ){
            uf_data.add(uf_data_nr.get(i));
        }
        
        return uf_data;
    }
    
    public static String FormatUpdateFineConditionSqlStringRow_Exists_For_LoanID(String Loan_id){
        String sqlString = "SELECT * FROM FINES WHERE Loan_id = '"+Loan_id+"';";
        return sqlString;
    }
    
    public static boolean get_UpdateFine_Row_Exists_For_LoanID_Condition(String Loan_id){
        boolean row_Exists_For_LoanID_Condition = false;
        
        String sqlString = FormatUpdateFineConditionSqlStringRow_Exists_For_LoanID(Loan_id);
        
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        
        try {
            if(rs.next()){
                row_Exists_For_LoanID_Condition = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        return row_Exists_For_LoanID_Condition;      
    }
    
    public static String FormatUpdateFinConditionSqlStringNot_Paid(String Loan_id){
        String sqlString = "SELECT * FROM FINES WHERE Loan_id = '"+Loan_id+"' AND Paid = false;";
        return sqlString;
    }
    
    public static boolean get_UpdateFine_Not_Paid_Condition(String Loan_id){
        boolean Not_Paid_Condition = false;
        
        String sqlString = FormatUpdateFinConditionSqlStringNot_Paid(Loan_id);
        
        ResultSet rs = DatabaseAccessAPI.MyExecuteQuery(sqlString);
        
        try {
            if(rs.next()){
                Not_Paid_Condition = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        return Not_Paid_Condition;
    }
    
    public static String FormatUpdateFines_UpdateTuple(String Loan_id, double FineAmt){
        String sqlString = 
            "UPDATE FINES\n" +
            "SET Fine_amt = "+String.format( "%.2f", FineAmt )+"\n" +
            "WHERE Loan_id = '"+Loan_id+"';";
        
        //System.out.println(sqlString);
        return sqlString;
    }
    
    public static void UpdateFines_UpdateTuple(String Loan_id, double FineAmt){
        String sqlString = FormatUpdateFines_UpdateTuple(Loan_id, FineAmt);
        DatabaseAccessAPI.MyExecuteStmt(sqlString);
    }
    
    public static String FormatUpdateFines_CreateTuple(String Loan_id, double FineAmt){
       String sqlString =
            "INSERT INTO FINES (Loan_id, Fine_amt, Paid)\n" +
            "VALUES ('"+Loan_id+"', "+String.format( "%.2f", FineAmt )+", false);";
        
        //System.out.println(sqlString);
        return sqlString;
    }
    
    public static void UpdateFines_CreateTuple(String Loan_id, double FineAmt){
        String sqlString = FormatUpdateFines_CreateTuple(Loan_id, FineAmt);
        DatabaseAccessAPI.MyExecuteStmt(sqlString);
    }
    
    public static void UpdateFines_Done(){
        ArrayList<UpdateFines_Data> uf_data = UpdateFines_Total_Data_Done();
        
        for(int i = 0; i < uf_data.size(); i++){
            String Loan_id = uf_data.get(i)._Loan_id;
            double FineAmt = uf_data.get(i)._FineAmt;
            
            //System.out.println("uf:" + i + " LoanID: " + Loan_id + " FineAMT: " + FineAmt);
            
            if(FineAmt <= 0){
            //if(false){
                //do nothing
            }
            else{
                if(get_UpdateFine_Row_Exists_For_LoanID_Condition(Loan_id)){
                    if(get_UpdateFine_Not_Paid_Condition(Loan_id)){
                        //System.out.println("Updated LoanID: " + Loan_id);
                        UpdateFines_UpdateTuple(Loan_id, FineAmt);
                    }
                    else{
                        //do nothing
                    }
                }
                else{
                    UpdateFines_CreateTuple(Loan_id, FineAmt);
                    //System.out.println("Created LoanID: " + Loan_id);
                }
            }
        }
    }
    
}
