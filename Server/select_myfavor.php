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
	
	$sql = "SELECT * FROM favor WHERE member_id='$_GET[member_id]' ORDER BY idx DESC";
	
	$menu = array();
	
	echo "{\"result\":";
	
	if(!mysql_query($sql, $con)){
		$check['isSuccess']="false";
		//die('Error: '.mysql_error());
	}else{
		$check['isSuccess']="true";
	}
	
	$result = mysql_query($sql, $con);
	
	
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		$menu[] = $row;
	};
	
	//$menu[0]=$check;
	
	echo json_encode($menu, JSON_UNESCAPED_UNICODE);
	echo "}";
	
	mysql_close($con);
	
	?>