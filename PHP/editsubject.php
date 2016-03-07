<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$username = $_POST["username"];
	$Subject = $_POST["Subject"];
	$SubjectName = $_POST["SubjectName"];
	
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
		
		
		
			$query = sprintf("UPDATE Subjects SET SubjectName=? WHERE SubjectName=? AND ClassID = ?");
			$params1 = array($Subject, $SubjectName, $ClassID);
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
		