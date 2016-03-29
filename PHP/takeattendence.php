<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{

			$Username = $_POST["Username"];
			
			
			$WorksheetID2 = $WorksheetID;
			
				$query = sprintf("SELECT UserID from Users where Username = ?");
				$params1 = array( $Username);
				$stmt = sqlsrv_query($conn, $query, $params1);
	
	
	
	    if ($stmt === false) 
	{
		//echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
		
		while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC))
		{
				$UserID = ($row['UserID']);
				
		}
		
	



			$query = sprintf("insert into Attendence values(GetDate(), ?)");

			$params1 = array($UserID);
			$stmt = sqlsrv_query($conn, $query, $params1);
			//print_r($row);
			
	if ($stmt === false) 
	{
		echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
else
{
			
			echo json_encode("success");
}
		
		
}
	
	
else
	{
     echo "Connection could not be established.<br />";
     die( print_r( sqlsrv_errors(), true));

	}
	
	
	
	?>
		