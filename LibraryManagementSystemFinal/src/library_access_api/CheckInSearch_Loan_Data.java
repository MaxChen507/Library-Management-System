/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library_access_api;

/**
 *
 * @author Max
 */
public class CheckInSearch_Loan_Data {
    public String _Bname;
    public String _LoanId; 
    public String _ISBN;
    public String _CardId;
    public String _DateOut;
    public String _DueDate;
    public String _DateIn;

    public CheckInSearch_Loan_Data(String Bname, String LoanId, String ISBN, String CardId, String DateOut, String DueDate, String DateIn)
    {
        _Bname = Bname;
        _LoanId = LoanId;
        _ISBN = ISBN;
        _CardId = CardId;
        _DateOut = DateOut;
        _DueDate = DueDate;
        _DateIn = DateIn;
    }
}
