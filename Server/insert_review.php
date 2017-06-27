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

$sql = "INSERT INTO review (type, type_idx, content, member_id, rate, type_name, name) VALUES ('$_GET[type]' , '$_GET[type_idx]', '$_GET[content]', '$_GET[member_id]', '$_GET[rate]', '$_GET[type_name]', '$_GET[name]')";

if(!mysql_query($sql, $con)) {
	die('Error during Query:' . mysql_error());
	echo "{\"result\":[";
	echo "{\"isReview\":\"false\"}";
	echo "]}";


} else {
	
	$sql2 = "SELECT rate FROM review WHERE type = $_GET[type] AND type_idx = $_GET[type_idx]";
	
	if(mysql_query($sql2, $con)) {
		$rate = array();
		$sum_rate = 0.0;
		$result = mysql_query($sql2, $con);
		while($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
			$rate[] = $row["rate"];
		}
		
		for($i = 0; $i<count($rate); $i++) {
			$sum_rate = $sum_rate + $rate[$i];
		}
		
		$sum_rate = $sum_rate / count($rate);
		$sql3 = null;
		switch ($_GET[type]) {
			case 1:
				$sql3 = "UPDATE hospital SET rate = $sum_rate WHERE idx = $_GET[type_idx]";
				break;
			case 2:
				$sql3 = "UPDATE center SET rate = $sum_rate WHERE idx = $_GET[type_idx]";
				break;
			case 3:
				$sql3 = "UPDATE emergency SET rate = $sum_rate WHERE idx = $_GET[type_idx]";
				break;
			case 4:
				$sql3 = "UPDATE dentist SET rate = $sum_rate WHERE idx = $_GET[type_idx]";
				break;
		}
		
		if(!mysql_query($sql3, $con)) {
			die('Error during Query:' . mysql_error());
			echo "{\"result\":[";
			echo "{\"isReview\":\"false\"}";
			echo "]}";
				
		}else {
			echo "{\"result\":[";
			echo "{\"isReview\":\"true\",\"rate\":\"$sum_rate\"}";
			echo "]}";
		}
		
	}
	
	
}

mysql_close($con);


?>