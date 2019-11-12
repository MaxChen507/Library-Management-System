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
public class ResolveFines_Data {
    public String _Card_id;
    public String _SumFineAmt;

    public ResolveFines_Data(String Card_id, String SumFineAmt)
    {
        _Card_id = Card_id;
        _SumFineAmt = SumFineAmt;
    }
}
