<?php

include 'header.php';
include 'jsonUtil.php';
include 'connect_mysql_db.php';

#$json_string = file_get_contents("php://input");
#$obj = json_decode($json_string);
#$username = $obj -> username;
#$password = $obj -> password;

$userId = $_GET['id'];
$password = $_GET['password'];
$password1 = $_GET['password1'];
//echo "$password1";
// 从表中提取信息的sql语句

$strsql = "select * from users where id=" . "\"" . $userId . "\" and password=" . "\"" . $password . "\"";
//print $strsql;
// 执行sql查询

$result = mysql_db_query($mysql_database, $strsql, $conn);

// 获取查询结果

$row = mysql_fetch_row($result);
$id = $row[0];
print $id;
if ($row) {
	$strsql = "update users set password="."'".$password1."'"." where id=$id";
	//echo $strsql;
	mysql_select_db($mysql_database, $conn);
	
	if (mysql_query($strsql)) {
		echo "0";
	} else {
		echo "1";
	}

} else {
	echo "1";
}

// 释放资源

mysql_free_result($result);

// 关闭连接

mysql_close();
?>