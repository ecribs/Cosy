<?php


$serverName = "cosy.database.windows.net"; 
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);



if( $conn ) 
{

	
	$username = $_POST["username"];
    $password = $_POST["password"];
	//echo($username);


     //echo "Connection established.<br />";

	
	$query = sprintf("SELECT * from Users where Username = ? and Password = ?");
	$params1 = array( $username, $password);
    $stmt = sqlsrv_query($conn, $query, $params1);
    if ($stmt === false) 
	{
        die(print_r(sqlsrv_errors(), true));
    }
	
	    $Users = array();

       while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC))
		{
				$username1 = urlencode($row['Username']);
				$password1= urlencode($row['Password']);
		}
	
		if($username1==null || $password==null)
		{
			//echo"nothing happened";
			
			return null;
		}
		
		else
		{

			$Users["username"] = $username1;
			$Users["password"] = $password1;
			
			echo urldecode(json_encode($Users));		
		}
					
    sqlsrv_free_stmt($stmt);
	
	
	
	sqlsrv_close( $conn );
	
	
 
}

	 
else{
     echo "Connection could not be established.<br />";
     die( print_r( sqlsrv_errors(), true));

	 }
	 
?>