<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
			$WorksheetID = $_POST["WorksheetID"];
			$Question_Num = $_POST["QuestionNum"];



			$query = sprintf("select Question from Worksheet_Questions where WorksheetID = ? and Question_Num = ?");
			$params1 = array($WorksheetID,$Question_Num);
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
				$data['Question'][$index] = $row['Question'];

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
		