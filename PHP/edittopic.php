<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$TopicID = $_POST["TopicID"];
	$Topic = $_POST["Topic"];
	$Topic1 = $_POST["Topic1"];

		
			$query = sprintf("UPDATE Topic SET TopicName=? WHERE TopicID=?");
			$params1 = array($Topic1, $TopicID);
			$stmt = sqlsrv_query($conn, $query, $params1);
			
	if ($stmt === false) 
	{
		//echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }

			   
	   
			echo json_encode ("success");
		
		}
	
	
else
	{
     echo "Connection could not be established.<br />";
     die( print_r( sqlsrv_errors(), true));

	}
	
	
	
	?>
		