<?php
$serverName = "cosy.database.windows.net";
$connectionInfo = array( "Database"=>"cosy", "UID"=>"eocribin", "PWD"=>"Ecribin1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
//echo "we have entered the file <br />"; 
if( $conn ) 
{
	//echo "connection works <br />";
	
	
	
	$SubjectID = $_POST["SubjectID"];
	
	
		
		
		
			$query = sprintf("select S.SubjectName, B.BookName from Subjects S join Books B on B.SubjectID =S.subjectID where S.SubjectID =?");
			$params1 = array($SubjectID);
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
				$data['SubjectName'][$index] = $row['SubjectName'];
				$data['BookName'][$index] = $row['BookName'];

				//echo "in the loop";

				$index++;

       }
	   
		
		
		
		
			
			$query = sprintf("select WorksheetID, WorksheetName,Num_Q from Worksheet join Topic on Worksheet.TopicID = Topic.TopicID where SubjectID = ? and W_Date = CONVERT(date, getdate())");
			$params1 = array($SubjectID);
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
				$data['WorksheetID'][$index] = $row['WorksheetID'];
				$data['WorksheetName'][$index] = $row['WorksheetName'];
				$data['Num_Q'][$index] = $row['Num_Q'];

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
		