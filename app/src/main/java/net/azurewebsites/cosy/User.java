package net.azurewebsites.cosy;


public class User
{
    String username, password, ClassID, Role;


    public User(String username, String password, String ClassID,String Role)
    {
        this.username = username;
        this.password = password;
        this.ClassID = ClassID;
        this.Role = Role;

    }
    public User(String username, String password)
    {
        this( username, password, "", "");
    }





}
