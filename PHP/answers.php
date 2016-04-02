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
			
			//echo $Username;
			
			$WorksheetID2 = $WorksheetID;
			
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
				$data['UserID'][$index] = $row['UserID'];

				
		}
		
		
		if( $UserID==null)
		{
			
			//echo "<br /> no class";
		}
		else
		{
			//echo $UserID;
		}



			$query = sprintf("select Distinct Question, Answer, Student_Answer  from Worksheet_Questions join Student_Answer on Worksheet_Questions.WorksheetID=Student_Answer.WorksheetID where Worksheet_Questions.Question_Num = ? And Worksheet_Questions.WorksheetID=? And UserID=? And Student_Answer.Question_Num = ?");
			$params1 = array($Question_Num, $WorksheetID, $UserID, $Question_Num);
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
				$data['Answer'][$index] = $row['Answer'];
				$data['Student_Answer'][$index] = $row['Student_Answer'];
				

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
		