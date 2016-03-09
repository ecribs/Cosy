package net.azurewebsites.cosy;

import android.content.Context;
import android.content.SharedPreferences;


public class UserLocalStore
{
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;



    public UserLocalStore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("username", user.username);
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.putString("ClassID", user.ClassID);
        userLocalDatabaseEditor.putString("Role", user.Role);

        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData()
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false)
        {
            return null;
        }

        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String ClassID = userLocalDatabase.getString("ClassID", "");
        String Role = userLocalDatabase.getString("Role", "");



        User user = new User(username, password, ClassID, Role);
        return user;
    }

}