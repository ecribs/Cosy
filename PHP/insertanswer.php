<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
			$WorksheetID = $_POST["WorksheetID"];
			$Question_Num = $_POST["Question_Num"];
			$Username = $_POST["Username"];
			$Answer = $_POST["Answer"];


	
	
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
		
		if( $UserID==null)
		{
			
			echo "<br /> no class";
		}
		else
		{
			echo $UserID;
		}
			



			$query = sprintf("insert into Student_Answer values(?,?,?,?)");
			$params1 = array($WorksheetID, $Question_Num, $UserID, $Answer);
			$stmt = sqlsrv_query($conn, $query, $params1);
			//print_r($row);
			
	if ($stmt === false) 
	{
		echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
				$TopicName = array();
			   $index=0;

			
			echo json_encode ("Success");

			
		
		
}
	
	
else
	{
     echo "Connection could not be established.<br />";
     die( print_r( sqlsrv_errors(), true));

	}
	
	
	
	?>
		