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
public class HomeSearch_Book_Data {
    public String _ISBN;
    public String _Title;
    public String _Authors;
    public Boolean _Availability_Flag; 

    public HomeSearch_Book_Data(String ISBN, String Title, String Authors, Boolean Availability_Flag)
    {
        _ISBN = ISBN;
        _Title = Title;
        _Authors = Authors;
        _Availability_Flag = Availability_Flag;
    }
}
