<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$WorksheetName = $_POST["WorksheetName"];
	$W_Type = $_POST["W_Type"];
	$TopicID = $_POST["TopicID"];
	$Num_Q = $_POST["Num_Q"];
	$W_Date = $_POST["W_Date"];
		
		
		
	$query = sprintf("insert into Worksheet values(?,?,?,?,?)");
	$params1 = array($WorksheetName, $TopicID, $Num_Q,$W_Type, $W_Date );
	$stmt = sqlsrv_query($conn, $query, $params1);
			
	if ($stmt === false) 
	{
		//echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }

			$query = sprintf("select WorksheetID from Worksheet where WorksheetName=?");
			$params1 = array($WorksheetName);
			$stmt = sqlsrv_query($conn, $query, $params1);
	
			//echo json_encode ("success");
			
			if ($stmt === false) 
	{
		echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
				$TopicName = array();
			   $index = 0;
			   $data = [];
			   while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC))
			  {
				//print_r($row);
				$data['WorksheetID'][$index] = $row['WorksheetID'];

				//echo "in the loop";

				$index++;

				}
	   
			echo json_encode($data);
		
		}
		
	
	
	
else
	{
     echo "Connection could not be established.<br />";
     die( print_r( sqlsrv_errors(), true));

	}
	
	
	
	?>
		