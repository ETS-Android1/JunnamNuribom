<?php 

	$con = mysql_connect("localhost", "rkskekfk2020", "Tjdgkr23!");
	
	if(!$con) {
		die('Could not connect : ' .mysql_error());
		
		
	}

	
	mysql_select_db("rkskekfk2020_project", $con);
	
	mysql_query("set names utf-8");
	
	mysql_query('set session character_set_connection=utf8;');
	mysql_query('set session character_set_results=utf8;');
	mysql_query('set session character_set_client=utf8;');
	
	$sql = "INSERT INTO member (member_id, member_pass, member_name) VALUES ('$_GET[member_id]' , '$_GET[member_pass]', '$_GET[member_name]')";
	
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
	
	mysql_close($con);

?>