<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$WorksheetID = $_POST["WorksheetID"];
	
	
		
		
		
			$query = sprintf("SELECT Num_Q from Worksheet where WorksheetID =?");
			$params1 = array($WorksheetID);
			$stmt = sqlsrv_query($conn, $query, $params1);
			print_r($row);
			
	if ($stmt === false) 
	{
		echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
			   $index = 0;
			   $data = [];
			   while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC))
			  {
				//print_r($row);
				$data['Num_Q'][$index] = $row['Num_Q'];
				//echo "in the loop";

				$index++;

       }
	   
		
		
		
		
			
			$query = sprintf("SELECT DISTINCT Username FROM users join Student_Answer on users.UserID=Student_Answer.UserID where WorksheetID =?");
			$params1 = array($WorksheetID);
			$stmt = sqlsrv_query($conn, $query, $params1);
			//print_r($row);
			
	if ($stmt === false) 
	{
		echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
				$TopicName = array();
			   $index=0;

			   while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC))
			  {
				//print_r($row);
				$data['Username'][$index] = $row['Username'];
				
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
		