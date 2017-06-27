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

$type = $_GET[type];

$sql = null;
switch ($type) {
	case 1:
		$sql = "SELECT * FROM hospital WHERE idx = $_GET[idx]";
		break;
	case 2:
		$sql = "SELECT * FROM center WHERE idx = $_GET[idx]";
		break;
	case 3:
		$sql = "SELECT * FROM emergency WHERE idx = $_GET[idx]";
		break;
	case 4:
		$sql = "SELECT * FROM dentist WHERE idx = $_GET[idx]";
		break;
}




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