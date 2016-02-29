<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$Subject = $_POST["SubjectID"];
	
	
		
		
		
			$query = sprintf("Select TopicName, TopicID from Topic join Subjects on Subjects.SubjectID = Topic.subjectID WHERE Subjects.SubjectID = ?");
			$params1 = array($Subject);
			$stmt = sqlsrv_query($conn, $query, $params1);
			print_r($row);
			
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
				$data['TopicName'][$index] = $row['TopicName'];
				$data['TopicID'][$index] = $row['TopicID'];

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
		