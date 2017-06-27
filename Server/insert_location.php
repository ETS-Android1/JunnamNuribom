<?php

header("Content-Type:text/html;charset=utf-8");

$con = mysql_connect("localhost", "rkskekfk2020", "Tjdgkr23!");

if(!$con){
	die('Could not connect:'.mysql_error());
}

mysql_select_db("rkskekfk2020_project",$con);

mysql_query("set names utf-8");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

$table = $_GET['table'];

$sql="UPDATE $table SET lng = $_GET[lng] , lat = $_GET[lat] WHERE idx = $_GET[idx]";

if(!mysql_query($sql, $con)) {
	die('Error during Query:' . mysql_error());
	echo "{\"result\":[";
	echo "{\"isJoin\":\"false\"}";
	echo "]}";
	
	
} else {
	echo "{\"result\":[";
	echo "{\"isJoin\":\"true\"}";
	echo "]}";
}

//$result = mysql_query($sql, $con);
//$total_record = mysql_num_rows($result);
//$row = array();

//if(mysql_query($sql, $con)){

// 	$row = array();
// 	mysql_data_seek($result, 0);
// 	$row = mysql_fetch_array($result);

	
// 	echo "\"member_idx\":\"$row[member_idx]\",";
// 	echo "\"member_device_id\":\"$row[member_device_id]\",";
// 	echo "\"member_name\":\"$row[member_name]\",";
// 	echo "\"member_email\":\"$row[member_email]\",";
// 	echo "\"member_pass\":\"$row[member_pass]\",";
// 	echo "\"member_point\":\"$row[member_point]\",";
// 	echo "\"member_rank\":\"$row[member_rank]\"}";
 

// }else{

	

// }

//echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

// if(!mysql_query($sql, $con)){
// 	die('Error: '.mysql_error());
// 	echo "{'false'}";
// }

// echo "{'true'}";

// echo "]}";

mysql_close($con)
?>