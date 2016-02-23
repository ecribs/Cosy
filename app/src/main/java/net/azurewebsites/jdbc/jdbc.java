package net.azurewebsites.jdbc;


public class jdbc
{
    public static final String connectionString = "jdbc:mysql://cosy.database.windows.net/cosy";
    public static final String db_user = "eocribin";
    public static final String db_pass = "Ecribin1";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String Table_NAME = "Books";


}

/* 02-21 10:55:08.940    9513-9561/? E/dalvikvm﹕ Could not find class 'javax.naming.StringRefAddr', referenced from method com.mysql.jdbc.ConnectionPropertiesImpl$ConnectionProperty.storeTo

02-21 11:23:28.940  19790-19877/net.azurewebsites.cosy W/dalvikvm﹕ VFY: unable to find class referenced in signature (Ljavax/naming/Reference;)
02-21 11:23:28.940  19790-19877/net.azurewebsites.cosy I/dalvikvm﹕ Could not find method javax.naming.Reference.get, referenced from method com.mysql.jdbc.ConnectionPropertiesImpl$ConnectionProperty.initializeFrom
02-21 11:23:28.940  19790-19877/net.azurewebsites.cosy W/dalvikvm﹕ VFY: unable to resolve virtual method 14328: Ljavax/naming/Reference;.get (Ljava/lang/String;)Ljavax/naming/RefAddr;
02-21 11:23:28.940  19790-19877/net.azurewebsites.cosy D/dalvikvm﹕ VFY: replacing opcode 0x6e at 0x0004
02-21 11:23:28.944  19790-19877/net.azurewebsites.cosy W/dalvikvm﹕ VFY: unable to find class referenced in signature (Ljavax/naming/Reference;)
02-21 11:23:28.948  19790-19877/net.azurewebsites.cosy E/dalvikvm﹕ Could not find class 'javax.naming.StringRefAddr', referenced from method com.mysql.jdbc.ConnectionPropertiesImpl$ConnectionProperty.storeTo
02-21 11:23:28.948  19790-19877/net.azurewebsites.cosy W/dalvikvm﹕ VFY: unable to resolve new-instance 1453 (Ljavax/naming/StringRefAddr;) in Lcom/mysql/jdbc/ConnectionPropertiesImpl$ConnectionProperty;
02-21 11:23:28.948  19790-19877/net.azurewebsites.cosy D/dalvikvm﹕ VFY: replacing opcode 0x22 at 0x0006
02-21 11:23:28.952  19790-19877/net.azurewebsites.cosy D/dalvikvm﹕ DexOpt: unable to opt direct call 0x37fa at 0x14 in Lcom/mysql/jdbc/ConnectionPropertiesImpl$ConnectionProperty;.storeTo


 jdbc:sqlserver://cosy.database.windows.net:1433;database=cosy;user=eocribin@cosy;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;*/