package net.azurewebsites.net.azurewebsites.helper;

import android.util.Log;

import net.azurewebsites.jdbc.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Owner on 2/20/2016.
 */
public class BookHelper
{
    private Connection conn;
    private String TAG = BookHelper.class.getSimpleName();


    public BookHelper()
    {
        conn = null;

        try
        {
            Log.d(TAG,"attempt to connect");

            Class.forName(jdbc.DRIVER);
            conn = DriverManager.getConnection(jdbc.connectionString, jdbc.db_user, jdbc.db_pass);

        }
        catch (SQLException e)
        {
            Log.d(TAG,"server broke");

            Log.e(TAG, e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.d(TAG,"connected");

            Log.e(TAG, e.getMessage());
        }
    }





    public ResultSet getAllDetails()
    {

        Log.d(TAG,"starting query");
        String query = "select Bookname, SubjectName from books join Subjects on Subjects.SubjectID = Books.subjectID where classID = '1a'";
        ResultSet temp = null;
        try
        {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            temp = rs;

        }
        catch(SQLException c)
        {
            Log.e(TAG, c.getMessage());

        }

        return temp;



    }



}
