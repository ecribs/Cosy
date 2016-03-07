<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$WorksheetID = $_POST["WorksheetID"];
	$QuestionNum = $_POST["QuestionNum"];
	$Question = $_POST["Question"];
	$Answer = $_POST["Answer"];
		
		
		
	$query = sprintf("insert into Worksheet_Questions values(?,?,?,?)");
	$params1 = array($WorksheetID, $QuestionNum,$Question ,$Answer);
	$stmt = sqlsrv_query($conn, $query, $params1);
			
	if ($stmt === false) 
	{
		//echo "statement fucked";
        die(print_r(sqlsrv_errors(), true));
    }
	   
			echo json_encode ("Success");
		
		}
		
	
	
	
else
	{
     echo "Connection could not be established.<br />";
     die( print_r( sqlsrv_errors(), true));

	}
	
	
	
	?>
		