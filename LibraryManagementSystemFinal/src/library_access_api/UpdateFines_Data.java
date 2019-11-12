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
public class UpdateFines_Data {
    public String _Loan_id;
    public double _FineAmt;

    public UpdateFines_Data(String Loan_id, double FineAmt)
    {
        _Loan_id = Loan_id;
        _FineAmt = FineAmt;
    }
}
