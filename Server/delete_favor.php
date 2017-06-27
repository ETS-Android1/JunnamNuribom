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

$sql = "DELETE FROM favor WHERE type=$_GET[type] AND type_idx=$_GET[type_idx] AND member_id=$_GET[member_id]";

if(!mysql_query($sql, $con)) {
	die('Error during Query:' . mysql_error());
	echo "{\"result\":[";
	echo "{\"isFavor\":\"false\"}";
	echo "]}";


} else {
	echo "{\"result\":[";
	echo "{\"isFavor\":\"true\"}";
	echo "]}";
}

mysql_close($con);


?>