<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$TopicName = $_POST["TopicName"];
	$SubjectID = $_POST["SubjectID"];
	
	$query = sprintf("insert into Topic values(?,?)");
	$params1 = array($TopicName, $SubjectID);
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
		