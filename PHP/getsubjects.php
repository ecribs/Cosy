<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$username = $_POST["username"];
	
	//echo $username;

	$query = sprintf("SELECT ClassID from Users where Username = ?");
	$params1 = array( $username);
    $stmt = sqlsrv_query($conn, $query, $params1);
	
	
	
	    if ($stmt === false) 
	{
		//echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
		
		while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC))
		{
				$ClassID = ($row['ClassID']);
				
		}
		
		if( $ClassID==null)
		{
			
			//echo "<br /> no class";
		}
		else
		{
			//echo $ClassID;
		}
		
		
		
			$query = sprintf("select SubjectName, SubjectID from subjects where ClassID = ?");
			$params1 = array( $ClassID);
			$stmt = sqlsrv_query($conn, $query, $params1);
			print_r($row);
			
	if ($stmt === false) 
	{
		echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
				$subjectname = array();
				$subjectid = array();
			   $index = 0;
			   $data = [];
			   while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC))
			  {
				//print_r($row);
				$data['SubjectName'][$index] = $row['SubjectName'];
				$data['SubjectID'][$index] = $row['SubjectID'];

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
		